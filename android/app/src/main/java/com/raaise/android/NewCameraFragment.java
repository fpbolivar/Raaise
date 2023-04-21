package com.raaise.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

public class NewCameraFragment extends Fragment {

    File videoFile;
    private static final int REQUEST_PERMISSIONS_CODE = 100;
    private static final String TAG = "MainActivity";
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaRecorder mediaRecorder;
    private Button recordButton;
    private boolean isRecording = false;
    private Camera camera;
    TextView flipIt;

    private TextureView mPreview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_camera, container, false);
        initComs(view);
        mPreview = (TextureView) view.findViewById(R.id.videoview);
        flipIt = view.findViewById(R.id.flip_it);
        camera = Camera.open();
        camera.unlock();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_CODE);
        } else {
            initRecorder();
        }

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
        });

        flipIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void initComs(View view) {
        surfaceView = view.findViewById(R.id.PreviewVIew);
        recordButton = view.findViewById(R.id.btn_start);
    }
    private void initRecorder() {

        mediaRecorder = new MediaRecorder();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    Log.i("surfaceShow", "surfaceCreated: " + (holder == null));
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    Log.i("surfaceShow", "surfaceCreated: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    private void startRecording() {
        if (mediaRecorder == null) {
            initRecorder();
        }
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        videoFile = new File(path, "helloRecord.mp4");
        try {
            videoFile.createNewFile();
        } catch (IOException e) {
            Log.i("newFrag", "onClick: " + e.getMessage());
            e.printStackTrace();
        }


        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            recordButton.setText("Stop Recording");
        } catch (IOException e) {
            Log.e(TAG, "Failed to start recording.", e);
            Toast.makeText(getContext(), "Failed to start recording.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null && isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            recordButton.setText("Start Recording");
        }
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }
}
