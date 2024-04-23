//
//  ShareVideoCell.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//
import UIKit
class ShareVideoCell: UICollectionViewCell {
    var delegate : ShareVideoDelegate?
    var dataModel : ShareVideoModel?
    //initialize stackview
    lazy var stackView : UIStackView  = {
        var view = UIStackView()
        view.axis =  .vertical
        return view
    }()
    //initialize UIView
    lazy var emptyView : UIView = {
       var view = UIView()
        return view
    }()
    //initialize imgview
    lazy var imgView : UIImageView = {
        var view = UIImageView()
        view.contentMode = .scaleAspectFit
//        view.backgroundColor = UIColor(named:"AddCardColor")
        view.isUserInteractionEnabled = true
        let val = Constant.ShareVideo.SHARE_COLLECTION_HEIGHT / 2
        view.heightAnchor.constraint(equalToConstant: val).isActive =  true
        view.widthAnchor.constraint(equalToConstant: val).isActive =  true
        view.layer.cornerRadius = val / 2
        view.clipsToBounds = true
        return view
    }()
    //intialize UILabel
    lazy var lbl : UILabel = {
       var view = UILabel()
        view.font = AppFont.FontName.medium.getFont(size: AppFont.pX10)
        view.textColor = .white
        view.textAlignment = .center
        return view
    }()
    //MARK: set font and values
    func setFontAndValues(){
        self.contentView.backgroundColor = .clear
        stackView.subviews.forEach({$0.removeFromSuperview()})
        //for stackview
        stackView.anchor(to: contentView,top: contentView.topAnchor,leading: contentView.leadingAnchor,trailing: contentView.trailingAnchor,bottom: contentView.bottomAnchor)
        //for img
        stackView.addArrangedSubview(emptyView)
        imgView.addToCenter(parent: emptyView)
        //for lbl
        stackView.addArrangedSubview(lbl)
        //add tap gesture
        imgView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(imgTapped)))
    }
    //MARK: SET DATA
    func setData(data: ShareVideoModel){
        self.dataModel = data
        if let img = data.img {
            imgView.image = img
        }
        lbl.text = data.title
    }
    //MARK: IMAGE TAPPED
    @objc func imgTapped(){
        if let model = dataModel{
            delegate?.shareVideo(data: model)
        }
    }
}
