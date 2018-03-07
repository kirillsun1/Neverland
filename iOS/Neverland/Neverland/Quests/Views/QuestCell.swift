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
        // Initialization code
    }
    
    func fillWith(quest: Quest) {
        questName.text = quest.title
        questTakenDate.text = "xx.xx.xxxx"
        if let time = quest.datePicked {
            questTakenDate.text = "\(time.day ?? 0).\(time.month ?? 0).\(time.year ?? 0)"
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)

        
    }

}
