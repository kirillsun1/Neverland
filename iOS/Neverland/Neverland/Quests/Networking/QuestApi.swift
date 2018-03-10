//
//  QuestApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 26/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import UIKit

enum QuestScope {
    case world, follwoing, groups
}


protocol QuestApi {
    
    func registerQuest(title: String, description: String, groupId: Int, onComplete: @escaping (QuestApiResponse) -> ())
    func fetchQuests(inGroup:Int, onComplete: @escaping ([NSDictionary]) -> ())
    func fetchQuests(inScope scope: QuestScope, onComplete: @escaping ([NSDictionary]) -> ())
    func fetchDetailedSolution(withId id: Int, onComplete: @escaping (QuestApiResponse) -> ())
    func submitSolution(forQuest quest: Int, photo: UIImage, onComplete: @escaping (QuestApiResponse) -> ())
    func dropQuest(qid: Int, onComplete: @escaping (QuestApiResponse) -> ())
}


