package com.raaise.android.Utilities.HelperClasses;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormatSymbols;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.widget.TextView;

import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.GetGlobalVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetSingleVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetVideosBasedOnAudioIdModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserVideoListModel;
import com.raaise.android.ApiManager.ApiModels.ReadSingleNotificationModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperClass {


    public static String getCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong.toString();
    }

    public static String calculateTime(long seconds) {
        try {
            SimpleDateFormat sdf
                    = new SimpleDateFormat(
                    "dd-MM-yyyy hh:mm:ss");


            String date = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new java.util.Date(seconds * 1000));
            Date d2 = sdf.parse(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault()).format(new Date()));
            Date d1 = sdf.parse(date);


            long difference_In_Time
                    = d2.getTime() - d1.getTime();

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


            if (difference_In_Years != 0) {
                return difference_In_Years + " y";
            } else if (difference_In_Days != 0) {
                return difference_In_Days + " d";
            } else if (difference_In_Hours != 0) {
                return difference_In_Hours + " h";
            } else if (difference_In_Minutes != 0) {
                return difference_In_Minutes + " m";
            } else if (difference_In_Seconds != 0) {
                return difference_In_Seconds + " s";
            } else {
                return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "";


    }

    public static void DoOpenUserProfile(GetGlobalVideoModel.Data obj, Context context) {

        context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.getUserId().get_id()).putExtra("UserNameForProfile", obj.getUserId().getUserName()).setFlags(FLAG_ACTIVITY_NEW_TASK));
    }

    public static void DoOpenUserProfileForSingleVideo(GetSingleVideoModel.Data obj, Context context) {

        context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.getGetVideo().getUserId().get_id()).putExtra("UserNameForProfile", obj.getGetVideo().getUserId().getUserName()).setFlags(FLAG_ACTIVITY_NEW_TASK));
    }

    public static void SetCaption(TextView hashTagsTV, String videoCaption) {
        if (videoCaption.length() > 15) {
            hashTagsTV.setText(Html.fromHtml(videoCaption.substring(0, 15) + "..." + "<font color='white'> <u>See More</u></font>"));
        } else {
            hashTagsTV.setText(videoCaption);
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

    public static String getMonthAndDay(String dateStr) {
        try {

            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(dateStr));
            return String.format("%02d", c.get(Calendar.MONTH) + 1) + "-"
                    + c.get(Calendar.DAY_OF_MONTH) + " "
                    + String.format("%02d", c.get(Calendar.HOUR_OF_DAY))
                    + ":" + String.format("%02d", c.get(Calendar.MINUTE));
        } catch (ParseException e) {
            return getMonthAndDay(dateStr);
        }
    }

    public static String findDifference(String start_date, String end_date) {


        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");


        try {

            Date d1 = sdf.parse(uTCToLocal("yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", start_date));
            Date d2 = sdf.parse(end_date);


            long difference_In_Time
                    = d2.getTime() - d1.getTime();

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


            if (difference_In_Years != 0) {
                return difference_In_Years + " years";
            } else if (difference_In_Days != 0) {
                return difference_In_Days + " days";
            } else if (difference_In_Hours != 0) {
                return difference_In_Hours + " hours";
            } else if (difference_In_Minutes != 0) {
                return difference_In_Minutes + " mins";
            } else if (difference_In_Seconds != 0) {
                return difference_In_Seconds + " sec";
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
        return dateToReturn;
    }

    public static void ChangeFollowUnFollow(List<GetGlobalVideoModel.Data> list, String userName, boolean b) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().getUserName().equalsIgnoreCase(userName)) {
                list.get(i).setFollow(b);
            }
        }
    }

    public static void ChangeFollowUnFollowAudio(List<GetVideosBasedOnAudioIdModel.Videos> list, String userName, boolean b) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().getUserName().equalsIgnoreCase(userName)) {
                list.get(i).setFollow(b);
            }
        }
    }

    public static void ChangeFollowUnFollowForOtherUser(List<PublicUserVideoListModel.Data> list, String userName, boolean b) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setFollow(b);
        }
    }

    public static void ReadNotification(String id, Context context) {
        ApiManager apiManager = App.getApiManager();
        ReadSingleNotificationModel model = new ReadSingleNotificationModel(id);
        apiManager.readSingleNotification(Prefs.GetBearerToken(context), model, new DataCallback<ReadSingleNotificationModel>() {
            @Override
            public void onSuccess(ReadSingleNotificationModel readSingleNotificationModel) {

            }

            @Override
            public void onError(ServerError serverError) {

            }
        });
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month.substring(0, 3);
    }
}
