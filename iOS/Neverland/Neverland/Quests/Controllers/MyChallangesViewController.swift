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
        
        configurePlusBtn()
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
            
            for quest in questsDict {
                let title = quest.value(forKey: "title") as! String
                let description = quest.value(forKey: "desc") as! String
                let id = quest.value(forKey: "id") as! Int
                let author = Person(creatorData: quest.value(forKey: "author") as! NSDictionary)
                let time = Time(from: ((quest.value(forKey: "time_taken") as? NSDictionary)?.value(forKey: "date") as? NSDictionary))
                let quest = Quest(id: id, title: title, groupId: 0, description: description, datePicked: time, creator: author)
                self.myQuests.append(quest)
            }
            self.tableView.reloadData()
            self.refreshControl.endRefreshing()
        }
    }
    
    func configurePlusBtn() {
        let actionButton = JJFloatingActionButton()
        actionButton.buttonColor = UIColor.neverlandDarkColor
        
        actionButton.addItem(title: "Take quest", image: UIImage(named: "search_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            self.performSegue(withIdentifier: "TakeQuestSegue", sender: nil)
        }
        
        actionButton.addItem(title: "Suggest quest", image: UIImage(named: "add_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            self.performSegue(withIdentifier: "CreateQuestSegue", sender: nil)
        }

        actionButton.display(inViewController: self)
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

