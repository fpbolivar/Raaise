package com.scripttube.android.ScriptTube.Home.Fragments.Home_Following_foryou_Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.scripttube.android.ScriptTube.Adapters.CommentsAdapter;
import com.scripttube.android.ScriptTube.Adapters.HomeFollowingAdapter;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetGlobalVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoCommentModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoCommentsReplyModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoLikeDislikeModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Settings.Payments.PaymentMethods;
import com.scripttube.android.ScriptTube.Try_AudioActivity;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;
import com.scripttube.android.ScriptTube.model.ReportVideoPojo;
import com.scripttube.android.ScriptTube.model.ReportVideoRes;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Home_FollowingFragment extends Fragment implements HomeFollowingAdapter.HomeReelsListener, CommentsAdapter.CommentReplyListener {
    HomeFollowingAdapter homeFollowingAdapter;
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
    TextView replyToText;
    String replyCommentId;
    boolean isSelected;
    private boolean _hasLoadedOnce = false;
//    @Override
//    public void setUserVisibleHint(boolean isFragmentVisible_) {
//        super.setUserVisibleHint(true);
//        Log.e("Chaiii   ", "Following: "+"isFragmentVisible_ OC"+isFragmentVisible_ );
//        if (this.isVisible()) {
//            if (isFragmentVisible_ && !_hasLoadedOnce) {
//                Log.e("MOVED", "setUserVisibleHint: "+"HERE" );
//                // move exoplayer code here so this code run only when user select this fragment
////                HitGlobalVideoApi("following","10", String.valueOf(PageCounter));
//                _hasLoadedOnce = true;
//            }
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home__following, container, false);
        // Inflate the layout for this fragment
        Initialization(v);
        clickListeners();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (((Home) requireActivity()).adapterVideoVIew != null){
                    ((Home) requireActivity()).adapterVideoVIew.stopPlayback();
                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == list.size() - 5) {
                    PageCounter++;
                    HitGlobalVideoApi("following","10", String.valueOf(PageCounter));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        return v;
    }

    private void clickListeners() {
    }

    @Override
    public void onResume() {
        HitGlobalVideoApi("following","10", String.valueOf(PageCounter));
        if (((Home) requireActivity()).adapterVideoVIew != null && ((Home) requireActivity()).adapterVideoVIew.isPlaying())
            ((Home) requireActivity()).adapterVideoVIew.stopPlayback();
        if (homeFollowingAdapter != null){
            homeFollowingAdapter.clearList();
        }

        super.onResume();
    }

    private void Initialization(View v) {
        context = getActivity().getApplicationContext();
        viewPager2 = v.findViewById(R.id.ViewPager);
        homeFollowingAdapter = new HomeFollowingAdapter(context, list, Home_FollowingFragment.this);
        viewPager2.setAdapter(homeFollowingAdapter);
    }

    void HitGlobalVideoApi(String type, String limit, String page) {

        GetGlobalVideoModel model = new GetGlobalVideoModel(type, limit, page);
        apiManager.GetGlobalVideo(Prefs.GetBearerToken(context), model, new DataCallback<GetGlobalVideoModel>() {
            @Override
            public void onSuccess(GetGlobalVideoModel getGlobalVideoModel) {
//                Dialogs.HideProgressDialog();

                list.addAll(getGlobalVideoModel.getData());
                homeFollowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
//                Dialogs.HideProgressDialog();
//                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }


    @Override
    public void ShowCommentBottomSheetDialog(String VideoId,TextView textView) {
        ShowSearchUserDialog(VideoId,textView);
    }

    @Override
    public void follow(String FollowerTo) {
        UserFollowUnfollowModel model = new UserFollowUnfollowModel(FollowerTo);
        apiManager.UserFollowUnfollow(Prefs.GetBearerToken(context), model, new DataCallback<UserFollowUnfollowModel>() {
            @Override
            public void onSuccess(UserFollowUnfollowModel userFollowUnfollowModel) {
            }
            @Override
            public void onError(ServerError serverError) {
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
                    img.setColorFilter(ContextCompat.getColor(context, R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    img.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void ShowDonationDialog() {
        isSelected = false;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_donation_dialog);
        LinearLayout Donate = dialog.findViewById(R.id.dialog_btn_Donation);
        RelativeLayout Layout = dialog.findViewById(R.id.layout_donation_amount);
        ImageView Tick = dialog.findViewById(R.id.select_firstAmount_icon);
        Layout.setOnClickListener(view -> {
            isSelected = true;
            Layout.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.dialog_bg_select_amount));
            Tick.setVisibility(View.VISIBLE);
        });
        Donate.setOnClickListener(view -> {
            if (isSelected) {
                dialog.hide();
                startActivity(new Intent(v.getContext(), PaymentMethods.class));
            } else {
                Toast.makeText(getActivity(), "Select Amount First", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public void notificationClicked(VideoView videoView) {
        ((Home) requireActivity()).adapterVideoVIew = videoView;
    }


    private void ShowSearchUserDialog(String VideoId,TextView textView) {
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
                        EditTextInCommentsBottomDialog, CommentsRecyclerView, commentsCount, sendButtonInCommentSheet,textView);
            } else {
                DoReplyOverComment(replyCommentId, EditTextInCommentsBottomDialog.getText().toString().trim(), EditTextInCommentsBottomDialog);
            }

        });
        adapter = new CommentsAdapter(v.getContext(), comments, Home_FollowingFragment.this);
        CommentsRecyclerView.setAdapter(adapter);
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(VideoId, "1000", "1");
        HitCommentsAPi(CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, VideoId,textView);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
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

    private void HitCommentsAPi(RecyclerView rv, TextView commentCount, TextView image, String VideoId,TextView textView) {
        ListOfVideoCommentsModel model = new ListOfVideoCommentsModel(VideoId, "1000", "1");
        apiManager.ListOfVideoCommentsModel(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<ListOfVideoCommentsModel>() {
            @Override
            public void onSuccess(ListOfVideoCommentsModel listOfVideoCommentsModel) {
                comments.clear();
                comments.addAll(listOfVideoCommentsModel.getData());
                adapter.notifyDataSetChanged();
                image.setVisibility(View.VISIBLE);
                commentCount.setText(String.format("%s Comments", String.valueOf(listOfVideoCommentsModel.getData().size())));
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
                           TextView commentsCount, TextView sendButtonInCommentSheet,TextView textView
    ) {
        if (!comment.equalsIgnoreCase("")) {
            VideoCommentModel model = new VideoCommentModel(VideoId, comment);
            apiManager.VideoComment(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<VideoCommentModel>() {
                @Override
                public void onSuccess(VideoCommentModel videoCommentModel) {
                    editText.setText("");
                    HitCommentsAPi(CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, VideoId,textView);
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
    public void reportVideo(String _id) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.report_video_layout);

        ImageView backBtn = dialog.findViewById(R.id.backArrowBtn);
        EditText reportReason = dialog.findViewById(R.id.reason_et);
        TextView submitReportBtn = dialog.findViewById(R.id.submit_report_btn);

        backBtn.setOnClickListener(view -> dialog.dismiss());
        submitReportBtn.setOnClickListener(view -> {
            if (reportReason.getText().toString().trim().isEmpty()){
                Toast.makeText(getActivity(), "Please tell us reason", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Prefs.GetBearerToken(getActivity()).isEmpty() || Prefs.GetBearerToken(getActivity()).equals("")){
                Prompt.SnackBar(v, "You must login first");
                return;
            }
            Dialogs.showProgressDialog(getActivity());
            ReportVideoPojo model = new ReportVideoPojo(_id, reportReason.getText().toString());
            apiManager.reportVideo(Prefs.GetBearerToken(getActivity()), model, new DataCallback<ReportVideoRes>() {
                @Override
                public void onSuccess(ReportVideoRes videoRes) {
                    Dialogs.dismissProgressDialog();
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
    public void SharVideoLink(String Link) {}

    public void tryAudio() {
        startActivity(new Intent(getActivity(), Try_AudioActivity.class));
    }

    @Override
    public void moreOptions(String id) {
        openMoreOptionsDialog(id);
    }

    private void openMoreOptionsDialog(String id) {
        Dialog dialog = new Dialog(getActivity());
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

    public void ShowReplySheet(String Name,String CommentId) {
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
}