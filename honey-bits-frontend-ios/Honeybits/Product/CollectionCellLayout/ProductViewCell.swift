//
//  ProductViewCell.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/17/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ProductViewCell: UICollectionViewCell {
    
    @IBOutlet weak var productImage: UIImageView!
    @IBOutlet weak var productTitle: UILabel!
    @IBOutlet weak var productMaker: UILabel!
    @IBOutlet weak var productPrice: UILabel!
    @IBOutlet weak var productSettings: UIButton!
    @IBOutlet weak var productFavorite: UIButton!
    @IBOutlet weak var loader: UIActivityIndicatorView!
    
    var product_id: String!
    var shop_id: String!
    var vc: UIViewController!
    var product: Product!
    
    func setValues(imageUrl: String, title: String, maker: String, price: String) {
        productImage.downloadedFrom(link: imageUrl).cropExtra()
        productTitle.text = title
        productMaker.text = maker
        productPrice.text = price
        loader.stopAnimating()
    }
    
    @IBAction func showShopProfile(_ sender: Any) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Product", bundle: nil)
        let newViewController = storyBoard.instantiateViewController(withIdentifier: "ProductProfileView") as! ProductProfileViewController
        newViewController.productId = product_id
        vc.present(newViewController, animated: true, completion: nil)
    }
    
    @IBAction func favorite(_ sender: Any) {
        productFavorite.isHidden = true
        loader.startAnimating()
        let urlString = Config.HBUrl + "products/favorite?id=\(product_id!)"
        let url = URL(string: urlString)
        var request = URLRequest(url: url!)
        let is_favorite: Bool! = product.is_favorite ?? false
        if(is_favorite){
            request.httpMethod = "DELETE"
        } else {
            request.httpMethod = "POST"
        }
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            if let response = response as? HTTPURLResponse {
                    OperationQueue.main.addOperation {
                        self.loader.stopAnimating()
                        self.productFavorite.isHidden = false
                    
                        if (response.statusCode == 200) {
                            do {
                                self.product = try JSONDecoder().decode(Product.self, from: data!)
                                self.product.is_favorite! ? self.productFavorite.setImage(#imageLiteral(resourceName: "favorite_ic_red"), for: .normal) : self.productFavorite.setImage(#imageLiteral(resourceName: "favorite_ic"), for: .normal)
                            } catch {
                                print(error)
                            }
                        } else if (response.statusCode == 401) {
                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                            let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                            popup.modalPresentationStyle = .overCurrentContext
                            self.vc.present(popup, animated: true, completion: nil)
                        }
                    }
            }
        }.resume()
    }
}
