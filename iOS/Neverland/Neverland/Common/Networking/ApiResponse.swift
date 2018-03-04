//
//  AuthApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

protocol ApiResponse {
    //var code {get set}
}

enum ResponseCode: Int {
    case Successful = 1
    case Error = -1
}
