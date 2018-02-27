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
    @IBOutlet weak var questTitle: UITextField!
    @IBOutlet weak var detailedText: UITextView!
    
    let MAX_QUEST_DESCRIPTION_COUNT = 250
    let MAX_QUEST_TITLE_COUNT = 15
    let questApi = FakeQuestApi() // change when ready
    let groupId = 0 // get through segue.
    
    override func viewDidLoad() {
        super.viewDidLoad()
        detailedText.delegate = self
    }

    @IBAction func inputInfoChanged() {
        saveBtn.isEnabled = questTitle.text != nil && questTitle.text!.count > 0 && questTitle.text!.count <= MAX_QUEST_TITLE_COUNT &&
                            detailedText.text != nil && detailedText.text!.count > 0 && detailedText.text!.count <= MAX_QUEST_DESCRIPTION_COUNT
    }

    
    @IBAction func savePressed() {
        questApi.registerQuest(title: questTitle.text!, description: detailedText.text!, groupId: groupId) { response in
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
