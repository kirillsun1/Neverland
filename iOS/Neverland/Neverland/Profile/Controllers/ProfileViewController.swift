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
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var avatarView: UIImageView!
   
    private let questApi = NLQuestApi()
    private let profileApi = NLProfileApi()
    private var mode = ProfileFeedMode.proofs
    private var proofs = [Proof]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    private var suggestedQuests = [Quest]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    var uid: Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
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
        if uid != nil {
            getUsersData()
        } else {
            getMyData()
        }
    }
    
    func getMyData() {
        questApi.fetchMyProofs { arr in
            self.proofs = Proof.createProofsArray(from: arr)
        }
        
        profileApi.getMyInfo { userInfo in
            if let imgLink = userInfo.photoURLString {
                self.avatarView.uploadImageFrom(url: imgLink)
            }
            self.navigationItem.title = userInfo.nickname
            self.nameLbl.text = "\(userInfo.firstName) \(userInfo.secondName)"
        }
        
        profileApi.getMySuggestedQuests { jsonArr in
            self.suggestedQuests = []
            jsonArr.forEach {
                let q = Quest(fromJSON: $0)
                if let q = q {
                    self.suggestedQuests.append(q)
                }
            }
        }
    }
    
    func getUsersData() {
        questApi.fetchUserProofs(uid: uid!) { arr in
            self.proofs = Proof.createProofsArray(from: arr)
        }
        
        profileApi.getUserInfo(uid: uid!) { userInfo in
            if let imgLink = userInfo.photoURLString {
                self.avatarView.uploadImageFrom(url: imgLink)
            }
            self.navigationItem.title = userInfo.nickname
            self.nameLbl.text = "\(userInfo.firstName) \(userInfo.secondName)"
        }
        
        profileApi.getSuggestedQuests(uid: uid!) { jsonArr in
            self.suggestedQuests = []
            jsonArr.forEach {
                let q = Quest(fromJSON: $0)
                if let q = q {
                    self.suggestedQuests.append(q)
                }
            }
        }
    }
    
    func confirmationPopup(for quest: Quest) {
        NLConfirmationPopupService().presentPopup(forQuest: quest, into: self) {
            NLQuestApi().takeQuest(qid: quest.id) { _ in
                
            }
        }
    }

}

// MARK: - TableView Protocols
extension ProfileViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.mode == .proofs ? proofs.count : suggestedQuests.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        // todo: Make that there are no space above&below pic
        return self.mode == .proofs ? self.view.frame.size.width + 25 + 45 + 6 + 4*2 : 70
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
