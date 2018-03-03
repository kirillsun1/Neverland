//
//  RegistrerViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 18/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import SCLAlertView

class RegistrerViewController: UIViewController {

    @IBOutlet weak var loginLbl: RegistrationField!
    @IBOutlet weak var pwdLabel: RegistrationField!
    @IBOutlet weak var nameLbl: RegistrationField!
    @IBOutlet weak var surnameLbl: RegistrationField!
    @IBOutlet weak var emailLbl: RegistrationField!
    
    var textFields: [RegistrationField]!
    
    @IBOutlet weak var agreementBtn: Checkbox!
    
    @IBOutlet weak var registerBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loginLbl.checkRegex = "^[a-zA-Z0-9]{4,12}$"
        pwdLabel.checkRegex = "^[a-zA-Z0-9]{6,16}$"
        nameLbl.checkRegex = "^\\w+$"
        surnameLbl.checkRegex = "^\\w+$"
        emailLbl.checkRegex = "^[^@]+@[^@]+\\.[a-zA-Z0-9]+$"
        textFields = [loginLbl, pwdLabel, nameLbl, surnameLbl, emailLbl]
    }
    
    @IBAction func inputChanged() {
        var enabled = agreementBtn.isActive
        for tf in textFields {
            if !tf.isValid {
                enabled = false
            }
        }
        registerBtn.isEnabled = enabled
    }
    
    @IBAction func registerPressed(_ sender: Any) {
        dismissKeyboard()
        guard let hash = pwdLabel.text!.neverlandDefaultHash else {
            fatalError("Could not create hash of pwd while registering")
        }
        
        let rData = RegistrationData(login: loginLbl.text!,
                                     password: hash, firstName: nameLbl.text!,
                                     secondName: surnameLbl.text!, email: emailLbl.text!)
        
        FakeAuthApi().registerAccount(withData:rData) { response in
            if response.code == .Successful {
                User.sharedInstance.token = response.message
                User.sharedInstance.userName = loginLbl.text!
                performSegue(withIdentifier: "LoginSegue", sender: nil)
            } else {
                SCLAlertView().showError("Registration error", subTitle: "Username is already taken.")
            }
        }
        
    }
    
    func dismissKeyboard() {
        textFields.forEach {
            $0.resignFirstResponder()
        }
    }

    @IBAction func regCancelled() {
        dismiss(animated: true, completion: nil)
    }

    @IBAction func toggleCheckbox(_ sender: Checkbox) {
        sender.isActive = !sender.isActive
        inputChanged()
    }
}
