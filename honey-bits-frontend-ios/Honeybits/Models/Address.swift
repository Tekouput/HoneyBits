//
//  Address.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation

class Address {
    
    var country: String
    var state: String
    var city: String?
    var zipCode: String?
    var street: String?
    var number: String?
    
    
    init(country: String, state: String, city: String, zipCode: String, street: String, number: String) {
        self.country = country
        self.state = state
        self.city = city
        self.zipCode = zipCode
        self.street = street
        self.number = number
    }
    
    init(country: String, state: String) {
        self.country = country
        self.state = state
    }
    
}
