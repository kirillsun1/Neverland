//
//  RoundedImage.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 17/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

@IBDesignable
class RoundedImage: UIImageView {

    @IBInspectable var borderWidth: CGFloat = 1 {
        didSet {
            layer.borderWidth = borderWidth
        }
    }
    
    @IBInspectable var maskToBound = false {
        didSet {
            layer.masksToBounds = false 
        }
    }
    
    @IBInspectable var borderColor: UIColor? = .clear {
        didSet {
            layer.borderColor = borderColor?.cgColor
        }
    }
    
    // negative value - default value is used (round image) otherwise - provided value
    @IBInspectable var customRadius: CGFloat = -1
    
    override func layoutSubviews() {
        layer.cornerRadius = customRadius < 0 ? frame.size.height / 2 : customRadius
    }
    

}
