const bcrypt = require("bcrypt");
var jwt = require("jsonwebtoken");
const saltRounds = 10;
const services = require("../services/services");
const { validatorErrorResponse } = require("../utils/universalFunction");
const { sendError } = require("../utils/universalFunction");
const {
    successResponse,
    uploadFileToAws,
    generateUniqueId,
    convertStringToObjectId,
} = require("../utils/universalFunction");
const { SECRET_JWT_CODE } = require("../config/Config");
const c = require("../config/Config");
const userModel = require("../models/userModel")
const moment = require("moment");
const { findIndex } = require("lodash");
const _ = require("lodash");
const wallet = require("../lib/wallet");
const Stripe = require("stripe")
const stripe = Stripe(c.STRIPE.SK_dev);

const adminLogin = async (req, res) => {
    try {
        const { email, password } = req.body;
        if (!email) {
            return await validatorErrorResponse(res, "email required", "email");
        }
        if (!password) {
            return await validatorErrorResponse(res, "password required", "password");
        }

        let admin = await services.getoneData("admin", { email: email });

        if (!admin) {
            return await validatorErrorResponse(res, "Invalid email", "email");
        }

        var match = await bcrypt.compare(password, admin.password);
        if (match) {
            const token = jwt.sign(
                { adminId: admin._id, name: admin.name, email: admin.email },
                SECRET_JWT_CODE
            );
            return res
                .status(200)
                .send({
                    status: 200, message: "login successful", token: token, details: {
                        name: admin.name,
                        image: admin.image
                    }
                });
        } else {
            return await validatorErrorResponse(res, "Invalid password", "password");
        }
    } catch (error) {
        if (error) {
            sendError(error, res);
        }
    }
};

const adminSignUp = async (req, res) => {
    try {
        const { name, email, password } = req.body;
        if (!name) {
            return await validatorErrorResponse(res, "name required", "name");
        }
        if (!email) {
            return await validatorErrorResponse(res, "email required", "email");
        }
        if (!password) {
            return await validatorErrorResponse(res, "password required", "password");
        }
        const admin = await services.getoneData("admin", { email: email });

        if (admin) {
            return res.status(200).send({
                status: 401,
                message: "Email already exists",
                error: true,
                params: "email",
            });
        }

        const hashPassword = await bcrypt.hash(password, saltRounds);

        const admindata = await services.InsertData("admin", {
            name: name,
            email: email,
            password: hashPassword,
        });

        const token = jwt.sign(
            { adminId: admindata._id, email: email },
            SECRET_JWT_CODE
        );

        return res
            .status(200)
            .send({ status: 200, message: "Admin Registered", token: token });
    } catch (error) {
        if (error) {
            sendError(error, res);
            console.log(error);
        }
    }
};

const changePassword = async (req, res) => {
    try {
        const { adminId } = req.admin;

        const { oldpassword, newpassword, confirmpassword } = req.body;

        if (!oldpassword) {
            return await validatorErrorResponse(
                res,
                "oldpassword required",
                "oldpassword"
            );
        }
        if (!newpassword) {
            return await validatorErrorResponse(
                res,
                "newpassword required",
                "newpassword"
            );
        }
        if (!confirmpassword) {
            return await validatorErrorResponse(
                res,
                "confirmpassword required",
                "confirmpassword"
            );
        }
        const admin = await services.getoneData("admin", { _id: adminId });
        var match = await bcrypt.compare(oldpassword, admin.password);
        if (!match) {
            return await validatorErrorResponse(
                res,
                "check your old password ",
                "oldpassword"
            );
        }

        if (newpassword != confirmpassword) {
            return await validatorErrorResponse(
                res,
                "Your new password or  confirm password must be same",
                "newpassword or confirm"
            );
        }

        const hashPassword = await bcrypt.hash(newpassword, saltRounds);
        let admindata = await services.updateData(
            "admin",
            {
                _id: adminId,
            },
            {
                password: hashPassword,
            }
        );
        successResponse(res, "Password changed");
    } catch (error) {
        if (error) {
            sendError(error, res);
        }
    }
};

const editProfile = async (req, res) => {
    try {
        const { adminId } = req.admin;
        const { name, email } = req.body;

        let admindata = {};
        if (name) {
            admindata.name = name;
        }
        if (email) {
            admindata.email = email;
        }
        if (req.files && req.files.image) {
            const file = req.files.image;
            const uploadImage = await uploadFileToAws(file);
            admindata.image = uploadImage.fileUrl;
        }
        const admin = await services.updateData(
            "admin",
            { _id: adminId },
            admindata,
            {
                new: true,
                projection: {
                    password: false,
                },
            }
        );

        return res
            .status(200)
            .send({ status: 200, message: "prfile updated", data: admin });
    } catch (error) {
        console.log(error);
    }
};

const userBlockUnblock = async (req, res) => {
    try {
        const modelName = "user";
        const { id, isBlock } = req.body;
        const user = await services.updateData(
            modelName,
            { _id: id },
            {
                isBlock: isBlock,
            },
            {
                new: true,
                projection: {
                    password: false,
                },
            }
        );

        return res.status(200).send({
            status: 200,
            message: isBlock ? "User blocked" : "User unblocked",
            data: user,
        });
    } catch (error) {
        console.log(error);
    }
};

