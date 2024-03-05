package com.raaise.android.Utilities.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.raaise.android.Settings.MyAccount.PrivacyControl;

public class Prefs {
    private static final String NAME_OF_THE_USER = "name_of_the_user";
    private static final String USER_NAME = "user_name";
    private static final String PHONE_NUMBER_OF_USER = "phone_number_of_user";
    private static final String USER_IMAGE = "user_image";
    private static final String USER_COVER_IMAGE = "user_cover_image";

    private static final String USER_EMAIL = "user_email";
    private static final String USER_ShortBio = "USER_ShortBio";
    private static final String USER_ID = "USER_ID";
    private static final String My_Prefference = "com.scripttube.android.ScriptTube";
    private static final String BearerToken = "User_Bearer_Token";
    private static final String BASEURL = "User_Base_Url";
    private static final String ForgetPasswordEmail = "Forget_Password_Email";
    private static final String FORGETPASSWORDTOKEN = "FORGET_PASSWORD_TOKEN";
    private static final String ForgetPasswordVerifyOtp = "Forget_Password_Verify_Otp";
    private static final String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
    private static final String EMAIL_NOTIFICATION = "EMAIL_NOTIFICATION";
    private static final String PRIVACY_POSITION = "PRIVACY_POSITION";

    public static void SetBearerToken(Context context, String loggedInUserToken) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BearerToken, loggedInUserToken);
        editor.apply();
    }

    public static String GetBearerToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(BearerToken, "");
    }

    public static void SetBaseUrl(Context context, String loggedInUserToken) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BASEURL, loggedInUserToken);
        editor.apply();
    }

    public static String GetBaseUrl(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(BASEURL, "");
    }

    public static void ClearBaseUrl(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BASEURL, "");
        editor.apply();
    }

    public static void ClearBearerToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BearerToken, "");
        editor.apply();
    }


    public static void SetForgetPasswordEmail(Context context, String loggedInUserToken) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ForgetPasswordEmail, loggedInUserToken);
        editor.apply();
    }

    public static String GetForgetPasswordEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(ForgetPasswordEmail, "");
    }

    public static void ClearForgetPasswordEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ForgetPasswordEmail, "");
        editor.apply();
    }


    public static void SetFORGETPASSWORDTOKEN(Context context, String loggedInUserToken) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FORGETPASSWORDTOKEN, loggedInUserToken);
        editor.apply();
    }

    public static String GetFORGETPASSWORDTOKEN(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(FORGETPASSWORDTOKEN, "");
    }

    public static void ClearFORGETPASSWORDTOKEN(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FORGETPASSWORDTOKEN, "");
        editor.apply();
    }


    public static void SetForgetPasswordVerifyOtp(Context context, String loggedInUserToken) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ForgetPasswordVerifyOtp, loggedInUserToken);
        editor.apply();
    }

    public static String GetForgetPasswordVerifyOtp(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(ForgetPasswordVerifyOtp, "");
    }

    public static void ClearForgetPasswordVerifyOtp(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ForgetPasswordVerifyOtp, "");
        editor.apply();
    }


    public static void SetPushNotification(Context context, String bool) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PUSH_NOTIFICATION, bool);
        editor.apply();
    }

    public static String GetPushNotification(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(PUSH_NOTIFICATION, "");
    }

    public static void ClearPushNotification(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PUSH_NOTIFICATION, "");
        editor.apply();
    }


    public static void SetEmailNotification(Context context, String bool) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(EMAIL_NOTIFICATION, bool);
        editor.apply();
    }

    public static String GetEmailNotification(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(EMAIL_NOTIFICATION, "");
    }

    public static void ClearEmailNotification(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(EMAIL_NOTIFICATION, "");
        editor.apply();
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_NAME, "");
    }

    public static void setUserImage(Context context, String userName) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_IMAGE, userName);
        editor.apply();
    }

    public static String getUserCoverImage(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_COVER_IMAGE, "");
    }

    public static void setUserCoverImage(Context context, String userName) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_COVER_IMAGE, userName);
        editor.apply();
    }

    public static String getUserImage(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_IMAGE, "");
    }

    public static void setNameOfUser(Context context, String userName) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(NAME_OF_THE_USER, userName);
        editor.apply();
    }

    public static String getNameOfUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(NAME_OF_THE_USER, "");
    }

    public static void clearUserDetails(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void SetPhoneNumberOfTheUser(Context context, String bool) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PHONE_NUMBER_OF_USER, bool);
        editor.apply();
    }

    public static String GetPhoneNumberOfTheUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(PHONE_NUMBER_OF_USER, "");
    }

    public static void ClearPhoneNumberOfTheUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PHONE_NUMBER_OF_USER, "");
        editor.apply();
    }


    //user Email
    public static void SetUserEmail(Context context, String bool) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_EMAIL, bool);
        editor.apply();
    }

    public static String GetUserEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_EMAIL, "");
    }

    public static void ClearUserEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_EMAIL, "");
        editor.apply();
    }

    //user ShortBio
    public static void SetUserShortBio(Context context, String bool) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ShortBio, bool);
        editor.apply();
    }

    public static String GetUserShortBio(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_ShortBio, "");
    }

    public static void ClearUserShortBio(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ShortBio, "");
        editor.apply();
    }


    public static void SetUserID(Context context, String bool) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ID, bool);
        editor.apply();
    }

    public static String GetUserID(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_ID, "");
    }

    public static void ClearUserID(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ID, "");
        editor.apply();
    }


    //
    public static void SetBankDetails(Context context, String Data, String Key) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, Data);
        editor.apply();
    }

    public static String GetBankDetails(Context context, String Key) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(Key, "");
    }

    public static void ClearBankDetails(Context context, String Key) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, "");
        editor.apply();
    }


    public static void SetExtra(Context context, String Data, String Key) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, Data);
        editor.apply();
    }

    public static String GetExtra(Context context, String Key) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(Key, "");
    }

    public static void ClearExtra(Context context, String Key) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, "");
        editor.apply();
    }

    public static void setPrivacyPosition(Context context, int position){
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PRIVACY_POSITION, position);
        editor.apply();
    }

    public static int getPrivacyPosition(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getInt(PRIVACY_POSITION, -1);
    }

    public static void clearPrivacyPosition(Context context){
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PRIVACY_POSITION, -1);
        editor.apply();
    }
}
