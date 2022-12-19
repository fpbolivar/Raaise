package com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper;

import com.google.gson.JsonObject;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ChangePasswordModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.CreateNewPasswordModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.DeactivateAccountModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.DeleteAccountModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.DeleteVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.FacebookLoginModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ForgetPasswordModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetAllUserVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetCategoryModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetGlobalVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetPolicyModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetUserProfile;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GoogleLoginModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.LoginModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.LogoutModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.Notification_On_OffModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.PublicUserVideoListModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.SignUpModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UpdateCaptionModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UpdateUserProfileModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowersModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowingModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VerifyOtpModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoCommentModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoCommentsReplyModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VideoLikeDislikeModel;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.StringHelper;
import com.scripttube.android.ScriptTube.model.LoginPojo;
import com.scripttube.android.ScriptTube.model.MusicResponse;
import com.scripttube.android.ScriptTube.model.ReportVideoPojo;
import com.scripttube.android.ScriptTube.model.ReportVideoRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    //SignUp APi
    @POST(StringHelper.SIGN_UP_URL)
    Call<SignUpModel> PerformSignUp(@Body SignUpModel signUpModel);

    //LoginAPi
    @POST(StringHelper.LOGIN_URL)
    Call<LoginModel> PerformLogin(@Body LoginPojo loginPojo);

    //ChangePassword
    @POST(StringHelper.CHANGE_PASSWORD_URL)
    Call<ChangePasswordModel> ChangePassword(@Header(StringHelper.AUTHORIZATION) String token,
                                             @Body ChangePasswordModel changePasswordModel);

    //UpdateUserProfile
    @POST(StringHelper.UPDATE_PROFILE_URL)
    Call<UpdateUserProfileModel> UpdateProfile(@Header(StringHelper.AUTHORIZATION) String token,
                                               @Body UpdateUserProfileModel updateUserProfileModel);

    //UpdateUserProfile
    @Multipart
    @POST(StringHelper.UPDATE_PROFILE_URL)
    Call<UpdateUserProfileModel> UpdateProfileWithImage(@Header(StringHelper.AUTHORIZATION) String token,
                                                        @Part MultipartBody.Part image,
                                                        @Part("name") RequestBody name,
                                                        @Part("phoneNumber") RequestBody phoneNumber);


    //GetUserProfile
    @GET(StringHelper.GET_USER_PROFILE)
    Call<GetUserProfile> GetUserProfile(@Header(StringHelper.AUTHORIZATION) String token);

    //FaceBookLogin
    @POST(StringHelper.FACEBOOK_LOGIN)
    Call<FacebookLoginModel> FacebookLogin(@Body FacebookLoginModel facebookLoginModel);

    //ForgetPassword
    @POST(StringHelper.FROGET_PASSWORD)
    Call<ForgetPasswordModel> ForgetPassword(@Body ForgetPasswordModel forgetPasswordModel);

    //VerifyOtp
    @POST(StringHelper.VERIFY_OTP)
    Call<VerifyOtpModel> VerifyOtp(@Body VerifyOtpModel verifyOtpModel);

    //CreateNewPassword
    @POST(StringHelper.CREATENEWPASSWORD)
    Call<CreateNewPasswordModel> CreateNewPassword(@Body CreateNewPasswordModel createNewPasswordModel);

    @POST(StringHelper.GET_MUSIC_LIST)
    Call<MusicResponse> getMusicList(@Header("Authorization") String token);

    //GetPolicyModel
    @POST(StringHelper.GET_POLICY)
    Call<GetPolicyModel> GetPolicy(@Header(StringHelper.AUTHORIZATION) String token, @Body GetPolicyModel getPolicy);

    //Notification_On_OffModel
    @POST(StringHelper.NOTIFICATION_ON_OFF)
    Call<Notification_On_OffModel> NotificationOnOff(@Header(StringHelper.AUTHORIZATION) String token, @Body Notification_On_OffModel notification_on_off);

    //DeactivateUserAccount
    @POST(StringHelper.DEACTIVATE_ACCOUNT)
    Call<DeactivateAccountModel> DeactivateAccount(@Header(StringHelper.AUTHORIZATION) String token);

    //GetAllUserVideo
    @GET(StringHelper.GET_ALL_USER_VIDEO)
    Call<GetAllUserVideoModel> GetAllUserVideo(@Header(StringHelper.AUTHORIZATION) String token);

    //UserFollowUnfollow
    @POST(StringHelper.USER_FOLLOW_UNFOLLOW)
    Call<UserFollowUnfollowModel> UserFollowUnfollow(@Header(StringHelper.AUTHORIZATION) String token,
                                                     @Body UserFollowUnfollowModel userFollowUnfollowModel);

    //VideoLikeDislike
    @POST(StringHelper.VIDEO_LIKE_DISLIKE)
    Call<VideoLikeDislikeModel> VideoLikeDislike(@Header(StringHelper.AUTHORIZATION) String token,
                                                 @Body VideoLikeDislikeModel videoLikeDislikeModel);

    //VideoComment
    @POST(StringHelper.VIDEO_COMMENT)
    Call<VideoCommentModel> VideoComment(@Header(StringHelper.AUTHORIZATION) String token,
                                         @Body VideoCommentModel videoCommentModel);

    @Multipart
    @POST(StringHelper.UPLOAD_VIDEO)
    Call<JsonObject> uploadVideo(@Header(StringHelper.AUTHORIZATION) String token,
                                 @Part("videoCaption") RequestBody videoCaption,
                                 @Part("isDonation") boolean isDonation,
                                 @Part("donationAmount") RequestBody donationAmount,
                                 @Part("audioId") RequestBody audioID,
                                 @Part("categoryId") RequestBody categoryID,
                                 @Part MultipartBody.Part video,
                                 @Part MultipartBody.Part image);

    //VideoCommentReply
    @POST(StringHelper.VIDEO_COMMENT_REPLY)
    Call<VideoCommentsReplyModel> VideoCommentReply(@Header(StringHelper.AUTHORIZATION) String token,
                                                    @Body VideoCommentsReplyModel videoCommentsReplyModel);

    //ListOfVideoComments
    @POST(StringHelper.LIST_OF_VIDEO_COMMENTS)
    Call<ListOfVideoCommentsModel> ListOfVideoComments(@Header(StringHelper.AUTHORIZATION) String token,
                                                       @Body ListOfVideoCommentsModel listOfVideoCommentsModel);

    //GetGlobalVideo
    @POST(StringHelper.GET_GLOBAL_VIDEO)
    Call<GetGlobalVideoModel> GetGlobalVideo(@Header(StringHelper.AUTHORIZATION) String token,
                                             @Body GetGlobalVideoModel getGlobalVideoModel);

    //DeleteUserAccount
    @GET(StringHelper.DELETE_USER_ACCOUNT)
    Call<DeleteAccountModel> DeleteAccount(@Header(StringHelper.AUTHORIZATION) String token);

    //Logout
    @POST(StringHelper.LOGOUT)
    Call<LogoutModel> LogOut(@Header(StringHelper.AUTHORIZATION) String token);

    // Report Video
    @POST(StringHelper.REPORT_VIDEO)
    Call<ReportVideoRes> reportVideo(@Header(StringHelper.AUTHORIZATION) String token,
                                     @Body ReportVideoPojo reportVideo);

    // Report Video
    @POST(StringHelper.GOOGLE_LOGIN)
    Call<GoogleLoginModel> GoogleLogin(@Body GoogleLoginModel googleLoginModel);

    @POST(StringHelper.GET_PUBLIC_USER_PROFILE)
    Call<GetPublicUserProfileModel> GetPublicUserProfile(@Header(StringHelper.AUTHORIZATION) String token, @Body GetPublicUserProfileModel getPublicUserProfileModel);

    @POST(StringHelper.GET_PUBLIC_USER_VIDEO_LIST)
    Call<PublicUserVideoListModel> GetPublicUserVideoList(@Header(StringHelper.AUTHORIZATION) String token, @Body PublicUserVideoListModel publicUserVideoListModel);

    @POST(StringHelper.USER_FOLLOWERS)
    Call<UserFollowersModel> GetUserFollowersList(@Header(StringHelper.AUTHORIZATION) String token, @Body UserFollowersModel userFollowersModel);

    @POST(StringHelper.USER_FOLLOWING)
    Call<UserFollowingModel> GetUserFollowingList(@Header(StringHelper.AUTHORIZATION) String token, @Body UserFollowingModel userFollowingModel);

    @POST(StringHelper.DELETE_USER_VIDEO)
    Call<DeleteVideoModel> DeleteUserVideo(@Header(StringHelper.AUTHORIZATION) String token, @Body DeleteVideoModel deleteVideoModel);

    @POST(StringHelper.UPDATE_VIDEO_CAPTION)
    Call<UpdateCaptionModel> UpdateVideoCaption(@Header(StringHelper.AUTHORIZATION) String token, @Body UpdateCaptionModel updateCaptionModel);

    @GET(StringHelper.GET_CATEGORIES)
    Call<GetCategoryModel> GetCategories(@Header(StringHelper.AUTHORIZATION) String token);
}
