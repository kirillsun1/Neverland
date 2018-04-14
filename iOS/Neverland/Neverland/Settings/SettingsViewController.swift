//
//  SettingsViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 30/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import ImagePicker
import CropViewController
import SCLAlertView

class SettingsViewController: UITableViewController {
    
    private let imagePickerController = ImagePickerController()
    private let profileApi = NLProfileApi()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        imagePickerController.delegate = self
        imagePickerController.imageLimit = 1
        
    }
    
    @IBAction func logoutPressed() {
        User.sharedInstance.logout()
        let storyboard = UIStoryboard.init(name: "Auth", bundle: nil)
        let vc = storyboard.instantiateInitialViewController()
        self.present(vc!, animated: true, completion: nil)
    }
    
    @IBAction func changePhotoPressed() {
        present(imagePickerController, animated: true, completion: nil)
    }

}

extension SettingsViewController: ImagePickerDelegate {
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

extension SettingsViewController: CropViewControllerDelegate {
    func presentCropper(forImg image: UIImage) {
        let cropViewController = CropViewController(image: image)
        cropViewController.aspectRatioPreset = .presetSquare
        cropViewController.aspectRatioPickerButtonHidden = true
        cropViewController.delegate = self
        present(cropViewController, animated: true, completion: nil)
    }
    
    func cropViewController(_ cropViewController: CropViewController, didCropToImage image: UIImage, withRect cropRect: CGRect, angle: Int) {
        cropViewController.dismiss(animated: true, completion: nil)
        profileApi.uploadAvatar(image) { response in
            if response.code == .Successful {
                self.navigationController?.popViewController(animated: true)
            } else {
                SCLAlertView().showError("Uploading error", subTitle: (response.message as? String) ?? "Avatar was not changed!")
            }
        }
    }
}
