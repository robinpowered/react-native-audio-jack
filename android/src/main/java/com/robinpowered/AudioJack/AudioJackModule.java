package com.robinpowered.AudioJack;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nullable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;

public class AudioJackModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String MODULE_NAME = "AudioJack";
    private static final String AUDIO_CHANGED_NOTIFICATION = "AUDIO_CHANGED_NOTIFICATION";
    private static final String IS_PLUGGED_IN = "isPluggedIn";

    private BroadcastReceiver headsetReceiver;

    public AudioJackModule(final ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private void maybeRegisterReceiver() {
        final ReactApplicationContext reactContext = getReactApplicationContext();

        if (headsetReceiver != null) {
            return;
        }

        headsetReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int pluggedInState = intent.getIntExtra("state", -1);

                WritableNativeMap data = new WritableNativeMap();
                data.putBoolean(IS_PLUGGED_IN, pluggedInState == 1);

                if (reactContext.hasActiveCatalystInstance()) {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(AUDIO_CHANGED_NOTIFICATION,
                            data);
                }
            }
        };

        IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        reactContext.registerReceiver(headsetReceiver, headsetFilter);
    }

    private void maybeUnregisterReceiver() {
        if (headsetReceiver == null) {
            return;
        }
        getReactApplicationContext().unregisterReceiver(headsetReceiver);
        headsetReceiver = null;
    }

    private boolean isHeadsetPluggedIn() {
        AudioManager audioManager = (AudioManager) getReactApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return audioManager.isWiredHeadsetOn();
        } else {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (int i = 0; i < devices.length; i++) {
                AudioDeviceInfo device = devices[i];
                if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public @Nullable Map<String, Object> getConstants() {
        HashMap<String, Object> constants = new HashMap<>();
        constants.put(AUDIO_CHANGED_NOTIFICATION, AUDIO_CHANGED_NOTIFICATION);
        return constants;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void isPluggedIn(final Promise promise) {
        promise.resolve(isHeadsetPluggedIn());
    }

    @Override
    public void initialize() {
        getReactApplicationContext().addLifecycleEventListener(this);
        maybeRegisterReceiver();
    }

    @Override
    public void onHostResume() {
        maybeRegisterReceiver();
    }

    @Override
    public void onHostPause() {
        maybeUnregisterReceiver();
    }

    @Override
    public void onHostDestroy() {
        maybeUnregisterReceiver();
    }
}
