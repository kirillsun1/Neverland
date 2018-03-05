//
//  MyChallangesViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit
import JJFloatingActionButton

class MyChallangesViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
        
        configurePlusBtn()
    }
    
    func configurePlusBtn() {
        let actionButton = JJFloatingActionButton()
        actionButton.buttonColor = UIColor.neverlandDarkColor
        
        actionButton.addItem(title: "Take quest", image: UIImage(named: "search_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            self.performSegue(withIdentifier: "TakeQuestSegue", sender: nil)
        }
        
        actionButton.addItem(title: "Suggest quest", image: UIImage(named: "add_btn")?.withRenderingMode(.alwaysTemplate)) { item in
            self.performSegue(withIdentifier: "CreateQuestSegue", sender: nil)
        }

        actionButton.display(inViewController: self)
    }

}

extension MyChallangesViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 5
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "QuestCell") as? QuestCell else {
            fatalError()
        }
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "QuestDetailed", sender: nil)
    }
}

