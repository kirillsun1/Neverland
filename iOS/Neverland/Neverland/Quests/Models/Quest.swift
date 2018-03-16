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
    
    init?(fromJSON json: NSDictionary) {
        groupId = 0
        
        guard let title = json.value(forKey: "title") as? String,
            let desc = json.value(forKey: "desc") as? String,
            let id = json.value(forKey: "id") as? Int,
            let creator = Person(creatorData: (json.value(forKey: "author") as? NSDictionary)) else {
                return nil
        }
        
        self.title = title
        self.description = desc
        self.id = id
        self.creator = creator
        self.datePicked = Time(from: ((json.value(forKey: "time_taken") as? NSDictionary)?.value(forKey: "date") as? NSDictionary))
    }

}

struct Time {
    var day: Int
    var month: Int
    var year: Int
    
    init(from json: NSDictionary?) {
        day = (json?.value(forKey: "day") as? Int) ?? 1
        month = (json?.value(forKey: "month") as? Int) ?? 1
        year = (json?.value(forKey: "year") as? Int) ?? 1970
    }
}

extension Quest: Equatable {
    static func ==(lhs: Quest, rhs: Quest) -> Bool {
        return lhs.id == rhs.id
    }
    
    
}


