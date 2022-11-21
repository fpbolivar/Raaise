const mongoose = require('mongoose');
const userSchema = new mongoose.Schema({
    userName: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: false
    },
    phoneNumber: {
        type: String,
        required: false
    },
    deviceType: {
        type: String,
        default: "" // android , ios
    },
    loginType: {
        type: String,
        default: "register" //  google , facebook , register
    },
    profileImage: {
        type: String,
        default: ""
    },
    isBlock: {
        type: Boolean,
        default: false
    },
    isActive: {
        type: Boolean,
        default: true
    },
    userGoogleId: {
        type: String,
        default: ""
    },
    userFaceBookId: {
        type: String,
        default: ""
    }

},
    { timestamps: true }
)

const user = mongoose.model('user', userSchema)
module.exports = user;