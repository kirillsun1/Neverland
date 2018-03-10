//
//  MyChallangesViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import JJFloatingActionButton

class MyChallangesViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    var myQuests = [Quest]()
    let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(refresh(_:)), for: .valueChanged)
        
        FloatingButton().add(intoViewController: self)
        fetchMyQuests()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        fetchMyQuests()
    }
    
    @objc func refresh(_ refreshControl: UIRefreshControl) {
        fetchMyQuests()
    }
    
    
    func fetchMyQuests() {
        NLQuestApi().fetchMyQuests { questsDict in
            
            self.myQuests = []

            for questJSON in questsDict {
                if let quest = Quest(fromJSON: questJSON) {
                    self.myQuests.append(quest)
                }
            }
            self.tableView.reloadData()
            self.refreshControl.endRefreshing()
        }
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "QuestDetailed" {
            if let dest = segue.destination as? QuestDetailedViewController {
                dest.quest = sender as? Quest
            }
        }
    }

}

extension MyChallangesViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return myQuests.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "QuestCell") as? QuestCell else {
            fatalError()
        }
        
        cell.fillWith(quest: myQuests[indexPath.row])
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "QuestDetailed", sender: myQuests[indexPath.row])
    }
}

