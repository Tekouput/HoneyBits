//
//  HBPopularShopCollectionViewCell.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 4/25/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import Cosmos
import MaterialComponents.MaterialCards

class HBPopularShopCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var shopMainProductImageView: UIImageView!
    @IBOutlet weak var shopLogoImageView: UIImageView!
    @IBOutlet weak var shopNameLabel: UILabel!
    @IBOutlet weak var shopRatingView: CosmosView!
    @IBOutlet weak var card: MDCCard!
    @IBOutlet weak var favoriteButton: FavoriteUIButton!
    @IBOutlet weak var loader: UIActivityIndicatorView!
    
    var vc: UIViewController!
    var shopId: String!
    var shop: ShopController.Shop!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        card.frame = UIEdgeInsetsInsetRect(card.frame, UIEdgeInsetsMake(10, 10, 10, 10))
        favoriteButton.loader = loader
        loader.hidesWhenStopped = true;
        favoriteButton.addTarget(self, action: #selector(addToFavorites), for: .touchUpInside)
        
    }
    
    @objc func addToFavorites(sender: FavoriteUIButton!){
        if(shop.is_favorite){
            sender.isHidden = true
            sender.loader.startAnimating()
            let urlString = Config.HBUrl + "favorite"
            let parameters = ["id": "\(sender.tag)"]
            
            let url = URL(string: urlString)
            var request = URLRequest(url: url!)
            request.httpMethod = "DELETE"
            request.addValue("application/json", forHTTPHeaderField: "Content-type")
            request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
            guard let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: []) else {return}
            
            request.httpBody = httpBody
            
            let session = URLSession.shared
            session.dataTask(with: request) { (data, response, error) in
                do {
                    if let response = response as? HTTPURLResponse {
                        if (response.statusCode == 401) {
                            OperationQueue.main.addOperation {
                                let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                                popup.modalPresentationStyle = .overCurrentContext
                                self.vc?.present(popup, animated: true, completion: nil)
                            }
                        } else {
                            if let data = data {
                                _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                                OperationQueue.main.addOperation {
                                    sender.loader.stopAnimating()
                                    sender.setBackgroundImage(UIImage(named: "favorite_ic")!, for: UIControlState.normal)
                                    sender.setBackgroundImage(UIImage(named: "favorite_ic")!, for: UIControlState.selected)
                                    sender.setBackgroundImage(UIImage(named: "favorite_ic")!, for: UIControlState.highlighted)
                                    sender.isHidden = false
                                    self.shop.is_favorite = false
                                }
                            }
                        }
                    }
                } catch {
                    print(error)
                }
            }.resume()
        } else {
            sender.isHidden = true
            sender.loader.startAnimating()
            let urlString = Config.HBUrl + "favorite"
            let parameters = ["id": "\(sender.tag)"]
            
            let url = URL(string: urlString)
            var request = URLRequest(url: url!)
            request.httpMethod = "POST"
            request.addValue("application/json", forHTTPHeaderField: "Content-type")
            request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
            guard let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: []) else {return}
            
            request.httpBody = httpBody
            
            let session = URLSession.shared
            session.dataTask(with: request) { (data, response, error) in
                do {
                    if let response = response as? HTTPURLResponse {
                        OperationQueue.main.addOperation {
                            do {
                            if (response.statusCode == 401) {
                                let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                                popup.modalPresentationStyle = .overCurrentContext
                                self.vc?.present(popup, animated: true, completion: nil)
                            } else {
                                if let data = data {
                                    _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                                    sender.setBackgroundImage(#imageLiteral(resourceName: "favorite_ic_red"), for: UIControlState.normal)
                                    sender.setBackgroundImage(#imageLiteral(resourceName: "favorite_ic_red"), for: UIControlState.selected)
                                    sender.setBackgroundImage(#imageLiteral(resourceName: "favorite_ic_red"), for: UIControlState.highlighted)
                                    self.shop.is_favorite = true
                                }
                            }
                            sender.loader.stopAnimating()
                            sender.isHidden = false
                            } catch {
                                print(error)
                            }
                        }
                    }
                } catch {
                    print(error)
                }
            }.resume()
        }
    }
    
    @IBAction func openProfileView(_ sender: Any) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let newViewController = storyBoard.instantiateViewController(withIdentifier: "ShopProfileViews") as! ShopProfileRouterViewController
        
        newViewController.shopId = shopId
        newViewController.viewController = vc
        vc.present(newViewController, animated: true, completion: nil)
    }
}
