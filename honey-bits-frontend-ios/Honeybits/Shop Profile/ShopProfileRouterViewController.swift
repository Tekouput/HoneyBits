//
//  ShopProfileRouterViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 6/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class ShopProfileRouterViewController: ButtonBarPagerTabStripViewController {

    let purpleInspireColor = UIColor(red:0.13, green:0.03, blue:0.25, alpha:1.0)
    var shopId: String!
    var viewController: UIViewController!

    override func viewDidLoad() {
        
        ShopProfileRouterViewController.getStoreProfile(shopId: shopId, vc: viewController)
        
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        // change selected bar color
        
        settings.style.buttonBarBackgroundColor = .white
        settings.style.buttonBarItemBackgroundColor = .white
        settings.style.selectedBarBackgroundColor = purpleInspireColor
        buttonBarView.selectedBar.backgroundColor = UIColor(rgb: 0xFFDF51)
        settings.style.buttonBarItemFont.withSize(15)
        settings.style.selectedBarHeight = 2.0
        settings.style.buttonBarMinimumLineSpacing = 0
        settings.style.buttonBarItemTitleColor = .white //.black
        settings.style.buttonBarItemsShouldFillAvailiableWidth = true
        settings.style.buttonBarLeftContentInset = 0
        settings.style.buttonBarRightContentInset = 0
        changeCurrentIndexProgressive = { [weak self] (oldCell: ButtonBarViewCell?, newCell: ButtonBarViewCell?, progressPercentage: CGFloat, changeCurrentIndex: Bool, animated: Bool) -> Void in
            guard changeCurrentIndex == true else { return }
            oldCell?.label.textColor = .black
            newCell?.label.textColor = self?.purpleInspireColor
        }
    }
    
    @IBAction func goBack(){
        self.dismiss(animated: true, completion: nil)
    }
    
    override func viewControllers(for pagerTabStripController: PagerTabStripViewController) -> [UIViewController] {
        
        let shopProfileViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "shopProfileViewController") as! ShopProfileViewController
        shopProfileViewController.storeId = shopId

        let reviewsViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "reviewsViewController")
        
        let aboutViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "aboutViewController")

        let policiesViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "policiesViewController")

        let moreViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "moreViewController")
        
        return [shopProfileViewController, reviewsViewController, aboutViewController, policiesViewController, moreViewController]
    }
    
    static var store: ShopController.Shop!
    
    static func getStoreProfile(shopId: String, vc: UIViewController?){
        let urlString = Config.HBUrl + "shops/single?id=\(shopId)"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
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
                            vc?.present(popup, animated: true, completion: nil)
                        }
                    } else {
                        if let data = data {
                            _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                            ShopProfileRouterViewController.store = try JSONDecoder().decode(ShopController.Shop.self, from: data)
                            print(self.store)
                        }
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
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
