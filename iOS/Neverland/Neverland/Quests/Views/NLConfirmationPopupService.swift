//
//  QuestAddPopup.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 01/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import PopupDialog
import UIKit

class NLConfirmationPopupService {
    
    init() {
        //configureAppearance()
    }
    
    func presentPopup(forQuest quest: Quest, into viewController: UIViewController, onComplete: @escaping ()->()) {
        let popup = PopupDialog(title: "TAKE QUEST", message: "Do you want to take \(quest.title)", image: nil)
        let cancel = CancelButton(title: "CANCEL") { }
        let confirm = DefaultButton(title: "ADD") {
            guard let vc = viewController as? NewQuestViewController else {
                fatalError("Class is now only supposed to be called from NewQuestVC")
            }
            
            vc.removeQuest(quest: quest)
            onComplete()
        }
        
        popup.addButtons([cancel, confirm])
        viewController.present(popup, animated: true, completion: nil)
    }
    
    
//    func configureAppearance() {
//        // Customize dialog appearance
//        let pv = PopupDialogDefaultView.appearance()
//        pv.titleFont    = UIFont(name: "HelveticaNeue-Light", size: 16)!
//        pv.titleColor   = UIColor.white
//        pv.messageFont  = UIFont(name: "HelveticaNeue", size: 14)!
//        pv.messageColor = UIColor(white: 0.8, alpha: 1)
//
//        // Customize the container view appearance
//        let pcv = PopupDialogContainerView.appearance()
//        pcv.backgroundColor = UIColor(red:0.23, green:0.23, blue:0.27, alpha:1.00)
//        pcv.cornerRadius    = 2
//        pcv.shadowEnabled   = true
//        pcv.shadowColor     = UIColor.black
//
//        // Customize overlay appearance
//        let ov = PopupDialogOverlayView.appearance()
//        ov.blurEnabled = true
//        ov.blurRadius  = 30
//        ov.liveBlur    = true
//        ov.opacity     = 0.7
//        ov.color       = UIColor.black
//
//        // Customize default button appearance
//        let db = DefaultButton.appearance()
//        db.titleFont      = UIFont(name: "HelveticaNeue-Medium", size: 14)!
//        db.titleColor     = UIColor.white
//        db.buttonColor    = UIColor(red:0.25, green:0.25, blue:0.29, alpha:1.00)
//        db.separatorColor = UIColor(red:0.20, green:0.20, blue:0.25, alpha:1.00)
//
//        // Customize cancel button appearance
//        let cb = CancelButton.appearance()
//        cb.titleFont      = UIFont(name: "HelveticaNeue-Medium", size: 14)!
//        cb.titleColor     = UIColor(white: 0.6, alpha: 1)
//        cb.buttonColor    = UIColor(red:0.25, green:0.25, blue:0.29, alpha:1.00)
//        cb.separatorColor = UIColor(red:0.20, green:0.20, blue:0.25, alpha:1.00)
//    }
}
