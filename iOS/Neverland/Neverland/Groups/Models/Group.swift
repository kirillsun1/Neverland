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
    let id: Int
    let avatarURL: String?
    let quantity: Int
    
    init?(json: NSDictionary) {
        guard let title = json.value(forKey: "name") as? String,
              let id = json.value(forKey: "id") as? Int,
              let person = Person.init(creatorData: json.value(forKey: "admin") as? NSDictionary),
              let quantity = json.value(forKey: "quantity") as? Int
            else {
                return nil
        }
        
        if let avatar = json.value(forKey: "avatar") as? String {
            avatarURL = avatar
        } else {
            avatarURL = nil
        }
        
        self.title = title
        self.creator = person
        self.id = id
        self.quantity = quantity
    }
}
