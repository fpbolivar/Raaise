//
//  LoginVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 09/11/22.
//

import UIKit
import FacebookLogin
import GoogleSignIn
import AuthenticationServices
class LoginVC: BaseControllerVC {


    @IBOutlet weak var appleSignInLbl: UILabel!
    @IBOutlet weak var aplleLoginBtn: UIButton!
    @IBOutlet weak var appleLoginBtnView: CardView!
    @IBOutlet weak var fbLbl: UILabel!
    @IBOutlet weak var eyeImg: UIImageView!
    @IBOutlet weak var  orSignInWithLbl:UILabel!
    @IBOutlet weak var  donthaveLbl:UILabel!

    @IBOutlet weak var  forgotBtn:UIButton!
    @IBOutlet weak var  loginLbl:UILabel!
    @IBOutlet weak var  signInBtn:UIButton!
    @IBOutlet weak var  emailTF:UITextField!
    @IBOutlet weak var  passTF:UITextField!
    @IBOutlet weak var  googleLbl:UILabel!
    @IBOutlet weak var  donthaveAccountLbl:ActiveLabel!
    override func viewDidLoad() {
        
        super.viewDidLoad()
        hideNavbar()
        setfonts()
        setPlaceholder()
        redColorUnderline()
        aplleLoginBtn.addTarget(self, action: #selector(handleAppleIdRequest), for: .touchUpInside)
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(true, animated: false)
    }
    @objc func handleAppleIdRequest() {
    let appleIDProvider = ASAuthorizationAppleIDProvider()
    let request = appleIDProvider.createRequest()
    request.requestedScopes = [.fullName, .email]
    let authorizationController = ASAuthorizationController(authorizationRequests: [request])
    authorizationController.delegate = self
    authorizationController.performRequests()
    }
    @IBAction func googleLogin(_ sender: Any) {
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        googleLogin()
    }
    @IBAction func facebookLogin(_ sender: Any) {
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        fbLogin()
    }
    //MARK: - APi Methods
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
        fbLoginManager.logOut()
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
    func appleLoginApi(param:[String:String]){
        self.pleaseWait()
        AuthManager.appleSignIn(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.clearAllNotice()
                self.goToTabBar()
            }
        }
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
          }
        })
      }
    }
    func goToTabBar(){
        let vc  = MainTabBarVC()
        UIApplication.keyWin!.rootViewController = vc
        UIApplication.keyWin!.makeKeyAndVisible()
    }
    func loginApi(){
        let param = ["email":emailTF.text ?? "","password":passTF.text ?? "","deviceToken":UserDefaultHelper.getDevice_Token()]
        AuthManager.loginApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.goToTabBar()
            }
        }
    }
    //MARK: -Validation
    func checkValidations(){
        if (emailTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyEmail+" or Username")
            return
        }
        else if(passTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyPassword)
            return
        }
        else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            loginApi()
        }
    }
    //MARK: - Setup
    func setfonts(){
        donthaveLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        eyeImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(showPassword)))
        orSignInWithLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        orSignInWithLbl.textColor = .white
        googleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        appleSignInLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        fbLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        forgotBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX12)
        signInBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX14)
        passTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        emailTF.overrideUserInterfaceStyle = .light
        passTF.overrideUserInterfaceStyle = .light
        emailTF.layer.cornerRadius = 10
        passTF.layer.cornerRadius = 10
        emailTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        emailTF.paddingLeftRightTextField(left: 20, right: 20)
        passTF.paddingLeftRightTextField(left: 20, right: 20)
        loginLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX18)
       
    }
    @objc func showPassword(){
        passTF.isSecureTextEntry = !passTF.isSecureTextEntry
        if passTF.isSecureTextEntry{
            eyeImg.image = UIImage(systemName: "eye")
        }else{
            eyeImg.image = UIImage(systemName: "eye.slash")
        }
    }
    func setPlaceholder(){
        emailTF.attributedPlaceholder = NSAttributedString(string: "Email Address or Username",attributes: [.foregroundColor: UIColor.lightGray])
        passTF.attributedPlaceholder = NSAttributedString(string: "Password",attributes: [.foregroundColor: UIColor.lightGray])
    }
    func redColorUnderline(){

        let customType = ActiveType.custom(pattern:  "SignUp")
        donthaveAccountLbl.enabledTypes.append(customType)
        donthaveAccountLbl.textColor = UIColor.white
        donthaveAccountLbl.underLineEnable = false
        donthaveAccountLbl.text = "You don’t have an account ?SignUp"
        donthaveAccountLbl.customColor[customType] = UIColor.theme
        donthaveAccountLbl.customSelectedColor[customType] = UIColor.gray
        donthaveAccountLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        donthaveAccountLbl.handleCustomTap(for: customType) { element in
                        let destination = SignUpVC()
                        self.navigationController?.pushViewController(destination, animated: true)

        }

    }
    @IBAction func forgotAction(_ sender: AnyObject) {
        let vc = ForgotVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func loginAction(_ sender: AnyObject) {
        checkValidations()
        
    }
}
//MARK: - Apple Sign In Delegate
extension LoginVC:ASAuthorizationControllerDelegate{
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
        }
    }
}
