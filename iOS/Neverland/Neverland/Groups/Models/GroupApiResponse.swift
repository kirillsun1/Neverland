//
//  GroupApiResponse.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 10/04/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

struct GroupApiResponse: ApiResponse {
    let code: ResponseCode
    let message: Any?
    
    init(code: ResponseCode, message: Any?) {
        self.code = code
        self.message = message
    }
}

