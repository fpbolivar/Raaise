package com.raaise.android.Home.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.raaise.android.Adapters.PublicRoomAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.PublicRoomPojo;

import java.util.ArrayList;

public class JoinRoomFragment extends Fragment implements PublicRoomAdapter.PublicRoomListener {

    private ApiManager apiManager = App.getApiManager();
    private ArrayList<LiveRoomData> liveRoomData;

    ImageView backBTN;
    private RecyclerView publicRoomRV;
    private PublicRoomAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joinroom, container, false);

        inItWidgets(view);
        getAllPublicRooms();
        backBTN.setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }

    private void getAllPublicRooms() {
        Log.i("userToken", "getAllPublicRooms: " + Prefs.GetBearerToken(getContext()));
        Dialogs.createProgressDialog(getContext());
        PublicRoomPojo model = new PublicRoomPojo("1", "20");
        apiManager.getPublicRooms(Prefs.GetBearerToken(getContext()), model, new DataCallback<ArrayList<LiveRoomData>>() {
            @Override
            public void onSuccess(ArrayList<LiveRoomData> roomData) {
                Log.i("publicRoom", "onSuccess: " + roomData.size());
                Dialogs.HideProgressDialog();
                liveRoomData.clear();
                liveRoomData.addAll(roomData);
                adapter.setList(liveRoomData);
            }

            @Override
            public void onError(ServerError serverError) {
                Log.i("publicRoom", "onError: " + serverError.error);
                Dialogs.HideProgressDialog();

            }
        });
    }

    private void inItWidgets(View view) {
        backBTN = view.findViewById(R.id.backBtn);
        publicRoomRV = view.findViewById(R.id.public_room_rv);
        liveRoomData = new ArrayList<>();
        adapter = new PublicRoomAdapter(getContext(), this);
        publicRoomRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        publicRoomRV.setAdapter(adapter);
    }

    @Override
    public void RoomSelected(LiveRoomData data) {
        ((Home) requireActivity()).fragmentManagerHelper.replace(new PublicRoomFragment(new Gson().toJson(data)), true);
    }
}
