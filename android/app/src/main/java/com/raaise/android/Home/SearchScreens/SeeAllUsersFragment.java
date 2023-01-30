package com.raaise.android.Home.SearchScreens;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.SeeAllUsersOfSearchAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;

public class SeeAllUsersFragment extends Fragment {

    View v;
    ImageView BackInSeeAllUser;
    SearchView SearchViewInSeeAllUser;
    RecyclerView RecyclerViewInSeeAllUser;
    SeeAllUsersOfSearchAdapter adapter;
    ApiManager apiManager = App.getApiManager();
    List<GlobalSearchModel.Data.Users> list;
    RelativeLayout NoUserFound;
    String TextToShow;

    public SeeAllUsersFragment(List<GlobalSearchModel.Data.Users> list, String TextToShow) {
        this.list = list;
        this.TextToShow = TextToShow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_see_all_users, container, false);
        Initialization();
        ClickListeners();
        SearchViewInSeeAllUser.setQueryHint(Html.fromHtml("<font color = #d6d6d6>" + "Search User" + "</font>"));
        SearchViewInSeeAllUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HitGlobalSearchApi(newText, "", "", "user");
                return false;
            }
        });
        return v;
    }

    public void HitGlobalSearchApi(String search, String limit, String page, String type) {
        GlobalSearchModel model = new GlobalSearchModel(search, limit, page, type);
        apiManager.GetGlobalSearch(Prefs.GetBearerToken(v.getContext()), model, new DataCallback<GlobalSearchModel>() {
            @Override
            public void onSuccess(GlobalSearchModel globalSearchModel) {

                if (globalSearchModel.getData().getUsers().size() == 0) {
                    NoUserFound.setVisibility(View.VISIBLE);
                    RecyclerViewInSeeAllUser.setVisibility(View.GONE);
                } else {
                    NoUserFound.setVisibility(View.GONE);
                    RecyclerViewInSeeAllUser.setVisibility(View.VISIBLE);
                }
                list.clear();
                list.addAll(globalSearchModel.getData().getUsers());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {


            }
        });
    }

    private void Initialization() {
        adapter = new SeeAllUsersOfSearchAdapter(v.getContext(), list);
        NoUserFound = v.findViewById(R.id.NoUserFound);
        BackInSeeAllUser = v.findViewById(R.id.BackInSeeAllUser);
        SearchViewInSeeAllUser = v.findViewById(R.id.SearchViewInSeeAllUser);
        RecyclerViewInSeeAllUser = v.findViewById(R.id.RecyclerViewInSeeAllUsers);
        RecyclerViewInSeeAllUser.setLayoutManager(new LinearLayoutManager(v.getContext()));
        RecyclerViewInSeeAllUser.setHasFixedSize(true);
        RecyclerViewInSeeAllUser.setAdapter(adapter);
    }

    private void ClickListeners() {
        BackInSeeAllUser.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }
}