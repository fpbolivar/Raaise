import Foundation
import SocketIO

class SocketChatManager {

    // MARK: - Properties
    let manager = SocketManager(socketURL: URL(string: URLHelper.SOCKET_URL)!, config: [.log(false), .compress])
    var socket: SocketIOClient? = nil

    // MARK: - Life Cycle
    init() {
        setupSocket()
        print(socket?.status)
    }
    
    // MARK: - Socket Disconnect
    func stop() {
        socket?.disconnect()
        socket?.removeAllHandlers()
    }

    // MARK: - Socket Setup
    func setupSocket() {
        self.socket = manager.defaultSocket
    }
    // MARK: - Socket Listeners
    func getChat(completion:@escaping(JSON)->Void){
        socket?.on("get-message", callback: { data, ack in
            completion(JSON(data.first))

        })
        socket?.on("send-message", callback: { data, ack in
            completion(JSON(data.first))
        })
    }
    func listenUser(otherUser:String,completion:@escaping(JSON)->Void){
        socket?.on(AuthManager.currentUser.id , callback: { data, ack in
            completion(JSON(data.first))
        })
    }
    func listenUser2(otherUser:String,completion:@escaping(JSON)->Void){
        socket?.on(otherUser + AuthManager.currentUser.id, callback: { data, ack in
            completion(JSON(data.first))
        })
    }
    func listenSlug(slug:String,completion:@escaping(JSON)->Void){
        socket?.on(slug, callback: { data, ack in
            completion(JSON(data.first))
        })
    }
    func listenDelete(slug:String,completion:@escaping(String)->Void){
        socket?.on("delete-chat" + slug, callback: { data, ack in
            completion(data.first as! String)
        })
    }
    // MARK: - Socket Connect
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
        socket?.emit("typing-chat", user){
            
        }
    }

    func send(message: [String:String]) {
        print("MESAGEPARAM",message)
        socket?.emit("send-message",message)
    }
    func deleteMsg(param:[String:String]){
        print("deleteMsg",param)
        socket?.emit("delete-chat", param){
           
        }
    }
   
}

