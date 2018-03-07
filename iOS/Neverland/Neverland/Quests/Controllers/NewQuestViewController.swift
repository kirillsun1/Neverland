//
//  NewQuestViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 17/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import PopupDialog

class NewQuestViewController: UIViewController {

    let refreshControl = UIRefreshControl()
    
    @IBOutlet weak var tableView: UITableView!
    var quests = [Quest]() {
        didSet {
            tableView.reloadData()
        }
    }
    var startScrollViewHeight: CGFloat!
    let spinner = UIActivityIndicatorView.init(activityIndicatorStyle: .gray)
    let groupId = 0 // get through segue.
//    var deletedQuests = 0
//    var tableViewisUpdating = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        spinner.stopAnimating()
        spinner.hidesWhenStopped = true
        spinner.frame = CGRect(x: 0, y: 0, width: 320, height: 44)
        
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = spinner
        
        refreshControl.addTarget(self, action: #selector(refresh(_:)), for: .valueChanged)
        
        tableView.refreshControl = refreshControl
        
        fetchAllQuests()
    }
    
    func confirmationPopup(for quest: Quest) {
        NLConfirmationPopupService().presentPopup(forQuest: quest, into: self) {
            NLQuestApi().takeQuest(qid: quest.id) { _ in
                
            }
        }
    }
    
    @objc func refresh(_ refreshControl: UIRefreshControl) {
        quests = []
        fetchAllQuests()
    }
    
    func removeQuest(quest: Quest) {
        tableView.beginUpdates()
        if let index = quests.index(of: quest) {
            tableView.deleteRows(at: [IndexPath.init(row: index, section: 0)], with: .automatic)
            quests.remove(at: index)
        }
        tableView.endUpdates()
    }
    
    func fetchAllQuests() {
        NLQuestApi().fetchAllQuestsWeShouldProbablyDeleteThisMethodLaterOmg { questsDictionary in
            //print(questsDictionary)
            for quest in questsDictionary {
                let title = quest.value(forKey: "title") as! String
                let description = quest.value(forKey: "desc") as! String
                let id = quest.value(forKey: "id") as! Int
                let author = Person(creatorData: quest.value(forKey: "author") as! NSDictionary)
                let quest = Quest(id: id, title: title, groupId: 0, description: description, datePicked:nil, creator: author)
                self.quests.append(quest)
            }
            self.tableView.reloadData()
            self.refreshControl.endRefreshing()
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        startScrollViewHeight = tableView.contentSize.height
    }
    
//    func fetchNewQuests() {
////        tableViewisUpdating = true
//        FakeQuestApi().fetchQuests(from: quests.count + deletedQuests, to: quests.count + deletedQuests + 20, inGroup: groupId, onComplete: { response in
//            self.tableView.beginUpdates()
//            var indexes = [IndexPath]()
//            for index in 0 ..< response.quests.count {
//                let q = response.quests[index]
//                self.quests.append(q)
//                indexes.append(IndexPath.init(row: self.quests.count - 1, section: 0))
//            }
//            self.tableView.insertRows(at: indexes, with: .automatic)
//
//            self.spinner.stopAnimating()
//            self.tableView.endUpdates()
//            self.tableViewisUpdating = false
//        })
//    }

}

extension NewQuestViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return quests.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "QuestInfoCell") as? QuestInfoCell else {
            fatalError()
        }
        
        cell.fill(quest: quests[indexPath.row])
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        confirmationPopup(for: quests[indexPath.row])
    }

//    func scrollViewDidScroll(_ scrollView: UIScrollView) {
//        let threshold = scrollView.contentOffset.y + scrollView.frame.size.height
//
//        if threshold > scrollView.contentSize.height {
//            spinner.startAnimating()
//        }
//
//        if threshold == scrollView.contentSize.height {
//            if !tableViewisUpdating {
//                fetchNewQuests()
//            }
//        }
//    }
}
