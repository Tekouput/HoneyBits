//
//  ShopManagerController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 7/29/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class ShopManagerController: UIViewController {

    @IBOutlet weak var imageHolder: UIImageView!
    @IBOutlet weak var shopTitle: UILabel!
    @IBOutlet weak var shopDescription: UILabel!
    var shopId: String!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getShopInfo(id: shopId)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is ManagerViewController{
            let vc = segue.destination as! ManagerViewController
            vc.shopId = shopId
        }
    }
    
    func getShopInfo(id: String){
        APIController.activityIndicator(title: "Getting store info", view: self.view)
        let urlString = Config.HBUrl + "shops/single?id=\(id)"
        print(urlString)
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            do {
                if let data = data {
                    _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                    let shop = try JSONDecoder().decode(ShopController.Shop.self, from: data)
                    self.showInfo(shop: shop)
                    APIController.removeActivityIndicator()
                }
            } catch {
                print(error)
            }
            }.resume()
    }

    func showInfo(shop: ShopController.Shop){
        OperationQueue.main.addOperation {
            self.imageHolder.downloadedFrom(link: Config.HBUrl + shop.shop_picture.big.absoluteString).cropExtra()
            self.shopTitle.font = UIFont.fontAwesome(ofSize: 18)
            self.shopTitle.text = shop.name ?? ""
            self.shopDescription.text = shop.description ?? ""
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
