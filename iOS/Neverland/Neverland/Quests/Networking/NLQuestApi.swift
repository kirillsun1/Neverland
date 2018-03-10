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

class NLQuestApi: QuestApi {
    
    var urlBase: String = "http://vrot.bounceme.net:8080"
    
    
    //MARK: - Fetching quests.
    
    func fetchQuests(inGroup: Int, onComplete: @escaping ([NSDictionary]) -> ()) {
        fetchingLogic(url: self.urlBase+"/getquests", onComplete: onComplete)
    }
    
    func fetchMyQuests(onComplete: @escaping ([NSDictionary])->()) {
        fetchingLogic(url: self.urlBase + "/getmyquests", onComplete: onComplete)
    }
    
    func fetchingLogic(url: String, onComplete: @escaping ([NSDictionary])->()) {
        let request = Alamofire.request(url, method: .get,
                                        parameters: ["token": User.sharedInstance.token ?? ""])
        
        request.responseJSON { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                let questsDict = JSON.value(forKey: "quests") as! [NSDictionary]
                onComplete(questsDict)
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
        questActionLogic(url: self.urlBase+"/submitquest", params: params, onComplete: onComplete)
        SwiftSpinner.hide()
    }
    
    func takeQuest(qid: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        questActionLogic(url: self.urlBase + "/takequest", params: ["token": User.sharedInstance.token ?? "",
                                                                     "qid": qid], onComplete: onComplete)
    }
    
    func dropQuest(qid: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        questActionLogic(url: self.urlBase + "/dropquest", params: ["token": User.sharedInstance.token ?? "",
                                                            "qid": qid], onComplete: onComplete)
    }
    
    func questActionLogic(url: String, params:[String:Any], onComplete: @escaping (QuestApiResponse) -> ()) {
        let request = Alamofire.request(url, method: .get, parameters: params)
        request.responseJSON { response in
            if let result = response.result.value {
                if let JSON = result as? NSDictionary, JSON.value(forKey: "code") as? Int == 1 {
                    onComplete(QuestApiResponse(code: .Successful, message: nil))
                } else {
                    onComplete(QuestApiResponse(code: .Error, message: nil))
                }
            }
        }
    }
    
    //MARK: - Not implemented yet methods.
    
    func fetchDetailedSolution(withId id: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError("Not implemented yet")
    }
    
    func submitSolution(forQuest quest: Int, photo: UIImage, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError("Not implemented yet")
    }
    
    func fetchQuests(inScope scope: QuestScope, onComplete: @escaping ([NSDictionary]) -> ()) {
        fatalError("Not implemented yet")
    }
    
}

