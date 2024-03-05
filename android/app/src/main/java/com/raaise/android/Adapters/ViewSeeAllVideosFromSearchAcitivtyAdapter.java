package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
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
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.mainHomeData;

import java.util.List;

public class ViewSeeAllVideosFromSearchAcitivtyAdapter extends RecyclerView.Adapter<ViewSeeAllVideosFromSearchAcitivtyAdapter.ViewHolder> {
    Context context;
    List<GlobalSearchModel.Data.Posts> list;
    HomeReelsListener homeReelsListener;
    boolean visible = false;

    public ViewSeeAllVideosFromSearchAcitivtyAdapter(Context context, List<GlobalSearchModel.Data.Posts> list, HomeReelsListener homeReelsListener) {
        this.context = context;
        this.list = list;
        this.homeReelsListener = homeReelsListener;
    }

    @NonNull
    @Override
    public ViewSeeAllVideosFromSearchAcitivtyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewSeeAllVideosFromSearchAcitivtyAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_videos_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSeeAllVideosFromSearchAcitivtyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GlobalSearchModel.Data.Posts obj = list.get(position);

        holder.VideoViewInHomeReels.setVideoPath(Prefs.GetBaseUrl(context) + obj.getVideoLink());
        holder.VideoViewInHomeReels.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        holder.VideoViewInHomeReels.start();
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
            holder.UserNameInHomeVideoSingleItem.setText(obj.getUserId().getUserName());
        }
        if (obj.getDonationAmount() != null) {
            holder.DonationRaisedInHomeVideoSingleItem.setText(String.format("$%s", obj.getDonationAmount().isEmpty() ? 0 : obj.getDonationAmount()));
        }
        holder.LikeCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideolikeCount()));
        holder.CommentCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideoCommentCount()));
        holder.VideoShareCountInHomeVideoSingleItem.setText(String.valueOf(obj.getVideoShareCount()));
        if (obj.getUserId().getProfileImage() != null) {
            Glide.with(context).load(Prefs.GetBaseUrl(context) + obj.getUserId().getProfileImage()).placeholder(R.drawable.placeholder).into(holder.ImageProfileInAdapter);
        }
        if (obj.isLiked()) {
            holder.LikeInHomeVideoSingleItem.setImageDrawable(context.getDrawable(R.drawable.like_icon));
        } else {
            holder.LikeInHomeVideoSingleItem.setImageDrawable(context.getDrawable(R.drawable.like_icon_white));
        }
        holder.LikeInHomeVideoSingleItem.setOnClickListener(view -> {
            mainHomeData.ShowHeart(holder.Lottie_Heart);
            homeReelsListener.VideoLikeDislike(obj.getSlug(), holder.LikeInHomeVideoSingleItem, holder.LikeCountInHomeVideoSingleItem);
        });
        holder.FollowButtonInHomeVideoSingleItem.setOnClickListener(view -> {
            homeReelsListener.follow(obj.getUserId().get_id());
            if (obj.isFollow()) {
                holder.FollowTextInHomeVideoSingleItem.setText("Follow");
            } else {
                holder.FollowTextInHomeVideoSingleItem.setText("Following");
            }
        });
        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeReelsListener.moreOptions(list.get(position)._id);
            }
        });

        holder.DollarDonation.setOnClickListener(view -> homeReelsListener.ShowDonationDialog(obj.getUserId().get_id(), obj.get_id(),obj));
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
            Glide.with(context).load(Prefs.GetBaseUrl(context) + obj.getAudioId().getThumbnail()).placeholder(R.drawable.placeholder).into(holder.SongImage);
        }
        holder.SongNameInHomeVideoSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeReelsListener.tryAudio(obj.getAudioId().get_id());
            }
        });

        holder.ShareVideo.setOnClickListener(view -> homeReelsListener.ShowShareVideoDialog(obj.getVideoLink(), obj.getUserId().get_id(), obj.getUserId().getProfileImage(), obj.getUserId().getShortBio(), obj.getUserId().getUserName(), obj.get_id()));
        holder.topRewardedHeading.setVisibility(View.GONE);

        holder.DonationLayout.setOnClickListener(view -> homeReelsListener.ShowDonationDialog(obj.getUserId().get_id(), obj.get_id(),obj));
        if(obj.getUserId().isVerified){
            holder.verficationCardView.setVisibility(View.VISIBLE);
        }else {
            holder.verficationCardView.setVisibility(View.GONE);
        }
        holder.videoViewCount.setText("" + obj.getVideoViewCount());
        holder.commentsConstraint.setOnClickListener(view -> homeReelsListener.ShowCommentBottomSheetDialog(obj.get_id(), holder.CommentCountInHomeVideoSingleItem));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface HomeReelsListener {
        void ShowCommentBottomSheetDialog(String VideoId, TextView textView);


        void follow(String FollowerTo);

        void InitializingSlug(String slug, String captionText, List<GlobalSearchModel.Data.Posts> list, int position, TextView caption, String link);

        void VideoLikeDislike(String slug, ImageView img, TextView likecount);

        void tryAudio(String AudioId);

        void moreOptions(String id);


        void ShowShareVideoDialog(String videoLink, String UserId, String UserImage, String UserShortBio, String UserName, String VideoId);


        void ShowDonationDialog(String UserId, String VideoId,GlobalSearchModel.Data.Posts obj);

        void notificationClicked(VideoView view);


        void SharVideoLink(String Link);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoViewCount;
        TextView topRewardedHeading;
        ImageView tryAudioBtn, more_options_btn_UP, ShareVideo;
        LinearLayout songInfoContainer;
        LinearLayout profileContainer;
        ImageView moreOptions, lottie_main, DonationLayout;
        ImageView CommentsInAdapter;
        LinearLayout Layout_Donation;
        VideoView VideoViewInHomeReels;
        ImageView LikeInHomeVideoSingleItem, ImageProfileInAdapter, DollarDonation, SongImage;
        TextView NameInHomeVideoSingleItem, UserNameInHomeVideoSingleItem, DonationRaisedInHomeVideoSingleItem, SongNameInHomeVideoSingleItem, DonationAmount,
                LikeCountInHomeVideoSingleItem, hashTagsTV, CommentCountInHomeVideoSingleItem, VideoShareCountInHomeVideoSingleItem, FollowTextInHomeVideoSingleItem;
        LottieAnimationView Lottie_Heart, Lottie_PausePlay;
        RelativeLayout MainLayoutInFollowingVideoSingleItem, AudioItem;
        RelativeLayout FollowButtonInHomeVideoSingleItem;
        ConstraintLayout commentsConstraint;
        CardView verficationCardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            verficationCardView=itemView.findViewById(R.id.verficationCardView);
            commentsConstraint=itemView.findViewById(R.id.commentsConstraint);
            videoViewCount = itemView.findViewById(R.id.view_count_tvk);
            topRewardedHeading = itemView.findViewById(R.id.top_rewarded_tv);
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
