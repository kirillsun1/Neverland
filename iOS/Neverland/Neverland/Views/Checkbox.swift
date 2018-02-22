//
//  Checkbox.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 18/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

@IBDesignable
class Checkbox: UIButton {

    static let checkedImage = UIImage(named: "checkicon")
    
    var isActive = false {
        didSet {
            setBackgroundImage(isActive ? Checkbox.checkedImage : nil, for: .normal)
        }
    }
    
    @IBInspectable var borderWidth: CGFloat = 1 {
        didSet {
            layer.borderWidth = borderWidth
        }
    }
    
    @IBInspectable var borderColor: UIColor? = .black {
        didSet {
            layer.borderColor = borderColor?.cgColor
        }
    }
    
    

}
