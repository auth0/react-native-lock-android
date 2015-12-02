/*
 * LockReactModule.java
 *
 * Copyright (c) 2015 Auth0 (http://auth0.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.auth0.android.reactnative;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.auth0.android.reactnative.bridge.InitOptions;
import com.auth0.android.reactnative.bridge.ShowOptions;
import com.auth0.android.reactnative.bridge.TokenBridge;
import com.auth0.android.reactnative.bridge.UserProfileBridge;
import com.auth0.core.Token;
import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
import com.auth0.lock.LockActivity;
import com.auth0.lock.LockContext;
import com.auth0.lock.passwordless.LockPasswordlessActivity;
import com.auth0.lock.receiver.AuthenticationReceiver;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.Map;

public class LockReactModule extends ReactContextBaseJavaModule {

    private final static String TAG = LockReactModule.class.getName();

    private final static String REACT_MODULE_NAME = "Auth0LockModule";

    private static final String NATIVE_KEY = "DEFAULT";
    private static final String NATIVE_VALUE = "default";
    private static final String EMAIL_KEY = "EMAIL";
    private static final String EMAIL_VALUE = "email";
    private static final String EMAIL_MAGIC_LINK_KEY = "EMAIL_MAGIC_LINK";
    private static final String EMAIL_MAGIC_LINK_VALUE = "email_magic_link";
    private static final String SMS_KEY = "SMS";
    private static final String SMS_VALUE = "sms";
    private static final String SMS_MAGIC_LINK_KEY = "SMS_MAGIC_LINK";
    private static final String SMS_MAGIC_LINK_VALUE = "sms_magic_link";
    private final LocalBroadcastManager broadcastManager;

    Lock.Builder lockBuilder;

    private Callback authCallback;
    private AuthenticationReceiver authenticationReceiver = new AuthenticationReceiver() {
        @Override
        public void onAuthentication(@NonNull UserProfile profile, @NonNull Token token) {
            Log.d(TAG, "User " + profile.getName() + " with token " + token.getIdToken());
            authCallbackSuccess(profile, token);
        }

        @Override
        protected void onSignUp() {
            final String message = "User signed up";
            Log.i(TAG, message);
            authCallbackError(message);
        }

        @Override
        protected void onCancel() {
            final String message = "User Cancelled";
            Log.i(TAG, message);
            authCallbackError(message);
        }
    };

    public LockReactModule(ReactApplicationContext reactContext) {
        this(reactContext, LocalBroadcastManager.getInstance(reactContext.getApplicationContext()));
    }

    LockReactModule(ReactApplicationContext reactApplicationContext, LocalBroadcastManager localBroadcastManager) {
        super(reactApplicationContext);
        this.broadcastManager = localBroadcastManager;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(NATIVE_KEY, NATIVE_VALUE);
        constants.put(EMAIL_KEY, EMAIL_VALUE);
        constants.put(EMAIL_MAGIC_LINK_KEY, EMAIL_MAGIC_LINK_VALUE);
        constants.put(SMS_KEY, SMS_VALUE);
        constants.put(SMS_MAGIC_LINK_KEY, SMS_MAGIC_LINK_VALUE);
        return constants;
    }

    @Override
    public String getName() {
        return REACT_MODULE_NAME;
    }

    @ReactMethod
    public void init(ReadableMap options) {
        InitOptions initOptions = new InitOptions(options);

        lockBuilder = new Lock.Builder()
                .useWebView(true)
                .clientId(initOptions.getClientId())
                .domainUrl(initOptions.getDomain())
                .configurationUrl(initOptions.getConfigurationDomain());
    }

    @ReactMethod
    public void show(ReadableMap options, Callback callback) {
        Context context = getReactApplicationContext();
        authCallback = callback;
        authenticationReceiver.registerIn(this.broadcastManager);

        ShowOptions showOptions = new ShowOptions(options);

        lockBuilder
                .closable(showOptions.isClosable())
                .authenticationParameters(showOptions.getAuthParams());

        LockContext.configureLock(lockBuilder);

        String connectionName = showOptions.getConnectionName();
        Intent intent = null;

        /*
        {
            "connections": ["sms"],
            "useMagicLink": true
        }
        {
            "connections": ["facebook", "twitter", "Username-Password-Authentication"]
        }
         */
        switch (connectionName) {
            case NATIVE_VALUE:
                intent = new Intent(context, LockActivity.class);
                break;
            case SMS_VALUE:
                intent = new Intent(context, LockPasswordlessActivity.class);
                intent.putExtra(LockPasswordlessActivity.PASSWORDLESS_TYPE_PARAMETER, LockPasswordlessActivity.MODE_SMS_CODE);
                break;
            case SMS_MAGIC_LINK_VALUE:
                intent = new Intent(context, LockPasswordlessActivity.class);
                intent.putExtra(LockPasswordlessActivity.PASSWORDLESS_TYPE_PARAMETER, LockPasswordlessActivity.MODE_SMS_MAGIC_LINK);
                break;
            case EMAIL_VALUE:
                intent = new Intent(context, LockPasswordlessActivity.class);
                intent.putExtra(LockPasswordlessActivity.PASSWORDLESS_TYPE_PARAMETER, LockPasswordlessActivity.MODE_EMAIL_CODE);
                break;
            case EMAIL_MAGIC_LINK_VALUE:
                intent = new Intent(context, LockPasswordlessActivity.class);
                intent.putExtra(LockPasswordlessActivity.PASSWORDLESS_TYPE_PARAMETER, LockPasswordlessActivity.MODE_EMAIL_MAGIC_LINK);
                break;
            default:
        }

        if (intent != null) {
            /*
             * android.util.AndroidRuntimeException:
             * Calling startActivity() from outside of an Activity context requires the
             * FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
             */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            String errMessage = "Unknown connection \"" + connectionName + "\"";
            Log.e(TAG, errMessage);
            authCallbackError(errMessage);
        }
    }

    private boolean invokeAuthCallback(String err, UserProfile profile, Token token) {
        if (authCallback == null) {
            Log.e(TAG, "Invalid/old callback called! err: " + err + " profile: " + profile + " token: " + token);
            return false;
        }

        authenticationReceiver.unregisterFrom(this.broadcastManager);

        UserProfileBridge userProfileBridge = new UserProfileBridge(profile);
        TokenBridge tokenBridge = new TokenBridge(token);

        authCallback.invoke(err, userProfileBridge.toMap(), tokenBridge.toMap());
        authCallback = null;

        return true;
    }

    private boolean authCallbackSuccess(UserProfile profile, Token token) {
        return invokeAuthCallback(null, profile, token);
    }

    private boolean authCallbackError(String errorMessage) {
        return invokeAuthCallback(errorMessage, null, null);
    }
}
