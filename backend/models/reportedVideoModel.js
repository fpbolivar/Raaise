const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const reportedVideoSchema = new mongoose.Schema(
    {

        videoId: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "userVideo",
        },
        reportedBy: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },
        reason: {
            type: String,
            required: false,
            default: ""
        },
        adminComment: {
            type: String,
            required: false,
            default: ""
        },
        status: {
            type: String,
            required: true,
            default: "pending" // approved , reject 
        },

    },
    { timestamps: true }
);

const reportedVideo = mongoose.model("reportedVideo", reportedVideoSchema);
module.exports = reportedVideo;
