//
//  HBFavoritesDD.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation
import UIKit


class HBFavoritesDD: NSObject, UICollectionViewDataSource, UICollectionViewDelegate {
    
    // DD = Delegate and Datasource
    
    let reuseIdentifier = "favoriteCell"
    
    var HBFavorites = [HBFavCategories]()
    
    weak var favoritesViewController: UIViewController? // CAUTION this can cause memory loop
    
    // MARK: - Collectionview
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return HBFavorites.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as? HBFavoritesCollectionViewCell else { fatalError("Cannot dequeue favorite cell as HBFavoritescollectionViewCell") }
        
        let category = HBFavorites[indexPath.row]
        
        cell.productCategoryNameLabel.text = category.name
        cell.productCategoryTagLabel.text = category.tag
        cell.productCategoryImageView.image = category.image
        
        return cell
    }
    
    // MARK: - Collection view delegate
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("TAPPED ITEM AT: ", indexPath.row)
        
        // Move to the products of that category
        //        HBFavoritesViewController.showProductsOf(category: )
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        if let categoryProductsViewController = storyboard.instantiateViewController(withIdentifier: "productsViewController") as? ProductsViewController {
            
            categoryProductsViewController.selectedCategory = HBFavorites[indexPath.row]
            favoritesViewController?.navigationController?.pushViewController(categoryProductsViewController, animated: true)
        }
        //        x?.present(categoryProductsViewController!, animated: true, completion: nil)
        //        let fav = HBFavoritesViewController()
        //        fav.showProductsOf(category: HBFavorites[indexPath.row])
    }
    
    
    
    // MARK: - load data
    
    func loadDummyFavoritesCategories() {
        
        let image1 = UIImage(named: "prod1")
        let image2 = UIImage(named: "prod2")
        let image3 = UIImage(named: "prod3")
        
        
        let cat1 = HBFavCategories(withName: "Best organic honey and magical tea", tag: "HoneyBits Favorite", image: image1)
        let cat2 = HBFavCategories(withName: "Packed Products", tag: "HoneyBits Favorite", image: image2)
        let cat3 = HBFavCategories(withName: "Imported Honey", tag: "HoneyBits Favorite", image: image3)
        
        HBFavorites.append(cat1)
        HBFavorites.append(cat2)
        HBFavorites.append(cat3)
        
    }
    
}
