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

class NLQuestApi: QuestApi {
    
    private var urlBase: String = "http://vrot.bounceme.net:8080"
    
    
    //MARK: - Fetching quests.
    func fetchQuests(onComplete: @escaping ([NSDictionary]) -> ()) {
         fetchingLogic(url: self.urlBase+"/getQuestsToTake", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete)
    }
    
    func fetchQuests(inGroup gid: Int, onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase+"/getGroupQuests", params: ["token": User.sharedInstance.token ?? "", "gid": gid], onComplete: onComplete)
    }
    
    func fetchMyQuests(onComplete: @escaping ([NSDictionary])->()) {
        fetchingLogic(url: self.urlBase + "/getMyQuests", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete)
    }
    
    func fetchProofsForQuest(withId id: Int, onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase + "/getQuestsProofs", params: ["token": User.sharedInstance.token ?? "", "qid": id], onComplete: onComplete, jsonKey: "proofs")
    }
    
    func fetchMyProofs(onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase + "/getMyProofs", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete, jsonKey: "proofs")
    }
    
    
    func fetchAllProofs(onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase + "/getAllProofs", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete, jsonKey: "proofs")
    }
    
    func fetchFollowingProofs(onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase + "/getMyFollowingsProofs", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete, jsonKey: "proofs")
    }
    
    func fetchGroupsProofs(onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase + "/getMyGroupsProofs", params: ["token": User.sharedInstance.token ?? ""], onComplete: onComplete, jsonKey: "proofs")
    }
    
    func fetchUserProofs(uid: Int, onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase + "/getUsersProofs", params: ["token": User.sharedInstance.token ?? "", "uid":uid], onComplete: onComplete, jsonKey: "proofs")
    }
    
    func fetchingLogic(url: String, params: [String: Any], onComplete: @escaping ([NSDictionary])->(), jsonKey: String = "quests") {
        let request = Alamofire.request(url, method: .get,
                                        parameters: params)
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])

        request.responseJSON(queue: queue) { response in
            if let result = response.result.value {
                //print(result)
                let JSON = (result as! NSDictionary).value(forKey: "elements") as! [NSDictionary]
                DispatchQueue.main.async {
                    onComplete(JSON)
                }
            }
        }
    }
    
    func voteForProof(pid: Int, value: Int, onComplete: @escaping (Rating)->()) {
        let request = Alamofire.request(self.urlBase + "/vote", method: .get, parameters: ["pid": pid, "value": value, "token": User.sharedInstance.token ?? ""])
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
        request.responseJSON(queue: queue) { response in
            print(response.result.value)
            if let rat = Rating(json: (response.result.value as? NSDictionary)?.value(forKey: "body") as? NSDictionary) {
                onComplete(rat)
            }
        }
    }
    
    //MARK: - Action with quests

    func registerQuest(title: String, description: String, groupId: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        SwiftSpinner.show("Connecting to server ... ")
        
        let params = ["token": User.sharedInstance.token ?? " ",
                      "desc": description,
                      "title": title,
                      "gid": groupId] as [String : Any]
        questActionLogic(url: self.urlBase+"/submitQuest", params: params, onComplete: { response in
            onComplete(response)
            SwiftSpinner.hide()
        })
    }
    
    func takeQuest(qid: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        questActionLogic(url: self.urlBase + "/takeQuest", params: ["token": User.sharedInstance.token ?? "",
                                                                     "qid": qid], onComplete: onComplete)
    }
    
    func dropQuest(qid: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        questActionLogic(url: self.urlBase + "/dropQuest", params: ["token": User.sharedInstance.token ?? "",
                                                            "qid": qid], onComplete: onComplete)
    }
    
    
    func questActionLogic(url: String, params:[String:Any], onComplete: @escaping (QuestApiResponse) -> ()) {
        let request = Alamofire.request(url, method: .get, parameters: params)
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])

        request.responseJSON(queue: queue) { response in
            if let result = response.result.value {
                //print(result as? NSDictionary)
                if let JSON = result as? NSDictionary, JSON.value(forKey: "code") as? Int == 1 {
                    DispatchQueue.main.async {
                        onComplete(QuestApiResponse(code: .Successful, message: nil))
                    }
                } else {
                    DispatchQueue.main.async {
                        onComplete(QuestApiResponse(code: .Error, message: nil))
                    }
                }
            }
        }
    }
    
    func submitProof(qid: Int, img: UIImage, comment: String?, onComplete: @escaping (QuestApiResponse) -> ()) {
        let img = img.wxCompress()
        let params = ["qid": String(qid), "token": User.sharedInstance.token ?? "", "comment": comment ?? ""]        
        Alamofire.upload(multipartFormData: { (multipartFormData) in
            multipartFormData.append(UIImageJPEGRepresentation(img, 1)!, withName: "file", fileName: "name.jpeg", mimeType: "image/jpeg")
            for (key, value) in params {
                multipartFormData.append(value.data(using: String.Encoding.utf8)!, withName: key)
            }
        }, to:urlBase + "/upload")
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
    
    //MARK: - Not implemented yet methods.
    
    func fetchQuests(inScope scope: QuestScope, onComplete: @escaping ([NSDictionary]) -> ()) {
        fatalError("Not implemented yet")
    }
    
}

