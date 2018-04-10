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
            if let vc = viewController as? NewQuestViewController {
                vc.removeQuest(quest: quest)
            }
            onComplete()
        }
        
        popup.addButtons([cancel, confirm])
        viewController.present(popup, animated: true, completion: nil)
    }

}
