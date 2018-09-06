//
//  Shop.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation
import UIKit

class Shop {
    
    var name: String
    var profilePicture: UIImage?
    var address: Address
    var description: String?
    var mainProductPicture: UIImage?
    
    init(withName name: String, profilePicture: UIImage?, address: Address, description: String?, mainProductPicture: UIImage?) {
        self.name = name
        self.profilePicture = profilePicture
        self.address = address
        self.description = description
        self.mainProductPicture = mainProductPicture
    }
    
    init(withName name: String, profilePicture: UIImage?, address: Address, description: String?) {
        self.name = name
        self.profilePicture = profilePicture
        self.address = address
        self.description = description
    }
    
}
