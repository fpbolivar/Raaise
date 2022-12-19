package com.scripttube.android.ScriptTube.Home.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scripttube.android.ScriptTube.R;

public class Try_AudioFragment extends Fragment {


    public Try_AudioFragment() {
        // Required empty public constructor
    }
    RelativeLayout layout_try_audioBtn;
    ImageView BackButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_try__audio, container, false);
        BackButton = v.findViewById(R.id.BackInSignUp);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .add(R.id.try_audio_container,new HomeFragment())
                        .addToBackStack(null)
                        .commit();
                getActivity().finish();
            }
        });
        layout_try_audioBtn = v.findViewById(R.id.layout_audio_btn);
        layout_try_audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }
}