//
//  DonationPopUpViewController.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit

class DonationPopUpViewController: UIViewController {
    @IBOutlet weak var otherAmtLbl: UILabel!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var dismissBackBigButton: UIButton!
    @IBOutlet weak var donateLbl: UILabel!
    @IBOutlet weak var donationTf: UITextField!
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var donationAmtLbl: UILabel!
    let amounts = ["$10"]
    var post:Post!
    var delegate:DonationPopupDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        dismissBackBigButton.addTarget(self, action: #selector(dismissPopup), for: .touchUpInside)
    }
    //MARK: - Setting Up
    func setup(){
        otherAmtLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX10)
        collectionView.contentMode = .center
        collectionView.register(UINib(nibName: AmountCell.identifier, bundle: nil), forCellWithReuseIdentifier: AmountCell.identifier)
        collectionView.delegate = self
        collectionView.dataSource = self
        donateLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        donationTf.attributedPlaceholder = NSAttributedString(string: "Enter amount",attributes: [.foregroundColor: UIColor(named: "loginTFColor") as Any])
        donationTf.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        messageLbl.font = AppFont.FontName.regular.getFont(size:AppFont.pX10)
        donationAmtLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
    }
    @objc func dismissPopup(){
        self.dismiss(animated: true)
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillAppear(true)
    }
    //MARK: - Donation Process
    @IBAction func donateBtnClicked(_ sender: Any) {
        if self.donationTf.text!.trimmingCharacters(in: .whitespaces).isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Enter Support Amount")
            return
        }
        let amt = self.donationTf.text!
        let param = ["amount":amt,"donateTo":post.userDetails!.id,"videoId":post.id]
        let sender = sender as! UIButton
        sender.isEnabled = false
        //Hit Donation Api when Default Card is Selected
        AuthManager.makePaymentWithCardId(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.clearAllNotice()
                self.dismiss(animated: true) {
                    self.delegate?.donationSuccess()
                }
            }
        } onError: {
            self.dismiss(animated: true) {
                //Create a new card when no default is available
                self.delegate?.donateBtnClicked(post: self.post,amt: amt)
            }
        }
    }
    
}
//MARK: - Donation Protocols
protocol DonationPopupDelegate{
    func donateBtnClicked(post:Post,amt:String)
    func donationSuccess()
}
//MARK: - Collection View Delegates
extension DonationPopUpViewController:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return amounts.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AmountCell.identifier, for: indexPath) as! AmountCell
        cell.updateCell(withAmt: amounts[indexPath.row],forPopup: true)
       
        
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: (self.collectionView.bounds.width / 5) - 5, height: (self.collectionView.bounds.height - 15))
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let cell = collectionView.cellForItem(at: indexPath) as! AmountCell
        cell.selectedCell(forPopup: true)
    }
    func collectionView(_ collectionView: UICollectionView, didDeselectItemAt indexPath: IndexPath) {
        let cell = collectionView.cellForItem(at: indexPath) as! AmountCell
        cell.unselectedCell()
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {

        let totalCellWidth = (Int(self.collectionView.bounds.width) / 5) - 5 * amounts.count
        let totalSpacingWidth = 5 * (amounts.count - 1)

        let leftInset = (self.collectionView.bounds.width - CGFloat(totalCellWidth + totalSpacingWidth)) / 2
        let rightInset = leftInset

        return UIEdgeInsets(top: 0, left: leftInset, bottom: 0, right: rightInset)
    }
}
