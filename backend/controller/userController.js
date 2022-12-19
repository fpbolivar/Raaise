const bcrypt = require("bcrypt");
var jwt = require("jsonwebtoken");
const saltRounds = 10;
const services = require("../services/services");
const { validatorErrorResponse, sendError, faceBookLoginApi, generateUserName, googleLoginApi, successResponse, generateToken, uploadFileToAws, generateUniqueId } = require("../utils/universalFunction");
const { notificationHelper } = require("../utils/notificationsHelper")
const { USER_JWT_CODE } = require("../config/Config");

const userLogin = async (req, res) => {
    try {
        const { email, password } = req.body;
        const modelName = "user";
        if (!email) {
            return await validatorErrorResponse(res, "Required", "email");
        }
        if (!password) {
            return await validatorErrorResponse(res, "Required", "password");
        }
        let criteria = { $or: [{ email: email }, { userName: email }] }
        let user = await services.getoneData(modelName, criteria);


        if (!user) {
            return await validatorErrorResponse(res, "Invaild email or username", "email");
        }
        // if user delete
        if (user.isDeleted === true) {
            res.send({
                statusCode: 400,
                message: "This account is deleted.",
            });
            return;
        }
        // if admin block user
        if (user.isBlock == true) {
            res.send({
                statusCode: 400,
                message: "Your account is blocked by admin.",
            });
            return;
        }
        let match = await bcrypt.compare(password, user.password);
        if (match) {
            // active user account if deavtive 
            if (user.isActive == false) {
                await services.updateData(modelName, { email: email }, { isActive: true })
            }

            // update  app token 
            if (req.body.deviceToken) {
                await services.updateData(modelName, { email: email }, { deviceToken: req.body.deviceToken }, { new: true })
            }

            const token = jwt.sign(
                { userId: user._id, email: user.email, userName: user.userName },
                USER_JWT_CODE,
                { expiresIn: "7d" }
            );
            return res
                .status(200)
                .send({ status: 200, message: "User login successfully", token: token });
        } else {
            return await validatorErrorResponse(
                res,
                "Incorrect password",
                "password"
            );
        }
    } catch (error) {
        if (error) {
            sendError(error, res);
        }
    }
};

const userSignUp = async (req, res) => {
    try {
        const { name, userName, email, password, phoneNumber, deviceType } = req.body;
        const modelName = "user";
        if (!userName) {
            return await validatorErrorResponse(res, "Required", "userName");
        }
        if (!email) {
            return await validatorErrorResponse(res, "Required", "email");
        }
        if (!password) {
            return await validatorErrorResponse(res, "Required", "password");
        }
        if (password.length < 8) {
            return await validatorErrorResponse(
                res,
                "Password should be 8 digits",
                "password"
            );
        }
        if (!phoneNumber) {
            return await validatorErrorResponse(
                res,
                "Required",
                "phoneNumber"
            );
        }

        const user = await services.getoneData(modelName, { email: email });

        if (user) {
            return await validatorErrorResponse(res, "Email already exists", "email");
        }
        if (!deviceType) {
            return await validatorErrorResponse(res, "Required", "deviceType");
        }

        const checkUserName = await services.getoneData(modelName, {
            userName: userName,
        });

        if (checkUserName) {
            return await validatorErrorResponse(
                res,
                "Username already exists",
                "UserName"
            );
        }

        const hashPassword = await bcrypt.hash(password, saltRounds);

        const userdata = await services.InsertData(modelName, {
            userName: userName,
            name: name,
            email: email,
            password: hashPassword,
            phoneNumber: phoneNumber,
            deviceType: deviceType,
            name: req.body.name ? req.body.name : "",
            deviceToken: req.body.deviceToken ? req.body.deviceToken : ""
        });

        const token = jwt.sign(
            { userId: userdata._id, email: email, userName: userdata.userName },
            USER_JWT_CODE,
            { expiresIn: "7d" }
        );

        return res.status(200).send({
            status: 200,
            message: "User registered successfully",
            name: userdata.name,
            userName: userdata.userName,
            email: userdata.email,
            token: token,
        });
    } catch (error) {
        if (error) {
            sendError(error, res);
        }
    }
};

