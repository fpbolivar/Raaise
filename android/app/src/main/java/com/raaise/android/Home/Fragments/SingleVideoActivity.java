package com.raaise.android.Home.Fragments;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.raaise.android.Adapters.CommentsAdapter;
import com.raaise.android.Adapters.ShareVideoUserListAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetSingleVideoModel;
import com.raaise.android.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentsReplyModel;
import com.raaise.android.ApiManager.ApiModels.VideoLikeDislikeModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.BuildConfig;
import com.raaise.android.R;
import com.raaise.android.Settings.Payments.PaymentMethods;
import com.raaise.android.Try_AudioActivity;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.Utilities.HelperClasses.StringHelper;
import com.raaise.android.Utilities.HelperClasses.mainHomeData;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.ReportVideoPojo;
import com.raaise.android.model.ReportVideoRes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SingleVideoActivity extends AppCompatActivity implements CommentsAdapter.CommentReplyListener, ShareVideoUserListAdapter.ChatListListener {
    public List<ChatListModel.Data> chatListModels;
    ImageView tryAudioBtn;
    LinearLayout songInfoContainer;
    LinearLayout profileContainer;
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
    ApiManager apiManager = App.getApiManager();
    String Slug;
    boolean visible = false;
    CommentsAdapter adapter;
    List<ListOfVideoCommentsModel.Data> comments;
    RelativeLayout CommentReply;
    boolean isReply = false;
    TextView replyToText;
    String replyCommentId;
    boolean isSelected;
    GetSingleVideoModel.Data obj;
    String VideoId;
    Dialog UserListDialog;

    private static void OpenIntent(String path) {

        try {
            File f = new File(path);
            String WaterMarkVideoName = "Scriptube_share" + new Date().getTime();
            String command = "-i " + f.getPath() + " -i " + Uri.parse(StringHelper.WaterMarkLogo) + " -filter_complex \"[1]scale=iw/2:-1[wm];[0][wm]  overlay=20:main_h-overlay_h\" " + Environment.getExternalStorageDirectory() + "/" + "Scriptube" + "/" + App.getContext().getPackageName() + "/" + "Shared_Videos" + "/" + WaterMarkVideoName + ".mp4";
            FFmpeg.executeAsync(command, new ExecuteCallback() {
                @Override
                public void apply(long executionId, int returnCode) {
                    if (returnCode == RETURN_CODE_SUCCESS) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("video/mp4");
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Objects.requireNonNull(App.getContext()), BuildConfig.APPLICATION_ID + ".provider", new File(Environment.getExternalStorageDirectory() + "/" + "Scriptube" + "/" + App.getContext().getPackageName() + "/" + "Shared_Videos" + "/" + WaterMarkVideoName + ".mp4")));
                            App.getContext().startActivity(Intent.createChooser(intent, "share").setFlags(FLAG_ACTIVITY_NEW_TASK));
                            if (f.exists()) {
                                f.delete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                    Dialogs.dismissProgressDialog();

                }
            });
        } catch (Exception e) {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);
        Initialization();
        ClickListeners();
        HitApi();
    }

    private void HitApi() {
        GetSingleVideoModel model = new GetSingleVideoModel(Slug);
        Dialogs.createProgressDialog(SingleVideoActivity.this);
        apiManager.GetSingleVideo(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<GetSingleVideoModel>() {
            @Override
            public void onSuccess(GetSingleVideoModel getSingleVideoModel) {
                Dialogs.HideProgressDialog();
                GetSingleVideoModel.Data obj1 = getSingleVideoModel.getData();
                obj = obj1;
                VideoViewInHomeReels.setVideoPath(Prefs.GetBaseUrl(SingleVideoActivity.this) + Uri.parse(obj1.getGetVideo().getVideoLink()));
                VideoViewInHomeReels.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.setLooping(true);
                    }
                });

                VideoViewInHomeReels.start();

                CommentsInAdapter.setOnClickListener(view -> ShowCommentBottomSheetDialog(obj1.getGetVideo().get_id(), CommentCountInHomeVideoSingleItem));


                HelperClass.SetCaption(hashTagsTV, obj1.getGetVideo().getVideoCaption());


                if (obj1.getGetVideo().getUserId().getName() != null) {
                    if (obj1.getGetVideo().getUserId().getName().length() > 10) {
                        NameInHomeVideoSingleItem.setText(obj1.getGetVideo().getUserId().getName().substring(0, 10) + "...");
                    } else {
                        NameInHomeVideoSingleItem.setText(obj1.getGetVideo().getUserId().getName());
                    }

                }
                if (obj1.getGetVideo().getUserId().getUserName() != null) {
                    if (obj1.getGetVideo().getUserId().getUserName().length() > 10) {
                        UserNameInHomeVideoSingleItem.setText(String.format("@%s...", obj1.getGetVideo().getUserId().getUserName().substring(0, 9)));
                    } else {
                        UserNameInHomeVideoSingleItem.setText(String.format("@%s", obj1.getGetVideo().getUserId().getUserName()));
                    }

                }
                if (obj1.getGetVideo().getDonationAmount() != null) {
                    DonationRaisedInHomeVideoSingleItem.setText(String.format("Total Raised $%s", obj1.getGetVideo().getDonationAmount().isEmpty() ? 0 : obj1.getGetVideo().getDonationAmount()));
                }
                LikeCountInHomeVideoSingleItem.setText(String.valueOf(obj1.getGetVideo().getVideolikeCount()));
                CommentCountInHomeVideoSingleItem.setText(String.valueOf(obj1.getGetVideo().getVideoCommentCount()));
                VideoShareCountInHomeVideoSingleItem.setText(String.valueOf(obj1.getGetVideo().getVideoShareCount()));
                if (obj1.getGetVideo().getUserId().getProfileImage() != null) {
                    Glide.with(SingleVideoActivity.this).load(Prefs.GetBaseUrl(SingleVideoActivity.this) + obj1.getGetVideo().getUserId().getProfileImage()).placeholder(R.drawable.placeholder).into(ImageProfileInAdapter);
                }
                if (obj1.isLike()) {
                    LikeInHomeVideoSingleItem.setColorFilter(ContextCompat.getColor(SingleVideoActivity.this, R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    LikeInHomeVideoSingleItem.setColorFilter(ContextCompat.getColor(SingleVideoActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }


                if (obj1.isFollow()) {
                    FollowTextInHomeVideoSingleItem.setText("Following");
                } else {
                    FollowTextInHomeVideoSingleItem.setText("Follow");
                }


                if (obj1.getGetVideo().getAudioId() == null) {
                    AudioItem.setVisibility(View.GONE);
                    SongNameInHomeVideoSingleItem.setVisibility(View.GONE);
                    lottie_main.setVisibility(View.GONE);
                } else {

                    AudioItem.setVisibility(View.VISIBLE);
                    lottie_main.setVisibility(View.VISIBLE);
                    SongNameInHomeVideoSingleItem.setVisibility(View.VISIBLE);
                    SongNameInHomeVideoSingleItem.setText(obj1.getGetVideo().getAudioId().getSongName());
                    Glide.with(SingleVideoActivity.this).load(Prefs.GetBaseUrl(SingleVideoActivity.this) + obj1.getGetVideo().getAudioId().getThumbnail()).placeholder(R.drawable.placeholder).into(SongImage);
                }


            }

            @Override
            public void onError(ServerError serverError) {
                Log.i("videoFound", "onError: Not found");
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
                FinishUserHere(serverError.getErrorMsg());
            }
        });
    }

    private void FinishUserHere(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SingleVideoActivity.this);
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SingleVideoActivity.this.finish();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void follow(String FollowerTo, TextView textView) {
        UserFollowUnfollowModel model = new UserFollowUnfollowModel(FollowerTo);
        apiManager.UserFollowUnfollow(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<UserFollowUnfollowModel>() {
            @Override
            public void onSuccess(UserFollowUnfollowModel userFollowUnfollowModel) {
                if (userFollowUnfollowModel.isFollowed()) {
                    textView.setText("Following");
                } else {
                    textView.setText("Follow");
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    public void ShowDonationDialog(String UserId, String VideoId) {
        final Dialog dialog = new Dialog(SingleVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_donation_dialog);
        LinearLayout Donate = dialog.findViewById(R.id.dialog_btn_Donation);
        EditText donation_amount_ET = dialog.findViewById(R.id.donation_amount_ET);
        RelativeLayout Layout = dialog.findViewById(R.id.layout_donation_amount);
        ImageView Tick = dialog.findViewById(R.id.select_firstAmount_icon);





        Donate.setOnClickListener(view -> {
            if (!donation_amount_ET.getText().toString().trim().equalsIgnoreCase("")) {

                if ((Long.parseLong(donation_amount_ET.getText().toString().trim()) * 1) != 0) {
                    dialog.dismiss();
                    startActivity(new Intent(SingleVideoActivity.this, PaymentMethods.class).putExtra("AmountToDonate", donation_amount_ET.getText().toString().trim()).putExtra("DonateTo", UserId).putExtra("VideoId", VideoId));
                } else {
                    Toast.makeText(SingleVideoActivity.this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SingleVideoActivity.this, "Enter Amount First", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void VideoLikeDislike(String slug, ImageView Img, TextView LikeCount) {
        VideoLikeDislikeModel model = new VideoLikeDislikeModel(slug);
        apiManager.VideoLikeDislike(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<VideoLikeDislikeModel>() {
            @Override
            public void onSuccess(VideoLikeDislikeModel videoLikeDislikeModel) {
                LikeCount.setText(String.valueOf(videoLikeDislikeModel.getVideoCount()));
                if (videoLikeDislikeModel.isLike()) {
                    Img.setColorFilter(ContextCompat.getColor(SingleVideoActivity.this, R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    Img.setColorFilter(ContextCompat.getColor(SingleVideoActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    public void tryAudio(String AudioId) {
        startActivity(new Intent(SingleVideoActivity.this, Try_AudioActivity.class).putExtra("AudioId", AudioId));
    }

    private void Initialization() {

        Slug = getIntent().getStringExtra("SlugForSingleVideo");
        ShareVideo = findViewById(R.id.ShareVideo1);
        SongImage = findViewById(R.id.SongImage1);
        hashTagsTV = findViewById(R.id.hashTagsTV1);
        AudioItem = findViewById(R.id.AudioItem1);
        DonationAmount = findViewById(R.id.DonationAmount1);
        tryAudioBtn = findViewById(R.id.try_audio_btn1);
        songInfoContainer = findViewById(R.id.SongLayout1);
        profileContainer = findViewById(R.id.profileContainer1);
        moreOptions = findViewById(R.id.more_options_btn1);
        DollarDonation = findViewById(R.id.DollarDonation1);
        Lottie_PausePlay = findViewById(R.id.Lottie_PausePlay1);
        lottie_main = findViewById(R.id.lottie_main1);
        ImageProfileInAdapter = findViewById(R.id.ImageProfileInAdapter1);
        VideoViewInHomeReels = findViewById(R.id.VideoViewInHomeReels1);
        CommentsInAdapter = findViewById(R.id.CommentsInAdapter1);
        LikeInHomeVideoSingleItem = findViewById(R.id.LikeInHomeVideoSingleItem1);
        Lottie_Heart = findViewById(R.id.Lottie_Heart1);
        MainLayoutInFollowingVideoSingleItem = findViewById(R.id.MainLayoutInFollowingVideoSingleItem1);

        NameInHomeVideoSingleItem = findViewById(R.id.NameInHomeVideoSingleItem1);
        UserNameInHomeVideoSingleItem = findViewById(R.id.UserNameInHomeVideoSingleItem1);
        DonationRaisedInHomeVideoSingleItem = findViewById(R.id.DonationRaisedInHomeVideoSingleItem1);
        SongNameInHomeVideoSingleItem = findViewById(R.id.SongNameInHomeVideoSingleItem1);

        LikeCountInHomeVideoSingleItem = findViewById(R.id.LikeCountInHomeVideoSingleItem1);
        CommentCountInHomeVideoSingleItem = findViewById(R.id.CommentCountInHomeVideoSingleItem1);
        VideoShareCountInHomeVideoSingleItem = findViewById(R.id.VideoShareCountInHomeVideoSingleItem1);
        FollowTextInHomeVideoSingleItem = findViewById(R.id.FollowTextInHomeVideoSingleItem1);
        FollowButtonInHomeVideoSingleItem = findViewById(R.id.FollowButtonInHomeVideoSingleItem1);
    }

    private void ClickListeners() {

        MainLayoutInFollowingVideoSingleItem.setOnClickListener(view -> {
            if (VideoViewInHomeReels.isPlaying()) {
                mainHomeData.ShowPause(Lottie_PausePlay, R.raw.pause);
                VideoViewInHomeReels.pause();
            } else {
                mainHomeData.ShowPlayPause(Lottie_PausePlay, R.raw.play);
                VideoViewInHomeReels.start();
            }
        });
        hashTagsTV.setOnClickListener(view -> {
            if (visible) {
                visible = false;
                HelperClass.SetCaption(hashTagsTV, obj.getGetVideo().getVideoCaption());
            } else {
                visible = true;
                hashTagsTV.setText(obj.getGetVideo().getVideoCaption());
            }
        });
        LikeInHomeVideoSingleItem.setOnClickListener(view -> {
            mainHomeData.ShowHeart(Lottie_Heart);
            VideoLikeDislike(obj.getGetVideo().getSlug(), LikeInHomeVideoSingleItem, LikeCountInHomeVideoSingleItem);
        });
        FollowButtonInHomeVideoSingleItem.setOnClickListener(view -> {

            follow(obj.getGetVideo().getUserId().get_id(), FollowTextInHomeVideoSingleItem);
            if (obj.isFollow()) {
                obj.setFollow(false);
                FollowTextInHomeVideoSingleItem.setText("Follow");
            } else {
                obj.setFollow(true);
                FollowTextInHomeVideoSingleItem.setText("Following");
            }
        });
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptions(obj.getGetVideo()._id);
            }
        });

        DollarDonation.setOnClickListener(view -> ShowDonationDialog(obj.getGetVideo().getUserId().get_id(), obj.getGetVideo().get_id()));
        SongNameInHomeVideoSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAudio(obj.getGetVideo().getAudioId().get_id());
            }
        });

        
        ImageProfileInAdapter.setOnClickListener(view -> HelperClass.DoOpenUserProfileForSingleVideo(obj, SingleVideoActivity.this));
        
        UserNameInHomeVideoSingleItem.setOnClickListener(view -> HelperClass.DoOpenUserProfileForSingleVideo(obj, SingleVideoActivity.this));
        

        ShareVideo.setOnClickListener(view -> ShowShareVideoDialog(obj.getGetVideo().getVideoLink(), obj.getGetVideo().get_id()));
    }

    public void ShowShareVideoDialog(String videoLink, String VideoId) {
        this.VideoId = VideoId;
        ShowUserListDialog(videoLink);
    }

    public void ShowCommentBottomSheetDialog(String VideoId, TextView textView) {
        ShowSearchUserDialog(VideoId, textView);
    }

    private void ShowSearchUserDialog(String VideoId, TextView textView) {
        Dialog dialog = new Dialog(SingleVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comments_bottom_sheet);

        ImageView imageCloseInReply = dialog.findViewById(R.id.imageCloseInReply);
        imageCloseInReply.setOnClickListener(view -> {
            CommentReply.setVisibility(View.GONE);
            isReply = false;
            replyToText.setText("");
        });
        CommentReply = dialog.findViewById(R.id.ReplyToNameLayout);
        replyToText = dialog.findViewById(R.id.NameInReply);
        RecyclerView CommentsRecyclerView = dialog.findViewById(R.id.commentsRV);
        CommentsRecyclerView.setHasFixedSize(true);
        CommentsRecyclerView.setLayoutManager(new LinearLayoutManager(SingleVideoActivity.this));
        comments = new ArrayList<>();
        ImageView imageClose = dialog.findViewById(R.id.imageClose);
        TextView commentsCount = dialog.findViewById(R.id.comments_count_tv);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView sendButtonInCommentSheet = dialog.findViewById(R.id.sendButtonInCommentSheet);
        EditText EditTextInCommentsBottomDialog = dialog.findViewById(R.id.EditTextInCommentsBottomDialog);
        sendButtonInCommentSheet.setOnClickListener(view -> {
            if (!isReply) {
                DoComment(VideoId,
                        EditTextInCommentsBottomDialog.getText().toString().trim(),
                        dialog,
                        EditTextInCommentsBottomDialog, CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, textView);
            } else {
                DoReplyOverComment(replyCommentId, EditTextInCommentsBottomDialog.getText().toString().trim(), EditTextInCommentsBottomDialog, CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, textView, VideoId);
            }
            EditTextInCommentsBottomDialog.setText("");

        });
        adapter = new CommentsAdapter(SingleVideoActivity.this, comments, SingleVideoActivity.this);
        CommentsRecyclerView.setAdapter(adapter);
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(VideoId, "1000", "1");
        HitCommentsAPi(CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, VideoId, textView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void DoReplyOverComment(String CommentId, String Reply, EditText editText, RecyclerView CommentsRecyclerView,
                                    TextView commentsCount, TextView sendButtonInCommentSheet, TextView textView, String VideoId) {
        VideoCommentsReplyModel model = new VideoCommentsReplyModel(CommentId, Reply);
        apiManager.VideoCommentReply(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<VideoCommentsReplyModel>() {
            @Override
            public void onSuccess(VideoCommentsReplyModel videoCommentsReplyModel) {
                editText.setText("");
                CommentReply.setVisibility(View.GONE);
                isReply = false;
                replyToText.setText("");
                HitCommentsAPi(CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, VideoId, textView);
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void HitCommentsAPi(RecyclerView rv, TextView commentCount, TextView image, String VideoId, TextView textView) {
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(VideoId, "1000", "1");
        apiManager.ListOfVideoCommentsModel(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<ListOfVideoCommentsModel>() {
            @Override
            public void onSuccess(ListOfVideoCommentsModel listOfVideoCommentsModel) {
                comments.clear();
                comments.addAll(listOfVideoCommentsModel.getData());
                adapter.notifyDataSetChanged();
                image.setVisibility(View.VISIBLE);
                commentCount.setText(String.format("%s comments", listOfVideoCommentsModel.getData().size()));
                textView.setText(String.valueOf(listOfVideoCommentsModel.getData().size()));
                rv.scrollToPosition(listOfVideoCommentsModel.getData().size() - 1);
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void DoComment(String VideoId, String comment, Dialog context, EditText editText, RecyclerView CommentsRecyclerView,
                           TextView commentsCount, TextView sendButtonInCommentSheet, TextView textView
    ) {
        if (!comment.equalsIgnoreCase("")) {
            VideoCommentModel model = new VideoCommentModel(VideoId, comment);
            apiManager.VideoComment(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<VideoCommentModel>() {
                @Override
                public void onSuccess(VideoCommentModel videoCommentModel) {
                    editText.setText("");
                    HitCommentsAPi(CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, VideoId, textView);
                }

                @Override
                public void onError(ServerError serverError) {
                    Prompt.SnackBar(context.getCurrentFocus(), serverError.getErrorMsg());
                }
            });
        } else {

            return;
        }
    }

    public void reportVideo(String _id) {
        Dialog dialog = new Dialog(SingleVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.report_video_layout);

        ImageView backBtn = dialog.findViewById(R.id.backArrowBtn);
        EditText reportReason = dialog.findViewById(R.id.reason_et);
        TextView submitReportBtn = dialog.findViewById(R.id.submit_report_btn);

        backBtn.setOnClickListener(view -> dialog.dismiss());
        submitReportBtn.setOnClickListener(view -> {
            if (reportReason.getText().toString().trim().isEmpty()) {
                Toast.makeText(SingleVideoActivity.this, "Please tell us reason", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Prefs.GetBearerToken(SingleVideoActivity.this).isEmpty() || Prefs.GetBearerToken(SingleVideoActivity.this).equals("")) {
                Prompt.SnackBar(findViewById(android.R.id.content), "You must login first");
                return;
            }
            Dialogs.showProgressDialog(SingleVideoActivity.this);
            ReportVideoPojo model = new ReportVideoPojo(_id, reportReason.getText().toString());
            apiManager.reportVideo(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<ReportVideoRes>() {
                @Override
                public void onSuccess(ReportVideoRes videoRes) {
                    Dialogs.dismissProgressDialog();
                    dialog.dismiss();
                    Prompt.SnackBar(findViewById(android.R.id.content), videoRes.message);
                }

                @Override
                public void onError(ServerError serverError) {
                    Dialogs.dismissProgressDialog();
                    dialog.dismiss();
                    Prompt.SnackBar(findViewById(android.R.id.content), serverError.error);
                }
            });
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();

    }

    public void ShowReplySheet(String Name, String CommentId) {
        replyCommentId = CommentId;
        isReply = true;
        CommentReply.setVisibility(View.VISIBLE);
        replyToText.setText(Name);
    }

    public void moreOptions(String id) {
        openMoreOptionsDialog(id);
    }

    private void openMoreOptionsDialog(String id) {
        Dialog dialog = new Dialog(SingleVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.more_options_dialog);

        LinearLayout reportVideoBtn = dialog.findViewById(R.id.report_video_btn);
        reportVideoBtn.setOnClickListener(view -> {
            dialog.dismiss();
            reportVideo(id);
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
    }


    private void ShowUserListDialog(String videoLink) {
        if (VideoViewInHomeReels != null) {
            if (VideoViewInHomeReels.isPlaying()) {
                VideoViewInHomeReels.pause();
            }
        }
        UserListDialog = new Dialog(SingleVideoActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        chatListModels = new ArrayList<>();

        UserListDialog.setCancelable(false);
        UserListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        UserListDialog.setContentView(R.layout.user_list_bottom_dialog);
        ShareVideoUserListAdapter adapter = new ShareVideoUserListAdapter(UserListDialog.getContext(), SingleVideoActivity.this, chatListModels);
        RecyclerView rv = UserListDialog.findViewById(R.id.chatListRV);
        SearchView SearchUserForShare = UserListDialog.findViewById(R.id.SearchUserForShare);
        SearchUserForShare.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                GetUserListApi(newText, adapter);
                return false;
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(UserListDialog.getContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        LinearLayout ShareOnOtherApps = UserListDialog.findViewById(R.id.ShareOnOtherApps);
        ImageView imageCloseInShareVideo = UserListDialog.findViewById(R.id.imageCloseInShareVideo);
        imageCloseInShareVideo.setOnClickListener(view -> {
            UserListDialog.dismiss();
            if (VideoViewInHomeReels != null) {
                if (VideoViewInHomeReels.isPlaying()) {
                    VideoViewInHomeReels.start();
                }
            }
        });
        ShareOnOtherApps.setOnClickListener(view -> {
            UserListDialog.dismiss();
            Dialogs.showProgressDialog(SingleVideoActivity.this);
            new BackgroundVideoDownload(videoLink).execute();
        });
        Dialogs.createProgressDialog(SingleVideoActivity.this);
        GetUserListApi("", adapter);
        UserListDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        UserListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        UserListDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        UserListDialog.getWindow().setGravity(Gravity.BOTTOM);
        UserListDialog.setCancelable(true);
        UserListDialog.show();
    }

    private void GetUserListApi(String search, ShareVideoUserListAdapter adapter) {
        ChatListModel model = new ChatListModel(search);
        apiManager.GetUserChatList(Prefs.GetBearerToken(SingleVideoActivity.this), model, new DataCallback<ChatListModel>() {
            @Override
            public void onSuccess(ChatListModel chatListModel) {
                Dialogs.HideProgressDialog();
                chatListModels.clear();
                chatListModels.addAll(chatListModel.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
            }
        });
    }

    @Override
    public void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String ShortBio) {
        if (UserListDialog.isShowing()) {
            UserListDialog.dismiss();
        }
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new ChatFragment(Slug, ReceiverId, SenderId, UserImageLink, Username, ShortBio, 2, VideoId)).commit();
    }

    public static class BackgroundVideoDownload extends AsyncTask<String, String, String> {
        String Link;
        Context context;
        String Timestamp = HelperClass.getCurrentTimeStamp();
        String path = "";

        public BackgroundVideoDownload(String link) {
            Link = link;
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStream input = null;
            OutputStream output = null;
            String DestinationFilePath = Environment.getExternalStorageDirectory() + "/" + "Scriptube" + "/" + App.getContext().getPackageName() + "/" + "Shared_Videos" + "/";
            File fell = new File(DestinationFilePath);

            if (fell.exists()) {

            } else
                fell.mkdirs();


            try {
                URL url = new URL(Link);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d("downloadZipFile", "Server ResponseCode=" + connection.getResponseCode() + " ResponseMessage=" + connection.getResponseMessage());
                }
                input = connection.getInputStream();
                File f = new File(DestinationFilePath, Timestamp + ".mp4");
                path = f.getPath();
                output = new FileOutputStream(f);
                int fileLength = connection.getContentLength();

                byte[] data = new byte[4096];
                int count;
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;


                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null) output.close();
                    if (input != null) input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            File f = new File(DestinationFilePath);
            OpenIntent(path);

            return null;
        }
    }
}