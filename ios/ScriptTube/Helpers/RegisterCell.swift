//
//  RegisterCell.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//

import Foundation
struct RegisterCell{
    ///***Identifiers for tableview cell and collection view cell
    
    //MARK: SHARE POPUP
    static let SHARE_POP_UP_CELL = "SharePopUpCell"
}
//MARK: // REGISTER CELLS //
extension RegisterCell{
    ///***Functions
    
    //MARK: COLLECTIONVIEW
    static func registerCellForCollectionView(_ collectionView : UICollectionView,_ className : AnyClass,_ identifier : String){
        collectionView.register(className.self, forCellWithReuseIdentifier: identifier)
    }
    //MARK: TABLEVIEW
    static func registerCellForTableView(_ tableView : UITableView,_ className : AnyClass,_ identifier : String){
        tableView.register(className, forCellReuseIdentifier: identifier)
    }
    //MARK: COLLECTION REUSABLE VIEW
    static func registerCellForCollectionReusableView(_ collectionView: UICollectionView,_ className : AnyClass,_ identifier: String,_ kind: String){
        collectionView.register(className.self, forSupplementaryViewOfKind: kind, withReuseIdentifier: identifier)
    }
}

