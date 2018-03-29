//
//  BorderedTextView.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 27/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

@IBDesignable
class BorderedTextView: UITextView {

    @IBInspectable var bWidth: CGFloat = 1.0 {
        didSet {
            self.layer.borderWidth = bWidth
        }
    }
    
    @IBInspectable var bColor: UIColor = UIColor.black {
        didSet {
            self.layer.borderColor = bColor.cgColor
        }
    }

}
