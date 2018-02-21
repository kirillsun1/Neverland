//
//  RegistrerViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 18/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class RegistrerViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    @IBAction func regCancelled() {
        dismiss(animated: true, completion: nil)
    }

    @IBAction func toggleCheckbox(_ sender: Checkbox) {
        sender.isActive = !sender.isActive
    }
}
