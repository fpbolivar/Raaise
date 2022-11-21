package com.scripttube.android.ScriptTube.Home.MainHome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.scripttube.android.ScriptTube.Home.Fragments.HomeFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.InboxFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.PlusFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.ProfileFragment;
import com.scripttube.android.ScriptTube.Home.Fragments.SearchFragment;
import com.scripttube.android.ScriptTube.R;

public class Home extends AppCompatActivity {
    LinearLayout Home, Search, Plus, Inbox, Profile;
    //    FrameLayout FragmentContainer;
    ImageView HomeImageView,
            SearchImageView,
            InboxImageView,
            ProfileImageView;
    Fragment fragment = null;


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

    private void SelectHomeScreen() {
        HomeImageView.setImageResource(R.drawable.home_s);
        SearchImageView.setImageResource(R.drawable.search_us);
        InboxImageView.setImageResource(R.drawable.inbox_us);
        ProfileImageView.setImageResource(R.drawable.profile_s);
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
                fragment = new InboxFragment();
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
        HomeImageView.setImageResource(R.drawable.home_us);
        SearchImageView.setImageResource(R.drawable.search_s);
        InboxImageView.setImageResource(R.drawable.inbox_us);
        ProfileImageView.setImageResource(R.drawable.profile_s);
        SelectFragment(2);

    }

    private void SelectPlusScreen() {
        HomeImageView.setImageResource(R.drawable.home_us);
        SearchImageView.setImageResource(R.drawable.search_us);
        InboxImageView.setImageResource(R.drawable.inbox_us);
        ProfileImageView.setImageResource(R.drawable.profile_s);
        SelectFragment(3);

    }

    private void SelectInboxScreen() {
        HomeImageView.setImageResource(R.drawable.home_us);
        SearchImageView.setImageResource(R.drawable.search_us);
        InboxImageView.setImageResource(R.drawable.inbox_s);
        ProfileImageView.setImageResource(R.drawable.profile_s);
        SelectFragment(4);

    }

    private void SelectProfileScreen() {
        HomeImageView.setImageResource(R.drawable.home_us);
        SearchImageView.setImageResource(R.drawable.search_us);
        InboxImageView.setImageResource(R.drawable.inbox_us);
        ProfileImageView.setImageResource(R.drawable.profile_s);
        SelectFragment(5);

    }

    private void Initialization() {

//        FragmentContainer = findViewById(R.id.FragmentContainer);
        Home = findViewById(R.id.Home);
        Search = findViewById(R.id.Search);
        Plus = findViewById(R.id.Plus);
        Inbox = findViewById(R.id.Inbox);
        Profile = findViewById(R.id.Profile);
        HomeImageView = findViewById(R.id.HomeImageView);
        SearchImageView = findViewById(R.id.SearchImageView);
        InboxImageView = findViewById(R.id.InboxImageView);
        ProfileImageView = findViewById(R.id.ProfileImageView);
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
}