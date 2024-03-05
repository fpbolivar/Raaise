package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GetAllUserVideoModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.mainHomeData;

import java.util.List;

public class UserVideosAdapter extends RecyclerView.Adapter<UserVideosAdapter.ViewHolder> {
    public static int visTrue, visFalse;
    Context context;
    List<GetAllUserVideoModel.Data> list;
    HomeReelsListener homeReelsListener;
    boolean visible = false;

    public UserVideosAdapter(Context context, List<GetAllUserVideoModel.Data> list, HomeReelsListener homeReelsListener) {
        this.context = context;
        this.list = list;
        this.homeReelsListener = homeReelsListener;
    }

    @NonNull
    @Override
    public UserVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserVideosAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_videos_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserVideosAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        GetAllUserVideoModel.Data obj = list.get(position);

        ViewHolder.VideoViewInHomeReels.setVideoPath(Prefs.GetBaseUrl(context) + obj.getVideoLink());
        ViewHolder.VideoViewInHomeReels.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);

            }
        });
        ViewHolder.VideoViewInHomeReels.start();
        holder.CommentsInAdapter.setOnClickListener(view -> homeReelsListener.ShowCommentBottomSheetDialog(obj.get_id(), ViewHolder.CommentCountInHomeVideoSingleItem));

        ViewHolder.DonationAmount.setVisibility(View.GONE);
        holder.MainLayoutInFollowingVideoSingleItem.setOnClickListener(view -> {
            if (ViewHolder.VideoViewInHomeReels.isPlaying()) {
                mainHomeData.ShowPause(holder.Lottie_PausePlay, R.raw.pause);
                ViewHolder.VideoViewInHomeReels.pause();
            } else {
                mainHomeData.ShowPlayPause(holder.Lottie_PausePlay, R.raw.play);
                ViewHolder.VideoViewInHomeReels.start();

            }
        });
        holder.more_options_btn_UP.setVisibility(View.VISIBLE);
        holder.more_options_btn_UP.setOnClickListener(view -> {
            homeReelsListener.InitializingSlug(obj.getSlug(), obj.getVideoCaption(), list, position, ViewHolder.hashTagsTV, obj.getVideoImage());
        });
        HelperClass.SetCaption(ViewHolder.hashTagsTV, obj.getVideoCaption());
        Log.i("caption", "No Update:- " + obj.getVideoCaption());
        ViewHolder.hashTagsTV.setOnClickListener(view -> {

            if (visible) {
                visible = false;
                visTrue = 0;
                Log.i("Visible", String.valueOf(visible));
                HelperClass.SetCaption(ViewHolder.hashTagsTV, obj.getVideoCaption());
            } else {
                visible = true;
                visTrue = 1;
                Log.i("Visible", String.valueOf(visible));
                ViewHolder.hashTagsTV.setText(obj.getVideoCaption());
            }
        });


        if (obj.getAudioId() == null) {
            holder.AudioItem.setVisibility(View.INVISIBLE);
            ViewHolder.SongNameInHomeVideoSingleItem.setVisibility(View.INVISIBLE);
            holder.lottie_main.setVisibility(View.INVISIBLE);
        } else {
            holder.lottie_main.setVisibility(View.VISIBLE);
            holder.AudioItem.setVisibility(View.VISIBLE);
            ViewHolder.SongNameInHomeVideoSingleItem.setVisibility(View.VISIBLE);
            ViewHolder.SongNameInHomeVideoSingleItem.setText(obj.getAudioId().getSongName());
            Glide.with(context).load(obj.getAudioId().getThumbnail()).placeholder(R.drawable.placeholder).into(holder.SongImage);
        }

        if (obj.getUserId().getName() != null) {
            if (obj.getUserId().getName().length() > 15) {
                holder.NameInHomeVideoSingleItem.setText(obj.getUserId().getName().substring(0, 15) + "...");
            } else {
                holder.NameInHomeVideoSingleItem.setText(obj.getUserId().getName());
            }

        }
        if (obj.getUserId().getUserName() != null) {
            ViewHolder.UserNameInHomeVideoSingleItem.setText(obj.getUserId().getUserName());
        }
        if (obj.getDonationAmount() != null) {
            ViewHolder.DonationRaisedInHomeVideoSingleItem.setText(String.format("$%s", obj.getDonationAmount().isEmpty() ? 0 : obj.getDonationAmount()));
        }
        ViewHolder.LikeCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideolikeCount()));
        ViewHolder.CommentCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideoCommentCount()));
        ViewHolder.VideoShareCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideoShareCount()));
        if (obj.getUserId().getProfileImage() != null) {
            Glide.with(context).load(obj.getUserId().getProfileImage()).placeholder(R.drawable.placeholder).into(holder.ImageProfileInAdapter);
        }
        if (obj.isLiked()) {
            holder.LikeInHomeVideoSingleItem.setImageDrawable(context.getDrawable(R.drawable.like_icon));
        } else {
            holder.LikeInHomeVideoSingleItem.setImageDrawable(context.getDrawable(R.drawable.like_icon_white));
        }
        holder.LikeInHomeVideoSingleItem.setOnClickListener(view -> {
            mainHomeData.ShowHeart(holder.Lottie_Heart);
            homeReelsListener.VideoLikeDislike(obj.getSlug(), holder.LikeInHomeVideoSingleItem, ViewHolder.LikeCountInHomeVideoSingleItem);
        });
        holder.FollowButtonInHomeVideoSingleItem.setVisibility(View.GONE);
        holder.moreOptions.setVisibility(View.INVISIBLE);

        holder.DollarDonation.setVisibility(View.GONE);
        if (obj.isDonation) {
            holder.DonationLayout.setVisibility(View.VISIBLE);
        } else {
            holder.DonationLayout.setVisibility(View.GONE);
        }

        ViewHolder.SongNameInHomeVideoSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeReelsListener.tryAudio(obj.getAudioId().get_id());
            }
        });
        holder.ShareVideo.setOnClickListener(view -> homeReelsListener.ShowShareVideoDialog(obj.getVideoLink(), obj.getUserId().get_id(), obj.getUserId().getProfileImage(), obj.getUserId().getShortBio(), obj.getUserId().getUserName(), obj.get_id()));

        holder.topRewardedTV.setVisibility(View.GONE);

        holder.videoViewCount.setText("" + obj.getVideoViewCount());
        if(obj.getUserId().isVerified){
            holder.verficationCardView.setVisibility(View.VISIBLE);
        }else {
            holder.verficationCardView.setVisibility(View.GONE);
        }

        holder.commentsConstraint.setOnClickListener(view -> homeReelsListener.ShowCommentBottomSheetDialog(obj.get_id(), holder.CommentCountInHomeVideoSingleItem));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface HomeReelsListener {
        void ShowCommentBottomSheetDialog(String VideoId, TextView textView);

        void ShowShareVideoDialog(String videoLink, String UserId, String UserImage, String UserShortBio, String UserName, String VideoId);

        void follow(String FollowerTo);

        void InitializingSlug(String slug, String captionText, List<GetAllUserVideoModel.Data> list, int position, TextView caption, String link);

        void VideoLikeDislike(String slug, ImageView img, TextView likecount);

        void tryAudio(String AudioId);

        void moreOptions(String id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoViewCount;
        TextView topRewardedTV;
        public static VideoView VideoViewInHomeReels;
        public static TextView NameInHomeVideoSingleItem, UserNameInHomeVideoSingleItem, DonationRaisedInHomeVideoSingleItem, SongNameInHomeVideoSingleItem, DonationAmount,
                LikeCountInHomeVideoSingleItem, hashTagsTV, CommentCountInHomeVideoSingleItem, VideoShareCountInHomeVideoSingleItem, FollowTextInHomeVideoSingleItem;
        ImageView tryAudioBtn, more_options_btn_UP, ShareVideo;
        LinearLayout songInfoContainer;
        LinearLayout profileContainer;
        CardView verficationCardView;
        ImageView moreOptions, lottie_main,DonationLayout;
        ImageView CommentsInAdapter;
        LinearLayout Layout_Donation;
        ImageView LikeInHomeVideoSingleItem, ImageProfileInAdapter, DollarDonation, SongImage;
        LottieAnimationView Lottie_Heart, Lottie_PausePlay;
        RelativeLayout MainLayoutInFollowingVideoSingleItem, AudioItem;
        RelativeLayout FollowButtonInHomeVideoSingleItem;
        ConstraintLayout commentsConstraint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            verficationCardView=itemView.findViewById(R.id.verficationCardView);
            commentsConstraint=itemView.findViewById(R.id.commentsConstraint);
            videoViewCount = itemView.findViewById(R.id.view_count_tvk);
            topRewardedTV = itemView.findViewById(R.id.top_rewarded_tv);
            DonationLayout = itemView.findViewById(R.id.DonationLayout);
            ShareVideo = itemView.findViewById(R.id.ShareVideo);
            more_options_btn_UP = itemView.findViewById(R.id.more_options_btn_UP);
            SongImage = itemView.findViewById(R.id.SongImage);
            hashTagsTV = itemView.findViewById(R.id.hashTagsTV);
            AudioItem = itemView.findViewById(R.id.AudioItem);
            DonationAmount = itemView.findViewById(R.id.DonationAmount);
            tryAudioBtn = itemView.findViewById(R.id.try_audio_btn);
            songInfoContainer = itemView.findViewById(R.id.SongLayout);
            profileContainer = itemView.findViewById(R.id.profileContainer);
            moreOptions = itemView.findViewById(R.id.more_options_btn);
            DollarDonation = itemView.findViewById(R.id.DollarDonation);
            Lottie_PausePlay = itemView.findViewById(R.id.Lottie_PausePlay);
            lottie_main = itemView.findViewById(R.id.lottie_main);
            ImageProfileInAdapter = itemView.findViewById(R.id.ImageProfileInAdapter);
            VideoViewInHomeReels = itemView.findViewById(R.id.VideoViewInHomeReels);
            CommentsInAdapter = itemView.findViewById(R.id.CommentsInAdapter);
            LikeInHomeVideoSingleItem = itemView.findViewById(R.id.LikeInHomeVideoSingleItem);
            Lottie_Heart = itemView.findViewById(R.id.Lottie_Heart);
            MainLayoutInFollowingVideoSingleItem = itemView.findViewById(R.id.MainLayoutInFollowingVideoSingleItem);

            NameInHomeVideoSingleItem = itemView.findViewById(R.id.NameInHomeVideoSingleItem);
            UserNameInHomeVideoSingleItem = itemView.findViewById(R.id.UserNameInHomeVideoSingleItem);
            DonationRaisedInHomeVideoSingleItem = itemView.findViewById(R.id.DonationRaisedInHomeVideoSingleItem);
            SongNameInHomeVideoSingleItem = itemView.findViewById(R.id.SongNameInHomeVideoSingleItem);

            LikeCountInHomeVideoSingleItem = itemView.findViewById(R.id.LikeCountInHomeVideoSingleItem);
            CommentCountInHomeVideoSingleItem = itemView.findViewById(R.id.CommentCountInHomeVideoSingleItem);
            VideoShareCountInHomeVideoSingleItem = itemView.findViewById(R.id.VideoShareCountInHomeVideoSingleItem);
            FollowTextInHomeVideoSingleItem = itemView.findViewById(R.id.FollowTextInHomeVideoSingleItem);
            FollowButtonInHomeVideoSingleItem = itemView.findViewById(R.id.FollowButtonInHomeVideoSingleItem);


        }
    }


}
