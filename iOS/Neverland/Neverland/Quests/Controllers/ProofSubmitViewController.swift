//
//  ProofSubmitViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 14/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import ImagePicker
import SCLAlertView
import CropViewController

class ProofSubmitViewController: UIViewController {

    private let imagePickerController = ImagePickerController()
    private let api = NLQuestApi()
    var quest: Quest!
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var commentField: UITextField!
    @IBOutlet weak var saveBtn: UIButton!
    @IBOutlet weak var bottomConstr: NSLayoutConstraint!
    @IBOutlet weak var topConstr: NSLayoutConstraint!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        imagePickerController.delegate = self
        imagePickerController.imageLimit = 1
        commentField.delegate = self
        
        present(imagePickerController, animated: true, completion: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ProofSubmitViewController.keyboardWillShow(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ProofSubmitViewController.keyboardWillHide(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)

    }
    
    func setSaveBtnState() {
        saveBtn.isEnabled = imageView.image?.description != "placeholder-02"
    }
    
    @IBAction func retakePressed() {
        present(imagePickerController, animated: true, completion: nil)
    }
    
    @IBAction func savePressed() {
        print("img: \(imageView.description), comment: \(commentField.text!)")
        api.submitProof(qid: quest.id, img: imageView.image!, comment: commentField.text) { response in
            if response.code == .Successful {
                guard let detVC = self.navigationController?.viewControllers[(self.navigationController?.viewControllers.count)!-2] as? QuestDetailedViewController else {
                    fatalError()
                }
                detVC.finished = true
                self.navigationController?.popViewController(animated: true)
            } else {
                SCLAlertView().showError("Uploading error", subTitle: (response.message as? String) ?? "Proof was not uploaded")
            }
        }
    }
}

extension ProofSubmitViewController: ImagePickerDelegate {
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

extension ProofSubmitViewController: CropViewControllerDelegate {
    func presentCropper(forImg image: UIImage) {
        let cropViewController = CropViewController(image: image)
        cropViewController.delegate = self
        present(cropViewController, animated: true, completion: nil)
    }
    
    func cropViewController(_ cropViewController: CropViewController, didCropToImage image: UIImage, withRect cropRect: CGRect, angle: Int) {
        self.imageView.image = image
        setSaveBtnState()
        cropViewController.dismiss(animated: true, completion: nil)
    }
}

extension ProofSubmitViewController: UITextFieldDelegate {
    @objc func keyboardWillShow(_ notification: Notification) {
        if let keyboardFrame: NSValue = notification.userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue {
            let keyboardRectangle = keyboardFrame.cgRectValue
            let keyboardHeight = keyboardRectangle.height
            self.bottomConstr.constant = keyboardHeight + 8
        }
    }
    
    @objc func keyboardWillHide(_ notification: Notification) {
        self.bottomConstr.constant = 8
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}

