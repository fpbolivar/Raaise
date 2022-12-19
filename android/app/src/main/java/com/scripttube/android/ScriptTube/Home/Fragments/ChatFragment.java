package com.scripttube.android.ScriptTube.Home.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.Adapters.ChatAdapter;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.model.ChatModel;

import java.util.ArrayList;

public class ChatFragment extends Fragment implements View.OnClickListener {
    TextView totalDonationTV;
    ImageView chatPersonIV;
    RecyclerView chatRV;
    ChatAdapter chatAdapter;
    ArrayList<ChatModel> chatModels;
    ImageView backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        inItWidgets(view);
        Glide.with(chatPersonIV)
                .load(R.drawable.delete_it)
                .circleCrop()
                .into(chatPersonIV);

        setupDummyChat();
        chatRV.setAdapter(chatAdapter);
        backBtn.setOnClickListener(this);

        chatAdapter.updateChats(chatModels);
        totalDonationTV.setText(R.string.total_donated);
        return view;
    }

    private void setupDummyChat() {

        chatModels.add(new ChatModel("Hi There", true, "15:00"));
        chatModels.add(new ChatModel("Hey", false, "15:01"));
        chatModels.add(new ChatModel("How u doing, nowadays??", true, "15:03"));
        chatModels.add(new ChatModel("Everything's alright.. u tell :)", false, "15:07"));
        chatModels.add(new ChatModel("Good to hear that :)", true, "15:30"));
    }

    private void inItWidgets(View view) {
        totalDonationTV = view.findViewById(R.id.totalDonationHeading);
        chatPersonIV = view.findViewById(R.id.chatPersonIV);
        chatRV = view.findViewById(R.id.chatRV);
        backBtn = view.findViewById(R.id.backBtn);

        chatRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatAdapter = new ChatAdapter(getActivity());
        chatModels = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                requireActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
    }
}
