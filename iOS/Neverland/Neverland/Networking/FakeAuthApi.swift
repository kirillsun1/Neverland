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
    
    func attemptLogin(withLogin login: String, passwordHash: String) -> AuthApiResponse {
        if FakeAuthApi.registeredUsers.contains(where: {$0.login == login && $0.password == passwordHash}) {
            let key = generateKey()
            FakeAuthApi.activeKeys.append(key)
            print(FakeAuthApi.activeKeys)
            return AuthApiResponse(code: .Successful, message: key)
        }
        
        return AuthApiResponse(code: .Error, message: "Invalid Data")
    }
    
    func registerAccount(withData data: RegistrationData) -> AuthApiResponse {
        if FakeAuthApi.registeredUsers.contains(where: {$0.login == data.login}) {
            return AuthApiResponse(code: .Error, message: nil)
        }
        
        FakeAuthApi.registeredUsers.append(UserData(login: data.login, name: data.firstName, surname: data.secondName, email: data.email, password: data.password))
        return attemptLogin(withLogin: data.login, passwordHash: data.password)
    }
    
    func isActive(token: String) -> Bool {
        return FakeAuthApi.activeKeys.contains(token)
    }
    
    func generateKey() -> String {
        return "BCD"
    }
    
}
