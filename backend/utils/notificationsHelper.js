const services = require("../services/services");
var axios = require('axios');
const {FCM_URL,FCM_AUTH_TOKEN} = require('../config/Config')

const notificationHelper = async (type,notififictionData) => {
    let { to,from,videoId } = notififictionData;
    let desc = "";
    let title = "";
    let notifyModel = "notification";
    let userModel = "user";
    //let userToken
    // let userToken ="f1e3djGAT4GnJgxHZ_eWmx:APA91bFcI_GxxfzazLZHZKnArjg6TM_KD0CUOLlOLHEVhCvHgiip6Hcy0b_Gyfw4a8oH6PaxDWTO7GQC-xPa3o0AImQv6KdaKfITTPbUkLjQi5IMryEncNDYsQ5C9MogClxuC9aLTjyN"
    
    /**
     * here according to type we get we pass message
     */
    if(type == "like"){
        title = "Like notifictaions";
        desc = "likes your video";
        const notifyCriteria = {to: to, from:from, videoId:videoId, type: type};
        const singleNotification = await services.getoneData(notifyModel,notifyCriteria,{},{});
        if(singleNotification && singleNotification._id){
            const dropCriteria = {_id:singleNotification._id};
            await services.dropIndex(notifyModel,dropCriteria);//insert data of notification 
            return true;
        }
    }else if(type === "comment"){
        title = "Comment notifictaions";
        desc = "commented on your video";
    }else if(type === "addVideo"){
        title = "Add video notifictaions";
        desc = "added a new video";
    }
    // fetch video user details
    const userDetail = await services.getoneData(userModel,{_id:to},{deviceToken:true, pushNotification:true},{});
    console.log(userDetail,"userDetail")
    console.log(to,"id")
    if(userDetail && userDetail.deviceToken && userDetail.pushNotification){
        // notification json data 
        let notifyJson = JSON.stringify({
            "to": userDetail.deviceToken,
            "priority": "high",
            "notification": {
                "body": desc,
                "title": title,
                "sound": "default"
            }
        });
        // FCM configuration detail
        let fcmConfig = {
            method: "post",
            url:FCM_URL,
            headers: { 
                "Authorization":FCM_AUTH_TOKEN,
                "Content-Type": "application/json",
            },
            data : notifyJson
        };
        // send notification
        await axios(fcmConfig);
    }else{
        console.log("=========USER DEVICE TOKEN IS MISSING=========");
    }
    // insert notification data
    let notifyData={ type:type, title:title, desc:desc, to:to, from:from, isRead:false, isSend:true ,videoId:videoId};
    await services.InsertData(notifyModel, notifyData);
}

module.exports={notificationHelper}