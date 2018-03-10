//
//  User.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 21/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

class User {
    static let sharedInstance = User()
    let userDefaults: UserDefaults!
    
    private init() {
        userDefaults = UserDefaults()
    }
    
    var userName: String? {
        get { return userDefaults.string(forKey: "username") }
        set { userDefaults.set(newValue, forKey: "username") }
    }

    var token: String? {
        get { return userDefaults.string(forKey: "token") }
        set { userDefaults.set(newValue, forKey: "token") }
    }
    
    func logout() {
        userName = nil
        token = nil
    }
}
