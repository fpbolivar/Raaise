//
//  OnboardingBaseController.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 7/7/22.
//

import UIKit

class OnboardingDataModal{
    var title = ""
    var title2 = ""
    var redTitle = ""
    var description = ""
    var img = UIImage()
    init(title:String="",description:String="",img:UIImage=UIImage()){
        self.title = title
        self.description = description
        self.img = img
    }
    init(title:String="",title2:String="",redTitle:String="",description:String="",img:UIImage=UIImage()){
        self.title = title
        self.title2 = title2
        self.redTitle = redTitle
        self.description = description
        self.img = img
    }
}
class OnboardingBaseController: UIViewController,UIScrollViewDelegate {
    var arrayOnboarding = [OnboardingDataModal]()
    @IBOutlet weak var  nextBtn:UIButton!
    @IBOutlet weak var  skipBtn:UIButton!
    @IBOutlet var holderScrollView: UIScrollView!
    @IBOutlet weak var progressBar: CircleProgress!
        var pageno = 0
    @IBOutlet weak var pageControl: SCPageControlView!
    @IBOutlet weak var pageControlHolder: UIView!
    var screenWidth : CGFloat = UIScreen.main.bounds.size.width
    var screenHeight : CGFloat = UIScreen.main.bounds.size.height
    var currentProgress: Float = 0.5
    lazy var pageControll: ExtendedpageControll = {
        let pc = ExtendedpageControll(numberOfPages:arrayOnboarding.count, currentPage: 0,isCircular: false)
        pc.currentIndicatorColor = .white
        return pc
    }()
    override func viewDidLoad() {
        super.viewDidLoad()
        //setNavBarColor()
        //progressBar.forgroundColor = UIColor.theme
        progressBar.forgroundColor = UIColor.new_theme
        updateProgress(index:0);
        arrayOnboarding = [
            OnboardingDataModal(title: "Get Inspration",
                                title2:"For Your Next Clips.",redTitle: "Clips.",description: "The worlds first Personal Security tool powered by Artificial Intelligence.", img: UIImage(named:"onBoarding1")!),
            OnboardingDataModal(title: "Get Inspration",
                                title2:"For Your Next Clips.",redTitle: "Clips.",description: "The worlds first Personal Security tool powered by Artificial Intelligence.", img: UIImage(named:"onBoarding1")!),

        ]
        holderScrollView.delegate = self
        setfonts()
        pageControl.scp_style = .SCNormal
        self.navigationController?.setNavigationBarHidden(true, animated: true)

        //setup()
        // Do any additional setup after loading the view.
    }
    override func viewDidAppear(_ animated: Bool) {
        setup()
    }

