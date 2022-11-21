const { STATUS_MSG } = require("../config/AppConstants");
const SUCCESS = STATUS_MSG.SUCCESS;
const ERROR = STATUS_MSG.ERROR;

const sendError = (error, res) => {
    res.send({ error: true, message: ERROR.APP_ERROR });
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


module.exports = { sendError, successResponse, validatorErrorResponse }
