//
//  AddNewPaymentMethodVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class AddNewPaymentMethodVC: BaseControllerVC {

    @IBOutlet weak var bottomInfoLbl:UILabel!
    @IBOutlet weak var topInfoLbl:UILabel!
    @IBOutlet weak var payBtn:UIButton!
    @IBOutlet weak var  tableView:ContentSizedTableView!
    @IBOutlet weak var  cardTF:UITextField!
    @IBOutlet weak var  monthTF:UITextField!
    @IBOutlet weak var  cvvTF:UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        //addNavBar(headingText:"Payment Methods",redText:"Methods")
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: CardCell.identifier, bundle: nil), forCellReuseIdentifier: CardCell.identifier)
        executeTblDelegate()
        // Do any additional setup after loading the view.
    }
    func setfonts(){

        bottomInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        topInfoLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        //cardTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        cardTF.placeholderColor(color: UIColor.darkGray, placeholderText: "Card Number")
        monthTF.placeholderColor(color: UIColor.darkGray, placeholderText: "MM / YY")
        cvvTF.placeholderColor(color: UIColor.darkGray, placeholderText: "CVV")
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
extension AddNewPaymentMethodVC:UITableViewDelegate,UITableViewDataSource{
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
