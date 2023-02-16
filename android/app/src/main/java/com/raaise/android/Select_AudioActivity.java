package com.raaise.android;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.raaise.android.Adapters.MusicListAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.Fragments.TryAudioMergeVideo;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.MusicData;

import java.util.ArrayList;

public class Select_AudioActivity extends Fragment implements View.OnClickListener, MusicListAdapter.MusicListener {
    ImageView backBtn;
    RecyclerView musicListRV;
    MusicListAdapter adapter;
    RelativeLayout NoResultFound;
    ArrayList<MusicData> allAudioArrayList = new ArrayList<>();
    ArrayList<MusicData> searchedAudioArrayList = new ArrayList<>();
    boolean isFromTryAudio = false;
    ApiManager apiManager = App.getApiManager();
    String VideoLink;
    View view;
    ImageView BackArrow;
    private MediaPlayer mMediaPlayer;
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
    private SearchView audioSearchET;

    public Select_AudioActivity() {
    }

    public Select_AudioActivity(boolean isFromTryAudio, String VideoLink) {
        this.isFromTryAudio = isFromTryAudio;
        this.VideoLink = VideoLink;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_select_audio, container, false);
        if (!isFromTryAudio) {
            ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        }
        inItWidgets(view);

        backBtn.setOnClickListener(this);
        musicListRV.setAdapter(adapter);
        getMusicList();
        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        searchAudio();
        return view;
    }

    private void searchAudio() {
        audioSearchET.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    searchedAudioArrayList.clear();
                    NoResultFound.setVisibility(View.GONE);
                    adapter.updateMusicList(allAudioArrayList);
                } else {
                    searchedAudioArrayList.clear();
                    allAudioArrayList.forEach(data -> {
                        if (data.songName.toLowerCase().trim().contains(newText.toLowerCase().trim())) {
                            searchedAudioArrayList.add(data);
                            NoResultFound.setVisibility(View.GONE);
                        }
                    });
                    adapter.updateMusicList(searchedAudioArrayList);
                }
                if (searchedAudioArrayList.size() != 0) {
                    NoResultFound.setVisibility(View.GONE);
                } else if (newText.isEmpty()) {
                    NoResultFound.setVisibility(View.GONE);
                } else {
                    NoResultFound.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

    private void inItWidgets(View view) {
        audioSearchET = view.findViewById(R.id.Edt_SearchView);
        audioSearchET.setQueryHint(Html.fromHtml("<font color = #FFFFFF>" + getResources().getString(R.string.search_hint) + "</font>", Html.FROM_HTML_MODE_COMPACT));
        backBtn = view.findViewById(R.id.BackInSignUp);
        NoResultFound = view.findViewById(R.id.NoResultFound);
        musicListRV = view.findViewById(R.id.music_list);
        musicListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        adapter = new MusicListAdapter(getActivity(), this);
        NoResultFound.setVisibility(View.GONE);
    }

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
        if (isFromTryAudio) {
        } else {
            ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
            getActivity().unregisterReceiver(onDownloadComplete);
        }
    }

    private void getMusicList() {
        try {
            Dialogs.createProgressDialog(view.getContext());
        } catch (Exception e) {
        }

        apiManager.getMusicList(Prefs.GetBearerToken(getActivity()), new DataCallback<ArrayList<MusicData>>() {
            @Override
            public void onSuccess(ArrayList<MusicData> musicData) {
                Dialogs.HideProgressDialog();
                allAudioArrayList.addAll(musicData);
                adapter.updateMusicList(musicData);

            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
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
                    MediaPlayer mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(Prefs.GetBaseUrl(getContext()) +songUrl));
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        }).start();

    }

    @Override
    public void audioSelected(MusicData data) {
        if (isFromTryAudio) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.try_audio_container, new TryAudioMergeVideo(VideoLink, data.getAudio(), data.getId(), data.getSongName(), 1), null)
                    .commit();

        } else {
            beginDownload(data);
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
    public void onDestroyView() {
        super.onDestroyView();
        releaseMediaPlayer();
    }    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
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
        releaseMediaPlayer();
    }

    private void beginDownload(MusicData data) {

        try {
            Dialogs.createProgressDialog(view.getContext());
            DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(Prefs.GetBaseUrl(getContext()) + data.audio));


            String title = URLUtil.guessFileName(data.audio, null, null);
            ((Home) requireActivity()).musicTitle = title;
            ((Home) requireActivity()).musicData = new Gson().toJson(data);
            ((Home) requireActivity()).shouldMergeAudio = true;

            downloadRequest.setTitle(title);

            downloadRequest.setDescription("Downloading, Please Wait...!!!");

            String cookie = CookieManager.getInstance().getCookie(data.audio);

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

    }    private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            releaseMediaPlayer();
        }
    };






}
