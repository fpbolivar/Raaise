package com.scripttube.android.ScriptTube.ApiManager;

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
import com.scripttube.android.ScriptTube.model.LoginPojo;
import com.scripttube.android.ScriptTube.model.MusicData;
import com.scripttube.android.ScriptTube.model.ReportVideoPojo;
import com.scripttube.android.ScriptTube.model.ReportVideoRes;

import java.util.ArrayList;

public interface ApiManager {
    void DeleteAccount(String token, DataCallback<DeleteAccountModel> Callback);

    void SignUp(SignUpModel signUpModel, DataCallback<SignUpModel> Callback);

    void Login(LoginPojo loginModel, DataCallback<LoginModel> Callback);

    void ChangePassword(String token, ChangePasswordModel changePasswordModel, DataCallback<ChangePasswordModel> Callback);

    void UpdateProfile(String token, UpdateUserProfileModel updateUserProfileModel, DataCallback<UpdateUserProfileModel> Callback);

    void GetUserProfile(String token, DataCallback<GetUserProfile> Callback);

    void FacebookLogin(FacebookLoginModel facebookLoginModel, DataCallback<FacebookLoginModel> Callback);

    void ForgetPassword(ForgetPasswordModel forgetPasswordModel, DataCallback<ForgetPasswordModel> Callback);

    void VerifyOtp(VerifyOtpModel verifyOtpModel, DataCallback<VerifyOtpModel> Callback);

    void CreateNewPassword(CreateNewPasswordModel createNewPasswordModel, DataCallback<CreateNewPasswordModel> Callback);

    void getMusicList(String token, DataCallback<ArrayList<MusicData>> callback);

    void GetPolicy(String token, GetPolicyModel getPolicy, DataCallback<GetPolicyModel> Callback);

    void NotificationOnOff(String token, Notification_On_OffModel notification_on_offModel, DataCallback<Notification_On_OffModel> Callback);

    void DeactivateAccount(String token, DataCallback<DeactivateAccountModel> Callback);

    void GetAllUserVideo(String token, DataCallback<GetAllUserVideoModel> Callback);

    void UserFollowUnfollow(String token, UserFollowUnfollowModel userFollowUnfollowModel, DataCallback<UserFollowUnfollowModel> Callback);

    void VideoLikeDislike(String token, VideoLikeDislikeModel videoLikeDislikeModel, DataCallback<VideoLikeDislikeModel> Callback);

    void VideoComment(String token, VideoCommentModel videoCommentModel, DataCallback<VideoCommentModel> Callback);

    void GetGlobalVideo(String token, GetGlobalVideoModel getGlobalVideoModel, DataCallback<GetGlobalVideoModel> Callback);

    void VideoCommentReply(String token, VideoCommentsReplyModel videoCommentsReplyModel, DataCallback<VideoCommentsReplyModel> Callback);

    void ListOfVideoCommentsModel(String token, ListOfVideoCommentsModel listOfVideoCommentsModel, DataCallback<ListOfVideoCommentsModel> Callback);

    void LogOut(String token, DataCallback<LogoutModel> Callback);

    void reportVideo(String token, ReportVideoPojo model, DataCallback<ReportVideoRes> callback);

    void GoogleLogin(GoogleLoginModel googleLogin, DataCallback<GoogleLoginModel> Callback);

    void GetPublicUserProfile(String token, GetPublicUserProfileModel getPublicUserProfile, DataCallback<GetPublicUserProfileModel> Callback);

    void GetPublicUserVideoList(String token, PublicUserVideoListModel publicUserVideoListModel, DataCallback<PublicUserVideoListModel> Callback);

    void GetFollowersList(String Token, UserFollowersModel userFollowersModel, DataCallback<UserFollowersModel> callback);

    void GetFollowingList(String Token, UserFollowingModel userFollowingModel, DataCallback<UserFollowingModel> callback);

    void DeleteUserVIDEO(String Token, DeleteVideoModel deleteVideoModel, DataCallback<DeleteVideoModel> Callback);

    void UpdateVideoCaption(String Token, UpdateCaptionModel updateCaptionModel, DataCallback<UpdateCaptionModel> Callback);

    void GetCategories(String Token, DataCallback<GetCategoryModel> Callback);
}
