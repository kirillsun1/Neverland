//
//  NewQuestViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 17/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import PopupDialog
import CropViewController
import ImagePicker
import SCLAlertView

class NewQuestViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var groupView: UIView!
    @IBOutlet weak var groupAv: UIImageView!
    @IBOutlet weak var adminAv: UIImageView!
    @IBOutlet weak var groupName: UILabel!
    @IBOutlet weak var adminName: UILabel!
    @IBOutlet weak var subscribersLabel: UIButton!
    @IBOutlet weak var changeAvButton: UIButton!
    @IBOutlet weak var leaveButton: UIButton!
    
    let groupApi = NLGroupApi()
    private let imagePickerController = ImagePickerController()
    
    @IBAction func showSubs() {
        performSegue(withIdentifier: "ShowSubsSegue", sender: nil)
    }
    
    @IBAction func unsubscripe() {
        groupApi.dropGroup(gid: group!.id) { res in
            print(res)
            if res.code == .Successful {
                self.navigationController?.popViewController(animated: true)
            }
        }
    }
    
    @IBAction func changeAvatar() {
        present(imagePickerController, animated: true, completion: nil)
    }
    
    private var quests = [Quest]() {
        didSet {
            tableView.reloadData()
        }
    }
    private let spinner = UIActivityIndicatorView.init(activityIndicatorStyle: .gray)
    var group: Group? = nil

    
    override func viewDidLoad() {
        super.viewDidLoad()

        spinner.stopAnimating()
        spinner.hidesWhenStopped = true
        spinner.frame = CGRect(x: 0, y: 0, width: 320, height: 44)
        
        imagePickerController.delegate = self
        imagePickerController.imageLimit = 1
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = spinner
        
        groupView.isHidden = group == nil
        
        changeAvButton.isHidden = group?.creator.nickname != User.sharedInstance.userName
        leaveButton.isHidden = group?.creator.nickname == User.sharedInstance.userName
        
        fetchAllQuests()
        
        self.tableView.es.addPullToRefresh {
            [unowned self] in
            
            self.quests = []
            self.fetchAllQuests()
            
            self.tableView.reloadData()
            self.tableView.es.stopPullToRefresh(ignoreDate: true)
            self.tableView.es.stopPullToRefresh(ignoreDate: true, ignoreFooter: false)
        }
        
        if let group = group {
            if let avlink = group.avatarURL {
                groupAv.uploadImageFrom(url: avlink)
            }
            
            if let adAvLink = group.creator.photoURLString {
                adminAv.uploadImageFrom(url: adAvLink)
            }
            
            groupName.text = group.title
            adminName.text = "\(group.creator.nickname)"
            subscribersLabel.setTitle("\(group.quantity) subscribers", for: .normal)
        }
    }
    
    func confirmationPopup(for quest: Quest) {
        NLConfirmationPopupService().presentPopup(type: "QUEST", into: self) {
            NLQuestApi().takeQuest(qid: quest.id) { res in
                if res.code == .Successful && self.group == nil {
                    self.removeQuest(quest: quest)
                }
            }
        }
    }
    
    func removeQuest(quest: Quest) {
        tableView.beginUpdates()
        if let index = quests.index(of: quest) {
            tableView.deleteRows(at: [IndexPath.init(row: index, section: 0)], with: .automatic)
            quests.remove(at: index)
        }
        tableView.endUpdates()
    }
    
    func fetchAllQuests() {
        if group == nil {
            NLQuestApi().fetchQuests { questsDictionary in
                for questJson in questsDictionary {
                    if let quest = Quest(fromJSON: questJson) {
                        self.quests.append(quest)
                    }
                }
            }
        } else {
            NLQuestApi().fetchQuests(inGroup: group!.id) { questsDictionary in
                for questJson in questsDictionary {
                    if let quest = Quest(fromJSON: questJson) {
                        self.quests.append(quest)
                    }
                }
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "NewQuestFromGroupSegue", let dest = segue.destination as? SuggestQuestViewController {
            dest.groupId = group?.id ?? 0
        } else if segue.identifier == "ShowSubsSegue", let vc = segue.destination as? PeopleListController {
            vc.thisType = .subscriptions
            vc.uid = group!.id
        }
    }
}

extension NewQuestViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return quests.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "QuestInfoCell") as? QuestInfoCell else {
            fatalError()
        }
        
        cell.fill(quest: quests[indexPath.row])
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        confirmationPopup(for: quests[indexPath.row])
    }

}

extension NewQuestViewController: ImagePickerDelegate {
    func wrapperDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
        
    }
    
    func doneButtonDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
        imagePicker.dismiss(animated: true, completion: nil)
        presentCropper(forImg: images[0])
    }
    
    func cancelButtonDidPress(_ imagePicker: ImagePickerController) {
        imagePicker.dismiss(animated: true, completion: nil)
    }
    
    
}

extension NewQuestViewController: CropViewControllerDelegate {
    func presentCropper(forImg image: UIImage) {
        let cropViewController = CropViewController(image: image)
        cropViewController.aspectRatioPreset = .presetSquare
        cropViewController.aspectRatioPickerButtonHidden = true
        cropViewController.delegate = self
        present(cropViewController, animated: true, completion: nil)
    }
    
    func cropViewController(_ cropViewController: CropViewController, didCropToImage image: UIImage, withRect cropRect: CGRect, angle: Int) {
        cropViewController.dismiss(animated: true, completion: nil)
        groupApi.uploadAvatar(image, gid: group!.id) { response in
            if response.code == .Successful && response.message != nil {
                self.groupAv.uploadImageFrom(url: response.message as! String)
            } else {
                SCLAlertView().showError("Uploading error", subTitle: (response.message as? String) ?? "Avatar was not changed!")
            }
        }
    }
}

