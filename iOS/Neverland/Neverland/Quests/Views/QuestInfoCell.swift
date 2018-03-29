//
//  QuestInfoCell.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 17/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class QuestInfoCell: UITableViewCell {

    @IBOutlet weak var questNameLbl: UILabel!
    @IBOutlet weak var questDescrView: UITextView!
    @IBOutlet weak var creatorUsername: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    func fill(quest: Quest) {
        questNameLbl.text = quest.title
        questDescrView.text  = quest.description
        creatorUsername.text = "by \(quest.creator.nickname)"
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)

        // Configure the view for the selected state
    }

}
