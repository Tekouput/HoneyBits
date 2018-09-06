//
//  ProductsDD.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation
import UIKit

class ProductsDD: NSObject, UICollectionViewDataSource, UICollectionViewDelegate {
    
    let reuseIdentifier = "productCell"
    var products = [Product]()
    
    weak var productsViewController: UIViewController?
    
    // MARK: - Collection View Data Source
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return products.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let productCell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as? ProductCollectionViewCell else { fatalError("Cannot dequeue product cell as ProductCollectionViewCell") }
        
        let product = products[indexPath.row]
        
        //productCell.imageView.image = product.image
        productCell.nameLabel.text = product.name
        productCell.priceLabel.text = "\(product.price)"
        //productCell.sellerNameLabel.text = product.shopName
        
        
        return productCell
    }
    
    // MARK: - Collection View delegate
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        if let productViewController = storyboard.instantiateViewController(withIdentifier: "productViewController") as? ProductViewController {
            
            let shop = Shop(withName: "Macaco Store", profilePicture: UIImage(named: "prod3"), address: Address(country: "Rep. Dom.", state: "Distrito Nacional"), description: "Best Shop!!")

            
            productViewController.product = products[indexPath.row]
            productViewController.seller = shop


            productsViewController?.navigationController?.pushViewController(productViewController, animated: true)
        }
        
    }
    
    
}
