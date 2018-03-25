//
//  NLAuthApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 01/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import Alamofire

// todo
class NLAuthApi: AuthApi {
    
    let urlBase = "http://vrot.bounceme.net:8080"
    
    func attemptLogin(withLogin login: String, passwordHash: String, onComplete: @escaping (AuthApiResponse) -> ()) {
        makeAuthApiRequest(url: urlBase + "/login", params: ["username": login, "password": passwordHash], onComplete: onComplete)
    }
    
    func registerAccount(withData data: RegistrationData, onComplete: @escaping (AuthApiResponse) -> ()) {
        makeAuthApiRequest(url: urlBase + "/register", params: ["username": data.login, "password": data.password, "email": data.email,
                                               "firstname": data.firstName, "secondname": data.secondName], onComplete: onComplete)
    }
    
    
    func ifActive(token: String, onComplete: @escaping (AuthApiResponse)->()) {
        makeAuthApiRequest(url: urlBase + "/tokencheck", params: ["token": token], onComplete: onComplete)
    }
    
    func makeAuthApiRequest(url: String, params: [String:Any], onComplete: @escaping (AuthApiResponse) -> ()) {
        SwiftSpinner.show("Connecting to server ... ")
        let request = Alamofire.request(url, method: .get, parameters: params)
        request.responseJSON { response in
            if response.error != nil {
                onComplete(AuthApiResponse(code: .Error, message: nil))
            }
            
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                
                guard let codeInt = JSON.value(forKey: "code") as? Int,
                    let code = ResponseCode(rawValue: codeInt) else {
                        fatalError("Unknown server code. Debug this")
                }
                
                onComplete(AuthApiResponse(code: code, message: JSON.value(forKey: "token") as? String))
            }
            
            SwiftSpinner.hide()
        }
    }
    
}