//
//  InterestViewController.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 04/03/24.
//

import UIKit

class InterestViewController: BaseControllerVC {

    @IBOutlet weak var lblChooseInterests: UILabel!
    @IBOutlet weak var continueBtnOtl: UIButton!
    @IBOutlet weak var interestCollectionView: UICollectionView!
    
    var category:[VideoCategoryModel] = []
    var selCategoryID = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()
       
        let nibName = UINib(nibName: "ChooseInterestCell", bundle: nil)
        self.interestCollectionView.register(nibName, forCellWithReuseIdentifier: ChooseInterestCell.identifier)
        self.interestCollectionView.delegate = self
        self.interestCollectionView.dataSource = self

        // Do any additional setup after loading the view.
        applyStyle()
        getVideoCategoryApi {
            
        }
    }
    func addInterestApi() {
        let param = ["categoryIds":selCategoryID.joined(separator: ",")]
        AuthManager.addInterest(delegate: self, param: param) {
            self.navigationController?.popToRootViewController(animated: true)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        
        //addInterestApi()
    }
    
    func applyStyle(){
        lblChooseInterests.applyGradientColorToLabelText(colors: [UIColor(named: "Gradient1") ?? .black, UIColor(named: "Gradient2") ?? .white])
    }
    
    func getVideoCategoryApi(completion:@escaping()->Void){
        DataManager.getVideoCategory(delegate: self) { json in
            json["data"].forEach { (message,data) in
                self.category.append(VideoCategoryModel(data: data))
            }
            debugPrint(self.category.count)
            self.interestCollectionView.reloadData()
            completion()
        }
    }


    @IBAction func skipBtnAction(_ sender: UIButton) {
        navigationController?.popToRootViewController(animated: true)
    }
    
    
    @IBAction func continueBtnAction(_ sender: UIButton) {
        if selCategoryID.isEmpty {
            ToastManager.errorToast(delegate: self, msg: "Please select an category")
        } else {
            addInterestApi()
        }
        
    }
    
}
extension InterestViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let obj = self.category[indexPath.row]
        
        if selCategoryID.contains(category[indexPath.row].id) {
            let index = selCategoryID.firstIndex(of: obj.id) ?? -1
            selCategoryID.remove(at: index)
        } else {
            selCategoryID.append(obj.id)
        }
        self.interestCollectionView.reloadData()
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        print(category.count)
        return category.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let interestCell = collectionView.dequeueReusableCell(withReuseIdentifier: ChooseInterestCell.identifier, for: indexPath) as! ChooseInterestCell
        interestCell.chooseInterestLabel.text = category[indexPath.row].name
        interestCell.chooseInterestImg.loadImg(url:category[indexPath.row].image)
        
        debugPrint(selCategoryID)
        if selCategoryID.contains(category[indexPath.row].id) {
            interestCell.chooseInterestView.backgroundColor = .white
            interestCell.chooseInterestLabel.textColor = .black
        }else{
            interestCell.chooseInterestView.backgroundColor = .clear
            interestCell.chooseInterestLabel.textColor = .white
        }
        return interestCell
    }
}
extension InterestViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        return CGSize(width: (self.interestCollectionView.bounds.width / 2) - 10,height: (50))
    }
}
