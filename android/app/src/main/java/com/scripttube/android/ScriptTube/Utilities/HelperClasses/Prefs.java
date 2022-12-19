package com.scripttube.android.ScriptTube.Utilities.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.lang.UScript;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Prefs {
    private static final String NAME_OF_THE_USER = "name_of_the_user";
    private static final String USER_NAME = "user_name";
    private static final String USER_IMAGE = "user_image";
    private static String My_Prefference = "com.scripttube.android.ScriptTube";
    private static String BearerToken = "User_Bearer_Token";
    private static String ForgetPasswordEmail = "Forget_Password_Email";
    private static String FORGETPASSWORDTOKEN = "FORGET_PASSWORD_TOKEN";
    private static String ForgetPasswordVerifyOtp = "Forget_Password_Verify_Otp";
    private static String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
    private static String EMAIL_NOTIFICATION = "EMAIL_NOTIFICATION";

    public  static  void SetBearerToken(Context context, String loggedInUserToken){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(BearerToken,loggedInUserToken);
        editor.apply();
    }

    public  static  String GetBearerToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(BearerToken,"");
    }

    public  static  void ClearBearerToken(Context context){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(BearerToken,"");
        editor.apply();
    }



    public  static  void SetForgetPasswordEmail(Context context, String loggedInUserToken){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(ForgetPasswordEmail,loggedInUserToken);
        editor.apply();
    }

    public  static  String GetForgetPasswordEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(ForgetPasswordEmail,"");
    }

    public  static  void ClearForgetPasswordEmail(Context context){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(ForgetPasswordEmail,"");
        editor.apply();
    }




    public  static  void SetFORGETPASSWORDTOKEN(Context context, String loggedInUserToken){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(FORGETPASSWORDTOKEN,loggedInUserToken);
        editor.apply();
    }

    public  static  String GetFORGETPASSWORDTOKEN(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(FORGETPASSWORDTOKEN,"");
    }

    public  static  void ClearFORGETPASSWORDTOKEN(Context context){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(FORGETPASSWORDTOKEN,"");
        editor.apply();
    }


    public  static  void SetForgetPasswordVerifyOtp(Context context, String loggedInUserToken){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(ForgetPasswordVerifyOtp,loggedInUserToken);
        editor.apply();
    }

    public  static  String GetForgetPasswordVerifyOtp(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(ForgetPasswordVerifyOtp,"");
    }

    public  static  void ClearForgetPasswordVerifyOtp(Context context){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(ForgetPasswordVerifyOtp,"");
        editor.apply();
    }



    public  static  void SetPushNotification(Context context, String bool){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(PUSH_NOTIFICATION,bool);
        editor.apply();
    }

    public  static  String GetPushNotification(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(PUSH_NOTIFICATION,"");
    }

    public  static  void ClearPushNotification(Context context){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(PUSH_NOTIFICATION,"");
        editor.apply();
    }


    public  static  void SetEmailNotification(Context context, String bool){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(EMAIL_NOTIFICATION,bool);
        editor.apply();
    }

    public  static  String GetEmailNotification(Context context) {
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(EMAIL_NOTIFICATION,"");
    }

    public  static  void ClearEmailNotification(Context context){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(EMAIL_NOTIFICATION,"");
        editor.apply();
    }

    public static void setUserName(Context context, String userName){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public static String getUserName(Context context){
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_NAME,"");
    }
    public static void setUserImage(Context context, String userName){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(USER_IMAGE, userName);
        editor.apply();
    }

    public static String getUserImage(Context context){
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(USER_IMAGE,"");
    }
    public static void setNameOfUser(Context context, String userName){
        SharedPreferences pref =context.getSharedPreferences(My_Prefference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(NAME_OF_THE_USER, userName);
        editor.apply();
    }

    public static String getNameOfUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        return pref.getString(NAME_OF_THE_USER,"");
    }

    public static void clearUserDetails(Context context){
        SharedPreferences preferences = context.getSharedPreferences(My_Prefference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
