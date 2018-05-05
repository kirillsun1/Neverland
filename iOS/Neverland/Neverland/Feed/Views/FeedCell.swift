//
//  QuestAnswerCell.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 16/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class FeedCell: UITableViewCell {

    @IBOutlet weak var titleLbl: UILabel!
    @IBOutlet weak var commentLbl: UILabel!
    @IBOutlet weak var photoView: UIImageView!
    @IBOutlet weak var userAvatarView: UIImageView!
    @IBOutlet weak var ratingProgr: UIProgressView!
    @IBOutlet weak var photoWidthConstr: NSLayoutConstraint!
    @IBOutlet weak var photoHeigthConstr: NSLayoutConstraint!
    @IBOutlet weak var segueButton: UIButton?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    
    }
    
    func fillWith(_ proof: Proof) {
        self.titleLbl.text = "\(proof.proofer.nickname) - \(proof.quest?.title ?? "Quest Title")"
        self.commentLbl.text = proof.comment
        self.photoView.uploadImageFrom(url: proof.picPath)
        if let btn = self.segueButton {
            btn.tag = proof.proofer.id
        }
        if let imgUrl = proof.proofer.photoURLString {
            self.userAvatarView.uploadImageFrom(url: imgUrl)
        } else {
            self.userAvatarView.image = UIImage.init(named: "1")
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

}
