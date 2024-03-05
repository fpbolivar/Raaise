package com.raaise.android.Home.Fragments.Home_Following_foryou_Fragments;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.raaise.android.Adapters.CommentsAdapter;
import com.raaise.android.Adapters.CommentsReplyAdapter;
import com.raaise.android.Adapters.HomeFollowingAdapter;
import com.raaise.android.Adapters.ShareVideoUserListAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetGlobalVideoModel;
import com.raaise.android.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentsReplyModel;
import com.raaise.android.ApiManager.ApiModels.VideoLikeDislikeModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.BuildConfig;
import com.raaise.android.Home.Fragments.ChatFragment;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Settings.Payments.PaymentMethods;
import com.raaise.android.Try_AudioActivity;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.Utilities.HelperClasses.ResumePlayFollowing;
import com.raaise.android.Utilities.HelperClasses.StopForYouVideo;
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


public class Home_FollowingFragment extends Fragment implements HomeFollowingAdapter.HomeReelsListener, CommentsAdapter.CommentReplyListener, StopForYouVideo, ResumePlayFollowing, ShareVideoUserListAdapter.ChatListListener, CommentsReplyAdapter.VideoReplyListener {
    public static boolean doRefresh = false;
    private String COMMENT_REPLY_EDIT_ID = "";
    private boolean EDITING_COMMENT_REPLY = false;
    private boolean EDITING_COMMENT = false;
    private String videoID = "";
    private String videoTitle = "";
    private String EDIT_COMMENT_ID = "";
    private EditText EditTextInCommentsBottomDialog;
    public static HomeFollowingAdapter homeFollowingAdapter;
    private final boolean _hasLoadedOnce = false;
    public List<ChatListModel.Data> chatListModels;
    Context context;
    List<GetGlobalVideoModel.Data> list = new ArrayList<>();
    View v;
    ViewPager2 viewPager2;
    ApiManager apiManager = App.getApiManager();
    CommentsAdapter adapter;
    int PageCounter = 1;
    List<ListOfVideoCommentsModel.Data> comments;
    RelativeLayout CommentReply;
    boolean isReply = false;
    TextView replyToText, dataNotFoundTV;
    String replyCommentId;
    boolean isSelected;
    VideoView videoView;
    boolean first = false;
    int iZ = 0;
    Dialog UserListDialog;
    String VideoId;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home__following, container, false);

        Initialization(v);
        clickListeners();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (((Home) requireActivity()).adapterVideoVIew != null) {
                    ((Home) requireActivity()).adapterVideoVIew.stopPlayback();
                }


            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                PageCounter++;
                HitGlobalVideoApi("following", "4", String.valueOf(PageCounter));


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });

        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return v;
    }

    private void clickListeners() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (doRefresh){
            list.clear();
            homeFollowingAdapter.notifyDataSetChanged();
            PageCounter = 1;
            HitGlobalVideoApi("following", "4", String.valueOf(PageCounter));
            Log.i("userDetails", "onResume: yes");
            doRefresh = false;
        }
    }

    private void Initialization(View v) {
        context = getActivity().getApplicationContext();
        viewPager2 = v.findViewById(R.id.ViewPager);
        dataNotFoundTV = v.findViewById(R.id.dataNotFound);
        homeFollowingAdapter = new HomeFollowingAdapter(context, list, Home_FollowingFragment.this);
        viewPager2.setAdapter(homeFollowingAdapter);


    }

    void HitGlobalVideoApi(String type, String limit, String page) {
        ((Home) requireActivity()).videoProgressBar.setVisibility(View.VISIBLE);
        GetGlobalVideoModel model = new GetGlobalVideoModel(type, limit, page);
        apiManager.GetGlobalVideo(Prefs.GetBearerToken(context), model, new DataCallback<GetGlobalVideoModel>() {
            @Override
            public void onSuccess(GetGlobalVideoModel getGlobalVideoModel) {
                try {
                    ((Home) requireActivity()).videoProgressBar.setVisibility(View.GONE);
                } catch (Exception e){
                }
                list.addAll(getGlobalVideoModel.getData());
                homeFollowingAdapter.notifyDataSetChanged();
                dataNotFoundTV.setVisibility(View.GONE);


            }

            @Override
            public void onError(ServerError serverError) {
                if (list.size() == 0)
                dataNotFoundTV.setVisibility(View.VISIBLE);
                try {
                    ((Home) requireActivity()).videoProgressBar.setVisibility(View.GONE);
                } catch (Exception e){
                }
            }
        });
    }


    @Override
    public void SharVideoView(VideoView videoView) {
        first = true;
        this.videoView = videoView;

        if (iZ == 0 || iZ == 1) {

        } else {
            this.videoView.start();
        }
        iZ++;


    }

    @Override
    public void ShowCommentBottomSheetDialog(String VideoId, TextView textView) {
        ShowSearchUserDialog(VideoId, textView);
    }

    @Override
    public void follow(String FollowerTo, TextView textView) {
        UserFollowUnfollowModel model = new UserFollowUnfollowModel(FollowerTo);
        apiManager.UserFollowUnfollow(Prefs.GetBearerToken(context), model, new DataCallback<UserFollowUnfollowModel>() {
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
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void ShowShareVideoDialog(String videoLink, String UserId, String UserImage, String UserShortBio, String UserName, String VideoId) {
        this.VideoId = VideoId;

        ShowUserListDialog(videoLink, UserId, UserImage, UserShortBio, UserName, VideoId);

    }

    private void ShowUserListDialog(String videoLink, String userId, String userImage, String userShortBio, String userName, String videoId) {
        UserListDialog = new Dialog(v.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        chatListModels = new ArrayList<>();

        UserListDialog.setCancelable(false);
        UserListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        UserListDialog.setContentView(R.layout.user_list_bottom_dialog);
        ShareVideoUserListAdapter adapter = new ShareVideoUserListAdapter(UserListDialog.getContext(), Home_FollowingFragment.this, chatListModels);
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
        Dialogs.createProgressDialog(v.getContext());
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
        apiManager.GetUserChatList(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<ChatListModel>() {
            @Override
            public void onSuccess(ChatListModel chatListModel) {
                chatListModels.clear();
                Dialogs.HideProgressDialog();
                chatListModels.addAll(chatListModel.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void VideoLikeDislike(String slug, ImageView img, TextView LikeCount) {
        VideoLikeDislikeModel model = new VideoLikeDislikeModel(slug);
        apiManager.VideoLikeDislike(Prefs.GetBearerToken(context), model, new DataCallback<VideoLikeDislikeModel>() {
            @Override
            public void onSuccess(VideoLikeDislikeModel videoLikeDislikeModel) {
                LikeCount.setText(String.valueOf(videoLikeDislikeModel.getVideoCount()));
                if (videoLikeDislikeModel.isLike()) {
                    img.setImageDrawable(getContext().getDrawable(R.drawable.like_icon));
                } else {
                    img.setImageDrawable(getContext().getDrawable(R.drawable.like_icon_white));
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void ShowDonationDialog(String UserId, String VideoId, GetGlobalVideoModel.Data obj) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.supports_amount_dialog);
        ImageView Donate = dialog.findViewById(R.id.dialog_btn_Donation);
        EditText donation_amount_ET = dialog.findViewById(R.id.donation_amount_ET);
        RelativeLayout Layout = dialog.findViewById(R.id.layout_donation_amount);
        ImageView Tick = dialog.findViewById(R.id.select_firstAmount_icon);


        Donate.setOnClickListener(view -> {
            if (!donation_amount_ET.getText().toString().trim().equalsIgnoreCase("")) {
                if ((Long.parseLong(donation_amount_ET.getText().toString().trim()) * 1) != 0) {
                    dialog.dismiss();
                    startActivity(new Intent(v.getContext(), PaymentMethods.class).putExtra("AmountToDonate", donation_amount_ET.getText().toString().trim()).putExtra("DonateTo", UserId).putExtra("VideoId", VideoId));
                } else {
                    Toast.makeText(getActivity(), "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Enter Amount First", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public void notificationClicked(VideoView videoView) {
        ((Home) requireActivity()).adapterVideoVIew = videoView;
    }


    private void ShowSearchUserDialog(String VideoId, TextView textView) {
        videoID = videoID;
        Dialog dialog = new Dialog(v.getContext());
        EDITING_COMMENT = false;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comments_bottom_sheet);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
        CommentsRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        comments = new ArrayList<>();
        ImageView imageClose = dialog.findViewById(R.id.imageClose);
        TextView commentsCount = dialog.findViewById(R.id.comments_count_tv);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                EDITING_COMMENT = false;
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
                DoReplyOverComment(replyCommentId, EditTextInCommentsBottomDialog.getText().toString().trim(), EditTextInCommentsBottomDialog);
            }
            EditTextInCommentsBottomDialog.setText("");

        });
        adapter = new CommentsAdapter(v.getContext(), comments, Home_FollowingFragment.this, Home_FollowingFragment.this);
        CommentsRecyclerView.setAdapter(adapter);
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(VideoId, "1000", "1");
        HitCommentsAPi(CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, VideoId, textView);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
    }

    private void hitCommentReplyEditApi(String replyID, String reply) {
        Dialogs.createProgressDialog(getContext());
        CommentReplyPojo model = new CommentReplyPojo(replyID, reply);
        apiManager.editCommentReply(Prefs.GetBearerToken(getContext()), model, new DataCallback<VideoCommentDelete>() {
            @Override
            public void onSuccess(VideoCommentDelete videoCommentDelete) {
                EDITING_COMMENT_REPLY = false;
                hitCommentApi();
                Dialogs.HideProgressDialog();
            }

            @Override
            public void onError(ServerError serverError) {
                Toast.makeText(getContext(), serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                Dialogs.HideProgressDialog();
            }
        });
    }
    private void hitEditVdoCommentApi(String edit_comment_id, String comment) {
        Dialogs.createProgressDialog(getContext());
        EditVideoCmntPojo pojo = new EditVideoCmntPojo(edit_comment_id, comment);
        apiManager.editVideoComment(Prefs.GetBearerToken(getContext()), pojo, new DataCallback<VideoCommentDelete>() {
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

    private void DoReplyOverComment(String CommentId, String Reply, EditText editText) {
        VideoCommentsReplyModel model = new VideoCommentsReplyModel(CommentId, Reply);
        apiManager.VideoCommentReply(Prefs.GetBearerToken(getActivity().getApplicationContext()), model, new DataCallback<VideoCommentsReplyModel>() {
            @Override
            public void onSuccess(VideoCommentsReplyModel videoCommentsReplyModel) {
                editText.setText("");
                CommentReply.setVisibility(View.GONE);
                isReply = false;
                replyToText.setText("");
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    private void HitCommentsAPi(RecyclerView rv, TextView commentCount, TextView image, String VideoId, TextView textView) {
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(VideoId, "1000", "1");
        apiManager.ListOfVideoCommentsModel(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<ListOfVideoCommentsModel>() {
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
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    private void DoComment(String VideoId, String comment, Dialog context, EditText editText, RecyclerView CommentsRecyclerView,
                           TextView commentsCount, TextView sendButtonInCommentSheet, TextView textView
    ) {

        if (!comment.equalsIgnoreCase("")) {
            VideoCommentModel model = new VideoCommentModel(VideoId, comment);
            apiManager.VideoComment(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<VideoCommentModel>() {
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


    public void reportVideo(List<GetGlobalVideoModel.Data> list, int position, ImageView img) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.report_video_layout);

        ImageView backBtn = dialog.findViewById(R.id.backArrowBtn);
        EditText reportReason = dialog.findViewById(R.id.reason_et);
        TextView submitReportBtn = dialog.findViewById(R.id.submit_report_btn);

        backBtn.setOnClickListener(view -> dialog.dismiss());
        submitReportBtn.setOnClickListener(view -> {
            if (reportReason.getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), "Please tell us reason", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Prefs.GetBearerToken(getActivity()).isEmpty() || Prefs.GetBearerToken(getActivity()).equals("")) {
                Prompt.SnackBar(v, "You must login first");
                return;
            }
            Dialogs.showProgressDialog(getActivity());
            ReportVideoPojo model = new ReportVideoPojo(list.get(position).get_id(), reportReason.getText().toString());
            apiManager.reportVideo(Prefs.GetBearerToken(getActivity()), model, new DataCallback<ReportVideoRes>() {
                @Override
                public void onSuccess(ReportVideoRes videoRes) {
                    Dialogs.dismissProgressDialog();

                    list.get(position).setReported(true);
                    dialog.dismiss();
                    Prompt.SnackBar(v, videoRes.message);
                }

                @Override
                public void onError(ServerError serverError) {
                    Dialogs.dismissProgressDialog();
                    dialog.dismiss();
                    Prompt.SnackBar(v, serverError.error);
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
    public void tryAudio(String AudioId) {
        startActivity(new Intent(getActivity(), Try_AudioActivity.class).putExtra("AudioId", AudioId));
    }

    @Override
    public void moreOptions(List<GetGlobalVideoModel.Data> list, int position, ImageView img, boolean isReported) {
        openMoreOptionsDialog(list, position, img, isReported);
    }

    private void openMoreOptionsDialog(List<GetGlobalVideoModel.Data> list, int position, ImageView img, Boolean isReported) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.more_options_dialog);

        LinearLayout reportVideoBtn = dialog.findViewById(R.id.report_video_btn);
        LinearLayout blockVideoBtn = dialog.findViewById(R.id.block_video_btn);
        blockVideoBtn.setOnClickListener(view -> {
            dialog.dismiss();
            Dialogs.createProgressDialog(getContext());
            BlockVideoPojo model = new BlockVideoPojo(list.get(position)._id);
            apiManager.blockVideo(Prefs.GetBearerToken(getContext()), model, new DataCallback<VideoCommentDelete>() {
                @Override
                public void onSuccess(VideoCommentDelete videoCommentDelete) {
                    Dialogs.HideProgressDialog();
                    Prompt.SnackBar(v, videoCommentDelete.message);
                }

                @Override
                public void onError(ServerError serverError) {
                    Dialogs.HideProgressDialog();
                    Prompt.SnackBar(v, serverError.getErrorMsg());
                }
            });
        });
        reportVideoBtn.setOnClickListener(view -> {
            dialog.dismiss();
            if (isReported) {
                Prompt.SnackBar(v, "Video Already Reported");
            } else {
                reportVideo(list, position, img);
            }
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
        if (EDITING_COMMENT){
            EDITING_COMMENT = false;
        }
        CommentReply.setVisibility(View.VISIBLE);
        replyToText.setText(Name);
    }

    @Override
    public void moreOptionsClicked(String commentID, String comment) {
        Dialog dialog = new Dialog(getActivity());
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

    private void editCmnt(String comment, Dialog dialog, String commentID) {
        EDITING_COMMENT = true;
        dialog.dismiss();
        if (!EditTextInCommentsBottomDialog.getText().toString().equalsIgnoreCase("")){
            EditTextInCommentsBottomDialog.setText("");
        }
        EditTextInCommentsBottomDialog.setText(comment);
        EDIT_COMMENT_ID = commentID;
    }

    private void deleteVdoComment(String videoID, String commentID, Dialog dialog) {
        Dialogs.createProgressDialog(getContext());
        DeleteCommentPojo pojo = new DeleteCommentPojo(videoID, commentID);
        apiManager.deleteVideoComment(Prefs.GetBearerToken(requireContext()), pojo, new DataCallback<VideoCommentDelete>() {
            @Override
            public void onSuccess(VideoCommentDelete videoCommentDelete) {
                Dialogs.HideProgressDialog();
                Toast.makeText(getContext(), videoCommentDelete.message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                hitCommentApi();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Toast.makeText(getContext(), serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    private void hitCommentApi() {
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(videoID, "1000", "1");
        apiManager.ListOfVideoCommentsModel(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<ListOfVideoCommentsModel>() {
            @Override
            public void onSuccess(ListOfVideoCommentsModel listOfVideoCommentsModel) {
                comments.clear();
                comments.addAll(listOfVideoCommentsModel.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (((Home) requireActivity()).adapterVideoVIew != null && ((Home) requireActivity()).adapterVideoVIew.isPlaying())
            ((Home) requireActivity()).adapterVideoVIew.stopPlayback();
    }

    @Override
    public void StopPlayingForYou() {

        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        }

    }

    @Override
    public void ResumePlay() {
        if (videoView != null) {
            if (!videoView.isPlaying()) {
                videoView.start();
            }
        }
    }

    @Override
    public void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String
            ShortBio) {
        if (UserListDialog.isShowing()) {
            UserListDialog.dismiss();
        }
        ((Home) requireActivity()).fragmentManagerHelper.replace(new ChatFragment(Slug, ReceiverId, SenderId, UserImageLink, Username, ShortBio, 1, VideoId), true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            this.iZ = 0;
            Log.i("fragmentResume", "onResume: Following");
            HitGlobalVideoApi("following", "1", String.valueOf(PageCounter));
            if (((Home) requireActivity()).adapterVideoVIew != null && ((Home) requireActivity()).adapterVideoVIew.isPlaying())
                ((Home) requireActivity()).adapterVideoVIew.stopPlayback();
        }
    }

    @Override
    public void VdoRplMoreOptions(String id, String reply) {
        Dialog dialog = new Dialog(getActivity());
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


    private void editCommentReply(String rplId, String reply) {
        EDITING_COMMENT_REPLY = true;
        COMMENT_REPLY_EDIT_ID = rplId;
        EditTextInCommentsBottomDialog.setText(reply);
    }

    private void deleteCommentReply(String id) {
        DeleteCommentReply model = new DeleteCommentReply(id);
        Dialogs.createProgressDialog(getContext());
        apiManager.deleteCommentReply(Prefs.GetBearerToken(getContext()), model, new DataCallback<VideoCommentDelete>() {
            @Override
            public void onSuccess(VideoCommentDelete videoCommentDelete) {
                hitCommentApi();
                Dialogs.HideProgressDialog();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Toast.makeText(getContext(), serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void beginDownload(String audioLink) {
        try {
            Dialogs.createProgressDialog(getContext());
            DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(Prefs.GetBaseUrl(getContext()) + audioLink));


            String title = URLUtil.guessFileName(audioLink, null, null);
            videoTitle = title;

            downloadRequest.setTitle(title);

            downloadRequest.setDescription("Downloading, Please Wait...!!!");

            String cookie = CookieManager.getInstance().getCookie(audioLink);

            downloadRequest.addRequestHeader("cookie", cookie);

            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

            downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);


            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(downloadRequest);
        } catch (Exception e) {
            Dialogs.HideProgressDialog();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}