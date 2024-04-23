//
//  UserAboutUsVC.swift
//  ScriptTube
//
//  Created by VivaJiva  on 04/03/24.
//

import UIKit

class UserAboutUsVC: UIViewController {
    @IBOutlet weak var lblShortBio: UITextView!
    var bioData = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        if bioData == "" {
            lblShortBio.text = "No Bio"
        } else {
            lblShortBio.text = bioData
        }
        
       // lblShortBio.text = bioData
        // Do any additional setup after loading the view.
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
