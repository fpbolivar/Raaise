package com.raaise.android.Adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.R;

import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import live.videosdk.rtc.android.Meeting;
import live.videosdk.rtc.android.Participant;
import live.videosdk.rtc.android.Stream;
import live.videosdk.rtc.android.VideoView;
import live.videosdk.rtc.android.listeners.MeetingEventListener;
import live.videosdk.rtc.android.listeners.ParticipantEventListener;

public class
ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.PeerViewHolder> {

    private final List<Participant> participants = new ArrayList<>();
    private SecondaryListener secondaryListener;
    private Context context;

    public ParticipantAdapter(Meeting meeting, SecondaryListener listener, Context con) {
        this.secondaryListener = listener;
        this.context = con;
        // adding the local participant(You) to the list
//        participants.add(participant);

        // adding Meeting Event listener to get the participant join/leave event in the meeting.
        meeting.addEventListener(new MeetingEventListener() {
            @Override
            public void onParticipantJoined(Participant participant) {
                // add participant to the list
                if (participants.size() == 2) {
                    secondaryListener.addParticipant(participant);
                } else {
                secondaryListener.participantOnline(true);
                    participants.add(participant);
                    notifyItemInserted(participants.size() - 1);
                }

            }

            @Override
            public void onParticipantLeft(Participant participant) {
                int pos = -1;
                for (int i = 0; i < participants.size(); i++) {
                    if (participants.get(i).getId().equals(participant.getId())) {
                        pos = i;
                        break;
                    }
                }
                // remove participant from the list
                participants.remove(participant);

                if (pos >= 0) {
                    notifyItemRemoved(pos);
                }
                if (participants.size() == 0){
                    secondaryListener.participantOnline(false);
                }
            }
        });
    }

    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_remote_peer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
        Participant participant = participants.get(position);

        holder.tvName.setText(participant.getDisplayName());

        holder.participantView.setMirror(true);
        // adding the initial video stream for the participant into the 'VideoView'
        for (Map.Entry<String, Stream> entry : participant.getStreams().entrySet()) {
            Stream stream = entry.getValue();
            if (stream.getKind().equalsIgnoreCase("video")) {
                holder.participantView.setVisibility(View.VISIBLE);
                VideoTrack videoTrack = (VideoTrack) stream.getTrack();
                holder.participantView.addTrack(videoTrack);
                break;
            }
        }
        // add Listener to the participant which will update start or stop the video stream of that participant
        participant.addEventListener(new ParticipantEventListener() {
            @Override
            public void onStreamEnabled(Stream stream) {
                if (stream.getKind().equalsIgnoreCase("audio")){
                    holder.userMicIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mic_on));
                }

                if (stream.getKind().equalsIgnoreCase("video")) {
                    holder.dummyImage.setVisibility(View.GONE);
                    holder.participantView.setVisibility(View.VISIBLE);
                    holder.participantView.setVisibility(View.VISIBLE);
                    VideoTrack videoTrack = (VideoTrack) stream.getTrack();
                    holder.participantView.addTrack(videoTrack);
                }
            }

            @Override
            public void onStreamDisabled(Stream stream) {
                if (stream.getKind().equalsIgnoreCase("audio")){
                    holder.userMicIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mic_off));
                }
                if (stream.getKind().equalsIgnoreCase("video")) {
                    holder.dummyImage.setVisibility(View.VISIBLE);
                    holder.participantView.setVisibility(View.GONE);
                    holder.participantView.removeTrack();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("partiesID", "getItemCount: " + participants.size());
        return participants.size();
    }

    static class PeerViewHolder extends RecyclerView.ViewHolder {
        // 'VideoView' to show Video Stream
        public VideoView participantView;
        public TextView tvName;
        public View itemView;

        ImageView userMicIV;
        ImageView dummyImage;

        PeerViewHolder(@NonNull View view) {
            super(view);
            itemView = view;
            tvName = view.findViewById(R.id.tvName);
            userMicIV = view.findViewById(R.id.user_mic);
            dummyImage = view.findViewById(R.id.participantImageView);
            participantView = view.findViewById(R.id.participantView);
        }
    }

    public interface SecondaryListener{
        void addParticipant(Participant participant);
        void participantOnline(boolean participantOnline);
    }
}
