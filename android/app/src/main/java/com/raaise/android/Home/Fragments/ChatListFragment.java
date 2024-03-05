package com.raaise.android.Home.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.raaise.android.Adapters.ChatListAdapter;
import com.raaise.android.Adapters.RoomAdapter;
import com.raaise.android.Adapters.SelectUsersAdapter;
import com.raaise.android.Adapters.SelectedUsersAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetVideosBasedOnAudioIdModel;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowersModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowingModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.MainActivity;
import com.raaise.android.R;
import com.raaise.android.Settings.MyAccount.PrivacyControl;
import com.raaise.android.Settings.MyAccount.ShortBio;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.Utilities.textPaint.TextPaint;
import com.raaise.android.model.BannerModel;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.ChatModel;
import com.raaise.android.model.ChatUserID;
import com.raaise.android.model.GetRoomPojo;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.LiveRoomResponse;
import com.raaise.android.model.PrivacyBody;
import com.raaise.android.model.PrivacyUsersRes;
import com.raaise.android.model.RoomPojo;
import com.raaise.android.model.RoomResponse;
import com.raaise.android.model.SelectUsersModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChatListFragment extends Fragment implements ChatListAdapter.ChatListListener, SelectUsersAdapter.SelectUserListener, SelectedUsersAdapter.UserUnselectedListener, RoomAdapter.RoomListener {
    RecyclerView chatListRecyclerV;
    ChatListAdapter adapter;
    List<ChatListModel.Data> chatListModels;
    ApiManager apiManager = App.getApiManager();
    View view;
    SearchView SearchViewInChatListMain;
    RelativeLayout NoChatFound;
    RecyclerView roomRV;
    private RoomAdapter roomAdapter;
    TextView roomLabel;
    private ArrayList<LiveRoomData> roomDataList;

    private CardView createRoomBtn;

    // New
    private ArrayList<SelectUsersModel> selectUsersModels;
    private ArrayList<SelectUsersModel> selectedUsersModels;
    private SelectUsersAdapter selectUsersAdapter;
    private SelectedUsersAdapter selectedUsersAdapter;
    TextView selectedLabel;
    TextView selectUsersLabel;
    ImageView roomLogo,chatBanner;
    Uri selectedImageUri = null;
    private boolean LogoSelected = false;
    private String roomType = "public";
    private String ROOM_TIMING;
    public static final String GO_LIVE_NOW = "go_live_now";
    public static final String SCHEDULE_LIVE_ROOM = "schedule_live_room";
    Handler handler;
    Runnable searchRunnable;
    private Socket socket;
    public Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] instanceof org.json.JSONArray) {
                org.json.JSONArray data = (org.json.JSONArray) args[0];
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ArrayList<String> chatUserIDS = new Gson().fromJson(String.valueOf(data), new TypeToken<ArrayList<String>>(){}.getType());
                            adapter.setChatIDS(chatUserIDS);
                            } catch (Exception e){
                            }

                        }
                    });
                } catch (Exception e) {
                }
            } else {
                Log.e("chatEmitted", "Invalid data type: " + args[0].getClass().getName());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        ((Home) requireActivity()).videoProgressBar.setVisibility(View.GONE);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
            }
        });
        inItWidgets(view);
        getBanner();
        App app = (App) getActivity().getApplication();
        socket = app.getSocket();
        if (!socket.connected()){
            socket.connect();
        }
        socket.on("onlineUsers", onLogin);

        GetUserListApi("");
        SearchViewInChatListMain.setQueryHint(Html.fromHtml("<font color = #d6d6d6>" + "Search Users" + "</font>"));
        SearchViewInChatListMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                GetUserListApi(newText);
                return false;
            }
        });

    //    getLiveRooms();

        return view;
    }

    private void getLiveRooms() {
        Dialogs.createProgressDialog(getContext());
        GetRoomPojo model = new GetRoomPojo("1", "", "");
        apiManager.getLiveRooms(Prefs.GetBearerToken(getContext()), model, new DataCallback<LiveRoomResponse>() {
            @Override
            public void onSuccess(LiveRoomResponse liveRoomResponse) {
                Dialogs.HideProgressDialog();
                Log.i("liveRooms", "onSuccess: " + new Gson().toJson(liveRoomResponse));
                if (liveRoomResponse.getData().size() > 0){
                    roomLabel.setVisibility(View.VISIBLE);
                } else {
                    roomLabel.setVisibility(View.GONE);
                }
                Log.i("roomLabel", "onSuccess: " + roomAdapter.getItemCount());
                roomDataList.clear();
                roomDataList.addAll(liveRoomResponse.getData());
                roomAdapter.setList(roomDataList);
                Log.i("roomLabel", "onSuccess: " + roomAdapter.getItemCount());
            }

            @Override
            public void onError(ServerError serverError) {
                Log.i("liveRooms", "onSuccess: " + serverError.error);
                Dialogs.HideProgressDialog();
                Toast.makeText(getContext(), serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetUserListApi(String search) {
        ChatListModel model = new ChatListModel(search);
        apiManager.GetUserChatList(Prefs.GetBearerToken(view.getContext()), model, new DataCallback<ChatListModel>() {
            @Override
            public void onSuccess(ChatListModel chatListModel) {
                if (chatListModel.getData().size() == 0) {
                    NoChatFound.setVisibility(View.VISIBLE);
                    chatListRecyclerV.setVisibility(View.GONE);
                } else {
                    chatListModels.clear();
                    NoChatFound.setVisibility(View.GONE);
                    chatListRecyclerV.setVisibility(View.VISIBLE);
                    chatListModels.addAll(chatListModel.getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());

            }
        });

        createRoomBtn.setOnClickListener(v -> showRoomOptionsDialog());


    }

    private void showRoomOptionsDialog(){
        Dialog roomOptionsDialog = new Dialog(getContext());
        roomOptionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        roomOptionsDialog.setContentView(R.layout.room_options_dialog);

        TextView cancelDialog = roomOptionsDialog.findViewById(R.id.cancel_button);
        TextView joinRoomBtn = roomOptionsDialog.findViewById(R.id.join_room_btn);
        TextView generateRoomBtn = roomOptionsDialog.findViewById(R.id.generate_room_btn);
        cancelDialog.setOnClickListener(view -> roomOptionsDialog.dismiss());
        generateRoomBtn.setOnClickListener(view -> {
            roomOptionsDialog.dismiss();
            showCreateRoomDialog();
        });

        joinRoomBtn.setOnClickListener(view -> {
            ((Home) requireActivity()).fragmentManagerHelper.replace(new JoinRoomFragment(true), true);
        roomOptionsDialog.dismiss();
        });

        roomOptionsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        roomOptionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        roomOptionsDialog.getWindow().setGravity(Gravity.CENTER);
        roomOptionsDialog.setCancelable(false);
        roomOptionsDialog.show();
    }

    private void showCreateRoomDialog() {
        Dialog roomDialog;
        selectUsersModels = new ArrayList<>();
        selectedUsersModels = new ArrayList<>();
        ROOM_TIMING = GO_LIVE_NOW;

        EditText roomNameET;
        EditText roomDescET;
        TextView createRoomBTN;
        ImageView closeDialog;

        selectUsersAdapter = new SelectUsersAdapter(getContext(), this);
        selectedUsersAdapter = new SelectedUsersAdapter(getContext(), this);

        TextView roomTypeTV;
        TextView roomTimingTV;
        TextView chooseLogoBtn;
        TextView scheduleTimeTV;
        TextView scheduleDateTV;
        RecyclerView selectUsersRV;
        RecyclerView selectedUsersRV;
        SwitchCompat roomTypeSwitch;
        SwitchCompat roomTimingSwitch;
        LinearLayout dateTimeContainer;

        EditText searchUserET;

        roomDialog = new Dialog(getContext());
        roomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        roomDialog.setContentView(R.layout.room_dialog);
        searchUserET = roomDialog.findViewById(R.id.search_user_et);

        roomNameET = roomDialog.findViewById(R.id.room_name_et);
        roomDescET = roomDialog.findViewById(R.id.room_desc_et);
        closeDialog = roomDialog.findViewById(R.id.imageClose);
        createRoomBTN = roomDialog.findViewById(R.id.create_room_btn);
        selectedLabel = roomDialog.findViewById(R.id.selected_users_label);
        TextPaint.getGradientColor(selectedLabel);
        selectUsersLabel = roomDialog.findViewById(R.id.select_users_label);
        TextPaint.getGradientColor(selectUsersLabel);

        roomTypeTV = roomDialog.findViewById(R.id.room_type_tv);
        roomTimingTV = roomDialog.findViewById(R.id.room_timing_tv);
        roomTypeSwitch = roomDialog.findViewById(R.id.room_type_switch);
        roomTimingSwitch = roomDialog.findViewById(R.id.room_timing_switch);
        chooseLogoBtn = roomDialog.findViewById(R.id.choose_room_logo_btn);
        TextPaint.getGradientColor(chooseLogoBtn);

        roomLogo = roomDialog.findViewById(R.id.room_logo);
        dateTimeContainer = roomDialog.findViewById(R.id.date_time_container);
        scheduleDateTV = roomDialog.findViewById(R.id.schedule_date_tv);
        scheduleTimeTV = roomDialog.findViewById(R.id.schedule_time_tv);

        selectUsersRV = roomDialog.findViewById(R.id.select_users_rv);
        selectedUsersRV = roomDialog.findViewById(R.id.selected_users_rv);
        if (Prefs.getPrivacyPosition(getContext()) == 3){
            selectUsersLabel.setVisibility(View.GONE);
            searchUserET.setVisibility(View.GONE);
        }

        selectedUsersRV.setAdapter(selectedUsersAdapter);

        selectedUsersRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getAllUsers(selectUsersModels, selectUsersAdapter);
        selectUsersRV.setAdapter(selectUsersAdapter);

        searchUserET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (count > 1){
                            Log.i("searchUsers", "onTextChanged: " + s);
                            getAllUsers(selectUsersModels, selectUsersAdapter, s);
                        } else getAllUsers(selectUsersModels, selectUsersAdapter);
                    }
                };
                handler.postDelayed(searchRunnable, 800);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scheduleDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new DatePickerDialog and show it
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Do something with the selected date
                                String formattedMonth = String.format("%02d", month + 1);
                                String selectedDate = year + "-" + formattedMonth + "-" + String.format("%02d", dayOfMonth);
                                scheduleDateTV.setText(selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });

        scheduleTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current time
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new TimePickerDialog and show it
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Format the selected time as "HH:mm:ss"
                                String formattedTime = String.format("%02d:%02d:%02d", hourOfDay, minute, 0);

                                // Set the selected time in the TextView
                                scheduleTimeTV.setText(formattedTime);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        roomTypeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                roomType = "private";
                roomTypeTV.setText("Private");
            } else {
                roomType = "public";
                roomTypeTV.setText("Public");
            }
        });

        roomTimingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                ROOM_TIMING = SCHEDULE_LIVE_ROOM;
                roomTimingTV.setText("Schedule Live Room");
                dateTimeContainer.setVisibility(View.VISIBLE);
            } else {
                ROOM_TIMING = GO_LIVE_NOW;
                roomTimingTV.setText("Go Live Now");
                dateTimeContainer.setVisibility(View.GONE);
            }
        });

        chooseLogoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 102);
            }
        });

        createRoomBTN.setOnClickListener(view -> {
            if (roomNameET.getText().toString().trim().equals("")){
                Toast.makeText(getContext(), "Please give a name to you Room", Toast.LENGTH_SHORT).show();
                return;
            }
            if (roomDescET.getText().toString().trim().equals("")){
                Toast.makeText(getContext(), "Please give a short description to you Room", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Prefs.getPrivacyPosition(getContext()) != 3){
                if (selectedUsersAdapter.getItemCount() == 0){
                    Toast.makeText(getContext(), "Atleast Select one Member to create room", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), roomNameET.getText().toString());
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), roomDescET.getText().toString());
            Bitmap bm=((BitmapDrawable)roomLogo.getDrawable()).getBitmap();
            Uri tempUri = getImageUri(requireContext(), bm, String.valueOf(System.currentTimeMillis() / 1000));
            File file = new File(getRealPathFromURI(tempUri));
            RequestBody requestFileImg = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part logo = MultipartBody.Part.createFormData("logo", file.getName(), requestFileImg);

            StringBuilder stringBuilder = new StringBuilder();
            int totalMembers = selectedUsersAdapter.getList().size();
            int index = 0;
            for (String memberId : selectedUsersAdapter.getList()){
                index++;
                stringBuilder.append(memberId);
                if (index < totalMembers){
                    stringBuilder.append(",");
                }
            }
            Log.i("membersIDS", "showCreateRoomDialog: " + stringBuilder);
            RequestBody memberIds = RequestBody.create(MediaType.parse("text/plain"), stringBuilder.toString());
            RequestBody roomTypeB = RequestBody.create(MediaType.parse("text/plain"), roomType);
            RequestBody scheduleType = null;
            RequestBody scheduleDateTime = null;
            String scheduleDateTimeStr;

            if (roomTimingTV.getText().equals("Go Live Now")){
                scheduleType = RequestBody.create(MediaType.parse("text/plain"), GO_LIVE_NOW);
                scheduleDateTime = RequestBody.create(MediaType.parse("text/plain"), "");
                Log.i("roomTiming", "showCreateRoomDialog: " + GO_LIVE_NOW);
            } else if (roomTimingTV.getText().equals("Schedule Live Room")){
                if (scheduleDateTV.getText().equals("")){
                    Toast.makeText(getContext(), "Please fill date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scheduleTimeTV.getText().equals("")){
                    Toast.makeText(getContext(), "Please fill time", Toast.LENGTH_SHORT).show();
                    return;
                }
                scheduleDateTimeStr = scheduleDateTV.getText() + " " + scheduleTimeTV.getText();
                scheduleType = RequestBody.create(MediaType.parse("text/plain"), SCHEDULE_LIVE_ROOM);
                scheduleDateTime = RequestBody.create(MediaType.parse("text/plain"), scheduleDateTimeStr);
            }
            RoomPojo pojo = new RoomPojo(title, description, logo, memberIds, roomTypeB, scheduleType, scheduleDateTime);

            Dialogs.createProgressDialog(getContext());
            apiManager.createLiveRoom(Prefs.GetBearerToken(getContext()), pojo, new DataCallback<RoomResponse>() {
                @Override
                public void onSuccess(RoomResponse roomResponse) {
                    Dialogs.HideProgressDialog();
                    Toast.makeText(getContext(), roomResponse.message, Toast.LENGTH_SHORT).show();
                    roomDialog.dismiss();
                    getLiveRooms();
                }

                @Override
                public void onError(ServerError serverError) {
                    Dialogs.HideProgressDialog();
                    Toast.makeText(getContext(), serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        closeDialog.setOnClickListener(view -> roomDialog.dismiss());

        roomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        roomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        roomDialog.getWindow().setGravity(Gravity.BOTTOM);
        roomDialog.setCancelable(true);
        roomDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 102) {

            LogoSelected = true;
            ((Home) requireActivity()).SelectInboxScreen();
            selectedImageUri = data.getData();
            Glide.with(getContext())
                    .load(selectedImageUri)
                    .into(roomLogo);

        }
    }

    private ArrayList<SelectUsersModel> getAllUsers(ArrayList<SelectUsersModel> UsersModels, SelectUsersAdapter adapter) {

        PrivacyBody model1 = new PrivacyBody("1", "", "");
        apiManager.getPrivacyUsers(Prefs.GetBearerToken(getContext()), model1, new DataCallback<ArrayList<PrivacyUsersRes.PrivacyData>>() {
            @Override
            public void onSuccess(ArrayList<PrivacyUsersRes.PrivacyData> privacyData) {
                UsersModels.clear();
                ArrayList<SelectUsersModel> allNewModels = selectedUsersAdapter.getUsersList();
                for (PrivacyUsersRes.PrivacyData mod : privacyData){
                    SelectUsersModel selectUsersModel = new SelectUsersModel(mod._id, mod.profileImage, mod.name);
                    UsersModels.add(selectUsersModel);

                }
                UsersModels.addAll(allNewModels);
                HashSet<SelectUsersModel> uniques = new HashSet<SelectUsersModel>(UsersModels);
                UsersModels.clear();
                UsersModels.addAll(uniques);
                ArrayList<SelectUsersModel> toRemove = new ArrayList<>();
                for (SelectUsersModel modg : UsersModels){
                    for (SelectUsersModel mok : allNewModels){
                        if (modg.getId().equals(mok.getId())){
                            toRemove.add(modg);
                        }
                    }
                }
                UsersModels.removeAll(toRemove);
                adapter.setList(UsersModels);
                if (UsersModels.size() == 0){
                    selectUsersLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });

        return UsersModels;

    }

    private ArrayList<SelectUsersModel> getAllUsers(ArrayList<SelectUsersModel> UsersModels, SelectUsersAdapter adapter, CharSequence userName) {

        PrivacyBody model1 = new PrivacyBody("1", "", userName.toString());
        apiManager.getPrivacyUsers(Prefs.GetBearerToken(getContext()), model1, new DataCallback<ArrayList<PrivacyUsersRes.PrivacyData>>() {
            @Override
            public void onSuccess(ArrayList<PrivacyUsersRes.PrivacyData> privacyData) {
                UsersModels.clear();
                ArrayList<SelectUsersModel> allNewModels = selectedUsersAdapter.getUsersList();
                for (PrivacyUsersRes.PrivacyData mod : privacyData){
                    SelectUsersModel selectUsersModel = new SelectUsersModel(mod._id, mod.profileImage, mod.name);
                    UsersModels.add(selectUsersModel);
                    UsersModels.addAll(allNewModels);

                }
                ArrayList<SelectUsersModel> toRemove = new ArrayList<>();
                for (SelectUsersModel modg : UsersModels){
                    for (SelectUsersModel mok : allNewModels){
                        if (modg.getId().equals(mok.getId())){
                            toRemove.add(modg);
                        }
                    }
                }
                UsersModels.removeAll(toRemove);
                adapter.setList(UsersModels);
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });

        return UsersModels;

    }

    private void inItWidgets(View view) {
        roomLabel = view.findViewById(R.id.room_label);
        TextPaint.getGradientColor(roomLabel);
        chatBanner=view.findViewById(R.id.chatBanner);
//        roomRV = view.findViewById(R.id.groupListRV);
//        roomDataList = new ArrayList<>();
//        roomRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        roomAdapter = new RoomAdapter(getContext(), this);
//        roomRV.setAdapter(roomAdapter);
        createRoomBtn = view.findViewById(R.id.create_room_btn);
        SearchViewInChatListMain = view.findViewById(R.id.SearchViewInChatListMain);
        NoChatFound = view.findViewById(R.id.NoChatFound);
        chatListRecyclerV = view.findViewById(R.id.chatListRV);
        chatListRecyclerV.setHasFixedSize(true);
        chatListRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatListModels = new ArrayList<>();
        adapter = new ChatListAdapter(getActivity(), this, chatListModels);
        chatListRecyclerV.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.fromTryAudio){

        }
    }

    @Override
    public void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String
        ShortBio) {
        ((Home) requireActivity()).fragmentManagerHelper.replace(new ChatFragment(Slug, ReceiverId, SenderId, UserImageLink, Username, ShortBio, 1), true);
    }

    @Override
    public void UserSelected(SelectUsersModel model) {
        selectedUsersAdapter.addItem(model);

        selectUsersModels.remove(model);
        selectUsersAdapter.setList(selectUsersModels);

        if (selectUsersAdapter.getItemCount() == 0){
            selectUsersLabel.setVisibility(View.GONE);
        } else {
            selectUsersLabel.setVisibility(View.VISIBLE);
        }
        if (selectedUsersAdapter.getItemCount() > 0){
            selectedLabel.setVisibility(View.VISIBLE);
        } else {
            selectedLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void UserUnselected(SelectUsersModel model) {
        selectUsersModels.add(model);
        selectUsersAdapter.setList(selectUsersModels);

        if (selectUsersAdapter.getItemCount() == 0){
            selectUsersLabel.setVisibility(View.GONE);
        } else {
            selectUsersLabel.setVisibility(View.VISIBLE);
        }
        if (selectedUsersAdapter.getItemCount() > 0){
            selectedLabel.setVisibility(View.VISIBLE);
        } else {
            selectedLabel.setVisibility(View.GONE);
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String name) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "temp" + name, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @Override
    public void roomSelected(LiveRoomData data) {
        String roomData = new Gson().toJson(data);
        try {
            ((Home) requireActivity()).fragmentManagerHelper.replace(new RoomFragment(roomData), true);

        } catch (Exception e){
            Log.i("roomSelected", "roomSelected: " + e.getMessage());
        }
    }

    private void getBanner(){
        apiManager.getBanner(Prefs.GetBearerToken(getContext()), new DataCallback<BannerModel>() {
            @Override
            public void onSuccess(BannerModel bannerModel) {
                if (getActivity() == null) {
                    return;
                }
                Glide.with(getActivity())
                        .load(Prefs.GetBaseUrl(getActivity())+bannerModel.getData().getImage())
                        .into(chatBanner);

                Log.e("link",bannerModel.getData().getLink());
                chatBanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerModel.getData().getLink()));
                                startActivity(openLink);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            }
            @Override
            public void onError(ServerError serverError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.off("onlineUsers", onLogin);
    }
}
