package com.raaise.android.Home.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Select_AudioActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class CameraFragment extends Fragment {
    private CameraSelector cameraSelector;
    private Preview preview;
    private Camera camera;
    public boolean isRecording = false;
    ImageView flashBtn,flashBtn1,cameraFlipBtn;
    private CameraControl cameraControl;
    boolean flashEnabled = false;
    View v;
    PreviewView previewView;
    ImageView GallerySelected;
    ImageView CameraButtonOuterShell,CameraButtonOuterShell1,imageClose;
    RelativeLayout CameraClickButton;
    TextView Timer;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    int PERMISSION_ALL = 1;
    RelativeLayout relativeEye;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
    };
    ProcessCameraProvider cameraProvider;
    int cameraFacing=CameraSelector.LENS_FACING_BACK;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_camera, container, false);
        ((Home) requireActivity()).videoProgressBar.setVisibility(View.GONE);
        Initialization();
        Log.i("Initialization", "onCreateView: " + (cameraProviderFuture == null));
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());


        cameraProviderFuture.addListener(() -> {
            Log.e("cameraProviderFuture","cameraProviderFuture");
            try {
                cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, getExecuter());

        ClickListeners();
//        flashBtn.setOnClickListener(view -> {
//            if (flashEnabled){
//                flashEnabled = false;
//                flashBtn.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.flash_off));
//            } else {
//                flashEnabled = true;
//                flashBtn.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.flash_on));
//            }
//        });

        flashBtn1.setOnClickListener(view -> {
            if (flashEnabled){
                flashEnabled = false;
                flashBtn1.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.flash_off));
            } else {
                flashEnabled = true;
                flashBtn1.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.flash_on));
            }
        });

        cameraFlipBtn.setOnClickListener(view -> {
           flipCamera();
        });

        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(@NonNull ScaleGestureDetector scaleGestureDetector) {
                // Get the camera's current zoom ratio
                float currentZoomRatio = camera.getCameraInfo().getZoomState().getValue().getZoomRatio();

                // Get the pinch gesture's scaling factor
                float delta = scaleGestureDetector.getScaleFactor();

                // Update the camera's zoom ratio. This is an asynchronous operation that returns
                // a ListenableFuture, allowing you to listen to when the operation completes.
                cameraControl.setZoomRatio(currentZoomRatio * delta);

                return true;
            }

            @Override
            public boolean onScaleBegin(@NonNull ScaleGestureDetector scaleGestureDetector) {

                return true;
            }

            @Override
            public void onScaleEnd(@NonNull ScaleGestureDetector scaleGestureDetector) {
            }
        });

        previewView.setOnTouchListener((view, motionEvent) -> {
            view.performClick();
            scaleGestureDetector.onTouchEvent(motionEvent);
            return true;
        });

        return v;
    }


    private void flipCamera() {
        if(cameraFacing==CameraSelector.LENS_FACING_BACK){
            cameraFacing=CameraSelector.LENS_FACING_FRONT;
        }else{
            cameraFacing=CameraSelector.LENS_FACING_BACK;
        }
        try {
            startCameraX(cameraProviderFuture.get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        checkPermissions();
    }

    private void checkPermissions() {
        if (!hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(((Home) requireActivity()), PERMISSIONS, PERMISSION_ALL);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("RestrictedApi")
    private void ClickListeners() {
        GallerySelected.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        });

        CameraClickButton.setOnClickListener(view -> {
            if (!isRecording) {
//                CameraButtonOuterShell.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.WhiteColor), android.graphics.PorterDuff.Mode.SRC_IN);
                try {
                            recordVideo();

                } catch (Exception e){
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else {
                GallerySelected.setVisibility(View.VISIBLE);
                flashBtn1.setVisibility(View.VISIBLE);
                Timer.setVisibility(View.GONE);
                isRecording = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        videoCapture.stopRecording();
                        if (flashEnabled){
                            cameraControl.enableTorch(false);
                        }
                    }
                }).start();

            }
        });
    }

    private void ShowTimer() {
        String FORMAT = "%02d:%02d";
        Timer.setVisibility(View.VISIBLE);
        new CountDownTimer(180000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                Timer.setText("Remaining Time: "+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @SuppressLint("RestrictedApi")
            public void onFinish() {
                Timer.setText("00:00");
                if (isRecording)
                    videoCapture.stopRecording();
                isRecording = false;
            }

        }.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101) {

            Uri selectedImageUri = data.getData();


            String filemanagerstring = selectedImageUri.getPath();


            String selectedImagePath = getPath(selectedImageUri);
            if (selectedImagePath != null) {
                ((Home) requireActivity()).fromGallery = true;
                ((Home) requireActivity()).videoPath = selectedImagePath;
                ((Home) requireActivity()).videoUri = data.getData().toString();
                cameraProvider.unbindAll();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.FragmentContainer, new PlusFragment(selectedImagePath), null)
                        .commit();
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void Initialization() {
        imageClose=v.findViewById(R.id.imageClose);
        GallerySelected = v.findViewById(R.id.GallerySelected);
        CameraButtonOuterShell1=v.findViewById(R.id.CameraButtonOuterShell11);
        CameraButtonOuterShell = v.findViewById(R.id.CameraButtonOuterShell);
        CameraClickButton = v.findViewById(R.id.CameraClickButton);
        previewView = v.findViewById(R.id.PreviewVIew);
        flashBtn = v.findViewById(R.id.flash_button);
        flashBtn1 = v.findViewById(R.id.flash_button1);
        cameraFlipBtn= v.findViewById(R.id.flipCam_button);
        relativeEye=v.findViewById(R.id.relativeEye);


        Timer = v.findViewById(R.id.Timer);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
                    ((Home) requireActivity()).SelectHomeScreen();
                } catch (Exception e){
                    Log.e("PlusFragment", "handleBackButtonClicked: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        try {
//            ((Home) requireActivity()).fragmentManagerHelper.replace(new HomeFragment(), false);
//            ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
//            ((Home) requireActivity()).SelectHomeScreen();
//        } catch (Exception e){
//            Log.e("PlusFragment", "handleBackButtonClicked: " + e.getMessage());
//        }
    }

    private Executor getExecuter() {
        return ContextCompat.getMainExecutor(requireContext());
    }
    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(cameraFacing)
                    .build();

        preview = new Preview.Builder().build();

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        videoCapture = new VideoCapture.Builder()
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .setVideoFrameRate(15)
                .build();

        cameraProvider.unbindAll();

        camera = cameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview, videoCapture);
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraControl = camera.getCameraControl();
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void recordVideo() {
        Log.e("Null","NotNull"+videoCapture);
        if (videoCapture != null) {

            String videoName = "Raaise" + System.currentTimeMillis();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, videoName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

            try {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1002);

                } else {
                    // Set background
                    CameraButtonOuterShell1.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

                    CameraButtonOuterShell.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
            if (flashEnabled){
                cameraControl.enableTorch(true);
            }
                    GallerySelected.setVisibility(View.GONE);
                    ShowTimer();
                    isRecording = true;
                    relativeEye.setVisibility(View.GONE);
                    imageClose.setVisibility(View.GONE);
                    videoCapture.startRecording(
                            new VideoCapture.OutputFileOptions.Builder(
                                    getActivity().getContentResolver(),
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    contentValues
                            ).build(),

                            getActivity().getMainExecutor(),
                            new VideoCapture.OnVideoSavedCallback() {
                                @Override
                                public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                                    Log.i("onVideoSaved", "onVideoSaved: " + outputFileResults.getSavedUri().toString());
                                    try {
                                            getActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .setReorderingAllowed(true)
                                                    .replace(R.id.FragmentContainer, new PlusFragment(String.valueOf(outputFileResults.getSavedUri())), null)
                                                    .commit();

                                    } catch (Exception e){
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.i("debugAudio", "onVideoSavedError: " + e.getMessage());
                                        }
                                }
                                @Override
                                public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                                    Toast.makeText(getActivity(), "Error saving video: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("cameraProvider", "onDetach: " + (cameraProvider == null));
        if (cameraProvider != null){
            cameraProvider.unbindAll();
        }
    }
        }