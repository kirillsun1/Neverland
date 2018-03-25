//
//  Solution.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

struct Proof {
    var id: Int
    
    var proofer: Person
    var picPath: String
    var comment: String
    var addTime: Time
    var quest: Quest?
    
    static func createProofsArray(from jsonArr: [NSDictionary]) -> [Proof] {
        var proofs = [Proof]()
        jsonArr.forEach {
            if let picPath = $0.value(forKey: "pic_path") as? String,
                let comment = $0.value(forKey: "comment") as? String,
                let id = $0.value(forKey: "id") as? Int,
                let questJSON = $0.value(forKey: "quest") as? NSDictionary,
                let proofer = Person(creatorData: ($0.value(forKey: "proofer") as? NSDictionary)) {
                    let time = Time(from: (($0.value(forKey: "add_time") as? NSDictionary)?.value(forKey: "date") as? NSDictionary))
                    proofs.append(Proof(id: id, proofer: proofer, picPath: picPath, comment: comment, addTime: time, quest: Quest(fromJSON: questJSON)))
            }
        }
        return proofs
    }
    
}
