package com.scripttube.android.ScriptTube.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scripttube.android.ScriptTube.Adapters.ChatListAdapter;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.model.ChatListModel;

import java.util.ArrayList;

public class ChatListFragment extends Fragment implements ChatListAdapter.ChatListListener {
    private RecyclerView chatListRecyclerV;
    private ChatListAdapter adapter;
    ArrayList<ChatListModel> chatListModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        inItWidgets(view);

        chatListRecyclerV.setAdapter(adapter);
        setupDummyChats();
        adapter.updateChatList(chatListModels);
        return view;
    }

    private void setupDummyChats() {
        chatListModels.add(new ChatListModel("Paul Walker", "Bye Bye"));
        chatListModels.add(new ChatListModel("James Cameron", "Hey you..."));
        chatListModels.add(new ChatListModel("Vin Diesel", "Hello, what's going on"));
        chatListModels.add(new ChatListModel("Travis Barker", "Are you ok?"));
        chatListModels.add(new ChatListModel("Kate Rose", "Hi, Buddy"));
        chatListModels.add(new ChatListModel("Rick Owens", "Good Bye..."));


    }

    private void inItWidgets(View view) {
        chatListRecyclerV = view.findViewById(R.id.chatListRV);
        chatListRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));

        chatListModels = new ArrayList<>();
        adapter = new ChatListAdapter(getActivity(), this);

    }

    @Override
    public void chatSelected() {
        ((Home) requireActivity()).fragmentManagerHelper.replace(new ChatFragment(), true);
    }
}
