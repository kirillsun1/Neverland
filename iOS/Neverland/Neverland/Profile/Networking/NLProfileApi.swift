//
//  NLProfileApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 08/04/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import UIKit
import Alamofire

class NLProfileApi {
    
    private var urlBase: String = "http://vrot.bounceme.net:8080"
    
    func getMyInfo(onComplete: @escaping (Person) -> ()) {
        getInfoLogic(url: urlBase + "/getMyInfo", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete)
//        let request = Alamofire.request(urlBase + "/getMyInfo", method: .get, parameters: ["token": User.sharedInstance.token ?? ""])
//        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
//
//        request.responseJSON(queue: queue) { response in
//            if let result = response.result.value {
//                let JSON = result as! NSDictionary
//                //print(JSON)
//                let person = Person.init(creatorData: JSON)
//                DispatchQueue.main.async {
//                    onComplete(person!)
//                }
//            }
//        }
    }
    
    func getUserInfo(uid: Int, onComplete: @escaping (Person) -> ()) {
        getInfoLogic(url: urlBase + "/getUsersInfo", params: ["token": User.sharedInstance.token ?? "", "uid": uid], onComplete: onComplete)
//        let request = Alamofire.request(urlBase + "/getUsersInfo", method: .get, parameters: ["token": User.sharedInstance.token ?? "", "uid": uid])
//        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
//
//        request.responseJSON(queue: queue) { response in
//            if let result = response.result.value {
//                let JSON = result as! NSDictionary
//                //print(JSON)
//                let person = Person.init(creatorData: JSON)
//                DispatchQueue.main.async {
//                    onComplete(person!)
//                }
//            }
//        }
    }
    
    func getInfoLogic(url: String, params: [String: Any], onComplete: @escaping (Person) -> ()) {
        let request = Alamofire.request(url, method: .get, parameters: params)
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
        
        request.responseJSON(queue: queue) { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                let person = Person.init(creatorData: JSON)
                DispatchQueue.main.async {
                    onComplete(person!)
                }
            }
        }
    }
    
    func getMySuggestedQuests(onComplete: @escaping ([NSDictionary]) -> ()) {
        getQuestsLogic(url: urlBase + "/getMySuggestedQuests", params: ["token": User.sharedInstance.token ?? "", "uid": 1], onComplete: onComplete)
    }
    
    func getSuggestedQuests(uid: Int, onComplete: @escaping ([NSDictionary]) -> ()) {
        
        getQuestsLogic(url: urlBase + "/getSuggestedQuests",
                       params: ["token": User.sharedInstance.token ?? "", "uid": uid], onComplete: onComplete)
    }
    
    func getQuestsLogic(url: String, params: [String: Any], onComplete: @escaping ([NSDictionary]) -> ()) {
        let request = Alamofire.request(url, method: .get, parameters: params)
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])

        request.responseJSON(queue: queue) { response in
            if let result = response.result.value {
                let JSON = result as! [NSDictionary]

                DispatchQueue.main.async {
                    onComplete(JSON)
                }
            }
        }
    }
    
    func uploadAvatar(_ img: UIImage, onComplete: @escaping (QuestApiResponse) -> ()) {
        let img = img.wxCompress()
        let params = ["token": User.sharedInstance.token ?? ""]
        Alamofire.upload(multipartFormData: { (multipartFormData) in
            multipartFormData.append(UIImageJPEGRepresentation(img, 1)!, withName: "file", fileName: "name.jpeg", mimeType: "image/jpeg")
            for (key, value) in params {
                multipartFormData.append(value.data(using: String.Encoding.utf8)!, withName: key)
            }
        }, to:urlBase + "/uploadAvatar")
        { (result) in
            switch result {
            case .success(let upload, _, _):
                upload.responseJSON { response in
                    if let result = response.result.value {
                        print(result)
                        if let JSON = result as? NSDictionary, JSON.value(forKey: "code") as? Int == 1 {
                            onComplete(QuestApiResponse(code: .Successful, message: nil))
                        } else {
                            onComplete(QuestApiResponse(code: .Error, message: nil))
                        }
                    }
                }
            case .failure:
                onComplete(QuestApiResponse(code: .Error, message: nil))
            }
        }
    }
}
