const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const videoLikesModelSchema = new mongoose.Schema(
    {
        videoId: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "userVideo",
        },
        userId: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },
        videoOwnerId: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },
    },
    { timestamps: true }
);

const videoLikesModel = mongoose.model("videoLikesModel", videoLikesModelSchema);
module.exports = videoLikesModel;
