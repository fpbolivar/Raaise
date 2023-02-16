const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const pushNotificationSchema = new mongoose.Schema({
    type: {
        type: String,
        required: "" // now or schedule,
    },
    title: {
        type: String,
        default: "" // title of the notification
    },
    desc: {
        type: String,
        required: "" // description  or body
    },
    to: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "user", // Receiver id
        default: []
    },
    from: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "admin", // sender id
    },
    scheduleTime: {
        type: Date,
        required: true,
    },
    isRead: {
        type: Boolean,
        required: false,
        default: false
    },
    /**
     * isSend for Schedule its default false when notification send value will be true
     * its use for other notification as well
     */
    isSend: {
        type: Boolean,
        required: false,
        default: false

    },
},
    { timestamps: true }
)

const pushNotification = mongoose.model('pushNotification', pushNotificationSchema)
module.exports = pushNotification;