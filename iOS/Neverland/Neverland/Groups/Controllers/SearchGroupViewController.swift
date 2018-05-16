//
//  SearchGroupViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 10/04/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class SearchGroupViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    let groupApi = NLGroupApi()
    let questApi = NLQuestApi()
    
    var groups = [Group]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
    }

    override func viewDidAppear(_ animated: Bool) {
        refresh()
    }
    
    func refresh() {
        groupApi.fetchGroups { dictArr in
            self.groups = []
            for json in dictArr {
                if let group = Group.init(json: json) {
                    self.groups.append(group)
                }
            }
        }
    }
    
    func confirmationPopup(for group: Group) {
        NLConfirmationPopupService().presentPopup(type: "GROUP", into: self) {
            self.groupApi.takeGroup(gid: group.id) { res in
                print(res)
            }
        }
    }
}

extension SearchGroupViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return groups.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if let cell = tableView.dequeueReusableCell(withIdentifier: "GroupCell") as? GroupCell {
            cell.fillWith(groups[indexPath.row])
            return cell
        }
        
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        confirmationPopup(for: groups[indexPath.row])
    }
}
