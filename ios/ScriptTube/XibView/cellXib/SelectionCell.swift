//
//  SelectionCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit

class SelectionCell: UITableViewCell {

    @IBOutlet weak var containerView: CardView!
    @IBOutlet weak var progressBar: CircleProgress!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var playBtn: UIButton!
    @IBOutlet weak var detailLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    static var identifier = "SelectionCell"
    var playAudio: ((String)->Void)?
    var audioData : AudioDataModel = AudioDataModel()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        progressBar.forgroundColor = UIColor.white
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        self.nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        self.detailLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        self.timeLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @IBAction func playBtnClicked(_ sender: UIButton) {
        playAudio!(audioData.audioUrl)
    }
    
    func updateCellForAudio(data:AudioDataModel){
        self.audioData = data
        self.nameLbl.text = data.artistName.localizedCapitalized
        print("GENRE",data.genre)
        self.detailLbl.text = "\(data.songName) | \(data.genre)".localizedCapitalized
        self.profileImg.loadImg(url: data.thumbnail)
    }
    func followerList(data:UserListDataModel){
        self.timeLbl.isHidden = true
        self.playBtn.isHidden = true
        self.detailLbl.text = (Int(data.count)?.shorten() ?? "0") + " Followers"
        self.nameLbl.text = data.name.localizedCapitalized
        self.profileImg.loadImgForProfile(url: data.image)
        //loadImg(url: data.thumbnail)
    }
    func chatList(){
        self.timeLbl.isHidden = false
        self.playBtn.isHidden = true
        self.detailLbl.text = "20k Followers".localizedCapitalized
    }
}
