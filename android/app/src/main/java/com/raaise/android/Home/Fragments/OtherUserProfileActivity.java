package com.raaise.android.Home.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.raaise.android.Adapters.GetAllUserVideoAdapter;
import com.raaise.android.Adapters.GetPublicUserVideoAdapter;
import com.raaise.android.Adapters.TagsCategoryAdapterInProfile;
import com.raaise.android.Adapters.ViewPagerApater;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.raaise.android.ApiManager.ApiModels.ProfileChatModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserVideoListModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.FollowersListFragment;
import com.raaise.android.Home.Fragments.tabFragments.aboutus.AboutUsTabFragment;
import com.raaise.android.Home.Fragments.tabFragments.donation.DonationTabFragment;
import com.raaise.android.Home.Fragments.tabFragments.followers.FollowerTabFragment;
import com.raaise.android.Home.Fragments.tabFragments.followings.FollowingTabFragment;
import com.raaise.android.Home.Fragments.tabFragments.posts.PostTabFragment;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.model.BlockUserPojo;
import com.raaise.android.model.VideoCommentDelete;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity implements GetAllUserVideoAdapter.UserVideoListener, ViewPagerApater.SelectedInterface {
    private boolean userBlocked;
    ImageView moreOptions,CoverImageInOtherUserProfile;
    TextView total_donation_tvInOtherUserProfile,
            UserNameInProfileInOtherUserProfile,
            shortBioTVInOtherUserProfile,
            PostsCountInOtherUserProfile,
            FollowersCountInOtherUserProfile,
            FollowingCountInOtherUserProfile,
            FollowTextInOtherUserProfile, FollowersTextInOtherUserProfile;
    ImageView ProfileImageInOtherUserProfile, Verification_BadgeInOtherUserProfile, BackInOtherUserProfile;
    RecyclerView RecyclerViewInProfileInOtherUserProfile,RecyclerViewInProfileTags;
    ApiManager apiManager = App.getApiManager();
    GetPublicUserVideoAdapter adapter;
    TagsCategoryAdapterInProfile tagsCategoryAdapter;

    CardView  MessageButtonInOtherUserProfile;
    String UserId, Username;
    GridLayoutManager G_LayoutManager;
    int CountOfVideos = 0, Page = 1,Tabposition;
    List<GetPublicUserProfileModel.Data.InterestedCategories> categories = new ArrayList<>();
    List<PublicUserVideoListModel.Data> list = new ArrayList<>();
    LinearLayout FollowingLayout, FollowersLayout, HideSelf;
    RelativeLayout FollowButtonInOtherUserProfile;
    String
            ReceiverId,
            SenderId,
            UserImageLink,
            ShortBio;

    String
            posts="",
            followers="",
            followings="";
    TabLayout tabLayout;
    ViewPager2 viewPager;
    String[] postString={};
    String[] postCount={};
    Fragment fragment;
    List<GetPublicUserProfileModel.Data.DonationUsers> donationUsers=new ArrayList<>();
    String totalRaised="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        Intent i = getIntent();
        UserId = i.getStringExtra("UserIdForProfile");
        Username = i.getStringExtra("UserNameForProfile");

        Initialization();
        ClickListeners();
        GetUserProfile(UserId);
//        RecyclerViewInProfileInOtherUserProfile.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (!recyclerView.canScrollVertically(1)) {
//                    Page++;
//                    GetAllUserVideo(Page);
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

    }


    private void ClickListeners() {
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(OtherUserProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.more_options_dialog_other_user);

                TextView blockUserTV;
                LinearLayout blobkUserBtn = dialog.findViewById(R.id.report_video_btn);
                LinearLayout reportUserBtn = dialog.findViewById(R.id.report_user_btn);
                blockUserTV = dialog.findViewById(R.id.block_tv);
                if (userBlocked){
                    blockUserTV.setText("Unblock User");
                } else {
                    blockUserTV.setText("Block User");
                }
                blobkUserBtn.setOnClickListener(view -> {
                    dialog.dismiss();
                    Dialogs.createProgressDialog(OtherUserProfileActivity.this);
                    BlockUserPojo model = new BlockUserPojo(UserId);
                    apiManager.blockUser(Prefs.GetBearerToken(OtherUserProfileActivity.this), model, new DataCallback<VideoCommentDelete>() {
                        @Override
                        public void onSuccess(VideoCommentDelete videoCommentDelete) {
                            App.userFollowUnfolle = true;
                            Dialogs.HideProgressDialog();
                            onBackPressed();
                        }

                        @Override
                        public void onError(ServerError serverError) {
                            Dialogs.HideProgressDialog();
                            Prompt.SnackBar(findViewById(android.R.id.content).getRootView(), serverError.getErrorMsg());
                        }
                    });
                });

                reportUserBtn.setOnClickListener(view -> {
                    dialog.dismiss();
                    Dialogs.createProgressDialog(OtherUserProfileActivity.this);
                    BlockUserPojo model = new BlockUserPojo(UserId);
                    apiManager.reportUser(Prefs.GetBearerToken(OtherUserProfileActivity.this), model, new DataCallback<VideoCommentDelete>() {
                        @Override
                        public void onSuccess(VideoCommentDelete videoCommentDelete) {
                            Dialogs.HideProgressDialog();
                            Prompt.SnackBar(findViewById(android.R.id.content).getRootView(), videoCommentDelete.message);
                        }

                        @Override
                        public void onError(ServerError serverError) {
                            Dialogs.HideProgressDialog();
                            Prompt.SnackBar(findViewById(android.R.id.content).getRootView(), serverError.getErrorMsg());
                        }
                    });
                });

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
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
                App.userFollowUnfolle = true;
                Log.i("userDetails", "onSuccess: " + App.userFollowUnfolle);
                FollowersCountInOtherUserProfile.setText(String.valueOf(userFollowUnfollowModel.getFollowersCount()));
                followers= String.valueOf(userFollowUnfollowModel.getFollowersCount());

                FollowingCountInOtherUserProfile.setText(String.valueOf(userFollowUnfollowModel.getFollowingCount()));
//                if (UserId.equalsIgnoreCase(Prefs.GetUserID(OtherUserProfileActivity.this))) {
//                      followings= String.valueOf(userFollowUnfollowModel.getFollowingCount());
//                }
                Log.e("Followers","Followers"+String.valueOf(userFollowUnfollowModel.getFollowersCount()));
                Log.e("Following","Following"+String.valueOf(userFollowUnfollowModel.getFollowingCount()));

                if (userFollowUnfollowModel.isFollowed()) {
                    FollowTextInOtherUserProfile.setText("Following");
                } else {
                    FollowTextInOtherUserProfile.setText("Follow");
                }
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setFollow(userFollowUnfollowModel.isFollowed);
                }
              //  tabLayout.removeAllTabs();
                tabText(true);
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
        if (App.fromTryAudio){
            onBackPressed();
        }
