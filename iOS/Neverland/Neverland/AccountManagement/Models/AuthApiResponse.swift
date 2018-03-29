//
//  AuthApiResponse.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 22/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation


struct AuthApiResponse: ApiResponse {
    let code: ResponseCode
    let message: String?
}
