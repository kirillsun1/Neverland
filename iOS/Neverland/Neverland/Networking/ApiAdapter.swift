//
//  ApiAdapter.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 21/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

class ApiAdapter {
    private var api: ApiConnector!
    
    init(forApi api: ApiConnector) {
        self.api = api
    }
    
    func sendLoginRequest(forUser user: String, hashedPassword: String) -> String {
        
        return ""
    }
    
    func sendSignupRequest(withData: [String]) -> String {
        
        return ""
    }
    
    
}
