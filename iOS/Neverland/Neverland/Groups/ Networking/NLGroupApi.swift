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
        fetchingLogic(url: self.urlBase+"/getNewGroups", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete)
    }
    
    func fetchMyGroups(onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase+"/getMyGroups", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete)
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


    //MARK: - Action with quests
    
    func registerGroup(g_name: String, onComplete: @escaping (GroupApiResponse) -> ()) {
        SwiftSpinner.show("Connecting to server ... ")
        
        let params = ["token": User.sharedInstance.token ?? " ", "g_name": g_name]
        groupActionLogic(url: self.urlBase+"/createGroup", params: params, onComplete: { response in
            onComplete(response)
            SwiftSpinner.hide()
        })
    }
    
    func takeGroup(gid: Int, onComplete: @escaping (GroupApiResponse) -> ()) {
        groupActionLogic(url: self.urlBase + "/subscribe", params: ["token": User.sharedInstance.token ?? "",
                                                                    "gid": gid], onComplete: onComplete)
    }
    
    func dropGroup(gid: Int, onComplete: @escaping (GroupApiResponse) -> ()) {
        groupActionLogic(url: self.urlBase + "/unsubscribe", params: ["token": User.sharedInstance.token ?? "",
                                                                      "gid": gid], onComplete: onComplete)
    }
    
    func getGroupSubscribers(gid: Int, onComplete: @escaping ([Person])->()) {
        let request = Alamofire.request(urlBase+"/getGroupSubscribers", method: .get, parameters: ["token": User.sharedInstance.token ?? "", "gid": gid])
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
        
        
        request.responseJSON(queue: queue) { response in
            if let result = response.result.value {
                if let JSON = result as? NSDictionary, let pjson = JSON.value(forKey: "elements") as? [NSDictionary] {
                    var retArray = [Person]()
                    for pData in pjson {
                        let person = Person.init(creatorData: pData)
                        if person != nil { retArray.append(person!) }
                    }
                    DispatchQueue.main.async {
                        onComplete(retArray)
                    }
                    
                }
            }
        }
    }
    
    func groupActionLogic(url: String, params:[String:Any], onComplete: @escaping (GroupApiResponse) -> ()) {
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
    
    func uploadAvatar(_ img: UIImage, gid:Int, onComplete: @escaping (GroupApiResponse) -> ()) {
        let img = img.wxCompress()
        let params = ["token": User.sharedInstance.token ?? "", "gid": "\(gid)"]
        Alamofire.upload(multipartFormData: { (multipartFormData) in
            multipartFormData.append(UIImageJPEGRepresentation(img, 1)!, withName: "file", fileName: "name.jpeg", mimeType: "image/jpeg")
            for (key, value) in params {
                multipartFormData.append(value.data(using: String.Encoding.utf8)!, withName: key)
            }
        }, to:urlBase + "/uploadGroupAvatar")
        { (result) in
            switch result {
            case .success(let upload, _, _):
                upload.responseJSON { response in
                    if let result = response.result.value {
                        print(result)
                        if let JSON = result as? NSDictionary, JSON.value(forKey: "code") as? Int == 1 {
                            onComplete(GroupApiResponse(code: .Successful, message: JSON.value(forKey: "avatar") as? String))
                        } else {
                            onComplete(GroupApiResponse(code: .Error, message: nil))
                        }
                    }
                }
            case .failure:
                onComplete(GroupApiResponse(code: .Error, message: nil))
            }
        }
    }
}


