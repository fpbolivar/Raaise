package com.raaise.android.Home.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.raaise.android.Adapters.GalleryAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.ImageGallery;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.UpdateRoomPojo;
import com.raaise.android.model.UpdateRoomRes;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditRoomFragment extends Fragment implements GalleryAdapter.GalleryListener {

    ApiManager apiManager = App.getApiManager();
    ImageView roomLogoIV;
    ImageView backBTN;
    EditText roomNameET;
    EditText roomDescET;
    LinearLayout updateRoomBtn;
    FloatingActionButton changeLogoBTN;
    private GalleryAdapter galleryAdapter;

    Dialog galleryDialog;
    private String liveData;
    LiveRoomData liveRoomData;

    public EditRoomFragment(String liveDataStr){
        this.liveData = liveDataStr;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_room, container, false);

        inItWidgets(view);
        if (liveRoomData != null){
            setupData(liveRoomData);
        }
        backBTN.setOnClickListener(view1 -> requireActivity().onBackPressed());
        changeLogoBTN.setOnClickListener(view12 -> openGalleryImagesDialog());
        updateRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roomNameET.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(), "Please fill Room name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (roomDescET.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(), "Please add Room description", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody title = RequestBody.create(MediaType.parse("text/plain"), roomNameET.getText().toString());
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), roomDescET.getText().toString());
                Bitmap bm=((BitmapDrawable)roomLogoIV.getDrawable()).getBitmap();
                Uri tempUri = getImageUri(requireContext(), bm, String.valueOf(System.currentTimeMillis() / 1000));
                File file = new File(getRealPathFromURI(tempUri));
                RequestBody requestFileImg = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part logo = MultipartBody.Part.createFormData("logo", file.getName(), requestFileImg);
                RequestBody slug = RequestBody.create(MediaType.parse("text/plain"), liveRoomData.slug);

                UpdateRoomPojo pojo = new UpdateRoomPojo(title, description, logo, slug);
                Dialogs.createProgressDialog(getContext());
                apiManager.updateLiveRoom(Prefs.GetBearerToken(getContext()), pojo, new DataCallback<UpdateRoomRes>() {
                    @Override
                    public void onSuccess(UpdateRoomRes updateRoomRes) {
                        App.fromEditRoom = true;
                        Dialogs.HideProgressDialog();
                        requireActivity().onBackPressed();
                        Log.i("updateLiveRoom", "onSuccess: Success");
                    }

                    @Override
                    public void onError(ServerError serverError) {
                        Dialogs.HideProgressDialog();
                        Log.i("updateLiveRoom", "onSError: " + serverError.getErrorMsg());
                    }
                });
            }
        });

        return view;
    }

    private void openGalleryImagesDialog() {
        galleryDialog = new Dialog(getContext());
        galleryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        galleryDialog.setContentView(R.layout.gallery_dialog);

        RecyclerView galleryRV = galleryDialog.findViewById(R.id.gallery_rv);
        galleryRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        galleryRV.setAdapter(galleryAdapter);


        galleryDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        galleryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        galleryDialog.getWindow().setGravity(Gravity.BOTTOM);
        galleryDialog.setCancelable(false);
        galleryDialog.show();
    }

    private void setupData(LiveRoomData liveRoomData) {
        Glide.with(getActivity())
                .load(Prefs.GetBaseUrl(getContext()) + liveRoomData.logo)
                .placeholder(R.drawable.placeholder)
                .into(roomLogoIV);

        roomNameET.setText(liveRoomData.title);
        roomDescET.setText(liveRoomData.description);
    }

    private void inItWidgets(View view) {
        if (!liveData.equals(""))
        liveRoomData = new Gson().fromJson(liveData, LiveRoomData.class);
        updateRoomBtn = view.findViewById(R.id.update_room_btn);
        galleryAdapter = new GalleryAdapter(getContext(), ImageGallery.listOfImages(getContext()), this);
        backBTN = view.findViewById(R.id.backBtn);
        roomLogoIV = view.findViewById(R.id.room_img);
        roomDescET = view.findViewById(R.id.room_desc_et);
        roomNameET = view.findViewById(R.id.room_name_et);
        changeLogoBTN = view.findViewById(R.id.change_room_logo_btn);
    }

    @Override
    public void ImageSelected(String imageUri) {
        Glide.with(getContext())
                .load(imageUri)
                .into(roomLogoIV);
        galleryDialog.dismiss();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String name) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "temp" + name, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}
