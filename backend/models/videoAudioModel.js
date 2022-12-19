const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const videoAudioSchema = new Schema({
    slug: {
        type: String,
        required: true
    },
    songName: {
        type: String,
        required: true,
    },
    artistName: {
        type: String,
        required: false,
        default: ""
    },
    Thumbnail: {
        type: String,
        required: false,
        default: ""
    },
    audio: {
        type: String,
        required: true
    },
    audioTime: {
        type: String,
        required: false,
        default: ""
    },
    genreId: {
        type: Schema.Types.ObjectId,
        required: false,
        ref: "genreModal",
    },

},
    { timestamps: true }
);

const videoAudio = mongoose.model('videoAudio', videoAudioSchema)
module.exports = videoAudio;

