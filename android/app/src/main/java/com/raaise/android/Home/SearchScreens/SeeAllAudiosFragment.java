package com.raaise.android.Home.SearchScreens;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.SeeAllAudioOfSearchAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.Fragments.TryAudioCameraFragment;
import com.raaise.android.Home.Fragments.TryAudioMergeVideo;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;


public class SeeAllAudiosFragment extends Fragment implements SeeAllAudioOfSearchAdapter.AudioListener {
    View v;
    ImageView BackInSeeAllAudios;
    SearchView SearchViewInSeeAllAudio;
    RecyclerView RecyclerViewInSeeAllAudios;
    SeeAllAudioOfSearchAdapter adapter;
    List<GlobalSearchModel.Data.Audios> list;
    ApiManager apiManager = App.getApiManager();
    String AudioLink, AudioID, AudioName;
    RelativeLayout NoAudioFound;
    String TextToShow;

    public SeeAllAudiosFragment(List<GlobalSearchModel.Data.Audios> list, String TextToShow) {
        this.list = list;
        this.TextToShow = TextToShow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_see_all_audios, container, false);
        Initialization();
        ClickListeners();
        SearchViewInSeeAllAudio.setQueryHint(Html.fromHtml("<font color = #d6d6d6>" + "Search Audio" + "</font>"));
        SearchViewInSeeAllAudio.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HitGlobalSearchApi(newText, "", "", "audio");
                return false;
            }
        });
        return v;
    }

    public void HitGlobalSearchApi(String search, String limit, String page, String type) {
        GlobalSearchModel model = new GlobalSearchModel(search, limit, page, type);
        apiManager.GetGlobalSearch(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<GlobalSearchModel>() {
            @Override
            public void onSuccess(GlobalSearchModel globalSearchModel) {
                if (globalSearchModel.getData().getAudios().size() == 0) {
                    NoAudioFound.setVisibility(View.VISIBLE);
                    RecyclerViewInSeeAllAudios.setVisibility(View.GONE);
                } else {
                    NoAudioFound.setVisibility(View.GONE);
                    RecyclerViewInSeeAllAudios.setVisibility(View.VISIBLE);
                }

                list.clear();
                list.addAll(globalSearchModel.getData().getAudios());
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onError(ServerError serverError) {


            }
        });
    }

    private void Initialization() {
        adapter = new SeeAllAudioOfSearchAdapter(v.getContext(), list, SeeAllAudiosFragment.this);
        BackInSeeAllAudios = v.findViewById(R.id.BackInSeeAllAudios);
        SearchViewInSeeAllAudio = v.findViewById(R.id.SearchViewInSeeAllAudio);
        NoAudioFound = v.findViewById(R.id.NoAudioFound);
        RecyclerViewInSeeAllAudios = v.findViewById(R.id.RecyclerViewInSeeAllAudios);
        RecyclerViewInSeeAllAudios.setLayoutManager(new LinearLayoutManager(v.getContext()));
        RecyclerViewInSeeAllAudios.setHasFixedSize(true);
        RecyclerViewInSeeAllAudios.setAdapter(adapter);

    }

    private void ClickListeners() {
        BackInSeeAllAudios.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void AudioClicked(String AudioLink, String AudioID, String AudioName) {
        this.AudioLink = AudioLink;
        this.AudioID = AudioID;
        this.AudioName = AudioName;
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.FragmentContainer, new TryAudioCameraFragment(AudioLink, AudioID, AudioName, 2))
                .commit();

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
}