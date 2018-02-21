//
//  MockedApi.swift
//  Neverland
//
//  Created by Konstantin Saposnitsenko on 21/02/2018.
//  Copyright © 2018 Konstantin Saposnitsenko. All rights reserved.
//

import Foundation

class MockedApi: ApiConnector {
    func send(query: String) -> String {
        return """
            Here some fake JSON for test while server is not working ...
        """
    }
}
