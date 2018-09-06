//
//  LoginViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/3/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import GoogleSignIn
import FBSDKCoreKit
import FBSDKLoginKit

class LoginViewController: UIViewController, UITextFieldDelegate, GIDSignInUIDelegate, GIDSignInDelegate {

    
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    
    @IBOutlet weak var signInButton: UIButton!
    @IBOutlet weak var forgotPasswordButton: UIButton!
    
    @IBOutlet weak var facebookButton: UIButton!
    @IBOutlet weak var googleButton: UIButton!
    
    @IBOutlet weak var registerButton: UIButton!

    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        APIController.loginAuth(token: (user?.serverAuthCode)!, provider: "google", caller: self)
        GIDSignIn.sharedInstance().signOut()
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
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        // Configure register button border
        signInButton.layer.cornerRadius = 20
        signInButton.clipsToBounds = true
        
        // Configure facebook and google login image, border, and spacing
        
        facebookButton.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 140)
        googleButton.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 150)
        
        let buttonsCornerRadius = CGFloat(20)
        let buttonsBorderColor = UIColor.lightGray.cgColor
        let buttonsBorderWidth = CGFloat(1)
        
        facebookButton.layer.cornerRadius = buttonsCornerRadius
        facebookButton.clipsToBounds = true
        facebookButton.layer.borderWidth = buttonsBorderWidth
        facebookButton.layer.borderColor = buttonsBorderColor
        
        googleButton.layer.cornerRadius = buttonsCornerRadius
        googleButton.clipsToBounds = true
        googleButton.layer.borderWidth = buttonsBorderWidth
        googleButton.layer.borderColor = buttonsBorderColor
        
        // Draw underline on every textField
        emailTextField.underlined()
        passwordTextField.underlined()

        // Set textfield delegates
        emailTextField.delegate = self
        passwordTextField.delegate = self

        // Dismiss keyboard on tap
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(LoginViewController.dismissKeyboard)))
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func dismissKeyboard() {
        emailTextField.resignFirstResponder()
        passwordTextField.resignFirstResponder()
    }
    
    
    // MARK: - TextField delegate methods
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool
    {
        // Try to find next responder
        if let nextField = textField.superview?.viewWithTag(textField.tag + 1) as? UITextField {
            nextField.becomeFirstResponder()
        } else {
            // Not found, so remove keyboard.
            textField.resignFirstResponder()
        }
        // Do not add a line break
        return false
    }

    @IBAction func logUserIn(_ sender: Any) {
        let email = emailTextField.text!
        let password = passwordTextField.text!
        APIController.loginUser(email: email, password: password, caller: self)
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
