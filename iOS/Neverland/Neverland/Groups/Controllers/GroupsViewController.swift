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

    override func viewDidLoad() {
        super.viewDidLoad()

        initFloatButton()
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
    

}
