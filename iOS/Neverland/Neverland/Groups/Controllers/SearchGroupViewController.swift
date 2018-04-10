//
//  SearchGroupViewController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 10/04/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class SearchGroupViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    var groups = [Group]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
    }

    override func viewDidAppear(_ animated: Bool) {
        NLGroupApi().fetchGroups { dictArr in
            self.groups = []
            for json in dictArr {
                self.groups.append(Group(title: json.value(forKey: "name") as! String, creator: Person.init(creatorData: json.value(forKey: "admin") as? NSDictionary)!))
            }
        }
    }
}

extension SearchGroupViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return groups.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if let cell = tableView.dequeueReusableCell(withIdentifier: "GroupCell") as? GroupCell {
            let group = groups[indexPath.row]
            
            cell.cretorLbl.text = "Created by " + group.creator.nickname
            if let lnk = group.creator.photoURLString {
                cell.av.uploadImageFrom(url: lnk)
            }
            cell.name.text = group.title
            return cell
        }
        
        return UITableViewCell()
    }
}
