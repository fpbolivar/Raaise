package com.raaise.android.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.raaise.android.Adapters.LiveChatAdapter;
import com.raaise.android.Adapters.ParticipantAdapter;
import com.raaise.android.Adapters.SecondaryParticipantAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.MainActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.GetLiveRoomChatBody;
import com.raaise.android.model.LiveChatData;
import com.raaise.android.model.LiveRoomChat;
import com.raaise.android.model.SendChatBody;

import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import live.videosdk.rtc.android.Meeting;
import live.videosdk.rtc.android.Participant;
import live.videosdk.rtc.android.Stream;
import live.videosdk.rtc.android.VideoSDK;
import live.videosdk.rtc.android.VideoView;
import live.videosdk.rtc.android.lib.PubSubMessage;
import live.videosdk.rtc.android.listeners.MeetingEventListener;
import live.videosdk.rtc.android.listeners.ParticipantEventListener;
import live.videosdk.rtc.android.listeners.PubSubMessageListener;
import live.videosdk.rtc.android.model.PubSubPublishOptions;

public class MeetingActivity extends AppCompatActivity implements ParticipantAdapter.SecondaryListener {
    // declare the variables we will be using to handle the meeting
    private Meeting meeting;
    private boolean micEnabled;
    private boolean webcamEnabled;
    ParticipantAdapter adapter;

    private ApiManager apiManager = App.getApiManager();

    LinearLayout micBG;
    ImageView micIV;
    LinearLayout webcamBG;
    ImageView webcamIV;
    private LiveChatAdapter liveChatAdapter;
    private RecyclerView liveCHatRV;
    private EditText liveChatET;
    private ImageView chatSendBTN;
    private PubSubMessageListener chatListener;

    private VideoView userVideoView;
    private String roomSlug;
//    RecyclerView secondaryParticiepants;
    LinearLayout bottomSheet;
    private GestureDetector gestureDetector;
    private boolean IN_FRONT = true;
    private RecyclerView rvParticipants;
    private SecondaryParticipantAdapter secondaryParticipantAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_new);
        final String token = getIntent().getStringExtra("token");
        final String meetingId = getIntent().getStringExtra("roomID");
        final String roomName = getIntent().getStringExtra("roomName");
        roomSlug = getIntent().getStringExtra("roomSlug");
        micEnabled = getIntent().getBooleanExtra("micStatus", true);
        webcamEnabled = getIntent().getBooleanExtra("camStatus", true);
        final String participantName = Prefs.getUserName(MeetingActivity.this);
        // 1. Configuration VideoSDK with Token
        VideoSDK.config(token);
        Log.i("roomSlug", "onCreate:senderID " + Prefs.GetUserID(MeetingActivity.this));

        // 2. Initialize VideoSDK Meeting
        meeting = VideoSDK.initMeeting(
                MeetingActivity.this, meetingId, participantName,
                micEnabled, webcamEnabled,null, null, null);
        inItWidgets();
        if (micEnabled) {
            micBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.white));
            micIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.mic_on));
        } else {
            micBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.meeting_grey));
            micIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.mic_off));
        }

        if (webcamEnabled) {
            webcamBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.white));
            webcamIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.camera_on));
        } else {
            webcamBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.meeting_grey));
            webcamIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.camera_off));
        }

        // 3. Add event listener for listening upcoming events
        meeting.addEventListener(meetingEventListener);
//        meeting.addEventListener(meetingEvent);

        //4. Join VideoSDK Meeting
        meeting.join();


        // actions
        setActionListeners();

        rvParticipants = findViewById(R.id.rvParticipants);
        rvParticipants.setLayoutManager(new LinearLayoutManager(MeetingActivity.this));
        adapter = new ParticipantAdapter(meeting, MeetingActivity.this, MeetingActivity.this);
        rvParticipants.setAdapter(adapter);

        final RecyclerView secondaryParticiepants = findViewById(R.id.rvParticipants2);
        secondaryParticiepants.setLayoutManager(new GridLayoutManager(MeetingActivity.this, 5));
        secondaryParticipantAdapter = new SecondaryParticipantAdapter(meeting);
        secondaryParticiepants.setAdapter(secondaryParticipantAdapter);

        Participant user = meeting.getLocalParticipant();
        for (Map.Entry<String, Stream> entry : user.getStreams().entrySet()) {
            Stream stream = entry.getValue();
            if (stream.getKind().equalsIgnoreCase("video")) {
                userVideoView.setVisibility(View.VISIBLE);
                VideoTrack videoTrack = (VideoTrack) stream.getTrack();
                userVideoView.addTrack(videoTrack);
                break;
            }
        }
        // add Listener to the participant which will update start or stop the video stream of that participant
        user.addEventListener(new ParticipantEventListener() {
            @Override
            public void onStreamEnabled(Stream stream) {
                if (stream.getKind().equalsIgnoreCase("video")) {
                    userVideoView.setVisibility(View.VISIBLE);
                    VideoTrack videoTrack = (VideoTrack) stream.getTrack();
                    userVideoView.addTrack(videoTrack);
                }
            }

            @Override
            public void onStreamDisabled(Stream stream) {
                if (stream.getKind().equalsIgnoreCase("video")) {
                    userVideoView.removeTrack();
//                    userVideoView.setVisibility(View.GONE);
                }
            }
        });
        GetLiveRoomChatBody body = new GetLiveRoomChatBody(Prefs.GetUserID(MeetingActivity.this), roomSlug, "1", "50");
        apiManager.getLiveChat(Prefs.GetBearerToken(MeetingActivity.this), body, new DataCallback<ArrayList<LiveRoomChat.ChatData>>() {
            @Override
            public void onSuccess(ArrayList<LiveRoomChat.ChatData> chatData) {
                Log.i("liveChats", "onSuccess: " + new Gson().toJson(chatData));
                try {
                    liveChatAdapter.setList(chatData);
                    liveCHatRV.scrollToPosition(liveChatAdapter.getItemCount() - 1);
                } catch (Exception e){

                }
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });

        chatSendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liveChatET.getText().toString().trim().equals("")){
                    Toast.makeText(MeetingActivity.this, "Please type something", Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = liveChatET.getText().toString();
                PubSubPublishOptions publishOptions = new PubSubPublishOptions();
                publishOptions.setPersist(true);

                LiveChatData liveChatData = new LiveChatData(Prefs.getUserImage(MeetingActivity.this),
                        Prefs.getUserName(MeetingActivity.this),
                        message);

                meeting.pubSub.publish("CHAT", new Gson().toJson(liveChatData), publishOptions);
                liveChatET.setText("");

                sendChatToDB(Prefs.GetUserID(MeetingActivity.this), roomSlug, message);
            }
        });

        userVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

    }

    private void sendChatToDB(String userID, String roomSlug, String message) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendChatBody chatBody = new SendChatBody(userID, roomSlug, message);
                    apiManager.sendLiveChat(Prefs.GetBearerToken(MeetingActivity.this), chatBody);
                }
            }).start();
        } catch (Exception e){

        }
    }

    private void inItWidgets() {

        gestureDetector = new GestureDetector(this, new MyGestureListener());
        bottomSheet = findViewById(R.id.bottom_participents);

        userVideoView = findViewById(R.id.user_video_view);
        userVideoView.setMirror(true);
        liveCHatRV = findViewById(R.id.live_chat_rv);
        liveCHatRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        liveChatAdapter = new LiveChatAdapter(this);
        liveChatET = findViewById(R.id.send_chat_et);
        chatSendBTN = findViewById(R.id.send_chat_btn);
        micBG = findViewById(R.id.mic_bg);
        micIV = findViewById(R.id.mic_iv);
        webcamBG = findViewById(R.id.webcam_bg);
        webcamIV = findViewById(R.id.webcam_iv);
    }

