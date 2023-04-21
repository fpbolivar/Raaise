package com.raaise.android.Home.MainHome;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.raaise.android.Activity.credentials.Login;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetPolicyModel;
import com.raaise.android.ApiManager.ApiModels.LoginModel;
import com.raaise.android.ApiManager.ApiModels.UnreadMessageCountModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.FragmentManagerHelper;
import com.raaise.android.Home.Fragments.CameraFragment;
import com.raaise.android.Home.Fragments.ChatListFragment;
import com.raaise.android.Home.Fragments.HomeFragment;
import com.raaise.android.Home.Fragments.PlusFragment;
import com.raaise.android.Home.Fragments.ProfileFragment;
import com.raaise.android.Home.Fragments.SearchFragment;
import com.raaise.android.MainActivity;
import com.raaise.android.NewCameraFragment;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.model.LoginPojo;

public class Home extends AppCompatActivity {
    public VideoView adapterVideoVIew;
    public ApiManager apiManager = App.getApiManager();
    public LinearLayout bottomBar;
    public FragmentManagerHelper fragmentManagerHelper;

    public String videoPath = "";
    public String musicData = "";
    public String musicTitle = "";
    public String videoUri = "";
    public boolean shouldMergeAudio = false;
    public boolean fromGallery = false;
    LinearLayout Home, Search, Plus, Inbox, Profile;

