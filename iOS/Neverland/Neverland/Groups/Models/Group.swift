//
//  Group.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 10/04/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

struct Group {
    let title: String
    let creator: Person
    
    init?(json: NSDictionary) {
        guard let title = json.value(forKey: "name") as? String,
              let person = Person.init(creatorData: json.value(forKey: "admin") as? NSDictionary)
            else {
                return nil
        }
        
        self.title = title
        self.creator = person
    }
}
