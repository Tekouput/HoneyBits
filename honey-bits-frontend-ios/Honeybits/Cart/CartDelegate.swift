//
//  CartDelegate.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/22/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class CartDelegate: NSObject, UITableViewDelegate, UITableViewDataSource {
    struct Cart: Codable {
        var products: [Product]! = []
        var Address: Address?
    }
    
    struct Address: Codable {
        
    }
    
    var cart: Cart!
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return cart.products.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProductCartCell", for: indexPath) as! ProductTableViewCell
        cell.productName.text = cart.products[indexPath.row].name
        cell.productPrice.text = cart.products[indexPath.row].price.formatted
        cell.productStore.text = cart.products[indexPath.row].shop.name
        
        var urlImages = "\(Config.HBUrl)images/honey.jpg";
        if ((cart.products[indexPath.row].pictures?.count)! > 0){
            urlImages = "\(Config.HBUrl)\(cart.products[indexPath.row].pictures![0].urls.thumb.absoluteURL)"
        }
        
        cell.productImage.downloadedFrom(link: urlImages).layer.cornerRadius = 15.0
        
        return cell;
    }
    
}
