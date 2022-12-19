package com.scripttube.android.ScriptTube.Home.MainHome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.FragmentManagerHelper;
import com.scripttube.android.ScriptTube.Home.Fragments.ChatListFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.HomeFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.InboxFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.PlusFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.ProfileFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.SearchFragment;
import com.scripttube.android.ScriptTube.R;

public class Home extends AppCompatActivity {
    public VideoView adapterVideoVIew;
    LinearLayout Home, Search, Plus, Inbox, Profile;
    public ApiManager apiManager = App.getApiManager();
    public LinearLayout bottomBar;
    //    FrameLayout FragmentContainer;
    ImageView HomeImageView,
            SearchImageView,
            InboxImageView,
            ProfileImageView;
    Fragment fragment = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    public FragmentManagerHelper fragmentManagerHelper;

    // For new implementation
    public String videoPath = "";
    public String musicData = "";
    public String musicTitle = "";
    public String videoUri = "";
    public boolean shouldMergeAudio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_home);
        Initialization();
        clickListeners();
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
        HomeImageView.setImageResource(R.drawable.svg_home_bottom_bar);
        SearchImageView.setImageResource(R.drawable.search_bottom_bar);
        InboxImageView.setImageResource(R.drawable.inbox_bottom_bar);
        ProfileImageView.setImageResource(R.drawable.profile_icon_bottom_bar);
        SelectFragment(1);

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
                fragment = new PlusFragment();
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
        HomeImageView.setImageResource(R.drawable.home_bottom_bar);
        SearchImageView.setImageResource(R.drawable.svg_search_bottom_bar);
        InboxImageView.setImageResource(R.drawable.inbox_bottom_bar);
        ProfileImageView.setImageResource(R.drawable.profile_icon_bottom_bar);
        SelectFragment(2);

    }

    private void SelectPlusScreen() {
        HomeImageView.setImageResource(R.drawable.home_bottom_bar);
        SearchImageView.setImageResource(R.drawable.search_bottom_bar);
        InboxImageView.setImageResource(R.drawable.inbox_bottom_bar);
        ProfileImageView.setImageResource(R.drawable.profile_icon_bottom_bar);
        startActivityForResult(new Intent(MediaStore.ACTION_VIDEO_CAPTURE).putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30).putExtra("aspectY", 1).putExtra("aspectX", 1), 100);
//        SelectFragment(3);

    }

    private void SelectInboxScreen() {
        HomeImageView.setImageResource(R.drawable.home_bottom_bar);
        SearchImageView.setImageResource(R.drawable.search_bottom_bar);
        InboxImageView.setImageResource(R.drawable.svg_inbox_bottom_bar);
        ProfileImageView.setImageResource(R.drawable.profile_icon_bottom_bar);
        SelectFragment(4);

    }

    private void SelectProfileScreen() {
        HomeImageView.setImageResource(R.drawable.home_bottom_bar);
        SearchImageView.setImageResource(R.drawable.search_bottom_bar);
        InboxImageView.setImageResource(R.drawable.inbox_bottom_bar);
        ProfileImageView.setImageResource(R.drawable.svg_profile_bottom_bar);
        SelectFragment(5);

    }

    private void Initialization() {

//        FragmentContainer = findViewById(R.id.FragmentContainer);
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
        checkPermissions();
    }
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(this, "Please Allow Permissions", Toast.LENGTH_SHORT).show();
            }

        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
//            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
            String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
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
//            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
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