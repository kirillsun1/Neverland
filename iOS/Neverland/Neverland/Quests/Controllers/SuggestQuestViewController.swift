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
    
    private let MAX_QUEST_DESCRIPTION_COUNT = 480
    private let MAX_QUEST_TITLE_COUNT = 30
    private let MIN_QUEST_DESCRIPTION_COUNT = 10
    private let MIN_QUEST_TITLE_COUNT = 4

    private let questApi = NLQuestApi()
    var groupId = 0 // get through segue.
    
    private var questTitle: String?
    private var questDescription: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        detailedTextView.delegate = self
        
        self.hideKeyboardWhenTappedAround()
    }

    @IBAction func inputInfoChanged() {
        questTitle = questTitleLbl.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        questDescription = detailedTextView.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        
        saveBtn.isEnabled = questTitle != nil && (questTitle!.matches(pattern: "^.{\(MIN_QUEST_TITLE_COUNT),\(MAX_QUEST_TITLE_COUNT)}$"))
            && questDescription != nil && questDescription!.matches(pattern: "^.{\(MIN_QUEST_DESCRIPTION_COUNT),\(MAX_QUEST_DESCRIPTION_COUNT)}$")
    }

    
    @IBAction func savePressed() {
        view.endEditing(true)
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
