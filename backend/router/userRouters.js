const express = require('express')
const router = express.Router();
const controller = require('../controller/userController')
const { userAuthJWT } = require("../middleware/jwtMiddleWare")


router.post('/login', controller.userLogin)

router.post("/register", controller.userSignUp)

router.post("/change-password", userAuthJWT, controller.changePassword)



module.exports = router;