//
//  PickLoginViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 4/24/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import GoogleSignIn
import FBSDKCoreKit
import FBSDKLoginKit

class PickLoginViewController: UIViewController, GIDSignInUIDelegate, GIDSignInDelegate {
    
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if user?.serverAuthCode != nil {
            APIController.loginAuth(token: (user?.serverAuthCode)!, provider: "google", caller: self)
        }
        GIDSignIn.sharedInstance().signOut()
    }
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var facebookButton: UIButton!
    @IBOutlet weak var googleButton: UIButton!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var signInButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let buttonsCornerRadius = CGFloat(20)
        let buttonsBorderColor = UIColor.lightGray.cgColor
        let buttonsBorderWidth = CGFloat(1)
        
        containerView.layer.cornerRadius = 20
        containerView.clipsToBounds = true
        
        
        facebookButton.layer.cornerRadius = buttonsCornerRadius
        facebookButton.clipsToBounds = true
        facebookButton.layer.borderWidth = buttonsBorderWidth
        facebookButton.layer.borderColor = buttonsBorderColor
        facebookButton.imageEdgeInsets = UIEdgeInsetsMake(10, 10, 10, 140)

        googleButton.layer.cornerRadius = buttonsCornerRadius
        googleButton.clipsToBounds = true
        googleButton.layer.borderWidth = buttonsBorderWidth
        googleButton.layer.borderColor = buttonsBorderColor
        googleButton.imageEdgeInsets = UIEdgeInsetsMake(10, 10, 10, 150)

        registerButton.layer.cornerRadius = buttonsCornerRadius
        registerButton.clipsToBounds = true
        registerButton.layer.borderWidth = buttonsBorderWidth
        registerButton.layer.borderColor = buttonsBorderColor
        
        signInButton.layer.cornerRadius = buttonsCornerRadius
        signInButton.clipsToBounds = true
        signInButton.layer.borderWidth = buttonsBorderWidth
        signInButton.layer.borderColor = buttonsBorderColor
    }
    
    @IBAction func facebookButtonClicked(_ sender: Any) {
        let fbLoginManager : FBSDKLoginManager = FBSDKLoginManager()
        fbLoginManager.logIn(withReadPermissions: ["email", "public_profile"], from: self) { (result, error) -> Void in
            if (error == nil){
                let fbloginresult : FBSDKLoginManagerLoginResult = result!
                // if user cancel the login
                if (result?.isCancelled)!{
                    return
                }
                APIController.loginAuth(token: fbloginresult.token.tokenString, provider: "facebook", caller: self)
            }
        }
    }
    
    @IBAction func googleButtonClicked(_ sender: Any) {
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().signIn()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
