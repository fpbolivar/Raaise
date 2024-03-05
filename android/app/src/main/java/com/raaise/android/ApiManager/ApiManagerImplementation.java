package com.raaise.android.ApiManager;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.raaise.android.ApiManager.RetrofitHelper.ApiUtilities;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.StringHelper;
import com.raaise.android.model.BannerModel;
import com.raaise.android.model.BlockUserPojo;
import com.raaise.android.model.BlockVideoPojo;
import com.raaise.android.model.CategoriesModel;
import com.raaise.android.model.CategoriesPojo;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.ChatModel;
import com.raaise.android.model.ClaimedAmountPojo;
import com.raaise.android.model.CommentReplyPojo;
import com.raaise.android.model.CurrentPrivacyResponse;
import com.raaise.android.model.DeleteCommentPojo;
import com.raaise.android.model.DeleteCommentReply;
import com.raaise.android.model.EditVideoCmntPojo;
import com.raaise.android.model.GetLiveRoomChatBody;
import com.raaise.android.model.GetRoomPojo;
import com.raaise.android.model.JoinRoomRes;
import com.raaise.android.model.LiveRoomChat;
import com.raaise.android.model.LiveRoomData;
import com.raaise.android.model.LiveRoomResponse;
import com.raaise.android.model.LiveRoomTokenData;
import com.raaise.android.model.LoginPojo;
import com.raaise.android.model.MusicData;
import com.raaise.android.model.MusicResponse;
import com.raaise.android.model.PrivacyBody;
import com.raaise.android.model.PrivacyModel;
import com.raaise.android.model.PrivacyResponse;
import com.raaise.android.model.PrivacyUsersRes;
import com.raaise.android.model.PublicRoomPojo;
import com.raaise.android.model.ReportVideoPojo;
import com.raaise.android.model.ReportVideoRes;
import com.raaise.android.model.RoomData;
import com.raaise.android.model.RoomPojo;
import com.raaise.android.model.RoomResponse;
import com.raaise.android.model.RoomSlug;
import com.raaise.android.model.SendChatBody;
import com.raaise.android.model.SlugRoomData;
import com.raaise.android.model.UpdateRoomPojo;
import com.raaise.android.model.UpdateRoomRes;
import com.raaise.android.model.VerifiedResponse;
import com.raaise.android.model.VideoCommentDelete;
import com.raaise.android.model.VideoDonationModal;
import com.raaise.android.model.VideoDonationPojo;
import com.raaise.android.model.WithdrawalsPojo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManagerImplementation implements ApiManager {


    @Override
    public void SignUp(SignUpModel signUpModel, DataCallback<SignUpModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().PerformSignUp(signUpModel).enqueue(new Callback<SignUpModel>() {
                @Override
                public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else if (response.body().getStatus() == 422) {
                                Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                            } else {
                                Callback.onError(new ServerError(response.message()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SignUpModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }

    }

    @Override
    public void Login(LoginPojo loginModel, DataCallback<LoginModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().PerformLogin(loginModel).enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    try {
                        Log.i("loginLet", "onResponse: " + new Gson().toJson(response.isSuccessful()));
                        if (response.isSuccessful()){
                            Log.i("loginLet", "onResponse: " + new Gson().toJson(response.body() == null));
                            if (response.body() != null){
                                try {
                                    Log.i("loginLet", "onResponse: " + new Gson().toJson(response.body()));
                                } catch (Exception e){
                                    Log.i("loginLet", "onResponse: " + e.getMessage());
                                }
                            }
                        }
                        if (response.isSuccessful() && response.code() == 200) {
                            if (response.body().getStatus() == 200) {

                                Callback.onSuccess(response.body());
                            } else if (response.body().getStatusCode() == 400) {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            } else if (response.body().getStatus() == 422) {
                                Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));

                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void ChangePassword(String token, ChangePasswordModel changePasswordModel, DataCallback<ChangePasswordModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().ChangePassword(token, changePasswordModel).enqueue(new Callback<ChangePasswordModel>() {
                @Override
                public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                    try {

                        if (response.isSuccessful() && response.code() == 200) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else if (response.body().getStatus() == 422) {
                                Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                            } else {
                                Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordModel> call, Throwable t) {

                    Callback.onError(new ServerError(t.getMessage()));

                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void UpdateProfile(String token, UpdateUserProfileModel updateUserProfileModel, DataCallback<UpdateUserProfileModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().UpdateProfile(token, updateUserProfileModel).enqueue(new Callback<UpdateUserProfileModel>() {
                @Override
                public void onResponse(Call<UpdateUserProfileModel> call, Response<UpdateUserProfileModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 200) {
                            if (response.body().getData() != null) {
                                Callback.onSuccess(response.body());
                            }

                        } else if (response.body().getStatusCode() == 422) {
                            Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                        } else {
                            Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<UpdateUserProfileModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));

                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void GetUserProfile(String token, DataCallback<GetUserProfile> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().GetUserProfile(token).enqueue(new Callback<GetUserProfile>() {
                @Override
                public void onResponse(Call<GetUserProfile> call, Response<GetUserProfile> response) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus() == 200) {
                            if (response.body().getData() != null) {
                                Callback.onSuccess(response.body());
                            } else if (response.body().getData() == null) {
                                Callback.onError(new ServerError("User Data Null"));
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }

                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<GetUserProfile> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void FacebookLogin(FacebookLoginModel facebookLoginModel, DataCallback<FacebookLoginModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().FacebookLogin(facebookLoginModel).enqueue(new Callback<FacebookLoginModel>() {
                @Override
                public void onResponse(Call<FacebookLoginModel> call, Response<FacebookLoginModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {

                            Callback.onSuccess(response.body());


                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<FacebookLoginModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void ForgetPassword(ForgetPasswordModel forgetPasswordModel, DataCallback<ForgetPasswordModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().ForgetPassword(forgetPasswordModel).enqueue(new Callback<ForgetPasswordModel>() {
                @Override
                public void onResponse(Call<ForgetPasswordModel> call, Response<ForgetPasswordModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null){
                            if (response.body().getStatusCode() == 200) {

                                Callback.onSuccess(response.body());


                            } else if (response.body().getStatusCode() == 422) {
                                Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                            } else {
                                Callback.onError(new ServerError(response.body().message));
                            }
                        } else Callback.onError(new ServerError(response.message()));

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<ForgetPasswordModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void VerifyOtp(VerifyOtpModel verifyOtpModel, DataCallback<VerifyOtpModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().VerifyOtp(verifyOtpModel).enqueue(new Callback<VerifyOtpModel>() {
                @Override
                public void onResponse(Call<VerifyOtpModel> call, Response<VerifyOtpModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 200) {

                            Callback.onSuccess(response.body());


                        } else if (response.body().getStatusCode() == 422) {
                            Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                        } else {
                            Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<VerifyOtpModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void CreateNewPassword(CreateNewPasswordModel createNewPasswordModel, DataCallback<CreateNewPasswordModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().CreateNewPassword(createNewPasswordModel).enqueue(new Callback<CreateNewPasswordModel>() {
                @Override
                public void onResponse(Call<CreateNewPasswordModel> call, Response<CreateNewPasswordModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {

                            Callback.onSuccess(response.body());


                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<CreateNewPasswordModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void getMusicList(String token, DataCallback<ArrayList<MusicData>> callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().getMusicList(token).enqueue(new Callback<MusicResponse>() {
                @Override
                public void onResponse(Call<MusicResponse> call, Response<MusicResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().status == 200) {
                                callback.onSuccess(response.body().data);
                            } else callback.onError(new ServerError(response.message()));
                        } else callback.onError(new ServerError(response.message()));
                    } else callback.onError(new ServerError(response.message()));
                }

                @Override
                public void onFailure(Call<MusicResponse> call, Throwable t) {
                    callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    public void GetPolicy(String token, GetPolicyModel getPolicy, DataCallback<GetPolicyModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().GetPolicy(token, getPolicy).enqueue(new Callback<GetPolicyModel>() {
                @Override
                public void onResponse(Call<GetPolicyModel> call, Response<GetPolicyModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Callback.onSuccess(response.body());
                        } else if (response.body().getStatus() == 422) {
                            Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }

                }

                @Override
                public void onFailure(Call<GetPolicyModel> call, Throwable t) {

                    Callback.onError(new ServerError(t.getMessage()));

                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void NotificationOnOff(String token, Notification_On_OffModel notification_on_offModel, DataCallback<Notification_On_OffModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().NotificationOnOff(token, notification_on_offModel).enqueue(new Callback<Notification_On_OffModel>() {
                @Override
                public void onResponse(Call<Notification_On_OffModel> call, Response<Notification_On_OffModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Callback.onSuccess(response.body());
                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<Notification_On_OffModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void DeactivateAccount(String token, DataCallback<DeactivateAccountModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().DeactivateAccount(token).enqueue(new Callback<DeactivateAccountModel>() {
                @Override
                public void onResponse(Call<DeactivateAccountModel> call, Response<DeactivateAccountModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Callback.onSuccess(response.body());
                        } else {
                            Callback.onError(new ServerError(response.body().message));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<DeactivateAccountModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }


    @Override
    public void GetAllUserVideo(String token, DataCallback<GetAllUserVideoModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().GetAllUserVideo(token).enqueue(new Callback<GetAllUserVideoModel>() {
                @Override
                public void onResponse(Call<GetAllUserVideoModel> call, Response<GetAllUserVideoModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Callback.onSuccess(response.body());
                        } else if (response.body().getStatus() == 404) {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }

                }

                @Override
                public void onFailure(Call<GetAllUserVideoModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }


    @Override
    public void UserFollowUnfollow(String token, UserFollowUnfollowModel userFollowUnfollowModel, DataCallback<UserFollowUnfollowModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().UserFollowUnfollow(token, userFollowUnfollowModel).enqueue(new Callback<UserFollowUnfollowModel>() {
                @Override
                public void onResponse(Call<UserFollowUnfollowModel> call, Response<UserFollowUnfollowModel> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }

                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                        }

                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }

                }

                @Override
                public void onFailure(Call<UserFollowUnfollowModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void VideoLikeDislike(String token, VideoLikeDislikeModel videoLikeDislikeModel, DataCallback<VideoLikeDislikeModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().VideoLikeDislike(token, videoLikeDislikeModel).enqueue(new Callback<VideoLikeDislikeModel>() {
                @Override
                public void onResponse(Call<VideoLikeDislikeModel> call, Response<VideoLikeDislikeModel> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<VideoLikeDislikeModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void VideoComment(String token, VideoCommentModel videoCommentModel, DataCallback<VideoCommentModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().VideoComment(token, videoCommentModel).enqueue(new Callback<VideoCommentModel>() {
                @Override
                public void onResponse(Call<VideoCommentModel> call, Response<VideoCommentModel> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<VideoCommentModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void GetGlobalVideo(String token, GetGlobalVideoModel getGlobalVideoModel, DataCallback<GetGlobalVideoModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().GetGlobalVideo(token, getGlobalVideoModel).enqueue(new Callback<GetGlobalVideoModel>() {
                @Override
                public void onResponse(Call<GetGlobalVideoModel> call, Response<GetGlobalVideoModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            if (response.body().getMessage().equalsIgnoreCase("Success")) {
                                if (response.body() != null) {
                                    Callback.onSuccess(response.body());
                                } else {
                                    Callback.onError(new ServerError(response.body().getMessage()));
                                    Log.i("Devil", "onFailure: 1" + response.body().getMessage());
                                }

                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                                Log.i("Devil", "onFailure: 2" + response.body().getMessage());
                            }
                        } else {
                            Callback.onError(new ServerError(response.body().getMessage()));
                            Log.i("Devil", "onFailure: 3" + response.body().getMessage());
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                        Log.i("Devil", "onFailure: 4" + response.message());
                    }
                }

                @Override
                public void onFailure(Call<GetGlobalVideoModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                    Log.i("Mafiaa", "onFailure: 5" + t.getMessage());
                    Log.i("Mafiaa", "onFailure: 5" + t.getCause());
                    Log.i("Mafiaa", "onFailure: 5" + t.getLocalizedMessage());
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void VideoCommentReply(String token, VideoCommentsReplyModel videoCommentsReplyModel, DataCallback<VideoCommentsReplyModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().VideoCommentReply(token, videoCommentsReplyModel).enqueue(new Callback<VideoCommentsReplyModel>() {
                @Override
                public void onResponse(Call<VideoCommentsReplyModel> call, Response<VideoCommentsReplyModel> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<VideoCommentsReplyModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void ListOfVideoCommentsModel(String token, ListOfVideoCommentsModel listOfVideoCommentsModel, DataCallback<ListOfVideoCommentsModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().ListOfVideoComments(token, listOfVideoCommentsModel).enqueue(new Callback<ListOfVideoCommentsModel>() {
                @Override
                public void onResponse(Call<ListOfVideoCommentsModel> call, Response<ListOfVideoCommentsModel> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<ListOfVideoCommentsModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }


    @Override
    public void DeleteAccount(String token, DataCallback<DeleteAccountModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().DeleteAccount(token).enqueue(new Callback<DeleteAccountModel>() {
                @Override
                public void onResponse(Call<DeleteAccountModel> call, Response<DeleteAccountModel> response) {


                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 200) {
                                Callback.onSuccess(response.body());
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<DeleteAccountModel> call, Throwable t) {
                    Log.i("CHECKINgg   ", "onResponse: " + t.getMessage());


                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void LogOut(String token, DataCallback<LogoutModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().LogOut(token).enqueue(new Callback<LogoutModel>() {
                @Override
                public void onResponse(Call<LogoutModel> call, Response<LogoutModel> response) {
                    if (response.isSuccessful()) {

                        if (response.code() == 200) {
                            if (response.body().getStatusCode() == 200) {
                                Callback.onSuccess(response.body());
                            } else {
                                Callback.onError(new ServerError(response.body().getMessage()));
                            }
                        } else {
                            Callback.onError(new ServerError(response.message()));
                        }
                    } else {
                        Callback.onError(new ServerError(response.message()));
                    }
                }


                @Override
                public void onFailure(Call<LogoutModel> call, Throwable t) {
                    Callback.onError(new ServerError(t.getMessage()));
                }
            });
        } else {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
        }
    }

    @Override
    public void reportVideo(String token, ReportVideoPojo model, DataCallback<ReportVideoRes> callback) {
        if (!HelperClass.haveNetworkConnection()) {
            callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().reportVideo(token, model).enqueue(new Callback<ReportVideoRes>() {
            @Override
            public void onResponse(Call<ReportVideoRes> call, Response<ReportVideoRes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().status == 200) {
                        callback.onSuccess(response.body());
                    } else callback.onError(new ServerError(response.body().message));
                } else callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<ReportVideoRes> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GoogleLogin(GoogleLoginModel googleLogin, DataCallback<GoogleLoginModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GoogleLogin(googleLogin).enqueue(new Callback<GoogleLoginModel>() {
            @Override
            public void onResponse(Call<GoogleLoginModel> call, Response<GoogleLoginModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().message));
                } else Callback.onError(new ServerError(response.message()));

            }

            @Override
            public void onFailure(Call<GoogleLoginModel> call, Throwable t) {


                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetPublicUserProfile(String token, GetPublicUserProfileModel getPublicUserProfile, DataCallback<GetPublicUserProfileModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetPublicUserProfile(token, getPublicUserProfile).enqueue(new Callback<GetPublicUserProfileModel>() {
            @Override
            public void onResponse(Call<GetPublicUserProfileModel> call, Response<GetPublicUserProfileModel> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().message));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<GetPublicUserProfileModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetPublicUserVideoList(String token, PublicUserVideoListModel publicUserVideoListModel, DataCallback<PublicUserVideoListModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetPublicUserVideoList(token, publicUserVideoListModel).enqueue(new Callback<PublicUserVideoListModel>() {
            @Override
            public void onResponse(Call<PublicUserVideoListModel> call, Response<PublicUserVideoListModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().status == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().message));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<PublicUserVideoListModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetFollowersList(String Token, UserFollowersModel userFollowersModel, DataCallback<UserFollowersModel> callback) {
        if (!HelperClass.haveNetworkConnection()) {
            callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetUserFollowersList(Token, userFollowersModel).enqueue(new Callback<UserFollowersModel>() {
            @Override
            public void onResponse(Call<UserFollowersModel> call, Response<UserFollowersModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().status == 200) {
                        callback.onSuccess(response.body());
                    } else callback.onError(new ServerError(response.body().getMessage()));
                } else callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<UserFollowersModel> call, Throwable t) {

                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetFollowingList(String Token, UserFollowingModel userFollowingModel, DataCallback<UserFollowingModel> callback) {
        if (!HelperClass.haveNetworkConnection()) {
            callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetUserFollowingList(Token, userFollowingModel).enqueue(new Callback<UserFollowingModel>() {
            @Override
            public void onResponse(Call<UserFollowingModel> call, Response<UserFollowingModel> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().status == 200) {
                        callback.onSuccess(response.body());
                    } else callback.onError(new ServerError(response.body().getMessage()));
                } else callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<UserFollowingModel> call, Throwable t) {

                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void DeleteUserVIDEO(String Token, DeleteVideoModel deleteVideoModel, DataCallback<DeleteVideoModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().DeleteUserVideo(Token, deleteVideoModel).enqueue(new Callback<DeleteVideoModel>() {
            @Override
            public void onResponse(Call<DeleteVideoModel> call, Response<DeleteVideoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<DeleteVideoModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void UpdateVideoCaption(String Token, UpdateCaptionModel updateCaptionModel, DataCallback<UpdateCaptionModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().UpdateVideoCaption(Token, updateCaptionModel).enqueue(new Callback<UpdateCaptionModel>() {
            @Override
            public void onResponse(Call<UpdateCaptionModel> call, Response<UpdateCaptionModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<UpdateCaptionModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetCategories(String Token, DataCallback<GetCategoryModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetCategories(Token).enqueue(new Callback<GetCategoryModel>() {
            @Override
            public void onResponse(Call<GetCategoryModel> call, Response<GetCategoryModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<GetCategoryModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetPublicUserFollowers(String Token, PublicUserFollowersModel publicUserFollowersModel, DataCallback<PublicUserFollowersModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetPublicUserFollowers(Token, publicUserFollowersModel).enqueue(new Callback<PublicUserFollowersModel>() {
            @Override
            public void onResponse(Call<PublicUserFollowersModel> call, Response<PublicUserFollowersModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<PublicUserFollowersModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });

    }

    @Override
    public void GetPublicUserFollowing(String Token, PublicUserFollowingModel publicUserFollowingModel, DataCallback<PublicUserFollowingModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetPublicUserFollowing(Token, publicUserFollowingModel).enqueue(new Callback<PublicUserFollowingModel>() {
            @Override
            public void onResponse(Call<PublicUserFollowingModel> call, Response<PublicUserFollowingModel> response) {


                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else
                        Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<PublicUserFollowingModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });

    }

    @Override
    public void GetVideoBaseOnAudio(String Token, GetVideosBasedOnAudioIdModel getVideosBasedOnAudioIdModel, DataCallback<GetVideosBasedOnAudioIdModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetVideoBaseOnAudio(Token, getVideosBasedOnAudioIdModel).enqueue(new Callback<GetVideosBasedOnAudioIdModel>() {
            @Override
            public void onResponse(Call<GetVideosBasedOnAudioIdModel> call, Response<GetVideosBasedOnAudioIdModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        if (response.body() != null)
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<GetVideosBasedOnAudioIdModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetGlobalSearch(String Token, GlobalSearchModel globalSearchModel, DataCallback<GlobalSearchModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetGlobalSearch(Token, globalSearchModel).enqueue(new Callback<GlobalSearchModel>() {
            @Override
            public void onResponse(Call<GlobalSearchModel> call, Response<GlobalSearchModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<GlobalSearchModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }


    @Override
    public void AddUpdateBankDetails(String Token, Payment_AddUpdateBankDetailsModel payment_addUpdateBankDetailsModel, DataCallback<Payment_AddUpdateBankDetailsModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }

        ApiUtilities.getApiInterface().AddUpdateBankDetails(Token, payment_addUpdateBankDetailsModel).enqueue(new Callback<Payment_AddUpdateBankDetailsModel>() {
            @Override
            public void onResponse(Call<Payment_AddUpdateBankDetailsModel> call, Response<Payment_AddUpdateBankDetailsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));

            }

            @Override
            public void onFailure(Call<Payment_AddUpdateBankDetailsModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));

            }
        });
    }

    @Override
    public void AddCard(String Token, Payment_AddCardModel payment_addCardModel, DataCallback<Payment_AddCardModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().AddCard(Token, payment_addCardModel).enqueue(new Callback<Payment_AddCardModel>() {
            @Override
            public void onResponse(Call<Payment_AddCardModel> call, Response<Payment_AddCardModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<Payment_AddCardModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetCards(String Token, DataCallback<Payment_GetCardsModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetCards(Token).enqueue(new Callback<Payment_GetCardsModel>() {
            @Override
            public void onResponse(Call<Payment_GetCardsModel> call, Response<Payment_GetCardsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<Payment_GetCardsModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void SetDefaultCard(String Token, SetDefaultCardModel setDefaultCardModel, DataCallback<SetDefaultCardModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().SetDefaultCard(Token, setDefaultCardModel).enqueue(new Callback<SetDefaultCardModel>() {
            @Override
            public void onResponse(Call<SetDefaultCardModel> call, Response<SetDefaultCardModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<SetDefaultCardModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void MakePaymentByCardId(String Token, MakePaymentByCardIdModel makePaymentByCardIdModel, DataCallback<MakePaymentByCardIdModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().MakePaymentByCardId(Token, makePaymentByCardIdModel).enqueue(new Callback<MakePaymentByCardIdModel>() {
            @Override
            public void onResponse(Call<MakePaymentByCardIdModel> call, Response<MakePaymentByCardIdModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<MakePaymentByCardIdModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void DeleteCard(String Token, DeleteCardModel deleteCardModel, DataCallback<DeleteCardModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().DeleteCards(Token, deleteCardModel).enqueue(new Callback<DeleteCardModel>() {
            @Override
            public void onResponse(Call<DeleteCardModel> call, Response<DeleteCardModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<DeleteCardModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetUserNotifications(String Token, GetUserNotificationsModel getUserNotificationsModel, DataCallback<GetUserNotificationsModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetUserNotifications(Token, getUserNotificationsModel).enqueue(new Callback<GetUserNotificationsModel>() {
            @Override
            public void onResponse(Call<GetUserNotificationsModel> call, Response<GetUserNotificationsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<GetUserNotificationsModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetSingleVideo(String Token, GetSingleVideoModel getSingleVideoModel, DataCallback<GetSingleVideoModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetSingleVideo(Token, getSingleVideoModel).enqueue(new Callback<GetSingleVideoModel>() {
            @Override
            public void onResponse(Call<GetSingleVideoModel> call, Response<GetSingleVideoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getErrors().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<GetSingleVideoModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetUserChatList(String Token, ChatListModel chatListModel, DataCallback<ChatListModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetChatList(Token, chatListModel).enqueue(new Callback<ChatListModel>() {
            @Override
            public void onResponse(Call<ChatListModel> call, Response<ChatListModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<ChatListModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });

    }

    @Override
    public void GetSingleChat(String Token, ChatModel chatModel, DataCallback<ChatModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().GetSingleChat(Token, chatModel).enqueue(new Callback<ChatModel>() {
            @Override
            public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<ChatModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void readSingleNotification(String Token, ReadSingleNotificationModel readSingleNotificationModel, DataCallback<ReadSingleNotificationModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().ReadSingleNotification(Token, readSingleNotificationModel).enqueue(new Callback<ReadSingleNotificationModel>() {
            @Override
            public void onResponse(Call<ReadSingleNotificationModel> call, Response<ReadSingleNotificationModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<ReadSingleNotificationModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void UnreadMessageCount(String Token, DataCallback<UnreadMessageCountModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().UnreadMessageCount(Token).enqueue(new Callback<UnreadMessageCountModel>() {
            @Override
            public void onResponse(Call<UnreadMessageCountModel> call, Response<UnreadMessageCountModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<UnreadMessageCountModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });

    }

    @Override
    public void UnReadNotificationCount(String Token, DataCallback<UnReadNotificationCountModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().UnreadNotificationCount(Token).enqueue(new Callback<UnReadNotificationCountModel>() {
            @Override
            public void onResponse(Call<UnReadNotificationCountModel> call, Response<UnReadNotificationCountModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<UnReadNotificationCountModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }


    @Override
    public void ShareVideo(String Token, SharVideoModel sharVideoModel, DataCallback<SharVideoModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().SharVideo(Token, sharVideoModel).enqueue(new Callback<SharVideoModel>() {
            @Override
            public void onResponse(Call<SharVideoModel> call, Response<SharVideoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<SharVideoModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void ProfileChat(String Token, ProfileChatModel profileChatModel, DataCallback<ProfileChatModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().ProfileChat(Token, profileChatModel).enqueue(new Callback<ProfileChatModel>() {
            @Override
            public void onResponse(Call<ProfileChatModel> call, Response<ProfileChatModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<ProfileChatModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void DonationHistory(String Token, DataCallback<UserDonationHistoryModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().DonationHistory(Token).enqueue(new Callback<UserDonationHistoryModel>() {
            @Override
            public void onResponse(Call<UserDonationHistoryModel> call, Response<UserDonationHistoryModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<UserDonationHistoryModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void VideoDonationHistory(String Token, UserVideoDonationHistoryModel userVideoDonationHistoryModel, DataCallback<UserVideoDonationHistoryModel> Callback) {
        if (!HelperClass.haveNetworkConnection()) {
            Callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().VideoDonationHistory(Token, userVideoDonationHistoryModel).enqueue(new Callback<UserVideoDonationHistoryModel>() {
            @Override
            public void onResponse(Call<UserVideoDonationHistoryModel> call, Response<UserVideoDonationHistoryModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Callback.onSuccess(response.body());
                    } else if (response.body().getStatus() == 422) {
                        Callback.onError(new ServerError(response.body().getMessage()));
                    } else
                        Callback.onError(new ServerError(response.body().getMessage()));
                } else Callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<UserVideoDonationHistoryModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void getVideoDonationHistory(String token, VideoDonationModal videoDonationModal, DataCallback<VideoDonationPojo> callback) {
        if (!HelperClass.haveNetworkConnection()){
            callback.onError(new ServerError(StringHelper.EnableInternetConnection));
            return;
        }
        ApiUtilities.getApiInterface().getVideoDonationHistory(token, videoDonationModal).enqueue(new Callback<VideoDonationPojo>() {
            @Override
            public void onResponse(Call<VideoDonationPojo> call, Response<VideoDonationPojo> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        if (response.body().getMessage().equalsIgnoreCase("Success")){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError(response.body().getMessage()));
                    } else callback.onError(new ServerError(response.body().getMessage()));
                } else callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<VideoDonationPojo> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void getUSerWithdrawals(String token, DataCallback<WithdrawalsPojo> callback) {
        ApiUtilities.getApiInterface().getUserWithdrawal(token).enqueue(new Callback<WithdrawalsPojo>() {
            @Override
            public void onResponse(Call<WithdrawalsPojo> call, Response<WithdrawalsPojo> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            if (response.body().message.equalsIgnoreCase("Success")){
                                callback.onSuccess(response.body());
                            } else callback.onError(new ServerError(response.message()));
                        } else callback.onError(new ServerError(response.message()));
                    } else callback.onError(new ServerError(response.message()));
                } else callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<WithdrawalsPojo> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void claimVideoAmount(String token, VideoDonationModal videoDonationModal, DataCallback<ClaimedAmountPojo> callback) {
        ApiUtilities.getApiInterface().claimVideoAmount(token, videoDonationModal).enqueue(new Callback<ClaimedAmountPojo>() {
            @Override
            public void onResponse(Call<ClaimedAmountPojo> call, Response<ClaimedAmountPojo> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200 && response.body().getMessage().equalsIgnoreCase("success")){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError(response.message()));
                    } else callback.onError(new ServerError(response.message()));
                } else callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<ClaimedAmountPojo> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void deleteVideoComment(String token, DeleteCommentPojo pojo, DataCallback<VideoCommentDelete> callback) {
        ApiUtilities.getApiInterface().deleteVideoComment(token, pojo).enqueue(new Callback<VideoCommentDelete>() {
            @Override
            public void onResponse(Call<VideoCommentDelete> call, Response<VideoCommentDelete> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200 ){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError("Something went wrong"));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<VideoCommentDelete> call, Throwable t) {
                    callback.onError(new ServerError("Something went wrong"));
            }
        });
    }

    @Override
    public void editVideoComment(String token, EditVideoCmntPojo pojo, DataCallback<VideoCommentDelete> callback) {
        ApiUtilities.getApiInterface().editVideoComment(token, pojo).enqueue(new Callback<VideoCommentDelete>() {
            @Override
            public void onResponse(Call<VideoCommentDelete> call, Response<VideoCommentDelete> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError("Something went wrong"));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<VideoCommentDelete> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void deleteCommentReply(String token, DeleteCommentReply model, DataCallback<VideoCommentDelete> callback) {
        ApiUtilities.getApiInterface().deleteCommentReply(token, model).enqueue(new Callback<VideoCommentDelete>() {
            @Override
            public void onResponse(Call<VideoCommentDelete> call, Response<VideoCommentDelete> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError(response.message()));
                    } else callback.onError(new ServerError(response.message()));
                } else callback.onError(new ServerError(response.message()));
            }

            @Override
            public void onFailure(Call<VideoCommentDelete> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void editCommentReply(String token, CommentReplyPojo model, DataCallback<VideoCommentDelete> callback) {
        ApiUtilities.getApiInterface().editCommentReply(token, model).enqueue(new Callback<VideoCommentDelete>() {
            @Override
            public void onResponse(Call<VideoCommentDelete> call, Response<VideoCommentDelete> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError("Something went wrong"));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<VideoCommentDelete> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void blockUser(String token, BlockUserPojo model, DataCallback<VideoCommentDelete> callback) {
        ApiUtilities.getApiInterface().blockUser(token, model).enqueue(new Callback<VideoCommentDelete>() {
            @Override
            public void onResponse(Call<VideoCommentDelete> call, Response<VideoCommentDelete> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError("Something went wrong"));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<VideoCommentDelete> call, Throwable t) {
                    callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void reportUser(String token, BlockUserPojo model, DataCallback<VideoCommentDelete> callback) {
        ApiUtilities.getUserApiInterface().reportUser(token, model).enqueue(new Callback<VideoCommentDelete>() {
            @Override
            public void onResponse(Call<VideoCommentDelete> call, Response<VideoCommentDelete> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError("Something went wrong"));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<VideoCommentDelete> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void blockVideo(String token, BlockVideoPojo model, DataCallback<VideoCommentDelete> callback) {
        ApiUtilities.getUserApiInterface().blockVideo(token, model).enqueue(new Callback<VideoCommentDelete>() {
            @Override
            public void onResponse(Call<VideoCommentDelete> call, Response<VideoCommentDelete> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200 || response.body().status == 422){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError("Something went wrong"));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<VideoCommentDelete> call, Throwable t) {
                    callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void createLiveRoom(String token, RoomPojo model, DataCallback<RoomResponse> callback) {
        Log.i("creatingRoom", "createLiveRoom: Model " + model.title);
        Log.i("creatingRoom", "createLiveRoom: Model " + model.description);
        Log.i("creatingRoom", "createLiveRoom: Model " + model.logo);
        Log.i("creatingRoom", "createLiveRoom: Model " + model.memberIds);
        Log.i("creatingRoom", "createLiveRoom: Model " + model.roomType);

        ApiUtilities.getApiInterface().createLiveRoom(token, model.getTitle(), model.getDescription(), model.getLogo(), model.getMemberIds(), model.getRoomType(), model.getScheduleType(), model.getScheduleDateTime()).enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else if (response.body().status == 422){
                            callback.onError(new ServerError(response.body().message));
                        }else {
                            Log.i("creatingRoom", "onFailure: Error not 200 " + response.body().message);
                            callback.onError(new ServerError(response.body().message));
                        }
                    } else {
                        callback.onError(new ServerError("Something went wrong"));
                        Log.i("creatingRoom", "onFailure: Error Body null " + response.message());
                    }
                } else {
                    Log.i("creatingRoom", "onFailure: not success " + response.message());
                    callback.onError(new ServerError("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
                Log.i("creatingRoom", "onFailure: Error throw " +t.getMessage());
            }
        });
    }

    @Override
    public void getLiveRooms(String token, GetRoomPojo model, DataCallback<LiveRoomResponse> callback) {
        ApiUtilities.getApiInterface().getLiveRooms(token, model).enqueue(new Callback<LiveRoomResponse>() {
            @Override
            public void onResponse(Call<LiveRoomResponse> call, Response<LiveRoomResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError(response.body().message));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<LiveRoomResponse> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void updateLiveRoom(String token, UpdateRoomPojo model, DataCallback<UpdateRoomRes> callback) {
        ApiUtilities.getApiInterface().updateLiveRoom(token, model.title, model.description, model.logo, model.slug).enqueue(new Callback<UpdateRoomRes>() {
            @Override
            public void onResponse(Call<UpdateRoomRes> call, Response<UpdateRoomRes> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } callback.onError(new ServerError(response.body().message));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<UpdateRoomRes> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void getRoomBySlug(String token, RoomSlug model, DataCallback<LiveRoomTokenData> callback) {
        ApiUtilities.getApiInterface().getRoomBySlug(token, model).enqueue(new Callback<SlugRoomData>() {
            @Override
            public void onResponse(Call<SlugRoomData> call, Response<SlugRoomData> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body().data);
                        } else callback.onError(new ServerError("Something went wrong"));
                    } callback.onError(new ServerError("Something went wrong"));
                } callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<SlugRoomData> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void getPublicRooms(String token, PublicRoomPojo model, DataCallback<ArrayList<LiveRoomData>> callback) {
        ApiUtilities.getApiInterface().getPublicRooms(token, model).enqueue(new Callback<LiveRoomResponse>() {
            @Override
            public void onResponse(Call<LiveRoomResponse> call, Response<LiveRoomResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body().data);
                        } callback.onError(new ServerError(response.body().message));
                    } else callback.onError(new ServerError("Try again later"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<LiveRoomResponse> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void joinLiveRoom(String token, RoomSlug model, DataCallback<JoinRoomRes> callback) {
        ApiUtilities.getApiInterface().joinPublicRoom(token, model).enqueue(new Callback<JoinRoomRes>() {
            @Override
            public void onResponse(Call<JoinRoomRes> call, Response<JoinRoomRes> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError(response.body().message));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<JoinRoomRes> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void leaveLiveRoom(String token, RoomSlug model, DataCallback<JoinRoomRes> callback) {
        ApiUtilities.getApiInterface().leavePublicRoom(token, model).enqueue(new Callback<JoinRoomRes>() {
            @Override
            public void onResponse(Call<JoinRoomRes> call, Response<JoinRoomRes> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        } else callback.onError(new ServerError(response.body().message));
                    } else callback.onError(new ServerError("Something went wrong"));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<JoinRoomRes> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void sendLiveChat(String token, SendChatBody model) {
        ApiUtilities.getApiInterface().sendLiveChat(token, model).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void getLiveChat(String token, GetLiveRoomChatBody model, DataCallback<ArrayList<LiveRoomChat.ChatData>> callback) {
        ApiUtilities.getApiInterface().getLiveRoomChat(token, model).enqueue(new Callback<LiveRoomChat>() {
            @Override
            public void onResponse(Call<LiveRoomChat> call, Response<LiveRoomChat> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200 && response.body().getData().size() > 1){
                            callback.onSuccess(response.body().data);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveRoomChat> call, Throwable t) {

            }
        });
    }

    @Override
    public void updatePrivacyPolicy(String token, PrivacyModel model, DataCallback<String> callback) {
        ApiUtilities.getApiInterface().updatePrivacyPolicy(token, model).enqueue(new Callback<PrivacyResponse>() {
            @Override
            public void onResponse(Call<PrivacyResponse> call, Response<PrivacyResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body().message);
                        } else callback.onError(new ServerError(response.body().message));
                    } else callback.onError(new ServerError(response.body().message));
                } else callback.onError(new ServerError(String.valueOf(response.code())));
            }

            @Override
            public void onFailure(Call<PrivacyResponse> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void getPrivacyUsers(String token, PrivacyBody model, DataCallback<ArrayList<PrivacyUsersRes.PrivacyData>> callback) {
        ApiUtilities.getApiInterface().getPrivacyUsers(token, model).enqueue(new Callback<PrivacyUsersRes>() {
            @Override
            public void onResponse(Call<PrivacyUsersRes> call, Response<PrivacyUsersRes> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body().getData());
                        } else callback.onError(new ServerError(response.body().message));
                    } else callback.onError(new ServerError(response.message()));
                } else callback.onError(new ServerError("Something went wrong"));
            }

            @Override
            public void onFailure(Call<PrivacyUsersRes> call, Throwable t) {
                    callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void getCurrentPrivacyControl(String token, DataCallback<CurrentPrivacyResponse> callback) {
        ApiUtilities.getApiInterface().getCurrentPrivacyControl(token).enqueue(new Callback<CurrentPrivacyResponse>() {
            @Override
            public void onResponse(Call<CurrentPrivacyResponse> call, Response<CurrentPrivacyResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CurrentPrivacyResponse> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void getBanner(String token, DataCallback<BannerModel> callback) {
        ApiUtilities.getApiInterface().getBanner(token).enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            callback.onSuccess(response.body());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void submitCategories(String Token, CategoriesPojo ids, DataCallback<CategoriesModel> Callback) {
        ApiUtilities.getApiInterface().submitCategories(Token,ids).enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            Callback.onSuccess(response.body());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CategoriesModel> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void applyForVerification(String token, VerifiedResponse verified, DataCallback<VerifiedResponse> Callback) {
        ApiUtilities.getApiInterface().applyForVerification(token,verified).enqueue(new Callback<VerifiedResponse>() {
            @Override
            public void onResponse(Call<VerifiedResponse> call, Response<VerifiedResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().status == 200){
                            Callback.onSuccess(response.body());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<VerifiedResponse> call, Throwable t) {
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }
}
