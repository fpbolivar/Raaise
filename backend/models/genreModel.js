const mongoose = require("mongoose");
const genreModalSchema = new mongoose.Schema(
    {
        name: {
            type: String,
            default: "",
        },
    },
    { timestamps: true }
);

const genreModal = mongoose.model("genreModal", genreModalSchema);
module.exports = genreModal;
