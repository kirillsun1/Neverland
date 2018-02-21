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
       
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)

        
    }

}
