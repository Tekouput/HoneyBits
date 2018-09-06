//
//  ProductsManager.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/2/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class ProductsManager: UIViewController, IndicatorInfoProvider, UICollectionViewDelegate, UICollectionViewDataSource {
    
    var shopId: String!
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        collectionView.dataSource = self
        collectionView.delegate = self
        print("Loading products")
        loadProducts(view: self.view, collection: collectionView, id: shopId)
        
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return products?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ItemCell", for: indexPath) as! ProductCollectionCapeViewCell
        
        var product = products[indexPath.row]
        if (product.pictures!.count > 0){
            cell.productImage.downloadedFrom(link: "\(Config.HBUrl)\(String(describing: product.pictures![0].urls.medium))").cropExtra()
        }else{
            cell.productImage.downloadedFrom(link: "\(Config.HBUrl)images/honey.jpg").cropExtra()
        }
        cell.productTitle.text = product.name
        cell.productMaker.text = product.shop.name
        cell.productPrice.text = product.price.formatted
        
        
        return cell;
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    var products: [Product]!

    func indicatorInfo(for pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return IndicatorInfo(title: "Products")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.destination is CreateProductViewController) {
            let destination = segue.destination as! CreateProductViewController
            destination.shop = shopId
            destination.cv = collectionView
            destination.vc = self
        }
    }
    
    func loadProducts(view: UIView, collection: UICollectionView, id: String) {
        APIController.activityIndicator(title: "Getting products", view: view)
        let urlString = Config.HBUrl + "shops/products?id=\(id)"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            do {
                if let data = data {
                    print("Im in")
                    print(data)
                    _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                    self.products = try JSONDecoder().decode([Product].self, from: data)
                    print(self.products)
                    APIController.removeActivityIndicator()
                    OperationQueue.main.addOperation {
                        collection.reloadData()
                    }
                }
            } catch {
                print(error)
            }
        }.resume()
    }
}
