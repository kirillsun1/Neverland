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
    @IBOutlet weak var photoWidthConstr: NSLayoutConstraint!
    @IBOutlet weak var photoHeigthConstr: NSLayoutConstraint!
    @IBOutlet weak var segueButton: UIButton?
    @IBOutlet weak var rateUpButton: UIButton!
    @IBOutlet weak var rateDownButton: UIButton!
    @IBOutlet weak var likesLbl: UILabel!
    @IBOutlet weak var dislikesLbl: UILabel!
    @IBOutlet weak var percProgBar: UIProgressView!
    
    private var pid: Int!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    
    }
    
    @IBAction func voteForPressed() {
        NLQuestApi().voteForProof(pid: pid, value: 1) { newRating in
            DispatchQueue.main.sync {
                self.setRatingBar(rating: newRating)
            }
        }
    }
    
    @IBAction func voteAgainPressed() {
        NLQuestApi().voteForProof(pid: pid, value: 0) { newRating in
            DispatchQueue.main.sync {
                self.setRatingBar(rating: newRating)
            }
        }
    }
    
    func fillWith(_ proof: Proof) {
        self.pid = proof.id
        self.titleLbl.text = "\(proof.proofer.nickname) - \(proof.quest?.title ?? "Quest Title")"
        self.commentLbl.text = proof.comment
        self.setRatingBar(rating: proof.rating)
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
    
    func setRatingBar(rating: Rating) {
        self.likesLbl.text = String(rating.likes)
        self.dislikesLbl.text = String(rating.dislikes)
        self.percProgBar.progress = rating.percentage
        
        rateUpButton.imageView?.image = UIImage(named: "ilike")
        rateDownButton.imageView?.image = UIImage(named: "idislike")
        
        switch(rating.myVote) {
        case .against:
            rateUpButton.isEnabled = false
            rateDownButton.isEnabled = false
            rateDownButton.imageView?.image = UIImage(named: "dislike")
        case .for:
            rateUpButton.isEnabled = false
            rateDownButton.isEnabled = false
            rateUpButton.imageView?.image = UIImage(named: "like")
        case .none:
            rateUpButton.isEnabled = true
            rateDownButton.isEnabled = true
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

}
