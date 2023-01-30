package com.raaise.android.Home.Fragments;

import static android.app.Activity.RESULT_OK;
import static com.arthenica.mobileffmpeg.Config.getPackageName;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.R;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class TryAudioCameraFragment extends Fragment {


    public boolean isRecording = false;
    View v;
    PreviewView previewView;
    MediaProjectionManager mProjectionManager;
    CardView GallerySelected;
    ImageView CameraButtonOuterShell;
    RelativeLayout CameraClickButton;
    File VideoPath;
    Handler h = new Handler();
    TextView Timer;
    String AudioLink, AudioId, AudioName;
    int from;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;

    public TryAudioCameraFragment(String audioLink, String audioId, String audioName, int from) {
        AudioLink = audioLink;
        AudioId = audioId;
        AudioName = audioName;
        this.from = from;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        v = inflater.inflate(R.layout.fragment_try_audio_camera, container, false);
        previewView = v.findViewById(R.id.PreviewVIew);
        Timer = v.findViewById(R.id.Timer);


        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());


        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, getExecuter());

        Initialization();
        ClickListeners();



        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(getActivity(), "Please Allow Permissions", Toast.LENGTH_SHORT).show();
            }

        }
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1001);
        }
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
        }
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1003);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1004);

        }

    }


    @SuppressLint("RestrictedApi")
    private void ClickListeners() {
        GallerySelected.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        });

        CameraClickButton.setOnClickListener(view -> {

            if (!isRecording) {
                GallerySelected.setVisibility(View.GONE);
                isRecording = true;
                ShowTimer();
                CameraButtonOuterShell.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
                recordVideo();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRecording) {
                            videoCapture.stopRecording();
                            GallerySelected.setVisibility(View.VISIBLE);
                        }
                    }
                }, 30000);
            } else {
                Timer.setVisibility(View.GONE);
                isRecording = false;
                videoCapture.stopRecording();
                GallerySelected.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ShowTimer() {
        Timer.setVisibility(View.VISIBLE);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                Timer.setText("Remaining Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Timer.setText("00:00");
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
                if (from == 1) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.try_audio_container, new TryAudioMergeVideo(selectedImagePath, AudioLink, AudioId, AudioName, 1), null)
                            .commit();
                } else if (from == 2) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.FragmentContainer, new TryAudioMergeVideo(selectedImagePath, AudioLink, AudioId, AudioName, 2))
                            .commit();
                }
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


    @SuppressLint("RestrictedApi")
    private void recordVideo() {
        if (videoCapture != null) {
            File movieDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Scriptube" + "/" + App.getContext().getPackageName() + "/" + "Created_Videos");

            if (!movieDir.exists()) {
                boolean yes = movieDir.mkdir();
                if (!yes) {
                    Toast.makeText(v.getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            Date date = new Date();


            VideoPath = new File(movieDir.getAbsolutePath() + "/" + new Date().getTime() + ".mp4");

            if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            videoCapture.startRecording(
                    new VideoCapture.OutputFileOptions.Builder(VideoPath).build(),
                    getExecuter(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            if (from == 1) {
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.try_audio_container, new TryAudioMergeVideo(VideoPath.getPath(), AudioLink, AudioId, AudioName, 1), null)
                                        .commit();
                            } else if (from == 2) {
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack(null)
                                        .replace(R.id.FragmentContainer, new TryAudioMergeVideo(VideoPath.getPath(), AudioLink, AudioId, AudioName, 2))
                                        .commit();
                            }
                        }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            Toast.makeText(v.getContext(), cause + "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(v.getContext(), message + "", Toast.LENGTH_SHORT).show();

                        }
                    }
            );
        }

    }

    private void Initialization() {
        GallerySelected = v.findViewById(R.id.GallerySelected);
        CameraButtonOuterShell = v.findViewById(R.id.CameraButtonOuterShell);
        CameraClickButton = v.findViewById(R.id.CameraClickButton);
    }

    private Executor getExecuter() {
        return ContextCompat.getMainExecutor(getActivity());
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();


        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(30)
                .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, videoCapture);
    }
}