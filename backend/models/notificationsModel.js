const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const notificationSchema = new mongoose.Schema({
    type: {
        type: String,
        required: true // like,comment ,message,
    },
    title: {
        type: String,
        default: false // title of the notification
    },
    desc: {
        type: String,
        required: true // description  or body
    },
    to:{
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user", // Receiver id
    },
    from:{
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user", // sender id
    },
    isRead:{
        type: Boolean,
        required: false,
        default:false 
    },
    /**
     * isSend for Schedule its default false when notification send value will be true
     * its use for other notification as well
     */
    isSend:{
        type: Boolean,
        required: false,
        default:false 

    },
    videoId:{
        type: Schema.Types.ObjectId,
        required: true,
        ref: "userVideo", // video id
    }
},
    { timestamps: true }
)

const notification = mongoose.model('notification', notificationSchema)
module.exports = notification;