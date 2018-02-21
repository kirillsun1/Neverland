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
    let apiAdapter: ApiAdapter!
    
    private init() {
        userDefaults = UserDefaults()
        apiAdapter = ApiAdapter(forApi: MockedApi())
    }
    
    var userName: String? {
        get { return userDefaults.string(forKey: "username") }
        set { userDefaults.set(newValue, forKey: "username") }
    }
    
    var password: String? {
        get { return userDefaults.string(forKey: "password") }
        set { userDefaults.set(newValue, forKey: "password") }
    }
    
    func logout() {
        userName = nil
        password = nil
    }
}
