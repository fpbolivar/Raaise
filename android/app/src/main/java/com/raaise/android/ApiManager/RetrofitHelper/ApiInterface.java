package com.raaise.android.ApiManager.RetrofitHelper;

import com.google.gson.JsonObject;
import com.raaise.android.ApiManager.ApiModels.ChangePasswordModel;
import com.raaise.android.ApiManager.ApiModels.CreateNewPasswordModel;
import com.raaise.android.ApiManager.ApiModels.DeactivateAccountModel;
import com.raaise.android.ApiManager.ApiModels.DeleteAccountModel;
import com.raaise.android.ApiManager.ApiModels.DeleteCardModel;
import com.raaise.android.ApiManager.ApiModels.DeleteVideoModel;
import com.raaise.android.ApiManager.ApiModels.FacebookLoginModel;
import com.raaise.android.ApiManager.ApiModels.ForgetPasswordModel;
import com.raaise.android.ApiManager.ApiModels.GetAllUserVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetCategoryModel;
import com.raaise.android.ApiManager.ApiModels.GetGlobalVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetPolicyModel;
import com.raaise.android.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.raaise.android.ApiManager.ApiModels.GetSingleVideoModel;
import com.raaise.android.ApiManager.ApiModels.GetUserNotificationsModel;
import com.raaise.android.ApiManager.ApiModels.GetUserProfile;
import com.raaise.android.ApiManager.ApiModels.GetVideosBasedOnAudioIdModel;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.ApiManager.ApiModels.GoogleLoginModel;
import com.raaise.android.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.raaise.android.ApiManager.ApiModels.LoginModel;
import com.raaise.android.ApiManager.ApiModels.LogoutModel;
import com.raaise.android.ApiManager.ApiModels.MakePaymentByCardIdModel;
import com.raaise.android.ApiManager.ApiModels.Notification_On_OffModel;
import com.raaise.android.ApiManager.ApiModels.Payment_AddCardModel;
import com.raaise.android.ApiManager.ApiModels.Payment_AddUpdateBankDetailsModel;
import com.raaise.android.ApiManager.ApiModels.Payment_GetCardsModel;
import com.raaise.android.ApiManager.ApiModels.ProfileChatModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserFollowersModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserFollowingModel;
import com.raaise.android.ApiManager.ApiModels.PublicUserVideoListModel;
import com.raaise.android.ApiManager.ApiModels.ReadSingleNotificationModel;
import com.raaise.android.ApiManager.ApiModels.SetDefaultCardModel;
import com.raaise.android.ApiManager.ApiModels.SharVideoModel;
import com.raaise.android.ApiManager.ApiModels.SignUpModel;
import com.raaise.android.ApiManager.ApiModels.UnReadNotificationCountModel;
import com.raaise.android.ApiManager.ApiModels.UnreadMessageCountModel;
import com.raaise.android.ApiManager.ApiModels.UpdateCaptionModel;
import com.raaise.android.ApiManager.ApiModels.UpdateUserProfileModel;
import com.raaise.android.ApiManager.ApiModels.UserDonationHistoryModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowUnfollowModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowersModel;
import com.raaise.android.ApiManager.ApiModels.UserFollowingModel;
import com.raaise.android.ApiManager.ApiModels.UserVideoDonationHistoryModel;
import com.raaise.android.ApiManager.ApiModels.VerifyOtpModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentModel;
import com.raaise.android.ApiManager.ApiModels.VideoCommentsReplyModel;
import com.raaise.android.ApiManager.ApiModels.VideoLikeDislikeModel;
import com.raaise.android.Utilities.HelperClasses.StringHelper;
import com.raaise.android.model.BlockUserPojo;
import com.raaise.android.model.BlockVideoPojo;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.ChatModel;
import com.raaise.android.model.ClaimedAmountPojo;
import com.raaise.android.model.CommentReplyPojo;
import com.raaise.android.model.DeleteCommentPojo;
import com.raaise.android.model.DeleteCommentReply;
import com.raaise.android.model.EditVideoCmntPojo;
import com.raaise.android.model.LoginPojo;
import com.raaise.android.model.MusicResponse;
import com.raaise.android.model.ReportVideoPojo;
import com.raaise.android.model.ReportVideoRes;
import com.raaise.android.model.VideoCommentDelete;
import com.raaise.android.model.VideoDonationModal;
import com.raaise.android.model.VideoDonationPojo;
import com.raaise.android.model.WithdrawalsPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

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

    @POST(StringHelper.GET_USER_FOLLOWER_LIST)
    Call<PublicUserFollowersModel> GetPublicUserFollowers(@Header(StringHelper.AUTHORIZATION) String token, @Body PublicUserFollowersModel publicUserFollowersModel);

    @POST(StringHelper.GET_PUBLIC_FOLLOWING_LIST)
    Call<PublicUserFollowingModel> GetPublicUserFollowing(@Header(StringHelper.AUTHORIZATION) String token, @Body PublicUserFollowingModel publicUserFollowingModel);

    @POST(StringHelper.GET_VIDEOS_BASED_ON_AUDIO)
    Call<GetVideosBasedOnAudioIdModel> GetVideoBaseOnAudio(@Header(StringHelper.AUTHORIZATION) String token, @Body GetVideosBasedOnAudioIdModel getVideosBasedOnAudioIdModel);

    @POST(StringHelper.GLOBAL_SEARCH)
    Call<GlobalSearchModel> GetGlobalSearch(@Header(StringHelper.AUTHORIZATION) String token, @Body GlobalSearchModel globalSearchModel);

    @POST(StringHelper.GET_USER_NOTIFICATION)
    Call<GetUserNotificationsModel> GetUserNotifications(@Header(StringHelper.AUTHORIZATION) String token, @Body GetUserNotificationsModel getUserNotificationsModel);

    @POST(StringHelper.GET_SINGLE_VIDEO)
    Call<GetSingleVideoModel> GetSingleVideo(@Header(StringHelper.AUTHORIZATION) String token, @Body GetSingleVideoModel getSingleVideoModel);

    @POST(StringHelper.CHALT_LIST)
    Call<ChatListModel> GetChatList(@Header(StringHelper.AUTHORIZATION) String token, @Body ChatListModel chatListModel);

    @POST(StringHelper.SINGLE_CHAT)
    Call<ChatModel> GetSingleChat(@Header(StringHelper.AUTHORIZATION) String token, @Body ChatModel chatModel);

    @POST(StringHelper.READ_SINGLE_NOTIFICATION)
    Call<ReadSingleNotificationModel> ReadSingleNotification(@Header(StringHelper.AUTHORIZATION) String token, @Body ReadSingleNotificationModel readSingleNotificationModel);

    @GET(StringHelper.UNREAD_MESSAGE_COUNT)
    Call<UnreadMessageCountModel> UnreadMessageCount(@Header(StringHelper.AUTHORIZATION) String token);

    @GET(StringHelper.UNREAD_NOTIFICATION_COUNT)
    Call<UnReadNotificationCountModel> UnreadNotificationCount(@Header(StringHelper.AUTHORIZATION) String token);

    @POST(StringHelper.SHARE_VIDEO_API)
    Call<SharVideoModel> SharVideo(@Header(StringHelper.AUTHORIZATION) String token, @Body SharVideoModel sharVideoModel);

    @POST(StringHelper.PROFILE_CHAT_API)
    Call<ProfileChatModel> ProfileChat(@Header(StringHelper.AUTHORIZATION) String token, @Body ProfileChatModel profileChatModel);


    //***************************************************************   Payment APi's Below  ***************************************************************
    @POST(StringHelper.ADD_UPDATE_BANK_DETAILS)
    Call<Payment_AddUpdateBankDetailsModel> AddUpdateBankDetails(@Header(StringHelper.AUTHORIZATION) String token, @Body Payment_AddUpdateBankDetailsModel payment_addUpdateBankDetailsModel);

    @POST(StringHelper.ADD_CARD)
    Call<Payment_AddCardModel> AddCard(@Header(StringHelper.AUTHORIZATION) String token, @Body Payment_AddCardModel payment_addCardModel);

    @POST(StringHelper.SET_DEFAULT_CARD)
    Call<SetDefaultCardModel> SetDefaultCard(@Header(StringHelper.AUTHORIZATION) String token, @Body SetDefaultCardModel setDefaultCardModel);

    @GET(StringHelper.GET_CARDS)
    Call<Payment_GetCardsModel> GetCards(@Header(StringHelper.AUTHORIZATION) String token);

    @POST(StringHelper.MAKE_PAYMENT_BY_CARD_ID)
    Call<MakePaymentByCardIdModel> MakePaymentByCardId(@Header(StringHelper.AUTHORIZATION) String token, @Body MakePaymentByCardIdModel makePaymentByCardIdModel);

    @POST(StringHelper.DELETE_CARD)
    Call<DeleteCardModel> DeleteCards(@Header(StringHelper.AUTHORIZATION) String token, @Body DeleteCardModel deleteCardModel);

    @POST(StringHelper.DONATION_HISTORY)
    Call<UserDonationHistoryModel> DonationHistory(@Header(StringHelper.AUTHORIZATION) String token);

    @POST(StringHelper.VIDEO_DONATION_HISTORY)
    Call<UserVideoDonationHistoryModel> VideoDonationHistory(@Header(StringHelper.AUTHORIZATION) String token, @Body UserVideoDonationHistoryModel userVideoDonationHistoryModel);

    @POST(StringHelper.VIDEO_DONATION_HISTORY)
    Call<VideoDonationPojo> getVideoDonationHistory(@Header(StringHelper.AUTHORIZATION) String token, @Body VideoDonationModal modal);

    @POST(StringHelper.GET_USER_WITHDRAWAL)
    Call<WithdrawalsPojo> getUserWithdrawal(@Header(StringHelper.AUTHORIZATION) String token);

    @POST(StringHelper.CLAIM_AMOUNT)
    Call<ClaimedAmountPojo> claimVideoAmount(@Header(StringHelper.AUTHORIZATION) String token, @Body VideoDonationModal videoDonationModal);

    @POST(StringHelper.DELETE_VIDEO_COMMENT)
    Call<VideoCommentDelete> deleteVideoComment(@Header(StringHelper.AUTHORIZATION) String token, @Body DeleteCommentPojo pojo);

    @POST(StringHelper.EDIT_VIDEO_COMMENT)
    Call<VideoCommentDelete> editVideoComment(@Header(StringHelper.AUTHORIZATION) String token, @Body EditVideoCmntPojo pojo);

    @POST(StringHelper.DELETE_COMMENT_REPLY)
    Call<VideoCommentDelete> deleteCommentReply(@Header(StringHelper.AUTHORIZATION) String toke, @Body DeleteCommentReply pojo);

    @POST(StringHelper.EDIT_COMMENT_REPLY)
    Call<VideoCommentDelete> editCommentReply(@Header(StringHelper.AUTHORIZATION) String token, @Body CommentReplyPojo pojo);

    @POST(StringHelper.BLOCK_USER)
    Call<VideoCommentDelete> blockUser(@Header(StringHelper.AUTHORIZATION) String token, @Body BlockUserPojo pojo);

    @POST(StringHelper.REPORT_USER)
    Call<VideoCommentDelete> reportUser(@Header(StringHelper.AUTHORIZATION) String token, @Body BlockUserPojo pojo);

    @POST(StringHelper.BLOCK_CONTENT)
    Call<VideoCommentDelete> blockVideo(@Header(StringHelper.AUTHORIZATION) String token, @Body BlockVideoPojo pojo);
}
