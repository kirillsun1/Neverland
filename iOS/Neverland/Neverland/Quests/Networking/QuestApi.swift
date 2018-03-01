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
    
    func registerQuest(title: String, description: String, groupId: Int, onComplete: (QuestApiResponse) -> ())
    func fetchQuests(from: Int, to: Int, inGroup:Int, onComplete: (QuestApiResponse) -> ())
    func fetchQuests(from: Int, to: Int, inScope scope: QuestScope, onComplete: (QuestApiResponse) -> ())
    func fetchDetailedSolution(withId id: Int, onComplete: (QuestApiResponse) -> ())
    func submitSolution(forQuest quest: Int, photo: UIImage, onComplete: (QuestApiResponse) -> ())
}


