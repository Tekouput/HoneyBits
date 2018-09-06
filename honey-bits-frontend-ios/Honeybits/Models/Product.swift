//
//  Product.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation
import UIKit


struct Product: Codable {
    
    var id: Int
    var name: String
    var description: String?
    var price: Price
    var category: [Category]?
    var shop: ShopController.Shop
    var pictures: [Picture]? = []
    var is_favorite: Bool?
}

struct Price: Codable {
    var raw: String
    var formatted: String
}

struct Picture: Codable {
    var id: Int
    var product: Int
    var urls: ShopController.Image
}

struct Category: Codable {
    var id: Int
    var name: String
    var description: String
}