//    private final MeetingEventListener meetingEvent = new MeetingEventListener() {
//        @Override
//        public void onSpeakerChanged(String participantId) {
//            Toast.makeText(MeetingActivity.this, "Active Speaker participantId" + participantId, Toast.LENGTH_SHORT).show();
//            super.onSpeakerChanged(participantId);
//        }
//    };

    // creating the MeetingEventListener
    private final MeetingEventListener meetingEventListener = new MeetingEventListener() {
        @Override
        public void onMeetingJoined() {
            liveCHatRV.setAdapter(liveChatAdapter);
            chatListener = new PubSubMessageListener() {
                @Override
                public void onMessageReceived(PubSubMessage pubSubMessage) {
                    liveChatAdapter.addChat(pubSubMessage.getMessage());
                    liveCHatRV.scrollToPosition(liveChatAdapter.getItemCount() - 1);

                }
            };
            meeting.pubSub.subscribe("CHAT", chatListener);
        }

        @Override
        public void onMeetingLeft() {
            if (!isDestroyed()) finish();
        }

        @Override
        public void onParticipantJoined(Participant participant) {

            Toast.makeText(MeetingActivity.this, participant.getDisplayName() + " joined", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onParticipantLeft(Participant participant) {

            Toast.makeText(MeetingActivity.this, participant.getDisplayName() + " left", Toast.LENGTH_SHORT).show();
        }
    };

    private void setActionListeners() {
        // toggle mic
        findViewById(R.id.btnMic).setOnClickListener(view -> {
            if (micEnabled) {

                // this will mute the local participant's mic
                meeting.muteMic();
                micBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.meeting_grey));
                micIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.mic_off));
            } else {
                // this will unmute the local participant's mic
                meeting.unmuteMic();
                micBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.white));
                micIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.mic_on));
            }
            micEnabled=!micEnabled;
        });

        // toggle webcam
        findViewById(R.id.btnWebcam).setOnClickListener(view -> {
            if (webcamEnabled) {
                // this will disable the local participant webcam
                meeting.disableWebcam();
                webcamBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.meeting_grey));
                webcamIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.camera_off));
            } else {
                // this will enable the local participant webcam
                meeting.enableWebcam();
                webcamBG.setBackgroundColor(ContextCompat.getColor(MeetingActivity.this, R.color.white));
                webcamIV.setImageDrawable(ContextCompat.getDrawable(MeetingActivity.this, R.drawable.camera_on));
            }
            webcamEnabled=!webcamEnabled;
        });

        // leave meeting
        findViewById(R.id.btnLeave).setOnClickListener(view -> {
            // this will make the local participant leave the meeting
            meeting.leave();
//            meeting = null;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (meeting != null){
            meeting.leave();
            meeting.removeAllListeners();
//            meeting = null;
        }

    }

    @Override
    public void addParticipant(Participant participant) {
        secondaryParticipantAdapter.addParticipant(participant);
    }

    @Override
    public void participantOnline(boolean participantOnline) {
        if (participantOnline){
            rvParticipants.setVisibility(View.VISIBLE);
        } else rvParticipants.setVisibility(View.GONE);
    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Define the action you want to perform when double-tap occurs
            if (IN_FRONT){
                IN_FRONT = false;
                meeting.changeWebcam();
                userVideoView.setMirror(false);
            } else {
                IN_FRONT = true;
                meeting.changeWebcam();
                userVideoView.setMirror(true);
            }
            return true;
        }
    }
}