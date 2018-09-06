//
//  ManagerViewController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class ManagerViewController: ButtonBarPagerTabStripViewController {

    let purpleInspireColor = UIColor(red:0.13, green:0.03, blue:0.25, alpha:1.0)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadDesign()
    }
    
    var shopId: String!

    
    func loadDesign(){
        self.settings.style.selectedBarHeight = 1
        self.settings.style.selectedBarBackgroundColor = UIColor.white
        self.settings.style.buttonBarBackgroundColor = UIColor.white
        self.settings.style.buttonBarItemBackgroundColor = UIColor.white
        self.settings.style.selectedBarBackgroundColor = UIColor.white
        self.settings.style.buttonBarItemFont = .boldSystemFont(ofSize: 13)
        self.settings.style.buttonBarMinimumLineSpacing = 0
        self.settings.style.buttonBarItemTitleColor = .white
        self.settings.style.buttonBarItemsShouldFillAvailiableWidth = true
        self.settings.style.buttonBarLeftContentInset = 10
        self.settings.style.buttonBarRightContentInset = 10
        self.buttonBarView.backgroundColor = UIColor.white
        changeCurrentIndexProgressive = { (oldCell: ButtonBarViewCell?, newCell: ButtonBarViewCell?, progressPercentage: CGFloat, changeCurrentIndex: Bool, animated: Bool) -> Void in
            guard changeCurrentIndex == true else { return }
            oldCell?.label.textColor = .black
            newCell?.label.textColor = .black
        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewControllers(for pagerTabStripController: PagerTabStripViewController) -> [UIViewController] {
        
        let products = UIStoryboard(name: "ShopOwner", bundle: nil).instantiateViewController(withIdentifier: "ProductsManager") as! ProductsManager
        products.shopId = shopId
        
        let about = UIStoryboard(name: "ShopOwner", bundle: nil).instantiateViewController(withIdentifier: "AboutManager")
        let policies = UIStoryboard(name: "ShopOwner", bundle: nil).instantiateViewController(withIdentifier: "PoliciesManager")
        let faq = UIStoryboard(name: "ShopOwner", bundle: nil).instantiateViewController(withIdentifier: "FAQManager")
        let stats = UIStoryboard(name: "ShopOwner", bundle: nil).instantiateViewController(withIdentifier: "StatsManager")
        
        
        return [products, about, policies, faq, stats]
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
