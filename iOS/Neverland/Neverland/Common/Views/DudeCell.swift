//
//  DudeCell.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/05/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class DudeCell: UITableViewCell {
    @IBOutlet weak var avatar: UIImageView!
    @IBOutlet weak var nicknameLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    func fillWith(person: Person) {
        if let av = person.photoURLString {
            avatar.uploadImageFrom(url: av)
        }
        
        nicknameLbl.text = person.nickname
        nameLbl.text = "\(person.firstName) \(person.secondName)"
    }
    

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
