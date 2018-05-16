//
//  ProfileViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {

    private enum ProfileFeedMode {
        case proofs, quests
    }
    
    @IBOutlet weak var actionButton: UIBarButtonItem!
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var avatarView: UIImageView!
    @IBOutlet weak var followersLbl: UIButton!
    @IBOutlet weak var followingLbl: UIButton!
    @IBOutlet weak var ratingLbl: UILabel!
   
    @IBAction func showFollowingPressed() {
        performSegue(withIdentifier: "ShowFollSegue", sender: 1)
    }
    
    @IBAction func showFollowersPressed() {
        performSegue(withIdentifier: "ShowFollSegue", sender: 0)
    }
    
    @IBAction func actionButtonPressed(_ sender: Any) {
        if uid == nil {
            performSegue(withIdentifier: "SettingsSegue", sender: nil)
        } else {
            guard let person = person else {
                fatalError("This should not happen")
            }
            
            profileApi.toggleFollowing(uid: uid!, person.isFollowedByMe ? .unfollow : .follow) {
                DispatchQueue.main.sync {
                    person.isFollowedByMe = !person.isFollowedByMe
                    let delta = person.isFollowedByMe ? 1 : -1
                    let oldValue = Int(self.followersLbl.titleLabel!.text!) ?? 0
                    self.followersLbl.titleLabel!.text = "\(oldValue + delta)"
                    self.actionButton.title = person.isFollowedByMe ? "Unfollow" : "Follow"
                }
            }
        }
    }
    
    var uid: Int?
    private let questApi = NLQuestApi()
    private let profileApi = NLProfileApi()
    private var mode = ProfileFeedMode.proofs
    private var proofs = [Proof]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    private var person: Person? {
        didSet {
            guard let person = person else { return }
    
            if let imgLink = person.photoURLString {
                avatarView.uploadImageFrom(url: imgLink)
            }

            followersLbl.setTitle("\(person.followers)", for: .normal)
            followingLbl.setTitle("\(person.followings)", for: .normal)
            if uid != nil { actionButton.title = person.isFollowedByMe ? "Unfollow" : "Follow" }
            navigationItem.title = person.nickname
            nameLbl.text = "\(person.firstName) \(person.secondName)"
            ratingLbl.text = "\(person.rating)"
        }
    }
    
    private var suggestedQuests = [Quest]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
        if uid == nil {
            actionButton.image = UIImage(named: "settings")
            actionButton.title = nil
        } else {
            actionButton.image = nil
        }
        
    }
    
    
    @IBAction func modeChanged(_ sender: UISegmentedControl) {
        self.mode = sender.selectedSegmentIndex == 0 ? .proofs : .quests
        refreshData()
        tableView.reloadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        refreshData()
    }
    
    func refreshData() {
        fetchInfo()
        fetchProofs()
        fetchSuggested()
    }
    
    func fetchProofs() {
        let completionHandler: ([NSDictionary]) -> () = { arr in
            self.proofs = Proof.createProofsArray(from: arr)
        }
        uid != nil ? questApi.fetchUserProofs(uid: uid!, onComplete: completionHandler) :  questApi.fetchMyProofs(onComplete: completionHandler)
    }
    
    func fetchInfo() {
        let completionHandler: (Person) -> () = {
            userInfo in
            self.person = userInfo
        }
        
        uid != nil ? profileApi.getUserInfo(uid: uid!, onComplete: completionHandler) : profileApi.getMyInfo(onComplete: completionHandler)
    }
    
    func fetchSuggested() {
        
        let completionHandler: ([NSDictionary]) -> () = { jsonArr in
            self.suggestedQuests = []
            jsonArr.forEach {
                let q = Quest(fromJSON: $0)
                if let q = q {
                    self.suggestedQuests.append(q)
                }
            }
        }
        
        uid != nil ? profileApi.getSuggestedQuests(uid: uid!, onComplete: completionHandler) : profileApi.getMySuggestedQuests(onComplete: completionHandler)
    }

    func confirmationPopup(for quest: Quest) {
        NLConfirmationPopupService().presentPopup(type: "QUEST", into: self) {
            NLQuestApi().takeQuest(qid: quest.id) { _ in
                
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowFollSegue", let vc = segue.destination as? PeopleListController {
            vc.thisType = (sender as! Int) == 0 ? .followers : .followings
            vc.uid = uid
        }
    }

}

// MARK: - TableView Protocols
extension ProfileViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.mode == .proofs ? proofs.count : suggestedQuests.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        var  s = self.view.frame.size.width
        s += 40 + 45 + 6 + 4*2
        return self.mode == .proofs ? s : 70
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if self.mode == .quests {
            confirmationPopup(for: suggestedQuests[indexPath.row])
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if self.mode == .proofs {
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "FeedCell", for: indexPath) as? FeedCell else {
                fatalError()
            }
            
            cell.fillWith(proofs[indexPath.row])
            return cell
        } else {
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "QuestCell", for: indexPath) as? QuestCell else {
                fatalError()
            }
            
            cell.fillWith(quest: suggestedQuests[indexPath.row])
            return cell
        }
    }
    
    
}
