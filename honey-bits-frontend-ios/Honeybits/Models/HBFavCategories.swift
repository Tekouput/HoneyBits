//
//  HBFavCategories.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation
import UIKit


class HBFavCategories {
    
    var name: String
    var tag: String
    var image: UIImage?
    
    init(withName name: String, tag: String, image: UIImage?) {
        self.name = name
        self.tag = tag
        self.image = image
    }
    
}
