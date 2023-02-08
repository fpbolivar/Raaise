package com.raaise.android.Home.Fragments.Home_Following_foryou_Fragments;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.raaise.android.Adapters.CommentsAdapter;
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
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.Utilities.HelperClasses.ResumePlayForYou;
import com.raaise.android.Utilities.HelperClasses.StopFollowingVideo;
import com.raaise.android.Utilities.HelperClasses.StringHelper;
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


public class Home_ForYouFragment extends Fragment implements HomeFollowingAdapter.HomeReelsListener, CommentsAdapter.CommentReplyListener, StopFollowingVideo, ResumePlayForYou, ShareVideoUserListAdapter.ChatListListener {
    public List<ChatListModel.Data> chatListModels;
    HomeFollowingAdapter homeFollowingAdapter;
    List<GetGlobalVideoModel.Data> list = new ArrayList<>();
    View v;
    ViewPager2 viewPager2;
    ApiManager apiManager = App.getApiManager();
    CommentsAdapter adapter;
    int PageCounter = 1;
    List<ListOfVideoCommentsModel.Data> comments;
    RelativeLayout CommentReply;
    boolean isReply = false;
    TextView replyToText;
    String replyCommentId;
    boolean isSelected;
    VideoView videoView;
    boolean first = false;
    String VideoId;
    Dialog UserListDialog;

    private static void OpenIntent(String path) {

        try {
            File f = new File(path);

            String WaterMarkVideoName = "Scriptube_share" + new Date().getTime();
            String command = "-i " + f.getPath() + " -i " + StringHelper.WaterMarkLogo + " -filter_complex \"[1]scale=iw/4:-1[wm];[0][wm]  overlay=20:main_h-overlay_h\" " + Environment.getExternalStorageDirectory() + "/" + "Scriptube" + "/" + App.getContext().getPackageName() + "/" + "Shared_Videos" + "/" + WaterMarkVideoName + ".mp4";
            FFmpeg.executeAsync(command, (executionId, returnCode) -> {
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

            });
        } catch (Exception e) {
        }

    }

    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home__for_you, container, false);
        Initialization(v);
        clickListeners();
        HitGlobalVideoApi("1", String.valueOf(PageCounter));
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
                HitGlobalVideoApi("2", String.valueOf(PageCounter));




            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {







                }
            }
        });
        return v;
    }

    private void clickListeners() {
    }

    private void Initialization(View v) {
        viewPager2 = v.findViewById(R.id.ViewPagerForYou);
        homeFollowingAdapter = new HomeFollowingAdapter(v.getContext(), list, Home_ForYouFragment.this);
        viewPager2.setAdapter(homeFollowingAdapter);

    }

    @Override
    public void onResume() {

        if (((Home) requireActivity()).adapterVideoVIew != null && ((Home) requireActivity()).adapterVideoVIew.isPlaying())
            ((Home) requireActivity()).adapterVideoVIew.stopPlayback();

        super.onResume();
    }

    void HitGlobalVideoApi(String limit, String page) {
        GetGlobalVideoModel model = new GetGlobalVideoModel(limit, page);
        apiManager.GetGlobalVideo(Prefs.GetBearerToken(App.getContext()), model, new DataCallback<GetGlobalVideoModel>() {
            @Override
            public void onSuccess(GetGlobalVideoModel getGlobalVideoModel) {
                list.addAll(getGlobalVideoModel.getData());
                homeFollowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });
    }

    @Override
    public void ShowCommentBottomSheetDialog(String VideoId, TextView textView) {
        ShowSearchUserDialog(VideoId, textView);
    }

    @Override
    public void follow(String FollowerTo, TextView textView) {
        UserFollowUnfollowModel model = new UserFollowUnfollowModel(FollowerTo);
        apiManager.UserFollowUnfollow(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<UserFollowUnfollowModel>() {
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
        ShareVideoUserListAdapter adapter = new ShareVideoUserListAdapter(UserListDialog.getContext(), Home_ForYouFragment.this, chatListModels);
        RecyclerView rv = UserListDialog.findViewById(R.id.chatListRV);
        SearchView SearchUserForShare = UserListDialog.findViewById(R.id.SearchUserForShare);
        SearchUserForShare.setQueryHint(Html.fromHtml("<font color = #949292>" + "Search Users" + "</font>", Html.FROM_HTML_MODE_LEGACY));
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
            Dialogs.showProgressDialog(getActivity());
            new BackgroundVideoDownload(Prefs.GetBaseUrl(requireContext()) + videoLink).execute();
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
    public void VideoLikeDislike(String slug, ImageView Img, TextView LikeCount) {
        VideoLikeDislikeModel model = new VideoLikeDislikeModel(slug);
        apiManager.VideoLikeDislike(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<VideoLikeDislikeModel>() {
            @Override
            public void onSuccess(VideoLikeDislikeModel videoLikeDislikeModel) {
                LikeCount.setText(String.valueOf(videoLikeDislikeModel.getVideoCount()));
                if (videoLikeDislikeModel.isLike()) {
                    Img.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    Img.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void ShowDonationDialog(String UserId, String VideoId) {
        final Dialog dialog = new Dialog(getActivity());
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
        Dialog dialog = new Dialog(v.getContext());
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
        CommentsRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
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
        adapter = new CommentsAdapter(v.getContext(), comments, Home_ForYouFragment.this);
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
        apiManager.VideoCommentReply(Prefs.GetBearerToken(getActivity().getApplicationContext()), model, new DataCallback<VideoCommentsReplyModel>() {
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

    public void ShowReplySheet(String Name, String CommentId) {
        replyCommentId = CommentId;
        isReply = true;
        CommentReply.setVisibility(View.VISIBLE);
        replyToText.setText(Name);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (((Home) requireActivity()).adapterVideoVIew != null && ((Home) requireActivity()).adapterVideoVIew.isPlaying())
            ((Home) requireActivity()).adapterVideoVIew.stopPlayback();
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

    @Override
    public void StopPlayingFollowing() {
        if (videoView != null)
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    public void SharVideoView(VideoView videoView) {
        first = true;
        this.videoView = videoView;
        videoView.start();

    }

    @Override
    public void ResumePlay() {
        if (videoView != null)
        if (!videoView.isPlaying()) {
            videoView.start();
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