package com.scripttube.android.ScriptTube.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetGlobalVideoModel;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.mainHomeData;

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
        HelperClass.BackgroundAsyncTask task = new HelperClass.BackgroundAsyncTask(holder.VideoViewInHomeReels, obj.getVideoLink());
        task.execute();
        holder.VideoViewInHomeReels.start();
        Log.e("Maninder ", "onBindViewHolder: " + obj.get_id());
        holder.CommentsInAdapter.setOnClickListener(view -> homeReelsListener.ShowCommentBottomSheetDialog(obj.get_id(),holder.CommentCountInHomeVideoSingleItem));


        if (obj.getDonationAmount() == null || obj.getDonationAmount().equalsIgnoreCase("0") || obj.getDonationAmount().equalsIgnoreCase("")) {
            holder.DonationAmount.setText("0");
        } else {
            holder.DonationAmount.setText(String.valueOf(obj.getDonationAmount()));
        }
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
            holder.NameInHomeVideoSingleItem.setText(obj.getUserId().getName());
        }
        if (obj.getUserId().getUserName() != null) {
            holder.UserNameInHomeVideoSingleItem.setText(obj.getUserId().getUserName());
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

        holder.DollarDonation.setOnClickListener(view -> homeReelsListener.ShowDonationDialog());
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
                homeReelsListener.tryAudio();
            }
        });
        // click Listener of image for public user profile
        holder.ImageProfileInAdapter.setOnClickListener(view -> HelperClass.DoOpenUserProfile(obj, context));
        // click Listener of UserName for public user profile
        holder.UserNameInHomeVideoSingleItem.setOnClickListener(view -> HelperClass.DoOpenUserProfile(obj, context));
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
        void ShowCommentBottomSheetDialog(String VideoId,TextView textView);

        void follow(String FollowerTo);

        void VideoLikeDislike(String slug, ImageView img, TextView likecount);

        void ShowDonationDialog();

        void notificationClicked(VideoView view);

        void reportVideo(String _id);

        void SharVideoLink(String Link);

        void tryAudio();

        void moreOptions(String id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tryAudioBtn;
        LinearLayout songInfoContainer;
        LinearLayout profileContainer;
        ImageView moreOptions;
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
