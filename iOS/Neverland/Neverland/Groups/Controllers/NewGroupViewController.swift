//
//  NewGroupViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 10/04/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import ImagePicker
import CropViewController
import SCLAlertView

class NewGroupViewController: UIViewController {

    @IBOutlet weak var saveBtn: UIButton!
    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var avatarView: UIImageView!
    
    private let imagePickerController = ImagePickerController()

    override func viewDidLoad() {
        super.viewDidLoad()

        
        imagePickerController.delegate = self
        imagePickerController.imageLimit = 1
    }
    
    
    @IBAction func editingChanged(_ sender: Any) {
        saveBtn.isEnabled = nameField.text != nil && !nameField.text!.isEmpty
    }
    
    @IBAction func saveBtnPressed(_ sender: Any) {
        print("pressed")
    }
    

    @IBAction func setImagePressed(_ sender: Any) {
        present(imagePickerController, animated: true, completion: nil)
    }
}

extension NewGroupViewController: ImagePickerDelegate {
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

extension NewGroupViewController: CropViewControllerDelegate {
    func presentCropper(forImg image: UIImage) {
        let cropViewController = CropViewController(image: image)
        cropViewController.aspectRatioPreset = .presetSquare
        cropViewController.aspectRatioPickerButtonHidden = true
        cropViewController.delegate = self
        present(cropViewController, animated: true, completion: nil)
    }
    
    func cropViewController(_ cropViewController: CropViewController, didCropToImage image: UIImage, withRect cropRect: CGRect, angle: Int) {
        cropViewController.dismiss(animated: true, completion: nil)
        avatarView.image = image
    }
}

