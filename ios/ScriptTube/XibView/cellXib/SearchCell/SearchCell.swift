//
//  SearchCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/12/22.
//

import UIKit

protocol SelectedSearchItemDelegate{
    func gotoResultPage(resultType:ResultType)
    func openProfileWithId(id:String)
    func tryAudioWithAudio(audio:AudioDataModel)
    func viewVideo(posts:[Post],selectedIndex:Int)
}

class SearchCell: UITableViewCell {

    @IBOutlet weak var headingStack: UIStackView!
    @IBOutlet weak var collectionViewHeight: NSLayoutConstraint!
    static var identifier = "SearchCell"
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var seeAllBtn: UIButton!
    @IBOutlet weak var nameLbl: UILabel!
    var cellType: ResultType!
    var delegate:SelectedSearchItemDelegate?
    var audioResult:[AudioDataModel] = []
    var postResult:[Post] = []
    var userResult:[UserProfileData] = []
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        selectionStyle = .none
        collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
        collectionView.register(UINib(nibName: UserResultCell.identifier, bundle: nil), forCellWithReuseIdentifier: UserResultCell.identifier)
        collectionView.register(UINib(nibName: AudioResult.identifier, bundle: nil), forCellWithReuseIdentifier: AudioResult.identifier)
        seeAllBtn.addTarget(self, action: #selector(seeAll), for: .touchUpInside)
        
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @objc func seeAll(){
        delegate?.gotoResultPage(resultType: self.cellType)
    }
    //MARK: - Setup
    func setupSearchCell(withType type :ResultType,data:[Any]){
        if data.isEmpty{
            headingStack.isHidden = true
        }else{
            headingStack.isHidden = false
        }
        if data.count < 5{
            seeAllBtn.isHidden = true
        }else{
            seeAllBtn.isHidden = false
        }
        self.cellType = type
        switch cellType{
        case .users:
            self.userResult = data as! [UserProfileData]
            nameLbl.text = "Users"
            break
        case .audio:
            self.audioResult = data as! [AudioDataModel]
            nameLbl.text = "Audio"
            break
        case .post:
            self.postResult = data as! [Post]
            nameLbl.text = "Recommended Videos"//"Trending"
        case .none:
            print("NO SUCH Type")
        }
        setupCollectionView()
    }
    func setupCollectionView(){
        collectionView.dataSource = self
        collectionView.delegate = self
        collectionView.reloadData()
    }
    
}
//MARK: - Collection View Delegates
extension SearchCell:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
        switch cellType{
        case .audio:
            return min(4, self.audioResult.count)
        case .post:
            return min(6, self.postResult.count)
        case .users:
            return min(4, self.userResult.count)
        case .none:
            return 0
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch cellType{
        case .post:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as!  ProfileVideoItemCell
            cell.updateCellData(data: self.postResult[indexPath.row])
            return cell
        case .users:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UserResultCell.identifier, for: indexPath) as!  UserResultCell
            cell.layoutIfNeeded()
            cell.profileImage.layer.cornerRadius = cell.profileImage.bounds.height / 2
            cell.updateCell(data: self.userResult[indexPath.row])
            return cell
        case .audio:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AudioResult.identifier, for: indexPath) as! AudioResult
            cell.updateCell(data: self.audioResult[indexPath.row])
            return cell
        case .none:
            return UICollectionViewCell()
        }
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch cellType{
        case .post:
            let wFrame = collectionView.frame.width
            let itemWidth = (wFrame/3) - 2
            let itemHeight =  itemWidth * 1.3
            print("SERCHCEKK",itemWidth,itemHeight)
            return CGSize.init(width: itemWidth, height: itemHeight)
        case .audio:
            let wFrame = collectionView.frame.width
            let itemWidth = (wFrame/2) - 15
            let itemHeight =  45.0
            print("SERCHCEKK",itemWidth,itemHeight)
            return CGSize.init(width: itemWidth, height: itemHeight)
        case .users:
            let wFrame = collectionView.frame.width
            let itemWidth = (wFrame/4) - 15
            let itemHeight =  itemWidth
            print("SERCHCEKK",itemWidth,itemHeight + 15.0)
            return CGSize.init(width: itemWidth, height: itemHeight + 10.0)
        default:
            return CGSize.init(width: 0, height: 0)
        }
        
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        switch cellType{
        case .audio:
            return 15
        case .post:
            return 2
        case .users:
            return 15
        case .none:
            return 0
        }
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        switch cellType{
        case .audio:
            return 15
        case .post:
            return 2
        case .users:
            return 15
        case .none:
            return 0
        }
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        switch cellType{
        case.users:
            print("aloo")
            self.delegate?.openProfileWithId(id: self.userResult[indexPath.row].id)
            break
        case.post:
            print("aloo1")
            self.delegate?.viewVideo(posts: self.postResult,selectedIndex: indexPath.row)
            break
        case.audio:
            print("aloo2")
            self.delegate?.tryAudioWithAudio(audio: self.audioResult[indexPath.row])
            break
        case.none:
            print("aloo3")
            break
        }
    }
}
