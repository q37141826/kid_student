/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zxing.camera;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.Collection;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

//import com.google.zxing.client.android.PreferencesActivity;

/**
 * A class which deals with reading, parsing, and setting the ico_camera parameters
 * which are used to configure the ico_camera hardware.
 */
final class CameraConfigurationManager {

    //    private static final String TAG = "CameraConfiguration";
    private static final int MIN_PREVIEW_PIXELS = 320 * 240; // small screen
    private static final int MAX_PREVIEW_PIXELS = 800 * 480; // large/HD screen

    private final Context context;
    private Point screenResolution;
    private Point cameraResolution;
    private static String mtyb;

    CameraConfigurationManager(Context context) {
        this.context = context;
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        //手机品牌
        mtyb = Build.BRAND;

    }

    /**
     * Reads, one time, values from the ico_camera that are needed by the app.
     */
    void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        // We're landscape-only, and have apparently seen issues with display
        // thinking it's portrait
        // when waking from sleep. If it's not landscape, assume it's mistaken
        // and reverse them:
        if (width < height) {
//            Log.i(TAG, "Display reports portrait orientation; assuming this is incorrect");
            int temp = width;
            width = height;
            height = temp;
        }
        screenResolution = new Point(height, width);
//        Log.i(TAG, "Screen resolution: " + scree
//
// nResolution);
// FIXME: 2017/11/29  majin  start 适配三星手机扫描二维码变形
        cameraResolution = getCloselyPreSize(width, height, parameters.getSupportedPreviewSizes());
// FIXME: 2017/11/29  majin  end   适配三星手机扫描二维码变形
//        cameraResolution = findBestPreviewSizeValue(parameters, new Point(width, height), false);
//        Log.i(TAG, "Camera resolution: " + cameraResolution);
    }


    void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters == null) {
//            Log.w(TAG, "Device error: no ico_camera parameters are available. Proceeding without configuration.");
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        initializeTorch(parameters, prefs);
        String focusMode = findSettableValue(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_AUTO,
                Camera.Parameters.FOCUS_MODE_MACRO);
        if (focusMode != null) {
            parameters.setFocusMode(focusMode);
        }

        parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);
        camera.startPreview();
    }


    Point getCameraResolution() {
        return cameraResolution;
    }

    Point getScreenResolution() {
        return screenResolution;
    }

    public static final String KEY_FRONT_LIGHT = "preferences_front_light";

    void setTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        doSetTorch(parameters, newSetting);
        camera.setParameters(parameters);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean currentSetting = prefs.getBoolean(KEY_FRONT_LIGHT, false);// PreferencesActivity.
        if (currentSetting != newSetting) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_FRONT_LIGHT, newSetting);// PreferencesActivity.
            editor.commit();
        }
    }

    private static void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs) {
        boolean currentSetting = prefs.getBoolean(KEY_FRONT_LIGHT, false);// PreferencesActivity.
        doSetTorch(parameters, currentSetting);
    }

    private static void doSetTorch(Camera.Parameters parameters, boolean newSetting) {
        String flashMode;
        if (newSetting) {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_TORCH,
                    Camera.Parameters.FLASH_MODE_ON);
        } else {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_OFF);
        }
        if (flashMode != null) {
            parameters.setFlashMode(flashMode);
        }
    }

//    private static Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution, boolean portrait) {
//        Point bestSize = null;
//        int diff = Integer.MAX_VALUE;
//        for (Camera.Size supportedPreviewSize : parameters.getSupportedPreviewSizes()) {
//            int pixels = supportedPreviewSize.height * supportedPreviewSize.width;
//            if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
//                continue;
//            }
//            int supportedWidth = portrait ? supportedPreviewSize.height : supportedPreviewSize.width;
//            int supportedHeight = portrait ? supportedPreviewSize.width : supportedPreviewSize.height;
//            int newDiff = Math.abs(screenResolution.x * supportedHeight - supportedWidth * screenResolution.y);
//            if (newDiff == 0) {
//                bestSize = new Point(supportedWidth, supportedHeight);
//                break;
//            }
//            if (newDiff < diff) {
//                bestSize = new Point(supportedWidth, supportedHeight);
//                diff = newDiff;
//            }
//        }
//        if (bestSize == null) {
//            Camera.Size defaultSize = parameters.getPreviewSize();
//            bestSize = new Point(defaultSize.width, defaultSize.height);
//        }
//        return bestSize;
//    }

    private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
//        Log.i(TAG, "Supported values: " + supportedValues);
        String result = null;
        if (supportedValues != null) {
            for (String desiredValue : desiredValues) {
                if (supportedValues.contains(desiredValue)) {
                    result = desiredValue;
                    break;
                }
            }
        }
//        Log.i(TAG, "Settable value: " + result);
        return result;
    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList   需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    private Point getCloselyPreSize(int surfaceWidth, int surfaceHeight,
                                    List<Camera.Size> preSizeList) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        ReqTmpWidth = surfaceWidth;
        ReqTmpHeight = surfaceHeight;
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList) {
            if ((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)) {
                return new Point(size.width, size.height);
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        cameraResolution = new Point(retSize.width, retSize.height);
        return cameraResolution;
    }
}
