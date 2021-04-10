package com.customcomponenttest;

import android.net.Uri;
import android.os.Environment;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.filter.Filters;

import java.io.File;
import java.util.Map;

public class GinCameraViewManager extends SimpleViewManager<CameraView> {
    public static final String REACT_CLASS = "GinCamera";

    ReactApplicationContext mCallerContext;

    public static final int COMMAND_TAKE_A_VIDEO = 1;

    public GinCameraViewManager(ReactApplicationContext reactContext) {
        mCallerContext = reactContext;
    }

    // Component name that will be called from JavaScript
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    // Return the view component instantiated with Activity context
    @Override
    public CameraView createViewInstance(ThemedReactContext reactContext) {
        CameraView mCameraView = new CameraView(reactContext.getCurrentActivity());
        mCameraView.setLifecycleOwner((ReactActivity) reactContext.getCurrentActivity());
        mCameraView.setMode(Mode.VIDEO);
        mCameraView.setPreview(Preview.GL_SURFACE);
        return mCameraView;

    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        // You need to implement this method and return a map with the readable
        // name and constant for each of your commands. The name you specify
        // here is what you'll later use to access it in react-native.
        return MapBuilder.of(
                "takeAVideo",
                COMMAND_TAKE_A_VIDEO
        );
    }

    @Override
    public void receiveCommand(final CameraView root, int commandId, @Nullable ReadableArray args) {
        // This will be called whenever a command is sent from react-native.
        switch (commandId) {
            case COMMAND_TAKE_A_VIDEO:
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"test2.mp4");
                if(file.exists()){
                    file.delete();
                }
                root.takeVideoSnapshot(file,args.getInt(0));
                break;
        }
    }

    @ReactProp(name="filter")
    public void setFilter(CameraView cameraView, String filterType) {
        if("BLACK_AND_WHITE".equals(filterType)){
            cameraView.setFilter(Filters.BLACK_AND_WHITE.newInstance());
        }else if("SEPIA".equals(filterType)){
            cameraView.setFilter(Filters.SEPIA.newInstance());
        }else if("TEMPERATURE".equals(filterType)){
            cameraView.setFilter(Filters.TEMPERATURE.newInstance());
        }else{
            cameraView.setFilter(Filters.NONE.newInstance());
        }
    }
}

