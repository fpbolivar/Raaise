package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GetGlobalVideoModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.mainHomeData;

import java.util.List;

public class HomeFollowingAdapter extends RecyclerView.Adapter<HomeFollowingAdapter.ViewHolder> {
    Context context;
    List<GetGlobalVideoModel.Data> list;
    HomeReelsListener homeReelsListener;
    boolean visible = false;


    public HomeFollowingAdapter(Context context, List<GetGlobalVideoModel.Data> list, HomeReelsListener homeReelsListener) {
        this.context = context;
        this.list = list;
        this.homeReelsListener = homeReelsListener;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @NonNull
    @Override
    public HomeFollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeFollowingAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_videos_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetGlobalVideoModel.Data obj = list.get(position);

        holder.VideoViewInHomeReels.setVideoURI(Uri.parse(obj.getVideoLink()));
        holder.VideoViewInHomeReels.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
        homeReelsListener.SharVideoView(holder.VideoViewInHomeReels);
        if (obj.isDonation) {
            holder.DonationLayout.setVisibility(View.VISIBLE);
        } else {
            holder.DonationLayout.setVisibility(View.GONE);
        }

        holder.CommentsInAdapter.setOnClickListener(view -> homeReelsListener.ShowCommentBottomSheetDialog(obj.get_id(), holder.CommentCountInHomeVideoSingleItem));

        holder.MainLayoutInFollowingVideoSingleItem.setOnClickListener(view -> {
            if (holder.VideoViewInHomeReels.isPlaying()) {
                mainHomeData.ShowPause(holder.Lottie_PausePlay, R.raw.pause);
                holder.VideoViewInHomeReels.pause();
            } else {
                mainHomeData.ShowPlayPause(holder.Lottie_PausePlay, R.raw.play);
                holder.VideoViewInHomeReels.start();
            }
        });
        HelperClass.SetCaption(holder.hashTagsTV, obj.getVideoCaption());
        holder.hashTagsTV.setOnClickListener(view -> {
            if (visible) {
                visible = false;
                HelperClass.SetCaption(holder.hashTagsTV, obj.getVideoCaption());
            } else {
                visible = true;
                holder.hashTagsTV.setText(obj.getVideoCaption());
            }
        });

        if (obj.getUserId().getName() != null) {
            if (obj.getUserId().getName().length() > 10) {
                holder.NameInHomeVideoSingleItem.setText(obj.getUserId().getName().substring(0, 10) + "...");
            } else {
                holder.NameInHomeVideoSingleItem.setText(obj.getUserId().getName());
            }

        }
        if (obj.getUserId().getUserName() != null) {
            if (obj.getUserId().getUserName().length() > 10) {
                holder.UserNameInHomeVideoSingleItem.setText(String.format("@%s...", obj.getUserId().getUserName().substring(0, 9)));
            } else {
                holder.UserNameInHomeVideoSingleItem.setText(String.format("@%s", obj.getUserId().getUserName()));
            }

        }
        if (obj.getDonationAmount() != null) {
            holder.DonationRaisedInHomeVideoSingleItem.setText(String.format("Total Raised $%s", obj.getDonationAmount().isEmpty() ? 0 : obj.getDonationAmount()));
        }
        holder.LikeCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideolikeCount()));
        holder.CommentCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideoCommentCount()));
        holder.VideoShareCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideoShareCount()));
        if (obj.getUserId().getProfileImage() != null) {
            Glide.with(context).load(obj.getUserId().getProfileImage()).placeholder(R.drawable.placeholder).into(holder.ImageProfileInAdapter);
        }
        if (obj.isLiked()) {
            holder.LikeInHomeVideoSingleItem.setColorFilter(ContextCompat.getColor(context, R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holder.LikeInHomeVideoSingleItem.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        holder.LikeInHomeVideoSingleItem.setOnClickListener(view -> {
            if (obj.isLiked()) {
                obj.setVideolikeCount(obj.getVideolikeCount() - 1);
                obj.setLiked(false);
            } else {
                obj.setVideolikeCount(obj.getVideolikeCount() + 1);
                obj.setLiked(true);
            }
            mainHomeData.ShowHeart(holder.Lottie_Heart);
            homeReelsListener.VideoLikeDislike(obj.getSlug(), holder.LikeInHomeVideoSingleItem, holder.LikeCountInHomeVideoSingleItem);
        });
        holder.FollowButtonInHomeVideoSingleItem.setOnClickListener(view -> {

            homeReelsListener.follow(obj.getUserId().get_id(), holder.FollowTextInHomeVideoSingleItem);
            if (obj.isFollow()) {
                obj.setFollow(false);
                HelperClass.ChangeFollowUnFollow(list, obj.getUserId().getUserName(), false);
                holder.FollowTextInHomeVideoSingleItem.setText("Follow");
            } else {
                obj.setFollow(true);
                holder.FollowTextInHomeVideoSingleItem.setText("Following");
                HelperClass.ChangeFollowUnFollow(list, obj.getUserId().getUserName(), true);
            }
        });

        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeReelsListener.moreOptions(list, position, holder.moreOptions, obj.isReported());
            }
        });

        holder.DollarDonation.setOnClickListener(view -> homeReelsListener.ShowDonationDialog(obj.getUserId().get_id(), obj.get_id()));
        if (obj.isFollow()) {
            holder.FollowTextInHomeVideoSingleItem.setText("Following");
        } else {
            holder.FollowTextInHomeVideoSingleItem.setText("Follow");
        }

        if (obj.getAudioId() == null) {
            holder.AudioItem.setVisibility(View.GONE);
            holder.SongNameInHomeVideoSingleItem.setVisibility(View.GONE);
            holder.lottie_main.setVisibility(View.GONE);
        } else {

            holder.AudioItem.setVisibility(View.VISIBLE);
            holder.lottie_main.setVisibility(View.VISIBLE);
            holder.SongNameInHomeVideoSingleItem.setVisibility(View.VISIBLE);
            holder.SongNameInHomeVideoSingleItem.setText(obj.getAudioId().getSongName());
            Glide.with(context).load(obj.getAudioId().getThumbnail()).placeholder(R.drawable.placeholder).into(holder.SongImage);
        }
        holder.SongNameInHomeVideoSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeReelsListener.tryAudio(obj.getAudioId().get_id());
            }
        });

        // click Listener of image for public user profile
        holder.ImageProfileInAdapter.setOnClickListener(view -> HelperClass.DoOpenUserProfile(obj, context));
        // click Listener of UserName for public user profile
        holder.UserNameInHomeVideoSingleItem.setOnClickListener(view -> HelperClass.DoOpenUserProfile(obj, context));
        //sharingVideo
