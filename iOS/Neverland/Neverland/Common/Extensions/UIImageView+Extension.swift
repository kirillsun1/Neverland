//
//  UIImageView+Extension.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 25/03/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation
import UIKit
import Alamofire

extension UIImageView {
    func uploadImageFrom(url: String) {
        let queue = DispatchQueue(label: "com.cnoon.response-queue", qos: .utility, attributes: [.concurrent])
        Alamofire.request(url).responseData(queue: queue) { (response) in
            if response.error == nil {
                if let data = response.data {
                    DispatchQueue.main.async {
                        self.image = UIImage(data: data)
                    }
                }
            }
        }
    }
}
