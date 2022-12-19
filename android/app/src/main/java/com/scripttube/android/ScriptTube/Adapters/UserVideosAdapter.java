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
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetAllUserVideoModel;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.mainHomeData;

import java.util.List;

public class UserVideosAdapter extends RecyclerView.Adapter<UserVideosAdapter.ViewHolder> {
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
        HelperClass.BackgroundAsyncTask task = new HelperClass.BackgroundAsyncTask(holder.VideoViewInHomeReels, obj.getVideoLink());
        task.execute();
        holder.VideoViewInHomeReels.start();
        holder.CommentsInAdapter.setOnClickListener(view -> homeReelsListener.ShowCommentBottomSheetDialog(obj.get_id(), holder.CommentCountInHomeVideoSingleItem));

        holder.DonationAmount.setVisibility(View.GONE);
        holder.MainLayoutInFollowingVideoSingleItem.setOnClickListener(view -> {
            if (holder.VideoViewInHomeReels.isPlaying()) {
                mainHomeData.ShowPause(holder.Lottie_PausePlay, R.raw.pause);
                holder.VideoViewInHomeReels.pause();
            } else {
                mainHomeData.ShowPlayPause(holder.Lottie_PausePlay, R.raw.play);
                holder.VideoViewInHomeReels.start();
            }

        });
        holder.more_options_btn_UP.setVisibility(View.VISIBLE);
        holder.more_options_btn_UP.setOnClickListener(view -> {
            homeReelsListener.InitializingSlug(obj.getSlug(),list,position,holder.hashTagsTV,obj.getVideoImage());
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

        if (obj.getAudioId() == null) {
            holder.AudioItem.setVisibility(View.INVISIBLE);
            holder.SongNameInHomeVideoSingleItem.setVisibility(View.INVISIBLE);
            holder.lottie_main.setVisibility(View.INVISIBLE);
        } else {
            holder.lottie_main.setVisibility(View.VISIBLE);
            holder.AudioItem.setVisibility(View.VISIBLE);
            holder.SongNameInHomeVideoSingleItem.setVisibility(View.VISIBLE);
            holder.SongNameInHomeVideoSingleItem.setText(obj.getAudioId().getSongName());
            Glide.with(context).load(obj.getAudioId().getThumbnail()).placeholder(R.drawable.placeholder).into(holder.SongImage);
        }

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
        holder.FollowButtonInHomeVideoSingleItem.setVisibility(View.GONE);
        holder.moreOptions.setVisibility(View.INVISIBLE);

        holder.DollarDonation.setVisibility(View.GONE);

        holder.SongNameInHomeVideoSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeReelsListener.tryAudio();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface HomeReelsListener {
        void ShowCommentBottomSheetDialog(String VideoId, TextView textView);


        void follow(String FollowerTo);

        void InitializingSlug(String slug,List<GetAllUserVideoModel.Data> list,int position,TextView caption,String link);

        void VideoLikeDislike(String slug, ImageView img, TextView likecount);

        void tryAudio();

        void moreOptions(String id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tryAudioBtn,more_options_btn_UP;
        LinearLayout songInfoContainer;
        LinearLayout profileContainer;
        ImageView moreOptions, lottie_main;
        ImageView CommentsInAdapter;
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
