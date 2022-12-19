const cron = require("node-cron");
const { reminderJobs } = require("./jobs");

const businessShowerReminder = cron.schedule("* * * * *", async () => {
    try {
        // reminderJobs().then();
    } catch (error) {}
});
module.exports = {
    businessShowerReminder,
};
