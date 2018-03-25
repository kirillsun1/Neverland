//
//  ProfileViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    let questApi = NLQuestApi()
    var proofs = [Proof]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = User.sharedInstance.userName!
        
        tableView.delegate = self
        tableView.dataSource = self
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        refreshProofs()
    }

    @IBAction func configBtnPressed(_ sender: Any) {
        // later will be config menu, now log out func
        User.sharedInstance.logout()
        
        let storyboard = UIStoryboard.init(name: "Auth", bundle: nil)
        let vc = storyboard.instantiateInitialViewController()
        self.present(vc!, animated: true, completion: nil)
    }
    
    func refreshProofs() {
        questApi.fetchMyProofs { arr in
            self.proofs = Proof.createProofsArray(from: arr)
        }
    }
    

}

// MARK: - CollectionViewProtocols
extension ProfileViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return proofs.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        // todo: Make that there are no space above&below pic
        return self.view.frame.size.width + 25 + 45 + 6 + 4*2
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "FeedCell", for: indexPath) as? FeedCell else {
            fatalError()
        }
        
        cell.fillWith(proofs[indexPath.row])
        return cell
    }
    
    
}
