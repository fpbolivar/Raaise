package com.raaise.android.Home.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.SearchScreenAudioListAdapter;
import com.raaise.android.Adapters.SearchScreenDummyVideoListAdapter;
import com.raaise.android.Adapters.SearchScreenUserListAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.SearchScreens.SeeAllAudiosFragment;
import com.raaise.android.Home.SearchScreens.SeeAllPostFragment;
import com.raaise.android.Home.SearchScreens.SeeAllUsersFragment;
import com.raaise.android.R;
import com.raaise.android.Try_AudioActivity;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchScreenAudioListAdapter.AudioListener {
    SearchView searchView;
    RecyclerView userListView, audioListView, videoListView;
    SearchScreenUserListAdapter userListAdapter;
    SearchScreenAudioListAdapter audioListAdapter;
    SearchScreenDummyVideoListAdapter dummyVideoListAdapter;
    List<GlobalSearchModel.Data.Audios> AudiosList;
    List<GlobalSearchModel.Data.Users> UsersList;
    List<GlobalSearchModel.Data.Posts> VideosList;
    ApiManager apiManager = App.getApiManager();
    View v;
    TextView UsersSeeAll, AudioSeeAll, PostsSeeAll;
    SearchView GlobalSearchView;
    RelativeLayout TypeSomethingForSearch, NoResultFound;
    LinearLayout WithoutSearch;
    String AudioLink, AudioID, AudioName;
    LinearLayout ParentOfVideoPart, ParentOfAudioPart, ParentOfUserPart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_search, container, false);
        inItWidgets(v);
        ClickListeners();

        GlobalSearchView.setQueryHint(Html.fromHtml("<font color = #d6d6d6>" + "Search Something" + "</font>"));
        GlobalSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equalsIgnoreCase("")) {
                    TypeSomethingForSearch.setVisibility(View.GONE);
                    WithoutSearch.setVisibility(View.VISIBLE);
                    HitGlobalSearchApi(newText, "", "", "");
                } else {
                    TypeSomethingForSearch.setVisibility(View.VISIBLE);
                    WithoutSearch.setVisibility(View.GONE);
                }
                return false;
            }
        });

        return v;
    }

    private void ClickListeners() {
        UsersSeeAll.setOnClickListener(view -> {
            if (UsersList.size() != 0) {
                ChangeFragment(new SeeAllUsersFragment(UsersList, GlobalSearchView.getQuery().toString()));
            }
        });
        AudioSeeAll.setOnClickListener(view -> {
            if (AudiosList.size() != 0) {
                ChangeFragment(new SeeAllAudiosFragment(AudiosList, GlobalSearchView.getQuery().toString()));
            }
        });
        PostsSeeAll.setOnClickListener(view -> {
            if (VideosList.size() != 0) {
                ChangeFragment(new SeeAllPostFragment(VideosList, GlobalSearchView.getQuery().toString()));
            }
        });
    }

    void ChangeFragment(Fragment frag) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.FragmentContainer, frag)
                .commit();
    }

    public void HitGlobalSearchApi(String search, String limit, String page, String type) {
        GlobalSearchModel model = new GlobalSearchModel(search, limit, page, type);

        apiManager.GetGlobalSearch(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<GlobalSearchModel>() {
            @Override
            public void onSuccess(GlobalSearchModel globalSearchModel) {

                ShowHideData(globalSearchModel.getData().getAudios().size(),
                        globalSearchModel.getData().getPosts().size(),
                        globalSearchModel.getData().getUsers().size());

                AudiosList.clear();
                UsersList.clear();
                VideosList.clear();
                AudiosList.addAll(globalSearchModel.getData().getAudios());
                UsersList.addAll(globalSearchModel.getData().getUsers());
                VideosList.addAll(globalSearchModel.getData().getPosts());
                userListAdapter.notifyDataSetChanged();
                audioListAdapter.notifyDataSetChanged();
                dummyVideoListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {


            }
        });
    }

    private void ShowHideData(int AUDIOS, int POSTS, int USERS) {
        if (AUDIOS == 0) {
            ParentOfAudioPart.setVisibility(View.GONE);
        } else {
            ParentOfAudioPart.setVisibility(View.VISIBLE);
            if (AUDIOS < 4) {
                AudioSeeAll.setVisibility(View.GONE);
            } else {
                AudioSeeAll.setVisibility(View.VISIBLE);
            }
        }
        if (POSTS == 0) {
            ParentOfVideoPart.setVisibility(View.GONE);
        } else {
            ParentOfVideoPart.setVisibility(View.VISIBLE);
            if (POSTS < 4) {
                PostsSeeAll.setVisibility(View.GONE);
            } else {
                PostsSeeAll.setVisibility(View.VISIBLE);
            }
        }
        if (USERS == 0) {
            ParentOfUserPart.setVisibility(View.GONE);
        } else {
            ParentOfUserPart.setVisibility(View.VISIBLE);
            if (USERS < 4) {
                UsersSeeAll.setVisibility(View.GONE);
            } else {
                UsersSeeAll.setVisibility(View.VISIBLE);
            }
        }
        if (USERS == 0 && POSTS == 0 && AUDIOS == 0) {
            NoResultFound.setVisibility(View.VISIBLE);
        } else {
            NoResultFound.setVisibility(View.GONE);
        }
    }

    private void inItWidgets(View view) {
        NoResultFound = view.findViewById(R.id.NoResultFound);
        ParentOfVideoPart = view.findViewById(R.id.ParentOfVideoPart);
        ParentOfAudioPart = view.findViewById(R.id.ParentOfAudioPart);
        ParentOfUserPart = view.findViewById(R.id.ParentOfUserPart);
        AudiosList = new ArrayList<>();
        UsersList = new ArrayList<>();
        VideosList = new ArrayList<>();


        TypeSomethingForSearch = view.findViewById(R.id.TypeSomethingForSearch);

        WithoutSearch = view.findViewById(R.id.WithoutSearch);
        userListView = view.findViewById(R.id.user_list);
        userListView.setHasFixedSize(true);
        userListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        audioListView = view.findViewById(R.id.audio_list);
        audioListView.setHasFixedSize(true);
        audioListView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        videoListView = view.findViewById(R.id.video_list);
        GlobalSearchView = view.findViewById(R.id.GlobalSearchView);
        videoListView.setHasFixedSize(true);
        videoListView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        userListAdapter = new SearchScreenUserListAdapter(v.getContext(), UsersList);
        userListView.setAdapter(userListAdapter);
        audioListAdapter = new SearchScreenAudioListAdapter(v.getContext(), AudiosList, SearchFragment.this);
        audioListView.setAdapter(audioListAdapter);
        dummyVideoListAdapter = new SearchScreenDummyVideoListAdapter(v.getContext(), VideosList);
        videoListView.setAdapter(dummyVideoListAdapter);
        UsersSeeAll = v.findViewById(R.id.UsersSeeAll);
        AudioSeeAll = v.findViewById(R.id.AudioSeeAll);
        PostsSeeAll = v.findViewById(R.id.PostsSeeAll);
        TypeSomethingForSearch.setVisibility(View.VISIBLE);
        WithoutSearch.setVisibility(View.GONE);
    }

    @Override
    public void AudioClicked(String AudioLink, String AudioID, String AudioName) {
        startActivity(new Intent(getActivity(), Try_AudioActivity.class).putExtra("AudioId", AudioID));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {

            Uri selectedImageUri = data.getData();


            String filemanagerstring = selectedImageUri.getPath();


            String selectedImagePath = getPath(selectedImageUri);
            if (selectedImagePath != null) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.FragmentContainer, new TryAudioMergeVideo(selectedImagePath, AudioLink, AudioID, AudioName, 2))
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

    @Override
    public void onResume() {
        super.onResume();
        if (App.fromTryAudio){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.FragmentContainer, new CameraFragment())
                    .commit();
        }
    }
}