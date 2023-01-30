package com.raaise.android.Home.Fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.raaise.android.Adapters.AmountAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetCategoryModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.ApiUtilities;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Select_AudioActivity;
import com.raaise.android.Try_AudioActivity;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.MusicData;

import java.io.ByteArrayOutputStream;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TryAudioMergeVideo extends Fragment implements AmountAdapter.AmountListener {
    public boolean isDonation = false;
    public String donationFromRV = "";
    public boolean isMergingAgain = false;
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
    String CategoryId;
    int size;
    boolean shouldMergeAudio = true;
    String VideoPath, AudioLink, AudioId, AudioName;
    View v;
    int from;
    private EditText donationAmtTV;
    private String MERGED_VIDEO_PATH = "";
    private TextView selectAudioTV;


    public TryAudioMergeVideo(String videoPath, String AudioLink, String AudioId, String AudioName, int from) {
        this.VideoPath = videoPath;
        this.AudioLink = AudioLink;
        this.AudioId = AudioId;
        this.AudioName = AudioName;
        this.from = from;
    }

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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_try_audio_merge_video, container, false);
        this.getActivity()
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Initialization();
        Dialogs.createProgressDialog(v.getContext());
        new BackgroundAudioDownload(AudioLink).execute();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                videoView.start();
            }
        });
        Iv_Btn_Donation.setOnClickListener(view -> {
            if (videoView.isPlaying()) {
                videoView.pause();
            }

            Bitmap thumb = MERGED_VIDEO_PATH.equals("") ? ThumbnailUtils.createVideoThumbnail(((Home) requireActivity()).videoPath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND) : ThumbnailUtils.createVideoThumbnail(MERGED_VIDEO_PATH, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

            // Making file out of Bitmap to upload to server.
            showUploadVideoDialog(thumb);
        });
        selectAudioTV.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.try_audio_container, new Select_AudioActivity(true, VideoPath), null)
                    .commit();
        });

        return v;
    }

    private void Initialization() {
        selectAudioTV = v.findViewById(R.id.select_audio_tv);
        backBtn = v.findViewById(R.id.backButtonInTryAudio);
        context = v.getContext().getApplicationContext();
        videoView = v.findViewById(R.id.pv1);
        SelectAudio = v.findViewById(R.id.select_audio);
        Iv_Btn_Donation = v.findViewById(R.id.Iv_Click_btn_donation1);
        selectAudioTV.setText(AudioName);
        backBtn.setOnClickListener(view -> {


            if (from == 1) {
                getActivity().finish();
            } else if (from == 2) {
                ((Home) requireActivity()).SelectHomeScreen();
            }


        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MusicData data = new Gson().fromJson(Prefs.GetExtra(getContext(), "OverSelectedMusicData"), MusicData.class);
        if (data != null) {
            AudioLink = data.getAudio();
            AudioId = data.getId();
            AudioName = data.getSongName();
            selectAudioTV.setText(AudioName);
            Dialogs.createProgressDialog(v.getContext());
            new BackgroundAudioDownload(data.getAudio()).execute();
        }
    }

    public void merge(String videoUri) {
        Dialogs.HideProgressDialog();
        mergedVideoName = "merged" + new Date().getTime();
        String[] c = {"-i", VideoPath
                , "-i", videoUri
                , "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest",
                Environment.getExternalStorageDirectory().getPath()
                        + "/Download/" + mergedVideoName + ".mp4"};
        MergeVideo(c);
    }

    public void MergeVideo(String[] co) {
        FFmpeg.executeAsync(co, (executionId, returnCode) -> {
            Dialogs.dismissProgressDialog();
            if (videoView.isPlaying())
                videoView.stopPlayback();

            MERGED_VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + "/Download/" + mergedVideoName + ".mp4";
            videoView.setVideoPath(MERGED_VIDEO_PATH);
            videoView.start();

        });
    }

    @Override
    public void amountSelected(String amount) {

    }


    private void showUploadVideoDialog(Bitmap thumb) {
        Dialog dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_video_layout);
        dialog.setCancelable(false);
        File thumbFile = makeFileOutOfImage(thumb, ".jpg");

        ArrayList<String> amountList = new ArrayList<>();
        setupAmountList(amountList);
        List<GetCategoryModel.Data> categories = new ArrayList<>();
        apiManager.GetCategories(Prefs.GetBearerToken(v.getContext()), new DataCallback<GetCategoryModel>() {
            @Override
            public void onSuccess(GetCategoryModel getCategoryModel) {
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

        videoDescText.setTextColor(ContextCompat.getColor(v.getContext(), R.color.white));
        AmountAdapter adapter = new AmountAdapter(v.getContext(), amountList, this::amountSelected);
        donationAmtRV.setLayoutManager(new GridLayoutManager(v.getContext(), 4));
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

        Glide.with(v.getContext())
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
                if (Prefs.GetBearerToken(v.getContext()).equals("")) {
                    Toast.makeText(v.getContext(), "You must login first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (videoDescText.getText().toString().trim().equals("")) {
                    Toast.makeText(v.getContext(), "Please add description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectCategorySpin.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
                    Toast.makeText(v.getContext(), "Please Select Category", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SelectCategorieId(selectCategorySpin.getSelectedItem().toString());
                }
                RequestBody audioID = null;
                RequestBody CategoryID = null;
                RequestBody donationAmt;
                // Getting video caption
                RequestBody videoCaption = RequestBody.create(MediaType.parse("text/plain"), videoDescText.getText().toString());

                if (isDonation) {
                    if (donationAmtTV.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Please Enter Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ((Long.parseLong(donationAmtTV.getText().toString().trim()) * 1) == 0) {
                        Toast.makeText(getActivity(), " Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (donationAmtTV.getText().toString().trim().equals(""))
                        donationAmt = RequestBody.create(MediaType.parse("text/plain"), donationFromRV);
                    else
                        donationAmt = RequestBody.create(MediaType.parse("text/plain"), donationAmtTV.getText().toString());
                } else {
                    donationAmt = RequestBody.create(MediaType.parse("text/plain"), "");
                }
                audioID = RequestBody.create(MediaType.parse("text/plain"), AudioId);
//                if (shouldMergeAudio) {
//                    MusicData data = new Gson().fromJson(musicData, MusicData.class);
//                    if (data._id != null)
//                        audioID = RequestBody.create(MediaType.parse("text/plain"), data._id);
//                } else {
//                    audioID = RequestBody.create(MediaType.parse("text/plain"), "");
//                }

//                String compressesVdoName = "compressed" + new Date().getTime();
//                String[] commandArray;
//                commandArray = new String[]{"-y", "-i", Environment.getExternalStorageDirectory().getPath() + "/Download/" + mergedVideoName + ".mp4", "-s", "1080x1920", "-r", "25", "-vcodec", "mpeg4", "-b:v", "300k", "-b:a", "48000", "-ac", "2", "-ar", "22050",
//                        Environment.getExternalStorageDirectory().getPath() + "/Download/" + compressesVdoName + ".mp4"};

                RequestBody requestFileImg = RequestBody.create(MediaType.parse("image/*"), thumbFile);
                MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", thumbFile.getName(), requestFileImg);

                CategoryID = RequestBody.create(MediaType.parse("text/plain"), CategoryId);
                RequestBody finalAudioID = audioID;
                RequestBody finalCategoryID = CategoryID;
                Dialogs.createProgressDialog(requireActivity());
//                FFmpeg.executeAsync(commandArray, new ExecuteCallback() {
//                    @Override
//                    public void apply(long executionId, int returnCode) {
                Dialogs.HideProgressDialog();
                File vdoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + mergedVideoName + ".mp4");
//                        File vdoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + compressesVdoName + ".mp4");
                RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), vdoFile);
                MultipartBody.Part vdoBody = MultipartBody.Part.createFormData("video", vdoFile.getName(), requestFile);
                Dialogs.createProgressDialog(requireActivity());

                ApiUtilities.getApiInterface().uploadVideo(Prefs.GetBearerToken(v.getContext()), videoCaption, isDonation, donationAmt, finalAudioID, finalCategoryID, vdoBody, imageBody).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                        if (response.isSuccessful()) {
                            try {
                                File mergedVideo = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + mergedVideoName + ".mp4");
                                File audioFile = new File(Environment.getDataDirectory() + "/Demo/");
                                File capturedVideo = new File(VideoPath);
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
                                Toast.makeText(v.getContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }

                            MERGED_VIDEO_PATH = "";
                            Dialogs.HideProgressDialog();
//                                    if (from == 2) {
//                                        getActivity().finish();
                            ((Try_AudioActivity) requireActivity()).FinishHere();
//                                    }
//                                    else {
                            getActivity().getSupportFragmentManager().popBackStack();
//                                    }
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Dialogs.HideProgressDialog();

                        Toast.makeText(v.getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//                    }
//                });
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void SelectCategorieId(String toString) {
        for (int i = 0; i < size; i++) {
            try {
                if (arrayOfCategories[i].matches(toString)) {
                    CategoryId = arrayOfCodes[i];


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //
    private void setupAmountList(ArrayList<String> amountList) {
        amountList.add("10");
        amountList.add("50");
        amountList.add("100");
        amountList.add("150");
    }

    //
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

//    @Override
//    public void amountSelected(String amount) {
//        donationFromRV = amount;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        shouldMergeAudio = false;
//        Log.i("onDetach", "onDetach: Called");
//    }

    private void hideKeybaord() {
        View v;
        v = this.getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }

    }

    protected void hideSoftKeyboard(SwitchCompat mSearchView) {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
    }

    public class BackgroundAudioDownload extends AsyncTask<String, String, String> {
        String Link;
        String Timestamp = HelperClass.getCurrentTimeStamp();
        String path = "";

        public BackgroundAudioDownload(String link) {
            Link = link;
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStream input = null;
            OutputStream output = null;
            String DestinationFilePath = Environment.getExternalStorageDirectory() + "/" + "Demo" + "/";
            File fell = new File(DestinationFilePath);

            if (fell.exists()) {
//            return null;
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
                File f = new File(DestinationFilePath, Timestamp + ".mp3");
                path = f.getPath();
                output = new FileOutputStream(f);
                int fileLength = connection.getContentLength();

                byte[] data = new byte[4096];
                int count;
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

//                    Percentage.setText(((total * 100) / fileLength) + " %");
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

            merge(path);
            return null;
        }


    }


}