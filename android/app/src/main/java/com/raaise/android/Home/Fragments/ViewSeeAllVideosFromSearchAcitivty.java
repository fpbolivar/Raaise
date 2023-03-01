package com.raaise.android.Home.Fragments;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raaise.android.Adapters.CommentsAdapter;
import com.raaise.android.Adapters.CommentsReplyAdapter;
import com.raaise.android.Adapters.ShareVideoUserListAdapter;
import com.raaise.android.Adapters.ViewSeeAllVideosFromSearchAcitivtyAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
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
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.Utilities.HelperClasses.StringHelper;
import com.raaise.android.model.BlockVideoPojo;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.CommentReplyPojo;
import com.raaise.android.model.DeleteCommentPojo;
import com.raaise.android.model.DeleteCommentReply;
import com.raaise.android.model.EditVideoCmntPojo;
import com.raaise.android.model.ReportVideoPojo;
import com.raaise.android.model.ReportVideoRes;
import com.raaise.android.model.VideoCommentDelete;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ViewSeeAllVideosFromSearchAcitivty extends AppCompatActivity implements ViewSeeAllVideosFromSearchAcitivtyAdapter.HomeReelsListener,
        CommentsReplyAdapter.VideoReplyListener, CommentsAdapter.CommentReplyListener, ShareVideoUserListAdapter.ChatListListener {
    private String COMMENT_REPLY_EDIT_ID = "";
    private boolean EDITING_COMMENT_REPLY = false;
    public List<ChatListModel.Data> chatListModels;
    int position;
    private String videoID = "";
    private String EDIT_COMMENT_ID = "";
    private boolean EDITING_COMMENT = false;
    private EditText EditTextInCommentsBottomDialog;
    ViewPager2 ViewPagerOfViewSeeAllVideosFromSearchAcitivty;
    ImageView back_btn_in_ViewPagerOfViewSeeAllVideosFromSearchAcitivty;
    ViewSeeAllVideosFromSearchAcitivtyAdapter VideoAdapter;
    List<GlobalSearchModel.Data.Posts> list;
    ApiManager apiManager = App.getApiManager();
    CommentsAdapter adapter;
    List<ListOfVideoCommentsModel.Data> comments;
    RelativeLayout CommentReply;
    boolean isReply = false;
    TextView replyToText;
    String replyCommentId;
    boolean isSelected;
    String VideoId;
    Dialog UserListDialog;
    private String videoTitle = "";
    private long downloadID;
    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadID == id) {
                shareIntent();
            }
        }
    };

    private void shareIntent() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + "/Download/" + videoTitle);

        String WaterMarkVideoName = "Scriptube_share" + new Date().getTime();
        String command = "-i " + file.getPath() + " -i " + StringHelper.WaterMarkLogo + " -filter_complex \"[1]scale=iw/4:-1[wm];[0][wm]  overlay=20:main_h-overlay_h\" " + Environment.getExternalStorageDirectory().getPath() + "/Download/"  + WaterMarkVideoName + ".mp4";
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                try {
                    File shareFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/"  + WaterMarkVideoName + ".mp4");
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("video/mp4");
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Objects.requireNonNull(App.getContext()), BuildConfig.APPLICATION_ID + ".provider", shareFile));
                    App.getContext().startActivity(Intent.createChooser(intent, "share").setFlags(FLAG_ACTIVITY_NEW_TASK));
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Dialogs.HideProgressDialog();

        });
    }

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
        setContentView(R.layout.activity_view_see_all_videos_from_search_acitivty);
        Initialization();
        ClickListeners();

        VideoAdapter = new ViewSeeAllVideosFromSearchAcitivtyAdapter(ViewSeeAllVideosFromSearchAcitivty.this, list, ViewSeeAllVideosFromSearchAcitivty.this);
        ViewPagerOfViewSeeAllVideosFromSearchAcitivty.setAdapter(VideoAdapter);
        ViewPagerOfViewSeeAllVideosFromSearchAcitivty.setCurrentItem(position);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void Initialization() {
        list = new ArrayList<>();
        ViewPagerOfViewSeeAllVideosFromSearchAcitivty = findViewById(R.id.ViewPagerOfViewSeeAllVideosFromSearchAcitivty);
        back_btn_in_ViewPagerOfViewSeeAllVideosFromSearchAcitivty = findViewById(R.id.back_btn_in_ViewPagerOfViewSeeAllVideosFromSearchAcitivty);
        Intent i = getIntent();
        list = new Gson().fromJson(i.getStringExtra("ListOfSeeAllVideosPlaying"), new TypeToken<List<GlobalSearchModel.Data.Posts>>() {
        }.getType());

        position = Integer.parseInt(i.getStringExtra("PositionListOfSeeAllVideosPlaying"));

    }

    private void ClickListeners() {
        back_btn_in_ViewPagerOfViewSeeAllVideosFromSearchAcitivty.setOnClickListener(view -> finish());
    }

    @Override
    public void ShowCommentBottomSheetDialog(String VideoId, TextView textView) {
        videoID = VideoId;
        EDITING_COMMENT = false;
        Dialog dialog = new Dialog(ViewSeeAllVideosFromSearchAcitivty.this);
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
        CommentsRecyclerView.setLayoutManager(new LinearLayoutManager(ViewSeeAllVideosFromSearchAcitivty.this));
        comments = new ArrayList<>();
        ImageView imageClose = dialog.findViewById(R.id.imageClose);
        TextView commentsCount = dialog.findViewById(R.id.comments_count_tv);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EDITING_COMMENT = false;
                EDITING_COMMENT_REPLY = false;
                dialog.dismiss();
            }
        });
        TextView sendButtonInCommentSheet = dialog.findViewById(R.id.sendButtonInCommentSheet);
        EditTextInCommentsBottomDialog = dialog.findViewById(R.id.EditTextInCommentsBottomDialog);
        sendButtonInCommentSheet.setOnClickListener(view -> {
            if (EDITING_COMMENT_REPLY){
                hitCommentReplyEditApi(COMMENT_REPLY_EDIT_ID, EditTextInCommentsBottomDialog.getText().toString());
            } else
            if (EDITING_COMMENT){
                hitEditVdoCommentApi(EDIT_COMMENT_ID, EditTextInCommentsBottomDialog.getText().toString());
            } else
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
        adapter = new CommentsAdapter(ViewSeeAllVideosFromSearchAcitivty.this, comments, ViewSeeAllVideosFromSearchAcitivty.this, ViewSeeAllVideosFromSearchAcitivty.this);
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

    private void hitCommentReplyEditApi(String replyID, String reply) {
        Dialogs.createProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
        CommentReplyPojo model = new CommentReplyPojo(replyID, reply);
        apiManager.editCommentReply(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<VideoCommentDelete>() {
            @Override
            public void onSuccess(VideoCommentDelete videoCommentDelete) {
                EDITING_COMMENT_REPLY = false;
                hitCommentApi();
                Dialogs.HideProgressDialog();
            }

            @Override
            public void onError(ServerError serverError) {
                Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                Dialogs.HideProgressDialog();
            }
        });
    }

    private void hitEditVdoCommentApi(String edit_comment_id, String comment) {
        Dialogs.createProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
        EditVideoCmntPojo pojo = new EditVideoCmntPojo(edit_comment_id, comment);
        apiManager.editVideoComment(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), pojo, new DataCallback<VideoCommentDelete>() {
            @Override
            public void onSuccess(VideoCommentDelete videoCommentDelete) {
                EDITING_COMMENT = false;
                EditTextInCommentsBottomDialog.setText("");
                Dialogs.HideProgressDialog();
                hitCommentApi();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
            }
        });
    }

    private void DoReplyOverComment(String CommentId, String Reply, EditText editText, RecyclerView CommentsRecyclerView,
                                    TextView commentsCount, TextView sendButtonInCommentSheet, TextView textView, String VideoId) {
        VideoCommentsReplyModel model = new VideoCommentsReplyModel(CommentId, Reply);
        apiManager.VideoCommentReply(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<VideoCommentsReplyModel>() {
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
        apiManager.ListOfVideoCommentsModel(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<ListOfVideoCommentsModel>() {
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
            apiManager.VideoComment(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<VideoCommentModel>() {
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

    @Override
    public void follow(String FollowerTo) {
        UserFollowUnfollowModel model = new UserFollowUnfollowModel(FollowerTo);
        apiManager.UserFollowUnfollow(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<UserFollowUnfollowModel>() {
            @Override
            public void onSuccess(UserFollowUnfollowModel userFollowUnfollowModel) {
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void InitializingSlug(String slug, String captionText, List<GlobalSearchModel.Data.Posts> list, int position, TextView caption, String link) {

    }

    @Override
    public void VideoLikeDislike(String slug, ImageView img, TextView likecount) {
        VideoLikeDislikeModel model = new VideoLikeDislikeModel(slug);
        apiManager.VideoLikeDislike(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<VideoLikeDislikeModel>() {
            @Override
            public void onSuccess(VideoLikeDislikeModel videoLikeDislikeModel) {
                likecount.setText(String.valueOf(videoLikeDislikeModel.getVideoCount()));
                if (videoLikeDislikeModel.isLike()) {
                    img.setColorFilter(ContextCompat.getColor(ViewSeeAllVideosFromSearchAcitivty.this, R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    img.setColorFilter(ContextCompat.getColor(ViewSeeAllVideosFromSearchAcitivty.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void tryAudio(String AudioId) {

        startActivity(new Intent(ViewSeeAllVideosFromSearchAcitivty.this, Try_AudioActivity.class).putExtra("AudioId", AudioId));
    }

    @Override
    public void moreOptions(String id) {
        openMoreOptionsDialog(id);
    }

    private void openMoreOptionsDialog(String id) {
        Dialog dialog = new Dialog(ViewSeeAllVideosFromSearchAcitivty.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.more_options_dialog);

        LinearLayout reportVideoBtn = dialog.findViewById(R.id.report_video_btn);
        LinearLayout blockVideoBtn = dialog.findViewById(R.id.block_video_btn);
        blockVideoBtn.setOnClickListener(view -> {
            dialog.dismiss();
            Dialogs.createProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
            BlockVideoPojo model = new BlockVideoPojo(list.get(position)._id);
            apiManager.blockVideo(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<VideoCommentDelete>() {
                @Override
                public void onSuccess(VideoCommentDelete videoCommentDelete) {
                    Dialogs.HideProgressDialog();
                    Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, videoCommentDelete.message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(ServerError serverError) {
                    Dialogs.HideProgressDialog();
                    Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            });
        });
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

    @Override
    public void ShowShareVideoDialog(String videoLink, String UserId, String UserImage, String UserShortBio, String UserName, String VideoId) {
        this.VideoId = VideoId;
        ShowUserListDialog(videoLink, UserId, UserImage, UserShortBio, UserName, VideoId);
    }

    private void ShowUserListDialog(String videoLink, String userId, String userImage, String userShortBio, String userName, String videoId) {
        UserListDialog = new Dialog(ViewSeeAllVideosFromSearchAcitivty.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        chatListModels = new ArrayList<>();

        UserListDialog.setCancelable(false);
        UserListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        UserListDialog.setContentView(R.layout.user_list_bottom_dialog);
        ShareVideoUserListAdapter adapter = new ShareVideoUserListAdapter(UserListDialog.getContext(), ViewSeeAllVideosFromSearchAcitivty.this, chatListModels);
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
        });
        ShareOnOtherApps.setOnClickListener(view -> {
            UserListDialog.dismiss();
                beginDownload(videoLink);
        });
        Dialogs.createProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
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
        apiManager.GetUserChatList(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<ChatListModel>() {
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
    public void ShowDonationDialog(String UserId, String VideoId) {
        final Dialog dialog = new Dialog(ViewSeeAllVideosFromSearchAcitivty.this);
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
                    startActivity(new Intent(ViewSeeAllVideosFromSearchAcitivty.this, PaymentMethods.class).putExtra("AmountToDonate", donation_amount_ET.getText().toString().trim()).putExtra("DonateTo", UserId).putExtra("VideoId", VideoId));
                } else {
                    Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, "Enter Amount First", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public void notificationClicked(VideoView view) {

    }


    public void reportVideo(String _id) {
        Dialog dialog = new Dialog(ViewSeeAllVideosFromSearchAcitivty.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.report_video_layout);

        ImageView backBtn = dialog.findViewById(R.id.backArrowBtn);
        EditText reportReason = dialog.findViewById(R.id.reason_et);
        TextView submitReportBtn = dialog.findViewById(R.id.submit_report_btn);

        backBtn.setOnClickListener(view -> dialog.dismiss());
        submitReportBtn.setOnClickListener(view -> {
            if (reportReason.getText().toString().trim().isEmpty()) {
                Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, "Please tell us reason", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this).isEmpty() || Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this).equals("")) {
                Prompt.SnackBar(findViewById(android.R.id.content), "You must login first");
                return;
            }
            Dialogs.showProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
            ReportVideoPojo model = new ReportVideoPojo(_id, reportReason.getText().toString());
            apiManager.reportVideo(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<ReportVideoRes>() {
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

    @Override
    public void SharVideoLink(String Link) {

    }

    @Override
    public void ShowReplySheet(String Name, String CommentId) {
        replyCommentId = CommentId;
        isReply = true;
        if (EDITING_COMMENT_REPLY){
            EDITING_COMMENT_REPLY = false;
        }
        if (EDITING_COMMENT){
            EDITING_COMMENT = false;
        }
        CommentReply.setVisibility(View.VISIBLE);
        replyToText.setText(Name);
    }

    @Override
    public void moreOptionsClicked(String commentID, String comment) {
        Dialog dialog = new Dialog(ViewSeeAllVideosFromSearchAcitivty.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comment_more_options);

        LinearLayout editComment = dialog.findViewById(R.id.edit_comment);
        TextView deleteVideoComment = dialog.findViewById(R.id.delete_comment);
        deleteVideoComment.setOnClickListener(view -> deleteVdoComment(videoID, commentID, dialog));
        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCmnt(comment, dialog, commentID);
            }
        });


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
    }
    private void deleteVdoComment(String videoID, String commentID, Dialog dialog) {
        Dialogs.createProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
        DeleteCommentPojo pojo = new DeleteCommentPojo(videoID, commentID);
        apiManager.deleteVideoComment(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), pojo, new DataCallback<VideoCommentDelete>() {
            @Override
            public void onSuccess(VideoCommentDelete videoCommentDelete) {
                Dialogs.HideProgressDialog();
                Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, videoCommentDelete.message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                hitCommentApi();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    private void hitCommentApi() {
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(videoID, "1000", "1");
        apiManager.ListOfVideoCommentsModel(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<ListOfVideoCommentsModel>() {
            @Override
            public void onSuccess(ListOfVideoCommentsModel listOfVideoCommentsModel) {
                comments.clear();
                comments.addAll(listOfVideoCommentsModel.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }


    private void editCmnt(String comment, Dialog dialog, String commentID) {
        EDITING_COMMENT = true;
        EDITING_COMMENT_REPLY = false;
        dialog.dismiss();
        if (!EditTextInCommentsBottomDialog.getText().toString().equalsIgnoreCase("")){
            EditTextInCommentsBottomDialog.setText("");
        }
        EditTextInCommentsBottomDialog.setText(comment);
        EDIT_COMMENT_ID = commentID;
    }


    @Override
    public void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String ShortBio) {
        if (UserListDialog.isShowing()) {
            UserListDialog.dismiss();
        }
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new ChatFragment(Slug, ReceiverId, SenderId, UserImageLink, Username, ShortBio, 2, VideoId)).commit();

    }

    @Override
    public void VdoRplMoreOptions(String id, String reply) {
        Dialog dialog = new Dialog(ViewSeeAllVideosFromSearchAcitivty.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comment_more_options);

        TextView deleteReply = dialog.findViewById(R.id.delete_comment);
        deleteReply.setOnClickListener(view -> {
            dialog.dismiss();
            deleteCommentReply(id);
        });
        LinearLayout editReply = dialog.findViewById(R.id.edit_comment);
        editReply.setOnClickListener(view -> {
            dialog.dismiss();
            editCommentReply(id, reply);
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
    }
    private void deleteCommentReply(String id) {
        DeleteCommentReply model = new DeleteCommentReply(id);
        Dialogs.createProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
        apiManager.deleteCommentReply(Prefs.GetBearerToken(ViewSeeAllVideosFromSearchAcitivty.this), model, new DataCallback<VideoCommentDelete>() {
            @Override
            public void onSuccess(VideoCommentDelete videoCommentDelete) {
                hitCommentApi();
                Dialogs.HideProgressDialog();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void editCommentReply(String rplId, String reply) {
        EDITING_COMMENT_REPLY = true;
        COMMENT_REPLY_EDIT_ID = rplId;
        EditTextInCommentsBottomDialog.setText(reply);
    }

    private void beginDownload(String audioLink) {
        try {
            Dialogs.createProgressDialog(ViewSeeAllVideosFromSearchAcitivty.this);
            DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(Prefs.GetBaseUrl(ViewSeeAllVideosFromSearchAcitivty.this) + audioLink));


            String title = URLUtil.guessFileName(audioLink, null, null);
            videoTitle = title;

            downloadRequest.setTitle(title);

            downloadRequest.setDescription("Downloading, Please Wait...!!!");

            String cookie = CookieManager.getInstance().getCookie(audioLink);

            downloadRequest.addRequestHeader("cookie", cookie);

            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

            downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);


            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(downloadRequest);
        } catch (Exception e) {
            Dialogs.HideProgressDialog();
            Toast.makeText(ViewSeeAllVideosFromSearchAcitivty.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}