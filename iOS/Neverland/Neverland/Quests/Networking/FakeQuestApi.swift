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
    
    
    func registerQuest(title: String, description: String, groupId: Int, onComplete: (QuestApiResponse) -> ()) {
        print("Registered \(title):\(description)")
        onComplete(QuestApiResponse(code: .Successful, message: nil))
    }
    
    func fetchDetailedSolution(withId id: Int, onComplete: (QuestApiResponse) -> ()) {
        
    }
    
    func fetchQuests(from: Int, to: Int, inGroup: Int, onComplete: (QuestApiResponse) -> ()) {
        
    }
    
    func submitSolution(forQuest quest: Int, photo: UIImage, onComplete: (QuestApiResponse) -> ()) {
        
    }
    
    func fetchQuests(from: Int, to: Int, inScope scope: QuestScope, onComplete: (QuestApiResponse) -> ()) {
        
    }
    

}
