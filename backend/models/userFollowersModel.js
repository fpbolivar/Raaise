const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const userFollowersModelSchema = new mongoose.Schema(
    {
        followTo: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },
        followedBy: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },
    },
    { timestamps: true }
);

const userFollowersModel = mongoose.model("userFollowersModel", userFollowersModelSchema);
module.exports = userFollowersModel;
