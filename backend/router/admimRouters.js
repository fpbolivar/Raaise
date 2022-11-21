const express = require('express')
const router = express.Router();
const controller = require('../controller/adminController')
const { adminAuthJWT } = require("../middleware/jwtMiddleWare")


router.post('/login', controller.adminLogin)

router.post("/sign-up", controller.adminSignUp)

router.post("/change-password", adminAuthJWT, controller.changePassword)

router.get("/get-admin-profile", adminAuthJWT, controller.getAdminProfile)

router.post("/edit-admin-profile", adminAuthJWT, controller.editProfile)

router.get("/get-user-list", adminAuthJWT, controller.getUserList)

module.exports = router;