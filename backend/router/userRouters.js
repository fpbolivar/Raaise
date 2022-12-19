const express = require("express");
const router = express.Router();
const controller = require("../controller/userController");
const paymentController = require("../controller/paymentController");
const notificationController = require("../controller/notificationController")
const { userAuthJWT } = require("../middleware/jwtMiddleware");

router.post("/login", controller.userLogin);

router.post("/register", controller.userSignUp);

router.post("/change-password", userAuthJWT, controller.changePassword);

router.post("/facebook-login", controller.faceBookLogin);

router.post("/google-login", controller.googleLogin);

router.get("/get-user-profile", userAuthJWT, controller.getUserProfile);

router.post("/update-profile", userAuthJWT, controller.updateUserProfile);

router.post("/forget-password", controller.forgetPassword);

router.post("/otp-verify", controller.otpVerify);

router.post("/reset-password", controller.resetPassword);

router.post("/audio-list", userAuthJWT, controller.audioList);

router.post("/general-settings", controller.getGernalSetting);

router.post("/upload-video", userAuthJWT, controller.userVideoUpload)

router.get("/delete-user", userAuthJWT, controller.deleteUser)

router.post("/notification-on-off", userAuthJWT, controller.userNotificationOnOff)

router.post("/deactive-account", userAuthJWT, controller.decativeUserAccount)

router.post("/video-like-dislike", userAuthJWT, controller.videoLikeDislike)

router.post("/user-follow-unfollow", userAuthJWT, controller.userFollowUnfollow)
router.post("/get-video", controller.getVideo)

router.get("/get-user-video", userAuthJWT, controller.getAllUserVideo)

router.post("/get-global-video", userAuthJWT, controller.globalVideo)

router.post("/video-comment", userAuthJWT, controller.videoComment)

router.post("/comment-reply", userAuthJWT, controller.commentReply)

router.post("/get-videos-comment", userAuthJWT, controller.getVideosComment)

router.post("/user-followers", userAuthJWT, controller.getUserFollowers)

router.post("/user-following", userAuthJWT, controller.getUserFollowing)

router.post("/report-video", userAuthJWT, controller.reportVideo)
router.post("/user-logout", userAuthJWT, controller.userlogout)


// get user profile by id
router.post("/get-user-profile-by-id", userAuthJWT, controller.getUserProfileById)

// get user videos by id
router.post("/get-user-videos-by-id", userAuthJWT, controller.getAllUserVideoById)

// video view by user
router.post("/video-view-by-user", userAuthJWT, controller.videoViewByUser)


/**
 * NOTIFICATION ROUTES USERS
 */

router.get("/get-user-notifications", userAuthJWT, notificationController.getNotificationforUser)

// payments routes
router.post("/make-payment", userAuthJWT, paymentController.makePayment)
router.post("/add-update-bank-details", userAuthJWT, paymentController.addUpdateBankDetails)
router.post("/send-request-to-payment", userAuthJWT, paymentController.sendRequestToPayment)
router.get("/get-cards", userAuthJWT, paymentController.getCards)
router.post("/delete-cards", userAuthJWT, paymentController.deleteCards)

//get categories
router.get("/get-categories", userAuthJWT, controller.getCategories)

//get videos based on audio list
router.post("/get-videos-by-audio-id", userAuthJWT, controller.getVideosByAudioId)

//global search
router.post("/global-search", userAuthJWT, controller.globalSearch)
// edit video caption 
router.post("/edit-video-caption", userAuthJWT, controller.editVideoCaption)
// delete video 
router.post("/delete-video", userAuthJWT, controller.deleteVideo)
// get public user follwers list 
router.post("/get-user-follwers-list", userAuthJWT, controller.getUserFollowersList)

// get public user follwing list
router.post("/get-user-follwing-list", userAuthJWT, controller.getUserFollowingList)


module.exports = router;
