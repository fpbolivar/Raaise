package com.raaise.android.Home.Fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.raaise.android.Adapters.GetAllUserVideoAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetAllUserVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetUserProfile;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.FollowersListFragment;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Settings.SettingsActivity;
import com.raaise.android.ShowUserVideoFragment;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements GetAllUserVideoAdapter.UserVideoListener {
    ImageView Settings_In_Profile, Verification_Badge, ProfileImage;
    ApiManager apiManager = App.getApiManager();
    View v;
    LinearLayout followersListBtn, FollowersListButton;
    TextView totalDonationTV;
    TextView userShortBio, FollowersTextView;
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

        GetUserProfile();

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
    }

    void GetUserProfile() {
        apiManager.GetUserProfile(Prefs.GetBearerToken(v.getContext()), new DataCallback<GetUserProfile>() {
            @Override
            public void onSuccess(GetUserProfile getUserProfile) {
                try {
                    PostsCount.setText(String.valueOf(getUserProfile.getData().getVideoCount()));
                    UserNameInProfile.setText(String.format("@%s", getUserProfile.getData().getUserName()));

                    Prefs.setUserName(getActivity(), getUserProfile.data.userName);
                    Prefs.setNameOfUser(getActivity(), getUserProfile.data.name);
                    Prefs.setUserImage(getActivity(), getUserProfile.data.profileImage);
                    Prefs.SetPhoneNumberOfTheUser(getActivity(), getUserProfile.data.phoneNumber);
                    Prefs.SetUserEmail(getActivity(), getUserProfile.data.email);
                    Prefs.SetUserShortBio(getActivity(), getUserProfile.data.getShortBio());

                    FollowersCount.setText(String.valueOf(getUserProfile.getData().getFollowersCount()));
                    FollowingCount.setText(String.valueOf(getUserProfile.getData().getFollowingCount()));
                    if (getUserProfile.getData().getFollowersCount() == 1) {
                        FollowersTextView.setText("Follower");
                    } else {
                        FollowersTextView.setText("Followers");
                    }
                    userShortBio.setText(getUserProfile.data.shortBio == null ? "" : getUserProfile.data.shortBio.replace("\n", " "));
                    if (getUserProfile.getData().isVerified()) {
                        Verification_Badge.setVisibility(View.VISIBLE);
                    }
                    Glide.with(v.getContext())
                            .load(Prefs.GetBaseUrl(getContext()) + getUserProfile.getData().getProfileImage())
                            .circleCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(ProfileImage);
                    totalDonationTV.setText(String.format("Total Supported ($%s)", (getUserProfile.getData().getDonatedAmount().equalsIgnoreCase("0")) ? "00:00" : getUserProfile.getData().getDonatedAmount()));
                    GetAllUserVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                GetUserProfile();
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    void GetAllUserVideo() {
        list = new ArrayList<>();
        apiManager.GetAllUserVideo(Prefs.GetBearerToken(v.getContext()), new DataCallback<GetAllUserVideoModel>() {
            @Override
            public void onSuccess(GetAllUserVideoModel getAllUserVideoModel) {
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
        ((Home) requireActivity()).fragmentManagerHelper.replace(new ShowUserVideoFragment(list, position), true);
    }
}