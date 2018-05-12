//
//  Rating.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 12/05/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

enum VoteType:Int {
    case `for` = 1
    case against = -1
    case none = 0
}

struct Rating {
    let likes: Int
    let dislikes: Int
    let percentage: Float
    let myVote: VoteType
    
    init?(json: NSDictionary?) {
        guard let json = json , let likes = json.value(forKey: "for") as? Int,
        let dislikes = json.value(forKey: "against") as? Int, let myVoteInt = json.value(forKey: "my_vote") as? Int else {
            return nil
        }
        
        self.likes = likes
        self.dislikes = dislikes
        self.myVote = VoteType.init(rawValue: myVoteInt)!
        self.percentage = likes != 0  ? Float(likes) / (Float(dislikes) + Float(likes)) : 0
    }
}
