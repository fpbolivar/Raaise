package com.raaise.android.Utilities.HelperClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.raaise.android.R;


public class Dialogs {
    private static ProgressDialog mProgressDialog;
    private static ProgressDialog dialog;

    public static void showProgressDialog(Context mContext) {
        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getString(R.string.Please_Wait));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static void createProgressDialog(Context context) {

        dialog = new ProgressDialog(context);
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        dialog.show();
    }

    public static void HideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

        } else {
            dialog.dismiss();

        }
    }
}
