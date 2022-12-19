const { verifyBankAccounts } = require("../../lib/wallet");

const reminderJobs = async () => {
    console.log("==================== cronjob start ====================");
    verifyBankAccounts();
    console.log("==================== cronjob end ====================");
};
module.exports = {
    reminderJobs,
};
