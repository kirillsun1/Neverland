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
    
    private var textFields: [RegistrationField]!
    
    @IBOutlet weak var agreementBtn: Checkbox!
    
    @IBOutlet weak var registerBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loginLbl.checkRegex = "^[a-z0-9_-]{6,16}$$"
        pwdLabel.checkRegex = "^[a-z0-9_-]{6,18}$"
        nameLbl.checkRegex = "^[A-Za-z ,.'-]+$"
        surnameLbl.checkRegex = "^[A-Za-z ,.'-]+$"
        emailLbl.checkRegex = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$"
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
        
        // optionals can be force-unwrapped securely, cause to get into this function, all fields should be filled.
        
        let rData = RegistrationData(login: loginLbl.text!,
                                     password: hash, firstName: nameLbl.text!,
                                     secondName: surnameLbl.text!, email: emailLbl.text!)
        
        NLAuthApi().registerAccount(withData:rData) { response in
            if response.code == .Successful {
                User.sharedInstance.token = response.message
                User.sharedInstance.userName = self.loginLbl.text!
                self.performSegue(withIdentifier: "LoginSegue", sender: nil)
            } else {
                SCLAlertView().showError("Registration error", subTitle: "Username is already taken.")
            }
        }
        
    }
    
    override func dismissKeyboard() {
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
