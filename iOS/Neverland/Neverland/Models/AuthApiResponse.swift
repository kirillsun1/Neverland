//
//  AuthApiResponse.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 22/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

// todo: think about it !!

enum ResponseCode: Int {
    case Successful = 200
    case Error = 400
}

struct AuthApiResponse {
    
    let code: ResponseCode
    let message: String?
}
