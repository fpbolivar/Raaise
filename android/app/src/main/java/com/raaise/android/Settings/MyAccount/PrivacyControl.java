package com.raaise.android.Settings.MyAccount;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.PrivacyAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.PrivacyModel;

import java.util.ArrayList;

public class PrivacyControl extends AppCompatActivity implements PrivacyAdapter.PrivacyListener {

    ApiManager apiManager = App.getApiManager();
    RecyclerView privacyRV;
    PrivacyAdapter adapter;
    ImageView backBTN;
    ArrayList<String> privacyStrings;

    private String mPrivacyName = "";
    private int mPrivacyPosition;

    LinearLayout updatePrivacyBTN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_control);

        inItWidgets();
        backBTN.setOnClickListener(view -> onBackPressed());
        updatePrivacyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPrivacyName.equals("") && mPrivacyPosition != -1){
                    Toast.makeText(PrivacyControl.this, "Please select another option to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPrivacyName.equals("") ){
                    Toast.makeText(PrivacyControl.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    return;
                }
                Dialogs.createProgressDialog(PrivacyControl.this);
                PrivacyModel model = new PrivacyModel(mPrivacyName.toLowerCase());
                apiManager.updatePrivacyPolicy(Prefs.GetBearerToken(PrivacyControl.this), model, new DataCallback<String>() {
                    @Override
                    public void onSuccess(String message) {
                        Prefs.setPrivacyPosition(PrivacyControl.this, mPrivacyPosition);
                        Dialogs.HideProgressDialog();
                        Toast.makeText(PrivacyControl.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ServerError serverError) {
                        Dialogs.HideProgressDialog();
                        Toast.makeText(PrivacyControl.this, serverError.error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void inItWidgets() {
        mPrivacyPosition = Prefs.getPrivacyPosition(PrivacyControl.this);
        updatePrivacyBTN = findViewById(R.id.update_privacy_control);
        backBTN = findViewById(R.id.back_btn);
        privacyRV = findViewById(R.id.privacy_control_rv);
        privacyStrings = new ArrayList<>();
        privacyStrings.add("Following");
        privacyStrings.add("Followers");
        privacyStrings.add("Anyone");
        privacyStrings.add("Nobody");
        adapter = new PrivacyAdapter(PrivacyControl.this, privacyStrings, this, mPrivacyPosition);
        privacyRV.setAdapter(adapter);
    }

    @Override
    public void privacySelected(String privacyName, int position) {
        mPrivacyName = privacyName;
        mPrivacyPosition = position;
    }
}
