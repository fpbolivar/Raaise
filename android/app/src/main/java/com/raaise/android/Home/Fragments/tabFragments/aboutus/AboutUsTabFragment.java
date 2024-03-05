package com.raaise.android.Home.Fragments.tabFragments.aboutus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raaise.android.R;

public class AboutUsTabFragment extends Fragment {

    View v;
    TextView aboutUs;
    public String ShortBio="";

    public AboutUsTabFragment(String ShortBio) {
        this.ShortBio=ShortBio;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_about_us_tab, container, false);
        inItWidgets();
        return v;
    }

    private void inItWidgets(){
        aboutUs=v.findViewById(R.id.AboutUs);
        aboutUs.setText(ShortBio.equals("")|| ShortBio==null ? "No data found" : ShortBio.replace("\n", " "));
    }
}