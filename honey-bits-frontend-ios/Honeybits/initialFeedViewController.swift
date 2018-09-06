//
//  initialFeedViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 4/24/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class initialFeedViewController: UIViewController, IndicatorInfoProvider {

    @IBOutlet weak var notUserElement: UIStackView!
    @IBOutlet weak var popularShopsFeed: UICollectionView!
    @IBOutlet weak var topPopularTitle: NSLayoutConstraint!
    @IBOutlet weak var latestProductsFeed: UICollectionView!
    
    var productDelegate: ProductCollection!
    var productDelegateLatest: ProductCollection!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if APIController.valid {
            notUserElement.isHidden = true
            topPopularTitle.constant = -40
        }
        
        productDelegate = ProductCollection()
        productDelegate.vc = self
        popularShopsFeed.delegate = productDelegate
        popularShopsFeed.dataSource = productDelegate
        getPopularProducts(datasource: productDelegate)
        
        productDelegateLatest = ProductCollection()
        productDelegateLatest.vc = self
        productDelegateLatest.viewPerVisible = 2.5
        latestProductsFeed.delegate = productDelegateLatest
        latestProductsFeed.dataSource = productDelegateLatest
        getLatestProducts(datasource: productDelegateLatest)
        
        
        // Do any additional setup after loading the view.
        
        //let getStartedViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "getStartedViewController")

        //self.present(getStartedViewController, animated: true, completion: nil)
    }

    func getPopularProducts(datasource: ProductCollection){
        let urlString = Config.HBUrl + "products/popular"
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
                    datasource.products = Array(try JSONDecoder().decode([Product].self, from: data).prefix(6))
                    APIController.removeActivityIndicator()
                    OperationQueue.main.addOperation {
                        self.popularShopsFeed.reloadData()
                    }
                }
            } catch {
                print(error)
            }
        }.resume()
    }
    
    func getLatestProducts(datasource: ProductCollection){
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
                    datasource.products = Array(try JSONDecoder().decode([Product].self, from: data).prefix(6))
                    APIController.removeActivityIndicator()
                    OperationQueue.main.addOperation {
                        self.latestProductsFeed.reloadData()
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
        return IndicatorInfo(title: "Your feed")
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