const changePassword = async (req, res) => {
    try {
        const { userId } = req.user;
        const { oldpassword, newpassword, confirmpassword } = req.body;
        const modelName = "user";

        if (!oldpassword) {
            return await validatorErrorResponse(
                res,
                "Required",
                "oldpassword"
            );
        }
        if (!newpassword) {
            return await validatorErrorResponse(
                res,
                "Required",
                "newpassword"
            );
        }
        if (!confirmpassword) {
            return await validatorErrorResponse(
                res,
                "Required",
                "confirmpassword"
            );
        }

        const user = await services.getoneData(modelName, { _id: userId });

        if (!user) {
            return await validatorErrorResponse(res, "User does not exists", "newPassword");
        }

        var match = await bcrypt.compare(oldpassword, user.password);

        if (!match) {
            return await validatorErrorResponse(
                res,
                "Old password is incorrect",
                "oldpassword"
            );
        }
        if (newpassword.length < 8) {
            return await validatorErrorResponse(
                res,
                "New password should be 8 digits",
                "password"
            );
        }
        if (newpassword != confirmpassword) {
            return await validatorErrorResponse(
                res,
                "New password and confirm password must be same",
                "newpassword or oldpassword"
            );
        }

        const hashPassword = await bcrypt.hash(newpassword, saltRounds);
        let userdata = await services.updateData(
            modelName,
            {
                _id: userId,
            },
            {
                password: hashPassword,
            }
        );
        return res.send({ status: 200, message: "Password changed successfully" });
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const faceBookLogin = async (req, res) => {
    try {
        const { token, deviceType } = req.body;
        const modelName = "user";

        if (!token) {
            return await validatorErrorResponse(res, "Required", "token");
        }
        if (!deviceType) {
            return await validatorErrorResponse(res, "Required", "deviceType");
        }

        let userFaceBookDetails = await faceBookLoginApi(res, token);
        // check user login or Register

        let login = await services.getoneData(modelName, { userFaceBookId: userFaceBookDetails.id });

        // login code
        if (login) {
            const token = jwt.sign({ userId: login._id, userName: login.userName }, USER_JWT_CODE, { expiresIn: "7d" });
            return res.status(200).send({
                status: 200,
                message: "User registered succesdfully",
                userName: login.userName,
                token: token,
            });
        }

        // first time login
        else {
            let UserName = await generateUserName();
            const userdata = await services.InsertData(modelName, {
                name: userFaceBookDetails.name,
                deviceType: deviceType,
                loginType: "facebook",
                userFaceBookId: userFaceBookDetails.id,
                userName: UserName,
            });

            const token = jwt.sign({ userId: userdata._id, userName: userdata.userName, }, USER_JWT_CODE, { expiresIn: "7d" });
            return res.status(200).send({ status: 200, message: "User registered successfully", userName: UserName, token: token, });
        }
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const googleLogin = async (req, res) => {
    try {
        const { token, deviceType } = req.body;
        const modelName = "user";
        // validations
        if (!token) {
            return await validatorErrorResponse(res, "Requried ", "token");
        }
        if (!deviceType) {
            return await validatorErrorResponse(res, "Requried", "deviceType");
        }

        let userGoogleDetails = await googleLoginApi(res, token, deviceType);
        // check user login or Register
        let login = await services.getoneData(modelName, { userGoogleId: userGoogleDetails.sub })

        // login code
        if (login) {
            const token = jwt.sign({ userId: login._id, email: login.email, userName: login.userName }, USER_JWT_CODE, { expiresIn: "7d" })
            return res.status(200).send({
                status: 200,
                message: "User login successfully",
                token: token,
            });

        }
        /**
         * check if email id is already exist then update google id in that record
         */
        let registeredUsers = await services.getoneData(modelName, { email: userGoogleDetails.email }, {}, {})
        if (registeredUsers) {
            const updatedUserDetails = await services.updateData(modelName, { email: registeredUsers.email }, { userGoogleId: userGoogleDetails.sub }, { new: true })
            /**
             * Gernrate token 
             */
            const token = jwt.sign({ userId: updatedUserDetails._id, email: updatedUserDetails.email, userName: updatedUserDetails.userName }, USER_JWT_CODE, { expiresIn: "7d" })
            return res.status(200).send({
                status: 200,
                message: "User details updated successfully",
                token: token,
            });
        }
        // first time login
        else {
            let UserName = await generateUserName() //genrate user name for new user
            const userdata = await services.InsertData(modelName, {
                email: userGoogleDetails.email,
                name: userGoogleDetails?.name,
                deviceType: deviceType,
                loginType: "google",
                userGoogleDetails: userGoogleDetails.sub,
                userName: UserName
            });
            /**Gernrate token */
            const token = jwt.sign({ userId: userdata._id, email: userdata.email, userName: userdata.userName }, USER_JWT_CODE, { expiresIn: "7d" })
            return res.status(200).send({
                status: 200,
                message: "User registered successfully",
                userName: userdata.userName,
                email: userdata.email,
                token: token,
            });

        }
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const getUserProfile = async (req, res) => {
    try {
        const { userId } = req.user;
        const modelName = "user";
        const user = await services.getoneData(modelName, { _id: userId }, { password: false });
        if (user.isDeleted === true) {
            res.send({
                statusCode: 400,
                message: "Account is deleted.",
            });
            return;
        }
        successResponse(res, user);
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const updateUserProfile = async (req, res) => {
    try {
        const { userId } = req.user;
        const { oldpassword, newpassword, confirmpassword } = req.body;
        const modelName = "user";

        let userProfileUpdate = {};

        if (req.body.name) {
            userProfileUpdate.name = req.body.name;
        }
        if (req.body.userName) {
            // check userName
            const userName = await services.getoneData(modelName, {
                userName: req.body.userName,
            });
            if (userName) {
                return await validatorErrorResponse(
                    res,
                    "This Username already exists",
                    "UserName"
                );
            } else {
                userProfileUpdate.userName = req.body.userName;
            }
        }
        if (req.body.shortBio) {
            userProfileUpdate.shortBio = req.body.shortBio;
        }
        if (req.body.phoneNumber) {
            userProfileUpdate.phoneNumber = req.body.phoneNumber;
        }

        // upadte user profile  image

        if (req.files && req.files.image) {
            const file = req.files.image
            const uploadImage = await uploadFileToAws(file);
            userProfileUpdate.profileImage = uploadImage.fileUrl
        }


        const user = await services.updateData(
            modelName,
            { _id: userId },
            userProfileUpdate,
            { new: true }
        );

        successResponse(res, user);
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const otpVerify = async (req, res) => {
    try {
        const { email, otp } = req.body;
        const modelName = "user";

        if (!email) {
            return await validatorErrorResponse(res, "Requried ", "email");
        }
        if (!otp) {
            return await validatorErrorResponse(res, "Requried", "otp");
        }

        let criteria = {
            email: email,
            otp: otp,
        };
        const user = await services.getoneData(modelName, criteria);
        if (!user) {
            return await validatorErrorResponse(res, "Invaild email", "otp");
        }

        // genrate token for reset password

        let token = await generateToken(16);

        const createTokenForResetPassword = await services.InsertData(
            "userTokenModel",
            {
                userId: user._id,
                email: user.email,
                token: token,
            }
        );

        res.send({
            statusCode: 200,
            message: "Success",
            email: user.email,
            token: createTokenForResetPassword.token,
        });
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const forgetPassword = async (req, res) => {
    try {
        const { email } = req.body;
        const modelName = "user";
        if (!email) {
            return await validatorErrorResponse(res, "Requried", "email");
        }

        let criteria = {
            email: email,
        };
        const user = await services.getoneData(modelName, criteria);

        if (!user) {
            return await validatorErrorResponse(
                res,
                "User with given email doesn't exists",
                "email"
            );
        }

        if (user.isDeleted === true) {
            return await validatorErrorResponse(
                res,
                "This account is deleted",
                "email"
            );
        }

        if (user.isBlock === true) {
            return await validatorErrorResponse(
                res,
                "This account is blocked by admin",
                "email"
            );
        }

        if (user.loginType == "register") {
            const otp = Math.floor(1000 + Math.random() * 9000);

            let setOtp = await services.updateData(
                modelName,
                { email: email },
                { otp: otp },
                { new: true }
            );

            res.send({
                statusCode: 200,
                message: `OTP has been sent to ${email} Email .Please enter the OTP `,
                email: setOtp.email,
                otp: setOtp.otp,
            });
        } else {
            return await validatorErrorResponse(
                res,
                "User with given email doesn't exists",
                "email"
            );
        }
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const resetPassword = async (req, res) => {
    try {
        const { newpassword, token } = req.body;
        const modelName = "user";
        const userTokenModelname = "userTokenModel";

        if (!newpassword) {
            return await validatorErrorResponse(
                res,
                "Requried",
                "newpassword"
            );
        }
        if (!token) {
            return await validatorErrorResponse(res, "Requried", "token");
        }

        const user = await services.getoneData(userTokenModelname, {
            token: token,
        });

        if (!user) {
            return await validatorErrorResponse(res, "Invaild token", "token");
        }

        const hashPassword = await bcrypt.hash(newpassword, saltRounds);
        let resetUserpassword = await services.updateData(
            modelName,
            {
                _id: user.userId,
            },
            {
                password: hashPassword,
                otp: "",
            }
        );

        // delete token
        await services.dropIndex(userTokenModelname, { token: token });

        return res.send({ status: 200, message: "Password changed successfully" });
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};
const audioList = async (req, res) => {
    try {
        const modelName = "videoAudio";
        let populateQuery = [
            {
                path: "genreId",
                select: "name",
            },
        ];

        const audioList = await services.findWithPopulate(
            modelName,
            {},
            {},
            {},
            populateQuery
        );

        successResponse(res, audioList);
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const userVideoUpload = async (req, res) => {
    try {
        const { userId } = req.user
        const { videoCaption, audioId, isDonation, donationAmount, categoryId } = req.body;
        let modelName = "userVideo"

        // if (!req.files.video) {
        //     return await validatorErrorResponse(res, "video required", "video")
        // }
        if (!videoCaption) {
            return await validatorErrorResponse(res, "Requried", "videoCaption")
        }

        // generate video slug 
        let slug = await generateUniqueId("userVideo")
        let userVideo = {
            videoCaption: videoCaption,
            isDonation: isDonation,
            slug: slug,
            userId: userId
        }

        if (isDonation == "true") {
            if (!donationAmount) {
                return await validatorErrorResponse(res, "Requried", "donationAmount")
            }
            userVideo.donationAmount = donationAmount
        }
        if (!categoryId) {
            return await validatorErrorResponse(res, "Requried", "categoryId")
        }
        if (audioId) {
            userVideo.audioId = audioId
        }
        userVideo.categoryId = categoryId
        if (req.files && req.files.video) {
            const file = req.files.video
            const path = "video/";
            const uploadImage = await uploadFileToAws(file, path);
            userVideo.videoLink = uploadImage.fileUrl
        }
        if (req.files && req.files.image) {
            console.log("req.files.image", req.files.image)
            const file = req.files.image;
            const uploadImage = await uploadFileToAws(file);
            userVideo.videoImage = uploadImage.fileUrl
        }

        let updateUserVideo = await services.InsertData(modelName, userVideo)
        await services.updateData("user", { _id: userId }, { $inc: { videoCount: 1 } }, {}, { new: true })

        successResponse(res, updateUserVideo)
        return

    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

const getGernalSetting = async (req, res) => {
    try {
        const modelName = "generalSettings";
        const { type } = req.body;

        if (!type) {
            return await validatorErrorResponse(res, "Requried", "type")
        }

        const gernalSetting = await services.getoneData(modelName, { type: type });

        if (gernalSetting) {
            successResponse(res, gernalSetting);
            return;
        }
    } catch (error) {
        if (error) {
            console.log(error)
            sendError(error, res);
        }
    }
};

const userNotificationOnOff = async (req, res) => {
    try {
        const { userId } = req.user
        const modelName = "user"
        const { emailNotification, pushNotification } = req.body;
        let notification = {}
        if (emailNotification === true) {
            notification.emailNotification = true
        }
        else if (emailNotification === false) {
            notification.emailNotification = false
        }
        if (pushNotification === true) {
            notification.pushNotification = true
        }
        else if (pushNotification === false) {
            notification.pushNotification = false
        }
        const upadteNotification = await services.updateData(modelName, { _id: userId }, notification, { new: true })
        return res
            .status(200)
            .send({ status: 200, message: "success", emailNotification: upadteNotification.emailNotification, pushNotification: upadteNotification.pushNotification });

    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}


const decativeUserAccount = async (req, res) => {
    try {
        const { userId } = req.user
        const modelName = "user"
        await services.updateData(modelName, { _id: userId }, { isActive: false })
        return res
            .status(200)
            .send({ status: 200, message: "User account is deactivated" });

    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}



const deleteUser = async (req, res) => {
    try {
        const { userId } = req.user;
        const modelName = "user";
        const user = await services.updateData(modelName, { _id: userId }, { isDeleted: true })
        await services.updateMany("userVideo", { userId: userId }, { isDeleted: true })
        return res.status(200).send({
            status: 200,
            message: "Account deleted",
        });
    } catch (error) {
        console.log(error);
        sendError(error, res);
    }
};

const videoLikeDislike = async (req, res) => {
    try {
        const videoLikesModel = "videoLikesModel";//define video like modal name 
        const userVideoModel = "userVideo";//define user video modal name 
        const { userId, userName } = req.user; //destructure login user value
        let { slug } = req.body;
        let dataObj = {}
        let isLike = false;
        let videoCount;
        if (userId) {
            dataObj.userId = userId; // check if user id get than insert into data object
        }
        //validation for video slug
        if (!slug) {
            return validatorErrorResponse(res, "Required", "slug");
        }
        //get single video data from its slug
        const video = await services.getoneData(userVideoModel, { slug: slug });
        if (!video) {
            //if slug not found send response
            return res.status(200).send({
                status: 404,
                message: "Video does not exits",
            });
        }
        //if video get than insert its data to dataObj
        dataObj.videoId = video._id;
        dataObj.videoOwnerId = video.userId;
        //get video data userid and video owner id 
        const isAlreadyLike = await services.getoneData(videoLikesModel, dataObj);
        if (!isAlreadyLike) {
            //if video like
            await services.InsertData(videoLikesModel, dataObj);//insert data of like 
            //increment video count
            videoCount = 1
            isLike = true;
        }
        else {
            //if video dislike
            await services.dropIndex(videoLikesModel, { _id: (isAlreadyLike._id) ? isAlreadyLike._id : '' });//delete data of like 
            //decrement video count
            videoCount = -1
            isLike = false;
        }
        const updateVideoLikeCount = await services.updateData(userVideoModel, { _id: video._id }, { $inc: { videolikeCount: videoCount } }, { new: true });
        //Create object like notification
        let notificationData = { to: dataObj.videoOwnerId, fromUserName: userName, from: userId, token: "", videoId: dataObj.videoId };
        notificationHelper("like", notificationData)// call notification helper
        return res.status(200).send({
            status: 200,
            message: isLike ? "You have liked the video" : "You have disliked the video",
            videoCount: updateVideoLikeCount.videolikeCount ? updateVideoLikeCount.videolikeCount : 0,
            isLike: isLike,
        });
    } catch (error) {
        console.log("error", error)
        if (error) {
            sendError(error, res);
        }
    }
};

const userFollowUnfollow = async (req, res) => {
    try {
        const modelName = "userFollowersModel";
        const { userId } = req.user;
        let { followerTo } = req.body;
        let dataObj = {}
        let isFollowed = false;
        let followersCount = ""
        let followingCount = ""
        if (userId) {
            dataObj.followedBy = userId
        }
        if (!followerTo) {
            return await validatorErrorResponse(res, "Required", "followerTo");
        }
        if (followerTo && (followerTo === userId)) {
            return await validatorErrorResponse(res, "You can't follow yourself", "followerTo");
        }
        dataObj.followTo = followerTo
        const isAlreadyFollow = await services.getoneData(modelName, dataObj);
        if (!isAlreadyFollow) {
            const create = await services.InsertData(modelName, dataObj);
            followersCount = await services.updateData("user", { _id: followerTo }, { $inc: { followersCount: 1 } }, { new: true })
            followingCount = await services.updateData("user", { _id: userId }, { $inc: { followingCount: 1 } }, { new: true })

            isFollowed = true;
        } else {
            const deleteRecord = await services.dropIndex(
                modelName,
                { _id: (isAlreadyFollow._id) ? isAlreadyFollow._id : '' },
            );
            followersCount = await services.updateData("user", { _id: followerTo }, { $inc: { followersCount: -1 } }, { new: true })
            followingCount = await services.updateData("user", { _id: userId }, { $inc: { followingCount: -1 } }, { new: true })
            isFollowed = false;
        }
        return res.status(200).send({
            status: 200,
            message: isFollowed ? "You have followed the user" : "You have unfollowed the user",
            isFollowed: isFollowed,
            followersCount: followersCount.followersCount ? followersCount.followersCount : 0,
            followingCount: followingCount.followingCount ? followingCount.followingCount : 0

        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};
const getVideo = async (req, res) => {
    try {
        // const { userId } = req.user
        const { slug } = req.body
        const modelName = "user"
        const videoModelName = "userVideo"

        if (!slug) {
            return validatorErrorResponse(res, "Requried", "slug")
        }
        let criteria = {
            slug: slug,
            isDeleted: false
        }
        let populateQuery = [
            {
                path: "userId",
                select: ['userName', 'name', 'email', 'phoneNumber', 'deviceType', 'loginType', 'profileImage', 'isBlock', 'isActive', 'shortBio', 'emailNotification', 'pushNotification', 'followersCount', 'followingCount', 'videoCount', 'isVerified', 'isDeleted', 'deviceToken']
            },
            {
                path: "audioId",
                populate: {
                    path: "genreId",
                    select: "name",
                },
            },
            {
                path: "categoryId",
                select: ['name', 'image']
            },
        ];
        const getVideo = await services.findOneWithPopulate(
            videoModelName,
            criteria,
            {},
            {},
            populateQuery
        );

        if (!getVideo) {
            return res.status(200).send({
                status: 404,
                message: "Video not Found",
            })

        }
        successResponse(res, getVideo)
        return

    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

const getAllUserVideo = async (req, res) => {
    try {
        const { userId } = req.user
        const videoModelName = "userVideo"
        let criteria = {
            userId: userId,
            isDeleted: false,
            isBlock: false
        }
        let populateQuery = [
            {
                path: "userId",
                select: ['userName', 'name', 'email', 'phoneNumber', 'deviceType', 'loginType', 'profileImage', 'isBlock', 'isActive', 'shortBio', 'emailNotification', 'pushNotification', 'followersCount', 'followingCount', 'videoCount', 'isVerified', 'isDeleted', 'deviceToken']
            },
            {
                path: "audioId",
                populate: {
                    path: "genreId",
                    select: "name",
                },
            },
            {
                path: "categoryId",
                select: ['name', 'image']
            },
        ];
        const getVideo = await services.findWithPopulate(
            videoModelName,
            criteria,
            {},
            {},
            populateQuery
        );

        if (getVideo.length == 0) {
            return res.status(200).send({
                status: 404,
                message: "Video not Found",
            })

        }
        successResponse(res, getVideo)
        return

    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

const videoComment = async (req, res) => {
    try {
        const modelName = "commentModel";
        /**
         * get loogged user id
         */
        const { userId, userName } = req.user;
        let { videoId, comment } = req.body;
        let dataObj = {}

        /**
         * validations of video id and comment
         */
        if (!videoId) {
            return await validatorErrorResponse(res, "Required", "videoId");
        }
        if (!comment) {
            return await validatorErrorResponse(res, "Required", "comment");
        }
        dataObj.videoId = videoId;
        dataObj.comment = comment;
        dataObj.commentBy = userId;
        let notificationData = { to: dataObj.videoOwnerId, fromUserName: userName, from: userId, token: "", }
        /**
         * Get logged user details like =>userName, and profile image 
         */
        const commentUserDetails = await services.getoneData("user", { _id: userId }, { userName: true, profileImage: true }, {})
        notificationHelper("comment", notificationData)
        /** insert comment details in model*/
        const insetComment = await services.InsertData(modelName, dataObj);

        /* update comment count in uservideo model */
        await services.updateData("userVideo", { _id: videoId }, { $inc: { videoCommentCount: 1 } }, { new: true })
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: insetComment,
            commentUserDetails: commentUserDetails
        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};


const globalVideo = async (req, res) => {
    try {

        const { userId } = req.user
        const videoModelName = "userVideo"
        const userFollowersModelName = "userFollowersModel"
        const videoLikesModelName = "videoLikesModel"
        let criteriaArr = []
        let criteria = { isDeleted: false, isBlock: false }
        criteriaArr.push({ userId: { "$ne": userId } })
        if (req.body.type == "following") {
            // get all user follow 
            let getAlluserFollowers = await services.getData(userFollowersModelName, {
                followedBy: userId
            }, { followTo: true })
            let followId = getAlluserFollowers.map((data) => data.followTo)
            criteriaArr.push({ userId: { "$in": followId } })
        }
        criteria.$and = criteriaArr
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: {
                createdAt: -1,
            },
        };
        let populateQuery = [
            {
                path: "userId",
                select: ['userName', 'name', 'email', 'phoneNumber', 'deviceType', 'loginType', 'profileImage', 'isBlock', 'isActive', 'shortBio', 'emailNotification', 'pushNotification', 'followersCount', 'followingCount', 'videoCount', 'isVerified', 'isDeleted', 'deviceToken']
            },
            {
                path: "audioId",
                populate: {
                    path: "genreId",
                    select: "name",
                },
            },
            {
                path: "categoryId",
                select: ['name', 'image']
            },
        ];
        const getVideo = await services.findWithPopulate(
            videoModelName,
            criteria,
            {},
            options,
            populateQuery
        );

        if (getVideo.length == 0) {
            return res.status(200).send({
                status: 404,
                message: "Video not Found",
            })

        }
        console.log("----getVideo----", getVideo)
        //check user like video
        if (getVideo.length) {
            for (let i = 0; i < getVideo.length; i++) {
                const videoId = getVideo[i]._id
                let checkLike = {
                    videoId: videoId,
                    userId: userId

                }
                let CheckLoginUserLikeVideo = await services.getoneData(videoLikesModelName, checkLike)
                if (CheckLoginUserLikeVideo) {
                    getVideo[i].isLiked = true
                }
                else {
                    getVideo[i].isLiked = false
                }

            }
        }

        // check user folower 
        if (getVideo.length) {
            for (let i = 0; i < getVideo.length; i++) {
                const videoOwnerId = getVideo[i].userId._id
                let checkFollwors = {
                    followTo: videoOwnerId,
                    followedBy: userId

                }
                let CheckLoginUserFollowers = await services.getoneData("userFollowersModel", checkFollwors)

                if (CheckLoginUserFollowers) {
                    getVideo[i].isFollow = true
                }
                else {
                    getVideo[i].isFollow = false
                }

            }
        }

        // check view video 
        if (getVideo.length) {
            for (let i = 0; i < getVideo.length; i++) {
                const videoId = getVideo[i]._id
                console.log("videoId", videoId);
                console.log("userId", userId);
                let criteria = {
                    videoId: videoId,
                    viewedBy: userId

                }
                let isUserViewedVideo = await services.getoneData("videosViewModel", criteria)

                if (isUserViewedVideo) {
                    getVideo[i].isViewed = true
                }
                else {
                    getVideo[i].isViewed = false
                }

            }
        }


        successResponse(res, getVideo)
        return


    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}


const commentReply = async (req, res) => {
    try {
        const modelName = "commentReply";
        const { userId } = req.user; // get logged user id
        let { commentId, reply } = req.body;
        let dataObj = {}
        /** validations of req parameters */
        if (!commentId) {
            return await validatorErrorResponse(res, "Required", "commentId");
        }
        if (!reply) {
            return await validatorErrorResponse(res, "Required", "reply");
        }

        /**
         * assign values to dataobject and Add Comment reply in comment reply modal
         */
        dataObj.commentId = commentId;
        dataObj.reply = reply;
        dataObj.replyBy = userId;
        const addReply = await services.InsertData(modelName, dataObj);
        /**
         * get id for reply comment than update it to the comment modal
         */
        const { _id } = addReply
        await services.updateData("commentModel", { _id: commentId }, { $push: { replyId: _id } }, { new: true })
        successResponse(res, addReply)
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getVideosComment = async (req, res) => {
    try {
        const modelName = "commentModel";
        let { videoId } = req.body;
        if (!videoId) {
            return await validatorErrorResponse(res, "Required", "videoId");
        }
        let criteria = { videoId: videoId }
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
        };
        let populateQuery = [
            {
                path: "replyId",
                select: "reply",
                populate: {
                    path: "replyBy",
                    select: ["userName", "profileImage", "isDeleted"],
                },
            },
            {
                path: "commentBy",
                select: ["userName", "profileImage", "isDeleted"],
            },
        ];
        const getComments = await services.findWithPopulate(
            modelName,
            criteria,
            {},
            options,
            populateQuery
        );

        successResponse(res, getComments)
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getUserFollowers = async (req, res) => {
    try {
        const modelName = "userFollowersModel";
        let { userId } = req.user;
        let criteria = { followTo: userId, }
        console.log(criteria);
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
        };
        let populateQuery = [
            {
                path: "followedBy",
                select: ["name", "userName", "followersCount", "profileImage", "isDeleted"],
                match: req.body.search ? { userName: { $regex: req.body.search, $options: "i" } } : {}

            },

        ];

        const getFollowers = await services.findWithPopulate(
            modelName,
            criteria,
            { followTo: false },
            options,
            populateQuery
        );
        let finalData = []
        if (getFollowers.length > 0) finalData = getFollowers.filter(item => item.followedBy)
        successResponse(res, finalData)
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getUserFollowing = async (req, res) => {
    try {
        const modelName = "userFollowersModel";
        let { userId } = req.user;
        let criteria = { followedBy: userId }
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
        };
        let populateQuery = [
            {
                path: "followTo",
                select: ["name", "userName", "followersCount", "profileImage", "isDeleted"],
                match: req.body.search ? { userName: { $regex: req.body.search, $options: "i" } } : {}
            },

        ];
        const getFollowing = await services.findWithPopulate(
            modelName,
            criteria,
            { followedBy: false },
            options,
            populateQuery
        );
        let finalData = []
        if (getFollowing.length > 0) finalData = getFollowing.filter(item => item.followTo)
        successResponse(res, finalData)
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const reportVideo = async (req, res) => {
    try {
        const modelName = "reportedVideo";
        let { userId } = req.user;
        let { videoId, reason } = req.body
        if (!videoId) {
            return await validatorErrorResponse(res, "Required", "videoId");
        }
        if (!reason) {
            return await validatorErrorResponse(res, "Required", "reason");
        }
        let reportVideo = {
            videoId: videoId,
            reason: reason,
            reportedBy: userId,
        }
        let isAlreadyReported = await services.getoneData(modelName, reportVideo);
        if (isAlreadyReported) {
            return res.status(200).send({
                status: 200,
                message: "Report submitted",
            })
        }
        const create = await services.InsertData(modelName, reportVideo);
        await services.updateData("userVideo", { _id: videoId }, { isReported: true, $inc: { videoReportCount: 1 } }, { deviceToken: "" }, { new: true })
        return res.status(200).send({
            status: 200,
            message: "Report submitted",
        })
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
}
const userlogout = async (req, res) => {
    try {
        const modelName = "user";
        let { userId } = req.user;
        let criteria = { _id: userId }

        let userLogout = await services.updateData(modelName, criteria, {}, { new: true })

        res.send({
            statusCode: 200,
            message: "user logout",
        });
        return;
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }

};

// get user profile by id
const getUserProfileById = async (req, res) => {
    try {
        const { userId } = req.user
        const { userIdentity } = req.body;
        if (!userIdentity) {
            return await validatorErrorResponse(res, "userId required", "userIdentity");
        }
        const modelName = "user";
        const user = await services.getoneData(modelName, { _id: userIdentity }, { password: false }, { lean: true })
        if (user.isDeleted === true) {
            res.send({
                statusCode: 400,
                message: "Account is deleted.",
            });
            return;
        }
        const isFollow = await services.getoneData("userFollowersModel", { followedBy: userId }, {}, {})
        if (isFollow) {
            user.follow = true
        } else {
            user.follow = false
        }
        successResponse(res, user);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

/* get all user videos by id*/
const getAllUserVideoById = async (req, res) => {
    try {
        const { userId } = req.user
        const { userIdentity } = req.body
        if (!userIdentity) {
            return await validatorErrorResponse(res, "userIdentity required", "userIdentity");
        }
        const videoModelName = "userVideo"
        const videoLikesModelName = "videoLikesModel"
        let criteria = { isDeleted: false, userId: userIdentity, isBlock: false }
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page > 0 ? req.body.page : 1);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: {
                createdAt: -1,
            },
        };
        let populateQuery = [
            {
                path: "userId",
                select: ['userName', 'name', 'email', 'phoneNumber', 'deviceType', 'loginType', 'profileImage', 'isBlock', 'isActive', 'shortBio', 'emailNotification', 'pushNotification', 'followersCount', 'followingCount', 'videoCount', 'isVerified', 'isDeleted', 'deviceToken']
            },
            {
                path: "audioId",
                populate: {
                    path: "genreId",
                    select: "name",
                },
            },
            {
                path: "categoryId",
                select: ['name', 'image']
            },
        ];
        const getVideo = await services.findWithPopulate(
            videoModelName,
            criteria,
            {},
            options,
            populateQuery
        );

        if (getVideo.length == 0) {
            return res.status(200).send({
                status: 404,
                message: "Video not Found",
            })

        }

        //check user like video
        if (getVideo.length) {
            for (let i = 0; i < getVideo.length; i++) {
                const videoId = getVideo[i]._id
                let checkLike = {
                    videoId: videoId,
                    userId: userId

                }
                let CheckLoginUserLikeVideo = await services.getoneData(videoLikesModelName, checkLike)
                if (CheckLoginUserLikeVideo) {
                    getVideo[i].isLiked = true
                }
                else {
                    getVideo[i].isLiked = false
                }
                // check user folower 
                const videoOwnerId = getVideo[i].userId._id
                let checkFollwors = {
                    followTo: videoOwnerId,
                    followedBy: userId

                }
                let CheckLoginUserFollowers = await services.getoneData("userFollowersModel", checkFollwors)

                if (CheckLoginUserFollowers) {
                    getVideo[i].isFollow = true
                }
                else {
                    getVideo[i].isFollow = false
                }
                // check view video 
                const videoIds = getVideo[i]._id
                let criteria = {
                    videoId: videoIds,
                    viewedBy: userId

                }
                let isUserViewedVideo = await services.getoneData("videosViewModel", criteria)

                if (isUserViewedVideo) {
                    getVideo[i].isViewed = true
                }
                else {
                    getVideo[i].isViewed = false
                }
            }
        }

        successResponse(res, getVideo)
        return


    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

/* get all user videos by id*/
const videoViewByUser = async (req, res) => {
    try {
        const { userId } = req.user;
        const { videoId } = req.body
        if (!videoId) {
            return await validatorErrorResponse(res, "VideoId required", "videoId");
        }
        const modelName = "videosViewModel"
        console.log(userId);
        /*check if video is already viewed*/
        const isViewed = await services.getoneData(modelName, { viewedBy: userId, videoId: videoId }, {}, {})
        if (isViewed) {
            return res.status(200).send({
                status: 422,
                message: "Already viewed the video",
            });
        }
        const insertRecord = await services.InsertData(modelName, { viewedBy: userId, videoId: videoId })
        const user = await services.updateData("userVideo", { _id: videoId }, { $inc: { videoViewCount: 1 } }, { new: true }, {})

        return res.status(200).send({
            status: 200,
            message: "success",
        });

    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

// get categories
const getCategories = async (req, res) => {
    try {

        // fetch categories from database
        const categories = await services.getData("category", { isDeleted: false }, {}, {});
        //send response
        successResponse(res, categories)
        return
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

// get videos by audio id
const getVideosByAudioId = async (req, res) => {
    try {
        // fetch data from request
        const { userId } = req.user
        const { audioId } = req.body
        // validation
        if (!audioId) {
            return await validatorErrorResponse(res, "Required", "audioId");
        }
        const videoModelName = "userVideo"
        const videoLikesModelName = "videoLikesModel"
        // criteria is condition based on data will retrieve from database
        let criteria = { isDeleted: false, audioId: audioId, isBlock: false }
        // pagination
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page > 0 ? req.body.page : 1);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: {
                createdAt: -1,
            },
        };
        // fetch data from anothers table basesd on reference column
        let populateQuery = [
            {
                path: "userId",
                select: ['userName', 'name', 'email', 'phoneNumber', 'deviceType', 'loginType', 'profileImage', 'isBlock', 'isActive', 'shortBio', 'emailNotification', 'pushNotification', 'followersCount', 'followingCount', 'videoCount', 'isVerified', 'isDeleted', 'deviceToken']
            },
            {
                path: "audioId",
                populate: {
                    path: "genreId",
                    select: "name",
                },
            },
        ];
        // retrieve audio from database
        let audio = await services.getoneData("videoAudio", { _id: audioId }, {}, {})
        // retrieve vvideo based on audio Id
        const getVideo = await services.findWithPopulate(
            videoModelName,
            criteria,
            {},
            options,
            populateQuery
        );
        // if no video found
        if (getVideo.length == 0) {
            return res.status(200).send({
                status: 404,
                message: "Video not Found",
            })

        }

        //check user like video
        if (getVideo.length) {
            for (let i = 0; i < getVideo.length; i++) {
                const videoId = getVideo[i]._id
                let checkLike = {
                    videoId: videoId,
                    userId: userId

                }
                let CheckLoginUserLikeVideo = await services.getoneData(videoLikesModelName, checkLike)
                if (CheckLoginUserLikeVideo) {
                    getVideo[i].isLiked = true
                }
                else {
                    getVideo[i].isLiked = false
                }
                // check user folower 
                const videoOwnerId = getVideo[i].userId._id
                let checkFollwors = {
                    followTo: videoOwnerId,
                    followedBy: userId

                }
                let CheckLoginUserFollowers = await services.getoneData("userFollowersModel", checkFollwors)

                if (CheckLoginUserFollowers) {
                    getVideo[i].isFollow = true
                }
                else {
                    getVideo[i].isFollow = false
                }
                // check view video 
                const videoIds = getVideo[i]._id
                let criteria = {
                    videoId: videoIds,
                    viewedBy: userId

                }
                let isUserViewedVideo = await services.getoneData("videosViewModel", criteria)

                if (isUserViewedVideo) {
                    getVideo[i].isViewed = true
                }
                else {
                    getVideo[i].isViewed = false
                }
            }
        }
        //send response
        return res.status(200).send({
            status: 200,
            message: "success",
            audio: audio,
            videos: getVideo
        })


    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

// global search
const globalSearch = async (req, res) => {
    try {
        const { userId } = req.user
        // fetch data from request
        const { type, search } = req.body
        let criteriaArr = []
        let postCriteria = { isDeleted: false, isBlock: false }
        let userCriteria = { isDeleted: false }
        let audioCriteria = {}
        criteriaArr.push({ userId: { "$ne": userId } })
        postCriteria.$and = criteriaArr
        // pagination
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page > 0 ? req.body.page : 1);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: {
                createdAt: -1,
            },
        };
        if (search) {
            userCriteria.$or = [
                { userName: { $regex: search, $options: "i" } },
                { email: { $regex: search, $options: "i" } },
                { phoneNumber: { $regex: search, $options: "i" } },
            ];
            postCriteria.$or = [
                { videoCaption: { $regex: search, $options: "i" } },
            ];
            audioCriteria.$or = [
                { songName: { $regex: search, $options: "i" } },
                { artistName: { $regex: search, $options: "i" } },
            ];
        }
        let data = { users: [], posts: [], audios: [] }
        // fetch data from anothers table basesd on reference column
        let postPopulateQuery = [
            {
                path: "userId",
                select: ['userName', 'name', 'email', 'phoneNumber', 'deviceType', 'loginType', 'profileImage', 'isBlock', 'isActive', 'shortBio', 'emailNotification', 'pushNotification', 'followersCount', 'followingCount', 'videoCount', 'isVerified', 'isDeleted', 'deviceToken']
            },
            {
                path: "audioId",
                populate: {
                    path: "genreId",
                    select: "name",
                },
            },
        ];
        let audioPopulateQuery = [
            {
                path: "genreId",
                select: 'name'
            },

        ];
        if (type) {
            if (type === 'user') {
                const users = await services.getData("user", userCriteria, { password: false, __v: false }, options);
                if (users.length > 0) data.users = users
            } else if (type === 'audio') {
                // retrieve audio from database
                let audio = await services.findWithPopulate(
                    "videoAudio",
                    audioCriteria,
                    {},
                    options,
                    audioPopulateQuery
                );
                if (audio.length > 0) data.audios = audio
            } else if (type === 'post') {
                // retrieve video from database
                const getVideo = await services.findWithPopulate(
                    "userVideo",
                    postCriteria,
                    {},
                    options,
                    postPopulateQuery
                );
                //check user like video
                if (getVideo.length > 0) {
                    for (let i = 0; i < getVideo.length; i++) {
                        const videoId = getVideo[i]._id
                        let checkLike = {
                            videoId: videoId,
                            userId: userId
                        }
                        let CheckLoginUserLikeVideo = await services.getoneData("videoLikesModel", checkLike)
                        if (CheckLoginUserLikeVideo) {
                            getVideo[i].isLiked = true
                        }
                        else {
                            getVideo[i].isLiked = false
                        }
                        // check user folower 
                        const videoOwnerId = getVideo[i].userId._id
                        let checkFollwors = {
                            followTo: videoOwnerId,
                            followedBy: userId

                        }
                        let CheckLoginUserFollowers = await services.getoneData("userFollowersModel", checkFollwors)

                        if (CheckLoginUserFollowers) {
                            getVideo[i].isFollow = true
                        }
                        else {
                            getVideo[i].isFollow = false
                        }
                        // check view video 
                        const videoIds = getVideo[i]._id
                        let criteria = {
                            videoId: videoIds,
                            viewedBy: userId

                        }
                        let isUserViewedVideo = await services.getoneData("videosViewModel", criteria)

                        if (isUserViewedVideo) {
                            getVideo[i].isViewed = true
                        }
                        else {
                            getVideo[i].isViewed = false
                        }
                    }
                }
                if (getVideo.length > 0) data.posts = getVideo
            }

        } else {
            // retrieve users from database
            const users = await services.getData("user", userCriteria, { password: false, __v: false }, options);
            // retrieve audio from database
            let audio = await services.findWithPopulate(
                "videoAudio",
                audioCriteria,
                {},
                options,
                audioPopulateQuery
            );
            // retrieve video from database
            const getVideo = await services.findWithPopulate(
                "userVideo",
                postCriteria,
                {},
                options,
                postPopulateQuery
            );
            //check user like video
            if (getVideo.length > 0) {
                for (let i = 0; i < getVideo.length; i++) {
                    const videoId = getVideo[i]._id
                    let checkLike = {
                        videoId: videoId,
                        userId: userId
                    }
                    let CheckLoginUserLikeVideo = await services.getoneData("videoLikesModel", checkLike)
                    if (CheckLoginUserLikeVideo) {
                        getVideo[i].isLiked = true
                    }
                    else {
                        getVideo[i].isLiked = false
                    }
                    // check user folower 
                    const videoOwnerId = getVideo[i].userId._id
                    let checkFollwors = {
                        followTo: videoOwnerId,
                        followedBy: userId

                    }
                    let CheckLoginUserFollowers = await services.getoneData("userFollowersModel", checkFollwors)

                    if (CheckLoginUserFollowers) {
                        getVideo[i].isFollow = true
                    }
                    else {
                        getVideo[i].isFollow = false
                    }
                    // check view video 
                    const videoIds = getVideo[i]._id
                    let criteria = {
                        videoId: videoIds,
                        viewedBy: userId

                    }
                    let isUserViewedVideo = await services.getoneData("videosViewModel", criteria)

                    if (isUserViewedVideo) {
                        getVideo[i].isViewed = true
                    }
                    else {
                        getVideo[i].isViewed = false
                    }
                }
            }
            if (users.length > 0) data.users = users
            if (getVideo.length > 0) data.posts = getVideo
            if (audio.length > 0) data.audios = audio
        }
        //send response
        return res.status(200).send({
            status: 200,
            message: "success",
            data: data
        })


    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

// edit video caption
const editVideoCaption = async (req, res) => {
    try {
        const userVideoModel = "userVideo";
        let { slug } = req.body;
        //validation for video slug
        if (!slug) {
            return validatorErrorResponse(res, "Required", "slug");
        }
        //get single video data from its slug
        const video = await services.getoneData(userVideoModel, { slug: slug });
        if (!video) {
            //if slug not found send response
            return res.status(200).send({
                status: 404,
                message: "Video does not exits",
            });
        }
        if (!req.body.videoCaption) {
            return validatorErrorResponse(res, "Required", "videoCaption");
        }
        const editVideoCaption = await services.updateData(userVideoModel, { slug: slug }, { videoCaption: req.body.videoCaption }, { new: true })
        successResponse(res, editVideoCaption);
        return

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

// edit video caption
const deleteVideo = async (req, res) => {
    try {
        const userVideoModel = "userVideo";
        const { userId } = req.user;
        let { slug } = req.body;
        //validation for video slug
        if (!slug) {
            return validatorErrorResponse(res, "Required", "slug");
        }
        //get single video data from its slug
        const video = await services.getoneData(userVideoModel, { slug: slug });
        if (!video) {
            //if slug not found send response
            return res.status(200).send({
                status: 404,
                message: "Video does not exits",
            });
        }
        const videoDelete = await services.updateData(userVideoModel, { slug: slug }, { isDeleted: true }, { new: true })
        // update user video count 
        if (video.isDeleted == false) {
            await services.updateData("user", { _id: userId }, { $inc: { videoCount: -1 } }, {}, { new: true })
        }

        res.send({
            status: 200,
            message: "Your Video Delete"

        });
        return;

    } catch (error) {
        console.log(error)
        if (error) {
            sendError(error, res);
        }
    }
};

// get public user followers list 
const getUserFollowersList = async (req, res) => {
    try {
        const modelName = "userFollowersModel";
        const { userName } = req.body
        if (!userName) {
            return await validatorErrorResponse(res, "Required", "userName");
        }
        // get userId 
        const user = await services.getoneData("user", { userName: userName })
        if (!user) {
            return successResponse(res, [])
        }
        let criteria = { followTo: user._id, }
        console.log(criteria);
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
        };
        let populateQuery = [
            {
                path: "followedBy",
                select: ["name", "userName", "followersCount", "profileImage", "isDeleted"],
                match: req.body.search ? { userName: { $regex: req.body.search, $options: "i" } } : {}

            },

        ];

        const getFollowers = await services.findWithPopulate(
            modelName,
            criteria,
            { followTo: false },
            options,
            populateQuery
        );
        let finalData = []
        if (getFollowers.length) {
            finalData = getFollowers.length > 0 && getFollowers.filter(item => item.followedBy)
        }
        successResponse(res, finalData)
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getUserFollowingList = async (req, res) => {
    try {
        const modelName = "userFollowersModel";
        const { userName } = req.body
        if (!userName) {
            return await validatorErrorResponse(res, "Required", "userName");
        }
        // get userId 
        const user = await services.getoneData("user", { userName: userName })
        if (!user) {
            return successResponse(res, [])
        }
        let criteria = { followedBy: user._id }
        let perPage = req.body.limit ? req.body.limit : "7";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
        };
        let populateQuery = [
            {
                path: "followTo",
                select: ["name", "userName", "followersCount", "profileImage", "isDeleted"],
                match: req.body.search ? { userName: { $regex: req.body.search, $options: "i" } } : {}
            },

        ];
        const getFollowing = await services.findWithPopulate(
            modelName,
            criteria,
            { followedBy: false },
            options,
            populateQuery
        );
        let finalData = []
        if (getFollowing.length) {
            finalData = getFollowing.length > 0 && getFollowing.filter(item => item.followTo)
        }
        successResponse(res, finalData)
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};




module.exports = {
    userLogin,
    userSignUp,
    changePassword,
    faceBookLogin,
    googleLogin,
    getUserProfile,
    updateUserProfile,
    forgetPassword,
    otpVerify,
    resetPassword,
    audioList,
    userVideoUpload,
    getGernalSetting,
    userNotificationOnOff,
    decativeUserAccount,
    deleteUser,
    userNotificationOnOff,
    videoLikeDislike,
    userFollowUnfollow,
    getVideo,
    getAllUserVideo,
    globalVideo,
    videoComment,
    commentReply,
    getVideosComment,
    getUserFollowers,
    getUserFollowing,
    reportVideo,
    userlogout,
    getUserProfileById,
    getAllUserVideoById,
    videoViewByUser,
    getCategories,
    getVideosByAudioId,
    globalSearch,
    editVideoCaption,
    deleteVideo,
    getUserFollowersList,
    getUserFollowingList
};
