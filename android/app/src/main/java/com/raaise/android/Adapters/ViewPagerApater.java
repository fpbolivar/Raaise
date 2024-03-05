package com.raaise.android.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPagerApater extends FragmentStateAdapter {
    SelectedInterface anInterface;

    public ViewPagerApater(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, SelectedInterface anInterface) {
        super(fragmentManager, lifecycle);
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        switch (position){
//            case 0:
//                return  anInterface.selectedView(0);
//            case 1:
//                return  anInterface.selectedView(1);
//            case 2:
//                return  anInterface.selectedView(2);
//            case 3:
//                return  anInterface.selectedView(3);
//            case 4:
//                return  anInterface.selectedView(4);
//        }
        return  anInterface.selectedView(position);
    }


    @Override
    public int getItemCount() {
        return 5;
    }

    public interface SelectedInterface{
        Fragment selectedView(int position);
    }
}
