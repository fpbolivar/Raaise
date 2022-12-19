//
//  SearchVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class SearchVC: UIViewController {

    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var backImage: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        setupCollectionView()
        // Do any additional setup after loading the view.
    }
    func setup(){
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        backImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(backAction)))
        //searchTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        backImage.image = UIImage(named: "ic_back")?.addPadding(0, 0, 20, 0)
        searchTf.paddingLeftRightTextField(left: 25, right: 0)
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Video",attributes: [.foregroundColor: UIColor.lightGray])
    }
    func setupCollectionView(){
        collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
        collectionView.dataSource = self
        collectionView.delegate = self
    }
    @objc func backAction(){
        self.navigationController?.popViewController(animated: true)
    }
}

extension SearchVC: UICollectionViewDelegate,UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
//    func numberOfSections(in collectionView: UICollectionView) -> Int {
//        return 1
//    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 10
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as! ProfileVideoItemCell
        //cell.contentView.backgroundColor = .gray
        //cell.updateCell(withImg: "")
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {

        let wFrame = collectionView.frame.width
        let itemWidth = (wFrame/3)//( wFrame - CGFloat(Int(wFrame) % 3)) / 3.0 - 1.0
        let itemHeight =  itemWidth * 1.3
        return CGSize.init(width: itemWidth, height: itemHeight)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
}
