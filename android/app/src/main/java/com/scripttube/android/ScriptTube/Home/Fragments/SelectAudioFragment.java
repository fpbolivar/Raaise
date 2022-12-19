package com.scripttube.android.ScriptTube.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scripttube.android.ScriptTube.Adapters.SelectAudioAdapter;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.model.SelectAudio;

import java.util.ArrayList;

public class SelectAudioFragment extends Fragment implements View.OnClickListener {

    SelectAudioAdapter audioAdapter;
    RecyclerView audioRecyclerView;
    ImageView backBtn;
    private ArrayList<SelectAudio> selectAudios = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_audio, container, false);
        ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        inItwidgets(v);
        audioRecyclerView.setAdapter(audioAdapter);
        audioAdapter.updateAudios(selectAudios);
        backBtn.setOnClickListener(this);
        return v;
    }

    private void inItwidgets(View v) {
        backBtn = v.findViewById(R.id.backBtn);
        audioRecyclerView = v.findViewById(R.id.audioRV);
        audioAdapter = new SelectAudioAdapter(getActivity());
        audioRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Set Dummy Content
        setDummyContent();
    }

    private void setDummyContent() {
        selectAudios.add(new SelectAudio("Take Me Back", "20k followers"));
        selectAudios.add(new SelectAudio("Love Nwanetti", "10k followers"));
        selectAudios.add(new SelectAudio("I am an albatroz", "50k followers"));
        selectAudios.add(new SelectAudio("Whether you go", "3k followers"));
        selectAudios.add(new SelectAudio("Take Out", "75k followers"));
        selectAudios.add(new SelectAudio("Electro folk", "18k followers"));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                requireActivity().onBackPressed();
                break;
        }
    }
}
