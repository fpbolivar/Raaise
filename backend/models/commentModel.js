const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const commentModelSchema = new mongoose.Schema(
    {
        comment: {
            type: String,
            required: true,
        },
        videoId: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "userVideo",
        },
        replyId: [{
            type: Schema.Types.ObjectId,
            ref: "commentReply",
            default: []
        }],
        commentBy: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },


    },
    { timestamps: true }
);

const commentModel = mongoose.model("commentModel", commentModelSchema);
module.exports = commentModel;
