//
//  AuthApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 22/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

// MAKE REAL API WORK IN DIFFERENT THREAD + SPINNER !!!

protocol AuthApi {
    
    func attemptLogin(withLogin login: String, passwordHash: String, onComplete: (AuthApiResponse) -> ())
    func registerAccount(withData data: RegistrationData, onComplete: (AuthApiResponse) -> ())
    func isActive(token: String) -> Bool
}
