package com.scripttube.android.ScriptTube.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.scripttube.android.ScriptTube.R;


public class InboxFragment extends Fragment {
    View v;
    ImageView BackInNotification;


    ImageView Back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inbox, container, false);
        Initialization();
        ClickListeners();


        return v;
    }

    private void ClickListeners() {
        BackInNotification.setOnClickListener(view -> {
            getFragmentManager()
                                           .beginTransaction()
                                           .replace(R.id.FragmentContainer, new HomeFragment(), null)
                                           .commit();
        });
    }

    private void Initialization() {
        BackInNotification = v.findViewById(R.id.BackInNotification);
    }
}