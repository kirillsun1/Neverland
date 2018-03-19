//
//  QuestDetailedViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 17/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class QuestDetailedViewController: UIViewController {

    @IBOutlet weak var photoCollectionView: UICollectionView!
    @IBOutlet weak var questDescrView: UITextView!
    @IBOutlet weak var sendBtn: UIButton!
    @IBOutlet weak var delBtn: UIBarButtonItem!
    
    var quest: Quest?
    var finished = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // 3 columns - 5px margin of main view ( 5 * 2) = 10 + 10px between images = 2 * 10 = 20 px. 30 px.
        let width = (view.frame.size.width - 30) / 3
        let layout = photoCollectionView.collectionViewLayout as! UICollectionViewFlowLayout
        layout.itemSize = CGSize(width: width, height: width)

        photoCollectionView.delegate = self
        photoCollectionView.dataSource = self
        
        self.navigationItem.title = quest?.title
        self.questDescrView.text = quest?.description
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        sendBtn.isHidden = finished
        sendBtn.isEnabled = !finished
        delBtn.isEnabled = !finished
        if finished {  NLQuestApi().dropQuest(qid: quest!.id) { response in } }
    }
    
    @IBAction func deleteQuest() {
        if (quest == nil) { return }
            
        NLQuestApi().dropQuest(qid: quest!.id) { response in
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ProofSegue" {
            guard let vc = segue.destination as? ProofSubmitViewController else {
                fatalError("oh-oh-oh. Something bad happened here :(")
            }
            
            vc.quest = self.quest
        }
    }

}

// MARK: - CollectionViewProtocols
extension QuestDetailedViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "PhotoCell", for: indexPath)
        
        return cell
    }
    
    
}



