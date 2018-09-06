//
//  RegisterViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/3/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import SwiftKeychainWrapper
import GoogleSignIn
import FBSDKCoreKit
import FBSDKLoginKit

class RegisterViewController: UIViewController, UITextFieldDelegate, GIDSignInUIDelegate, GIDSignInDelegate  {

    
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextField: UITextField!
    @IBOutlet weak var publicNameDisclaimerLabel: UILabel!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    @IBOutlet weak var usernameTextField: UITextField!
    
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var termsButton: UIButton!
    @IBOutlet weak var privacyPolicyButton: UIButton!
    
    @IBOutlet weak var facebookButton: UIButton!
    @IBOutlet weak var googleButton: UIButton!
    
    @IBOutlet weak var signInButton: UIButton!
    
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
    
    func loadUserView(){
        
    }
    
    @IBAction func RegisterButtonTapped(_ sender: Any) {
        
        let firstName = firstNameTextField.text!
        let lastName = lastNameTextField.text!
        let email = emailTextField.text!
        let password = passwordTextField.text!
        let passwordConfirmation = confirmPasswordTextField.text!
        let userName = usernameTextField.text!
        
        APIController.registerUser(
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            passwordConfirmation: passwordConfirmation,
            userName: userName,
            caller: self
        )
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        // Configure register button border
        registerButton.layer.cornerRadius = 20
        registerButton.clipsToBounds = true
        
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
        firstNameTextField.underlined()
        lastNameTextField.underlined()
        emailTextField.underlined()
        passwordTextField.underlined()
        confirmPasswordTextField.underlined()
        usernameTextField.underlined()
        
        // Set textfield delegates
        firstNameTextField.delegate = self
        lastNameTextField.delegate = self
        emailTextField.delegate = self
        passwordTextField.delegate = self
        confirmPasswordTextField.delegate = self
        usernameTextField.delegate = self

        // Dismiss keyboard on tap
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(RegisterViewController.dismissKeyboard)))
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    @IBAction func DismissRegisterView(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @objc func dismissKeyboard() {
        firstNameTextField.resignFirstResponder()
        lastNameTextField.resignFirstResponder()
        emailTextField.resignFirstResponder()
        passwordTextField.resignFirstResponder()
        confirmPasswordTextField.resignFirstResponder()
        usernameTextField.resignFirstResponder()
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
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}



extension UITextField {
    func underlined(){
        let border = CALayer()
        let width = CGFloat(1.0)
        border.borderColor = UIColor.lightGray.cgColor
        print("\(self.frame.size.height - width)")
        border.frame = CGRect(x: 0, y: ((self.frame.size.height - width)+0.5), width:  self.frame.size.width, height: self.frame.size.height)
        border.borderWidth = width
        self.layer.addSublayer(border)
        self.layer.masksToBounds = true
    }
}
