package com.scripttube.android.ScriptTube.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scripttube.android.ScriptTube.Adapters.MyContentAdapter;
import com.scripttube.android.ScriptTube.R;

public class MyContentFragment extends Fragment {
    private RecyclerView myContentRV;
    private MyContentAdapter myContentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_content, container, false);
        inItWidgets(v);
        myContentRV.setAdapter(myContentAdapter);
        myContentRV.setAdapter(myContentAdapter);
        return v;
    }

    private void inItWidgets(View view) {
        myContentRV = view.findViewById(R.id.myContentRV);
        myContentRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        myContentAdapter = new MyContentAdapter(getActivity());
    }
}
