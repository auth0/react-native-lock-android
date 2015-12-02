package com.auth0.android.reactnative;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.auth0.lock.LockActivity;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.SimpleMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18, manifest = Config.NONE)
public class LockReactModuleTest {

    private LockReactModule module;

    @Mock
    private ReactApplicationContext reactContext;
    @Mock
    private Callback callback;
    @Mock
    private LocalBroadcastManager broadcastManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        module = new LockReactModule(reactContext, broadcastManager);
    }

    @Test
    public void shouldStartDefaultLockActivity() throws Exception {
        module.init(initOptions("CLIENT_ID", "samples.auth0.com", null));
        SimpleMap map = new SimpleMap();
        module.show(map, callback);
        ArgumentCaptor<Intent> captor = ArgumentCaptor.forClass(Intent.class);
        verify(reactContext).startActivity(captor.capture());
        final Intent intent = captor.getValue();
        final ComponentName component = intent.getComponent();
        assertEquals(component.getClassName(), LockActivity.class.getCanonicalName());
    }

    private ReadableMap initOptions(String clientId, String domain, String configDomain) {
        SimpleMap options = new SimpleMap();
        options.putString("clientId", clientId);
        options.putString("domain", domain);
        options.putString("configurationDomain", configDomain);
        return options;
    }
}