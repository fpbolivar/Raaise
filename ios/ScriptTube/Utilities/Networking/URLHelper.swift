//
//  URLHelper.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 8/3/22.
//

import Foundation

class URLHelper{
    static let BASE_URL: String               =       "http://13.233.101.218:3000/"
    static let SEGMENTCLIENT: String          =       ""
    static let SEGMENTAPIVERSION: String      =       ""

    static let LOGIN_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/login"
    static let SIGNIN_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/register"
    static let GET_PROFILE_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-user-profile"
    static let CHANGE_PASSWORD = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/change-password"
    static let GET_UPDATE_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/update-profile"
    static let FB_LOGIN_URLx = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/facebook-login"
    static let FORGOT_PASSWORD = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/forget-password"
    static let VERIFY_OTP = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/otp-verify"
    static let RESET_PASSWORD = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/reset-password"
    static let GET_SETTINGS_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/general-settings"
    static let DEACTIVATE_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/deactive-account"
    static let GET_AUDIO_LIST_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/audio-list"
    static let DELETE_ACCOUNT_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/delete-user"
    static let LOGOUT_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/user-logout"
    static let NOTIFICATIONS_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/notification-on-off"
    static let UPLOAD_VIDEO_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/upload-video"
    static let GET_USER_PROFILE_VIDEOS_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-user-video"
    static let GET_GLOBAL_VIDEOS_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-global-video"
    static let VIDEO_LIKE_UNLIKE_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/video-like-dislike"
    static let VIDEO_REPORT_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/report-video"
    static let GOOGLE_LOGIN_URLx = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/google-login"
    static let GET_VIDEO_COMMENTS_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-videos-comment"
    static let USER_FOLLOW_UNFOLLOW_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/user-follow-unfollow"
    static let POST_COMMENTS_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/video-comment"
    static let POST_REPLY_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/comment-reply"
    static let GET_USER_FOLLOWER_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/user-followers"
    static let GET_USER_FOLLOWING_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/user-following"
    static let GET_USER_PROFILE_BY_ID_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-user-profile-by-id"
    static let GET_USER_VIDEOS_BY_ID_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-user-videos-by-id"
    static let GET_VIDEOS_BY_AUDIO_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-videos-by-audio-id"
    static let ADD_BANK_DETAILS_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/add-update-bank-details"
    static let GET_CARDS_LIST_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/get-cards"
    static let DELETE_POST_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/delete-video"
    static let EDIT_POST_URL = BASE_URL+SEGMENTCLIENT+SEGMENTAPIVERSION+"user/edit-video-caption"
}

