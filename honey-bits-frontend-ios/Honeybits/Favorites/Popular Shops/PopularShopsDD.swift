//
//  PopularShopsDD.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/2/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import Cosmos

class PopularShopsDD: NSObject, UICollectionViewDataSource, UICollectionViewDelegate  {

    let reuseIdentifier = "popularShopCell"
    var shops: [ShopController.Shop]!
    
    weak var favoritesViewController: UIViewController?
    
    // MARK: - Collection View Data Source
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return shops?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let shopCell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as? HBPopularShopCollectionViewCell else { fatalError("Cannot dequeue shop cell as HBPopularShopCollectionViewCell") }
        
        let shop = shops[indexPath.row]
        
        shopCell.vc = favoritesViewController
        shopCell.shopId = String(shop.id)
        shopCell.shop = shop
        
        shopCell.shopLogoImageView.contentMode = .scaleAspectFit
        shopCell.shopLogoImageView.cropExtra()
        shopCell.shopLogoImageView.downloadedFrom(link: Config.HBUrl + shop.shop_logo.thumb.absoluteString).makeRounded()
        shopCell.shopMainProductImageView.contentMode = .scaleAspectFill
        shopCell.shopMainProductImageView.downloadedFrom(link: Config.HBUrl + shop.shop_picture.medium.absoluteString).cropExtra()
        shopCell.shopNameLabel.text = shop.name
        shopCell.shopRatingView.rating = 4
        shopCell.favoriteButton.tag = shop.id
        if (shop.is_favorite){
            shopCell.favoriteButton.setBackgroundImage(UIImage(named: "favorite_ic_red")!, for: .normal)
        }
        
        
        return shopCell
    }
    
    func loadFavorites(view: UIView, collection: UICollectionView) {
        APIController.activityIndicator(title: "Getting stores", view: view)
        let urlString = Config.HBUrl + "shops/favorites"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            do {
                if let response = response as? HTTPURLResponse {
                    if (response.statusCode == 401) {
                        OperationQueue.main.addOperation {
                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                            let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                            popup.modalPresentationStyle = .overCurrentContext
                            self.favoritesViewController?.present(popup, animated: true, completion: nil)
                        }
                    } else {
                        if let data = data {
                            _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                            self.shops = try JSONDecoder().decode([ShopController.Shop].self, from: data)
                            APIController.removeActivityIndicator()
                            OperationQueue.main.addOperation {
                                collection.reloadData()
                            }
                        }
                    }
                    
                }
            } catch {
                print(error)
            }
        }.resume()
    }
}
