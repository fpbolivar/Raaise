package com.raaise.android.Home.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.Adapters.GetVideosBasedOnAudio;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetVideosBasedOnAudioIdModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Try_AudioFragment extends Fragment {
    private final Handler mHandler = new Handler();
    public String videoPath = "";
    public String videoUri = "";
    public String AudioLink = "";
    public String AudioID = "";
    public String AudioName = "";
    public String songName = "";
    String AudioId;
    RelativeLayout layout_try_audioBtn;
    ImageView BackButton, AudioThumbnail, iv_play_btn;
    View v;
    List<GetVideosBasedOnAudioIdModel.Videos> list;
    TextView AudioNameTextView, use_music_Username, music_duration, btn_Try_Audio_New;
    RecyclerView RecyclerViewOfVideoBasedOnAudio;
    ApiManager apiManager = App.getApiManager();
    GetVideosBasedOnAudio adapter;
    MediaPlayer mMediaPlayer;
    int musicLength;
    SeekBar MusicSeekBar;
    boolean playing = false;
    private AudioManager mAudioManager;

    private long downloadID;
    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadID == id) {
                Dialogs.HideProgressDialog();
                requireActivity().onBackPressed();

            }
        }
    };

    public Try_AudioFragment(String audioId) {
        AudioId = audioId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_try__audio, container, false);


        Initialization();
        ClickListeners();
        HitGetVideoBaseOnAudioAPi();
        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return v;
    }

    private void HitGetVideoBaseOnAudioAPi() {
        GetVideosBasedOnAudioIdModel model = new GetVideosBasedOnAudioIdModel(AudioId);
        Dialogs.createProgressDialog(v.getContext());
        apiManager.GetVideoBaseOnAudio(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<GetVideosBasedOnAudioIdModel>() {
            @Override
            public void onSuccess(GetVideosBasedOnAudioIdModel getVideosBasedOnAudioIdModel) {
                Dialogs.HideProgressDialog();
                try {
                    Glide.with(v).load(Prefs.GetBaseUrl(getContext()) + getVideosBasedOnAudioIdModel.getAudio().getThumbnail()).placeholder(R.drawable.placeholder).into(AudioThumbnail);
                    music_duration.setText(getVideosBasedOnAudioIdModel.getAudio().getAudioTime());
                    AudioNameTextView.setText(getVideosBasedOnAudioIdModel.getAudio().getSongName());
                    use_music_Username.setText(getVideosBasedOnAudioIdModel.getAudio().getArtistName());
                    AudioName = getVideosBasedOnAudioIdModel.getAudio().getSongName();
                    AudioLink = getVideosBasedOnAudioIdModel.getAudio().getAudio();
                    songName = getVideosBasedOnAudioIdModel.getAudio().getSongName();

                    AudioID = getVideosBasedOnAudioIdModel.getAudio().get_id();
                    list.addAll(getVideosBasedOnAudioIdModel.getVideos());
                    adapter.notifyDataSetChanged();
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    Log.i("audioLink", "onSuccess: " + (retriever == null) + AudioLink);
                    String link = Prefs.GetBaseUrl(getContext()) + AudioLink;
                    if (Build.VERSION.SDK_INT >= 14)
                        retriever.setDataSource(link, new HashMap<String, String>());
                    else
                        retriever.setDataSource(link);
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInmillisec = Long.parseLong(time);
                    long duration = timeInmillisec / 1000;
                    long hours = duration / 3600;
                    long minutes = (duration - hours * 3600) / 60;
                    long l = duration - (hours * 3600 + minutes * 60);
                    music_duration.setText(String.format("0:%d", l));
                    MusicSeekBar.setMax((int) l);
                } catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();


                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    private void Initialization() {
        list = new ArrayList<>();
        MusicSeekBar = v.findViewById(R.id.MusicSeekBar);
        iv_play_btn = v.findViewById(R.id.iv_play_btn);
        btn_Try_Audio_New = v.findViewById(R.id.btn_Try_Audio_New);
        RecyclerViewOfVideoBasedOnAudio = v.findViewById(R.id.RecyclerViewOfVideoBasedOnAudio);
        RecyclerViewOfVideoBasedOnAudio.setHasFixedSize(true);
        RecyclerViewOfVideoBasedOnAudio.setLayoutManager(new GridLayoutManager(v.getContext(), 3));
        adapter = new GetVideosBasedOnAudio(v.getContext(), list);
        RecyclerViewOfVideoBasedOnAudio.setAdapter(adapter);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        music_duration = v.findViewById(R.id.music_duration);
        use_music_Username = v.findViewById(R.id.use_music_Username);
        AudioNameTextView = v.findViewById(R.id.AudioNameTextView);
        AudioThumbnail = v.findViewById(R.id.AudioThumbnail);
        BackButton = v.findViewById(R.id.BackInSignUp);
        layout_try_audioBtn = v.findViewById(R.id.layout_audio_btn);
    }

    private void ClickListeners() {
        MusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mMediaPlayer != null && b) {
                    mMediaPlayer.seekTo(i * 1000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .add(R.id.try_audio_container, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
                getActivity().finish();
            }
        });


        layout_try_audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.try_audio_container, new TryAudioCameraFragment(AudioLink, AudioID, AudioName, 1), null)
                        .commit();


            }
        });
        btn_Try_Audio_New.setOnClickListener(view -> {
            if (
                    mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }

            beginDownload(AudioLink);


        });
        iv_play_btn.setOnClickListener(view -> {
            try {
                if (playing) {
                    playing = false;
                    mMediaPlayer.pause();
                    iv_play_btn.setImageDrawable(getResources().getDrawable(R.drawable.playsvg));
                } else {
                    playing = true;
                    iv_play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause_tryaudio));
                    releaseMediaPlayer();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mMediaPlayer != null) {
                                int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                                MusicSeekBar.setProgress(mCurrentPosition);
                                music_duration.setText(String.format("0:%d", mCurrentPosition));
                            }
                            mHandler.postDelayed(this, 1000);
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                                mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(Prefs.GetBaseUrl(getContext()) + AudioLink));
                                mMediaPlayer.start();
                                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                            }
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void beginDownload(String audioLink) {
        try {
            Dialogs.createProgressDialog(getContext());
            DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(Prefs.GetBaseUrl(getContext()) + audioLink));


            String title = URLUtil.guessFileName(audioLink, null, null);
            App.musicTitle = songName;
            App.fromTryAudio = true;

            downloadRequest.setTitle(title);

            downloadRequest.setDescription("Downloading, Please Wait...!!!");

            String cookie = CookieManager.getInstance().getCookie(audioLink);

            downloadRequest.addRequestHeader("cookie", cookie);

            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

            downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);


            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(downloadRequest);
        } catch (Exception e) {
            Dialogs.HideProgressDialog();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void releaseMediaPlayer() {

        if (mMediaPlayer != null) {


            mMediaPlayer.release();


            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {

            Uri selectedImageUri = data.getData();


            String filemanagerstring = selectedImageUri.getPath();


            String selectedImagePath = getPath(selectedImageUri);
            if (selectedImagePath != null) {
                videoPath = selectedImagePath;
                videoUri = data.getData().toString();


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

    private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            iv_play_btn.setImageDrawable(getResources().getDrawable(R.drawable.playsvg));
            MusicSeekBar.setProgress(0);
            releaseMediaPlayer();
        }
    };


    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {


                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {


                releaseMediaPlayer();
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                releaseMediaPlayer();
            }
        }
    }
}