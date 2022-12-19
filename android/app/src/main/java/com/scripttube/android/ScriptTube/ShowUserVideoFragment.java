package com.scripttube.android.ScriptTube;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.Adapters.CommentsAdapter;
import com.scripttube.android.ScriptTube.Adapters.UserVideosAdapter;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.DeleteVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetAllUserVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UpdateCaptionModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoCommentModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoCommentsReplyModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoLikeDislikeModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class ShowUserVideoFragment extends Fragment implements View.OnClickListener, CommentsAdapter.CommentReplyListener, UserVideosAdapter.HomeReelsListener {

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

    public ShowUserVideoFragment(List<GetAllUserVideoModel.Data> list, int position) {
        this.list = list;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.show_user_video_layout, container, false);
        inItWidgets(v);
        ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        backBtn.setOnClickListener(this);
        moreOptionsBtn.setOnClickListener(this);

        VideoAdapter = new UserVideosAdapter(getActivity(), list, ShowUserVideoFragment.this);
        ViewPagerOfUserVideo.setAdapter(VideoAdapter);
        ViewPagerOfUserVideo.setCurrentItem(position);


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
                        EditTextInCommentsBottomDialog, CommentsRecyclerView, commentsCount, sendButtonInCommentSheet, textView);
            } else {
                DoReplyOverComment(replyCommentId, EditTextInCommentsBottomDialog.getText().toString().trim(), EditTextInCommentsBottomDialog);
            }

        });
        adapter = new CommentsAdapter(v.getContext(), comments, ShowUserVideoFragment.this);
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

    @Override
    public void follow(String FollowerTo) {

    }

    @Override
    public void InitializingSlug(String slug, List<GetAllUserVideoModel.Data> list, int position, TextView caption, String link) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.more_options_user_video);
        LinearLayout edit_video_btn, delete_video_btn;
        edit_video_btn = dialog.findViewById(R.id.edit_video_btn);
        delete_video_btn = dialog.findViewById(R.id.delete_video_btn);
        edit_video_btn.setOnClickListener(view -> {
            dialog.hide();
            ShowEditVideoSheet(slug, caption, link);
        });
        delete_video_btn.setOnClickListener(view -> {
            dialog.hide();
            HitDeleteVideoApi(slug, list, position);
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void ShowEditVideoSheet(String slug, TextView caption, String link) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_video_caption_bottom_sheet);
        ImageView video_thumbnail_InEditCaption, backBtn_InEditVideo;
        EditText desc_edit_text_InEditCaption;
        CardView submitVideoBtn_InEditCaption;
        video_thumbnail_InEditCaption = dialog.findViewById(R.id.video_thumbnail_InEditCaption);
        Glide.with(v.getContext()).load(link).placeholder(R.drawable.placeholder).into(video_thumbnail_InEditCaption);
        backBtn_InEditVideo = dialog.findViewById(R.id.backBtn_InEditVideo);
        desc_edit_text_InEditCaption = dialog.findViewById(R.id.desc_edit_text_InEditCaption);
        submitVideoBtn_InEditCaption = dialog.findViewById(R.id.submitVideoBtn_InEditCaption);
        backBtn_InEditVideo.setOnClickListener(view -> dialog.hide());
        submitVideoBtn_InEditCaption.setOnClickListener(view -> {
            if (desc_edit_text_InEditCaption.getText().toString().trim().isEmpty()) {
                Prompt.SnackBar(dialog.getCurrentFocus(), "Enter Description");
            } else {
                dialog.hide();
                HitUpdateCaptionApi(slug, caption, desc_edit_text_InEditCaption.getText().toString().trim());
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void HitUpdateCaptionApi(String slug, TextView caption, String trim) {
        UpdateCaptionModel model = new UpdateCaptionModel(slug, trim);
        Dialogs.createProgressDialog(v.getContext());
        apiManager.UpdateVideoCaption(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<UpdateCaptionModel>() {
            @Override
            public void onSuccess(UpdateCaptionModel updateCaptionModel) {
                Dialogs.HideProgressDialog();
                caption.setText(updateCaptionModel.getData().getVideoCaption());
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
                    img.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.Red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    img.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void tryAudio() {

    }

    @Override
    public void moreOptions(String id) {

    }

    private void ShowCommentSheetDialog(String VideoId) {

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
        CommentReply.setVisibility(View.VISIBLE);
        replyToText.setText(Name);
    }
}