    ImageView HomeImageView,
            SearchImageView,
            InboxImageView,
            ProfileImageView;
    Fragment fragment = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    TextView textViewCountMessage;
    private static Dialog uploadDialog;
    private static View view;
    private static View uploadCompleteProgress;
    private static Activity homeActivity;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
    };

    public static void uploadStarted() {
        Log.i("serviceClass", "uploadStarted: Inside dialog");
        homeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
            }
        });

    }

    public static void uploadCompleted() {
        homeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
                showUploadCompleteDialog();
            }
        });

    }

    private static void showUploadCompleteDialog() {
        homeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uploadCompleteProgress.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uploadCompleteProgress.setVisibility(View.GONE);
                    }
                }, 3000);
            }
        });

    }

    public static void interruptedDialog() {
        homeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("tokenUser", "onCreate: " + Prefs.GetBearerToken(com.raaise.android.Home.MainHome.Home.this));
        homeActivity = com.raaise.android.Home.MainHome.Home.this;
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_home);
        view = findViewById(R.id.upload_progress);
        uploadCompleteProgress = findViewById(R.id.upload_complete_progress);
        Log.i("authKey", "onCreate: " + Prefs.GetBearerToken(this));
        Initialization();
        clickListeners();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CheckNotificationCount();
            }
        }).start();
        Prefs.SetExtra(getApplicationContext(), "", "OverSelectedMusicData");

    }

    private void clickListeners() {
        Home.setOnClickListener(view -> {
            SelectHomeScreen();
        });
        Search.setOnClickListener(view -> {
            SelectSearchScreen();
        });
        Plus.setOnClickListener(view -> {
            SelectPlusScreen();
        });
        Inbox.setOnClickListener(view -> {
            SelectInboxScreen();
        });
        Profile.setOnClickListener(view -> {
            SelectProfileScreen();
        });
    }

    public void SelectHomeScreen() {
        HomeImageView.setImageResource(R.drawable.svg_select_home);
        SearchImageView.setImageResource(R.drawable.svg_unselect_search);
        InboxImageView.setImageResource(R.drawable.svg_unselect_inbox);
        ProfileImageView.setImageResource(R.drawable.svg_unselect_profile);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SelectFragment(1);
            }
        }).start();
    }

    private void SelectFragment(int position) {

        switch (position) {
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new SearchFragment();
                break;
            case 3:
                fragment = new CameraFragment();

                break;
            case 4:
                fragment = new ChatListFragment();
                break;
            case 5:
                fragment = new ProfileFragment();
                break;
            default:
                fragment = null;
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.FragmentContainer, fragment, null)
                .commit();
    }

    private void SelectSearchScreen() {
                HomeImageView.setImageResource(R.drawable.svg_unselect_home);
                SearchImageView.setImageResource(R.drawable.svg_select_search);
                InboxImageView.setImageResource(R.drawable.svg_unselect_inbox);
                ProfileImageView.setImageResource(R.drawable.svg_unselect_profile);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SelectFragment(2);
                    }
                }).start();
    }

    private void SelectPlusScreen() {
        HomeImageView.setImageResource(R.drawable.svg_unselect_home);
        SearchImageView.setImageResource(R.drawable.svg_unselect_search);
        InboxImageView.setImageResource(R.drawable.svg_unselect_inbox);
        ProfileImageView.setImageResource(R.drawable.svg_unselect_profile);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SelectFragment(3);
            }
        }).start();

    }

    public void SelectInboxScreen() {
        Log.i("fromChat", "SelectInboxScreen: Selected");
        HomeImageView.setImageResource(R.drawable.svg_unselect_home);
        SearchImageView.setImageResource(R.drawable.svg_unselect_search);
        InboxImageView.setImageResource(R.drawable.svg_select_inbox);
        ProfileImageView.setImageResource(R.drawable.svg_unselect_profile);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SelectFragment(4);
            }
        }).start();

    }

    private void SelectProfileScreen() {
        HomeImageView.setImageResource(R.drawable.svg_unselect_home);
        SearchImageView.setImageResource(R.drawable.svg_unselect_search);
        InboxImageView.setImageResource(R.drawable.svg_unselect_inbox);
        ProfileImageView.setImageResource(R.drawable.svg_select_profile);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SelectFragment(5);
            }
        }).start();

    }

    private void Initialization() {


        textViewCountMessage = findViewById(R.id.textViewCountMessage);
        bottomBar = findViewById(R.id.bottomBar);
        Home = findViewById(R.id.Home);
        Search = findViewById(R.id.Search);
        Plus = findViewById(R.id.Plus);
        Inbox = findViewById(R.id.Inbox);
        Profile = findViewById(R.id.Profile);
        HomeImageView = findViewById(R.id.HomeImageView);
        SearchImageView = findViewById(R.id.SearchImageView);
        InboxImageView = findViewById(R.id.InboxImageView);
        ProfileImageView = findViewById(R.id.ProfileImageView);
        fragmentManagerHelper = new FragmentManagerHelper(fragmentManager);
        Log.i("whereTo", "Initialization: Home");
        SelectHomeScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT < 33){
            checkPermissions();
        } else {
            String[] PERMISSIONS = {
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.CAMERA
            };
            if (!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }
        CheckNotificationCount();
    }

    public void CheckNotificationCount() {
        apiManager.UnreadMessageCount(Prefs.GetBearerToken(com.raaise.android.Home.MainHome.Home.this), new DataCallback<UnreadMessageCountModel>() {
            @Override
            public void onSuccess(UnreadMessageCountModel unreadMessageCountModel) {
                if ((unreadMessageCountModel.getUnreadMessageCount() == 0)) {
                    textViewCountMessage.setVisibility(View.INVISIBLE);
                } else {
                    textViewCountMessage.setVisibility(View.VISIBLE);
                    textViewCountMessage.setText(String.valueOf(unreadMessageCountModel.getUnreadMessageCount()));
                }
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkPermissions() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//            }
//
//        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {

            Uri selectedImageUri = data.getData();


            String filemanagerstring = selectedImageUri.getPath();


            String selectedImagePath = getPath(selectedImageUri);
            if (selectedImagePath != null) {
                videoPath = selectedImagePath;
                videoUri = data.getData().toString();


                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.FragmentContainer, new PlusFragment(), null)
                        .commit();
            }

        } else if (resultCode == RESULT_OK && requestCode == 101) {

            Uri selectedImageUri = data.getData();


            String filemanagerstring = selectedImageUri.getPath();


            String selectedImagePath = getPath(selectedImageUri);
            if (selectedImagePath != null) {
                videoPath = selectedImagePath;
                videoUri = data.getData().toString();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.FragmentContainer, new PlusFragment(), null)
                        .commit();
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {


            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        SelectHomeScreen();
    }
}