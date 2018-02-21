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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // 3 columns - 5px margin of main view ( 5 * 2) = 10 + 10px between images = 2 * 10 = 20 px. 30 px.
        let width = (view.frame.size.width - 30) / 3
        let layout = photoCollectionView.collectionViewLayout as! UICollectionViewFlowLayout
        layout.itemSize = CGSize(width: width, height: width)

        photoCollectionView.delegate = self
        photoCollectionView.dataSource = self
        
    }

}

// MARK: - CollectionViewProtocols
extension QuestDetailedViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 20
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "PhotoCell", for: indexPath)
        
        return cell
    }
    
    
}



