//
//  GroupCell.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 10/04/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class GroupCell: UITableViewCell {

    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var av: UIImageView!
    @IBOutlet weak var cretorLbl: UILabel!
    
    
    func fillWith(_ group: Group) {
        self.cretorLbl.text = "Created by " + group.creator.nickname
        if let lnk = group.avatarURL {
            self.av.uploadImageFrom(url: lnk)
        } else {
            self.av.image = UIImage(named: "placeholder_02")
        }
        self.name.text = group.title
    }

}
