package com.raaise.android;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.FollowerFolowingAdapter;
import com.raaise.android.Adapters.GetFolowingListAdapter;
import com.raaise.android.Adapters.PublicUserFollowersAdapter;
import com.raaise.android.Adapters.PublicUserFollowingAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.PublicUserFollowersModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserFollowingModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowersModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowingModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class FollowersListFragment extends Fragment implements View.OnClickListener {

    ImageView backBTN;
    TextView select_audio_title;
    int From;
    FollowerFolowingAdapter adapter;
    GetFolowingListAdapter adapter2;
    RecyclerView followers_list_RV;
    RelativeLayout NoResultFound;
    List<UserFollowersModel.Data> list;
    ApiManager manager = App.apiManager;
    List<UserFollowingModel.Data> list2;
    List<PublicUserFollowingModel.Data> list3;
    List<PublicUserFollowersModel.Data> list4;
    View view;
    PublicUserFollowersAdapter adapter4;
    PublicUserFollowingAdapter adapter3;
    String Username;
    SearchView CommonFollowerFollowingListSearchView;

    public FollowersListFragment(int From) {
        this.From = From;
    }

    public FollowersListFragment(int From, String Username) {
        this.From = From;
        this.Username = Username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_followers_list, container, false);

        inItWidgets(view);
        backBTN.setOnClickListener(this);

        CommonFollowerFollowingListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (From == 1) {
                    DoHitFollowingAPi(newText);
                } else if (From == 2) {
                    DoHitFollowerAPi(newText);
                } else if (From == 3) {

                    DoHitPublicUserFollowingAPi(Username, newText);
                } else if (From == 4) {

                    DoHitPublicUserFollowerAPi(Username, newText);
                } else if (newText.isEmpty()) {
                    NoResultFound.setVisibility(View.GONE);
                }
                return false;
            }
        });
        return view;
    }

    private void inItWidgets(View view) {
        NoResultFound = view.findViewById(R.id.NoResultFound);
        CommonFollowerFollowingListSearchView = view.findViewById(R.id.CommonFollowerFollowingListSearchView);
        CommonFollowerFollowingListSearchView.setQueryHint(Html.fromHtml("<font color = #949292>" + "Search Users" + "</font>", Html.FROM_HTML_MODE_LEGACY));
        select_audio_title = view.findViewById(R.id.select_audio_title);
        backBTN = view.findViewById(R.id.backBtn);
        followers_list_RV = view.findViewById(R.id.followers_list_RV);
        followers_list_RV.setHasFixedSize(true);
        followers_list_RV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        NoResultFound.setVisibility(View.GONE);
        if (From == 1) {

            select_audio_title.setText(R.string.following_list);
            list2 = new ArrayList<>();
            adapter2 = new GetFolowingListAdapter(list2, getActivity());
            followers_list_RV.setAdapter(adapter2);
            DoHitFollowingAPi("");

        } else if (From == 2) {

            select_audio_title.setText(R.string.follwer_list);
            list = new ArrayList<>();
            adapter = new FollowerFolowingAdapter(getActivity(), list);
            followers_list_RV.setAdapter(adapter);
            DoHitFollowerAPi("");

        } else if (From == 3) {

            select_audio_title.setText(R.string.following_list);
            list3 = new ArrayList<>();
            adapter3 = new PublicUserFollowingAdapter(list3, getActivity());
            followers_list_RV.setAdapter(adapter3);

            DoHitPublicUserFollowingAPi(Username, "");


        } else if (From == 4) {

            select_audio_title.setText(R.string.follwer_list);
            list4 = new ArrayList<>();
            adapter4 = new PublicUserFollowersAdapter(getActivity(), list4);
            followers_list_RV.setAdapter(adapter4);
            DoHitPublicUserFollowerAPi(Username, "");

        }
    }

    private void DoHitPublicUserFollowingAPi(String username, String s) {
        PublicUserFollowingModel model = new PublicUserFollowingModel(username, s, "", "1");

        manager.GetPublicUserFollowing(Prefs.GetBearerToken(getActivity()), model, new DataCallback<PublicUserFollowingModel>() {
            @Override
            public void onSuccess(PublicUserFollowingModel publicUserFollowingModel) {

                list3.clear();
                list3.addAll(publicUserFollowingModel.getData());
                NoResultFound.setVisibility(View.GONE);
                if (list3.size() != 0) {
                    NoResultFound.setVisibility(View.GONE);
                } else {
                    NoResultFound.setVisibility(View.VISIBLE);
                }
                adapter3.notifyDataSetChanged();

            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, "DoHitPublicUserFollowingAPi" + serverError.getErrorMsg());
                NoResultFound.setVisibility(View.VISIBLE);

            }
        });
    }

    private void DoHitPublicUserFollowerAPi(String username, String s) {
        PublicUserFollowersModel model = new PublicUserFollowersModel(username, s, "", "1");

        manager.GetPublicUserFollowers(Prefs.GetBearerToken(getActivity()), model, new DataCallback<PublicUserFollowersModel>() {
            @Override
            public void onSuccess(PublicUserFollowersModel publicUserFollowersModel) {
                list4.clear();

                list4.addAll(publicUserFollowersModel.getData());
                NoResultFound.setVisibility(View.GONE);
                if (list4.size() != 0) {
                    NoResultFound.setVisibility(View.GONE);
                } else {
                    NoResultFound.setVisibility(View.VISIBLE);
                }
                adapter4.notifyDataSetChanged();

            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, "DoHitPublicUserFollowerAPi" + serverError.getErrorMsg());
                NoResultFound.setVisibility(View.VISIBLE);

            }
        });
    }


    private void DoHitFollowingAPi(String s) {


        UserFollowingModel model = new UserFollowingModel(s, "", "1");

        manager.GetFollowingList(Prefs.GetBearerToken(getActivity()), model, new DataCallback<UserFollowingModel>() {
            @Override
            public void onSuccess(UserFollowingModel userFollowingModel) {
                list2.clear();

                list2.addAll(userFollowingModel.getData());
                NoResultFound.setVisibility(View.GONE);
                if (list2.size() != 0) {
                    NoResultFound.setVisibility(View.GONE);
                } else {
                    NoResultFound.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());
                NoResultFound.setVisibility(View.VISIBLE);

            }
        });
    }

    private void DoHitFollowerAPi(String s) {

        UserFollowersModel model = new UserFollowersModel(s, "", "1");


        manager.GetFollowersList(Prefs.GetBearerToken(getActivity()), model, new DataCallback<UserFollowersModel>() {
            @Override
            public void onSuccess(UserFollowersModel userFollowersModel) {

                list.clear();
                list.addAll(userFollowersModel.getData());
                NoResultFound.setVisibility(View.GONE);
                if (list.size() != 0) {
                    NoResultFound.setVisibility(View.GONE);
                } else {
                    NoResultFound.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(view, serverError.getErrorMsg());
                NoResultFound.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                requireActivity().onBackPressed();
                break;
        }
    }
}