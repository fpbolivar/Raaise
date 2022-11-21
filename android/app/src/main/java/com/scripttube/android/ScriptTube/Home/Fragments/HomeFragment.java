package com.scripttube.android.ScriptTube.Home.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scripttube.android.ScriptTube.Adapters.HomeFollowingAdapter;
import com.scripttube.android.ScriptTube.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    RecyclerView RecyclerViewInHome;
    HomeFollowingAdapter homeFollowingAdapter;
    Context context;
    List<String> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Initialization(v);
        clickListeners();
        return v;
    }

    private void clickListeners() {
    }

    private void Initialization(View v) {
        context = getActivity().getApplicationContext();
        RecyclerViewInHome = v.findViewById(R.id.RecyclerViewInHome);
        RecyclerViewInHome.setHasFixedSize(true);
        RecyclerViewInHome.setLayoutManager(new GridLayoutManager(context,1));
        list.add("M");
        list.add("M");
        list.add("M");
        list.add("M");
        list.add("M");
        list.add("M");
        list.add("M");
        homeFollowingAdapter = new HomeFollowingAdapter(context,list);
        RecyclerViewInHome.setAdapter(homeFollowingAdapter);

    }
}