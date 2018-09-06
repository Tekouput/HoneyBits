//
//  ProductCollection.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/17/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ProductCollection: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    var viewPerVisible: CGFloat = 2.0
    var products: [Product]! = []
    var vc: UIViewController!
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return products.count
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = (collectionView.bounds.width / viewPerVisible) - 2
        let height = CGFloat(190.0)
        
        return CGSize(width: width, height: height)
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ProductReusableCell", for: indexPath) as! ProductViewCell
        let product = products[indexPath.row]
        if (product.pictures!.count > 0){
            cell.productImage.downloadedFrom(link: "\(Config.HBUrl)\(String(describing: product.pictures![0].urls.medium))").cropExtra()
        }else{
            cell.productImage.downloadedFrom(link: "\(Config.HBUrl)images/honey.jpg").cropExtra()
        }
        
        cell.product_id = String(product.id)
        cell.shop_id = String(product.shop.id)
        cell.vc = vc
        
        cell.productTitle.text = product.name
        cell.productMaker.text = product.shop.name
        cell.productPrice.text = product.price.formatted
        cell.product = product
        product.is_favorite! ? cell.productFavorite.setImage(#imageLiteral(resourceName: "favorite_ic_red"), for: .normal) : cell.productFavorite.setImage(#imageLiteral(resourceName: "favorite_ic"), for: .normal)
        
        return cell
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
