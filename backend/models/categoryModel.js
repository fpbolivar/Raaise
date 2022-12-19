const mongoose = require('mongoose')
const categorySchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    image: {
        type: String,
        required: false
    },
    isDeleted: {
        type: Boolean,
        default: false
    }
},
    { timestamps: true }
)

const category = mongoose.model('category', categorySchema)
module.exports = category;