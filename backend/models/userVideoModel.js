const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const userVideoSchema = new mongoose.Schema({
    userId: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user",
    },
    videoCaption: {
        type: String,
        required: true,
        default: ""
    },
    videoLink: {
        type: String,
        required: true,
        default: ""
    },
    audioId: {
        type: Schema.Types.ObjectId,
        required: false,
        ref: "videoAudio",
    },
    categoryId: {
        type: Schema.Types.ObjectId,
        required: false,
        ref: "category",
    },
    isDonation: {
        type: Boolean,
        default: false
    },
    donationAmount: {
        type: String,
        require: false,
        default: ""
    },
    videoImage: {
        type: String,
        require: false,
        default: ""
    },
    slug: {
        type: String,
        require: true,
    },
    videolikeCount: {
        type: Number,
        default: 0
    },
    videoCommentCount: {
        type: Number,
        default: 0
    },
    videoReportCount: {
        type: Number,
        default: 0
    },
    videoShareCount: {
        type: Number,
        default: 0
    },
    isReported: {
        type: Boolean,
        default: false
    },
    isBlock: {
        type: Boolean,
        default: false
    },
    isDeleted: {
        type: Boolean,
        default: false
    },
    videoViewCount: {
        type: Number,
        default: 0
    },
},
    { timestamps: true }
)

const userVideo = mongoose.model('userVideo', userVideoSchema)
module.exports = userVideo;