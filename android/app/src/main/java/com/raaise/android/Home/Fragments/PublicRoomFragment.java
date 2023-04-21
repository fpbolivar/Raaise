package com.raaise.android.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.Adapters.RoomMemberAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.JoinRoomRes;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.LiveRoomTokenData;
import com.raaise.android.model.RoomSlug;

public class PublicRoomFragment extends Fragment {

    private ApiManager apiManager = App.getApiManager();
    private String roomStr;
    private LiveRoomData liveRoomData;

    TextView usersLabel;
    TextView joinRoomBTN;
    ImageView roomLogoIV;
    TextView roomNameTV;
    TextView roomDescTV;
    ImageView backBTN;
    RecyclerView publicMembersRV;
    RoomMemberAdapter roomMemberAdapter;

    public PublicRoomFragment(String liveStr){
        this.roomStr = liveStr;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_room, container, false);

        inItWidgets(view);
        joinRoomBTN.setOnClickListener(view1 -> joinRoom());
        backBTN.setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }

    private void joinRoom() {
        Dialogs.createProgressDialog(getContext());
        RoomSlug slug = new RoomSlug(liveRoomData.slug);
        apiManager.joinLiveRoom(Prefs.GetBearerToken(getContext()), slug, new DataCallback<JoinRoomRes>() {
            @Override
            public void onSuccess(JoinRoomRes joinRoomRes) {
                Dialogs.HideProgressDialog();
                Toast.makeText(getContext(), "You joined this room", Toast.LENGTH_SHORT).show();
                joinRoomBTN.setVisibility(View.GONE);

                getLiveRoom();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Toast.makeText(getContext(), serverError.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLiveRoom() {
        Dialogs.createProgressDialog(getContext());
        RoomSlug slug = new RoomSlug(liveRoomData.slug);
        apiManager.getRoomBySlug(Prefs.GetBearerToken(getContext()), slug, new DataCallback<LiveRoomTokenData>() {
            @Override
            public void onSuccess(LiveRoomTokenData data) {
                Dialogs.HideProgressDialog();
                LiveRoomTokenData liveRoomTokenData = data;
                setupDataByToken(data);
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
            }
        });
    }

    private void setupDataByToken(LiveRoomTokenData data) {
        Glide.with(getActivity())
                .load(Prefs.GetBaseUrl(getContext()) + data.logo)
                .placeholder(R.drawable.placeholder)
                .into(roomLogoIV);

        roomNameTV.setText(data.title);
        roomDescTV.setText(data.description);
        roomMemberAdapter.setList(data.getMemberIds());

        if (data.memberIds.size() == 0){
            usersLabel.setVisibility(View.GONE);
        } else usersLabel.setVisibility(View.VISIBLE);
    }

    private void inItWidgets(View view) {
        liveRoomData = new Gson().fromJson(roomStr, LiveRoomData.class);
        joinRoomBTN = view.findViewById(R.id.join_room_btn);
        usersLabel = view.findViewById(R.id.avable_users_label);
        roomLogoIV = view.findViewById(R.id.room_logo_iv);
        roomNameTV = view.findViewById(R.id.room_name_tv);
        backBTN = view.findViewById(R.id.backBtn);
        roomDescTV = view.findViewById(R.id.description_room_tv);

        roomMemberAdapter = new RoomMemberAdapter(getContext());
        publicMembersRV = view.findViewById(R.id.room_members_rv);
        publicMembersRV.setAdapter(roomMemberAdapter);

        if (liveRoomData != null)
            setupData(liveRoomData);
    }

    private void setupData(LiveRoomData liveRoomData) {
        Glide.with(getActivity())
                .load(Prefs.GetBaseUrl(getContext()) + liveRoomData.logo)
                .placeholder(R.drawable.placeholder)
                .into(roomLogoIV);

        roomNameTV.setText(liveRoomData.title);
        roomDescTV.setText(liveRoomData.description);
        roomMemberAdapter.setList(liveRoomData.getMemberIds());

        if (liveRoomData.memberIds.size() == 0){
            usersLabel.setVisibility(View.GONE);
        } else usersLabel.setVisibility(View.VISIBLE);
    }
}
