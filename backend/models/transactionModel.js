const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const modelSchema = new mongoose.Schema({
    userId: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user",
    },
    donatedBy: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user",
    },
    videoId: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "userVideo",
    },
    status: {
        type: String,
        default: "pending" // pending , failed, succeeded
    },
    transactionId: {
        type: String,
        default: "" // pending , rejected, completed
    },
    amount: {
        type: String,
        default: 0
    }
},
    { timestamps: true }
)

const transaction = mongoose.model('transaction', modelSchema)
module.exports = transaction;