//        holder.ShareVideo.setOnClickListener(view -> HelperClass.ShareVideoDialog(obj.getVideoLink(),context));
        holder.ShareVideo.setOnClickListener(view -> homeReelsListener.ShowShareVideoDialog(obj.getVideoLink(), obj.getUserId().get_id(), obj.getUserId().getProfileImage(), obj.getUserId().getShortBio(), obj.getUserId().getUserName(), obj.get_id()));

        // Setup Top Rewarded Users List
        holder.topRewardedRV.setLayoutManager(new LinearLayoutManager(context));
//        holder.topRewardedRV.setAdapter(new TopRewardedAdapter(context));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearList() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public interface HomeReelsListener {

        void SharVideoView(VideoView videoView);

        void ShowCommentBottomSheetDialog(String VideoId, TextView textView);

        void follow(String FollowerTo, TextView textView);

        void ShowShareVideoDialog(String videoLink, String UserId, String UserImage, String UserShortBio, String UserName, String VideoId);

        void VideoLikeDislike(String slug, ImageView img, TextView likecount);

        void ShowDonationDialog(String UserId, String VideoId);

        void notificationClicked(VideoView view);


        void SharVideoLink(String Link);

        void tryAudio(String AudioId);

        void moreOptions(List<GetGlobalVideoModel.Data> list, int position, ImageView img, boolean isReported);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView topRewardedRV;
        ImageView tryAudioBtn;
        LinearLayout songInfoContainer;
        LinearLayout profileContainer, DonationLayout;
        ImageView moreOptions, ShareVideo;
        ImageView CommentsInAdapter, lottie_main;
        LinearLayout Layout_Donation;
        VideoView VideoViewInHomeReels;
        ImageView LikeInHomeVideoSingleItem, ImageProfileInAdapter, DollarDonation, SongImage;
        TextView NameInHomeVideoSingleItem, UserNameInHomeVideoSingleItem, DonationRaisedInHomeVideoSingleItem, SongNameInHomeVideoSingleItem, DonationAmount,
                LikeCountInHomeVideoSingleItem, hashTagsTV, CommentCountInHomeVideoSingleItem, VideoShareCountInHomeVideoSingleItem, FollowTextInHomeVideoSingleItem;
        LottieAnimationView Lottie_Heart, Lottie_PausePlay;
        RelativeLayout MainLayoutInFollowingVideoSingleItem, AudioItem;
        CardView FollowButtonInHomeVideoSingleItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topRewardedRV = itemView.findViewById(R.id.top_rewarded_rv);
            DonationLayout = itemView.findViewById(R.id.DonationLayout);
            ShareVideo = itemView.findViewById(R.id.ShareVideo);
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
