package com.raaise.android.Activity.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.raaise.android.Activity.Credentials.Login;
import com.raaise.android.R;

public class Welcome2 extends Fragment {
    View v;
    ImageView NextInIntroduction2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_welcome2, container, false);
        Initialization();
        clickListeners();
        return v;
    }

    private void clickListeners() {
        NextInIntroduction2.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), Login.class));
            ((WelcomeActivity) requireActivity()).Finishing();
        });

    }

    private void Initialization() {
        NextInIntroduction2 = v.findViewById(R.id.NextInIntroduction2);
    }
}