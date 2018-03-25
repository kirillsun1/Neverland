//
//  ProofCell.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 25/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import Alamofire

class ProofPhotoCell: UICollectionViewCell {
    @IBOutlet weak var picture: UIImageView!
    
    func setPic(urlString: String) {
        self.picture.uploadImageFrom(url: urlString)
    }
}
