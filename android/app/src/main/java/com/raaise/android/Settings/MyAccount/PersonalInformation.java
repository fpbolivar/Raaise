package com.raaise.android.Settings.MyAccount;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.UpdateUserProfileModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.ApiUtilities;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInformation extends AppCompatActivity {
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION = 121;
    ImageView userImage,CoverImageInOtherUserProfile;
    EditText NameEditTextInPersonalInformation, UserNameEditTextInPersonalInformation, PhoneNumberEditTextInPersonalInformation;
    LinearLayout UpdateButtonInPersonalInformation;
    String UserName, Name, PhnNo;
    ApiManager apiManager = App.getApiManager();
    ImageView BackArrow;
    FloatingActionButton UploadImageInPersonalInformation,UploadCoverImageInPersonalInformation;
    Uri imageUri = null;
    File imageFile = null,coverFile=null;
    boolean isProfilePhoto=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_personal_information);

        inItWidgets();
        Clicklisteners();
        GetUserProfile();
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkPermissions();
    }

    private void inItWidgets() {
        userImage = findViewById(R.id.userImage);
        BackArrow = findViewById(R.id.BackInSignUp);
        NameEditTextInPersonalInformation = findViewById(R.id.NameEditTextInPersonalInformation);
        UserNameEditTextInPersonalInformation = findViewById(R.id.UserNameEditTextInPersonalInformation);
        PhoneNumberEditTextInPersonalInformation = findViewById(R.id.PhoneNumberEditTextInPersonalInformation);
        UpdateButtonInPersonalInformation = findViewById(R.id.UpdateButtonInPersonalInformation);
        UploadImageInPersonalInformation = findViewById(R.id.UploadImageInPersonalInformation);
        UploadCoverImageInPersonalInformation=findViewById(R.id.UploadCoverImageInPersonalInformation);
        CoverImageInOtherUserProfile=findViewById(R.id.CoverImageInOtherUserProfile);
    }

    private void Clicklisteners() {
        UploadImageInPersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfilePhoto=true;
                ShowUlpoadimageDialog();
            }
        });
        UploadCoverImageInPersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfilePhoto=false;
                ShowUlpoadimageDialog();
            }
        });
        UpdateButtonInPersonalInformation.setOnClickListener(view -> Validate());
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
    }

    private void checkPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//                Toast.makeText(this, "Please Allow Permissions", Toast.LENGTH_SHORT).show();
