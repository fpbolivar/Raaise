package com.scripttube.android.ScriptTube.Utilities.HelperClasses;

import android.text.Html;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.scripttube.android.ScriptTube.R;

public class Prompt {
    public static void SnackBar(View view, String Message) {
        Snackbar.make(view, Html.fromHtml("<b>" + Message + "</b>"), Snackbar.LENGTH_LONG)
                .setBackgroundTint(view.getResources().getColor(R.color.LogoGreyColor))
                .setTextColor(view.getResources().getColor(R.color.WhiteColor)).show();
    }
}
