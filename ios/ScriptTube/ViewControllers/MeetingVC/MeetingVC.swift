//
//  MeetingVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/04/23.
//
//self.remoteParticipantVideoContainer.transform = CGAffineTransformMakeScale(-1.0, 1.0)
import UIKit
import VideoSDKRTC
import WebRTC
import AVFoundation
class MeetingVC: UIViewController {
 
    @IBOutlet weak var chatTable: UITableView!
    @IBOutlet weak var bottomViewHeight: NSLayoutConstraint!
    @IBOutlet weak var bottomParticipantView: UIView!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var messageTf: UITextField!
    @IBOutlet weak var endCallBtn: UIButton!
    @IBOutlet weak var cameraBtn: UIButton!
    @IBOutlet weak var micBtn: UIButton!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var localParticipantViewVideoContainer: RTCMTLVideoView!
    private var meeting: Meeting?
    private var participants: [Participant] = []
    private var chatData: [LiveRoomChatModel] = []
    private var localParticipant : Participant?
    var roomData = LiveRoomDataModel()
    var isHidden = true
    var needMic = true
    var needCam = true
    var isBottomHidden = true
    var setupComplete = false
    var page = 1
    private var cameraPosition = CameraPosition.front
    override func viewDidLoad() {
        super.viewDidLoad()
        setupMeeting()
        setupBtns()
        setupTableView()
        //tableView.isHidden = true
        setupCollectionView()
        setupLocalUserView()
        getChat()
        cameraBtn.isSelected = !needCam
        micBtn.isSelected = !needMic
        let doubleTap = UITapGestureRecognizer(target: self, action: #selector(flipCamera))
        doubleTap.numberOfTapsRequired = 2
        self.view.addGestureRecognizer(doubleTap)
        //bottomViewHeight.constant = 0
        messageTf.attributedPlaceholder = NSAttributedString(string: "Say Something...",attributes: [.foregroundColor: UIColor.white])
        messageTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        paddingTF(tf: messageTf)
//        let panGesture = UIPanGestureRecognizer(target: self, action: #selector(handlePanGesture(_:)))
//        bottomParticipantView.addGestureRecognizer(panGesture)
    }
    @objc func flipCamera(){
        print("jjjnjnjnjnjn")
        //cameraPosition.toggle()

            // switch camera to front/back
            // Values: .front, .back
        self.meeting?.switchWebcam()
        
    }
    @objc func handlePanGesture(_ gesture: UIPanGestureRecognizer) {
        let translation = gesture.translation(in: bottomParticipantView)
       
        let newHeight = self.bottomViewHeight.constant + (-translation.y)
        
        // Limit the minimum and maximum height of the view if needed
        let minHeight: CGFloat = 200
        let maxHeight: CGFloat = self.view.frame.height * 0.5
        let clampedHeight = min(max(newHeight, minHeight), maxHeight)
        self.bottomViewHeight.constant = clampedHeight
        gesture.setTranslation(.zero, in: bottomParticipantView)
    }
    func setupTableView(){
        tableView.register(UINib(nibName: VideoCallCell.identifier, bundle: nil), forCellReuseIdentifier: VideoCallCell.identifier)
        chatTable.register(UINib(nibName: VideoChatCell.identifier, bundle: nil), forCellReuseIdentifier: VideoChatCell.identifier)
        chatTable.delegate = self
        chatTable.dataSource = self
        chatTable.transform = CGAffineTransform(rotationAngle: -(CGFloat)(Double.pi));
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
        showHideView()
    }
    func getChat(){
        let param = ["userId":AuthManager.currentUser.id,"slug":roomData.slug,"page":"\(page)","limit":"20"]
        DataManager.getVideoChat(param: param) { error in
            print("error",error)
        } completion: { data in
            print("uhsdvywsvdutewvdxweghj",data)
            data["data"].forEach { (_,singleChat) in
                self.chatData.append(LiveRoomChatModel(data2: singleChat))
            }
            self.chatTable.reloadData()
        }
    }
    func setupCollectionView(){
        collectionView.register(UINib(nibName: VideoCallCollectionCell.identifier, bundle: nil), forCellWithReuseIdentifier: VideoCallCollectionCell.identifier)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.reloadData()
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func showHideView(){
//        isHidden = participants.isEmpty
        if isHidden {
               UIView.animate(withDuration: 0.3, animations: {
                   self.tableView.alpha = 0.0
               }, completion: { _ in
                   self.tableView.isHidden = true
               })
           } else {
               self.tableView.alpha = 0.0
               self.tableView.isHidden = false
               UIView.animate(withDuration: 0.3, animations: {
                   self.tableView.alpha = 1.0
               })
           }
    }
    func showHideBottomView(){
        bottomParticipantView.layoutIfNeeded()
        if !isBottomHidden {
               UIView.animate(withDuration: 0.3, animations: {
                   //self.bottomViewHeight.constant = 200
               }, completion: { _ in
                   self.collectionView.isHidden = false
               })
           } else {
               //self.bottomViewHeight.constant = 0
               UIView.animate(withDuration: 0.3, animations: {
                   self.collectionView.isHidden = true
               })
           }
    }
    func setupLocalUserView(){
        self.localParticipantViewVideoContainer.transform = CGAffineTransformMakeScale(-1.0, 1.0)
        self.localParticipantViewVideoContainer.contentMode = .scaleToFill
    }
    func setupBtns(){
        micBtn.setImage(UIImage(systemName: "mic.slash"), for: .selected)//
        micBtn.setImage(UIImage(systemName: "mic"), for: .normal)
        cameraBtn.setImage(UIImage(systemName: "video.slash.fill"), for: .selected)
        cameraBtn.setImage(UIImage(systemName: "video.fill"), for: .normal)
    }
    func setupMeeting(){
        VideoSDK.config(token: roomData.token)
        meeting = VideoSDK.initMeeting(
            meetingId: roomData.roomId, // required
                    participantName: "", // required
                    micEnabled: needMic, // optional, default: true
                    webcamEnabled: needCam // optional, default: true
                )
        
        meeting?.join()
        meeting?.addEventListener(self)
        
        self.setupComplete = true
    }
    @IBAction func micBtnClicked(_ sender: UIButton) {
        sender.checkboxAnimation {
            sender.isSelected ? self.meeting?.muteMic() : self.meeting?.unmuteMic()
        }
    }
    
    @IBAction func cameraBtnClicked(_ sender: UIButton) {
        sender.checkboxAnimation {
            sender.isSelected ? self.meeting?.disableWebcam() : self.meeting?.enableWebcam()
        }
    }
    
    @IBAction func endCallBtnClicked(_ sender: Any) {
        self.meeting?.leave()
    }

    @IBAction func sendBtnClicked(_ sender: Any) {
        //print(#function)
        print("text",messageTf.text!.trimmingCharacters(in: .whitespaces))
        if !messageTf.text!.trimmingCharacters(in: .whitespaces).isEmpty{
            print(#function)
            let param = ["message":messageTf.text!.trimmingCharacters(in: .whitespacesAndNewlines),"userName":AuthManager.currentUser.userName,"userProfileImage":AuthManager.currentUser.profileImage]
            let param2 = ["senderId":AuthManager.currentUser.id,
                          "slug":self.roomData.slug,
                         "message":messageTf.text!.trimmingCharacters(in: .whitespaces)]
            AuthManager.sendMessageApi(delegate: self, param: param2) {
                //
            }
            self.meeting?.pubsub.publish(topic: "CHAT", message:param.toJSONString() , options: [:])
            messageTf.text = ""
        }
        
    }
    
}
//MARK: Meeting Listners

extension MeetingVC:MeetingEventListener{
    func onMeetingJoined() {
        guard let localParticipant = self.meeting?.localParticipant else{return}
        self.localParticipant = localParticipant
        localParticipant.addEventListener(self)
        localParticipant.setQuality(.high)
        meeting?.pubsub.subscribe(topic: "CHAT", forListener: self)
        //self.participants.append(localParticipant)
    }
    
    func onMeetingLeft() {
        self.meeting?.localParticipant.removeEventListener(self)
        self.meeting?.removeEventListener(self)
        self.navigationController?.popViewController(animated: true)
    }
    
    func onParticipantJoined(_ participant: VideoSDKRTC.Participant) {
        print(#function)
        self.participants.append(participant)
       
        if(participants.count >= 3){
            tableView.isScrollEnabled = true
        }else{
            tableView.isScrollEnabled = false
        }
        participant.addEventListener(self)
        participant.setQuality(.high)
        let indexPathTable = IndexPath(item: self.participants.count - 1, section: 0)
        if self.participants.count > 4{
            isHidden = true
            isBottomHidden = false
            collectionView.insertItems(at: [indexPathTable])
            collectionView.isHidden = false
            collectionView.reloadData()
        }else{
            isHidden = false
            isBottomHidden = true
            tableView.insertRows(at: [indexPathTable], with: .none)
            collectionView.isHidden = true
            //tableView.reloadData()
        }
        //showHideBottomView()
        showHideView()
    }
    
    func onParticipantLeft(_ participant: VideoSDKRTC.Participant) {
        print(#function)
        if let index = self.participants.firstIndex(where: {$0.id == participant.id }){
            self.participants.remove(at: index)
            if self.participants.count + 1 > 4{
                collectionView.deleteItems(at: [IndexPath(row: index, section: 0)])
                //collectionView.reloadData()
            }else{
                tableView.deleteRows(at: [IndexPath(row: index, section: 0)], with: .fade)
                //tableView.reloadData()
                collectionView.isHidden = true
            }
            
        }
    }
    
    func onRecordingStarted() {
        print(#function)
    }
    
    func onRecordingStoppped() {
        print(#function)
    }
    
    func onLivestreamStarted() {
        print(#function)
    }
    
    func onLivestreamStopped() {
        print(#function)
    }
    
    func onSpeakerChanged(participantId: String?) {
        //
    }
    
    func onMicRequested(participantId: String?, accept: @escaping () -> Void, reject: @escaping () -> Void) {
        //
    }
    
    func onWebcamRequested(participantId: String?, accept: @escaping () -> Void, reject: @escaping () -> Void) {
        //
    }
}
extension MeetingVC:PubSubMessageListener{
    func onMessageReceived(_ message: VideoSDKRTC.PubSubMessage) {
        let jsonString = message.message//"{\"message\":\"ey tu\",\"userName\":\"user9717\",\"userProfileImage\":\"\"}"

        if let jsonData = jsonString.data(using: .utf8) {
            do {
                let json = try JSONSerialization.jsonObject(with: jsonData, options: [])
                print(json)
                var data = JSON(json)
                self.chatData.insert(LiveRoomChatModel(data: data), at: 0)
                self.chatTable.performBatchUpdates({
                    chatTable.insertRows(at: [IndexPath(row: 0, section: 0)], with: .none)
                        }, completion: nil)
                print(data["message"].stringValue)
            } catch {
                print("Error: \(error.localizedDescription)")
            }
        } else {
            print("Error: Invalid JSON string")
        }
    }
}
extension MeetingVC:ParticipantEventListener{
    func onStreamEnabled(_ stream: VideoSDKRTC.MediaStream, forParticipant participant: VideoSDKRTC.Participant) {
        print(#function)
        if participant.isLocal && stream.kind == .state(value: .video){
            self.localParticipant = participant
            guard let track = stream.track as? RTCVideoTrack else{return}
            DispatchQueue.main.async {
                self.localParticipantViewVideoContainer.isHidden = false
                track.add(self.localParticipantViewVideoContainer)
            }
        }else{
            reloadCellOnStreamChange(participant: participant)
        }
    }
    
    func onStreamDisabled(_ stream: VideoSDKRTC.MediaStream, forParticipant participant: VideoSDKRTC.Participant) {
        print(#function)
        if participant.isLocal && stream.kind == .state(value: .video){
            self.localParticipantViewVideoContainer.isHidden = true
            guard let track = stream.track as? RTCVideoTrack else{return}
            DispatchQueue.main.async {
                track.remove(self.localParticipantViewVideoContainer)
            }
        }else if stream.kind == .state(value: .video){
            reloadCellOnStreamChange(participant: participant)
        }
    }
    func reloadCellOnStreamChange(participant: Participant){
        if participants.count < 4{
            if let index = self.participants.firstIndex(where: {$0.id == participant.id}) as? Int{
                if tableView.cellForRow(at: IndexPath(row: index, section: 0)) != nil {
                    tableView.reloadRows(at: [IndexPath(row: index, section: 0)], with: .none)
                }
            }
        }else{
            if let index = self.participants.firstIndex(where: {$0.id == participant.id}) as? Int{
                if collectionView.cellForItem(at: IndexPath(row: index, section: 0)) != nil {
                    collectionView.reloadItems(at: [IndexPath(row: index, section: 0)])
                }
            }
        }
    }
}

extension MeetingVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView == chatTable{
            return chatData.count
        }else{
            return participants.count
        }
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if tableView == chatTable{
            let cell = tableView.dequeueReusableCell(withIdentifier: VideoChatCell.identifier, for: indexPath) as! VideoChatCell
            cell.updateCell(data: chatData[indexPath.row])
            cell.transform = CGAffineTransform(rotationAngle: CGFloat(Double.pi));
            return cell
        }else{
            let cell = tableView.dequeueReusableCell(withIdentifier: VideoCallCell.identifier, for: indexPath) as! VideoCallCell
            DispatchQueue.main.async {
                if self.participants.indices.contains(indexPath.row){
                    cell.nameLbl.text = self.participants[indexPath.row].displayName ?? "Guest"
                    if let currentVideoTrack = self.participants[indexPath.row].streams.first(where: {$1.kind == .state(value: .video)})?.value.track as? RTCVideoTrack {
                        cell.videoView.isHidden = false
                        print("ADDINGTO CEll")
                        currentVideoTrack.add(cell.videoView)
                    }else{
                        cell.videoView.isHidden = true
        //                cell.nameLbl.text = self.participants[indexPath.row].displayName ?? "Guest"
                        print("NOY")
                    }
                }
            }
            return cell
        }
       
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if tableView == chatTable{
            return UITableView.automaticDimension
        }else{
            return tableView.frame.height / 3 + 20
        }
        
    }
}

extension MeetingVC:UICollectionViewDelegate,UICollectionViewDelegateFlowLayout,UICollectionViewDataSource{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.participants.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: VideoCallCollectionCell.identifier, for: indexPath) as! VideoCallCollectionCell
        cell.layoutIfNeeded()
        DispatchQueue.main.async {
            cell.nameLbl.text = self.participants[indexPath.row].displayName ?? "Guest"
            //cell.layer.cornerRadius = self.collectionView.frame.width / 5  - 5
            if let currentVideoTrack = self.participants[indexPath.row].streams.first(where: {$1.kind == .state(value: .video)})?.value.track as? RTCVideoTrack {
                cell.videoView.isHidden = false
                print("ADDINGTO CEll")
                currentVideoTrack.add(cell.videoView)
            }else{
                cell.videoView.isHidden = true
//                cell.nameLbl.text = self.participants[indexPath.row].displayName ?? "Guest"
                print("NOY")
            }
        }
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: self.collectionView.frame.width / 5  - 5, height: self.collectionView.frame.width / 5  - 5)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        5
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        5
    }
}
