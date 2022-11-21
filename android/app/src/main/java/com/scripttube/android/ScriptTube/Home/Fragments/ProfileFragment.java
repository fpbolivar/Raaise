package com.scripttube.android.ScriptTube.Home.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Settings.SettingsActivity;

import java.util.Set;

public class ProfileFragment extends Fragment {
    ImageView Settings_In_Profile;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        Initialization(v);
        ClickListeners();

        return v;
    }

    private void ClickListeners() {
        Settings_In_Profile.setOnClickListener(view -> startActivity(new Intent(context, SettingsActivity.class)));
    }

    private void Initialization(View v) {
        context = getActivity().getApplicationContext();
        Settings_In_Profile = v.findViewById(R.id.Settings_In_Profile);
    }
}