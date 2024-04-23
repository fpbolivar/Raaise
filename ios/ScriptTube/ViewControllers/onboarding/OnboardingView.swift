//
//  OnboardingView.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 7/7/22.
//

import UIKit

class OnboardingView: UIView {
    @IBOutlet var headingLbl: UILabel!
    @IBOutlet var headingLbl2: ActiveLabel!
    @IBOutlet var descriptionLbl: UILabel!
    @IBOutlet var scrollView: UIScrollView!
    @IBOutlet var imgView: UIImageView!
    func config(data:OnboardingDataModal){
        headingLbl.text = data.title
        descriptionLbl.text = data.description
        scrollView.isScrollEnabled = false
        imgView.image = data.img
        if #available(iOSApplicationExtension 13.0, *) {
            scrollView.automaticallyAdjustsScrollIndicatorInsets = false
        } else {
            // Fallback on earlier versions
        }
        setfonts()
        redColorUnderline(data:data)
    }
    func redColorUnderline(data:OnboardingDataModal){

        let customType = ActiveType.custom(pattern: data.redTitle)
        headingLbl2.enabledTypes.append(customType)

        headingLbl2.textColor = UIColor.white

        headingLbl2.text = data.title2
        headingLbl2.customColor[customType] = .new_theme//UIColor.theme
        headingLbl2.customSelectedColor[customType] = UIColor.gray
        headingLbl2.font = AppFont.FontName.regular.getFont(size: AppFont.pX25)
        headingLbl2.handleCustomTap(for: customType) { element in


        }

    }
    func setfonts(){
        headingLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX20)
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)

    }
    func setData(title:String,description:String){
        descriptionLbl.text = description
        headingLbl.text = title
    }
}
