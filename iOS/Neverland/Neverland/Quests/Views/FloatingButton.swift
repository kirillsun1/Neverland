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
    
    func add(intoViewController vc: UIViewController) {
        let actionButton = JJFloatingActionButton()
        actionButton.buttonColor = UIColor.neverlandDarkColor

        actionButton.addItem(title: "Take quest", image: UIImage(named: "search_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            vc.performSegue(withIdentifier: "TakeQuestSegue", sender: nil)
        }

        
        actionButton.addItem(title: "Suggest quest", image: UIImage(named: "add_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            vc.performSegue(withIdentifier: "CreateQuestSegue", sender: nil)
        }

        actionButton.display(inViewController: vc)
    }
}
