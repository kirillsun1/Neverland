//
//  QuestCell.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 16/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class QuestCell: UITableViewCell {

    @IBOutlet weak var questName: UILabel!
    @IBOutlet weak var questTakenDate: UILabel!
       
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func fillWith(quest: Quest) {
        questName.text = quest.title
        questTakenDate.text = "01.01.1990"
        if let time = quest.datePicked {
            questTakenDate.text = "\(time.day).\(time.month).\(time.year)"
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)

        
    }

}
