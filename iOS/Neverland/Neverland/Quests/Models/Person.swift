//
//  Person.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation


class Person {
    var id: Int
    var nickname: String
    var firstName: String
    var secondName: String
    var photoURLString: String?
    var followers: Int
    var followings: Int
    var rating: Int
    var isFollowedByMe: Bool
    
    init?(creatorData: NSDictionary?) {
        
        guard let creatorData = creatorData else {
            return nil
        }
        //print(creatorData)
        id = creatorData.value(forKey: "u_id") as! Int
        nickname = creatorData.value(forKey: "user_name") as! String
        firstName = creatorData.value(forKey: "first_name") as! String
        secondName = creatorData.value(forKey: "second_name") as! String
        photoURLString = creatorData.value(forKey: "avatar") as? String
        followers = creatorData.value(forKey: "followers") as! Int
        followings = creatorData.value(forKey: "followings") as! Int
        isFollowedByMe = creatorData.value(forKey: "do_i_follow") as! Bool
        rating = Int((creatorData.value(forKey: "rating") as! Double) * 100)
    }
}
