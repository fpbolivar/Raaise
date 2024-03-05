package com.raaise.android.ApiManager;

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
import com.raaise.android.model.PrivacyBody;
import com.raaise.android.model.PrivacyModel;
import com.raaise.android.model.PrivacyUsersRes;
import com.raaise.android.model.PublicRoomPojo;
import com.raaise.android.model.ReportVideoPojo;
import com.raaise.android.model.ReportVideoRes;
import com.raaise.android.model.RoomData;
import com.raaise.android.model.RoomPojo;
import com.raaise.android.model.RoomResponse;
import com.raaise.android.model.RoomSlug;
import com.raaise.android.model.SendChatBody;
import com.raaise.android.model.UpdateRoomPojo;
import com.raaise.android.model.UpdateRoomRes;
import com.raaise.android.model.VerifiedResponse;
import com.raaise.android.model.VideoCommentDelete;
import com.raaise.android.model.VideoDonationModal;
import com.raaise.android.model.VideoDonationPojo;
import com.raaise.android.model.WithdrawalsPojo;

import java.util.ArrayList;
import java.util.List;

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

    void GetPublicUserFollowers(String Token, PublicUserFollowersModel publicUserFollowersModel, DataCallback<PublicUserFollowersModel> Callback);

    void GetPublicUserFollowing(String Token, PublicUserFollowingModel publicUserFollowingModel, DataCallback<PublicUserFollowingModel> Callback);

    void GetVideoBaseOnAudio(String Token, GetVideosBasedOnAudioIdModel getVideosBasedOnAudioIdModel, DataCallback<GetVideosBasedOnAudioIdModel> Callback);

    void GetGlobalSearch(String Token, GlobalSearchModel globalSearchModel, DataCallback<GlobalSearchModel> Callback);

    void AddUpdateBankDetails(String Token, Payment_AddUpdateBankDetailsModel payment_addUpdateBankDetailsModel, DataCallback<Payment_AddUpdateBankDetailsModel> Callback);

    void AddCard(String Token, Payment_AddCardModel payment_addCardModel, DataCallback<Payment_AddCardModel> Callback);

    void GetCards(String Token, DataCallback<Payment_GetCardsModel> Callback);

    void SetDefaultCard(String Token, SetDefaultCardModel setDefaultCardModel, DataCallback<SetDefaultCardModel> Callback);

    void MakePaymentByCardId(String Token, MakePaymentByCardIdModel makePaymentByCardIdModel, DataCallback<MakePaymentByCardIdModel> Callback);

    void DeleteCard(String Token, DeleteCardModel deleteCardModel, DataCallback<DeleteCardModel> Callback);

    void GetUserNotifications(String Token, GetUserNotificationsModel getUserNotificationsModel, DataCallback<GetUserNotificationsModel> Callback);

    void GetSingleVideo(String Token, GetSingleVideoModel getSingleVideoModel, DataCallback<GetSingleVideoModel> Callback);

    void GetUserChatList(String Token, ChatListModel chatListModel, DataCallback<ChatListModel> Callback);

    void GetSingleChat(String Token, ChatModel chatModel, DataCallback<ChatModel> Callback);

    void readSingleNotification(String Token, ReadSingleNotificationModel readSingleNotificationModel, DataCallback<ReadSingleNotificationModel> Callback);

    void UnreadMessageCount(String Token, DataCallback<UnreadMessageCountModel> Callback);

    void UnReadNotificationCount(String Token, DataCallback<UnReadNotificationCountModel> Callback);

    void ShareVideo(String Token, SharVideoModel sharVideoModel, DataCallback<SharVideoModel> Callback);

    void ProfileChat(String Token, ProfileChatModel profileChatModel, DataCallback<ProfileChatModel> Callback);

    void DonationHistory(String Token, DataCallback<UserDonationHistoryModel> Callback);

    void VideoDonationHistory(String Token, UserVideoDonationHistoryModel userVideoDonationHistoryModel, DataCallback<UserVideoDonationHistoryModel> Callback);

    void getVideoDonationHistory(String token, VideoDonationModal videoDonationPojo, DataCallback<VideoDonationPojo> callback);

    void getUSerWithdrawals(String token, DataCallback<WithdrawalsPojo> callback);

    void claimVideoAmount(String token, VideoDonationModal videoDonationModal, DataCallback<ClaimedAmountPojo> callback);

    void deleteVideoComment(String token, DeleteCommentPojo pojo, DataCallback<VideoCommentDelete> callback);

    void editVideoComment(String token, EditVideoCmntPojo pojo, DataCallback<VideoCommentDelete> callback);

    void deleteCommentReply(String token, DeleteCommentReply replyID, DataCallback<VideoCommentDelete> callback);

    void editCommentReply(String token, CommentReplyPojo model, DataCallback<VideoCommentDelete> callback);

    void blockUser(String token, BlockUserPojo model, DataCallback<VideoCommentDelete> callback);

    void reportUser(String token, BlockUserPojo model, DataCallback<VideoCommentDelete> callback);

    void blockVideo(String token, BlockVideoPojo model, DataCallback<VideoCommentDelete> callback);

    void createLiveRoom(String token, RoomPojo model , DataCallback<RoomResponse> callback);

    void getLiveRooms(String token, GetRoomPojo model, DataCallback<LiveRoomResponse> callback);

    void updateLiveRoom(String token, UpdateRoomPojo model, DataCallback<UpdateRoomRes> callback);

    void getRoomBySlug(String token, RoomSlug model, DataCallback<LiveRoomTokenData> callback);

    void getPublicRooms(String token, PublicRoomPojo model, DataCallback<ArrayList<LiveRoomData>> callback);

    void joinLiveRoom(String token, RoomSlug model, DataCallback<JoinRoomRes> callback);

    void leaveLiveRoom(String token, RoomSlug model, DataCallback<JoinRoomRes> callback);

    void sendLiveChat(String token, SendChatBody model);

    void getLiveChat(String token, GetLiveRoomChatBody model, DataCallback<ArrayList<LiveRoomChat.ChatData>> callback);

    void updatePrivacyPolicy(String token, PrivacyModel model, DataCallback<String> callback);

    void getPrivacyUsers(String token, PrivacyBody model, DataCallback<ArrayList<PrivacyUsersRes.PrivacyData>> callback);

    void getCurrentPrivacyControl(String token, DataCallback<CurrentPrivacyResponse> callback);

    void getBanner(String token, DataCallback<BannerModel> callback);
    void submitCategories(String Token, CategoriesPojo ids, DataCallback<CategoriesModel> Callback);

    void applyForVerification(String token,VerifiedResponse verified,DataCallback<VerifiedResponse> Callback);

}