    func setfonts(){


        nextBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        skipBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)

    }
    func setup(){

        pageControlHolder.addSubview(pageControll)
        pageControll.translatesAutoresizingMaskIntoConstraints = false
        pageControlHolder.addConstraints([NSLayoutConstraint(item: pageControll, attribute: .leading, relatedBy: .equal, toItem: pageControlHolder, attribute: .leading, multiplier: 1.0, constant: 0),
                                   NSLayoutConstraint(item: pageControll, attribute: .trailing, relatedBy: .equal, toItem: pageControlHolder, attribute: .trailing, multiplier: 1.0, constant: 0.0),
                                   NSLayoutConstraint(item: pageControll, attribute: .top, relatedBy: .equal, toItem: pageControlHolder, attribute: .top, multiplier: 1.0, constant: 0.0),
                                   NSLayoutConstraint(item: pageControll, attribute: .bottom, relatedBy: .equal, toItem: pageControlHolder, attribute: .bottom, multiplier: 1.0, constant: 0.0)])

        holderScrollView.frame = CGRect(x: 0, y: 50, width: holderScrollView.frame.width, height: holderScrollView.frame.height-50)
        pageControlHolder.isHidden = true
        pageControl.numberOfPage = arrayOnboarding.count
        //pageControl.set_view(arrayOnboarding.count, current: 0, current_color: UIColor.theme)
        pageControl.set_view(arrayOnboarding.count, current: 0, current_color: UIColor.new_theme)
        holderScrollView.isPagingEnabled =  true
        holderScrollView.contentSize = CGSize(width:holderScrollView.frame.width*CGFloat(arrayOnboarding.count) , height: holderScrollView.frame.height)
        for  index in arrayOnboarding.indices {
          let   vc =  (Bundle.main.loadNibNamed("OnboardingView", owner: self, options: nil)?.first as! OnboardingView)
            setupView(view:vc,index:index)
        }
        pageControl.scroll_did(holderScrollView)

    }
    func setupView(view:OnboardingView,index:Int){
        view.config(data: arrayOnboarding[index])
        view.frame = CGRect(x: holderScrollView.frame.width*CGFloat(index), y:0, width: holderScrollView.frame.width, height: holderScrollView.frame.height)
        holderScrollView.addSubview(view)
    }
    @IBAction func skipBtnAction(_ sender: AnyObject) {
        gotoLogin();
    }
    func gotoLogin(){
        let vc  =  UINavigationController(rootViewController: LoginVC())
        UIApplication.keyWin?.rootViewController = vc
        UIApplication.keyWin?.makeKeyAndVisible()
    }
    @IBAction func nextBtnAction(_ sender: AnyObject) {

        if((pageno==1)){
            let vc  =  UINavigationController(rootViewController: LoginVC())
            UIApplication.keyWin?.rootViewController = vc
            UIApplication.keyWin?.makeKeyAndVisible()
        }else{
        holderScrollView.scrollRectToVisible(CGRect(x: CGFloat(pageno+1)*holderScrollView.frame.width, y: 0, width: holderScrollView.frame.width, height:  holderScrollView.frame.height), animated: true)
        }

    }
    func scrollViewDidScroll(_ scrollView: UIScrollView) {

        let pageIndex = round(scrollView.contentOffset.x/view.frame.width)
          pageno = Int(pageIndex)
        print(pageno)
        pageControll.currentpage = pageno
        updateProgress(index:pageno);
        pageControl.scroll_did(scrollView)

    }
    func updateProgress(index:Int){
        if(index == 0){
            progressBar.progress = CGFloat(0.5)
        }else{
            progressBar.progress = CGFloat(1)
        }
    }

}

class ExtendedpageControll: UIView{
    var numberOfPage: Int
    var currentpage : Int                  = 0{didSet{reloadView()}}
    var currentIndicatorColor: UIColor     = .black
    var indicatorColor: UIColor            = UIColor(white: 0.9, alpha: 1)
    var circleIndicator: Bool              = false
    private var dotView                    = [UIView]()
    private let spacing: CGFloat           = 1
    private lazy var  extraWidth: CGFloat  = circleIndicator ? 6 : 20

    init(numberOfPages: Int,currentPage: Int,isCircular: Bool){
        self.numberOfPage    = numberOfPages
        self.currentpage     = currentPage
        self.circleIndicator = isCircular
        super.init(frame: .zero)
        configView()
    }
    required init?(coder: NSCoder) {fatalError("not implemented")}

    private func configView(){
        backgroundColor = .clear
        (0..<numberOfPage).forEach { _ in
            let view = UIView()
            addSubview(view)
            dotView.append(view)
        }
    }

    private func reloadView(){
        dotView.forEach{$0.backgroundColor = indicatorColor}
        dotView[currentpage].backgroundColor = currentIndicatorColor
        UIView.animate(withDuration: 0.2) {
            self.dotView[self.currentpage].frame.origin.x   = self.dotView[self.currentpage].frame.origin.x - self.extraWidth
            self.dotView[self.currentpage].frame.size.width = self.dotView[self.currentpage].frame.size.width + (self.extraWidth * 2)
        }
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        print("DOT,layoutSubviews")
        for (i,view) in dotView.enumerated(){
            view.clipsToBounds      = true
            view.layer.cornerRadius = bounds.height / 2
            var width: CGFloat      = circleIndicator ? self.bounds.height : CGFloat(self.bounds.width / CGFloat(self.numberOfPage) - self.spacing) - self.extraWidth
            UIView.animate(withDuration: 0.2) {
                view.frame = CGRect(x: ((self.bounds.width / CGFloat(self.numberOfPage)) * CGFloat(i)) + self.spacing, y: 0, width: width , height: self.bounds.height)
            }
        }
        reloadView()
    }
}
