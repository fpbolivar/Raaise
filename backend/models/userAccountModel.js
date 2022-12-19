const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const userAccountSchema = new mongoose.Schema({
    userId: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user",
    },
    status: {
        type: String,
        default: "review" //  review, confirmed, error
    },
    message: {
        type: String,
        default: "" //  for handle error and success message
    },
    bankName: {
        type: String,
        default: ""
    },
    accountNumber: {
        type: String,
        default: ""
    },
    routingNumber: {
        type: String,
        default: ""
    }
},
    { timestamps: true }
)

const userAccount = mongoose.model('userAccount', userAccountSchema)
module.exports = userAccount;