//
//  SignUpVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit
import FacebookLogin
import GoogleSignIn
import AuthenticationServices
class SignUpVC: BaseControllerVC {
    @IBOutlet weak var appleSignInLbl: UILabel!
    @IBOutlet weak var signUpLbl: UILabel!
    @IBOutlet weak var cnfPassTfEyeImg: UIImageView!
    @IBOutlet weak var passTfEyeImg: UIImageView!
    @IBOutlet weak var  orSignInWithLbl:UILabel!
    @IBOutlet weak var  alreadyhaveLbl:ActiveLabel!
    @IBOutlet weak var  signUpBtn:UIButton!
    @IBOutlet weak var  emailTF:UITextField!
    @IBOutlet weak var  passTF:UITextField!
    @IBOutlet weak var  cnfPassTF:UITextField!
    @IBOutlet weak var  mobileTF:UITextField!
    @IBOutlet weak var  nameTF:UITextField!
    @IBOutlet weak var  usernameTF:UITextField!
    @IBOutlet weak var  googleLbl:UILabel!
    @IBOutlet weak var  fbLbl:UILabel!

    var passTap : UITapGestureRecognizer!
    var cnfPassTap : UITapGestureRecognizer!
    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        redColorUnderline()
        setPlaceholder()
        addNavBar(headingText:"Sign Up for Scriptube",redText:"Scriptube")
        // Do any additional setup after loading the view.
    }
    @IBAction func appleSignInBtnClicked(_ sender: Any) {
        let appleIDProvider = ASAuthorizationAppleIDProvider()
        let request = appleIDProvider.createRequest()
        request.requestedScopes = [.fullName, .email]
        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        authorizationController.performRequests()
    }
    @IBAction func fbLoginBtnClicked(_ sender: Any) {
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        fbLogin()
    }
    @IBAction func googleLoginBtnClicked(_ sender: Any) {
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        googleLogin()
    }
    func googleLoginApi(withToken token:String){
        let param = ["token":token,"deviceType":"ios","deviceToken":UserDefaultHelper.getDevice_Token()]
        AuthManager.googleLoginApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.goToTabBar()
            }
        }
    }

    func googleLogin() {
        GIDSignIn.sharedInstance.signOut()
        GIDSignIn.sharedInstance.signIn(with: AppDelegate.signInConfig, presenting: self){user,error in
            print("GOOGLETOKEN",user?.authentication.idToken,user?.authentication.accessToken)
            guard let googleToken = user?.authentication.accessToken else{return}
            self.googleLoginApi(withToken: googleToken)
        }
    }
    func fbLogin(){
        let fbLoginManager : LoginManager = LoginManager()
        //fbLoginManager.logOut()
        fbLoginManager.logIn(permissions: ["email","public_profile"], from: self) { (result, error) -> Void in
            if (error == nil){
                let fbloginresult : LoginManagerLoginResult = result!
                // if user cancel the login
                if (result?.isCancelled)!{
                    return
                }
                if(fbloginresult.grantedPermissions.contains("email"))
                {
                    self.getFBUserData()
                }
            }
        }
    }
    func goToTabBar(){
        let vc  = MainTabBarVC()
        UIApplication.keyWin!.rootViewController = vc
        UIApplication.keyWin!.makeKeyAndVisible()
    }
    func fbLoginApi(withToken token:String){
        let param = ["token":token,"deviceType":"ios","deviceToken":UserDefaultHelper.getDevice_Token()]
        AuthManager.facebookLoginApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.goToTabBar()
            }
        }
    }
    func getFBUserData(){
        if((AccessToken.current) != nil){
            GraphRequest(graphPath: "me", parameters: ["fields": "id, name, first_name, last_name, picture.type(large), email"]).start(completion: { (connection, result, error) -> Void in
          if (error == nil){
            //everything works print the user data
              if let fbToken = AccessToken.current?.tokenString{
                  print("FACEBOOKTOKEN",fbToken)
                  self.fbLoginApi(withToken: fbToken)
              }
            //print(result)
          }
        })
      }
    }
    func signUpApi(){
        let param = ["name":nameTF.text ?? "","userName":usernameTF.text ?? "","email":emailTF.text ?? "","password":passTF.text ?? "","phoneNumber":mobileTF.text ?? "","deviceType":"ios","deviceToken":UserDefaultHelper.getDevice_Token()]
        AuthManager.signInApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                //self.gotoPersonalInfoVC()
                self.navigationController?.popViewController(animated: true)
            }
        }
    }
    @objc func showPassword(sender: UITapGestureRecognizer){
        if sender == passTap{
            passTF.isSecureTextEntry = !passTF.isSecureTextEntry
            if passTF.isSecureTextEntry{
                passTfEyeImg.image = UIImage(systemName: "eye")
            }else{
                passTfEyeImg.image = UIImage(systemName: "eye.slash")
            }
        }else{
            cnfPassTF.isSecureTextEntry = !cnfPassTF.isSecureTextEntry
            if passTF.isSecureTextEntry{
                cnfPassTfEyeImg.image = UIImage(systemName: "eye")
            }else{
                cnfPassTfEyeImg.image = UIImage(systemName: "eye.slash")
            }
        }
    }
    func setfonts(){
        alreadyhaveLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        passTap = UITapGestureRecognizer(target: self, action: #selector(showPassword))
        cnfPassTap = UITapGestureRecognizer(target: self, action: #selector(showPassword))
        passTfEyeImg.addGestureRecognizer(passTap)
        cnfPassTfEyeImg.addGestureRecognizer(cnfPassTap)
        orSignInWithLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)

        googleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        appleSignInLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        fbLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)

        signUpBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
        signUpLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
        //passTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        //emailTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        emailTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        passTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        nameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        usernameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        mobileTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        passTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        cnfPassTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        emailTF.paddingLeftRightTextField(left: 20, right: 20)
        passTF.paddingLeftRightTextField(left: 20, right: 20)
        nameTF.paddingLeftRightTextField(left: 20, right: 20)
        usernameTF.paddingLeftRightTextField(left: 20, right: 20)
        mobileTF.paddingLeftRightTextField(left: 20, right: 20)
        passTF.paddingLeftRightTextField(left: 20, right: 20)
        cnfPassTF.paddingLeftRightTextField(left: 20, right: 20)
        
        emailTF.layer.cornerRadius = 10
        passTF.layer.cornerRadius = 10
        nameTF.layer.cornerRadius = 10
        usernameTF.layer.cornerRadius = 10
        cnfPassTF.layer.cornerRadius = 10
        mobileTF.layer.cornerRadius = 10
        
        emailTF.overrideUserInterfaceStyle = .light
        passTF.overrideUserInterfaceStyle = .light
        nameTF.overrideUserInterfaceStyle = .light
        usernameTF.overrideUserInterfaceStyle = .light
        cnfPassTF.overrideUserInterfaceStyle = .light
        mobileTF.overrideUserInterfaceStyle = .light

    }
    func gotoPersonalInfoVC(){
        let vc = PersonalInfoFormVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func appleLoginApi(param:[String:String]){
        self.pleaseWait()
        AuthManager.appleSignIn(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.clearAllNotice()
                self.goToTabBar()
            }
        }
    }
    func checkValidations(){
        if (nameTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyName)
            return
        }else if(usernameTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyUsername)
            return
        }
