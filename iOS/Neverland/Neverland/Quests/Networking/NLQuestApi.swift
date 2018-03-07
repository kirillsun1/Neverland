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
    
    func registerQuest(title: String, description: String, groupId: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        SwiftSpinner.show("Connecting to server ... ")

        let params = ["token": User.sharedInstance.token ?? " ",
                      "desc": description,
                      "title": title,
                      "gid": groupId] as [String : Any]
        
        let request = Alamofire.request(self.urlBase+"/submitquest", method: .get,
                                        parameters: params)
        
        request.responseJSON { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
//                guard let codeInt = JSON.value(forKey: "code") as? Int,
//                    let code = ResponseCode(rawValue: codeInt) else {
//                        fatalError("Unknown server code. Debug this")
//                }
                var code: ResponseCode
                if JSON.value(forKey: "code") as? Int == 1 {
                    code = ResponseCode.Successful
                } else {
                    code = ResponseCode.Error
                }
                
                onComplete(QuestApiResponse(code: code, message: nil))
                SwiftSpinner.hide()
                
            }
        }
    }
    
    func fetchQuests(from: Int, to: Int, inGroup: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError()
    }
    
    func fetchQuests(from: Int, to: Int, inScope scope: QuestScope, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError()

    }
    
    func fetchMyQuests(onComplete: @escaping ([NSDictionary])->()) {
        let request = Alamofire.request(self.urlBase+"/getmyquests", method: .get,
                                        parameters: ["token": User.sharedInstance.token ?? ""])
        
        request.responseJSON { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                print(JSON)
                let questsDict = JSON.value(forKey: "quests") as! [NSDictionary]
                onComplete(questsDict)
            }
        }
    }
    
    func fetchAllQuestsWeShouldProbablyDeleteThisMethodLaterOmg(onComplete: @escaping ([NSDictionary])->()) {
        let request = Alamofire.request(self.urlBase+"/getquests", method: .get,
                                        parameters: ["token": User.sharedInstance.token ?? ""])
        
        request.responseJSON { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                let questsDict = JSON.value(forKey: "quests") as! [NSDictionary]
                onComplete(questsDict)
            }
        }
    }
    
    func takeQuest(qid: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        let request = Alamofire.request(self.urlBase+"/takequest", method: .get,
                                        parameters: ["token": User.sharedInstance.token ?? "",
                                                     "qid": qid])
        
        request.responseJSON { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                print(JSON)
            } else {
                print("ehh.. fail")
            }
        }
    }
    
    func dropQuest(qid: Int, onComplete: @escaping () -> ()) {
        let request = Alamofire.request(self.urlBase+"/dropquest", method: .get,
                                        parameters: ["token": User.sharedInstance.token ?? "",
                                                     "qid": qid])
        
        request.responseJSON { response in
            if let result = response.result.value {
                let JSON = result as! NSDictionary
                print(JSON)
                onComplete()
            } else {
                print("ehh.. fail")
            }
        }
    }
    
    func fetchDetailedSolution(withId id: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError()
    }
    
    func submitSolution(forQuest quest: Int, photo: UIImage, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError()
    }
    
    
    
}

