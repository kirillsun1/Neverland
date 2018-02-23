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

    @IBOutlet weak var loginLbl: UITextField!
    @IBOutlet weak var pwdLabel: UITextField!
    @IBOutlet weak var nameLbl: UITextField!
    @IBOutlet weak var surnameLbl: UITextField!
    @IBOutlet weak var emailLbl: UITextField!
    @IBOutlet weak var agreementBtn: Checkbox!
    
    @IBOutlet weak var registerBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }
    
    // todo: button disabled design + alerts
    // todo: make validation work !!!
    @IBAction func inputChanged() {
        registerBtn.isEnabled = isValid(loginLbl) &&
                                isValid(pwdLabel) &&
                                isValid(nameLbl) &&
                                isValid(surnameLbl) &&
                                isValid(emailLbl) &&
                                agreementBtn.isActive
    }
    
    @IBAction func registerPressed(_ sender: Any) {
        dismissKeyboard()
        guard let hash = pwdLabel.text!.neverlandDefaultHash else {
            fatalError("Could not create hash of pwd while registering")
        }
        
        let response = FakeAuthApi().registerAccount(withData:
            RegistrationData(login: loginLbl.text!, password: hash,
                            firstName: nameLbl.text!, secondName: surnameLbl.text!,
                            email: emailLbl.text!)
        )
        
        if response.code == .Successful {
            User.sharedInstance.token = response.message
            User.sharedInstance.userName = loginLbl.text!
            performSegue(withIdentifier: "LoginSegue", sender: nil)
        } else {
            SCLAlertView().showError("Registration error", subTitle: "Username is already taken.")

        }
        
    }
    
    func dismissKeyboard() {
        loginLbl.resignFirstResponder()
        pwdLabel.resignFirstResponder()
        nameLbl.resignFirstResponder()
        surnameLbl.resignFirstResponder()
        emailLbl.resignFirstResponder()

    }
    
    func isValid(_ field: UITextField) -> Bool {
        return field.text != nil && !field.text!.isEmpty
    }

    @IBAction func regCancelled() {
        dismiss(animated: true, completion: nil)
    }

    @IBAction func toggleCheckbox(_ sender: Checkbox) {
        sender.isActive = !sender.isActive
        inputChanged()
    }
}
