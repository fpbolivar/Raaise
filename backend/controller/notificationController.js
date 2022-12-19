const services = require("../services/services");

/**
 * Get Notification api for app
 */
const getNotificationforUser = async (req,res)=>{
    try{
        const { userId } = req.user // get logged user Id
        /** 
         * Condition ==> user id matched with logged userId and isRead is false
         */
        let notificationMessage = []
        console.log("userId",userId)
        const getUserNotification = await services.getData("notification",{from:userId},{},{})
        for(let i=0;i<getUserNotification.length;i++){
            /**
             * Get user details who are like , comment etc push value to array of object
             */
            var likedByUserDetail=await services.getData("user",{_id:getUserNotification[i].from},{password:false},{})
            let obj = {type:getUserNotification[i].type,message:"",createdAt:getUserNotification[i].createdAt,videoId:getUserNotification[i].videoId}
            for(let j=0;j<likedByUserDetail.length;j++){
                 obj.message=likedByUserDetail[j].userName +" " + getUserNotification[i].desc
                 notificationMessage.push(obj)
            }
         }
        return res.status(200).send({ 
            status: 200,
            message: "Success",
            notificationMessage:notificationMessage
        });
    }catch(error){
        if(error){
            console.log("=====ERROR=====",error)
            sendError(error, res);
        } 
    } 
}

/**
 * Admin Notification Api
 */


module.exports = {
    getNotificationforUser
}