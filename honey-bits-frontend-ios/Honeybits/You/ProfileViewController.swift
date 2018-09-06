//
//  ProfiileViewController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 6/23/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func setRole(_ sender: UIButton) {
        switch sender.restorationIdentifier {
        case "beekeeper":
            APIController.setRole(caller: self, role_id: "1")
            break
        case "distributer":
            APIController.setRole(caller: self, role_id: "2")
            break
        case .some(_):
            
            break
        case .none:
            
            break
        }
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
