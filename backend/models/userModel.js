const mongoose = require('mongoose');
const userSchema = new mongoose.Schema({
    userName: {
        type: String,
        required: false
    },
    name: {
        type: String,
        default: ""
    },
    email: {
        type: String,
        required: false,
    },
    password: {
        type: String,
        required: false
    },
    phoneNumber: {
        type: String,
        required: false,
        default: ""
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
    },
    shortBio: {
        type: String,
        default: ""
    },
    otp: {
        type: String,
        default: ""
    },
    city: {
        type: String,
        default: ""
    },
    state: {
        type: String,
        default: ""
    },
    address: {
        type: String,
        default: ""
    },
    postalCode: {
        type: String,
        default: ""
    },
    customerId: {
        type: String,
        default: ""
    },
    accountId: {
        type: String,
        default: ""
    },
    routingNumber: {
        type: String,
        default: ""
    },
    bankPhone: {
        type: String,
        default: ""
    },
    accountHolderName: {
        type: String,
        default: ""
    },
    stripeAccountId: {
        type: String,
        default: ""
    },
    walletCreditAmount: {
        type: String,
        default: 0
    },
    donatedAmount: {
        type: String,
        default: 0
    },
    walletDebitAmount: {
        type: String,
        default: 0
    },
    emailNotification: {
        type: Boolean,
        default: true
    },
    pushNotification: {
        type: Boolean,
        default: true
    },
    followersCount: {
        type: Number,
        default: 0
    },
    followingCount: {
        type: Number,
        default: 0
    },
    videoCount: {
        type: Number,
        default: 0
    },
    isVerified: {
        type: Boolean,
        default: false,
        required: true,
    },
    deviceToken: {
        type: String,
        default: "",
        required: false
    },
    donation_comment: {
        type: String,
        default: "",
    },
    isDeleted: {
        type: Boolean,
        default: false
    }
},
    { timestamps: true }
)

const user = mongoose.model('user', userSchema)
module.exports = user;