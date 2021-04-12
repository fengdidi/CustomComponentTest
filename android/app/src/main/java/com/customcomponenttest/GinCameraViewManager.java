package com.customcomponenttest;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.overlay.OverlayLayout;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;

public class GinCameraViewManager extends SimpleViewManager<CameraView> {
    public static final String REACT_CLASS = "GinCamera";

    ReactApplicationContext mCallerContext;

    public static final int COMMAND_TAKE_A_VIDEO = 1;

    private int typeFace = 0;

    CameraView mCameraView;
    private GraphicOverlay mGraphicOverlay;
    FaceGraphic mFaceGraphic;
    FaceDetector detector;

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
        mCameraView = new CameraView(reactContext.getCurrentActivity());
        mCameraView.setLifecycleOwner((ReactActivity) reactContext.getCurrentActivity());
        mCameraView.setMode(Mode.VIDEO);
        mCameraView.setPreview(Preview.GL_SURFACE);
        mCameraView.setFacing(Facing.FRONT);

        mGraphicOverlay = new GraphicOverlay(reactContext.getCurrentActivity());
        //mGraphicOverlay.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        OverlayLayout.LayoutParams params = new OverlayLayout.LayoutParams(-1, -1);
        params.drawOnPreview = true;
        params.drawOnVideoSnapshot = true;
        params.drawOnPictureSnapshot = true;
        mCameraView.addView(mGraphicOverlay, params);



        detector = new FaceDetector.Builder(reactContext)
                .setProminentFaceOnly(true)
                .build();




        mCameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            @WorkerThread
            public void process(@NonNull Frame frame) {
//                long time = frame.getTime();
//                Size size = frame.getSize();
//                int format = frame.getFormat();
//                int userRotation = frame.getRotationToUser();
                int viewRotation = frame.getRotationToView();
                Log.i("viewRotation",String.valueOf(viewRotation));
//                if (frame.getDataClass() == byte[].class) {
//                    byte[] data = frame.getData();
//                    // Process byte array...
//                } else if (frame.getDataClass() == Image.class) {
//                    // Process android.media.Image...
//                }


                com.google.android.gms.vision.Frame frameToDect = new com.google.android.gms.vision.Frame.Builder().
                        setImageData(ByteBuffer.wrap(frame.getData()), frame.getSize().getWidth(), frame.getSize().getHeight(), frame.getFormat())
                        .setRotation(viewRotation/90)
                        .build();



                SparseArray<Face> faces = detector.detect(frameToDect);
                Log.i("sss",faces.toString());

                Log.i("frame width",String.valueOf(frame.getSize().getWidth()));
                Log.i("frame height",String.valueOf(frame.getSize().getHeight()));

                Log.i("camera width",String.valueOf(mCameraView.getWidth()));
                Log.i("camera height",String.valueOf(mCameraView.getHeight()));

                Log.i("mGraphicOverlay width",String.valueOf(mGraphicOverlay.getWidth()));
                Log.i("mGraphicOverlay height",String.valueOf(mGraphicOverlay.getHeight()));


                if(faces.size() > 0){
                    if(mFaceGraphic == null){
                        mFaceGraphic = new FaceGraphic(mGraphicOverlay,typeFace);
                        mGraphicOverlay.add(mFaceGraphic);
                    }

                    Face f = faces.get(faces.keyAt(0));

                    Log.i("face x",String.valueOf(f.getPosition().x));
                    Log.i("face y",String.valueOf(f.getPosition().y));

                    Log.i("face id",String.valueOf(f.getId()));
                    mFaceGraphic.updateFace(f,typeFace);
                }else{
                    if(mFaceGraphic != null){
                        mGraphicOverlay.remove(mFaceGraphic);
                        mFaceGraphic = null;
                    }

                }

            }
        });

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
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"test3.mp4");
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

    @ReactProp(name="overlay")
    public void setOverlay(CameraView cameraView, String overlayType) {
        if("snap".equals(overlayType)){
            typeFace = 3;
        }else if("glasses5".equals(overlayType)){
            typeFace = 7;
        }else{
            typeFace = 0;
        }
    }
}

