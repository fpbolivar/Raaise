const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const walletSchema = new mongoose.Schema({
    userId: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user",
    },
    donatedBy: {
        type: Schema.Types.ObjectId,
        required: false,
        ref: "user",
    },
    videoId: {
        type: Schema.Types.ObjectId,
        required: false,
        ref: "userVideo",
    },
    status: {
        type: String,
        default: "credit" //  credit , debit
    },
    adminAmount: {
        type: String,
        default: 0
    },
    userAmount: {
        type: String,
        default: 0
    },
    amount: {
        type: String,
        default: 0
    },
    transferId: {
        type: String,
        default: ""
    }
},
    { timestamps: true }
)

const wallet = mongoose.model('wallet', walletSchema)
module.exports = wallet;