package com.raaise.android.Home.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.Adapters.ChatAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.SharVideoModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.PaginationScrollListener;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.model.ChatModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ChatFragment extends Fragment implements View.OnClickListener, ChatAdapter.ChatListener {

    public boolean ShowHideLayout = true;
    public boolean isLoading = false;
    public boolean HasMore = false;
    TextView totalDonationTV, chatPersonName, ShortBioOfChatUser, chatPersonNameName;
    ImageView chatPersonIV, send_chat_btn;
    RecyclerView chatRV;
    ChatAdapter chatAdapter;
    EditText send_chat_et;
    List<ChatModel.Data> chatModels;
    ImageView backBtn;
    String ReceiverId, SenderId, UserImageLink, Username, ShortBio, VideoId, Slug;
    View view;
    public Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (data.getString("messageType").equalsIgnoreCase("Text")) {
                                chatModels.add(0, new Gson().fromJson(String.valueOf(data), ChatModel.Data.class));
                                chatAdapter.notifyDataSetChanged();
                                chatAdapter.notifyItemInserted(chatModels.size());
                                chatRV.scrollToPosition(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                Prompt.SnackBar(view, e.getMessage());

            }
        }
    };
    public Emitter.Listener OnDelete = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data = (String) args[0];
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            for (int i = 0; i < chatModels.size(); i++) {
                                if (chatModels.get(i).get_id().equalsIgnoreCase(data)) {
                                    chatModels.remove(i);
                                    chatAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                Prompt.SnackBar(view, e.getMessage());

            }
        }
    };
    LinearLayoutManager manager;
    int page = 1;
    LinearLayout ShowHideProfile;
    ApiManager apiManager = App.getApiManager();
    int From;
    private Socket mSocket;

    public ChatFragment() {
    }


    public ChatFragment(String slug, String receiverId, String senderId, String userImageLink, String username, String shortBio, int from) {
        ReceiverId = receiverId;
        SenderId = senderId;
        UserImageLink = userImageLink;
        Username = username;
        ShortBio = shortBio;
        From = from;
        Slug = slug;
    }

    public ChatFragment(String slug, String receiverId, String senderId, String userImageLink, String username, String shortBio, int from, String videoId) {
        ReceiverId = receiverId;
        SenderId = senderId;
        UserImageLink = userImageLink;
        Username = username;
        ShortBio = shortBio;
        From = from;
        VideoId = videoId;
        Slug = slug;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        if (From == 1) {
            ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        }


        inItWidgets(view);
        App app = (App) getActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.connect();

        mSocket.on(Slug, onLogin);
        mSocket.on("delete-chat" + Slug, OnDelete);

        send_chat_btn.setOnClickListener(view1 -> {
            EmitApi();
        });

        ShowHideProfile.setOnClickListener(view1 -> {
            startActivity(new Intent(view.getContext(), OtherUserProfileActivity.class).putExtra("UserIdForProfile", ReceiverId).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });
        if (VideoId != null) {
            ShareVideo();
        } else {
            HitChatApi(page, 20);
        }

        chatRV.addOnScrollListener(new PaginationScrollListener(manager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                HitChatApi(page, 20);
            }

            @Override
            public boolean isLastPage() {
                return HasMore;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        return view;
    }


    private void EmitApi() {
        if (!send_chat_et.getText().toString().trim().equalsIgnoreCase("")) {


            try {
                JSONObject json = new JSONObject();
                json.put("message", send_chat_et.getText().toString().trim());
                json.put("userId", SenderId);
                json.put("senderId", SenderId);
                json.put("receiverId", ReceiverId);
                json.put("messageType", "Text");
                send_chat_et.setText("");


                mSocket.emit("send-message", json);
            } catch (Exception e) {
                Prompt.SnackBar(view, e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Slug, onLogin);
        mSocket.off("delete-chat" + Slug, OnDelete);
    }

    public void ShareVideo() {
        Dialogs.createProgressDialog(view.getContext());
        SharVideoModel model = new SharVideoModel(ReceiverId, SenderId, VideoId);
        apiManager.ShareVideo(Prefs.GetBearerToken(view.getContext()), model, new DataCallback<SharVideoModel>() {
            @Override
            public void onSuccess(SharVideoModel sharVideoModel) {
                Dialogs.HideProgressDialog();
                HitChatApi(page, 20);
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(view, serverError.getErrorMsg());
            }
        });
    }


    private void HitChatApi(int Page, int Limit) {
        ChatModel model = new ChatModel(Slug, String.valueOf(Page), String.valueOf(Limit));
        apiManager.GetSingleChat(Prefs.GetBearerToken(view.getContext()), model, new DataCallback<ChatModel>() {
            @Override
            public void onSuccess(ChatModel chatModel) {

                isLoading = false;
                if (chatModel.getData().size() != 0) {
                    if (page == 1) {
                        chatModels.addAll(chatModel.getData());
                        Collections.reverse(chatModels);
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < chatModel.getData().size(); i++) {
                            chatModels.add(chatModel.getData().get(i));
                            chatAdapter.notifyDataSetChanged();
                        }
                    }
                    if (page == 1) {
                        chatRV.scrollToPosition(0);
                    }
                    page++;
                } else {
                    HasMore = true;
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());
            }
        });
    }


    private void inItWidgets(View view) {
        chatPersonNameName = view.findViewById(R.id.chatPersonNameName);
        ShowHideProfile = view.findViewById(R.id.ShowHideProfile);
        chatPersonName = view.findViewById(R.id.chatPersonName);
        send_chat_btn = view.findViewById(R.id.send_chat_btn);
        totalDonationTV = view.findViewById(R.id.totalDonationHeading);
        chatPersonIV = view.findViewById(R.id.chatPersonIV);
        chatRV = view.findViewById(R.id.chatRV);
        backBtn = view.findViewById(R.id.backBtn);
        send_chat_et = view.findViewById(R.id.send_chat_et);

        chatModels = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        chatRV.setLayoutManager(manager);
        chatAdapter = new ChatAdapter(getActivity(), chatModels, ChatFragment.this);
        chatRV.setAdapter(chatAdapter);
        backBtn.setOnClickListener(this);
        chatPersonName.setText("@" + Username);
        chatPersonNameName.setText(Username);
        Log.i("baseUrl", "inItWidgets: " + Prefs.GetBaseUrl(requireContext()) + UserImageLink);
        if (UserImageLink.contains("https://")){
            Glide.with(chatPersonIV)
                    .load(UserImageLink)
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(chatPersonIV);
        } else {
            Glide.with(chatPersonIV)
                    .load(Prefs.GetBaseUrl(requireContext()) + UserImageLink)
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(chatPersonIV);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                backBtnClicked();
                break;
        }
    }

    private void backBtnClicked() {
        requireActivity().onBackPressed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (From == 1) {
            ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void DeleteMessage(String MessageId, String ChatSlug) {
        try {

            JSONObject json = new JSONObject();
            json.put("id", MessageId);
            json.put("slug", ChatSlug);

            mSocket.emit("delete-chat", json);
        } catch (Exception e) {
            Prompt.SnackBar(view, e.getMessage());

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.fromTryAudio){
            try {
                for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof HomeFragment){
                        Log.i("fragment", "onResume: ");
                    } else
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.FragmentContainer, new CameraFragment())
                        .commit();
            } catch (Exception e){
                Log.i("onResumeMusic", "onResume: " + e.getMessage());
            }
        }
    }
}
