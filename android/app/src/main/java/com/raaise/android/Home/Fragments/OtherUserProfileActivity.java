package com.raaise.android.Home.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.Adapters.GetAllUserVideoAdapter;
import com.raaise.android.Adapters.GetPublicUserVideoAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.raaise.android.ApiManager.ApiModels.ProfileChatModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserVideoListModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.FollowersListFragment;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity implements GetAllUserVideoAdapter.UserVideoListener {
    TextView total_donation_tvInOtherUserProfile,
            UserNameInProfileInOtherUserProfile,
            shortBioTVInOtherUserProfile,
            PostsCountInOtherUserProfile,
            FollowersCountInOtherUserProfile,
            FollowingCountInOtherUserProfile,
            FollowTextInOtherUserProfile, FollowersTextInOtherUserProfile;
    ImageView ProfileImageInOtherUserProfile, Verification_BadgeInOtherUserProfile, BackInOtherUserProfile;
    RecyclerView RecyclerViewInProfileInOtherUserProfile;
    ApiManager apiManager = App.getApiManager();
    GetPublicUserVideoAdapter adapter;
    CardView FollowButtonInOtherUserProfile, MessageButtonInOtherUserProfile;
    String UserId, Username;
    GridLayoutManager G_LayoutManager;
    int CountOfVideos = 0, Page = 1;
    List<PublicUserVideoListModel.Data> list = new ArrayList<>();
    LinearLayout FollowingLayout, FollowersLayout, HideSelf;
    String
            ReceiverId,
            SenderId,
            UserImageLink,
            ShortBio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        Intent i = getIntent();
        UserId = i.getStringExtra("UserIdForProfile");
        Username = i.getStringExtra("UserNameForProfile");

        Initialization();
        ClickListeners();
        RecyclerViewInProfileInOtherUserProfile.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    private void ClickListeners() {
        FollowButtonInOtherUserProfile.setOnClickListener(view -> DoFollowUser());
        BackInOtherUserProfile.setOnClickListener(view -> finish());
        MessageButtonInOtherUserProfile.setOnClickListener(view -> {
            HitApiForGettingSlug();

        });
        FollowingLayout.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new FollowersListFragment(3, Username)).commit();
        });
        FollowersLayout.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new FollowersListFragment(4, Username)).commit();
        });
    }

    public void HitApiForGettingSlug() {
        ProfileChatModel model = new ProfileChatModel(Prefs.GetUserID(OtherUserProfileActivity.this), SenderId);
        apiManager.ProfileChat(Prefs.GetBearerToken(OtherUserProfileActivity.this), model, new DataCallback<ProfileChatModel>() {
            @Override
            public void onSuccess(ProfileChatModel profileChatModel) {
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new ChatFragment(profileChatModel.getChatSlug(), SenderId, Prefs.GetUserID(OtherUserProfileActivity.this), UserImageLink, Username, ShortBio, 2)).commit();
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
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
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setFollow(userFollowUnfollowModel.isFollowed);
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
        list.clear();
        adapter.notifyDataSetChanged();
        Page = 1;
        CountOfVideos = 0;
        GetUserProfile(UserId);
        GetAllUserVideo(Page);
    }

    void GetUserProfile(String token) {
        GetPublicUserProfileModel model = new GetPublicUserProfileModel(token);
        apiManager.GetPublicUserProfile(Prefs.GetBearerToken(getApplicationContext()), model, new DataCallback<GetPublicUserProfileModel>() {
            @Override
            public void onSuccess(GetPublicUserProfileModel getPublicUserProfileModel) {
                try {
                    total_donation_tvInOtherUserProfile.setVisibility(View.VISIBLE);

                    CountOfVideos = getPublicUserProfileModel.getData().getVideoCount();
                    ReceiverId = getPublicUserProfileModel.getData().get_id();
                    SenderId = getPublicUserProfileModel.getData().get_id();
                    UserImageLink = Prefs.GetBaseUrl(OtherUserProfileActivity.this) + getPublicUserProfileModel.getData().getProfileImage();
                    ShortBio = getPublicUserProfileModel.getData().getShortBio();

                    total_donation_tvInOtherUserProfile.setText(String.format("Total Donated ($%s)", getPublicUserProfileModel.getData().getDonatedAmount() == null ? 00.00 : getPublicUserProfileModel.getData().getDonatedAmount()));
                    PostsCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getVideoCount()));
                    UserNameInProfileInOtherUserProfile.setText(String.format("@%s", getPublicUserProfileModel.getData().getUserName()));
                    Username = getPublicUserProfileModel.getData().getUserName();

                    if (getPublicUserProfileModel.getData().get_id().equalsIgnoreCase(Prefs.GetUserID(OtherUserProfileActivity.this))) {
                        HideSelf.setVisibility(View.GONE);
                    }

                    FollowersCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getFollowersCount()));

                    if (getPublicUserProfileModel.getData().getFollowersCount() == 1) {
                        FollowersTextInOtherUserProfile.setText("Follower");
                    } else {
                        FollowersTextInOtherUserProfile.setText("Followers");
                    }
                    FollowingCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getFollowingCount()));
                    shortBioTVInOtherUserProfile.setText((getPublicUserProfileModel.data.shortBio == null || getPublicUserProfileModel.data.shortBio.equalsIgnoreCase("")) ? "" : getPublicUserProfileModel.data.shortBio.replace("\n", " "));
                    if (getPublicUserProfileModel.getData().isVerified()) {
                        Verification_BadgeInOtherUserProfile.setVisibility(View.VISIBLE);
                    }
                    if (getPublicUserProfileModel.getData().isFollow()) {
                        FollowTextInOtherUserProfile.setText("Following");
                    } else {
                        FollowTextInOtherUserProfile.setText("Follow");
                    }
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(UserImageLink)
                            .override(1600, 1600)
                            .circleCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(ProfileImageInOtherUserProfile);
//                    Glide.with(getApplicationContext())
//                            .asBitmap()
//                            .load('url')
//                            .apply(new RequestOptions().override(1600, 1600)) //This is important
//                            .into(new BitmapImageViewTarget('imageview') {
//                                @Override
//                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                    super.onResourceReady(resource, transition);
//                                    'imageview'.setImageBitmap(resource);
//                                    'imageview'.setZoom(1); //This is important
//                                }
//                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                total_donation_tvInOtherUserProfile.setVisibility(View.GONE);
                CheckDeleted(serverError.getErrorMsg());
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });

    }

    private void CheckDeleted(String userName) {
        total_donation_tvInOtherUserProfile.setVisibility(View.GONE);
        HideSelf.setVisibility(View.GONE);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OtherUserProfileActivity.this);
        builder1.setMessage(userName);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OtherUserProfileActivity.this.finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    void GetAllUserVideo(int page) {
        try {


            String PageNumber = String.valueOf(page);

            PublicUserVideoListModel model = new PublicUserVideoListModel(UserId, PageNumber, "6");
            apiManager.GetPublicUserVideoList(Prefs.GetBearerToken(getApplicationContext()), model, new DataCallback<PublicUserVideoListModel>() {
                @Override
                public void onSuccess(PublicUserVideoListModel publicUserVideoListModel) {
                    list.addAll(publicUserVideoListModel.getData());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(ServerError serverError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Initialization() {

        HideSelf = findViewById(R.id.HideSelf);
        BackInOtherUserProfile = findViewById(R.id.BackInOtherUserProfile);
        FollowingLayout = findViewById(R.id.FollowingLayout);
        FollowersLayout = findViewById(R.id.FollowersLayout);
        FollowersTextInOtherUserProfile = findViewById(R.id.FollowersTextInOtherUserProfile);
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


}