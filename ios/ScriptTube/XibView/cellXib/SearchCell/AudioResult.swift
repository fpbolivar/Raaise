//
//  AudioResult.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/12/22.
//

import UIKit

class AudioResult: UICollectionViewCell {
    @IBOutlet weak var audioNameLbl: UILabel!
    static var identifier = "AudioResult"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        audioNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
    }

    func updateCell(data:AudioDataModel){
        self.audioNameLbl.text = data.songName.localizedCapitalized
    }
}
