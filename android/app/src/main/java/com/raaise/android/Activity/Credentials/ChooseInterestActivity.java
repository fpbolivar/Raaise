package com.raaise.android.Activity.credentials;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.raaise.android.Adapters.TagsCategoryAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetCategoryModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.Fragments.PlusFragment;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.Utilities.textPaint.TextPaint;
import com.raaise.android.model.CategoriesModel;
import com.raaise.android.model.CategoriesPojo;

import java.util.ArrayList;
import java.util.List;

public class ChooseInterestActivity extends AppCompatActivity implements TagsCategoryAdapter.TagsCategoryListener,View.OnClickListener {
    TagsCategoryAdapter tagsCategoryAdapter;
    public static ApiManager apiManager = App.getApiManager();
    RecyclerView RecyclerViewInProfileTags;
    ProgressBar progressBar;
    TextView chooseTags;
    List<String> idsCategories=new ArrayList<>();
    RelativeLayout submitBtn,cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_interest);
        initWidgets();
        getCategories();
    }
    private void initWidgets(){
         RecyclerViewInProfileTags=findViewById(R.id.RvforCategory);
         progressBar=findViewById(R.id.progressBar);
         chooseTags=findViewById(R.id.chooseTags);
         TextPaint.getGradientColor(chooseTags);
         submitBtn=findViewById(R.id.submitBtn);
         cancelBtn=findViewById(R.id.cancelBtn);
         submitBtn.setOnClickListener(this);
         cancelBtn.setOnClickListener(this);
    }

    private void getCategories(){
        progressBar.setVisibility(View.VISIBLE);
        apiManager.GetCategories(Prefs.GetBearerToken(this), new DataCallback<GetCategoryModel>() {
            @Override
            public void onSuccess(GetCategoryModel getCategoryModel) {
                progressBar.setVisibility(View.GONE);
                RecyclerViewInProfileTags.setHasFixedSize(true);
                RecyclerViewInProfileTags.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                tagsCategoryAdapter = new TagsCategoryAdapter(getApplicationContext(), ChooseInterestActivity.this,getCategoryModel.getData(),true);
                RecyclerViewInProfileTags.setAdapter(tagsCategoryAdapter);
                tagsCategoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(ServerError serverError) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void multiSelectCategory(String id,List<String> ids) {
        idsCategories=ids;
    }

    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.submitBtn:
             submitCategories();
             break;
         case R.id.cancelBtn:
             Prompt.SnackBar(findViewById(android.R.id.content), "Login With New Id");
             startActivity(new Intent(getApplicationContext(), Login.class));
             finish();
     }
    }
    private void showMessage(String message) {
        Prompt.SnackBar(findViewById(android.R.id.content), message);
    }

    private void submitCategories(){
        if(idsCategories.size()==0){
            showMessage("Please select category");
            return;
        }
        CategoriesPojo categoriesPojo=new CategoriesPojo(idsCategories);
        Dialogs.createProgressDialog(ChooseInterestActivity.this);
        apiManager.submitCategories(Prefs.GetBearerToken(this), categoriesPojo, new DataCallback<CategoriesModel>() {
            @Override
            public void onSuccess(CategoriesModel categoriesModel) {
                Log.e("categoriesPojo","Response categoriesPojo"+new Gson().toJson(categoriesModel));
                Prefs.SetBearerToken(ChooseInterestActivity.this,"");
                Prompt.SnackBar(findViewById(android.R.id.content), "Login With New Id");
                Dialogs.HideProgressDialog();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                showMessage(serverError.getErrorMsg());
            }
        });

    }
}