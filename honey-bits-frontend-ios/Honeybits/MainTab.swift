//
//  MainTab.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 6/18/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class MainTab: UITabBarController, UITabBarControllerDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.delegate = self
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        if viewController is YouViewController && !APIController.valid {
            let viewController :UIViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "LogInView") as UIViewController
            self.present(viewController, animated: true, completion: nil)
        } else {
            APIController.refreshKey(caller: self)
        }
        
    }
    
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        print("From logger")
        if sender is LoginViewController {
            print("From logger")
        }
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }

}
