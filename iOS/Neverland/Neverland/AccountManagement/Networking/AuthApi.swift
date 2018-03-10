//
//  AuthApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 22/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

protocol AuthApi {
    
    func attemptLogin(withLogin login: String, passwordHash: String, onComplete: @escaping (AuthApiResponse) -> ())
    func registerAccount(withData data: RegistrationData, onComplete: @escaping (AuthApiResponse) -> ())
    func ifActive(token: String, onComplete: @escaping (AuthApiResponse)->())
}
