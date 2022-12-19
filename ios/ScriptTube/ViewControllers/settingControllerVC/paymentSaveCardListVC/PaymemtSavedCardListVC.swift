//
//  PaymemtSavedCardListVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class PaymemtSavedCardListVC: BaseControllerVC {
    @IBOutlet weak var creditLbl:UILabel!
    @IBOutlet weak var addNewLbl:UILabel!
    @IBOutlet weak var bottomInfoLbl:UILabel!
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var payBtn:UIButton!
    @IBOutlet weak var  tableView:ContentSizedTableView!
    var cardList:[CardListModel] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Payment Methods",redText:"Methods",type: .addNewCard)
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: CardCell.identifier, bundle: nil), forCellReuseIdentifier: CardCell.identifier)

        getCardsApi(){
            self.executeTblDelegate()
        }
        addNewLbl.isUserInteractionEnabled = true
        addNewLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(addNewCardAction)))
        // Do any additional setup after loading the view.
    }
   
    func getCardsApi(completion:@escaping()->Void){
        DataManager.getCardListApi(delegate: self) { json in
            json["cards"].forEach { (message,data) in
                self.cardList.append(CardListModel(data: data))
            }
            completion()
        }
    }
    @objc func addNewCardAction(){
        let vc =  AddNewPaymentMethodVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func setfonts(){
        messageLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX12)
        creditLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX15)
        addNewLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX15)
        bottomInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        payBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
    }
    func executeTblDelegate(){
        DispatchQueue.main.async {
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.reloadData()
        }
    }
}
extension PaymemtSavedCardListVC:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
       1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       3
    }


    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {

        return 80
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  CardCell.identifier, for: indexPath) as!  CardCell
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }

}
