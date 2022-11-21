const bcrypt = require("bcrypt");
var jwt = require('jsonwebtoken');
const saltRounds = 10;
const services = require("../services/services");
const { validatorErrorResponse } = require("../utils/universalFunction");
const { sendError } = require("../utils/universalFunction");
const { successResponse } = require("../utils/universalFunction");
const { SECRET_JWT_CODE } = require("../config/Config");


const adminLogin = async (req, res) => {

    try {

        const { email, password } = req.body;
        if (!email) {
            return await validatorErrorResponse(res, "email required", "email")
        }
        if (!password) {
            return await validatorErrorResponse(res, "password required", "password")
        }

        let admin = await services.getoneData("admin", { email: email })

        if (!admin) {
            return await validatorErrorResponse(res, "Invalid email", "email")
        }

        var match = await bcrypt.compare(
            password,
            admin.password
        );
        if (match) {

            const token = jwt.sign({ adminId: admin._id, name: admin.name, email: admin.email }, SECRET_JWT_CODE)
            return res.status(200).send({ status: 200, message: "login successful", token: token })
        } else {
            return await validatorErrorResponse(res, "Invalid password", "password")
        }
    } catch (error) {
        if (error) {
            sendError(error, res)
        }
    }
};


const adminSignUp = async (req, res) => {
    try {
        const { name, email, password } = req.body;
        if (!name) {
            return await validatorErrorResponse(res, "name required", "name")
        }
        if (!email) {
            return await validatorErrorResponse(res, "email required", "email")
        }
        if (!password) {
            return await validatorErrorResponse(res, "password required", "password")
        }
        const admin = await services.getoneData("admin", { email: email })

        if (admin) {
            return res.status(200).send({ status: 401, message: "Email already exists", error: true, params: "email" })
        }

        const hashPassword = await bcrypt.hash(password, saltRounds);

        const admindata = await services.InsertData("admin", {
            name: name,
            email: email,
            password: hashPassword,
        });

        const token = jwt.sign({ adminId: admindata._id, email: email }, SECRET_JWT_CODE)

        return res.status(200).send({ status: 200, message: "Admin Registered", token: token });
    } catch (error) {
        if (error) {
            sendError(error, res)
            console.log(error)
        }
    }
};


const changePassword = async (req, res) => {
    try {
        const { adminId } = req.admin;

        const { oldpassword, newpassword, confirmpassword } = req.body;

        if (!oldpassword) {
            return await validatorErrorResponse(res, "oldpassword required", "oldpassword")
        }
        if (!newpassword) {
            return await validatorErrorResponse(res, "newpassword required", "newpassword")
        }
        if (!confirmpassword) {
            return await validatorErrorResponse(res, "confirmpassword required", "confirmpassword")
        }
        const admin = await services.getoneData("admin", { _id: adminId })
        var match = await bcrypt.compare(
            oldpassword,
            admin.password
        );
        if (!match) {
            return await validatorErrorResponse(res, "check your old password ", "oldpassword")
        }

        if (newpassword != confirmpassword) {
            return await validatorErrorResponse(res, "Your new password or  confirm password must be same", "newpassword or confirm")
        }

        const hashPassword = await bcrypt.hash(newpassword, saltRounds);
        let admindata = await services.updateData("admin", {
            _id: adminId
        }, {
            password: hashPassword,
        });
        successResponse(res, "Password changed")

    } catch (error) {
        if (error) {
            sendError(error, res)
        }
    }
}


const editProfile = async (req, res) => {
    try {
        const { adminId } = req.admin;
        const { name, email } = req.body;

        let admindata = {}
        if (name) {
            admindata.name = name
        }
        if (email) {
            admindata.email = email
        }

        const admin = await services.updateData("admin", { _id: adminId },
            admindata, {
            new: true, projection: {
                password: false
            }
        })

        return res.status(200).send({ status: 200, message: "prfile updated", data: admin })

    } catch (error) {
        console.log(error)
    }
}


const getAdminProfile = async (req, res) => {
    try {
        const { adminId } = req.admin;

        const admin = await services.getoneData("admin", { _id: adminId }, { name: true, email: true })
        successResponse(res, admin)

    } catch (error) {
        if (error) {
            console.log(error)
            sendError(error, res)
        }
    }
}

const getUserList = async (req, res) => {
    try {

        const user = await services.getData("user", {}, { password: false })
        successResponse(res, user)

    } catch (error) {
        if (error) {
            console.log(error)
            sendError(error, res)
        }
    }
}


module.exports = { adminLogin, adminSignUp, changePassword, getAdminProfile, editProfile, getUserList };
