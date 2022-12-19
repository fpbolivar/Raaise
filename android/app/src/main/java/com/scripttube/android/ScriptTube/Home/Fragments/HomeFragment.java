package com.scripttube.android.ScriptTube.Home.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.scripttube.android.ScriptTube.Home.Fragments.Home_Following_foryou_Fragments.Home_FollowingFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.Home_Following_foryou_Fragments.Home_ForYouFragment;
import com.scripttube.android.ScriptTube.R;


public class HomeFragment extends Fragment {

    View v;
    TabLayout tabLayout;
    ViewPager ViewPager;
    PagerAdapter pagerAdapter;
    ImageView NotificationBellIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        
        Initialization(v);
        clickListeners();

        return v;
    }

    private void clickListeners() {
        NotificationBellIcon.setOnClickListener(view -> {
//            getFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.FragmentContainer, new InboxFragment(), null)
//                    .commit();
//            startActivity(new Intent(getActivity(),));
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                        .addToBackStack("Home")
                    .replace(R.id.FragmentContainer, new InboxFragment(), null)
                    .commit();
        });
    }


    private void Initialization(View v) {
        NotificationBellIcon = v.findViewById(R.id.NotificationBellIcon);
        tabLayout = v.findViewById(R.id.tabLayout);
        ViewPager = v.findViewById(R.id.ViewPager1);
        tabLayout.addTab(tabLayout.newTab().setText("For You"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        showViewPager();
//        ViewPager.setCurrentItem(0);

        tabLayout.getTabAt(0).select();
        ViewPager.setCurrentItem(0);


    }

    private void showViewPager() {

        ViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Home_ForYouFragment frag = new Home_ForYouFragment();
                        return frag;
                    case 1:
                        Home_FollowingFragment followingFrag = new Home_FollowingFragment();
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
        ViewPager.setOffscreenPageLimit(2);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition())
                {
                    case 0:
                        tabLayout.setSelectedTabIndicator(R.drawable.custom_tablayout_indicator1);
                        break;
                    case 1:
                        tabLayout.setSelectedTabIndicator(R.drawable.custom_tablayout_indicator);
                        break;

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ViewPager.setCurrentItem(tab.getPosition());
            }
        });
        ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


}