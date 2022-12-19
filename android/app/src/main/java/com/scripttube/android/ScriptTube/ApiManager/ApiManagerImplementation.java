package com.scripttube.android.ScriptTube.ApiManager;

import android.util.Log;

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
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.ApiUtilities;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.StringHelper;
import com.scripttube.android.ScriptTube.model.LoginPojo;
import com.scripttube.android.ScriptTube.model.MusicData;
import com.scripttube.android.ScriptTube.model.MusicResponse;
import com.scripttube.android.ScriptTube.model.ReportVideoPojo;
import com.scripttube.android.ScriptTube.model.ReportVideoRes;

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
                        if (response.body().getStatus() == 200) {
                            if (response.body().getData() != null) {
                                Callback.onSuccess(response.body());
                            }

                        } else if (response.body().getStatus() == 422) {
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

                        if (response.body().getStatus() == 200 && response.body().getMessage().equalsIgnoreCase("Success")) {
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
//                        if (response.body().getData() != null) {
                            Callback.onSuccess(response.body());
//                        }

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
                        if (response.body().getStatusCode() == 200) {
//                        if (response.body().getData() != null) {
                            Callback.onSuccess(response.body());
//                        }

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
//                        if (response.body().getData() != null) {
                            Callback.onSuccess(response.body());
//                        }

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
//                        if (response.body().getData() != null) {
                            Callback.onSuccess(response.body());
//                        }

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
                            if (response.body().status == 200 && response.body().message.equals("Success")) {
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

//    @Override
//    public void GetAllUserVideo(String token, DataCallback<GetAllUserVideoModel> Callback) {
//        ApiUtilities.getApiInterface().GetAllUserVideo(token).enqueue(new Callback<GetAllUserVideoModel>() {
//            @Override
//            public void onResponse(Call<GetAllUserVideoModel> call, Response<GetAllUserVideoModel> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getStatus() == 200) {
//                        Callback.onSuccess(response.body());
//                    } else {
//                        Callback.onError(new ServerError(response.body().getMessage()));
//                    }
//
//                } else {
//                    Callback.onError(new ServerError(response.message()));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<GetAllUserVideoModel> call, Throwable t) {
//                Callback.onError(new ServerError(t.getMessage()));
//            }
//        });
//    }

    @Override
    public void GetAllUserVideo(String token, DataCallback<GetAllUserVideoModel> Callback) {
        if (HelperClass.haveNetworkConnection()) {
            ApiUtilities.getApiInterface().GetAllUserVideo(token).enqueue(new Callback<GetAllUserVideoModel>() {
                @Override
                public void onResponse(Call<GetAllUserVideoModel> call, Response<GetAllUserVideoModel> response) {
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
                    Log.e("Delete", "onResponse: " + response.code());
                    Log.e("Delete", "onResponse: " + response.message());
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

//                
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
                Log.e("CheckGoogleResponse", "Error: " + t.getMessage());
                Log.e("CheckGoogleResponse", "Error: " + t.getLocalizedMessage());
                Log.e("CheckGoogleResponse", "Error: " + t.getCause());
                Callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetPublicUserProfile(String token, GetPublicUserProfileModel getPublicUserProfile, DataCallback<GetPublicUserProfileModel> Callback) {
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
                Log.e("CheckManinder", "onFailure: " + t.getMessage());
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void GetFollowingList(String Token, UserFollowingModel userFollowingModel, DataCallback<UserFollowingModel> callback) {
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
                Log.e("CheckManinder", "onFailure: " + t.getMessage());
                callback.onError(new ServerError(t.getMessage()));
            }
        });
    }

    @Override
    public void DeleteUserVIDEO(String Token, DeleteVideoModel deleteVideoModel, DataCallback<DeleteVideoModel> Callback) {
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
    public void GetCategories(String Token, DataCallback<GetCategoryModel> Callback){
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
}