//        list.clear();
//        adapter.notifyDataSetChanged();
//        Page = 1;
//        CountOfVideos = 0;
//        GetUserProfile(UserId);
//        GetAllUserVideo(Page);
    }

    void GetUserProfile(String token) {
        GetPublicUserProfileModel model = new GetPublicUserProfileModel(token);
        Log.e("ID","IDS"+new Gson().toJson(model));
        apiManager.GetPublicUserProfile(Prefs.GetBearerToken(getApplicationContext()), model, new DataCallback<GetPublicUserProfileModel>() {
            @Override
            public void onSuccess(GetPublicUserProfileModel getPublicUserProfileModel) {
                try {
                    Log.e("ID","getPublicUserProfileModel"+new Gson().toJson(getPublicUserProfileModel));

                    total_donation_tvInOtherUserProfile.setVisibility(View.VISIBLE);

                    if (getPublicUserProfileModel.getData().getBlocked_by() != null && getPublicUserProfileModel.getData().getBlocked_by().equalsIgnoreCase("me")){
                        userBlocked = true;
                    } else {
                        userBlocked = false;
                    }
                    if (getPublicUserProfileModel.getData()._id.equals(Prefs.GetUserID(OtherUserProfileActivity.this))){
                        moreOptions.setVisibility(View.GONE);
                    }
                    CountOfVideos = getPublicUserProfileModel.getData().getVideoCount();
                    ReceiverId = getPublicUserProfileModel.getData().get_id();
                    SenderId = getPublicUserProfileModel.getData().get_id();
                    UserImageLink = Prefs.GetBaseUrl(OtherUserProfileActivity.this) + getPublicUserProfileModel.getData().getProfileImage();
                    ShortBio = getPublicUserProfileModel.getData().getShortBio();

                    donationUsers.addAll(getPublicUserProfileModel.getData().getDonationUsers());

                    totalRaised=getPublicUserProfileModel.getData().getDonatedAmount();

                    total_donation_tvInOtherUserProfile.setText(String.format("$%s", getPublicUserProfileModel.getData().getDonatedAmount() == null ? 00.00 : getPublicUserProfileModel.getData().getDonatedAmount()));
                    PostsCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getVideoCount()));
                    posts= String.valueOf(getPublicUserProfileModel.getData().getVideoCount());
                    UserNameInProfileInOtherUserProfile.setText(String.format("@%s", getPublicUserProfileModel.getData().getUserName()));
                    Username = getPublicUserProfileModel.getData().getUserName();

                    if (getPublicUserProfileModel.getData().get_id().equalsIgnoreCase(Prefs.GetUserID(OtherUserProfileActivity.this))) {
                        HideSelf.setVisibility(View.GONE);
                    }

                    FollowersCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getFollowersCount()));
                    followers= String.valueOf(getPublicUserProfileModel.getData().getFollowersCount());
                    Log.e("Followers","Followers"+String.valueOf(getPublicUserProfileModel.getData().getFollowersCount()));
                    Log.e("Following","Following"+String.valueOf(getPublicUserProfileModel.getData().getFollowingCount()));

                    if (getPublicUserProfileModel.getData().getFollowersCount() == 1) {
                        FollowersTextInOtherUserProfile.setText("Follower");
                    } else {
                        FollowersTextInOtherUserProfile.setText("Followers");
                    }
                    FollowingCountInOtherUserProfile.setText(String.valueOf(getPublicUserProfileModel.getData().getFollowingCount()));
                    followings= String.valueOf(getPublicUserProfileModel.getData().getFollowingCount());

              //      shortBioTVInOtherUserProfile.setText((getPublicUserProfileModel.data.shortBio == null || getPublicUserProfileModel.data.shortBio.equalsIgnoreCase("")) ? "" : getPublicUserProfileModel.data.shortBio.replace("\n", " "));
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

                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(Prefs.GetBaseUrl(OtherUserProfileActivity.this) + getPublicUserProfileModel.getData().coverImage)
                            .placeholder(R.drawable.cover_image_1)
                            .into(CoverImageInOtherUserProfile);

                    categories.addAll(getPublicUserProfileModel.getData().getInterestCategoryData());
                    RecyclerViewInProfileTags.setHasFixedSize(true);
                    RecyclerViewInProfileTags.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    tagsCategoryAdapter = new TagsCategoryAdapterInProfile(OtherUserProfileActivity.this,categories);
                    RecyclerViewInProfileTags.setAdapter(tagsCategoryAdapter);
                    tabText(false);

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
            Log.i("modelVideo", "GetAllUserVideo: " + new Gson().toJson(model));
            apiManager.GetPublicUserVideoList(Prefs.GetBearerToken(getApplicationContext()), model, new DataCallback<PublicUserVideoListModel>() {
                @Override
                public void onSuccess(PublicUserVideoListModel publicUserVideoListModel) {
                    list.addAll(publicUserVideoListModel.getData());
                    Log.i("getPublicVideoUser", "onSuccess: " + new Gson().toJson(publicUserVideoListModel.data));
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
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        moreOptions = findViewById(R.id.more_options);
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
        shortBioTVInOtherUserProfile.setVisibility(View.GONE);
        PostsCountInOtherUserProfile = findViewById(R.id.PostsCountInOtherUserProfile);
        FollowersCountInOtherUserProfile = findViewById(R.id.FollowersCountInOtherUserProfile);
        FollowingCountInOtherUserProfile = findViewById(R.id.FollowingCountInOtherUserProfile);
        ProfileImageInOtherUserProfile = findViewById(R.id.ProfileImageInOtherUserProfile);
        CoverImageInOtherUserProfile=findViewById(R.id.CoverImageInOtherUserProfile);

        RecyclerViewInProfileTags=findViewById(R.id.RecyclerViewInProfileTags);

//        RecyclerViewInProfileInOtherUserProfile = findViewById(R.id.RecyclerViewInProfileInOtherUserProfile);
//        RecyclerViewInProfileInOtherUserProfile.setHasFixedSize(true);
//        RecyclerViewInProfileInOtherUserProfile.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
//        adapter = new GetPublicUserVideoAdapter(getApplicationContext(), list);
//        RecyclerViewInProfileInOtherUserProfile.setAdapter(adapter);
//        RecyclerViewInProfileInOtherUserProfile.setNestedScrollingEnabled(false);
        setupViewPager(viewPager);

    }

    private void tabText(boolean update){
        postString= new String[]{"Post", "About us", "Donation", "Followers", "Following"};
        postCount=new String[]{""+posts,"","",""+followers,""+followings};

        if(!update){
            for (int i = 0; i < postString.length; i++) {
                View tab = LayoutInflater.from(this).inflate(R.layout.custom_tabbar_view, null);
                TextView textName = (TextView) tab.findViewById(R.id.title);
                TextView textViewNo = (TextView) tab.findViewById(R.id.count);
                textName.setTextColor(ContextCompat.getColor(this, R.color.white));
                textViewNo.setTextColor(ContextCompat.getColor(this, R.color.white));

                textName.setText(postString[i]);
                textViewNo.setText(postCount[i].equals("")?"" : "("+postCount[i]+")");
                tabLayout.addTab(tabLayout.newTab().setCustomView(tab));
            }
        }else{
            TabLayout.Tab tab = tabLayout.getTabAt(3); // third tab
            View tabViewFollowers = tab.getCustomView();
            TextView count = (TextView) tabViewFollowers.findViewById(R.id.count);
            count.setText("("+followers+")");
//            if (UserId.equalsIgnoreCase(Prefs.GetUserID(OtherUserProfileActivity.this))) {
//                TabLayout.Tab tab1 = tabLayout.getTabAt(4); // fourth tab
//                View tabViewFollowings = tab1.getCustomView();
//                TextView count1 = (TextView) tabViewFollowings.findViewById(R.id.count);
//                count1.setText("("+followings+")");
//            }
        }
    }

    private void setupViewPager(ViewPager2 viewPager)
    {
        ViewPagerApater adapter = new ViewPagerApater(getSupportFragmentManager(),getLifecycle(),this);
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Tabposition=tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    public void showVideo(int position) {
    }

    @Override
    public Fragment selectedView(int position) {
        switch (position){
            case 0:
                return new  PostTabFragment(UserId);
            case 1:
                return  new AboutUsTabFragment(ShortBio);
            case 2:
                return new DonationTabFragment(donationUsers,totalRaised);
            case 3:
                return new FollowerTabFragment(Username);
            case 4:
                return new FollowingTabFragment(Username);
        }
        return new PostTabFragment(UserId);
    }
}