package com.scripttube.android.ScriptTube.Home.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scripttube.android.ScriptTube.Adapters.GetAllUserVideoAdapter;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetAllUserVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetUserProfile;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.FollowersListFragment;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Settings.SettingsActivity;
import com.scripttube.android.ScriptTube.ShowUserVideoFragment;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements GetAllUserVideoAdapter.UserVideoListener {
    ImageView Settings_In_Profile, Verification_Badge, ProfileImage;
    ApiManager apiManager = App.getApiManager();
    View v;
    LinearLayout followersListBtn,FollowersListButton;
    TextView totalDonationTV;
    TextView userShortBio,FollowersTextView;
    TextView UserNameInProfile, PostsCount, FollowersCount, FollowingCount;
    RecyclerView RecyclerViewInProfile;
    GetAllUserVideoAdapter adapter;
    List<GetAllUserVideoModel.Data> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        Initialization(v);
        ClickListeners();

        totalDonationTV.setText(String.format(getString(R.string.total_donation), 20));
        return v;
    }

    private void ClickListeners() {
        Settings_In_Profile.setOnClickListener(view -> startActivity(new Intent(v.getContext(), SettingsActivity.class)));
        followersListBtn.setOnClickListener(v1 -> ((Home) requireActivity()).fragmentManagerHelper.replace(new FollowersListFragment(1), true));
        FollowersListButton.setOnClickListener(v1 -> ((Home) requireActivity()).fragmentManagerHelper.replace(new FollowersListFragment(2), true));
    }

    private void Initialization(View v) {
        FollowersListButton = v.findViewById(R.id.FollowersListButton);
        followersListBtn = v.findViewById(R.id.followers_list_btn);
        totalDonationTV = v.findViewById(R.id.total_donation_tv);
        userShortBio = v.findViewById(R.id.shortBioTV);
        ProfileImage = v.findViewById(R.id.ProfileImage);
        PostsCount = v.findViewById(R.id.PostsCount);
        FollowersTextView = v.findViewById(R.id.FollowersTextView);
        FollowersCount = v.findViewById(R.id.FollowersCount);
        FollowingCount = v.findViewById(R.id.FollowingCount);
        Settings_In_Profile = v.findViewById(R.id.Settings_In_Profile);
        UserNameInProfile = v.findViewById(R.id.UserNameInProfile);
        Verification_Badge = v.findViewById(R.id.Verification_Badge);
        RecyclerViewInProfile = v.findViewById(R.id.RecyclerViewInProfile);
        RecyclerViewInProfile.setHasFixedSize(true);
        RecyclerViewInProfile.setLayoutManager(new GridLayoutManager(v.getContext(), 3));

    }

    @Override
    public void onResume() {
        super.onResume();
        GetUserProfile();
        GetAllUserVideo();
    }

    void GetUserProfile() {
        apiManager.GetUserProfile(Prefs.GetBearerToken(v.getContext()), new DataCallback<GetUserProfile>() {
            @Override
            public void onSuccess(GetUserProfile getUserProfile) {
                try {
                    UserNameInProfile.setText(String.format("@%s", getUserProfile.getData().getUserName()));
                    Prefs.setUserName(getActivity(), getUserProfile.data.userName);
                    Prefs.setNameOfUser(getActivity(), getUserProfile.data.name);
                    Prefs.setUserImage(getActivity(), getUserProfile.data.profileImage);
                    FollowersCount.setText(String.valueOf(getUserProfile.getData().getFollowersCount()));
                    FollowingCount.setText(String.valueOf(getUserProfile.getData().getFollowingCount()));
                    if (getUserProfile.getData().getFollowersCount() == 1) {
                        FollowersTextView.setText("Follower");
                    } else {
                        FollowersTextView.setText("Followers");
                    }
                    userShortBio.setText(getUserProfile.data.shortBio == null ? "Your short bio will be shown here." : getUserProfile.data.shortBio);
                    if (getUserProfile.getData().isVerified()) {
                        Verification_Badge.setVisibility(View.VISIBLE);
                    }
                    Glide.with(v.getContext())
                            .load(getUserProfile.getData().getProfileImage())
                            .circleCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(ProfileImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    void GetAllUserVideo() {
     list = new ArrayList<>();
        apiManager.GetAllUserVideo(Prefs.GetBearerToken(v.getContext()), new DataCallback<GetAllUserVideoModel>() {
            @Override
            public void onSuccess(GetAllUserVideoModel getAllUserVideoModel) {

                PostsCount.setText(String.valueOf(getAllUserVideoModel.getData().size()));
                list.addAll(getAllUserVideoModel.getData());
                adapter = new GetAllUserVideoAdapter(getActivity(), list, ProfileFragment.this::showVideo);
                RecyclerViewInProfile.setAdapter(adapter);

            }

            @Override
            public void onError(ServerError serverError) {
                if (serverError.getErrorMsg().equalsIgnoreCase("Video not Found")) {
                    PostsCount.setText("0");
                } else {
                    Prompt.SnackBar(v, serverError.getErrorMsg());
                }
            }
        });
    }

    @Override
    public void showVideo(int position) {
//        ((Home) requireActivity()).fragmentManagerHelper.replace(new ShowUserVideoFragment(new Gson().toJson(videoData)), true);
        ((Home) requireActivity()).fragmentManagerHelper.replace(new ShowUserVideoFragment(list,position), true);
    }
}