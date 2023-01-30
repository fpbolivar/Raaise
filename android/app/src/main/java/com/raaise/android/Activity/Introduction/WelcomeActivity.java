package com.raaise.android.Activity.Introduction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.raaise.android.R;

public class WelcomeActivity extends AppCompatActivity implements IntroInterFace {
    ViewPager WelcomeViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Initialization();
        SetViewPager();
        WelcomeViewPager.setCurrentItem(0);
    }

    private void SetViewPager() {
        WelcomeViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Welcome1 frag = new Welcome1();
                        return frag;
                    case 1:
                        Welcome2 followingFrag = new Welcome2();
                        return followingFrag;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

    private void Initialization() {
        WelcomeViewPager = findViewById(R.id.WelcomeViewPager);
    }

    @Override
    public void Finishing() {
        finish();
    }

    @Override
    public void SelectItem2() {
        WelcomeViewPager.setCurrentItem(1);
    }
}