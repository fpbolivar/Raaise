package com.scripttube.android.ScriptTube.Settings.MyAccount;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetUserProfile;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UpdateUserProfileModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.ApiUtilities;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

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
    ImageView userImage;
    EditText NameEditTextInPersonalInformation, UserNameEditTextInPersonalInformation, PhoneNumberEditTextInPersonalInformation;
    LinearLayout UpdateButtonInPersonalInformation;
    String UserName, Name, PhnNo;
    ApiManager apiManager = App.getApiManager();
    ImageView BackArrow;
    FloatingActionButton  UploadImageInPersonalInformation;
    Uri imageUri = null;
    File imageFile = null;

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

    private void inItWidgets() {
        userImage = findViewById(R.id.userImage);
        BackArrow = findViewById(R.id.BackInSignUp);
        NameEditTextInPersonalInformation = findViewById(R.id.NameEditTextInPersonalInformation);
        UserNameEditTextInPersonalInformation = findViewById(R.id.UserNameEditTextInPersonalInformation);
        PhoneNumberEditTextInPersonalInformation = findViewById(R.id.PhoneNumberEditTextInPersonalInformation);
        UpdateButtonInPersonalInformation = findViewById(R.id.UpdateButtonInPersonalInformation);
        UploadImageInPersonalInformation = findViewById(R.id.UploadImageInPersonalInformation);
    }

    private void Clicklisteners() {
        UploadImageInPersonalInformation.setOnClickListener(view -> ShowUlpoadimageDialog());
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
            dialog.hide();
            openCamera();
        });
        SelectGallery.setOnClickListener(view -> {
            dialog.hide();
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
        if (Name.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Name");
        } else if (PhnNo.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Phone Number");
        } else if (PhnNo.length() < 8) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Phone Number Should Be Greater Than 8");
        } else {
            if (imageUri != null && imageFile != null) {
                UpdateUserDataWithImage(imageFile, Name, PhnNo);
            } else {
                UpdateUserData(Name, PhnNo);
            }

        }
    }

    private void UpdateUserDataWithImage(File file, String namee, String phoneNumberr) {
        Dialogs.createProgressDialog(PersonalInformation.this);
        RequestBody requestFileImg = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestFileImg);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), namee);
        RequestBody phoneNumber = RequestBody.create(MediaType.parse("text/plain"), phoneNumberr);
        ApiUtilities.getApiInterface().UpdateProfileWithImage(Prefs.GetBearerToken(PersonalInformation.this),
                image, name, phoneNumber).enqueue(new Callback<UpdateUserProfileModel>() {
            @Override
            public void onResponse(Call<UpdateUserProfileModel> call, Response<UpdateUserProfileModel> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (response.body().getStatus() == 200) {
                            Dialogs.HideProgressDialog();
                            Prompt.SnackBar(findViewById(android.R.id.content), response.body().getMessage());
                            Glide.with(PersonalInformation.this)
                                    .load(response.body().getData().getProfileImage())
                                    .circleCrop()
                                    .placeholder(R.drawable.placeholder)
                                    .into(userImage);
                        } else if (response.body().getStatus() == 422) {
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
                    imageFile = new File(getRealPathFromURI(imageUri));

                    Glide.with(this)
                            .load(imageUri)
                            .circleCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(userImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                try {
                    imageUri = data.getData();
                    imageFile = new File(getRealPathFromURI(imageUri));
                    Glide.with(this)
                            .load(imageUri)
                            .circleCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(userImage);
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
//        final ProgressDialog dialog = Dialogs.createProgressDialog(v.getContext());
//        dialog.show();
        Dialogs.createProgressDialog(findViewById(android.R.id.content).getContext());
        apiManager.GetUserProfile(Prefs.GetBearerToken(getApplicationContext()), new DataCallback<GetUserProfile>() {
            @Override
            public void onSuccess(GetUserProfile getUserProfile) {
                try {
                    Dialogs.HideProgressDialog();
                    NameEditTextInPersonalInformation.setText(getUserProfile.getData().getName());
                    UserNameEditTextInPersonalInformation.setText(getUserProfile.getData().getEmail());
                    PhoneNumberEditTextInPersonalInformation.setText(getUserProfile.getData().getPhoneNumber());
                    Glide.with(PersonalInformation.this)
                            .load(getUserProfile.getData().getProfileImage())
                            .placeholder(R.drawable.placeholder)
                            .circleCrop()
                            .into(userImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void UpdateUserData(String Name, String PhnNo) {
        UpdateUserProfileModel update = new UpdateUserProfileModel(Name, PhnNo);
//        final ProgressDialog dialog =  Dialogs.createProgressDialog(PersonalInformation.this);
//        dialog.show();
        Dialogs.createProgressDialog(PersonalInformation.this);
        apiManager.UpdateProfile(Prefs.GetBearerToken(findViewById(android.R.id.content).getContext()), update, new DataCallback<UpdateUserProfileModel>() {
            @Override
            public void onSuccess(UpdateUserProfileModel updateUserProfileModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), "Profile Updated Successfully");
                NameEditTextInPersonalInformation.setText("");
                UserNameEditTextInPersonalInformation.setText("");
                PhoneNumberEditTextInPersonalInformation.setText("");
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }
}