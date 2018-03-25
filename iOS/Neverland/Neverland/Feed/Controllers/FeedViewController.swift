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
    var dropDown: DropDown!
    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        dropDown = DropDown()
        dropDown.anchorView = mainView // UIView or UIBarButtonItem
        dropDown.dataSource = ["World", "Following", "Groups"]
        
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
    
    

    @IBAction func changeTypePressed() {
        dropDown.show()
    }
    
    func refreshProofs() {
        questApi.fetchAllProofs(onComplete: { arr in
            self.proofs = Proof.createProofsArray(from: arr)
        })
    }

}

extension FeedViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return proofs.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        // todo: Make that there are no space above&below pic
        return self.view.frame.size.width + 25 + 45 + 6 + 4*2
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "FeedCell") as? FeedCell else {
            fatalError()
        }
        
        cell.fillWith(proofs[indexPath.row])
        return cell
    }
    
}

