//
//  RegistrationField.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 23/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class RegistrationField: UITextField {

    var checkRegex = ""
    
    var isValid: Bool {
        
        let result = self.text != nil &&
            !self.text!.isEmpty &&
            self.text!.matches(pattern: checkRegex)
        
        if !result {
            self.layer.borderWidth = 3.5
            self.layer.borderColor = UIColor.red.cgColor
        } else {
            self.layer.borderWidth = 0.0
            self.layer.borderColor = UIColor.clear.cgColor
        }
        
        return result
    }

}
