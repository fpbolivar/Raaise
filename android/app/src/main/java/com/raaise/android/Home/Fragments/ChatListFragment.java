package com.raaise.android.Home.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.ChatListAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.model.ChatListModel;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment implements ChatListAdapter.ChatListListener {
    RecyclerView chatListRecyclerV;
    ChatListAdapter adapter;
    List<ChatListModel.Data> chatListModels;
    ApiManager apiManager = App.getApiManager();
    View view;
    SearchView SearchViewInChatListMain;
    RelativeLayout NoChatFound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        inItWidgets(view);
        GetUserListApi("");
        SearchViewInChatListMain.setQueryHint(Html.fromHtml("<font color = #d6d6d6>" + "Search Users" + "</font>"));
        SearchViewInChatListMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                GetUserListApi(newText);
                return false;
            }
        });

        return view;
    }

    private void GetUserListApi(String search) {
        ChatListModel model = new ChatListModel(search);
        apiManager.GetUserChatList(Prefs.GetBearerToken(view.getContext()), model, new DataCallback<ChatListModel>() {
            @Override
            public void onSuccess(ChatListModel chatListModel) {
                if (chatListModel.getData().size() == 0) {
                    NoChatFound.setVisibility(View.VISIBLE);
                    chatListRecyclerV.setVisibility(View.GONE);
                } else {
                    chatListModels.clear();
                    NoChatFound.setVisibility(View.GONE);
                    chatListRecyclerV.setVisibility(View.VISIBLE);
                    chatListModels.addAll(chatListModel.getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());

            }
        });


    }

    private void inItWidgets(View view) {
        SearchViewInChatListMain = view.findViewById(R.id.SearchViewInChatListMain);
        NoChatFound = view.findViewById(R.id.NoChatFound);
        chatListRecyclerV = view.findViewById(R.id.chatListRV);
        chatListRecyclerV.setHasFixedSize(true);
        chatListRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatListModels = new ArrayList<>();
        adapter = new ChatListAdapter(getActivity(), this, chatListModels);
        chatListRecyclerV.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((Home) requireActivity()).CheckNotificationCount();
    }

    @Override
    public void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String ShortBio) {
        ((Home) requireActivity()).fragmentManagerHelper.replace(new ChatFragment(Slug, ReceiverId, SenderId, UserImageLink, Username, ShortBio, 1), true);
    }
}
