//
//  FeedViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import DropDown

class FeedViewController: UIViewController {
    
    let questApi = NLQuestApi()
    var proofs = [Proof]() {
        didSet {
            self.tableView.reloadData()
        }
    }
    
    let scopes = ["World", "Follow", "Groups"]
    var dropDown: DropDown!
    var scopeInd = 0 {
        didSet {
           self.scopeLbl.setTitle(scopes[scopeInd] + " ▾", for: .normal)
            refreshProofs()
        }
    }
    
    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var scopeLbl: UIButton!
    
    private var refreshCompletionParser: (([NSDictionary]) -> ())!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        dropDown = DropDown()
        dropDown.anchorView = mainView // UIView or UIBarButtonItem
        dropDown.dataSource = scopes
        
        dropDown.selectionAction = { [unowned self] (index: Int, item: String) in
            self.scopeInd = index
            self.refreshProofs()
            
        }
        
        
        refreshCompletionParser = { arr in
            self.proofs = Proof.createProofsArray(from: arr)
        }
        
        tableView.dataSource = self
        tableView.delegate = self
        
        refreshProofs()
        
        self.tableView.es.addPullToRefresh {
            [unowned self] in
            self.refreshProofs()
            self.tableView.es.stopPullToRefresh(ignoreDate: true)
            self.tableView.es.stopPullToRefresh(ignoreDate: true, ignoreFooter: false)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        refreshProofs()
    }
    
    @IBAction func segueButtonTapped(_ sender: UIButton!) {
        performSegue(withIdentifier: "UserDetailed", sender: sender.tag)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "UserDetailed" {
            let vc = segue.destination as! ProfileViewController
            vc.uid = (sender as! Int)
        }
    }

    @IBAction func changeTypePressed() {
        dropDown.show()
    }
    
    func refreshProofs() {
        
        switch scopeInd {
        case 0:
            questApi.fetchAllProofs(onComplete: refreshCompletionParser)
        case 1:
            questApi.fetchFollowingProofs(onComplete: refreshCompletionParser)
        case 2:
            questApi.fetchGroupsProofs(onComplete: refreshCompletionParser)
        default:
            self.proofs = []
        }
    }
}

extension FeedViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return proofs.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        // todo: Make that there are no space above&below pic
        let s = self.view.frame.size.width
        return s + 40 + 45 + 6 + 4*2
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "FeedCell") as? FeedCell else {
            fatalError()
        }
        
        cell.fillWith(proofs[indexPath.row])
        return cell
    }
    
}

