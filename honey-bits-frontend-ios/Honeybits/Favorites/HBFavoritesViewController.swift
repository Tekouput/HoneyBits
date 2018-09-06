//
//  honeybitsFavoritesViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 4/24/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class FavoriteUIButton: UIButton {
    var loader: UIActivityIndicatorView!
}

class HBFavoritesViewController: UIViewController, IndicatorInfoProvider, UIPopoverPresentationControllerDelegate {

    var favoritesDD = ProductCollection()
    @IBOutlet weak var HBFavoritesCollectionView: UICollectionView!
    
    var popularShopsDD = PopularShopsDD()
    @IBOutlet weak var HBPopularShopsCollectionView: UICollectionView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        favoritesDD.vc = self
        HBFavoritesCollectionView.delegate = favoritesDD
        HBFavoritesCollectionView.dataSource = favoritesDD
        getFavorites(datasource: favoritesDD)
        
        popularShopsDD.favoritesViewController = self
        HBPopularShopsCollectionView.delegate = popularShopsDD
        HBPopularShopsCollectionView.dataSource = popularShopsDD
        popularShopsDD.loadFavorites(view: self.view, collection: HBPopularShopsCollectionView)
        
    }
    
    func getFavorites(datasource: ProductCollection){
        APIController.activityIndicator(title: "Getting stores", view: view)
        let urlString = Config.HBUrl + "products/latest"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            do {
                if let data = data {
                    _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                    datasource.products = try JSONDecoder().decode([Product].self, from: data)
                    APIController.removeActivityIndicator()
                    OperationQueue.main.addOperation {
                        self.HBFavoritesCollectionView.reloadData()
                    }
                }
            } catch {
                print(error)
            }
            }.resume()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func indicatorInfo(for pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return IndicatorInfo(title: "Editors' Choice")
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
