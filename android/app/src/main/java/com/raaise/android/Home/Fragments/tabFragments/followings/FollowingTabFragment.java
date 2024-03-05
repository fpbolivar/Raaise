package com.raaise.android.Home.Fragments.tabFragments.followings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.raaise.android.Adapters.PublicUserFollowersAdapter;
import com.raaise.android.Adapters.PublicUserFollowingAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.PublicUserFollowersModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserFollowingModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.ArrayList;
import java.util.List;

public class FollowingTabFragment extends Fragment {
    RecyclerView followers_list_RV;
    View v;
    RelativeLayout notDataFound;
    List<PublicUserFollowingModel.Data> followingList=new ArrayList<>();
    PublicUserFollowingAdapter adapterFollowing;
    int PageNo=1;
    String Username;

    ApiManager apiManager = App.getApiManager();
    public FollowingTabFragment(String username) {
        this.Username=username;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_following_tab, container, false);
        inItWidgets();
        return v;
    }
    private void inItWidgets(){
        followers_list_RV=v.findViewById(R.id.followers_list_RV);
        notDataFound=v.findViewById(R.id.NoResultFound);
    }
    private void getFollowing(){
        followers_list_RV = v.findViewById(R.id.followers_list_RV);
        followers_list_RV.setHasFixedSize(true);
        followers_list_RV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        notDataFound.setVisibility(View.GONE);
        followingList = new ArrayList<>();
        adapterFollowing = new PublicUserFollowingAdapter(followingList, getActivity());
        followers_list_RV.setAdapter(adapterFollowing);
        followingList.clear();
        PageNo=1;
        DoHitPublicUserFollowingAPi(Username, "",PageNo);
        followers_list_RV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    PageNo++;
                    DoHitPublicUserFollowingAPi(Username, "",PageNo);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void DoHitPublicUserFollowingAPi(String username, String s,int PageNo) {
        PublicUserFollowingModel model = new PublicUserFollowingModel(username, s, "10", String.valueOf(PageNo));
        apiManager.GetPublicUserFollowing(Prefs.GetBearerToken(getActivity()), model, new DataCallback<PublicUserFollowingModel>() {
            @Override
            public void onSuccess(PublicUserFollowingModel publicUserFollowingModel) {
                followingList.addAll(publicUserFollowingModel.getData());
                notDataFound.setVisibility(View.GONE);
                if (followingList.size() != 0) {
                    notDataFound.setVisibility(View.GONE);
                } else {
                    notDataFound.setVisibility(View.VISIBLE);
                }
                adapterFollowing.notifyDataSetChanged();

            }

            @Override
            public void onError(ServerError serverError) {
                notDataFound.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getFollowing();
    }
}