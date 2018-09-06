//
//  YouSignedOutViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/4/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class YouSettingsViewController: UITableViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    @IBAction func signOut(_ sender: Any) {
        APIController.invalidateUser(caller: self)
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        self.hidesBottomBarWhenPushed = false
    }
    

}
