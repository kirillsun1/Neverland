//
//  Hasher.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 21/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

protocol Hasher {
    
    func hash(_ input: String) -> String
}
