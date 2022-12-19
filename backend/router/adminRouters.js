const express = require("express");
const router = express.Router();
const controller = require("../controller/adminController");
const { adminAuthJWT } = require("../middleware/jwtMiddleware");

router.post("/login", controller.adminLogin);

router.post("/sign-up", controller.adminSignUp);

router.post("/change-password", adminAuthJWT, controller.changePassword);

router.get("/get-admin-profile", adminAuthJWT, controller.getAdminProfile);

router.post("/edit-admin-profile", adminAuthJWT, controller.editProfile);

router.post("/get-user-list", adminAuthJWT, controller.getUserList);

router.get("/search-user/:query?/:page?", adminAuthJWT, controller.getSearchUsers);//get all users list in dropdown

router.post("/user-block-unblock", controller.userBlockUnblock);

router.post("/donation-setting", adminAuthJWT, controller.donationSetting);

router.post("/add-video-audio", adminAuthJWT, controller.addVideoAudio);

router.post("/get-video-audio-list", adminAuthJWT, controller.getVideoAudioList);

router.get("/get-donation-setting", adminAuthJWT, controller.getDonationSetting);

router.post("/add-genre", adminAuthJWT, controller.addGenre);

router.get("/get-genre", adminAuthJWT, controller.getGenre);

// router.post("/add-setting", controller.addSetting)
router.post("/general-settings-by-type", adminAuthJWT, controller.getGernalSettingByType);

router.get("/get-general-settings", adminAuthJWT, controller.getGernalSetting);

router.post("/edit-general-settings", adminAuthJWT, controller.editGeneralSettings);

router.post("/reported-videos", adminAuthJWT, controller.reportedVideos);

router.post("/verify-unverify-user", adminAuthJWT, controller.verifyUnVerifyUser);

router.post("/get-verified-user", adminAuthJWT, controller.getAllVerifiedUser);

router.get("/dashboard", controller.dashboard);

router.post("/reported-video-detail", adminAuthJWT, controller.ReportedVideoDetail);

router.post("/block-unblock-video", adminAuthJWT, controller.blockUnblockVideo);

router.post("/ignore-reported-video", adminAuthJWT, controller.ignoreReportedVideo);

router.get("/get-graph-data", adminAuthJWT, controller.getGraphData)

router.post("/filter-verified-unverified-users", adminAuthJWT, controller.filterVerifiedUnverified);

// donations
router.post("/transfer-amount-to-bank", adminAuthJWT, controller.transferAmountToBank);

// donation received
router.post("/donation-received", adminAuthJWT, controller.donationReceived);

// Amount withdrawal request
router.post("/withdrawal-request", adminAuthJWT, controller.withdrawalRequest);

// transferred transactions
router.post("/transferred-transactions", adminAuthJWT, controller.transferredTransactions);

// admin commission
router.post("/admin-commission", adminAuthJWT, controller.adminCommission);

// pending transactions
router.post("/pending-transactions", adminAuthJWT, controller.pendingTransactions);

//add category
router.post("/add-category", adminAuthJWT, controller.addCategory)

//edit category
router.post("/edit-category", adminAuthJWT, controller.editCategory)

//delete category
router.post("/delete-category", adminAuthJWT, controller.deleteCategory)

//get categories
router.post("/get-categories", adminAuthJWT, controller.getCategories)

module.exports = router;
