package com.raaise.android.Home.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.raaise.android.Adapters.NotificationAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetSingleVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetUserNotificationsModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;


public class InboxFragment extends Fragment implements NotificationAdapter.notificationListener{
    View v;
    ImageView BackInNotification;
    List<GetUserNotificationsModel.NotificationMessage> list;
    SwipeRefreshLayout SwipeRefreshLayout;

    ImageView Back;
    TextView markAllAsReadBtn;
    ApiManager apiManager = App.getApiManager();
    NotificationAdapter notificationAdapter;
    RecyclerView NotificationRV;
    int PageNo = 1;
    long SizeOfNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inbox, container, false);
        Initialization();
        ClickListeners();
        HitNotificationApi(PageNo);

        return v;
    }

    private void HitNotificationApi(int Page) {
        if (PageNo == 1) {
            Dialogs.createProgressDialog(v.getContext());
        }
        GetUserNotificationsModel model = new GetUserNotificationsModel(String.valueOf(Page), "10");
        apiManager.GetUserNotifications(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<GetUserNotificationsModel>() {
            @Override
            public void onSuccess(GetUserNotificationsModel getUserNotificationsModel) {
                if (PageNo == 1) {
                    Dialogs.HideProgressDialog();
                }
                PageNo++;
                SizeOfNotification = getUserNotificationsModel.getTotalData();
                list.addAll(getUserNotificationsModel.getNotificationMessage());
                notificationAdapter.notifyDataSetChanged();
                if (list.size() != SizeOfNotification) {
                    HitNotificationApi(PageNo);
                }
                SwipeRefreshLayout.setRefreshing(false);
                for (int i = 0; i < getUserNotificationsModel.getNotificationMessage().size() - 1; i++ ){
                    if (!getUserNotificationsModel.getNotificationMessage().get(i).isRead){
                        markAllAsReadBtn.setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }


    private void ClickListeners() {
        BackInNotification.setOnClickListener(view -> {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new HomeFragment(), null)
                    .commit();
        });
        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageNo = 1;
                list.clear();
                HitNotificationApi(PageNo);
            }
        });

        markAllAsReadBtn.setOnClickListener(view -> {
            notificationAdapter.markAllAsRead();
        });
    }

    private void Initialization() {
        list = new ArrayList<>();
        markAllAsReadBtn = v.findViewById(R.id.mark_all_tv);
        SwipeRefreshLayout = v.findViewById(R.id.SwipeRefreshLayout);
        NotificationRV = v.findViewById(R.id.NotificationRV);
        BackInNotification = v.findViewById(R.id.BackInNotification);
        NotificationRV.setHasFixedSize(true);
        NotificationRV.setLayoutManager(new LinearLayoutManager(v.getContext()));
        notificationAdapter = new NotificationAdapter(v.getContext(), list, this);
        NotificationRV.setAdapter(notificationAdapter);
    }

    @Override
    public void showVideo(String slug) {
        GetSingleVideoModel model = new GetSingleVideoModel(slug);
        Dialogs.createProgressDialog(getContext());
        apiManager.GetSingleVideo(Prefs.GetBearerToken(getContext()), model, new DataCallback<GetSingleVideoModel>() {
            @Override
            public void onSuccess(GetSingleVideoModel getSingleVideoModel) {
                Dialogs.HideProgressDialog();
                getContext().startActivity(new Intent(getContext(), SingleVideoActivity.class).putExtra("SlugForSingleVideo", slug));
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                FinishUserHere(serverError.getErrorMsg());
            }
        });
    }

    private void FinishUserHere(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "OK",
                (dialog, id) -> dialog.dismiss());


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}