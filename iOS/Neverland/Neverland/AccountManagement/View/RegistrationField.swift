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
        
        self.layer.borderWidth = !result ? 3.5 : 0.0
        self.layer.borderColor = !result ? UIColor.red.cgColor : UIColor.clear.cgColor
        
        return result
    }

}
