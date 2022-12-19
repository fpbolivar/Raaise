package com.scripttube.android.ScriptTube.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.scripttube.android.ScriptTube.R;


public class SearchFragment extends Fragment {
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        inItWidgets(view);

        return view;
    }

    private void inItWidgets(View view) {
        searchView = view.findViewById(R.id.search_view);
    }
}