//            }
//
//        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1003);
        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void ShowUlpoadimageDialog() {

        Dialog dialog = new Dialog(PersonalInformation.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_image_dialog);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        CardView SelectCamera, SelectGallery, CancelInChatSelectImageBottomLayout;
        SelectCamera = dialog.findViewById(R.id.SelectCamera);
        SelectGallery = dialog.findViewById(R.id.SelectGallery);
        CancelInChatSelectImageBottomLayout = dialog.findViewById(R.id.CancelInChatSelectImageBottomLayout);
        SelectCamera.setOnClickListener(view -> {
            dialog.dismiss();
            openCamera();
        });
        SelectGallery.setOnClickListener(view -> {
            dialog.dismiss();
            openGallery();
        });
        CancelInChatSelectImageBottomLayout.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void Validate() {
        Name = NameEditTextInPersonalInformation.getText().toString().trim();
        PhnNo = PhoneNumberEditTextInPersonalInformation.getText().toString().trim();
        if (PhnNo.length() > 0 && PhnNo.length() < 10){
            Prompt.SnackBar(findViewById(android.R.id.content), "Phone number must be of 10 digits");
            return;
        }
        if (Name.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Name");
        } else {
            if (imageUri != null && (imageFile != null || coverFile != null)) {
                UpdateUserDataWithImage(imageFile,coverFile, Name, PhnNo);
            } else {
                UpdateUserData(Name, PhnNo);
            }

        }
    }

    private void UpdateUserDataWithImage(File file,File Coverfile, String namee, String phoneNumberr) {
        if (phoneNumberr == null || phoneNumberr.isEmpty()){
            phoneNumberr = "";
        }

        Dialogs.createProgressDialog(PersonalInformation.this);
        RequestBody coverRequestFileImg ;
        MultipartBody.Part coverImage;
       String coverImagePref= Prefs.getUserCoverImage(PersonalInformation.this);
       String imagePref =Prefs.getUserCoverImage(PersonalInformation.this);
        if(coverFile==null){
            if(coverImagePref.equals("")){
                coverRequestFileImg = RequestBody.create(MediaType.parse("image/*"),"");
                coverImage=MultipartBody.Part.createFormData("coverImage", "");
            }else {
                coverImage=MultipartBody.Part.createFormData("coverImage", coverImagePref);
            }

        }else {
            coverRequestFileImg = RequestBody.create(MediaType.parse("image/*"), Coverfile);
            coverImage=MultipartBody.Part.createFormData("coverImage", Coverfile.getName(), coverRequestFileImg);
        }

        RequestBody requestFileImg;
        MultipartBody.Part image ;

        if(file==null){
            if(imagePref.equals("")){
                requestFileImg = RequestBody.create(MediaType.parse("image/*"),"");
                image=MultipartBody.Part.createFormData("image", "");
            }else {
                image=MultipartBody.Part.createFormData("image", imagePref);
            }

        }else {
            requestFileImg = RequestBody.create(MediaType.parse("image/*"), file);
            image=MultipartBody.Part.createFormData("image", file.getName(), requestFileImg);
        }

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), namee);
        RequestBody phoneNumber = RequestBody.create(MediaType.parse("text/plain"), phoneNumberr);

        ApiUtilities.getApiInterface().UpdateProfileWithImage(Prefs.GetBearerToken(PersonalInformation.this),
                image,coverImage, name, phoneNumber).enqueue(new Callback<UpdateUserProfileModel>() {
            @Override
            public void onResponse(Call<UpdateUserProfileModel> call, Response<UpdateUserProfileModel> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (response.body().getStatusCode() == 200) {
                            Dialogs.HideProgressDialog();
                            Prompt.SnackBar(findViewById(android.R.id.content), response.body().getMessage());
                            Log.i("personalImg", "onResponse: " + Prefs.GetBaseUrl(PersonalInformation.this) + response.body().getData().getProfileImage());
                            Log.i("personalImg", "onResponse: " + Prefs.GetBaseUrl(PersonalInformation.this) + response.body().getData().getCoverImage());

                            Glide.with(PersonalInformation.this)
                                    .load(Prefs.GetBaseUrl(PersonalInformation.this) + response.body().getData().getProfileImage())
                                    .circleCrop()
                                    .placeholder(R.drawable.placeholder)
                                    .into(userImage);

                            Glide.with(PersonalInformation.this)
                                    .load(Prefs.GetBaseUrl(PersonalInformation.this) + response.body().getData().getCoverImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(CoverImageInOtherUserProfile);

                            Prefs.setNameOfUser(PersonalInformation.this, response.body().getData().getName());
                            Prefs.SetPhoneNumberOfTheUser(PersonalInformation.this, response.body().getData().getPhoneNumber());
                            Prefs.setUserImage(PersonalInformation.this, response.body().getData().getProfileImage());
                            Prefs.setUserCoverImage(PersonalInformation.this, response.body().getData().getCoverImage());

                        } else if (response.body().getStatusCode() == 422) {
                            Dialogs.HideProgressDialog();

                            Prompt.SnackBar(findViewById(android.R.id.content), response.body().getErrors().getMessage());
                        } else {
                            Dialogs.HideProgressDialog();

                            Prompt.SnackBar(findViewById(android.R.id.content), response.body().getMessage());
                        }
                    } else {
                        Dialogs.HideProgressDialog();
                        Prompt.SnackBar(findViewById(android.R.id.content), response.message());
                    }
                } else {
                    Dialogs.HideProgressDialog();
                    Prompt.SnackBar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<UpdateUserProfileModel> call, Throwable t) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), t.getMessage());
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CAMERA_PERMISSION) {
            openCamera();
        }
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (data != null) {
                try {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    imageUri = getImageUri(getApplicationContext(), image);
                    if(isProfilePhoto){
                        imageFile = new File(getRealPathFromURI(imageUri));

                        Glide.with(this)
                                .load(imageUri)
                                .circleCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(userImage);
                    }else{
                        coverFile = new File(getRealPathFromURI(imageUri));
                        Glide.with(this)
                                .load(imageUri)
                                .placeholder(R.drawable.placeholder)
                                .into(CoverImageInOtherUserProfile);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                try {
                    imageUri = data.getData();
                    if(isProfilePhoto){
                        imageFile = new File(getRealPathFromURI(imageUri));
                        Glide.with(this)
                                .load(imageUri)
                                .circleCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(userImage);
                    }else{
                        coverFile = new File(getRealPathFromURI(imageUri));
                        Glide.with(this)
                                .load(imageUri)
                                .placeholder(R.drawable.placeholder)
                                .into(CoverImageInOtherUserProfile);
                    }
                } catch (Exception e) {
                    Log.i("galleryException", "onActivityResult: " + e.getMessage());
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private Uri getImageUri(Context context, Bitmap image) {
        MediaStore.Images.Media.insertImage(context.getContentResolver(), image, HelperClass.getCurrentTimeStamp(), null);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, HelperClass.getCurrentTimeStamp(), null);
        return Uri.parse(path);
    }

    void GetUserProfile() {
        try {
            NameEditTextInPersonalInformation.setText(Prefs.getNameOfUser(PersonalInformation.this));
            UserNameEditTextInPersonalInformation.setText(Prefs.GetUserEmail(PersonalInformation.this));
            PhoneNumberEditTextInPersonalInformation.setText(Prefs.GetPhoneNumberOfTheUser(PersonalInformation.this));
            Glide.with(PersonalInformation.this)
                    .load(Prefs.GetBaseUrl(PersonalInformation.this) + Prefs.getUserImage(PersonalInformation.this))
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(userImage);

            Glide.with(PersonalInformation.this)
                    .load(Prefs.GetBaseUrl(PersonalInformation.this) + Prefs.getUserCoverImage(PersonalInformation.this))
                    .placeholder(R.drawable.placeholder)
                    .into(CoverImageInOtherUserProfile);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    private void UpdateUserData(String Name, String PhnNo) {
        if (PhnNo == null || PhnNo.isEmpty() ){
            PhnNo = "";
        }
        UpdateUserProfileModel update = new UpdateUserProfileModel(Name, PhnNo);


        Dialogs.createProgressDialog(PersonalInformation.this);
        apiManager.UpdateProfile(Prefs.GetBearerToken(findViewById(android.R.id.content).getContext()), update, new DataCallback<UpdateUserProfileModel>() {
            @Override
            public void onSuccess(UpdateUserProfileModel updateUserProfileModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), "Profile Updated Successfully");


                Prefs.setNameOfUser(PersonalInformation.this, updateUserProfileModel.getData().getName());
                Prefs.SetPhoneNumberOfTheUser(PersonalInformation.this, updateUserProfileModel.getData().getPhoneNumber());

            }

            @Override
            public void onError(ServerError serverError) {

                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }
}