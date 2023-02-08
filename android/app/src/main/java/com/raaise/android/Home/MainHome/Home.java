package com.raaise.android.Home.MainHome;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.raaise.android.ApiManager.ApiManager;
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
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

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

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_home);

        Initialization();
        clickListeners();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CheckNotificationCount();
            }
        }).start();
        Log.i("token", "onCreate: " + Prefs.GetBearerToken(this));
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
        Log.i("videoSent", "SelectHomeScreen: Home");
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
//                SelectFragment(2);
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

    private void ShowGalleryCameraPrompt() {
        final Dialog dialog = new Dialog(Home.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.camera_gallery_dialog);

        TextView closeBtn = dialog.findViewById(R.id.cancel_button);
        TextView openCameraBtn = dialog.findViewById(R.id.open_camera_btn);
        TextView openGalleryBtn = dialog.findViewById(R.id.open_gallery_btn);

        closeBtn.setOnClickListener(view -> dialog.dismiss());
        openCameraBtn.setOnClickListener(view -> {
            dialog.dismiss();
            startActivityForResult(new Intent(MediaStore.ACTION_VIDEO_CAPTURE).putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30).putExtra("aspectY", 1).putExtra("aspectX", 1).putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT), 100);
        });

        openGalleryBtn.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        });

        dialog.show();
    }

    private void SelectInboxScreen() {
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

        SelectHomeScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkPermissions();
            }
        }).start();
//        checkPermissions();
//        CheckNotificationCount();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }

        }
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
//        }
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
//        }
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1003);
//        }
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.RECORD_AUDIO},
//                    1004);
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


}