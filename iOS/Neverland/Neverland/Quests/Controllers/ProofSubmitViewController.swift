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
    
    override func viewDidLoad() {
        super.viewDidLoad()

        imagePickerController.delegate = self
        imagePickerController.imageLimit = 1
        
        present(imagePickerController, animated: true, completion: nil)
    }
    
    func setSaveBtnState() {
        saveBtn.isEnabled = imageView.image?.description != "placeholder-02"
    }
    
    @IBAction func retakePressed() {
        present(imagePickerController, animated: true, completion: nil)
    }
    
    @IBAction func savePressed() {
        print("img: \(imageView.description), comment: \(commentField.text!)")
        api.submitSolution(qid: quest.id, img: imageView.image!, comment: commentField.text) { response in
            if response.code == .Successful {
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
