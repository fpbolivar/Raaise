package com.raaise.android.Home.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.Adapters.RoomMemberAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MeetingActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.textPaint.TextPaint;
import com.raaise.android.model.JoinRoomRes;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.LiveRoomTokenData;
import com.raaise.android.model.RoomSlug;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PublicRoomFragment extends Fragment implements RoomMemberAdapter.RoomUserListener {

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
    LinearLayout callOptionsContainer;

    LinearLayout webcamBG;
    ImageView webcamIV;
    LinearLayout micBG;
    ImageView micIV;
    CardView btnWebcam;
    CardView btnMic;
    private boolean CAM_STATUS = true;
    private boolean MIC_STATUS = true;
    private boolean android10 = true;

    private CardView initiateCallBtn;
    private LiveRoomTokenData liveRoomTokenData;

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
        initiateCallBtn.setOnClickListener(view13 -> {
            startActivity(new Intent(getActivity(), MeetingActivity.class).putExtra("roomName", liveRoomTokenData.title)
                    .putExtra("token", liveRoomTokenData.token)
                    .putExtra("roomID", liveRoomTokenData.roomId)
                    .putExtra("roomSlug", liveRoomTokenData.slug)
                    .putExtra("micStatus", MIC_STATUS)
                    .putExtra("camStatus", CAM_STATUS));

            MIC_STATUS = true;
            CAM_STATUS = true;
            webcamBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            webcamIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.camera_on));
            micBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            micIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mic_on));
        });

        btnWebcam.setOnClickListener(v -> {
            if (CAM_STATUS) {
                webcamBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.meeting_grey));
                webcamIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.camera_off));
            } else {
                webcamBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                webcamIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.camera_on));
            }
            CAM_STATUS=!CAM_STATUS;
            Log.i("camStatus", "onClick: " + CAM_STATUS);
        });

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MIC_STATUS) {

                    micBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.meeting_grey));
                    micIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mic_off));
                } else {
                    micBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    micIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mic_on));
                }
                MIC_STATUS=!MIC_STATUS;
            }
        });

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
                liveRoomTokenData = data;
                setupDataByToken(data);
                callOptionsContainer.setVisibility(View.VISIBLE);
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
        if (data.scheduleType != null && data.scheduleDateTime != null){
            if (data.scheduleType.equals("schedule_live_room")){
                String dateTimeString = data.scheduleDateTime;

                // Convert the date and time string to a Date object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dateTime = dateFormat.parse(dateTimeString);
                    Date now = new Date();

                    // Compare the two dates
                    int result = dateTime.compareTo(now);
                    if (result < 0) {
                        // The specified date and time is in the past
                        Log.i("dateTime", "The specified date and time is in the past.");
                    } else if (result == 0) {
                        // The specified date and time is the same as the current date and time
                        Log.i("dateTime", "The specified date and time is the same as the current date and time.");
                    } else {
                        // The specified date and time is in the future
                        initiateCallBtn.setVisibility(View.GONE);
                        Log.i("dateTime", "The specified date and time is in the future.");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void inItWidgets(View view) {
        btnWebcam = view.findViewById(R.id.btnWebcam);
        btnMic = view.findViewById(R.id.btnMic);
        webcamBG = view.findViewById(R.id.webcam_bg);
        webcamIV = view.findViewById(R.id.webcam_iv);
        micBG = view.findViewById(R.id.mic_bg);
        micIV = view.findViewById(R.id.mic_iv);
        callOptionsContainer = view.findViewById(R.id.call_options_container);
        initiateCallBtn = view.findViewById(R.id.initiate_call_btn);
        liveRoomData = new Gson().fromJson(roomStr, LiveRoomData.class);
        joinRoomBTN = view.findViewById(R.id.join_room_btn);
        TextPaint.getGradientColor(joinRoomBTN);
        usersLabel = view.findViewById(R.id.avable_users_label);
        TextPaint.getGradientColor(usersLabel);
        roomLogoIV = view.findViewById(R.id.room_logo_iv);
        roomNameTV = view.findViewById(R.id.room_name_tv);
        backBTN = view.findViewById(R.id.backBtn);
        roomDescTV = view.findViewById(R.id.description_room_tv);

        roomMemberAdapter = new RoomMemberAdapter(getContext(), PublicRoomFragment.this);
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

    @Override
    public void UserSelected(String id, String userName) {
        startActivity(new Intent(getContext(), OtherUserProfileActivity.class).putExtra("UserIdForProfile", id).putExtra("UserNameForProfile", userName).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Code for Android 10
            while (android10){
                getLiveRoom();
                android10 = false;
            }
        } else {
            getLiveRoom();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        android10 = true;
    }
}
