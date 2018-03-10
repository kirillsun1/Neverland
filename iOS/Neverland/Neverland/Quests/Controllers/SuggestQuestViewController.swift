//
//  SuggestQuestViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import SCLAlertView

class SuggestQuestViewController: UIViewController {

    @IBOutlet weak var saveBtn: UIButton!
    @IBOutlet weak var questTitleLbl: UITextField!
    @IBOutlet weak var detailedTextView: UITextView!
    
    let MAX_QUEST_DESCRIPTION_COUNT = 480
    let MAX_QUEST_TITLE_COUNT = 30
    let MIN_QUEST_DESCRIPTION_COUNT = 10
    let MIN_QUEST_TITLE_COUNT = 4

    let questApi = FakeQuestApi() // change when ready
    let groupId = 0 // get through segue.
    
    var questTitle: String?
    var questDescription: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        detailedTextView.delegate = self
    }

    @IBAction func inputInfoChanged() {
        questTitle = questTitleLbl.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        questDescription = detailedTextView.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        
        saveBtn.isEnabled = questTitle != nil && (questTitle!.matches(pattern: "^.{\(MIN_QUEST_TITLE_COUNT),\(MAX_QUEST_TITLE_COUNT)}$"))
            && questDescription != nil && questDescription!.matches(pattern: "^.{\(MIN_QUEST_DESCRIPTION_COUNT),\(MAX_QUEST_DESCRIPTION_COUNT)}$")
    }

    
    @IBAction func savePressed() {
        questApi.registerQuest(title: questTitleLbl.text!, description: detailedTextView.text!, groupId: groupId) { response in
            if response.code == .Successful {
                self.navigationController?.popViewController(animated: true)
            } else {
                SCLAlertView().showError("Creation error", subTitle: "Quest was not created.")
            }
        }
    }

}


extension SuggestQuestViewController: UITextViewDelegate {
    
    func textViewDidChange(_ textView: UITextView) {
        inputInfoChanged()
    }
}
