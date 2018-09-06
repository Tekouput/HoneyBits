//
//  GetStartedViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 4/24/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class GetStartedViewController: UIViewController, IndicatorInfoProvider {

    @IBOutlet weak var getStartedButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        getStartedButton.layer.cornerRadius = 20
        getStartedButton.clipsToBounds = true
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func indicatorInfo(for pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return IndicatorInfo(title: "Your feed")
    }
    
    @IBAction func GetStarted(_ sender: Any) {
        
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
