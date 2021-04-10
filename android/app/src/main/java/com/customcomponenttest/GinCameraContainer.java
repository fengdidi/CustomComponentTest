package com.customcomponenttest;

import android.content.Context;
import android.widget.LinearLayout;

import com.facebook.react.ReactActivity;
import com.facebook.react.uimanager.ThemedReactContext;
import com.otaliastudios.cameraview.CameraView;

public class GinCameraContainer extends LinearLayout {

    ThemedReactContext mContext;
    CameraView cameraView;

    public GinCameraContainer(ThemedReactContext context) {
        super(context);
        mContext = context;
    }

    private void init(){
        inflate(mContext,R.layout.gin_camera_container,this);
        cameraView = findViewById(R.id.camera);
        cameraView.setLifecycleOwner((ReactActivity)mContext.getCurrentActivity());
    }
}
