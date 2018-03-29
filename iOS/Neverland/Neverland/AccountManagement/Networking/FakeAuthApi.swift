//
//  FakeAuthApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 22/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

class FakeAuthApi: AuthApi {

    static var registeredUsers = [UserData]()
    static var activeKeys = ["BCD"]
    
    func attemptLogin(withLogin login: String, passwordHash: String, onComplete: @escaping (AuthApiResponse) -> ()) {
        if FakeAuthApi.registeredUsers.contains(where: {$0.login == login && $0.password == passwordHash}) {
            let key = generateKey()
            FakeAuthApi.activeKeys.append(key)
            print(FakeAuthApi.activeKeys)
            onComplete(AuthApiResponse(code: .Successful, message: key))
            return
        }
        
        onComplete(AuthApiResponse(code: .Error, message: "Invalid Data"))
    }
    
    func registerAccount(withData data: RegistrationData, onComplete: @escaping (AuthApiResponse) -> ()) {
        if FakeAuthApi.registeredUsers.contains(where: {$0.login == data.login}) {
            onComplete(AuthApiResponse(code: .Error, message: nil))
            return
        }
        
        FakeAuthApi.registeredUsers.append(UserData(login: data.login, name: data.firstName, surname: data.secondName, email: data.email, password: data.password))
        attemptLogin(withLogin: data.login, passwordHash: data.password, onComplete: onComplete)
    }
    
    func ifActive(token: String, onComplete: @escaping (AuthApiResponse)->()) {
        //return FakeAuthApi.activeKeys.contains(token)
    }
    
    func generateKey() -> String {
        return "BCD"
    }
    
}
