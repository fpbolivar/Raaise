const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const modelSchema = new mongoose.Schema({
    userId: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user",
    },
    status: {
        type: String,
        default: "pending" // pending, completed
    },
    amount: {
        type: String,
        default: 0
    }
},
    { timestamps: true }
)

const paymentRequest = mongoose.model('paymentRequest', modelSchema)
module.exports = paymentRequest;