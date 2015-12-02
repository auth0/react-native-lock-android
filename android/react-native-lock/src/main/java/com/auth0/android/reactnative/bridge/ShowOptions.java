/*
 * ShowOptions.java
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

package com.auth0.android.reactnative.bridge;


import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.util.HashMap;
import java.util.Map;

public class ShowOptions {

    private static final String TAG = ShowOptions.class.getName();

    private static final String CLOSABLE_KEY = "closable";
    private static final String AUTH_PARAMS_KEY = "authParams";
    private static final String CONNECTIONS_KEY = "connections";

    private boolean closable;
    private Map<String, Object> authParams;
    private String connectionName;

    public ShowOptions(@Nullable ReadableMap options) {
        if (options == null) {
            return;
        }

        if (options.hasKey(CLOSABLE_KEY)) {
            closable = options.getBoolean(CLOSABLE_KEY);
            Log.d(TAG, CLOSABLE_KEY + closable);
        }

        if (options.hasKey(AUTH_PARAMS_KEY)) {
            authParams = new HashMap<>();
            ReadableMap reactMap = options.getMap(AUTH_PARAMS_KEY);
            ReadableMapKeySetIterator keySet = reactMap.keySetIterator();
            while (keySet.hasNextKey()) {
                String key = keySet.nextKey();
                Object object = null;
                switch (reactMap.getType(key)) {
                    case Array:
                        object = reactMap.getArray(key);
                        break;
                    case Boolean:
                        object = reactMap.getBoolean(key);
                        break;
                    case Map:
                        object = reactMap.getMap(key);
                        break;
                    case Null:
                        object = null;
                        break;
                    case Number:
                        object = reactMap.getDouble(key);
                        break;
                    case String:
                        object = reactMap.getString(key);
                        break;
                    default:
                        Log.e(TAG, "Unknown type: " + reactMap.getType(key) + " for key: " + key);
                }
                authParams.put(key, object);
            }
            Log.d(TAG, AUTH_PARAMS_KEY + authParams);
        }

        // connections
        connectionName = "default";
        if (options.hasKey(CONNECTIONS_KEY)) {
            ReadableArray connections = options.getArray(CONNECTIONS_KEY);
            if (connections.size() != 1) {
                Log.e(TAG, "Only one connection allowed for now...");
            } else {
                connectionName = connections.getString(0);
            }
            Log.d(TAG, CONNECTIONS_KEY + connectionName);
        }
    }

    public boolean isClosable() {
        return closable;
    }

    public Map<String, Object> getAuthParams() {
        return authParams;
    }

    public String getConnectionName() {
        return connectionName;
    }
}
