package com.raaise.android.Home.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.Adapters.RoomMemberAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.Home.MeetingActivity;
import com.raaise.android.MainActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.JoinRoomRes;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.LiveRoomTokenData;
import com.raaise.android.model.RoomSlug;

import java.util.Objects;


public class RoomFragment extends Fragment {

    // Layout files
    CardView initiateCallBTN;
    LinearLayout callBG;
    ImageView editRoomBtn;
    TextView leaveRoomBTN;
    ImageView backBTN;
    ImageView roomLogoIV;
    TextView roomNameTV;
    TextView roomDescTV;
    RecyclerView roomMemebersRV;
    RoomMemberAdapter roomMemberAdapter;

    ApiManager apiManager = App.getApiManager();

    // Room data from previous screen
    String liveStr;
    LiveRoomData liveRoomData;
    LiveRoomTokenData liveRoomTokenData;

    public RoomFragment(String liveRoomStr){
        this.liveStr = liveRoomStr;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        inItWidgets(view);

        ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        liveRoomData = new Gson().fromJson(liveStr, LiveRoomData.class);
        if (liveRoomData != null){
            Log.i("roomSlug", "onCreateView: " + liveRoomData.slug);
            getLiveRoom();
        }

        backBTN.setOnClickListener(view1 -> requireActivity().onBackPressed());
        editRoomBtn.setOnClickListener(view12 -> {
            Log.i("roomCred", "onClick:User ID " + Prefs.GetUserID(getContext()));
            Log.i("roomCred", "onClick:Host ID " + liveRoomData.hostId._id);
            ((Home) requireActivity()).fragmentManagerHelper.replace(new EditRoomFragment(liveStr), true);
        });

        leaveRoomBTN.setOnClickListener(v -> showLeaveRoomDialog());

        initiateCallBTN.setOnClickListener(v -> startActivity(new Intent(getActivity(), MeetingActivity.class).putExtra("roomName", liveRoomTokenData.title)
                .putExtra("token", liveRoomTokenData.token)
                .putExtra("roomID", liveRoomTokenData.roomId)
                .putExtra("roomSlug", liveRoomTokenData.slug)));

        return view;
    }

    private void showLeaveRoomDialog() {
        Dialog leaveRoomDialog = new Dialog(getContext());
        leaveRoomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        leaveRoomDialog.setContentView(R.layout.confirm_leaving_dialog);
        TextView leaveRoomBtn = leaveRoomDialog.findViewById(R.id.leave_room_tv);
        TextView cancel = leaveRoomDialog.findViewById(R.id.cancel_dialog_tv);
        leaveRoomBtn.setOnClickListener(v -> {
            leaveRoomDialog.dismiss();
            leaveRoom();
        });
        cancel.setOnClickListener(v -> leaveRoomDialog.dismiss());

        leaveRoomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leaveRoomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        leaveRoomDialog.getWindow().setGravity(Gravity.CENTER);
        leaveRoomDialog.setCancelable(false);
        leaveRoomDialog.show();
    }

    private void leaveRoom() {
        Dialogs.createProgressDialog(getContext());
        RoomSlug slug = new RoomSlug(liveRoomData.slug);
        apiManager.leaveLiveRoom(Prefs.GetBearerToken(getContext()), slug, new DataCallback<JoinRoomRes>() {
            @Override
            public void onSuccess(JoinRoomRes joinRoomRes) {
                Dialogs.HideProgressDialog();
                requireActivity().onBackPressed();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Toast.makeText(getContext(), serverError.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupData(LiveRoomTokenData liveRoomData) {
        Glide.with(getActivity())
                .load(Prefs.GetBaseUrl(getContext()) + liveRoomData.logo)
                .placeholder(R.drawable.placeholder)
                .into(roomLogoIV);

        roomNameTV.setText(liveRoomData.title);
        roomDescTV.setText(liveRoomData.description);
        roomMemberAdapter.setList(liveRoomData.getMemberIds());
    }

    private void inItWidgets(View view) {
        initiateCallBTN = view.findViewById(R.id.initiate_call_btn);
        callBG = view.findViewById(R.id.call_bg);
        editRoomBtn = view.findViewById(R.id.edit_room_btn);
        leaveRoomBTN = view.findViewById(R.id.leave_room_btn);
        roomMemebersRV = view.findViewById(R.id.room_members_rv);
        roomMemberAdapter = new RoomMemberAdapter(getContext());
        roomMemebersRV.setLayoutManager(new LinearLayoutManager(getContext()));
        roomMemebersRV.setAdapter(roomMemberAdapter);
        backBTN = view.findViewById(R.id.backBtn);
        roomLogoIV = view.findViewById(R.id.room_logo_iv);
        roomNameTV = view.findViewById(R.id.room_name_tv);
        roomDescTV = view.findViewById(R.id.description_room_tv);
    }

    @Override
    public void onDetach() {
        super.onDetach();
            ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("roomResume", "onResume: Resumed");
        if (App.fromEditRoom){
            App.fromEditRoom = false;
            getLiveRoom();

        }
    }

    private void getLiveRoom() {
        Dialogs.createProgressDialog(getContext());
        RoomSlug model = new RoomSlug(liveRoomData.slug);
        apiManager.getRoomBySlug(Prefs.GetBearerToken(getContext()), model, new DataCallback<LiveRoomTokenData>() {
            @Override
            public void onSuccess(LiveRoomTokenData roomData) {
                Dialogs.HideProgressDialog();
                liveRoomTokenData = roomData;
                setupData(liveRoomTokenData);
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
            }
        });
    }

}
