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
    var q = [Quest]() // delete later
    var quests = [Quest]()
    
    init(code: ResponseCode, message: String?) {
        self.code = code
        self.message = message
        q = FakeQuestApi().generateQuests() // delete later.
    }
    
    mutating func fillQuestArray(from startIndex:Int, to endIndex: Int) {
        self.quests = Array(q[startIndex ... endIndex])
    }
    
}
