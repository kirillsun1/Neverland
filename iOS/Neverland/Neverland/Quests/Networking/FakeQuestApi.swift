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
    

    // called from QuestApi now.
//    func generateQuests() -> [Quest] {
//        var quests = [Quest]()
//        let person = Person(id: 1, nickname: "Voldemort", photoURLString: "https://cdn.pinkvilla.com/files/styles/contentpreview/public/Albus-Dumbledore-Bollywood-Actor-Audition.jpg?itok=-ejADgE3")
//        for i in 0 ..< 200 {
//            let q = Quest.init(id: i, title: "Quest # \(i)", groupId: 0, description: "Some description here blah blah", creator: person) //solution: [Solution]())
//            quests.append(q)
//        }
//        return quests
//    }
    
    func registerQuest(title: String, description: String, groupId: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        print("Registered \(title):\(description)")
        onComplete(QuestApiResponse.init(code: .Successful, message: nil))
    }
    
    func fetchDetailedSolution(withId id: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        
    }
    
    func fetchQuests(from minValue: Int, to maxValue: Int, inGroup: Int, onComplete: @escaping (QuestApiResponse) -> ()) {
        // group part will be implemented during 2nd iteration
        
        var response = QuestApiResponse.init(code: .Successful, message: nil)
        let end = maxValue >= response.q.count ? response.q.count - 1 : maxValue
        
        if minValue >= end {
            onComplete(QuestApiResponse.init(code: .Error, message: "No more entries"))
        } else {
            response.fillQuestArray(from: minValue, to: end)
            onComplete(response)
        }

        
    }
    
    func submitSolution(forQuest quest: Int, photo: UIImage, onComplete: @escaping (QuestApiResponse) -> ()) {
        
    }
    
    func fetchQuests(from: Int, to: Int, inScope scope: QuestScope, onComplete: @escaping (QuestApiResponse) -> ()) {
        // it'll be done during 2nd and 3rd iteration
    }
    

}
