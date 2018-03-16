//
//  FakeNetworkingAp.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 27/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import UIKit

class FakeQuestApi: QuestApi {

    private let fakeData = "some fake json data should be here if i'll want to test methods with fake api"
    
    func registerQuest(title: String, description: String, groupId: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        print("Registered \(title):\(description)")
        onComplete(QuestApiResponse.init(code: .Successful, message: nil))
    }
    
    func fetchDetailedSolution(withId id: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError("Not implemented")
    }
    
    func fetchQuests(inGroup: Int, onComplete: @escaping ([NSDictionary]) -> ()) {
        fatalError("Not implemented")
    }
    
    func submitSolution(qid: Int, img: UIImage, comment: String?, onComplete: @escaping (QuestApiResponse) -> ()) {
        print("Submited quest \(qid) with photo \(img.description)")
        onComplete(QuestApiResponse(code: .Successful, message: nil))
    }
    
    func fetchQuests(inScope scope: QuestScope, onComplete: @escaping ([NSDictionary]) -> ()) {
        fatalError("Not implemented yet")
    }
    
    func dropQuest(qid: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        fatalError("Not implemented yet")
    }

}