const getAdminProfile = async (req, res) => {
    try {
        const { adminId } = req.admin;
        const admin = await services.getoneData(
            "admin",
            { _id: adminId },
            { name: true, email: true, image: true }
        );
        successResponse(res, admin);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getUserList = async (req, res) => {
    try {
        let { query, limit, page, type } = req.body
        let criteria = { isDeleted: false };
        if (query) {
            criteria.$or = [
                { userName: { $regex: query, $options: "i" } },
                { email: { $regex: query, $options: "i" } },
                { phoneNumber: { $regex: query, $options: "i" } },
            ];
        }
        let perPage = limit ? limit : "10";
        let pageNo = Math.max(0, page);
        let options = {
            skip: parseInt(perPage * (pageNo - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: { createdAt: -1 },
        };
        const user = await services.getData("user", criteria, { password: false, __v: false }, options);
        const userListLength = await services.getData("user", criteria, { password: false, __v: false }, {});
        let numPages = Math.ceil(parseInt(userListLength.length) / perPage);
        if (type == "export") {
            return res.status(200).send({
                status: 200,
                message: "Success.",
                dataLength: userListLength.length,
                data: userListLength
            });
        } else {
            return res.status(200).send({
                status: 200,
                message: "Success.",
                data: user,
                current: pageNo,
                previous: pageNo > 1 ? pageNo - 1 : undefined,
                next: pageNo < numPages ? parseInt(pageNo) + parseInt(1) : undefined,
                totalData: userListLength.length,
                totalPages: numPages,
            });
        }

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

//user Search for dropdown
const getSearchUsers = async (req, res) => {
    try {
        let { query, page } = req.query
        /** Condition =Active and unblocked user */
        let criteria = { isActive: true, isBlock: false, isDeleted: false };

        if (query) {
            criteria.$and = [
                { userName: { $regex: query, $options: "i" } },
            ];
        }
        const perPage = 10;
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: { createdAt: -1 },
        };
        const user = await services.getData("user", criteria, { userName: true, _id: true, phoneNumber: true, isVerified: true }, options)
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: user,
        });
    } catch (error) {
        console.log("error", error)
        if (error) {
            sendError(error, res);
        }
    }
};

const getDonationSetting = async (req, res) => {
    try {
        const donationSetting = await services.getoneData("paymentSetting", {}, {});
        console.log("donationSetting", donationSetting);
        successResponse(res, donationSetting);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const donationSetting = async (req, res) => {
    try {
        const modelName = "paymentSetting";
        let { adminCommision } = req.body;
        if (!adminCommision) {
            return await validatorErrorResponse(res, "Required", "adminCommision");
        } else if (isNaN(adminCommision)) {
            return await validatorErrorResponse(
                res,
                "Value should be digit",
                "adminCommision"
            );
        } else if (adminCommision > 100) {
            return await validatorErrorResponse(
                res,
                "Number should not less than 100",
                "adminCommision"
            );
        }
        const donationSetting = await services.getoneData(modelName, {});
        if (donationSetting) {
            let updatedDonation = await services.updateData(
                modelName,
                {},
                { adminCommision: adminCommision }
            );
        } else {
            let create = await services.InsertData(modelName, {
                adminCommision: adminCommision,
            });
        }

        successResponse(res);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const addVideoAudio = async (req, res) => {
    try {
        const modelName = "videoAudio";
        const { songName, artistName, audioTime, genreId } = req.body;

        if (!req.files.audio) {
            return await validatorErrorResponse(res, "audio required ", "audio");
        }
        if (!req.files.Thumbnail) {
            return await validatorErrorResponse(
                res,
                "Thumbnail required ",
                "Thumbnail"
            );
        }
        if (!songName) {
            return await validatorErrorResponse(
                res,
                "songName required ",
                "songName"
            );
        }
        if (!artistName) {
            return await validatorErrorResponse(
                res,
                "artistName required ",
                "artistName"
            );
        }
        if (!genreId) {
            return await validatorErrorResponse(res, "genreId required ", "genreId");
        }

        let genreIdObject = await convertStringToObjectId(genreId);

        let audio = {
            songName: songName,
            artistName: artistName,
            audioTime: audioTime,
            genreId: genreIdObject,
        };
        const slug = await generateUniqueId(modelName, "slug");
        if (slug) {
            audio.slug = slug;
        }

        if (req.files && req.files.audio) {
            const file = req.files.audio;
            const uploadImage = await uploadFileToAws(file);
            audio.audio = uploadImage.fileUrl;
        }
        if (req.files && req.files.Thumbnail) {
            const file = req.files.Thumbnail;
            const uploadImage = await uploadFileToAws(file);
            audio.Thumbnail = uploadImage.fileUrl;
        }

        let addvideoAudio = await services.InsertData(modelName, audio);

        successResponse(res, addvideoAudio);
        return;
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getVideoAudioList = async (req, res) => {
    try {
        const modelName = "videoAudio";
        let criteria = {};
        let perPage = req.body.limit ? req.body.limit : "10";
        let page = Math.max(0, req.body.page);
        let startIndex = (page * perPage) - perPage
        let lastIndex = (page * perPage)
        let options = {
            lean: true,
            sort: { createdAt: -1 },
        };
        let populateQuery = [
            {
                path: "genreId",
                select: "name",
                match: req.body.query ? { name: { $regex: req.body.query, $options: "i" } } : {}
            },
        ];
        let finalData = []
        const audioList = await services.findWithPopulate(modelName, criteria, {}, options, populateQuery);
        finalData = audioList
        query = req.body.query && req.body.query.toLowerCase()
        if (finalData.length > 0 && query) finalData = finalData.filter(item => item.artistName && (item.artistName.indexOf(query) === 0) || item.songName && (item.songName.indexOf(query) === 0) || item.genreId && (item.genreId.name.toLowerCase().indexOf(query) === 0))
        let numPages = Math.ceil(parseInt(finalData.length) / perPage);
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: finalData.slice(startIndex, lastIndex),
            current: page,
            previous: page > 1 ? page - 1 : undefined,
            next: page < numPages ? parseInt(page) + parseInt(1) : undefined,
            totalData: finalData.length,
            totalPages: numPages,
        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const addGenre = async (req, res) => {
    try {
        const modelName = "genreModal";
        let { name } = req.body;
        if (!name) {
            return await validatorErrorResponse(res, "Required", "name");
        }
        let create = await services.InsertData(modelName, {
            name: name,
        });

        successResponse(res, create);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getGenre = async (req, res) => {
    try {
        const genre = await services.getData("genreModal", {});
        console.log("genreModal", genre);
        successResponse(res, genre);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const addSetting = async (req, res) => {
    try {
        let test = await services.InsertData("generalSettings", {
            type: "copyright", title: "copyright", description: "copyright copyright ",
        });
        successResponse(res, test);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getGernalSetting = async (req, res) => {
    try {
        const modelName = "generalSettings";
        const gernalSetting = await services.getData(modelName, {});
        successResponse(res, gernalSetting);

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getGernalSettingByType = async (req, res) => {
    try {
        const modelName = "generalSettings";
        const { type } = req.body;

        if (!type) {
            return validatorErrorResponse(res, "type required", "type")
        }

        const gernalSetting = await services.getoneData(modelName, { type: type });
        successResponse(res, gernalSetting);
        return;
    } catch (error) {
        if (error) {
            console.log(error)
            sendError(error, res);
        }
    }
};


const editGeneralSettings = async (req, res) => {
    try {
        const { type, title, description } = req.body;
        if (!type) {
            return await validatorErrorResponse(res, "Required", "type");
        }
        const data = {}
        if (title) {
            data.title = title
        }
        if (description) {
            data.description = description
        }
        let generalSetting = await services.updateData("generalSettings", { type: type }, data, { new: true, });
        successResponse(res, generalSetting);
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};


const reportedVideos = async (req, res) => {
    try {
        let criteria = { isReported: true };
        if (req.body.query) {
            criteria.$or = [
                { videoCaption: { $regex: req.body.query, $options: "i" } },
            ];
        }
        let perPage = req.body.limit ? req.body.limit : "10";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: { createdAt: -1 },
        };
        let populateQuery = [
            {
                path: "userId",
                select: "userName",
                match: { isDeleted: false }
            },
        ];
        const userVideos = await services.findWithPopulate("userVideo", criteria, {}, options, populateQuery);
        const userVideosListLength = await services.findWithPopulate("userVideo", criteria, {}, {}, populateQuery);
        let totalReporedVideos = []
        if (userVideosListLength.length > 0) totalReporedVideos = userVideosListLength.filter(item => item.userId !== null)
        let numPages = Math.ceil(parseInt(totalReporedVideos.length) / perPage);
        let reportedVideo = []
        if (userVideos.length > 0) reportedVideo = userVideos.filter(item => item.userId !== null)
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: reportedVideo,
            current: page,
            previous: page > 1 ? page - 1 : undefined,
            next: page < numPages ? parseInt(page) + parseInt(1) : undefined,
            totalData: totalReporedVideos.length,
            totalPages: numPages,
        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const verifyUnVerifyUser = async (req, res) => {
    try {
        let modelName = "user"
        let { isVerified } = req.body
        if (!req.body.userId) {
            return validatorErrorResponse(res, "userId required", "userId")
        }

        const user = await services.updateData(modelName, { _id: req.body.userId }, { isVerified: isVerified, }, { new: true });
        return res.status(200).send({
            status: 200,
            message: isVerified ? "User Verified" : "User unverified",
            data: user,
        });
    }
    catch (e) {
        sendError(error, res);
    }

}

const getAllVerifiedUser = async (req, res) => {
    try {
        let criteria = { isVerified: true, isDeleted: false };
        if (req.body.query) {
            criteria.$or = [
                { userName: { $regex: req.body.query, $options: "i" } },
                { email: { $regex: req.body.query, $options: "i" } },
                { phoneNumber: { $regex: req.body.query, $options: "i" } },
            ];
        }
        let perPage = req.body.limit ? req.body.limit : "10";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: { createdAt: -1 },
        };
        const user = await services.getData("user", criteria, {}, options);
        const userListLength = await services.getData("user", criteria, {}, {});
        let numPages = Math.ceil(parseInt(userListLength.length) / perPage);
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: user,
            current: page,
            previous: page > 1 ? page - 1 : undefined,
            next: page < numPages ? parseInt(page) + parseInt(1) : undefined,
            totalData: userListLength.length,
            totalPages: numPages,
        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
}


const ReportedVideoDetail = async (req, res) => {
    try {
        let { videoId } = req.body
        if (!videoId) {
            return await validatorErrorResponse(res, "Required", "videoId");
        }
        let criteria = { videoId: videoId };

        let populateQuery = [
            {
                path: "reportedBy",
                select: ["name", "userName",],
            },
            {
                path: "videoId",
                select: ["videoCaption", "videoLink", "videoImage"],
            },

        ];
        const reportedVideos = await services.findWithPopulate(
            "reportedVideo",
            criteria,
            {},
            {},
            populateQuery
        )
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: reportedVideos,
        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
}

const dashboard = async (req, res) => {
    try {
        let modelName = "user"
        let dashboard = []
        const totalUser = await services.getData(modelName, { isDeleted: false }, {}, {});
        const blockUser = await services.getData(modelName, { isBlock: true, isDeleted: false, isActive: true }, {}, {});
        let ActiveUserCount = await services.getData(modelName, { isBlock: false, isDeleted: false, isActive: true }, {}, {})
        const deActiveUser = await services.getData(modelName, { isActive: false, isDeleted: false }, {}, {});
        const facebookUser = await services.getData(modelName, { loginType: "facebook", isDeleted: false }, {}, {});
        const googleUser = await services.getData(modelName, { loginType: "google", isDeleted: false }, {}, {});
        const andriodUser = await services.getData(modelName, { deviceType: "android", isDeleted: false }, {}, {});
        const iosUser = await services.getData(modelName, { deviceType: "ios", isDeleted: false }, {}, {});
        const blockedVideos = await services.getData("userVideo", { isBlock: true, isDeleted: false }, {}, {});
        const totalVideos = await services.getData("userVideo", { isDeleted: false }, {}, {});
        console.log(totalVideos.length);
        const TotalAudios = await services.getData("videoAudio", {}, {}, {});
        const donationAmountData = await services.getData("transaction", { status: 'succeeded' }, {}, { lean: true });
        let donationAmount = 0
        if (donationAmountData.length > 0) donationAmountData.forEach(item => {
            if (item.amount) donationAmount += parseInt(item.amount)
        })
        const transferredAmountData = await services.getData("wallet", { status: 'debit' }, {}, { lean: true });
        let transferredAmount = 0
        if (transferredAmountData.length > 0) transferredAmountData.forEach(item => {
            if (item.amount) transferredAmount += parseInt(item.amount)
        })
        const adminCommissionData = await services.getData("wallet", { status: 'credit' }, {}, { lean: true });
        let adminCommission = 0
        if (adminCommissionData.length > 0) adminCommissionData.forEach(item => {
            if (item.adminAmount) adminCommission += parseInt(item.adminAmount)
        })
        const pendingTransactionsData = await services.getData(modelName, {}, {}, {});
        let pendingTransactions = 0
        if (pendingTransactionsData.length > 0) pendingTransactionsData.forEach(item => {
            if (item.walletCreditAmount) pendingTransactions += parseInt(item.walletCreditAmount)
        })
        return res.status(200).send({
            status: 200,
            message: "success",
            data: {
                totalUser: totalUser.length > 0 ? totalUser.length : 0,
                blockUser: blockUser.length > 0 ? blockUser.length : 0,
                ActiveUser: ActiveUserCount.length > 0 ? ActiveUserCount.length : 0,
                deActiveUser: deActiveUser.length > 0 ? deActiveUser.length : 0,
                facebookUser: facebookUser.length > 0 ? facebookUser.length : 0,
                googleUser: googleUser.length > 0 ? googleUser.length : 0,
                andriodUser: andriodUser.length > 0 ? andriodUser.length : 0,
                iosUser: iosUser.length > 0 ? iosUser.length : 0,
                totalVideos: totalVideos.length > 0 ? totalVideos.length : 0,
                blockedVideos: blockedVideos.length > 0 ? blockedVideos.length : 0,
                TotalAudios: TotalAudios.length > 0 ? TotalAudios.length : 0,
                pendingWithdrawalTransaction: pendingTransactions,
                TranferredDonation: transferredAmount,
                TotalDonationReceived: donationAmount,
                TotalAdminComission: adminCommission,
                deActiveUserLength: deActiveUser.length
            }
        });
    }
    catch (e) {
        console.log(e)
    }
}

const blockUnblockVideo = async (req, res) => {
    try {
        const modelName = "userVideo";
        const { id, isBlock } = req.body;
        if (!id) {
            return await validatorErrorResponse(res, "Required", "id");
        }
        const video = await services.updateData(
            modelName,
            { _id: id },
            {
                isBlock: isBlock,
            },
            {
                new: true,
            }
        );

        return res.status(200).send({
            status: 200,
            message: isBlock ? "Video blocked" : "Video unblocked",
            data: video,
        });
    } catch (error) {
        console.log(error);
    }
};

const ignoreReportedVideo = async (req, res) => {
    try {
        const modelName = "userVideo";
        const { id } = req.body;
        if (!id) {
            return await validatorErrorResponse(res, "Required", "id");
        }
        const video = await services.updateData(
            modelName,
            { _id: id },
            {
                videoReportCount: 0,
                isBlock: false,
                isReported: false
            },
            {
                new: true,
            }
        );

        const deleteReportedVideoData = await services.dropIndex(
            "reportedVideo",
            { videoId: id },
        );
        console.log(deleteReportedVideoData);
        return res.status(200).send({
            status: 200,
            message: "Remove Video",
            data: video,
        });
    } catch (error) {
        console.log(error);
    }
};

const filterVerifiedUnverified = async (req, res) => {
    try {
        const { userIds } = req.body
        if (!userIds || userIds.length === 0) {
            return await validatorErrorResponse(res, "Required", "userIds");
        }
        let criteria = { _id: { "$in": userIds }, isDeleted: false };
        if (req.body.query) {
            criteria.$or = [
                { userName: { $regex: req.body.query, $options: "i" } },
                { email: { $regex: req.body.query, $options: "i" } },
                { phoneNumber: { $regex: req.body.query, $options: "i" } },
            ];
        }
        let perPage = req.body.limit ? req.body.limit : "10";
        let page = Math.max(0, req.body.page);
        let options = {
            skip: parseInt(perPage * (page - 1)),
            limit: parseInt(perPage),
            lean: true,
        };
        const user = await services.getData("user", criteria, {}, options);
        let userLenght = user.length > 0 ? user.length : 0
        let numPages = Math.ceil(parseInt(userLenght) / perPage);
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: user,
            current: page,
            previous: page > 1 ? page - 1 : undefined,
            next: page < numPages ? parseInt(page) + parseInt(1) : undefined,
            totalData: userLenght,
            totalPages: numPages,
        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

const getGraphData = async (req, res) => {
    try {
        const startOfMonth = moment().startOf('year').format('YYYY-MM-DD');//get start month of year
        const endOfMonth = moment().endOf('year').format('YYYY-MM-DD');//get start month of year
        let criteria = { createdAt: { $gte: startOfMonth, $lt: endOfMonth }, isDeleted: false }//condition on basis user get data
        let donationCriteria = { createdAt: { $gte: startOfMonth, $lt: endOfMonth }, status: "succeeded" }//condition on donation amount
        let withdrawCriteria = { createdAt: { $gte: startOfMonth, $lt: endOfMonth }, status: "debit" }//condition on withdraw amount

        //temporary month array
        const tempMonthArray = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December",];

        //push tempMonthArray value to array with default value 0
        const userRegistrationGraph = tempMonthArray.map((x) => ({ key: x, value: 0 }))//for user registeration graph
        const videoCountGraph = tempMonthArray.map((x) => ({ key: x, value: 0 }))//for user video graph
        const withdrawGraph = tempMonthArray.map((x) => ({ key: x, value: 0 }))//for user withdraw graph
        const donationAmount = tempMonthArray.map((x) => ({ key: x, value: 0 }))//for user get donation

        //user Registration Graph Data
        let userData = await services.getData("user", criteria, {}, { lean: true })
        for (let i = 0; i < userData.length; i++) {
            userData[i].createdAtMonth = userData[i].createdAt.getMonth();//get month from time stamp
            const monthIndex = userRegistrationGraph.findIndex((x) => x.key == userRegistrationGraph[userData[i].createdAtMonth].key)
            if (monthIndex != -1) {
                //check if month index match tempMonthArray then we add into old value
                userRegistrationGraph[monthIndex].value = userRegistrationGraph[monthIndex].value + 1
            }
        }

        //Get video  Graph Data
        let videoData = await services.getData("userVideo", criteria, {}, { lean: true })
        for (let i = 0; i < videoData.length; i++) {
            videoData[i].createdAtMonth = videoData[i].createdAt.getMonth();
            const monthIndex = videoCountGraph.findIndex((x) => x.key == videoCountGraph[videoData[i].createdAtMonth].key)
            if (monthIndex != -1) {
                videoCountGraph[monthIndex].value = videoCountGraph[monthIndex].value + 1
            }
        }

        //Get withdraw data
        let withdrawAmountData = await services.getData("wallet", withdrawCriteria, {}, { lean: true })
        for (let i = 0; i < withdrawAmountData.length; i++) {
            withdrawAmountData[i].createdAtMonth = videoData[i].createdAt.getMonth();
            const monthIndex = withdrawGraph.findIndex((x) => x.key == withdrawGraph[withdrawAmountData[i].createdAtMonth].key)
            if (monthIndex != -1) {
                withdrawGraph[monthIndex].value = withdrawGraph[monthIndex].value + 1
            }
        }

        //Get donation data
        let donationAmountData = await services.getData("transaction", donationCriteria, {}, { lean: true })
        for (let i = 0; i < donationAmountData.length; i++) {
            donationAmountData[i].createdAtMonth = videoData[i].createdAt.getMonth();
            const monthIndex = donationAmount.findIndex((x) => x.key == donationAmount[donationAmountData[i].createdAtMonth].key)
            if (monthIndex != -1) {
                donationAmount[monthIndex].value = donationAmount[monthIndex].value + 1
            }
        }

        return res.status(200).send({
            status: 200,
            message: "Success",
            UserRegistrationGraph: userRegistrationGraph,
            VideoCountGraph: videoCountGraph,
            WithdrawGraph: withdrawGraph,
            donationData: donationAmount
        });
    }
    catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

/* push notification by admin*/
const pushNotication = async (req, res) => {
    try {
        // fetch data from request
        const { adminId } = req.admin
        const { scheduleTime, type, title, desc, to, sendTo } = req.body
        // validations
        if (!type) {
            return await validatorErrorResponse(res, "Required", "type");
        }
        if ((type && type === "schedule") && !scheduleTime) {
            return await validatorErrorResponse(res, "Required", "scheduleTime");
        }
        if (!title) {
            return await validatorErrorResponse(res, "Required", "title");
        }
        if (!desc) {
            return await validatorErrorResponse(res, "Required", "desc");
        }
        if (!sendTo) {
            return await validatorErrorResponse(res, "Required", "sendTo");
        }
        if ((sendTo && sendTo === "multiple") && !to) {
            return await validatorErrorResponse(res, "Required", "to");
        }

        // create object to store value in database
        const userIds = []
        if (sendTo === "multiple") {
            userIds = to
        } else {
            const user = await services.getData("user", { isDeleted: false }, { _id: true }, {});

        }
        const notificationObj = {
            type: type,
            title: title,
            desc: desc,
            from: adminId,
            scheduleTime: type === "now" ? new Date() : scheduleTime,
            to: userIds
        }
        return res.status(200).send({
            status: 200,
            message: 'hello',
        });
    }
    catch (e) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
}
/**
 *  transfer to bank account 
 */
const transferAmountToBank = async (req, res) => {
    try {
        const { requestId } = req.body;
        if (!requestId) {
            return validatorErrorResponse(res, "requestId required", "requestId")
        }
        const requestData = await services.getoneData("paymentRequest", { _id: requestId, status: "pending" });
        if (requestData) {
            console.log(requestData.userId);
            const user = await services.getoneData("user", { _id: requestData.userId }, { password: false });
            if (!user.isVerified) {
                return res.status(400).send({
                    status: 400,
                    message: "user bank account not verified"
                });
            }
            if (!user.stripeAccountId) {
                return res.status(400).send({
                    status: 400,
                    message: "user bank account not added"
                });
            }
            const userWallet = await services.getData("wallet", { userId: requestData.userId }, { userAmount: 1, status: 1 });
            const userTotalCreditAmount = _.sumBy(userWallet, function (o) {
                if (o.status == "credit") {
                    return parseFloat(o.userAmount);
                }
                return 0;
            });
            const userDebitAmount = _.sumBy(userWallet, function (o) {
                if (o.status == "debit") {
                    return parseFloat(o.userAmount);
                }
                return 0;
            });
            const userPendingAmount = userTotalCreditAmount - userDebitAmount;
            if (requestData.amount > userPendingAmount) {
                return res.status(400).send({
                    status: 400,
                    message: "requested amount cannot be greater then wallet amount."
                });
            }

            const transfer = await stripe.transfers.create({
                amount: requestData.amount * 100,
                currency: 'usd',
                destination: user.stripeAccountId
            });

            if (transfer.id) {
                await services.InsertData("wallet", {
                    userId: requestData.userId,
                    status: "debit",
                    userAmount: requestData.amount,
                    amount: requestData.amount,
                    transferId: transfer.id
                });
                await wallet.updateUserWallet(requestData.userId);
                await services.updateData(
                    "paymentRequest",
                    { _id: requestId },
                    { status: "completed" },
                    { new: true }
                );
                return res.status(200).send({
                    status: 200,
                    message: "transfer amount successfully"
                });
            } else {
                return res.status(400).send({
                    status: 400,
                    message: "transfer failed."
                });
            }
        } else {
            return res.status(400).send({
                status: 400,
                message: "invalid request"
            });
        }
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

//donation received
const donationReceived = async (req, res) => {
    try {
        //fetch data from request
        let { query, limit, page, } = req.body
        let criteria = { status: 'succeeded' };
        let perPage = limit ? limit : "10";
        let pageNo = Math.max(0, page);
        //for pagination
        let startIndex = (pageNo * perPage) - perPage
        let lastIndex = (pageNo * perPage)
        let options = {
            lean: true,
            sort: { createdAt: -1 },
        };
        // populate data from reference table
        let populateQuery = [
            {
                path: "userId",
                select: ["name", "email", "phoneNumber"],
            },
            {
                path: "donatedBy",
                select: ["name", "email", "phoneNumber"],
            },
            {
                path: "videoId",
                select: ["videoCaption", "videoImage", "videoLink"],
            },
        ];
        // fetch data from database
        let finalData = []
        const transactions = await services.findWithPopulate("transaction", criteria, {}, options, populateQuery);
        finalData = transactions
        query = query && query.toLowerCase()
        if (finalData.length > 0 && query) finalData = finalData.filter(item => item.userId && (item.userId.name.indexOf(query) === 0) || item.userId && (item.userId.email.indexOf(query) === 0) || item.userId && (item.userId.phoneNumber.indexOf(query) === 0) || item.videoId && (item.videoId.videoCaption.indexOf(query) === 0) || item.donatedBy && (item.donatedBy.name.indexOf(query) === 0))
        let numPages = Math.ceil(parseInt(finalData.length) / perPage);
        // response
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: finalData.slice(startIndex, lastIndex),
            current: pageNo,
            previous: pageNo > 1 ? pageNo - 1 : undefined,
            next: pageNo < numPages ? parseInt(pageNo) + parseInt(1) : undefined,
            totalData: finalData.length,
            totalPages: numPages,
        });

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

// Add category
const addCategory = async (req, res) => {
    try {
        // fetch data from request
        const { name } = req.body;
        // validations
        if (!name) {
            return validatorErrorResponse(res, "Required", "name")
        }
        if (!req.files) {
            return validatorErrorResponse(res, "Required", "image")
        }
        //inialize object
        let categoryData = {}
        categoryData.name = name
        // uploading image to aws
        if (req.files && req.files.image) {
            const file = req.files.image;
            const uploadImage = await uploadFileToAws(file);
            categoryData.image = uploadImage.fileUrl;
        }
        //insert category
        const category = await services.InsertData("category", categoryData);
        return res
            .status(200)
            .send({ status: 200, message: "Category Added", data: category });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

// edit category
const editCategory = async (req, res) => {
    try {
        // fetch data from request
        const { name, categoryId } = req.body;
        // validations
        if (!categoryId) {
            return validatorErrorResponse(res, "Required", "categoryId")
        }
        if (!name) {
            return validatorErrorResponse(res, "Required", "name")
        }
        let criteria = { _id: categoryId }
        //inialize object
        let categoryData = {}
        categoryData.name = name
        // uploading image to aws
        if (req.files && req.files.image) {
            const file = req.files.image;
            const uploadImage = await uploadFileToAws(file);
            categoryData.image = uploadImage.fileUrl;
        }
        //updade category
        await services.updateData("category", criteria, categoryData, { new: true, });
        return res
            .status(200)
            .send({ status: 200, message: "Category updated" });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

// get categories
const getCategories = async (req, res) => {
    try {
        // fetch data from request
        let { query, limit, page, } = req.body
        let criteria = { isDeleted: false };
        //search data
        if (query) {
            criteria.$or = [
                { name: { $regex: query, $options: "i" } },
            ];
        }
        // paginations
        let perPage = limit ? limit : "10";
        let pageNo = Math.max(0, page);
        let options = {
            skip: parseInt(perPage * (pageNo - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: { createdAt: -1 },
        };
        // fetch categories from database
        const categories = await services.getData("category", criteria, {}, options);
        const categoriesLength = await services.getData("category", criteria, {}, {});
        let numPages = Math.ceil(parseInt(categoriesLength.length) / perPage);
        //send response
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: categories,
            current: pageNo,
            previous: pageNo > 1 ? pageNo - 1 : undefined,
            next: pageNo < numPages ? parseInt(pageNo) + parseInt(1) : undefined,
            totalData: categoriesLength.length,
            totalPages: numPages,
        });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

// delete category
const deleteCategory = async (req, res) => {
    try {
        // fetch data from request
        const { categoryId } = req.body;
        const categoryModelName = "category"
        // validations
        if (!categoryId) {
            return validatorErrorResponse(res, "Required", "categoryId")
        }

        let criteria = { _id: categoryId }

        //delete category
        await services.updateData(categoryModelName, criteria, { isDeleted: true }, { new: true, });
        return res
            .status(200)
            .send({ status: 200, message: "Category deleted" });
    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

//withdrawal request
const withdrawalRequest = async (req, res) => {
    try {
        //fetch data from request
        let { query, limit, page, } = req.body
        let criteria = { status: 'pending' };
        let perPage = limit ? limit : "10";
        let pageNo = Math.max(0, page);
        //for pagination
        let startIndex = (pageNo * perPage) - perPage
        let lastIndex = (pageNo * perPage)
        let options = {
            lean: true,
            sort: { createdAt: -1 },
        };
        // populate data from reference table
        let populateQuery = [
            {
                path: "userId",
                select: ["name", "email", "phoneNumber"],
            },
        ];
        // fetch data from database
        let finalData = []
        const paymentRequest = await services.findWithPopulate("paymentRequest", criteria, {}, options, populateQuery);
        finalData = paymentRequest
        query = query && query.toLowerCase()
        if (finalData.length > 0 && query) finalData = finalData.filter(item => item.userId && (item.userId.name.indexOf(query) === 0) || item.userId && (item.userId.email.indexOf(query) === 0) || item.userId && (item.userId.phoneNumber.indexOf(query) === 0))
        let numPages = Math.ceil(parseInt(finalData.length) / perPage);
        // response
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: finalData.slice(startIndex, lastIndex),
            current: pageNo,
            previous: pageNo > 1 ? pageNo - 1 : undefined,
            next: pageNo < numPages ? parseInt(pageNo) + parseInt(1) : undefined,
            totalData: finalData.length,
            totalPages: numPages,
        });

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

//transferred transactions
const transferredTransactions = async (req, res) => {
    try {
        //fetch data from request
        let { query, limit, page, } = req.body
        let criteria = { status: 'debit' };
        let perPage = limit ? limit : "10";
        let pageNo = Math.max(0, page);
        //for pagination
        let startIndex = (pageNo * perPage) - perPage
        let lastIndex = (pageNo * perPage) - 1
        let options = {
            lean: true,
            sort: { createdAt: -1 },
        };
        // populate data from reference table
        let populateQuery = [
            {
                path: "userId",
                select: ["name", "email", "phoneNumber"],
            },
            {
                path: "videoId",
                select: ["videoCaption", "videoImage", "videoLink"],
            },
        ];
        // fetch data from database
        let finalData = []
        const transferredTransactions = await services.findWithPopulate("wallet", criteria, {}, options, populateQuery);
        finalData = transferredTransactions
        query = query && query.toLowerCase()
        if (finalData.length > 0 && query) finalData = finalData.filter(item => item.userId && (item.userId.name.indexOf(query) === 0) || item.userId && (item.userId.email.indexOf(query) === 0) || item.userId && (item.userId.phoneNumber.indexOf(query) === 0) || item.videoId && (item.videoId.videoCaption.indexOf(query) === 0))
        let numPages = Math.ceil(parseInt(finalData.length) / perPage);
        // response
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: finalData.slice(startIndex, lastIndex),
            current: pageNo,
            previous: pageNo > 1 ? pageNo - 1 : undefined,
            next: pageNo < numPages ? parseInt(pageNo) + parseInt(1) : undefined,
            totalData: finalData.length,
            totalPages: numPages,
        });

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

//admin commission
const adminCommission = async (req, res) => {
    try {
        //fetch data from request
        let { query, limit, page, } = req.body
        let criteria = { status: 'credit' };
        let perPage = limit ? limit : "10";
        let pageNo = Math.max(0, page);
        //for pagination
        let startIndex = (pageNo * perPage) - perPage
        let lastIndex = (pageNo * perPage)
        let options = {
            lean: true,
            sort: { createdAt: -1 },
        };
        // populate data from reference table
        let populateQuery = [
            {
                path: "userId",
                select: ["name", "email", "phoneNumber"],
            },
            {
                path: "donatedBy",
                select: ["name", "email", "phoneNumber"],
            },
            {
                path: "videoId",
                select: ["videoCaption", "videoImage", "videoLink"],
            },
        ];
        // fetch data from database
        let finalData = []
        const adminCommision = await services.findWithPopulate("wallet", criteria, {}, options, populateQuery);
        finalData = adminCommision
        query = query && query.toLowerCase()
        if (finalData.length > 0 && query) finalData = finalData.filter(item => item.userId && (item.userId.name.indexOf(query) === 0) || item.userId && (item.userId.email.indexOf(query) === 0) || item.userId && (item.userId.phoneNumber.indexOf(query) === 0) || item.videoId && (item.videoId.videoCaption.indexOf(query) === 0) || item.donatedBy && (item.donatedBy.name.indexOf(query) === 0))
        let numPages = Math.ceil(parseInt(finalData.length) / perPage);
        // response
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: finalData.slice(startIndex, lastIndex),
            current: pageNo,
            previous: pageNo > 1 ? pageNo - 1 : undefined,
            next: pageNo < numPages ? parseInt(pageNo) + parseInt(1) : undefined,
            totalData: finalData.length,
            totalPages: numPages,
        });

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

/**
 * Workflow of below method
 *  - Fetch pending Amount from DB
 *  - It will RETURN : 
 *    - x, y, z
 */
const pendingTransactions = async (req, res) => {
    try {
        //fetch data from request
        let { query, limit, page } = req.body
        let criteria = { isDeleted: false, walletCreditAmount: { $gt: 0 } };
        if (query) {
            //for search
            criteria.$or = [
                { userName: { $regex: query, $options: "i" } },
                { email: { $regex: query, $options: "i" } },
                { phoneNumber: { $regex: query, $options: "i" } },
            ];
        }
        // for pagination
        let perPage = limit ? limit : "10";
        let pageNo = Math.max(0, page);
        let options = {
            skip: parseInt(perPage * (pageNo - 1)),
            limit: parseInt(perPage),
            lean: true,
            sort: { createdAt: -1 },
        };
        const pendingTransactions = await services.getData("user", criteria, { password: false, __v: false }, options);
        const pendingTransactionsLength = await services.getData("user", criteria, { password: false, __v: false }, {});
        let numPages = Math.ceil(parseInt(pendingTransactionsLength.length) / perPage);
        return res.status(200).send({
            status: 200,
            message: "Success.",
            data: pendingTransactions,
            current: pageNo,
            previous: pageNo > 1 ? pageNo - 1 : undefined,
            next: pageNo < numPages ? parseInt(pageNo) + parseInt(1) : undefined,
            totalData: pendingTransactionsLength.length,
            totalPages: numPages,
        });

    } catch (error) {
        if (error) {
            console.log(error);
            sendError(error, res);
        }
    }
};

module.exports = {
    adminLogin,
    adminSignUp,
    changePassword,
    getAdminProfile,
    editProfile,
    getUserList,
    getDonationSetting,
    donationSetting,
    addVideoAudio,
    addGenre,
    getGenre,
    getVideoAudioList,
    userBlockUnblock,
    addSetting,
    getGernalSettingByType,
    getGernalSetting,
    editGeneralSettings,
    reportedVideos,
    verifyUnVerifyUser,
    getAllVerifiedUser,
    dashboard,
    ReportedVideoDetail,
    blockUnblockVideo,
    ignoreReportedVideo,
    getSearchUsers,
    getGraphData,
    filterVerifiedUnverified,
    pushNotication,
    transferAmountToBank,
    donationReceived,
    addCategory,
    getCategories,
    editCategory,
    deleteCategory,
    withdrawalRequest,
    transferredTransactions,
    adminCommission,
    pendingTransactions,
}
