//
//  QuestAnswerCell.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 16/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class FeedCell: UITableViewCell {

    @IBOutlet weak var userNameLbl: UILabel!
    @IBOutlet weak var questTitleLbl: UILabel!
    @IBOutlet weak var photoView: UIImageView!
    @IBOutlet weak var userAvatarView: UIImageView!
    @IBOutlet weak var ratingProgr: UIProgressView!
    @IBOutlet weak var photoWidthConstr: NSLayoutConstraint!
    @IBOutlet weak var photoHeigthConstr: NSLayoutConstraint!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    
    }
    
    func fillWith(_ proof: Proof) {
        self.userNameLbl.text = proof.proofer.nickname
        self.questTitleLbl.text = proof.quest?.title ?? "Quest Title"
        self.photoView.uploadImageFrom(url: proof.picPath)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

}
