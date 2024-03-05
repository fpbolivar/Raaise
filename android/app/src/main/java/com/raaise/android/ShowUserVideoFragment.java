package com.raaise.android;

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
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.google.firebase.BuildConfig;
import com.raaise.android.Adapters.CommentsAdapter;
import com.raaise.android.Adapters.CommentsReplyAdapter;
import com.raaise.android.Adapters.ShareVideoUserListAdapter;
import com.raaise.android.Adapters.UserVideosAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.DeleteVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetAllUserVideoModel;
import com.raaise.android.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.raaise.android.ApiManager.ApiModels.UpdateCaptionModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentsReplyModel;
import com.raaise.android.ApiManager.ApiModels.VideoLikeDislikeModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.Fragments.ChatFragment;
import com.raaise.android.Home.Fragments.ShowOtherUserVideoActivity;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.Utilities.HelperClasses.StringHelper;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.CommentReplyPojo;
import com.raaise.android.model.DeleteCommentPojo;
import com.raaise.android.model.DeleteCommentReply;
import com.raaise.android.model.EditVideoCmntPojo;
import com.raaise.android.model.VideoCommentDelete;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ShowUserVideoFragment extends Fragment implements View.OnClickListener,
        CommentsReplyAdapter.VideoReplyListener, CommentsAdapter.CommentReplyListener, UserVideosAdapter.HomeReelsListener, ShareVideoUserListAdapter.ChatListListener {
    private String COMMENT_REPLY_EDIT_ID = "";
    private boolean EDITING_COMMENT_REPLY = false;
    private String videoID = "";
    private String EDIT_COMMENT_ID = "";
    private boolean EDITING_COMMENT = false;
    private EditText EditTextInCommentsBottomDialog;
    public static int valStatic = 0;
    public List<ChatListModel.Data> chatListModels;
    ImageView backBtn;
    ImageView moreOptionsBtn;
    CommentsAdapter adapter;
    View v;
    String replyCommentId;
    List<ListOfVideoCommentsModel.Data> comments;
    RelativeLayout CommentReply;
    ApiManager apiManager = App.getApiManager();
    boolean isReply = false;
    TextView replyToText;
    List<GetAllUserVideoModel.Data> list;
    ViewPager2 ViewPagerOfUserVideo;
    UserVideosAdapter VideoAdapter;
    int position;
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
                    if (shareFile.exists()){
                        Log.i("sharingFile", "shareIntent: Exist");
                    }
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("video/mp4");
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Objects.requireNonNull(App.getContext()), com.raaise.android.BuildConfig.APPLICATION_ID + ".provider", shareFile));
                    App.getContext().startActivity(Intent.createChooser(intent, "share").setFlags(FLAG_ACTIVITY_NEW_TASK));
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    Log.i("sharingFile", "shareIntent: " + e.getMessage());
                    e.printStackTrace();
                }

            }
            Dialogs.HideProgressDialog();

        });
    }

    public ShowUserVideoFragment(List<GetAllUserVideoModel.Data> list, int position) {
        this.list = list;
        this.position = position;
    }

    private static void OpenIntent(String path) {

        try {
            File f = new File(path);
            String WaterMarkVideoName = "Raaise_share" + new Date().getTime();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.show_user_video_layout, container, false);
        valStatic = 1;
        inItWidgets(v);
        ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        backBtn.setOnClickListener(this);
        moreOptionsBtn.setOnClickListener(this);

        VideoAdapter = new UserVideosAdapter(getActivity(), list, ShowUserVideoFragment.this);
        ViewPagerOfUserVideo.setAdapter(VideoAdapter);
        ViewPagerOfUserVideo.setCurrentItem(position);
        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return v;
    }

    private void inItWidgets(View view) {
        ViewPagerOfUserVideo = view.findViewById(R.id.ViewPagerOfUserVideo);
        backBtn = view.findViewById(R.id.back_btn);
        moreOptionsBtn = view.findViewById(R.id.more_options_btn);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                requireActivity().onBackPressed();
                break;
            case R.id.more_options_btn:
                showMoreOptionsDialog();
                break;


        }
    }

    @Override
    public void ShowCommentBottomSheetDialog(String VideoId, TextView textView) {
        videoID = VideoId;
        EDITING_COMMENT = false;
        Dialog dialog = new Dialog(v.getContext());
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
            sendButtonInCommentSheet.setEnabled(false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendButtonInCommentSheet.setEnabled(true);
                    if (!isReply) {
                        DoComment(VideoId,
                                EditTextInCommentsBottomDialog.getText().toString().trim(),
                                dialog,
                                EditTextInCommentsBottomDialog, CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, textView);
                    } else {
                        DoReplyOverComment(replyCommentId, EditTextInCommentsBottomDialog.getText().toString().trim(), EditTextInCommentsBottomDialog);
                    }
                }
            }, 2000);

        });
        adapter = new CommentsAdapter(v.getContext(), comments, ShowUserVideoFragment.this, ShowUserVideoFragment.this);
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
        apiManager.editCommentReply(Prefs.GetBearerToken(requireContext()), model, new DataCallback<VideoCommentDelete>() {
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
        ShareVideoUserListAdapter adapter = new ShareVideoUserListAdapter(UserListDialog.getContext(), ShowUserVideoFragment.this, chatListModels);
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
                Dialogs.HideProgressDialog();
                chatListModels.clear();
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
    public void follow(String FollowerTo) {
    }

    @Override
    public void InitializingSlug(String slug, String captionText, List<GetAllUserVideoModel.Data> list, int position, TextView caption, String link) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.more_options_user_video);
        LinearLayout edit_video_btn, delete_video_btn;
        edit_video_btn = dialog.findViewById(R.id.edit_video_btn);
        delete_video_btn = dialog.findViewById(R.id.delete_video_btn);
        edit_video_btn.setOnClickListener(view -> {
            dialog.dismiss();

            ShowEditVideoSheet(slug, caption, link, captionText, list, position);
        });
        delete_video_btn.setOnClickListener(view -> {
            dialog.dismiss();

            showConfirmationDialog(slug, list, position);

        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showConfirmationDialog(String slug, List<GetAllUserVideoModel.Data> list, int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_video_dialog);

        TextView deleteVdoBtn = dialog.findViewById(R.id.yes_btn);
        TextView noBtn = dialog.findViewById(R.id.no_btn);
        noBtn.setOnClickListener(view -> dialog.dismiss());

        deleteVdoBtn.setOnClickListener(view -> {
            dialog.dismiss();
            HitDeleteVideoApi(slug, list, position);
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.show();

    }

    private void ShowEditVideoSheet(String slug, TextView caption, String link, String captionText, List<GetAllUserVideoModel.Data> list, int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_video_caption_bottom_sheet);
        ImageView video_thumbnail_InEditCaption, backBtn_InEditVideo;
        EditText desc_edit_text_InEditCaption;
        RelativeLayout submitVideoBtn_InEditCaption;
        video_thumbnail_InEditCaption = dialog.findViewById(R.id.video_thumbnail_InEditCaption);
        Glide.with(v.getContext()).load(link).placeholder(R.drawable.placeholder).into(video_thumbnail_InEditCaption);
        backBtn_InEditVideo = dialog.findViewById(R.id.backBtn_InEditVideo);
        desc_edit_text_InEditCaption = dialog.findViewById(R.id.desc_edit_text_InEditCaption);
        submitVideoBtn_InEditCaption = dialog.findViewById(R.id.submitVideoBtn_InEditCaption);
        desc_edit_text_InEditCaption.setText(captionText);
        backBtn_InEditVideo.setOnClickListener(view -> dialog.hide());
        submitVideoBtn_InEditCaption.setOnClickListener(view -> {
            if (desc_edit_text_InEditCaption.getText().toString().trim().isEmpty()) {
                Prompt.SnackBar(dialog.getCurrentFocus(), "Enter Description");
            } else {
                dialog.dismiss();
                HitUpdateCaptionApi(slug, caption, desc_edit_text_InEditCaption.getText().toString().trim(), list, position);
                UserVideosAdapter.ViewHolder.VideoViewInHomeReels.start();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
        UserVideosAdapter.ViewHolder.VideoViewInHomeReels.pause();





    }

    private void HitUpdateCaptionApi(String slug, TextView caption, String trim, List<GetAllUserVideoModel.Data> list, int position) {
        UpdateCaptionModel model = new UpdateCaptionModel(slug, trim);
        Dialogs.createProgressDialog(v.getContext());
        apiManager.UpdateVideoCaption(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<UpdateCaptionModel>() {
            @Override
            public void onSuccess(UpdateCaptionModel updateCaptionModel) {
                Dialogs.HideProgressDialog();
                caption.setText(updateCaptionModel.getData().getVideoCaption());
                list.get(position).setVideoCaption(updateCaptionModel.getData().getVideoCaption());
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
            }
        });
    }

    private void HitDeleteVideoApi(String slug, List<GetAllUserVideoModel.Data> list, int position) {
        DeleteVideoModel model = new DeleteVideoModel(slug);
        Dialogs.createProgressDialog(v.getContext());
        apiManager.DeleteUserVIDEO(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<DeleteVideoModel>() {
            @Override
            public void onSuccess(DeleteVideoModel deleteVideoModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(v, deleteVideoModel.getMessage());
                list.remove(position);
                VideoAdapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
            }
        });
    }

    @Override
    public void VideoLikeDislike(String slug, ImageView img, TextView LikeCount) {
        VideoLikeDislikeModel model = new VideoLikeDislikeModel(slug);
        apiManager.VideoLikeDislike(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<VideoLikeDislikeModel>() {
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
    public void tryAudio(String AudioId) {
        startActivity(new Intent(getActivity(), Try_AudioActivity.class).putExtra("AudioId", AudioId));
    }

    @Override
    public void moreOptions(String id) {

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

    private void showMoreOptionsDialog() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
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
        Dialog dialog = new Dialog(getContext());
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
        EDITING_COMMENT_REPLY = false;
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
        apiManager.deleteVideoComment(Prefs.GetBearerToken(getContext()), pojo, new DataCallback<VideoCommentDelete>() {
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
        apiManager.ListOfVideoCommentsModel(Prefs.GetBearerToken(getContext()), model, new DataCallback<ListOfVideoCommentsModel>() {
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


    @Override
    public void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String ShortBio) {
        if (UserListDialog.isShowing()) {
            UserListDialog.dismiss();
        }
        ((Home) requireActivity()).fragmentManagerHelper.replace(new ChatFragment(Slug, ReceiverId, SenderId, UserImageLink, Username, ShortBio, 1, VideoId), true);

    }

    @Override
    public void VdoRplMoreOptions(String id, String reply) {
        Dialog dialog = new Dialog(getContext());
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
        Dialogs.createProgressDialog(getContext());
        apiManager.deleteCommentReply(Prefs.GetBearerToken(requireContext()), model, new DataCallback<VideoCommentDelete>() {
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
    private void editCommentReply(String rplId, String reply) {
        EDITING_COMMENT_REPLY = true;
        COMMENT_REPLY_EDIT_ID = rplId;
        EditTextInCommentsBottomDialog.setText(reply);
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
