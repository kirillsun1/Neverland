//
//  AuthApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 22/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation


protocol AuthApi {
    
    func attemptLogin(withLogin login: String, passwordHash: String) -> AuthApiResponse
    func registerAccount(withData data: RegistrationData) -> AuthApiResponse
    func isActive(token: String) -> Bool
}
