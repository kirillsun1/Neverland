//
//  AuthViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import SCLAlertView

class AuthViewController: UIViewController {

    let user = User.sharedInstance
    let api = NLAuthApi()
    
    @IBOutlet weak var stackViewWidthConstr: NSLayoutConstraint!
    @IBOutlet weak var stackViewHeightConstr: NSLayoutConstraint!
    @IBOutlet weak var stackViewBotomOffsetConstr: NSLayoutConstraint!
    @IBOutlet weak var logoTopConstr: NSLayoutConstraint!
    @IBOutlet weak var logoHeightConstr: NSLayoutConstraint!

    @IBOutlet weak var passwordField: RegistrationField!
    @IBOutlet weak var userField: RegistrationField!
    
    var defaultVerticalConstr: CGFloat! {
        didSet {
            stackViewBotomOffsetConstr.constant = defaultVerticalConstr
        }
    }
    
    var imgSizes: (CGFloat, CGFloat)! {
        didSet {
            logoHeightConstr.constant = imgSizes.1
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        passwordField.delegate = self
        userField.delegate = self
        
        userField.checkRegex = "^[a-z0-9_-]{6,16}$"
        
        passwordField.checkRegex = "^[a-z0-9_-]{6,18}$"

        setConstraints()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.keyboardNotification(notification:)),
                                               name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                               object: nil)
        
        if let uname = user.userName, let token = user.token {
            userField.text = uname
            api.ifActive(token: token, onComplete: { response in
                if response.code == .Successful {
                    self.performSegue(withIdentifier: "LoginSegue", sender: nil)
                }
            })
        }
        
    }
    
    func setConstraints() {
        let screenSize = self.view.frame.size
        stackViewWidthConstr.constant = screenSize.width * 0.8
        stackViewHeightConstr.constant = screenSize.height * 0.5
        defaultVerticalConstr = (screenSize.height - stackViewHeightConstr.constant) / 2
        
        let imgNormalSize = stackViewHeightConstr.constant / 2.5
        imgSizes = (imgNormalSize * 0.75, imgNormalSize)
    }
    
    func sendAuthRequest(uname: String, pwd: String) {
        guard let hash = pwd.neverlandDefaultHash else {
            fatalError("Could not create pwd hash")
        }
        
        api.attemptLogin(withLogin: uname, passwordHash: hash) { response in
            switch response.code {
            case .Successful:
                self.user.userName = uname
                self.user.token = response.message
                self.performSegue(withIdentifier: "LoginSegue", sender: nil)
            case .Error:
                SCLAlertView().showError("Authorization error", subTitle: "Username or password is incorrect.")
            }
        }
    }
    
    @objc func keyboardNotification(notification: NSNotification) {
        if let userInfo = notification.userInfo {
            let endFrame = (userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue
            let endFrameY = endFrame?.origin.y ?? 0
            let duration:TimeInterval = (userInfo[UIKeyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue ?? 0
            
            if endFrameY >= UIScreen.main.bounds.size.height {
                self.stackViewBotomOffsetConstr.constant = defaultVerticalConstr
                logoHeightConstr.constant = imgSizes.1
                logoTopConstr.constant = 0
            } else {
                self.stackViewBotomOffsetConstr.constant = (endFrame?.size.height ?? 0) + 16
                logoHeightConstr.constant = imgSizes.0
                logoTopConstr.constant = 16
            }
            UIView.animate(withDuration: duration,
                           delay: TimeInterval(0),
                           options: .curveEaseIn,
                           animations: { self.view.layoutIfNeeded() },
                           completion: nil)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        NotificationCenter.default.removeObserver(self)
    }


}


extension AuthViewController: UITextFieldDelegate {
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        guard let uname = userField.text, let pwd = passwordField.text, passwordField.isValid, userField.isValid else {
            return false
        }
        
        textField.resignFirstResponder()
        sendAuthRequest(uname: uname, pwd: pwd)
        return true
    }
}
