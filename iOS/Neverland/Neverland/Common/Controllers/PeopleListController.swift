//
//  PeopleListController.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 15/05/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import UIKit

class PeopleListController: UIViewController {

    public enum `Type` {
        case followers, followings, subscriptions
    }
    
    public var thisType: Type = .followers
    public var uid: Int?
    
    private let groupApi = NLGroupApi()
    private let profileApi = NLProfileApi()
    private var people = [Person]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
        
        switch thisType {
        case .followings:
            self.navigationItem.title = "Followings"
        case .followers:
            self.navigationItem.title = "Followers"
        case .subscriptions:
            self.navigationItem.title = "Subscribers"
        }
        
        fetchData()
        
    }
    
    
    func fetchData() {
        switch thisType {
        case .subscriptions:
            groupApi.getGroupSubscribers(gid: uid!)  { self.people = $0 }
        case .followings:
            if uid == nil { profileApi.getMyFollowings { self.people = $0 } }
            else { profileApi.getUsersFollowings(uid: uid!)  { self.people = $0 } }
        case .followers:
            if uid == nil { profileApi.getMyFollowers { self.people = $0 } }
            else { profileApi.getUsersFollowers(uid: uid!)  { self.people = $0 } }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "UserDetailed" {
            let vc = segue.destination as! ProfileViewController
            vc.uid = (sender as! Int)
        }
    }
 
}

extension PeopleListController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return people.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "DudeCell") as? DudeCell else {
            return UITableViewCell()
        }
        
        cell.fillWith(person: people[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "UserDetailed", sender: (people[indexPath.row]).id )
    }
    
}
