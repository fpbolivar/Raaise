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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.SeeAllPostsOfSearchAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;


public class SeeAllPostFragment extends Fragment {

    View v;
    ImageView BackInSeeAllPosts;
    SearchView SearchViewInSeeAllPosts;
    RecyclerView RecyclerViewInSeeAllPosts;
    SeeAllPostsOfSearchAdapter adapter;
    List<GlobalSearchModel.Data.Posts> list;
    ApiManager apiManager = App.getApiManager();
    RelativeLayout NoVideoFound;
    String TextToShow;

    public SeeAllPostFragment(List<GlobalSearchModel.Data.Posts> list, String TextToShow) {
        this.list = list;
        this.TextToShow = TextToShow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_see_all_post, container, false);
        Initialization();
        ClickListeners();
        SearchViewInSeeAllPosts.setQueryHint(Html.fromHtml("<font color = #d6d6d6>" + "Search Video" + "</font>"));
        SearchViewInSeeAllPosts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HitGlobalSearchApi(newText, "", "", "post");
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
                if (globalSearchModel.getData().getPosts().size() == 0) {
                    NoVideoFound.setVisibility(View.VISIBLE);
                    RecyclerViewInSeeAllPosts.setVisibility(View.GONE);
                } else {
                    NoVideoFound.setVisibility(View.GONE);
                    RecyclerViewInSeeAllPosts.setVisibility(View.VISIBLE);
                }
                list.clear();
                list.addAll(globalSearchModel.getData().getPosts());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {


            }
        });
    }

    private void Initialization() {
        adapter = new SeeAllPostsOfSearchAdapter(v.getContext(), list);
        NoVideoFound = v.findViewById(R.id.NoVideoFound);
        BackInSeeAllPosts = v.findViewById(R.id.BackInSeeAllPost);
        SearchViewInSeeAllPosts = v.findViewById(R.id.SearchViewInSeeAllPosts);
        RecyclerViewInSeeAllPosts = v.findViewById(R.id.RecyclerViewInSeeAllPosts);
        RecyclerViewInSeeAllPosts.setLayoutManager(new GridLayoutManager(v.getContext(), 3));
        RecyclerViewInSeeAllPosts.setHasFixedSize(true);
        RecyclerViewInSeeAllPosts.setAdapter(adapter);
    }

    private void ClickListeners() {
        BackInSeeAllPosts.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }
}