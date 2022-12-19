const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const commentReplySchema = new mongoose.Schema(
    {
        reply: {
            type: String,
            required: true
        },
        replyBy: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },
        commentId: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "",
        },
    },
    { timestamps: true }
);

const commentReply = mongoose.model("commentReply", commentReplySchema);
module.exports = commentReply;
