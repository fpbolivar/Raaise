package com.scripttube.android.ScriptTube;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scripttube.android.ScriptTube.Adapters.FollowerFolowingAdapter;
import com.scripttube.android.ScriptTube.Adapters.GetFolowingListAdapter;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowersModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowingModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class FollowersListFragment extends Fragment implements View.OnClickListener {

    ImageView backBTN;
    TextView select_audio_title;
    int From;
    FollowerFolowingAdapter adapter;
    GetFolowingListAdapter adapter2;
    RecyclerView followers_list_RV;
    List<UserFollowersModel.Data> list;
    ApiManager manager = App.apiManager;
    List<UserFollowingModel.Data> list2;
    View view;

    public FollowersListFragment(int From) {
        this.From = From;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_followers_list, container, false);
        ((Home) requireActivity()).bottomBar.setVisibility(View.GONE);
        inItWidgets(view);
        backBTN.setOnClickListener(this);
        return view;
    }

    private void inItWidgets(View view) {

        select_audio_title = view.findViewById(R.id.select_audio_title);
        backBTN = view.findViewById(R.id.backBtn);
        followers_list_RV = view.findViewById(R.id.followers_list_RV);
        followers_list_RV.setHasFixedSize(true);
        followers_list_RV.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        if (From == 1) {
//            select_audio_title.setText(getResources().getString(R.string.following_list));
            select_audio_title.setText(R.string.following_list);
            list2 = new ArrayList<>();
            adapter2 = new GetFolowingListAdapter(list2,getActivity());
            followers_list_RV.setAdapter(adapter2);
            DoHitFollowingAPi();
        } else {
//            select_audio_title.setText(getResources().getString(R.string.follwer_list));
            select_audio_title.setText(R.string.follwer_list);
            list = new ArrayList<>();
            adapter = new FollowerFolowingAdapter(getActivity(),list);
            followers_list_RV.setAdapter(adapter);
            DoHitFollowerAPi();
        }
    }

    private void DoHitFollowingAPi() {

        UserFollowingModel model = new UserFollowingModel();
//        UserFollowingModel model = new UserFollowingModel("","","");
        Dialogs.createProgressDialog(view.getContext());
        manager.GetFollowingList(Prefs.GetBearerToken(getActivity()), model, new DataCallback<UserFollowingModel>() {
            @Override
            public void onSuccess(UserFollowingModel userFollowingModel) {
                Log.e("CheckManinder", "onFailure: "+userFollowingModel.getData().size() );
                Dialogs.HideProgressDialog();
                list2.addAll(userFollowingModel.getData());
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());
                Dialogs.HideProgressDialog();
            }
        });
    }

    private void DoHitFollowerAPi() {

//        UserFollowersModel model = new UserFollowersModel("","","");
        UserFollowersModel model = new UserFollowersModel();
        Dialogs.createProgressDialog(view.getContext());
        manager.GetFollowersList(Prefs.GetBearerToken(getActivity()), model, new DataCallback<UserFollowersModel>() {
            @Override
            public void onSuccess(UserFollowersModel userFollowersModel) {
                Dialogs.HideProgressDialog();
                list.addAll(userFollowersModel.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());
                Dialogs.HideProgressDialog();
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Home) requireActivity()).bottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                ((Home) requireActivity()).onBackPressed();
                break;
        }
    }
}