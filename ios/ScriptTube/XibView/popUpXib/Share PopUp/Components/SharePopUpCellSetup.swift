//
//  SharePopUpCellSetup.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//
import Foundation
class SharePopUpCellSetup  {
    ///***Functions
    
    //MARK: ADD SHARE VIDEO CELL
    /// USE ShareVideoCell to share the video on a social media platform
    /// - Parameters:
    ///   - collectionView: instnace of the collection view
    ///   - indexPath: indexPath of the cell
    ///   - delegate: instance of SearchvideoDelegate
    ///   - data: data description
    /// - Returns: instance of ShareVideoCell
    static func useShareVideoCell(collectionView : UICollectionView,indexPath: IndexPath,delegate: ShareVideoDelegate,data: ShareVideoModel)->ShareVideoCell{
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: RegisterCell.SHARE_POP_UP_CELL, for: indexPath) as! ShareVideoCell
        cell.delegate = delegate
        cell.setFontAndValues()
        cell.setData(data: data)
        return cell
    }
}
