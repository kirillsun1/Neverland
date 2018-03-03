//
//  UserData.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 22/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

// used only for mocked api (as a served-side structure)

struct UserData {
    let login: String
    let name: String
    let surname: String
    let email: String
    let password: String
    
    let activeKeys: [String] = [String]()
}
