//
//  AuthViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class AuthViewController: UIViewController {

    let user = User.sharedInstance
    let apiAdapter = ApiAdapter(forApi: MockedApi())
    var hasher: Hasher!
    
    @IBOutlet weak var stackViewWidthConstr: NSLayoutConstraint!
    @IBOutlet weak var stackViewHeightConstr: NSLayoutConstraint!
    @IBOutlet weak var stackViewBotomOffsetConstr: NSLayoutConstraint!
    @IBOutlet weak var logoTopConstr: NSLayoutConstraint!
    @IBOutlet weak var logoHeightConstr: NSLayoutConstraint!

    @IBOutlet weak var passwordField: UITextField!
    @IBOutlet weak var userField: UITextField!
    
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
        
        setConstraints()
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.keyboardNotification(notification:)),
                                               name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                               object: nil)
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if let uname = user.userName, let pwd = user.password {
            sendAuthRequest(uname: uname, pwd: pwd)
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
        // pwd to be hashed here !!!!
        // let passwordHash = hasher.hash(pwd)
        
        // cases:
        // username does not exist -> alert
        // ....
        // password is incorrect -> alert
        // ....
        // ok -> update User class instance. Proceed further ...
        user.userName = uname
        user.password = pwd
        
        performSegue(withIdentifier: "LoginSegue", sender: nil)
        
    }
    
    
    // TODO: Make this only happen by login screen text fields. Not NSNot. observer !!!!!
    // taken from stackoverflow + updated
    @objc func keyboardNotification(notification: NSNotification) {
        if let userInfo = notification.userInfo {
            let endFrame = (userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue
            let endFrameY = endFrame?.origin.y ?? 0
            let duration:TimeInterval = (userInfo[UIKeyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue ?? 0
            //
            let animationCurveRawNSN = userInfo[UIKeyboardAnimationCurveUserInfoKey] as? NSNumber
            let animationCurveRaw = animationCurveRawNSN?.uintValue ?? UIViewAnimationOptions.curveEaseInOut.rawValue
            let animationCurve:UIViewAnimationOptions = UIViewAnimationOptions(rawValue: animationCurveRaw)
            //
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
                           options: animationCurve,
                           animations: { self.view.layoutIfNeeded() },
                           completion: nil)
        }
    }


}


extension AuthViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        guard let uname = userField.text, let pwd = textField.text else {
            return false
        }
        
        sendAuthRequest(uname: uname, pwd: pwd)
        
        return true
    }
}
