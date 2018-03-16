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
    
    @IBOutlet weak var tableView: UITableView!
    private var quests = [Quest]() {
        didSet {
            tableView.reloadData()
        }
    }
    private let spinner = UIActivityIndicatorView.init(activityIndicatorStyle: .gray)
    let groupId = 0 // get through segue.

    
    override func viewDidLoad() {
        super.viewDidLoad()

        spinner.stopAnimating()
        spinner.hidesWhenStopped = true
        spinner.frame = CGRect(x: 0, y: 0, width: 320, height: 44)
        
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = spinner
        
        
        fetchAllQuests()
        
        self.tableView.es.addPullToRefresh {
            [unowned self] in
            
            self.quests = []
            self.fetchAllQuests()
            
            self.tableView.reloadData()
            self.tableView.es.stopPullToRefresh(ignoreDate: true)
            self.tableView.es.stopPullToRefresh(ignoreDate: true, ignoreFooter: false)
        }
    }
    
    func confirmationPopup(for quest: Quest) {
        NLConfirmationPopupService().presentPopup(forQuest: quest, into: self) {
            NLQuestApi().takeQuest(qid: quest.id) { _ in
                
            }
        }
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
        NLQuestApi().fetchQuests(inGroup: 0) { questsDictionary in
            for questJson in questsDictionary {
                if let quest = Quest(fromJSON: questJson) {
                    self.quests.append(quest)
                }
            }
        }
    }

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
