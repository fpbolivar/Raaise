const DB_URL = "mongodb+srv://ScripTube:ScripTube@cluster0.46czdvo.mongodb.net/?retryWrites=true&w=majority";
const SECRET_JWT_CODE = "secret_this_should_be_longer1234"
const USER_JWT_CODE = "hfevghfjjj"
// aws bucket  details
const AWS_BUCKET_NAME = "scriptube-s3";
const AWS_ACCESS_KEY_ID = "AKIA334AKNTBOWRYNQ5J";
const AWS_SECRET_KEY_ID = "lOQ0XONSftgxIbxMK+8ThMHImt4L88HkDgWsv1xx";
const STRIPE = {
    SK_dev: "sk_test_51JiJTBBks3ZH5n96kkpcitGlxYTzU8IYkWQ9YbSET272oZEGyseXpAm47b7aYvLotxKmJOCaQTIx43QKpQhRmwFb00yeqIdXBv",
    PK_dev: "pk_test_51JiJTBBks3ZH5n969KjtyFh8EQLT4muRAHIodbU8de1Ls816qFdsijS0yZPoE3eqZnShAyLwYkj0I6u6FmFz1rbe00LxSvMpgY",
    // PK_dev:"pk_test_qPYYGMdKszRH2E8L5BeG2Zs3",
    // SK_dev:"sk_test_gXwcRHpVAUrGpFH1XY01GvtB"
}

//FCM
const FCM_URL = "https://fcm.googleapis.com/fcm/send"
const FCM_AUTH_TOKEN = 'key=AAAAWgFK-lM:APA91bG6FlDdaW20CuNG5cE8iCE_UIa5iLiMaKMOCPvvU75sbMFVbllBklWA4I2ynShv30f0IuoyCyWOBry824bJEZbowki-p4P34mkbdl2Lh_If0AUS7CFl1cQ-Lk18ai0l2Cg1QKu4'

module.exports = { DB_URL, SECRET_JWT_CODE, USER_JWT_CODE, AWS_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY_ID, STRIPE, FCM_URL, FCM_AUTH_TOKEN }
