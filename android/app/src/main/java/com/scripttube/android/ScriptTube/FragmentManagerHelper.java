package com.scripttube.android.ScriptTube;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentManagerHelper {
    private FragmentManager mFragmentManager;

    public FragmentManagerHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void replace(Fragment fragment, boolean addToBackStack) {
        replace(R.id.FragmentContainer, fragment, addToBackStack);
    }

    public void replace(@IdRes int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction replaceTransaction = mFragmentManager.beginTransaction();
        replaceTransaction.replace(containerId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            replaceTransaction.addToBackStack(fragment.getClass().getName());
        }
        replaceTransaction.commit();
    }
}
