package com.scripttube.android.ScriptTube.Utilities.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetGlobalVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.Home.Fragments.OtherUserProfileActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperClass {

    public static String getCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong.toString();
    }
    public static void DoOpenUserProfile(GetGlobalVideoModel.Data obj, Context context) {
        context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.getUserId().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void SetCaption(TextView hashTagsTV, String videoCaption) {
        if(videoCaption.length() > 15)
        {
            hashTagsTV.setText(Html.fromHtml(videoCaption.substring(0,15)+"..."+"<font color='white'> <u>See More</u></font>"));
        }
        else
        {
            hashTagsTV.setText(videoCaption);
        }
    }


    public static class BackgroundAsyncTask extends AsyncTask<String, Uri, Void> {
        VideoView videoView;
        String Url;
        public BackgroundAsyncTask(VideoView videoView, String url) {
            this.videoView = videoView;
            Url = url;
        }
        @Override
        protected Void doInBackground(String... strings) {
            Log.e("CheckUrl   ", "doInBackground: "+Url );
            videoView.setVideoURI(Uri.parse(Url));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
//                    videoView.start();
                }
            });
            return null;
        }
    }

    public static boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean passwordCharValidation(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static String
    findDifference(String start_date,
                   String end_date) {


        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        // Try Block
        try {
//            System.out.println("maninder   "+start_date+" "+uTCToLocal("yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd HH:mm:ss",start_date));
//            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date d = sdf1.parse(start_date);
//            String formattedTime = output.format(d);

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(uTCToLocal("yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd HH:mm:ss",start_date));
            Date d2 = sdf.parse(end_date);

            // Calculate time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();
//
//            long time = UTCtime;
//
////convert it
//            Time timeFormat = new Time();
//            timeFormat.set(time+ TimeZone.getDefault().getOffset(time));
//
////use the value
//            long difference_In_Time = timeFormat.toMillis(true);

            // Calculate time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;
            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds
//            System.out.print(
//                    "Difference "
//                            + "between two dates is: ");
//
//            System.out.println(
//                    difference_In_Years
//                            + " years, "
//                            + difference_In_Days
//                            + " days, "
//                            + difference_In_Hours
//                            + " hours, "
//                            + difference_In_Minutes
//                            + " minutes, "
//                            + difference_In_Seconds
//                            + " seconds");
            if (difference_In_Years != 0) {
                return difference_In_Years + "Y";
            } else if (difference_In_Days != 0) {
                return difference_In_Days + "D";
            } else if (difference_In_Hours != 0) {
                return difference_In_Hours + "H";
            } else if (difference_In_Minutes != 0) {
                return difference_In_Minutes + "M";
            } else if (difference_In_Seconds != 0) {
                return difference_In_Seconds + "S";
            } else {
                return "";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String uTCToLocal(String dateFormatInPut, String dateFomratOutPut, String datesToConvert) {


        String dateToReturn = datesToConvert;

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatInPut);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date gmt = null;

        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat(dateFomratOutPut);
        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        try {

            gmt = sdf.parse(datesToConvert);
            dateToReturn = sdfOutPutToSend.format(gmt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToReturn; }
}
