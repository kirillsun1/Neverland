//
//  SettingsViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 30/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class SettingsViewController: UITableViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func logoutPressed() {
        User.sharedInstance.logout()
        let storyboard = UIStoryboard.init(name: "Auth", bundle: nil)
        let vc = storyboard.instantiateInitialViewController()
        self.present(vc!, animated: true, completion: nil)
    }
    
    @IBAction func changePhotoPressed() {
        print("photo")
    }

}
