//
//  NLAuthApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 01/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import Alamofire

//
class NLAuthApi: AuthApi {
    
    let urlBase = "http://vrot.bounceme.net:8080"
    
    func attemptLogin(withLogin login: String, passwordHash: String, onComplete: @escaping (AuthApiResponse) -> ()) {
        let request = Alamofire.request(urlBase+"/login", method: .get, parameters: ["username": login, "password": passwordHash])
        SwiftSpinner.show("Connecting to server ... ")
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
    
    func registerAccount(withData data: RegistrationData, onComplete: @escaping (AuthApiResponse) -> ()) {
        SwiftSpinner.show("Connecting to server ... ")
        let request = Alamofire.request(urlBase+"/register", method: .get,
                          parameters: ["username": data.login, "password": data.password, "email": data.email,
                                       "firstname": data.firstName, "secondname": data.secondName])
        
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
                SwiftSpinner.hide()
            }
        }
    }
    
    func ifActive(token: String, onComplete: @escaping (AuthApiResponse)->()) {
        SwiftSpinner.show("Connecting to server ... ")
        let request = Alamofire.request(urlBase+"/tokencheck", method: .get,
                                        parameters: ["token": token])
        
        request.responseJSON { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                guard let codeInt = JSON.value(forKey: "code") as? Int,
                    let code = ResponseCode(rawValue: codeInt) else {
                        fatalError("Unknown server code. Debug this")
                }
                onComplete(AuthApiResponse(code: code, message: nil))
                SwiftSpinner.hide()
            
            }
        }
    }
    
    
}
