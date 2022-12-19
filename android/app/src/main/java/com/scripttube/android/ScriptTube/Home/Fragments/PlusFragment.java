package com.scripttube.android.ScriptTube.Home.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scripttube.android.ScriptTube.Adapters.AmountAdapter;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetCategoryModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.ApiUtilities;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Select_AudioActivity;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.model.MusicData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlusFragment extends Fragment implements AmountAdapter.AmountListener {
    public boolean isDonation = false;
    public String donationFromRV = "";
    String mergedVideoName;
    ImageView backBtn;
    Context context;
    VideoView videoView;
    ImageView Iv_Btn_Donation;
    LinearLayout SelectAudio;
    ApiManager apiManager = App.getApiManager();
    String[] arrayOfCategories;
    String[] arrayOfCodes;
    Spinner selectCategorySpin;
    private EditText donationAmtTV;
    private String MERGED_VIDEO_PATH = "";
    private TextView selectAudioTV;
    String CategoryId;
    int size ;

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plus, container, false);
        Initialization(v);

        videoView.setVideoURI(Uri.parse(((Home) requireActivity()).videoPath));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                videoView.start();
            }
        });

        SelectAudio.setOnClickListener(v1 -> {
            if (videoView.isPlaying())
                videoView.stopPlayback();
            if (Prefs.GetBearerToken(getActivity()).equals("")) {
                Toast.makeText(getActivity(), "You must Login first", Toast.LENGTH_SHORT).show();
            } else
                ((Home) requireActivity()).fragmentManagerHelper.replace(new Select_AudioActivity(), true);
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButtonClicked();
            }
        });

        Iv_Btn_Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                }

                Bitmap thumb = MERGED_VIDEO_PATH.equals("") ? ThumbnailUtils.createVideoThumbnail(((Home) requireActivity()).videoPath, MediaStore.Images.Thumbnails.MICRO_KIND) : ThumbnailUtils.createVideoThumbnail(MERGED_VIDEO_PATH, MediaStore.Images.Thumbnails.MINI_KIND);

                // Making file out of Bitmap to upload to server.
                showUploadVideoDialog(thumb);
            }
        });
        return v;
    }

    private void Initialization(View v) {
        selectAudioTV = v.findViewById(R.id.select_audio_tv);
        backBtn = v.findViewById(R.id.backButton);
        context = getActivity().getApplicationContext();
        videoView = v.findViewById(R.id.pv);
        SelectAudio = v.findViewById(R.id.select_audio);
        Iv_Btn_Donation = v.findViewById(R.id.Iv_Click_btn_donation);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((Home) requireActivity()).shouldMergeAudio) {
            merge(((Home) requireActivity()).videoUri);
            MusicData data = new Gson().fromJson(((Home) requireActivity()).musicData, MusicData.class);
            selectAudioTV.setText(data.songName);
        }
    }

    private void merge(String videoUri) {
        mergedVideoName = "merged" + new Date().getTime();
        Dialogs.showProgressDialog(getActivity());
        String[] c = {"-i", getRealPathFromUri(getActivity(), Uri.parse(videoUri))
                , "-i", Environment.getExternalStorageDirectory().getPath()
                + "/Download/" + ((Home) requireActivity()).musicTitle
                , "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest",
                Environment.getExternalStorageDirectory().getPath()
                        + "/Download/" + mergedVideoName + ".mp4"};
        MergeVideo(c);
    }

    private void MergeVideo(String[] co) {
        FFmpeg.executeAsync(co, (executionId, returnCode) -> {
            Dialogs.dismissProgressDialog();
            if (videoView.isPlaying())
                videoView.stopPlayback();

            MERGED_VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + "/Download/" + mergedVideoName + ".mp4";
            videoView.setVideoPath(MERGED_VIDEO_PATH);
            videoView.start();

        });
    }

    private void handleBackButtonClicked() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure to quit?")
                .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Home) requireActivity()).musicTitle = "";
                        ((Home) requireActivity()).musicData = "";
                        ((Home) requireActivity()).videoPath = "";
                        ((Home) requireActivity()).videoUri = "";
                        ((Home) requireActivity()).SelectHomeScreen();
                    }
                })
                .setNegativeButton("Back to editing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog1 = dialog.create();
        dialog1.show();
    }

    private void showUploadVideoDialog(Bitmap thumb) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_video_layout);
        dialog.setCancelable(false);
        File thumbFile = makeFileOutOfImage(thumb, ".jpg");

        ArrayList<String> amountList = new ArrayList<>();
        setupAmountList(amountList);
        List<GetCategoryModel.Data> categories = new ArrayList<>();
        apiManager.GetCategories(Prefs.GetBearerToken(getActivity()), new DataCallback<GetCategoryModel>() {
            @Override
            public void onSuccess(GetCategoryModel getCategoryModel) {
//                categories.addAll(GetCategoryModel.Data);
               size = getCategoryModel.getData().size();
                categories.addAll(getCategoryModel.getData());
                arrayOfCategories = new String[size];
                arrayOfCodes = new String[size];
                for (int i = 0; i < size; i++) {
                    arrayOfCategories[i] = getCategoryModel.getData().get(i).getName();
                    arrayOfCodes[i] = getCategoryModel.getData().get(i).get_id();
                }
                ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), R.layout.select_category_spin_text);
                ad.add("Select Category");
                ad.addAll(arrayOfCategories);

                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectCategorySpin.setAdapter(ad);
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });

        donationAmtTV = dialog.findViewById(R.id.donation_amount_tv);
        LinearLayout parentLayout = dialog.findViewById(R.id.amountParentLayout);
        RecyclerView donationAmtRV = dialog.findViewById(R.id.amount_rv);
        SwitchCompat toggleBtn = dialog.findViewById(R.id.donationToggleBtn);
        ImageView thumbnailImage = dialog.findViewById(R.id.video_thumbnail);
        ImageView backButton = dialog.findViewById(R.id.backBtn);
        CardView submitBtn = dialog.findViewById(R.id.submitVideoBtn);
        EditText videoDescText = dialog.findViewById(R.id.desc_edit_text);
        selectCategorySpin = dialog.findViewById(R.id.select_category_spin);

        videoDescText.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        AmountAdapter adapter = new AmountAdapter(getActivity(), amountList, this::amountSelected);
        donationAmtRV.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        donationAmtRV.setAdapter(adapter);
        toggleBtn.setChecked(false);

        donationAmtTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    adapter.checkedPosition = -1;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDonation = true;
                    parentLayout.setVisibility(View.VISIBLE);
                } else {
                    isDonation = false;
                    parentLayout.setVisibility(View.GONE);
                    donationAmtTV.setText("");
                    donationFromRV = "";
                }
            }
        });

        Glide.with(getActivity())
                .load(thumb)
                .into(thumbnailImage);
        backButton.setOnClickListener(view -> {
            dialog.dismiss();
            if (!videoView.isPlaying())
                videoView.start();
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Prefs.GetBearerToken(getActivity()).equals("")) {
                    Toast.makeText(getActivity(), "You must login first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (videoDescText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please add description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectCategorySpin.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
                    Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    SelectCategorieId(selectCategorySpin.getSelectedItem().toString());
                }
                RequestBody audioID = null;
                RequestBody CategoryID = null;
                RequestBody donationAmt;
                // Getting video caption
                RequestBody videoCaption = RequestBody.create(MediaType.parse("text/plain"), videoDescText.getText().toString());

                if (isDonation) {
                    if (donationFromRV.equals("")) {
                        Toast.makeText(getActivity(), "Please select Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (donationAmtTV.getText().toString().trim().equals(""))
                        donationAmt = RequestBody.create(MediaType.parse("text/plain"), donationFromRV);
                    else
                        donationAmt = RequestBody.create(MediaType.parse("text/plain"), donationAmtTV.getText().toString());
                } else {
                    donationAmt = RequestBody.create(MediaType.parse("text/plain"), "");
                }

                if (((Home) requireActivity()).shouldMergeAudio) {
                    MusicData data = new Gson().fromJson(((Home) requireActivity()).musicData, MusicData.class);
                    if (data._id != null)
                        audioID = RequestBody.create(MediaType.parse("text/plain"), data._id);
                } else {
                    audioID = RequestBody.create(MediaType.parse("text/plain"), "");
                }

                String compressesVdoName = "compressed" + new Date().getTime();
                String[] commandArray;
                if (((Home) requireActivity()).shouldMergeAudio) {
                    commandArray = new String[]{"-y", "-i", Environment.getExternalStorageDirectory().getPath() + "/Download/" + mergedVideoName + ".mp4", "-s", "1280x720", "-r", "25", "-vcodec", "mpeg4", "-b:v", "300k", "-b:a", "48000", "-ac", "2", "-ar", "22050",
                            Environment.getExternalStorageDirectory().getPath() + "/Download/" + compressesVdoName + ".mp4"};

                } else {
                    commandArray = new String[]{"-y", "-i", getRealPathFromUri(getActivity(), Uri.parse(((Home) requireActivity()).videoUri)), "-s", "1280x720", "-r", "25", "-vcodec", "mpeg4", "-b:v", "300k", "-b:a", "48000", "-ac", "2", "-ar", "22050",
                            Environment.getExternalStorageDirectory().getPath() + "/Download/" + compressesVdoName + ".mp4"};
                }
                RequestBody requestFileImg = RequestBody.create(MediaType.parse("image/*"), thumbFile);
                MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", thumbFile.getName(), requestFileImg);

                CategoryID = RequestBody.create(MediaType.parse("text/plain"),CategoryId);
                RequestBody finalAudioID = audioID;
                RequestBody finalCategoryID = CategoryID;
                Dialogs.showProgressDialog(getActivity());
                FFmpeg.executeAsync(commandArray, new ExecuteCallback() {
                    @Override
                    public void apply(long executionId, int returnCode) {
                        Dialogs.dismissProgressDialog();
                        File vdoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + compressesVdoName + ".mp4");
                        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), vdoFile);
                        MultipartBody.Part vdoBody = MultipartBody.Part.createFormData("video", vdoFile.getName(), requestFile);

                        Dialogs.showProgressDialog(getActivity());
                        dialog.dismiss();
                        ApiUtilities.getApiInterface().uploadVideo(Prefs.GetBearerToken(getActivity()), videoCaption, isDonation, donationAmt, finalAudioID, finalCategoryID, vdoBody, imageBody).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Log.e("VideoUploading", "onFailure: " + response);
                                Log.e("VideoUploading", "onFailure: " + response.body());
                                Log.e("VideoUploading", "onFailure: " + response.code());
                                Log.e("VideoUploading", "onFailure: " + response.isSuccessful());
                                Log.e("VideoUploading", "onFailure: " + response.message());
                                if (response.isSuccessful()) {
                                    try {
                                        File mergedVideo = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + mergedVideoName + ".mp4");
                                        File audioFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + ((Home) requireActivity()).musicTitle);
                                        File capturedVideo = new File(getRealPathFromUri(getActivity(), Uri.parse(((Home) requireActivity()).videoUri)));
                                        if (audioFile.exists()) {
                                            audioFile.delete();
                                        }
                                        if (capturedVideo.exists()) {
                                            capturedVideo.delete();
                                        }
                                        if (mergedVideo.exists()) {
                                            mergedVideo.delete();
                                        }
                                        if (vdoFile.exists()) {
                                            vdoFile.delete();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                                    }
                                    ((Home) requireActivity()).shouldMergeAudio = false;
                                    ((Home) requireActivity()).musicData = "";
                                    ((Home) requireActivity()).musicTitle = "";
                                    MERGED_VIDEO_PATH = "";
                                    Dialogs.dismissProgressDialog();

                                    ((Home) requireActivity()).SelectHomeScreen();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Dialogs.dismissProgressDialog();
                                Log.e("VideoUploading", "onFailure: " + t.getLocalizedMessage());
                                Toast.makeText(getActivity(), t.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void SelectCategorieId(String toString) {
        for (int i = 0; i <= size; i++) {
            try {
                if (arrayOfCategories[i].matches(toString)) {
                    CategoryId = arrayOfCodes[i];


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void setupAmountList(ArrayList<String> amountList) {
        amountList.add("10");
        amountList.add("50");
        amountList.add("100");
        amountList.add("150");
    }

    public File makeFileOutOfImage(Bitmap thumb, String name) {
        // Making file out of Bitmap to upload to server.
        File f = new File(context.getCacheDir(), name);
        try {
            f.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);

            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public void amountSelected(String amount) {
        donationFromRV = amount;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Home) requireActivity()).shouldMergeAudio = false;
        Log.i("onDetach", "onDetach: Called");
    }
}