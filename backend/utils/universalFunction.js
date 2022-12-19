const { STATUS_MSG } = require("../config/AppConstants");
const SUCCESS = STATUS_MSG.SUCCESS;
const ERROR = STATUS_MSG.ERROR;
const moment = require('moment')
const AWS = require("aws-sdk");
const service = require("../services/services");
const { AWS_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY_ID } = require('../config/Config')

const axios = require('axios');

const sendError = (error, res) => {
    // res.send({ error: true, message: ERROR.APP_ERROR });
    res.status(200).send({
        status: 500,
        result: false,
        message: ERROR.APP_ERROR.customMessage
    });
    return

};

const successResponse = (res, data, successMsg) => {
    const responceSuccessMsg = successMsg ? successMsg : SUCCESS.DEFAULT;
    res.send({
        status: responceSuccessMsg.statusCode,
        message: responceSuccessMsg.customMessage,
        data: data,
    });
    return;
};

const validatorErrorResponse = (res, msg, param) => {
    res.status(200).json({
        status: 422,
        result: false,
        errors: {
            message: msg,
            param: param,
        },
    });
    return;
};

let faceBookLoginApi = async (res, token) => {
    return new Promise(async (resolve, reject) => {
        try {

            var config = {
                method: 'get',
                url: `https://graph.facebook.com/me?fields=id,name,email,picture&access_token=${token}`,
                headers: {}
            };

            let data = await axios(config)
            resolve(data.data)

        }
        catch (error) {
            return res.status(200).json({
                status: 422,
                result: false,
                errors: {
                    message: "Invaild Token",
                    param: "token",
                },
            });

        }

    })

}

let generateUserName = async () => {
    const modelName = "user";
    let currDate = moment();
    let month = currDate.format("MM");
    let day = currDate.format("DD");
    let year = currDate.format("YYYY");
    let randomNumber = Math.floor(Math.random() * 100000)
        .toString()
        .substring(0, 4);

    let uniqueId = "user" + randomNumber;

    let finddata = {
        userName: uniqueId,
    };

    const finduniqueId = await service.getData(modelName, finddata);

    if (finduniqueId.length > 0) {
        uniqueId = uniqueId + "-" + finduniqueId.length;
    }
    return uniqueId;
};


let googleLoginApi = async (res, token, deviceType) => {
    return new Promise(async (resolve, reject) => {
        try {
            var config = {
                method: 'get',
                url: deviceType === 'ios' ? `https://www.googleapis.com/oauth2/v3/userinfo?access_token=${token}` : `https://oauth2.googleapis.com/tokeninfo?id_token=${token}`,
                headers: {
                    'Content-Type': 'application/json',
                    'Accept-Encoding': 'application/json',
                }
            };

            let data = await axios(config)
            console.log("===========GOOGLE LOGIN RESPONSE===========", JSON.stringify(data.data));
            resolve(data.data)
        }
        catch (error) {
            console.log("===========GOOGLE LOGIN RESPONSE===========", error)
            return res.status(200).json({
                status: 422,
                result: false,
                errors: {
                    message: "Invaild Token",
                    param: "token",
                },
            });
        }
    })
}

let generateToken = async (length) => {
    var result = "IH";
    var characters =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    var charactersLength = characters.length;
    for (var i = 0; i < length; i++) {
        result += characters.charAt(
            Math.floor(Math.random() * charactersLength)
        );
    }

    return result;
};

const uploadFileToAws = async (file, path) => {
    const s3 = new AWS.S3({
        accessKeyId: AWS_ACCESS_KEY_ID,
        secretAccessKey: AWS_SECRET_KEY_ID,
    });
    if (!path) {
        path = "user/"
    }

    let removeSpace = file.name.replace(/\s/g, "");
    const fileName = `${new Date().getTime()}_${removeSpace}`;

    const mimetype = file.mimetype;
    const params = {
        Bucket: AWS_BUCKET_NAME,
        Key: path + fileName,
        Body: file.data,
        ContentType: mimetype,
    };
    const res = await new Promise((resolve, reject) => {
        s3.upload(params, (err, data) =>
            err == null ? resolve(data) : reject(err)
        );
    });
    return { fileUrl: res.Location };
};

let generateUniqueId = async (tableName, slug) => {
    const modelName = tableName;
    let currDate = moment();
    let month = currDate.format("MM");
    let day = currDate.format("DD");
    let year = currDate.format("YYYY");
    let randomNumber = Math.floor(Math.random() * 100000)
        .toString()
        .substring(0, 4);

    let uniqueId = month + day + year + randomNumber;

    let finddata = {
        slug: uniqueId,
    };

    const finduniqueId = await service.getData(modelName, finddata);

    if (finduniqueId.length > 0) {
        uniqueId = uniqueId + "-" + finduniqueId.length;
    }
    return uniqueId;
};
const convertStringToObjectId = (string) => {
    var mongoose = require("mongoose");
    var id = mongoose.Types.ObjectId(string);
    return id;
};


const sripeErrorHandling = (err, res) => {
    switch (err.type) {
        case "StripeCardError":
            res.send({
                status: 500,
                result: false,
                message: err.message
            });

            break;
        case "StripeRateLimitError":
            res.send({
                status: 500,
                result: false,
                message: err.message
            });
            break;
        case "StripeInvalidRequestError":
            res.send({
                status: 500,
                result: false,
                message: err.message
            });
            break;
        case "StripeAPIError":
            res.send({
                status: 500,
                result: false,
                message: err.message
            });
            break;
        case "StripeConnectionError":
            res.send({
                status: 500,
                result: false,
                message: err.message
            });
            break;
        case "StripeAuthenticationError":
            res.send({
                status: 500,
                result: false,
                message: err.message
            });
            break;
        default:
            res.status(200).send({
                status: 500,
                result: false,
                message: ERROR.APP_ERROR.customMessage
            });
            break;
    }
};


module.exports = { sendError, successResponse, validatorErrorResponse, faceBookLoginApi, generateUserName, googleLoginApi, generateToken, uploadFileToAws, generateUniqueId, convertStringToObjectId, sripeErrorHandling }
