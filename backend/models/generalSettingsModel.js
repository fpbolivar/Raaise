const mongoose = require("mongoose");
const generalSettingsSchema = new mongoose.Schema(
  {
    type: {
      type: String,
      require: true,
      default: "",
    },
    title: {
      type: String,
      require: true,
      default: "",
    },
    description: {
      type: String,
      require: true,
      default: "",
    },
  },
  { timestamps: true }
);

const generalSettings = mongoose.model("generalSettings",generalSettingsSchema);
module.exports = generalSettings;
