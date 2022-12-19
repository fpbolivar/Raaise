const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const videosViewModelSchema = new mongoose.Schema(
    {
        viewedBy: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "user",
        },
        videoId: {
            type: Schema.Types.ObjectId,
            required: true,
            ref: "userVideo",
        },
    },
    { timestamps: true }
);

const videosViewModel = mongoose.model("videosViewModel", videosViewModelSchema);
module.exports = videosViewModel;
