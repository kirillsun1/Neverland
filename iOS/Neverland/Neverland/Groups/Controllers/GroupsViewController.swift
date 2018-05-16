//
//  GroupsViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import JJFloatingActionButton
import SCLAlertView

class GroupsViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    var groups = [Group]() {
        didSet {
            tableView.reloadData()
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        initFloatButton()
        tableView.dataSource = self
        tableView.delegate = self
    }
    
    override func viewDidAppear(_ animated: Bool) {
        refresh()
    }
    
    func initFloatButton() {
        let actionButton = JJFloatingActionButton()
        actionButton.buttonColor = UIColor.neverlandDarkColor
        
        actionButton.addItem(title: "Join Group", image: UIImage(named: "search_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            self.performSegue(withIdentifier: "JoinGroupSegue", sender: nil)
        }
        
        actionButton.addItem(title: "Create group", image: UIImage(named: "add_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            self.initGroupAddDialog()
        }
        
        actionButton.display(inViewController: self)
    }
    
    func refresh() {
        NLGroupApi().fetchMyGroups { dictArr in
            self.groups = []
            for json in dictArr {
                if let group = Group.init(json: json) {
                    self.groups.append(group)
                }
            }
        }
    }
    
    
    func initGroupAddDialog() {
        let alert = SCLAlertView()
        let groupNameField = alert.addTextField("Enter your name")
        alert.addButton("Create Group") {
            self.addGroup(named: groupNameField.text!)
        }
        alert.showEdit("Group", subTitle: "Create new group")
    }
    
    func addGroup(named gName: String) {
        NLGroupApi().registerGroup(g_name: gName) { response in
            switch response.code {
            case .Successful:
                self.navigationController?.popViewController(animated: true)
            case .Error:
                SCLAlertView().showError("Server error", subTitle: "Group was not added")
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "DetailedGroupSegue" {
            guard let destvc = segue.destination as? NewQuestViewController, let group = sender as? Group else {
                fatalError("ohohoh, this should not happen")
            }
            
            destvc.group = group
        }
    }
}

extension GroupsViewController: UITableViewDelegate, UITableViewDataSource {
    
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
        performSegue(withIdentifier: "DetailedGroupSegue", sender: groups[indexPath.row])
    }
}

