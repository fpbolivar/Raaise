const bcrypt = require("bcrypt");
var jwt = require('jsonwebtoken');
const saltRounds = 10;
const services = require("../services/services");
const { validatorErrorResponse, sendError } = require("../utils/universalFunction");
const { SECRET_JWT_CODE, USER_JWT_CODE } = require("../config/Config");
const { modelName } = require("../models/adminModel");


const userLogin = async (req, res) => {

    try {
        const { email, password } = req.body;
        const modelName = "user"
        if (!email) {
            return await validatorErrorResponse(res, "email required", "email")
        }
        if (!password) {
            return await validatorErrorResponse(res, "password required", "password")
        }

        let user = await services.getoneData(modelName, { email: email })

        if (!user) {
            return await validatorErrorResponse(res, "Invaild email", "email")
        }

        // if admin block user 
        if (user.isBlock == true) {
            res.send({
                statusCode: 400,
                message: "Your account is blocked by admin.",
            });
            return;
        }
        let match = await bcrypt.compare(
            password,
            user.password
        );
        if (match) {
            const token = jwt.sign({ userId: user._id, email: user.email, userName: user.userName }, USER_JWT_CODE, { expiresIn: "7d" })
            return res.status(200).send({ status: 200, message: "login successful", token: token })
        } else {
            return await validatorErrorResponse(res, "Incorrect password", "password")
        }
    } catch (error) {
        if (error) {
            sendError(error, res)
        }
    }
};


const userSignUp = async (req, res) => {
    try {
        const { userName, email, password, phoneNumber } = req.body;
        const modelName = "user"
        if (!userName) {
            return await validatorErrorResponse(res, "userName required", "userName")
        }
        if (!email) {
            return await validatorErrorResponse(res, "email required", "email")
        }
        if (!password) {
            return await validatorErrorResponse(res, "password required", "password")
        }
        if (password.length < 8) {
            return await validatorErrorResponse(res, "password length must be minimum 8", "password")
        }
        if (!phoneNumber) {
            return await validatorErrorResponse(res, "phoneNumber required", "phoneNumber")
        }

        const user = await services.getoneData(modelName, { email: email })

        if (user) {
            return await validatorErrorResponse(res, "Email already exists", "email")
        }

        const checkUserName = await services.getoneData(modelName, { userName: userName })

        if (checkUserName) {
            return await validatorErrorResponse(res, "This UserName  already exists", "UserName")
        }

        const hashPassword = await bcrypt.hash(password, saltRounds);

        const userdata = await services.InsertData(modelName, {
            userName: userName,
            email: email,
            password: hashPassword,
            phoneNumber: phoneNumber
        });

        const token = jwt.sign({ userId: userdata._id, email: email, userName: userdata.userName }, USER_JWT_CODE, { expiresIn: "7d" })

        return res.status(200).send({
            status: 200,
            message: "User Registered", userName: userdata.userName,
            email: userdata.email, token: token,
        });
    } catch (error) {
        if (error) {
            sendError(error, res)
        }
    }
};


const changePassword = async (req, res) => {
    try {
        const { userId } = req.user
        const { oldpassword, newpassword, confirmpassword } = req.body;
        const modelName = "user"

        if (!oldpassword) {
            return await validatorErrorResponse(res, "oldpassword required", "oldpassword")
        }
        if (!newpassword) {
            return await validatorErrorResponse(res, "newpassword required", "newpassword")
        }
        if (!confirmpassword) {
            return await validatorErrorResponse(res, "confirmpassword required", "confirmpassword")
        }

        const user = await services.getoneData(modelName, { _id: userId })

        if (!user) {
            return await validatorErrorResponse(res, "user not Found", "newPassword")
        }

        var match = await bcrypt.compare(
            oldpassword,
            user.password
        );

        if (!match) {
            return await validatorErrorResponse(res, "check your old password ", "oldpassword")
        }
        if (newpassword.length < 8) {
            return await validatorErrorResponse(res, "newpassword length must be 8", "password")
        }
        if (newpassword != confirmpassword) {
            return await validatorErrorResponse(res, "Your new password or  confirm password must be same", "newpassword or oldpassword")
        }

        const hashPassword = await bcrypt.hash(newpassword, saltRounds);
        let userdata = await services.updateData(modelName, {
            _id: userId
        }, {
            password: hashPassword,
        });
        return res.send({ status: 200, message: "Password changed" });

    } catch (error) {
        console.log(error)
        sendError(error, res)
    }
}

module.exports = { userLogin, userSignUp, changePassword };