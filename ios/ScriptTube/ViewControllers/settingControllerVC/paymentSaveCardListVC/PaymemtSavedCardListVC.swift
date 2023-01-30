//
//  PaymemtSavedCardListVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class PaymemtSavedCardListVC: BaseControllerVC {
    @IBOutlet weak var noCardLbl: UILabel!
    @IBOutlet weak var payBtnView: CardView!
    @IBOutlet weak var payLbl: UILabel!
    @IBOutlet weak var creditLbl:UILabel!
    @IBOutlet weak var addNewLbl:UILabel!
    @IBOutlet weak var bottomInfoLbl:UILabel!
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var payBtn:UIButton!
    @IBOutlet weak var  tableView:ContentSizedTableView!
    var cardList:[CardListModel] = []
    var selectedIndex: IndexPath? = nil
    var selectedCard = ""
    var amount = ""
    var donateTo = ""
    var videoId = ""
    var forPayment = false
    var deleteCardId = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        payBtn.addTarget(self, action: #selector(pay), for: .touchUpInside)
        addNavBar(headingText:"Payment Methods",redText:"Methods",type: .addNewCard,addNewCardSelector: #selector(addNewCard))
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: CardCell.identifier, bundle: nil), forCellReuseIdentifier: CardCell.identifier)
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        self.pleaseWait()
        getCardsApi(){
            self.clearAllNotice()
            self.executeTblDelegate()
        }
        let vc = AddNewPaymentMethodVC()
        vc.newCardAdded = {
            print("tttatattatat")
        }
        addNewLbl.isUserInteractionEnabled = true
        addNewLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(addNewCardAction)))
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
        print("sdasdxsa")
    }
    func setDefaultCardApi(){
        let param = ["cardId":selectedCard]
        AuthManager.setDefaultCard(delegate: self, param: param) {
            DispatchQueue.main.async {
                //self.tableView.reloadData()
            }
        }
    }
    @objc func pay(){
        if selectedCard == ""{
            ToastManager.errorToast(delegate: self, msg: "Select a Card")
            
        }else{
            let param = ["cardId":selectedCard,"amount":amount,"donateTo":donateTo,"videoId":videoId]
            
            self.pleaseWait()
            print("PaymentParam",param)
            AuthManager.makePaymentWithCardId(delegate: self, param: param) {
                DispatchQueue.main.async {
                    self.clearAllNotice()
                    self.payBtnView.isHidden = true
                    let vc = PaymentSuccessVC()
                    vc.modalTransitionStyle = .coverVertical
                    vc.modalPresentationStyle = .overCurrentContext
                    self.present(vc, animated: true)
                    //self.navigationController?.popViewController(animated: true)
                }
            } onError: {
                
            }
        }
    }
    @objc func addNewCard(){
        let vc = AddNewPaymentMethodVC()
        vc.modalPresentationStyle = .overFullScreen
        vc.newCardAdded = {
            print("ahbcsjahcb")
            DispatchQueue.main.async {
                ToastManager.successToast(delegate: self, msg: "Card Added Successfully")
            }
            self.getCardsApi(){
                self.executeTblDelegate()
            }
        }
        self.present(vc, animated: true)
    }
   
    func getCardsApi(completion:@escaping()->Void){
        self.cardList = []
        DataManager.getCardListApi(delegate: self) { json in
            json["cards"].forEach { (message,data) in
                self.cardList.append(CardListModel(data: data))
            }
            if self.cardList.count >= 1{
                self.noCardLbl.isHidden = true
                if self.forPayment{
                    self.payBtnView.isHidden = false
                }
            }else{
                self.noCardLbl.isHidden = false
                self.payBtnView.isHidden = true
            }
            completion()
        }
    }
    @objc func addNewCardAction(){
        let vc =  AddNewPaymentMethodVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func setfonts(){
        noCardLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX14)
        messageLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX10)
        creditLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX15)
        addNewLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX15)
        bottomInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        payBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        payLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
    }
    func executeTblDelegate(){
        DispatchQueue.main.async {
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.reloadData()
        }
    }
    func deleteCardApi(){
        let param = ["cardId":self.deleteCardId]
        AuthManager.deleteCardApi(delegate: self, param: param) {
            self.getCardsApi {
                DispatchQueue.main.async {
                    self.clearAllNotice()
                    self.tableView.reloadData()
                }
            }
        }
    }
}
extension PaymemtSavedCardListVC:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
       1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return cardList.count
    }


    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {

        return 80
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  CardCell.identifier, for: indexPath) as!  CardCell
        if cardList[indexPath.row].defaultCard{
            self.selectedCard = cardList[indexPath.row].id
            self.selectedIndex = indexPath
            print("defaultCard",selectedCard)
        }
        cell.update(data: cardList[indexPath.row])
        cell.cardDelete = {
            self.deleteCardId = self.cardList[indexPath.row].id
            self.pleaseWait()
            self.deleteCardApi()
//            self.cardList.remove(at: indexPath.row)
            self.cardList.count == 0 ? self.noCardLbl.isHidden = false : print("alsl")
        }
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        self.selectedCard = self.cardList[indexPath.row].id
        if selectedIndex == nil{
            let cell = tableView.cellForRow(at: indexPath) as! CardCell
            cell.cellSelected()
            selectedIndex = indexPath
        }else if selectedIndex != indexPath{
            let cell = tableView.cellForRow(at: indexPath) as! CardCell
            cell.cellSelected()
            
            let cell2 = tableView.cellForRow(at: selectedIndex!) as! CardCell
            cell2.cellUnselected()
            
            selectedIndex = indexPath
        }
        cardList.forEach { card in
            print("defaultCard",card.defaultCard)
            if selectedCard == card.id{
                card.defaultCard = true
                print("defaultCardtrue",card.defaultCard,card.id)
            }else{
                card.defaultCard = false
                print("defaultCardfalse",card.defaultCard,card.id)
            }
        }
        self.setDefaultCardApi()
    }

}