//        else if (!Validator.isValidEmail(email: usernameTF.text ?? "")){
//            ToastManager.errorToast(delegate: self, msg: LocalStrings.validUsername)
//            return
//        }
        else if (emailTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyEmail)
            return
        }else if (!Validator.isValidEmail(email: emailTF.text ?? "")){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validEmail)
            return
        }else if (mobileTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyMobile)
            return
        }else if (mobileTF.text!.count < 8 || mobileTF.text!.count > 10){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validMobile)
            return
        }else if (passTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyPassword)
            return
        }else if (!Validator.isValidPassword(value: passTF.text ?? "")){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validPassword)
            return
        }else if (passTF.text != cnfPassTF.text){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.passwordNotSame)
            return
        }else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            signUpApi()
        }
    }
    func setPlaceholder(){
        emailTF.attributedPlaceholder = NSAttributedString(string: "Email Address",attributes: [.foregroundColor: UIColor.lightGray])
        passTF.attributedPlaceholder = NSAttributedString(string: "Password",attributes: [.foregroundColor: UIColor.lightGray])
        cnfPassTF.attributedPlaceholder = NSAttributedString(string: "Confirm Password",attributes: [.foregroundColor: UIColor.lightGray])
        mobileTF.attributedPlaceholder = NSAttributedString(string: "Phone Number",attributes: [.foregroundColor: UIColor.lightGray])
        nameTF.attributedPlaceholder = NSAttributedString(string: "Name",attributes: [.foregroundColor: UIColor.lightGray])
        usernameTF.attributedPlaceholder = NSAttributedString(string: "Username",attributes: [.foregroundColor: UIColor.lightGray])
//        mobileTF.placeholder = "9876543210"
//        emailTF.placeholder = "abc@ymail.uk"
    }
    func redColorUnderline(){

        let customType = ActiveType.custom(pattern:  "SignIn")
        alreadyhaveLbl.enabledTypes.append(customType)
        alreadyhaveLbl.textColor = UIColor.white
        alreadyhaveLbl.underLineEnable = false
        alreadyhaveLbl.text = "Already have an account? SignIn"
        alreadyhaveLbl.customColor[customType] = UIColor.theme
        alreadyhaveLbl.customSelectedColor[customType] = UIColor.gray
        alreadyhaveLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        alreadyhaveLbl.handleCustomTap(for: customType) { element in
            self.navigationController?.popViewController(animated: true)
        }
    }
    @IBAction func signUpAction(_ sender: AnyObject) {
        checkValidations()
    }
}
extension SignUpVC:ASAuthorizationControllerDelegate{
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
    // Handle error.
    }
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        if let appleIDCredential = authorization.credential as?  ASAuthorizationAppleIDCredential {
            let userIdentifier = appleIDCredential.user
            let fullName = appleIDCredential.fullName
            let email = appleIDCredential.email
            let param = ["AppleId":userIdentifier,"email":email ?? "","name":(fullName?.givenName ?? "") + "" + (fullName?.familyName ?? "")]
            appleLoginApi(param: param)
            let appleIDProvider = ASAuthorizationAppleIDProvider()
            appleIDProvider.getCredentialState(forUserID: userIdentifier) {  (credentialState, error) in
                 switch credentialState {
                    case .authorized:
                     print("AUTH")
                        // The Apple ID credential is valid.
                        break
                    case .revoked:
                     print("REVOKE")
                        // The Apple ID credential is revoked.
                        break
                    case .notFound:
                     print("NONE")
                        // No credential was found, so show the sign-in UI.
                     break
                    default:
                        break
                 }
            }
           print(userIdentifier,fullName,email)
        }
    }
}
