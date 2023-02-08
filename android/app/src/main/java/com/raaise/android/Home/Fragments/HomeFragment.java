package com.raaise.android.Home.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetUserProfile;
import com.raaise.android.ApiManager.ApiModels.UnReadNotificationCountModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.Fragments.Home_Following_foryou_Fragments.Home_FollowingFragment;
import com.raaise.android.Home.Fragments.Home_Following_foryou_Fragments.Home_ForYouFragment;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;


public class HomeFragment extends Fragment {

    public static int Reselected, Unselected;
    View v;
    TabLayout tabLayout;
    ViewPager ViewPager;
    PagerAdapter pagerAdapter;
    ImageView NotificationBellIcon;
    Home_ForYouFragment frag;
    Home_FollowingFragment followingFrag;
    ApiManager apiManager = App.getApiManager();
    TextView NotificationCount;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        Initialization(v);
        clickListeners();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//                Toast.makeText(getContext(), "Please Allow Permissions", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        if (!hasPermissions(getContext(), PERMISSIONS)) {
//            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
//        }

        return v;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    void GetUserProfile() {
        apiManager.GetUserProfile(Prefs.GetBearerToken(v.getContext()), new DataCallback<GetUserProfile>() {
            @Override
            public void onSuccess(GetUserProfile getUserProfile) {
                try {
                    Prefs.setUserName(getActivity(), getUserProfile.data.userName);
                    Prefs.setNameOfUser(getActivity(), getUserProfile.data.name);
                    Prefs.setUserImage(getActivity(), getUserProfile.data.profileImage);
                    Prefs.SetPhoneNumberOfTheUser(getActivity(), getUserProfile.data.phoneNumber);
                    Prefs.SetUserEmail(getActivity(), getUserProfile.data.email);
                    Prefs.SetUserShortBio(getActivity(), getUserProfile.data.getShortBio());
                    Prefs.SetUserID(getActivity(), getUserProfile.data.get_id());

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

    private void clickListeners() {
        NotificationBellIcon.setOnClickListener(view -> {

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack("Home")
                    .replace(R.id.FragmentContainer, new InboxFragment(), null)
                    .commit();
        });
    }


    private void Initialization(View v) {
        NotificationCount = v.findViewById(R.id.NotificationCount);
        NotificationBellIcon = v.findViewById(R.id.NotificationBellIcon);
        tabLayout = v.findViewById(R.id.tabLayout);
        ViewPager = v.findViewById(R.id.ViewPager1);
        tabLayout.addTab(tabLayout.newTab().setText("For You"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));


        showViewPager();
        tabLayout.getTabAt(0).select();
        ViewPager.setCurrentItem(0);


    }

    @Override
    public void onResume() {
        super.onResume();
        GetUserProfile();
        ChackNotificationCount();
    }

    private void ChackNotificationCount() {
        apiManager.UnReadNotificationCount(Prefs.GetBearerToken(v.getContext()), new DataCallback<UnReadNotificationCountModel>() {
            @Override
            public void onSuccess(UnReadNotificationCountModel unReadNotificationCountModel) {
                if (unReadNotificationCountModel.getNotificationUnreadCount() == 0) {
                    NotificationCount.setVisibility(View.INVISIBLE);
                } else {
                    NotificationCount.setVisibility(View.VISIBLE);
                    NotificationCount.setText(String.valueOf(unReadNotificationCountModel.getNotificationUnreadCount()));
                }
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });
    }

    private void showViewPager() {

        ViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        frag = new Home_ForYouFragment();
                        return frag;
                    case 1:
                        followingFrag = new Home_FollowingFragment();
                        tabLayout.setSelectedTabIndicator(R.drawable.custom_tablayout_indicator);
                        return followingFrag;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }

        });
        ViewPager.setOffscreenPageLimit(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        frag.ResumePlay();
                        followingFrag.StopPlayingForYou();
                        ViewPager.getAdapter().notifyDataSetChanged();
                        tabLayout.setSelectedTabIndicator(R.drawable.custom_tablayout_indicator1);
                        setUserVisibleHint(true);
                        break;
                    case 1:
                        followingFrag.ResumePlay();
                        frag.StopPlayingFollowing();
                        ViewPager.getAdapter().notifyDataSetChanged();
                        tabLayout.setSelectedTabIndicator(R.drawable.custom_tablayout_indicator);
                        setUserVisibleHint(true);
                        break;

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getPosition();
                Log.i("Position", "Unselected" + tab.getPosition());
                Unselected = Integer.parseInt(String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ViewPager.setCurrentItem(tab.getPosition());
                Log.i("Position", "Reselected" + tab.getPosition());
                Reselected = Integer.parseInt(String.valueOf(tab.getPosition()));
            }

        });
        ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


}