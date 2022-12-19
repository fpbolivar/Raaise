package com.scripttube.android.ScriptTube.Home.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.Adapters.GetAllUserVideoAdapter;
import com.scripttube.android.ScriptTube.Adapters.GetPublicUserVideoAdapter;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetAllUserVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.PublicUserVideoListModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity implements GetAllUserVideoAdapter.UserVideoListener {
    TextView total_donation_tvInOtherUserProfile,
            UserNameInProfileInOtherUserProfile,
            shortBioTVInOtherUserProfile,
            PostsCountInOtherUserProfile,
            FollowersCountInOtherUserProfile,
            FollowingCountInOtherUserProfile,
            FollowTextInOtherUserProfile;
    ImageView ProfileImageInOtherUserProfile, Verification_BadgeInOtherUserProfile;
    RecyclerView RecyclerViewInProfileInOtherUserProfile;
    ApiManager apiManager = App.getApiManager();
    GetPublicUserVideoAdapter adapter;
    CardView FollowButtonInOtherUserProfile, MessageButtonInOtherUserProfile;
    String UserId;
    GridLayoutManager G_LayoutManager;
    int CountOfVideos = 0,Page = 1;
    List<PublicUserVideoListModel.Data> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        Intent i = getIntent();
        UserId = i.getStringExtra("UserIdForProfile");
        Initialization();
        ClickListeners();
        GetUserProfile(UserId);
        GetAllUserVideo(Page);


    }


    private void ClickListeners() {
        FollowButtonInOtherUserProfile.setOnClickListener(view -> DoFollowUser());
//        MessageButtonInOtherUserProfile.setOnClickListener(view ->);
    }

    private void DoFollowUser() {
        UserFollowUnfollowModel model = new UserFollowUnfollowModel(UserId);
        apiManager.UserFollowUnfollow(Prefs.GetBearerToken(OtherUserProfileActivity.this), model, new DataCallback<UserFollowUnfollowModel>() {
            @Override
            public void onSuccess(UserFollowUnfollowModel userFollowUnfollowModel) {
                FollowersCountInOtherUserProfile.setText(String.valueOf(userFollowUnfollowModel.getFollowersCount()));
                FollowingCountInOtherUserProfile.setText(String.valueOf(userFollowUnfollowModel.getFollowingCount()));
                if (userFollowUnfollowModel.isFollowed()) {
                    FollowTextInOtherUserProfile.setText("Following");
                } else {
                    FollowTextInOtherUserProfile.setText("Follow");
                }
            }
            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    void GetUserProfile(String token) {
        GetPublicUserProfileModel model = new GetPublicUserProfileModel(token);
        apiManager.GetPublicUserProfile(Prefs.GetBearerToken(getApplicationContext()), model, new DataCallback<GetPublicUserProfileModel>() {
            @Override
            public void onSuccess(GetPublicUserProfileModel getPublicUserProfileModel) {
                try {
                    CountOfVideos = getPublicUserProfileModel.getData().getVideoCount();

//                    total_donation_tvInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().get));
                    PostsCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getVideoCount()));
                    UserNameInProfileInOtherUserProfile.setText(String.format("@%s", getPublicUserProfileModel.getData().getUserName()));
                    FollowersCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getFollowersCount()));
                    FollowingCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getFollowingCount()));
                    shortBioTVInOtherUserProfile.setText((getPublicUserProfileModel.data.shortBio == null||getPublicUserProfileModel.data.shortBio.equalsIgnoreCase("")) ? "Your short bio will be shown here." : getPublicUserProfileModel.data.shortBio);
                    if (getPublicUserProfileModel.getData().isVerified()) {
                        Verification_BadgeInOtherUserProfile.setVisibility(View.VISIBLE);
                    }
                    if (getPublicUserProfileModel.getData().isFollow()) {
                        FollowTextInOtherUserProfile.setText("Following");
                    }
                    Glide.with(getApplicationContext())
                            .load(getPublicUserProfileModel.getData().getProfileImage())
                            .circleCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(ProfileImageInOtherUserProfile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });

    }

    void GetAllUserVideo(int page) {
       String PageNumber = String.valueOf(page);
        PublicUserVideoListModel model = new PublicUserVideoListModel(UserId, PageNumber, "6");
        apiManager.GetPublicUserVideoList(Prefs.GetBearerToken(getApplicationContext()), model, new DataCallback<PublicUserVideoListModel>() {
            @Override
            public void onSuccess(PublicUserVideoListModel publicUserVideoListModel) {
                Page++;
                list.addAll(publicUserVideoListModel.getData());
                adapter.notifyDataSetChanged();
                Log.e("LALAD ", "onSuccess: "+Page+"  "+list.size()+"  "+CountOfVideos );
                if(list.size() != CountOfVideos-1)
                {
                    GetAllUserVideo(Page);
                }

            }

            @Override
            public void onError(ServerError serverError) {
//                if (serverError.getErrorMsg().equalsIgnoreCase("Video not Found")) {
//                    PostsCountInOtherUserProfile.setText("0");
//                } else {
//                    Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
//                }
            }
        });

    }

    private void Initialization() {


        FollowTextInOtherUserProfile = findViewById(R.id.FollowTextInOtherUserProfile);
        FollowButtonInOtherUserProfile = findViewById(R.id.FollowButtonInOtherUserProfile);
        MessageButtonInOtherUserProfile = findViewById(R.id.MessageButtonInOtherUserProfile);
        Verification_BadgeInOtherUserProfile = findViewById(R.id.Verification_BadgeInOtherUserProfile);
        total_donation_tvInOtherUserProfile = findViewById(R.id.total_donation_tvInOtherUserProfile);
        UserNameInProfileInOtherUserProfile = findViewById(R.id.UserNameInProfileInOtherUserProfile);
        shortBioTVInOtherUserProfile = findViewById(R.id.shortBioTVInOtherUserProfile);
        PostsCountInOtherUserProfile = findViewById(R.id.PostsCountInOtherUserProfile);
        FollowersCountInOtherUserProfile = findViewById(R.id.FollowersCountInOtherUserProfile);
        FollowingCountInOtherUserProfile = findViewById(R.id.FollowingCountInOtherUserProfile);
        ProfileImageInOtherUserProfile = findViewById(R.id.ProfileImageInOtherUserProfile);
        RecyclerViewInProfileInOtherUserProfile = findViewById(R.id.RecyclerViewInProfileInOtherUserProfile);
        RecyclerViewInProfileInOtherUserProfile.setHasFixedSize(true);
        RecyclerViewInProfileInOtherUserProfile.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        adapter = new GetPublicUserVideoAdapter(getApplicationContext(), list);
        RecyclerViewInProfileInOtherUserProfile.setAdapter(adapter);

    }

    @Override
    public void showVideo(int position) {

    }

//    @Override
//    public void showVideo(GetAllUserVideoModel.Data videoData) {
////        ((Home) requireActivity()).fragmentManagerHelper.replace(new ShowUserVideoFragment(new Gson().toJson(videoData)), true);
//    }
}