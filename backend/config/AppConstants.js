let STATUS_MSG = {
    SUCCESS: {
        CREATED: {
            statusCode: 201,
            customMessage: 'Created Successfully',
            type: 'CREATED'
        },
        DEFAULT: {
            statusCode: 200,
            customMessage: 'Success',
            type: 'DEFAULT'
        },
        DELETED: {
            statusCode: 200,
            customMessage: 'Deleted Successfully',
            type: 'DELETED'
        },
        LOGOUT: {
            statusCode: 200,
            customMessage: 'Logged Out Successfully',
            type: 'LOGOUT'
        },
        UPDATED: {
            statusCode: 200,
            customMessage: 'Updated Successfully',
            type: 'UPDATED'
        },
        CHANGEPASSWORD: {
            statusCode: 200,
            customMessage: "Password Change Successfully",
            type: "change password"
        }
    },
    ERROR: {

        APP_ERROR: {
            statusCode: 500,
            customMessage: 'Application Error',
            type: 'APP_ERROR'
        },
        DB_ERROR: {
            statusCode: 400,
            customMessage: 'DB Error.',
            type: 'DB_ERROR'
        },
        DEVICE_TOKEN_ALREADY_EXISTS: {
            statusCode: 400,
            customMessage: 'Device Token already exists',
            type: 'DEVICE_TOKEN_ALREADY_EXISTS'

        },
        FIRSTTIMEISSUER: {
            statusCode: 400,
            customMessage: 'First time issuer field is required',
            type: 'FIRSTTIMEISSUER'
        },
        ISSUANCERATED: {
            statusCode: 400,
            customMessage: 'Issuance rated field is required',
            type: 'ISSUANCERATED'
        },
        EXCHANGEISSUER: {
            statusCode: 400,
            customMessage: 'Exchange Issuer field is required',
            type: 'Required'
        },
        VALIDATIONERROR: {
            statusCode: 422,
            customMessage: 'validation error',
            type: 'validation'
        },
        ADMINBLOCKERROR: {
            statusCode: 400,
            customMessage: "Your account is blocked by admin.",
            type: "ADMINBLOCK"
        },


    }

}

module.exports = {
    STATUS_MSG
}