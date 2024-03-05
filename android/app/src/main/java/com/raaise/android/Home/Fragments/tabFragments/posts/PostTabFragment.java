package com.raaise.android.Home.Fragments.tabFragments.posts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.raaise.android.Adapters.GetPublicUserVideoAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.PublicUserVideoListModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.ArrayList;
import java.util.List;

public class PostTabFragment extends Fragment {

    RecyclerView postRecyclerView;
    ApiManager apiManager = App.getApiManager();
    GetPublicUserVideoAdapter adapter;
    List<PublicUserVideoListModel.Data> list = new ArrayList<>();
    int CountOfVideos = 0, Page = 1;
    String userId;
    RelativeLayout notDataFound;
    View v;

    public PostTabFragment(String userId) {
        this.userId=userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_posts_tab, container, false);
        initWidgets(v);
        getUserVideo();
        return v;
    }

    private void initWidgets(View v) {
        postRecyclerView = v.findViewById(R.id.postRecyclerView);
        notDataFound=v.findViewById(R.id.NoResultFound);
    }
    private void getUserVideo(){
        list.clear();
        Page = 1;
        CountOfVideos = 0;
        GetAllUserVideo(Page);
        postsMethod();
    }

    private void postsMethod(){
        postRecyclerView.setVisibility(View.VISIBLE);
        notDataFound.setVisibility(View.GONE);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new GetPublicUserVideoAdapter(getActivity(), list);
        postRecyclerView.setAdapter(adapter);

        postRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    Page++;
                    GetAllUserVideo(Page);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    void GetAllUserVideo(int page) {
        try {
            String PageNumber = String.valueOf(page);
            PublicUserVideoListModel model = new PublicUserVideoListModel(userId, PageNumber, "6");
            Log.i("modelVideo", "GetAllUserVideo: " + new Gson().toJson(model));
            apiManager.GetPublicUserVideoList(Prefs.GetBearerToken(getActivity()), model, new DataCallback<PublicUserVideoListModel>() {
                @Override
                public void onSuccess(PublicUserVideoListModel publicUserVideoListModel) {
                    list.addAll(publicUserVideoListModel.getData());
                    notDataFound.setVisibility(View.GONE);
                    if (list.size() != 0) {
                        notDataFound.setVisibility(View.GONE);
                    } else {
                        notDataFound.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(ServerError serverError) {
                    notDataFound.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
   
    @Override
    public void onResume() {
        super.onResume();
    }
}