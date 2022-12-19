const mongoose = require("mongoose");
const paymentSettingSchema = new mongoose.Schema(
    {
        adminCommision: {
            type: String,
            default: "",
        },
    },
    { timestamps: true }
);

const paymentSetting = mongoose.model("paymentSetting", paymentSettingSchema);
module.exports = paymentSetting;
