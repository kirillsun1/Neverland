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
    var quests = [Quest]()
    var startScrollViewHeight: CGFloat!
    let spinner = UIActivityIndicatorView.init(activityIndicatorStyle: .gray)
    let groupId = 0 // get through segue.
    
    override func viewDidLoad() {
        super.viewDidLoad()

        spinner.stopAnimating()
        spinner.hidesWhenStopped = true
        spinner.frame = CGRect(x: 0, y: 0, width: 320, height: 44)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = spinner
        
        fetchNewQuests()
    }
    
    func confirmationPopup(for quest: Quest) {
       NLConfirmationPopupService().presentPopup(forQuest: quest, into: self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        startScrollViewHeight = tableView.contentSize.height
    }
    
    func fetchNewQuests() {
        FakeQuestApi().fetchQuests(from: quests.count, to: quests.count + 20, inGroup: groupId, onComplete: { response in
            var indexes = [IndexPath]()
            for index in 0 ..< response.quests.count {
                let q = response.quests[index]
                quests.append(q)
                indexes.append(IndexPath.init(row: quests.count - 1, section: 0))
            }
            tableView.insertRows(at: indexes, with: .automatic)
            
            spinner.stopAnimating()
        })
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

    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let threshold = scrollView.contentOffset.y + scrollView.frame.size.height
        
        if threshold > scrollView.contentSize.height {
            spinner.startAnimating()
        }
            
        if threshold == scrollView.contentSize.height {
            fetchNewQuests()
        }
    }
}