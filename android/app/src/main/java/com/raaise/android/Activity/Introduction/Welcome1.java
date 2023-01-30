package com.raaise.android.Activity.Introduction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.raaise.android.R;


public class Welcome1 extends Fragment {
    ImageView NextInIntroduction1;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_welcome1, container, false);
        Initialization();
        clickListeners();
        return v;
    }

    private void clickListeners() {
        NextInIntroduction1.setOnClickListener(view -> {
            ((WelcomeActivity) requireActivity()).SelectItem2();
        });
    }

    private void Initialization() {
        NextInIntroduction1 = v.findViewById(R.id.NextInIntroduction1);
    }
}