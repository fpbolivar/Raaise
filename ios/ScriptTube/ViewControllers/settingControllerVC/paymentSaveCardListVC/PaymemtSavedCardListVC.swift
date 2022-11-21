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
    @IBOutlet weak var payBtn:UIButton!
    @IBOutlet weak var  tableView:ContentSizedTableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Payment Methods",redText:"Methods")
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: CardCell.identifier, bundle: nil), forCellReuseIdentifier: CardCell.identifier)

        executeTblDelegate()
        addNewLbl.isUserInteractionEnabled = true
        addNewLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(addNewCardAction)))
        // Do any additional setup after loading the view.
    }
    @objc func addNewCardAction(){
        let vc =  AddNewPaymentMethodVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func setfonts(){
        creditLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX15)
        addNewLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX15)
        bottomInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        payBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
    }
    func executeTblDelegate(){
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

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
    }

}
