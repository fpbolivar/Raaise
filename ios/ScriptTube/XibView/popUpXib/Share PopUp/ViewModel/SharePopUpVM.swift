//
//  SharePopUpVM.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//

import Foundation
class SharePopUpVM{
    var shareModel  =  [ShareVideoModel]() //array of SharevideoModel
}
//MARK: // FUNCTIONS //
extension SharePopUpVM{
    
    //MARK: CONFIGURE UI
    func configureUI(VC: SharePopUp){
        VC.searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
        VC.searchTf.paddingLeftRightTextField(left: 35, right: 0)
        VC.searchTf.layer.cornerRadius = 10
        VC.searchTf.overrideUserInterfaceStyle = .light
        VC.tableView.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        //add data for share model
        shareModel.append(ShareVideoModel(shareOn: .whatsapp))
        shareModel.append(ShareVideoModel(shareOn: .facebook))
        shareModel.append(ShareVideoModel(shareOn: .instagram))
        shareModel.append(ShareVideoModel(shareOn: .other))
        //add height to collection view
        VC.heightConstraint.constant = Constant.ShareVideo.SHARE_COLLECTION_HEIGHT
        VC.view.layoutIfNeeded()
        //add delegate to collection view
        VC.collectionView.dataSource = VC
        VC.collectionView.delegate = VC
        //register cell
        RegisterCell.registerCellForCollectionView(VC.collectionView, ShareVideoCell.self, RegisterCell.SHARE_POP_UP_CELL)
       // VC.otherImg.addGestureRecognizer(UITapGestureRecognizer(target: VC, action: #selector(VC.shareToOtherApp)))
        VC.searchTf.delegate = VC
    }
}
