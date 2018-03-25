//
//  Time.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 25/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

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
