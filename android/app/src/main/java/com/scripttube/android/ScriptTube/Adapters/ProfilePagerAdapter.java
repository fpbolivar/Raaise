package com.scripttube.android.ScriptTube.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.scripttube.android.ScriptTube.Home.Fragments.MyContentFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.MyProfileFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.MyVideosFragment;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public ProfilePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new MyContentFragment();

            case 1:
                return new MyVideosFragment();

            case 2:
                return new MyProfileFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}