//
//  QuestApiResponse.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

struct QuestApiResponse: ApiResponse {
    let code: ResponseCode
    let message: String?
}
