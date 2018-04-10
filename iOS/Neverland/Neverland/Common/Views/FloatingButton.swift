//
//  FloatingButton.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 10/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import UIKit
import JJFloatingActionButton

class FloatingButton {
    
    enum BtnType {
        case quest, group
    }
    
    
    func add(intoViewController vc: UIViewController, type: BtnType) {
        let actionButton = JJFloatingActionButton()
        actionButton.buttonColor = UIColor.neverlandDarkColor
        
        let takeTitle = type == .quest ? "Take quest" : "Join Group"
        let takeSegueTitle = type == .quest ? "TakeQuestSegue" : "JoinGroupSegue"
        
        let addTitle = type == .quest ? "Suggest quest" : "Create group"
        let addSegueTitle = type == .quest ? "CreateQuestSegue" : "CreateGroupSegue"

        actionButton.addItem(title: takeTitle, image: UIImage(named: "search_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            vc.performSegue(withIdentifier: takeSegueTitle, sender: nil)
        }

        actionButton.addItem(title: addTitle, image: UIImage(named: "add_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            vc.performSegue(withIdentifier: addSegueTitle, sender: nil)
        }

        actionButton.display(inViewController: vc)
    }
}
