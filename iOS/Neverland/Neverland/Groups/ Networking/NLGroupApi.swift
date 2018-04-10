//
//  NLQuestApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 06/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import UIKit
import Alamofire
import WXImageCompress

class NLGroupApi {
    
    private var urlBase: String = "http://vrot.bounceme.net:8080"
    
    
    //MARK: - Fetching quests.
    
    func fetchGroups(onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase+"/getAllGroups", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete)
    }

    func fetchingLogic(url: String, params: [String: Any], onComplete: @escaping ([NSDictionary])->()) {
        let request = Alamofire.request(url, method: .get,
                                        parameters: params)
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
        
        request.responseJSON(queue: queue) { response in
            if let result = response.result.value {
                print(result)
                let JSON = (result as! NSDictionary).value(forKey: "elements") as! [NSDictionary]
                DispatchQueue.main.async {
                    onComplete(JSON)
                }
            }
        }
    }
//
    //MARK: - Action with quests
    
    func registerGroup(g_name: String, onComplete: @escaping (GroupApiResponse) -> ()) {
        SwiftSpinner.show("Connecting to server ... ")
        
        let params = ["token": User.sharedInstance.token ?? " ", "g_name": g_name]
        questActionLogic(url: self.urlBase+"/createGroup", params: params, onComplete: { response in
            onComplete(response)
            SwiftSpinner.hide()
        })
    }
    
    
    func questActionLogic(url: String, params:[String:Any], onComplete: @escaping (GroupApiResponse) -> ()) {
        let request = Alamofire.request(url, method: .get, parameters: params)
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
        
        request.responseJSON(queue: queue) { response in
            if let result = response.result.value {
                print(result as? NSDictionary)
                if let JSON = result as? NSDictionary, JSON.value(forKey: "code") as? Int == 1 {
                    DispatchQueue.main.async {
                        onComplete(GroupApiResponse(code: .Successful, message: nil))
                    }
                } else {
                    DispatchQueue.main.async {
                        onComplete(GroupApiResponse(code: .Error, message: nil))
                    }
                }
            }
        }
    }
    
}


