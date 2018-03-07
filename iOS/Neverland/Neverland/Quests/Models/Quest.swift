//
//  Quest.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

struct Quest {
    
    var id: Int
    var title: String
    var groupId: Int
    var description: String
    var datePicked: Time?
    var creator: Person
    //var solution: [Solution]

}

struct Time {
    var day: Int?
    var month: Int?
    var year: Int?
    
    init(from time: NSDictionary?) {
        day = time?.value(forKey: "day") as? Int
        month = time?.value(forKey: "month") as? Int
        year = time?.value(forKey: "year") as? Int
    }
}

extension Quest: Equatable {
    static func ==(lhs: Quest, rhs: Quest) -> Bool {
        return lhs.id == rhs.id
    }
    
    
}


