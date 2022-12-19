package com.scripttube.android.ScriptTube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scripttube.android.ScriptTube.Adapters.MusicListAdapter;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.model.MusicData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Select_AudioActivity extends Fragment implements View.OnClickListener, MusicListAdapter.MusicListener {
    ImageView backBtn;
    RecyclerView musicListRV;
    MusicListAdapter adapter;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private long downloadID;
    private SearchView audioSearchET;
    ArrayList<MusicData> allAudioArrayList = new ArrayList<>();
    ArrayList<MusicData> searchedAudioArrayList = new ArrayList<>();

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Dialogs.dismissProgressDialog();
                requireActivity().onBackPressed();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_audio, container, false);
        ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        inItWidgets(view);

        backBtn.setOnClickListener(this);
        musicListRV.setAdapter(adapter);
        getMusicList();
        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        searchAudio();
        return view;
    }

    private void searchAudio() {
        audioSearchET.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchedAudioArrayList.clear();
                searchedAudioArrayList.addAll(allAudioArrayList);
                searchedAudioArrayList.removeIf(data -> (!data.songName.toLowerCase().trim().contains(query.toLowerCase().trim())));
                adapter.updateMusicList(searchedAudioArrayList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    searchedAudioArrayList.clear();
                    adapter.updateMusicList(allAudioArrayList);
                }
                searchedAudioArrayList.clear();
                allAudioArrayList.forEach(data -> {
                    if (data.songName.toLowerCase().trim().contains(newText.toLowerCase().trim())){
                        searchedAudioArrayList.add(data);
                    }
                });
                adapter.updateMusicList(searchedAudioArrayList);
                return false;
            }
        });
    }

    private void inItWidgets(View view) {
        audioSearchET = view.findViewById(R.id.Edt_SearchView);
        audioSearchET.setQueryHint(Html.fromHtml("<font color = #D3D3D3>" + getResources().getString(R.string.search_hint)+ "</font>", Html.FROM_HTML_MODE_COMPACT));
        backBtn = view.findViewById(R.id.BackInSignUp);
        musicListRV = view.findViewById(R.id.music_list);
        musicListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAudioManager = (AudioManager) ((Home) requireActivity()).getSystemService(Context.AUDIO_SERVICE);
        adapter = new MusicListAdapter(getActivity(), this);
    }

    ImageView BackArrow;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BackInSignUp:
                requireActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
        getActivity().unregisterReceiver(onDownloadComplete);
    }

    private void getMusicList() {
        try {
            Dialogs.showProgressDialog(getActivity());
        } catch (Exception e){
        }

        ((Home) requireActivity()).apiManager.getMusicList(Prefs.GetBearerToken(getActivity()), new DataCallback<ArrayList<MusicData>>() {
            @Override
            public void onSuccess(ArrayList<MusicData> musicData) {
                Dialogs.dismissProgressDialog();
                allAudioArrayList.addAll(musicData);
                adapter.updateMusicList(musicData);

            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.dismissProgressDialog();
                Toast.makeText(getActivity(), serverError.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void playMusic(String songUrl) {
        releaseMediaPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(songUrl));
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        }).start();

    }

    @Override
    public void audioSelected(MusicData data) {
        beginDownload(data);
    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseMediaPlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releaseMediaPlayer();
    }

    private void beginDownload(MusicData data){

            try {
                Dialogs.showProgressDialog(getActivity());
                DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(data.audio));

                // Retrieving the Title from given Url
                String title = URLUtil.guessFileName(data.audio, null, null);
                ((Home) requireActivity()).musicTitle = title;
                ((Home) requireActivity()).musicData = new Gson().toJson(data);
                ((Home) requireActivity()).shouldMergeAudio = true;
                // Setting Title to download dialog
                downloadRequest.setTitle(title);
                // Setting Description to dialog
                downloadRequest.setDescription("Downloading, Please Wait...!!!");
                //
                String cookie = CookieManager.getInstance().getCookie(data.audio);
                //
                downloadRequest.addRequestHeader("cookie", cookie);
                // Setting notification dialog accordingly
                downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //
                downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                // Setting Download Manager
                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadID = downloadManager.enqueue(downloadRequest);
            } catch (Exception e){
                Dialogs.dismissProgressDialog();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

    }
}
