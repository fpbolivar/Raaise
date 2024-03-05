package com.raaise.android.Home.Fragments.tabFragments.donation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raaise.android.Adapters.DonationUserAdapter;
import com.raaise.android.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.raaise.android.R;

import java.util.List;

public class DonationTabFragment extends Fragment {

    RelativeLayout totalDonation;
    RecyclerView totalDonationRecycler;
    View v;
    DonationUserAdapter donationUsersAdapter;
    TextView totalDonationAmount,totalText;
    List<GetPublicUserProfileModel.Data.DonationUsers> donationUsers;
    String totalRaised;

    public DonationTabFragment(List<GetPublicUserProfileModel.Data.DonationUsers> donationUsers, String totalRaised) {
       this.donationUsers=donationUsers;
       this.totalRaised=totalRaised;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_donation_tab, container, false);
        inItWidgets();
        getDonationList();
        return v;
    }
    private void inItWidgets(){
        totalDonation=v.findViewById(R.id.totalDonation);
        totalDonationRecycler=v.findViewById(R.id.totalDonationRecycler);
        totalDonationAmount=v.findViewById(R.id.FollowrCountInFF);
        totalText=v.findViewById(R.id.NameInFF);
    }
    private void getDonationList(){
        totalText.setText("Total Raised");
        totalDonationAmount.setText(String.format("$%s", totalRaised == null || totalRaised.equals("") ? 00.00 : totalRaised));

        if(donationUsers.size()==0){
            totalDonation.setVisibility(View.GONE);
            totalDonationRecycler.setVisibility(View.GONE);
            totalDonation.setBackgroundColor(getActivity().getColor(R.color.Transparent));
        }else{
            totalDonation.setVisibility(View.VISIBLE);
            totalDonationRecycler.setVisibility(View.VISIBLE);
            totalDonation.setBackground(getActivity().getDrawable(R.drawable.curved_dark_grey_background));
        }
        totalDonationRecycler.setNestedScrollingEnabled(false);
        totalDonationRecycler.setHasFixedSize(true);
        totalDonationRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        donationUsersAdapter = new DonationUserAdapter(donationUsers);
        totalDonationRecycler.setAdapter(donationUsersAdapter);
        donationUsersAdapter.notifyDataSetChanged();
    }
}