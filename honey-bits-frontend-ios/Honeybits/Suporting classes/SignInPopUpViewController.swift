//
//  SignInPopUpViewController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/26/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class SignInPopUpViewController: UIViewController {

    @IBOutlet weak var titleLabel: UILabel!
    
    @IBAction func googleButton(_ sender: Any) {
    }
    
    @IBAction func facebookButton(_ sender: Any) {
    }
    
    @IBAction func registerButton(_ sender: Any) {
    }
    
    @IBAction func signButton(_ sender: Any) {
    }
    
    @IBAction func quitPopUp(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
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
