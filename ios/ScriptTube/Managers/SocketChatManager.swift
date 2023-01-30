import Foundation
import SocketIO

class SocketChatManager {

    // MARK: - Properties
    let manager = SocketManager(socketURL: URL(string: "http://13.233.101.218:3000")!, config: [.log(false), .compress])
    var socket: SocketIOClient? = nil

    // MARK: - Life Cycle
    init() {
        //stop()
        setupSocket()
        //setupSocketEvents()
        
        print(socket?.status)
    }
    

    func stop() {
        socket?.disconnect()
        socket?.removeAllHandlers()
        
    }

    // MARK: - Socket Setup
    func setupSocket() {
        self.socket = manager.defaultSocket
    }
    func getChat(completion:@escaping(JSON)->Void){
        var any = [Any]()
        socket?.on("get-message", callback: { data, ack in
            print("alooo",JSON(data.first))
            completion(JSON(data.first))

        })
        socket?.on("send-message", callback: { data, ack in
            print("alooo2",JSON(data.first))
            completion(JSON(data.first))
        })
    }
    func listenUser(otherUser:String,completion:@escaping(JSON)->Void){
        socket?.on(AuthManager.currentUser.id , callback: { data, ack in
            print("alooo4",JSON(data.first))
            completion(JSON(data.first))
//            JSON(data.first).forEach { (message,da) in
//                any.append(da)
//            }
//           print(any.count)
        })
//        socket?.on(otherUser + AuthManager.currentUser.id, callback: { data, ack in
//            print("alooo4",JSON(data.first))
//            completion(JSON(data.first))
////            JSON(data.first).forEach { (message,da) in
////                any.append(da)
////            }
////           print(any.count)
//        })
    }
    func listenUser2(otherUser:String,completion:@escaping(JSON)->Void){
        socket?.on(otherUser + AuthManager.currentUser.id, callback: { data, ack in
            print("alooo5",JSON(data.first))
            completion(JSON(data.first))
//            JSON(data.first).forEach { (message,da) in
//                any.append(da)
//            }
//           print(any.count)
        })
    }
    func listenSlug(slug:String,completion:@escaping(JSON)->Void){
        print("LISTENCHATSLUG",slug)
        socket?.on(slug, callback: { data, ack in
            print("alooo3",JSON(data.first))
            completion(JSON(data.first))
//            JSON(data.first).forEach { (message,da) in
//                any.append(da)
//            }
//           print(any.count)
        })
    }
    func listenDelete(slug:String,completion:@escaping(String)->Void){
        print("LISTENCHATSLUGDELETE",slug)
        socket?.on("delete-chat" + slug, callback: { data, ack in
            print("alooo6",data.first as! String)
            completion(data.first as! String)
//            JSON(data.first).forEach { (message,da) in
//                any.append(da)
//            }
//           print(any.count)
        })
    }
    func connect(){
        socket?.connect()
    }
    func setupSocketEvents() {
        socket?.on(clientEvent: .connect) {data, ack in
            print("Connected")
        }
    }

    // MARK: - Socket Emits
    func register(user: String) {
        print("SOCKETDATA",user)
    
        socket?.emit("typing-chat", user){
            print("EMit SI")
        }
    }

    func send(message: [String:String]) {
        print("MESAGEPARAM",message)
        socket?.emit("send-message",message)
    }
    func deleteMsg(param:[String:String]){
        print("deleteMsg",param)
        socket?.emit("delete-chat", param){
            print("EMit SI")
        }
    }
   
}

