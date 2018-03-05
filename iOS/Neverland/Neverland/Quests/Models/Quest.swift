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
    var creator: Person
    var solution: [Solution]

}

extension Quest: Equatable {
    static func ==(lhs: Quest, rhs: Quest) -> Bool {
        return lhs.id == rhs.id
    }
    
    
}


