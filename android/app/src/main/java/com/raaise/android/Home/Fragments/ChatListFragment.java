package com.raaise.android.Home.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.raaise.android.Adapters.ChatListAdapter;
import com.raaise.android.Adapters.RoomAdapter;
import com.raaise.android.Adapters.SelectUsersAdapter;
import com.raaise.android.Adapters.SelectedUsersAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.UserFollowersModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowingModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Settings.MyAccount.ShortBio;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.GetRoomPojo;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.LiveRoomResponse;
import com.raaise.android.model.RoomPojo;
import com.raaise.android.model.RoomResponse;
import com.raaise.android.model.SelectUsersModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
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
    ImageView roomLogo;
    Uri selectedImageUri = null;
    private boolean LogoSelected = false;
    private String roomType = "public";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        inItWidgets(view);
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

        getLiveRooms();

        return view;
    }

    private void getLiveRooms() {
        Dialogs.createProgressDialog(getContext());
        GetRoomPojo model = new GetRoomPojo("1", "", "");
        apiManager.getLiveRooms(Prefs.GetBearerToken(getContext()), model, new DataCallback<LiveRoomResponse>() {
            @Override
            public void onSuccess(LiveRoomResponse liveRoomResponse) {
                Dialogs.HideProgressDialog();
                if (liveRoomResponse.getData().size() > 1){
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
            ((Home) requireActivity()).fragmentManagerHelper.replace(new JoinRoomFragment(), true);
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

        EditText roomNameET;
        EditText roomDescET;
        TextView createRoomBTN;
        ImageView closeDialog;

        selectUsersAdapter = new SelectUsersAdapter(getContext(), this);
        selectedUsersAdapter = new SelectedUsersAdapter(getContext(), this);

        TextView roomTypeTV;
        TextView chooseLogoBtn;
        RecyclerView selectUsersRV;
        RecyclerView selectedUsersRV;
        SwitchCompat roomTypeSwitch;

        roomDialog = new Dialog(getContext());
        roomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        roomDialog.setContentView(R.layout.room_dialog);

        roomNameET = roomDialog.findViewById(R.id.room_name_et);
        roomDescET = roomDialog.findViewById(R.id.room_desc_et);
        closeDialog = roomDialog.findViewById(R.id.imageClose);
        createRoomBTN = roomDialog.findViewById(R.id.create_room_btn);
        selectedLabel = roomDialog.findViewById(R.id.selected_users_label);
        selectUsersLabel = roomDialog.findViewById(R.id.select_users_label);
        roomTypeTV = roomDialog.findViewById(R.id.room_type_tv);
        roomTypeSwitch = roomDialog.findViewById(R.id.room_type_switch);
        chooseLogoBtn = roomDialog.findViewById(R.id.choose_room_logo_btn);
        roomLogo = roomDialog.findViewById(R.id.room_logo);

        selectUsersRV = roomDialog.findViewById(R.id.select_users_rv);
        selectedUsersRV = roomDialog.findViewById(R.id.selected_users_rv);

        selectedUsersRV.setAdapter(selectedUsersAdapter);

        selectedUsersRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getAllUsers(selectUsersModels, selectUsersAdapter);
        selectUsersRV.setAdapter(selectUsersAdapter);

        roomTypeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                roomType = "private";
                roomTypeTV.setText("Private");
            } else {
                roomType = "public";
                roomTypeTV.setText("Public");
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
                Toast.makeText(getContext(), "Please give ashort description to you Room", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedUsersAdapter.getItemCount() == 0){
                Toast.makeText(getContext(), "Please select participants for you room", Toast.LENGTH_SHORT).show();
                return;
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
            RequestBody memberIds = RequestBody.create(MediaType.parse("text/plain"), stringBuilder.toString());
            RequestBody roomTypeB = RequestBody.create(MediaType.parse("text/plain"), roomType);

            RoomPojo pojo = new RoomPojo(title, description, logo, memberIds, roomTypeB);
            Log.i("roomCreation", "showCreateRoomDialog: " + roomNameET.getText().toString());
            Log.i("roomCreation", "showCreateRoomDialog: " + roomDescET.getText().toString());
            Log.i("roomCreation", "showCreateRoomDialog: " + logo.toString());
            Log.i("roomCreation", "showCreateRoomDialog: " + stringBuilder);
            Log.i("roomCreation", "showCreateRoomDialog: " + roomType);
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

            Log.i("roomCreation", "onClick: Creating " + file.getPath());
        });

        closeDialog.setOnClickListener(view -> roomDialog.dismiss());

        roomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private ArrayList<SelectUsersModel> getAllUsers(ArrayList<SelectUsersModel> selectUsersModels, SelectUsersAdapter adapter) {

        UserFollowersModel model1 = new UserFollowersModel("", "", "1");
        apiManager.GetFollowersList(Prefs.GetBearerToken(getActivity()), model1, new DataCallback<UserFollowersModel>() {
            @Override
            public void onSuccess(UserFollowersModel userFollowersModel) {

                if (userFollowersModel != null){
                    for (UserFollowersModel.Data mod : userFollowersModel.getData()){
                        SelectUsersModel selectUsersModel = new SelectUsersModel(mod.followedBy._id, mod.getFollowedBy().getProfileImage(), mod.getFollowedBy().getName());
                        Log.i("userList", "onSuccess: " + selectUsersModel.getId());
                        selectUsersModels.add(selectUsersModel);
                    }
                    adapter.setList(selectUsersModels);
                }

            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());
            }
        });

        return selectUsersModels;

    }

    private void inItWidgets(View view) {
        roomLabel = view.findViewById(R.id.room_label);
        roomRV = view.findViewById(R.id.groupListRV);
        roomDataList = new ArrayList<>();
        roomRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        roomAdapter = new RoomAdapter(getContext(), this);
        roomRV.setAdapter(roomAdapter);
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
        Log.i("roomSelected", "roomSelected: Selected");
        try {
            ((Home) requireActivity()).fragmentManagerHelper.replace(new RoomFragment(roomData), true);

        } catch (Exception e){
            Log.i("roomSelected", "roomSelected: " + e.getMessage());
        }
    }
}
