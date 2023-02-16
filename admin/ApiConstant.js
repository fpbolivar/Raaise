import c from "./utils/Constants";
module.exports = {
    AUTH: {
        signIn: `${c.API_BASE_URL}/admin/login`,
        changePassword: `${c.API_BASE_URL}/admin/change-password`,
        getAdmin: `${c.API_BASE_URL}/admin/get-admin-profile`,
        editAdmin: `${c.API_BASE_URL}/admin/edit-admin-profile`,
        donationSetting: `${c.API_BASE_URL}/admin/donation-setting`,
        getDonationSetting: `${c.API_BASE_URL}/admin/get-donation-setting`,

    },
    DASHBOARD: {
        getDashboardData: `${c.API_BASE_URL}/admin/dashboard`,
        getGraphData: `${c.API_BASE_URL}/admin/get-graph-data`,

        },
    USERS: {
        getUsers: `${c.API_BASE_URL}/admin/get-user-list`,
        verifyUnVerifyUser: `${c.API_BASE_URL}/admin/verify-unverify-user`,
        filterVerifiedUnVerifiedUser: `${c.API_BASE_URL}/admin/filter-verified-unverified-users`,
        getVerifiedUser: `${c.API_BASE_URL}/admin/get-verified-user`,
        searchUser: `${c.API_BASE_URL}/admin/search-user`,
        userDelete: `${c.API_BASE_URL}/admin/api/delete-user`,
        userBlockUnblock: `${c.API_BASE_URL}/admin/user-block-unblock`,
    },
    AUDIO: {
        getGenre: `${c.API_BASE_URL}/admin/get-genre`,
        uploadAudio: `${c.API_BASE_URL}/admin/add-video-audio`,
        getAudioList: `${c.API_BASE_URL}/admin/get-video-audio-list`,
        deleteAudio: `${c.API_BASE_URL}/admin/delete-video-audio`,
    },
    GENERALSETTING:{
        getlisting: `${c.API_BASE_URL}/admin/get-general-settings`,
        getSettingByType :`${c.API_BASE_URL}/admin/general-settings-by-type`,
        editGeneralSetting:`${c.API_BASE_URL}/admin/edit-general-settings`,

    },
    VIDEO: {
        getAllVideoList: `${c.API_BASE_URL}/admin/reported-videos`,
        reportedVideo:`${c.API_BASE_URL}/admin/reported-video-detail`,
        blockUnblockVideo:`${c.API_BASE_URL}/admin/block-unblock-video`,
        ignoreVideo:`${c.API_BASE_URL}/admin/ignore-reported-video`,
        addVideoCategory:`${c.API_BASE_URL}/admin/add-category`,
        getVideoCategories:`${c.API_BASE_URL}/admin/get-categories`,
        editVideoCategory:`${c.API_BASE_URL}/admin/edit-category`,
        deleteVideoCategory:`${c.API_BASE_URL}/admin/delete-category`,
    },
    DONATION:{
        donationReceived: `${c.API_BASE_URL}/admin/donation-received`,
        withdrawalRequest:`${c.API_BASE_URL}/admin/withdrawal-request`,
        transferredTransaction:`${c.API_BASE_URL}/admin/transferred-transactions`,
        adminCommission:`${c.API_BASE_URL}/admin/admin-commission`,
        pendingTransaction:`${c.API_BASE_URL}/admin/pending-transactions`,
        payWithdrawalAmount:`${c.API_BASE_URL}/admin/transfer-amount-to-bank`,
        payPendingTransaction:`${c.API_BASE_URL}/admin/transfer-all-amount-to-user-bank`,

    
    },
    NOTIFICATION:{
        getNotification :`${c.API_BASE_URL}/admin/get-notifications`,
        pushNotification:`${c.API_BASE_URL}/admin/push-notication`,
        logNotification:`${c.API_BASE_URL}/admin/get-notifications-logs`,
    }
};
