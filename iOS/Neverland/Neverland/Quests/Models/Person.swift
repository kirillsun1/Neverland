//
//  Person.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation


struct Person {
    //var id: Int
    var nickname: String
    var firstName: String
    var secondName: String
    //var photoURLString: String
    
    init?(creatorData: NSDictionary?) {
        
        guard let creatorData = creatorData else {
            return nil
        }
        
        nickname = creatorData.value(forKey: "user_name") as! String
        firstName = creatorData.value(forKey: "first_name") as! String
        secondName = creatorData.value(forKey: "second_name") as! String
    }
}
