//
//  HomeViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 4/24/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip
import SwiftKeychainWrapper

class HomeViewController: ButtonBarPagerTabStripViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        // change selected bar color
        
        settings.style.buttonBarBackgroundColor = .white
        settings.style.buttonBarItemBackgroundColor = .white
        settings.style.selectedBarBackgroundColor = UIColor(rgb: 0xED9B40)
        buttonBarView.selectedBar.backgroundColor = UIColor(rgb: 0xF3652B)
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
            newCell?.label.textColor = UIColor(rgb: 0xED9B40)
        }
    }
    
    
    override func viewControllers(for pagerTabStripController: PagerTabStripViewController) -> [UIViewController] {
        let favorites = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "honeybitsFavoritesViewController")
        let feedView = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "initialFeedViewController")
        APIController.refreshKey(caller: self)
        
        
        return [feedView, favorites]
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}


extension UIColor {
    convenience init(red: Int, green: Int, blue: Int) {
        assert(red >= 0 && red <= 255, "Invalid red component")
        assert(green >= 0 && green <= 255, "Invalid green component")
        assert(blue >= 0 && blue <= 255, "Invalid blue component")
        
        self.init(red: CGFloat(red) / 255.0, green: CGFloat(green) / 255.0, blue: CGFloat(blue) / 255.0, alpha: 1.0)
    }
    
    convenience init(rgb: Int) {
        self.init(
            red: (rgb >> 16) & 0xFF,
            green: (rgb >> 8) & 0xFF,
            blue: rgb & 0xFF
        )
    }
}

