//
//  APIController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 6/18/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation
import UIKit
import SwiftKeychainWrapper

struct Media {
    let key: String
    let filename: String
    let data: Data
    let mimeType: String
    
    init?(withImage image: UIImage, forKey key: String) {
        self.key = key
        self.mimeType = "image/jpeg"
        self.filename = "kyleleeheadiconimage234567.jpg"
        
        guard let data = UIImageJPEGRepresentation(image, 0.7) else { return nil }
        self.data = data
    }
    
}

typealias Parameters = [String: String]

class APIController {
    
    static var API_KEY = ""
    static var valid = false
    static var role = "none"
    
    static func invalidateUser(caller: UIViewController){
        KeychainWrapper.standard.removeObject(forKey: "authToken")
        
        //TODO: Invalidate in server also
        
        API_KEY = ""
        valid = false
        
        OperationQueue.main.addOperation {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let mainTab = storyboard.instantiateViewController(withIdentifier: "MainTab") as! MainTab
            caller.present(mainTab, animated: true, completion: nil)
        }
    }
    
    static func setRole (caller: UIViewController, role_id: String) {
        
        let urlString = Config.HBUrl + "users/role"
        let url = URL(string: urlString)
        var request = URLRequest(url: url!)
        request.httpMethod = "PATCH"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let parameters = ["role_id":"\(role_id)"]
        guard let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: []) else {return}
        request.httpBody = httpBody
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            
            OperationQueue.main.addOperation {
                let storyboard = UIStoryboard(name: "ShopOwner", bundle: nil)
                let mainTab = storyboard.instantiateViewController(withIdentifier: "MainController")
                caller.present(mainTab, animated: true, completion: nil)
            }
        }.resume()
    }
    
    static func refreshKey(caller: UIViewController){
        let key: String? = KeychainWrapper.standard.string(forKey: "authToken")
        APIController.API_KEY = key ?? ""
        let urlString = Config.HBUrl + "validity"
        
        let url = URL(string: urlString)
        var request = URLRequest(url: url!)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            _ = response as? HTTPURLResponse
            
            if let data = data {
                do{
                    let p_valid = valid
                    let jsonTest = try JSONSerialization.jsonObject(with: data, options: []) as? [String:Any]
                    if (jsonTest!["valid"] as! Int) == 1 {
                        valid = true
                    }
                    else {
                        valid = false
                    }
                    if valid != p_valid {
                        OperationQueue.main.addOperation {
                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                            let mainTab = storyboard.instantiateViewController(withIdentifier: "MainTab") as! MainTab
                            caller.present(mainTab, animated: true, completion: nil)
                        }
                    }
                } catch {
                    print(error)
                }
            }
            }.resume()
    }
    
    
    //Label items
    static var strLabel = UILabel()
    static var activityIndicator = UIActivityIndicatorView()
    static let effectView = UIVisualEffectView(effect: UIBlurEffect(style: .dark))
    
    static func removeActivityIndicator(){
        DispatchQueue.main.async {
            strLabel.removeFromSuperview()
            activityIndicator.removeFromSuperview()
            effectView.removeFromSuperview()
        }
    }
    
    //Indicvates the actual proccess being done by the app.
    static func activityIndicator(title: String, view: UIView) {
        DispatchQueue.main.async {
            strLabel.removeFromSuperview()
            activityIndicator.removeFromSuperview()
            effectView.removeFromSuperview()
            
            strLabel = UILabel(frame: CGRect(x: 50, y: 0, width: 160, height: 46))
            strLabel.text = title
            strLabel.font = UIFont.systemFont(ofSize: 14, weight: UIFont.Weight.medium)
            strLabel.textColor = UIColor(white: 0.9, alpha: 0.7)
            
            effectView.frame = CGRect(x: view.frame.midX - strLabel.frame.width/2, y: view.frame.midY - strLabel.frame.height/2 , width: 160, height: 46)
            effectView.layer.cornerRadius = 15
            effectView.layer.masksToBounds = true
            
            activityIndicator = UIActivityIndicatorView(activityIndicatorStyle: .white)
            activityIndicator.frame = CGRect(x: 0, y: 0, width: 46, height: 46)
            activityIndicator.startAnimating()
            
            effectView.contentView.addSubview(activityIndicator)
            effectView.contentView.addSubview(strLabel)
            view.addSubview(effectView)
        }
    }
    
    static func registerUser(firstName: String, lastName: String, email: String, password: String, passwordConfirmation: String, userName: String, caller: UIViewController){
        
        APIController.activityIndicator(title: "Creating user", view: caller.view)
        
        let parameters = ["email":"\(email)","password":"\(password)","password_confirmation":"\(passwordConfirmation)","first_name":"\(firstName)","last_name":"\(lastName)","username":"\(userName)"]
        
        let urlString = Config.HBUrl + "users"
        
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        
        guard let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: []) else {return}
        
        request.httpBody = httpBody
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            
            if let response = response {
                print(response)
            }
            
            if let data = data {
                do{
                    _ = try JSONSerialization.jsonObject(with: data, options: [])
                    self.loginUser(email: email, password: password, caller: caller)
                    
                } catch {
                    print(error)
                }
            }
            }.resume()
    }
    
    
    static func updateStore(id: String, name: String?, description: String?, latitude: String, longitude: String, shopImage: UIImage?, shopLogo: UIImage?, caller: UIViewController, placeId: String){
        
        APIController.activityIndicator(title: "Updating shop!", view: caller.view)
        
        let link = Config.HBUrl + "shops/single"
        let url = URLComponents(string: link)!
        
        var request = URLRequest(url: url.url!)
        request.httpMethod = "PATCH"
        
        
        let boundary = generateBoundary()
        
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let parameters = [
            "id": "\(id)",
            "title": "\(name ?? "")",
            "description": "\(description ?? "")",
            "latitude": "\(latitude)",
            "longitude": "\(longitude)",
            "place_id": "\(placeId)"
        ]
        
        var dataBody = createDataBody(withParameters: parameters, media: nil, boundary: boundary)
        
        if (shopImage != nil && shopLogo != nil) {
            let mediaImage = Media(withImage: (shopImage)!, forKey: "shop_picture")
            let mediaLogo = Media(withImage: (shopLogo)!, forKey: "shop_logo")
            dataBody = createDataBody(withParameters: parameters, media: [mediaImage!, mediaLogo!], boundary: boundary)
        } else if (shopImage != nil) {
            let mediaImage = Media(withImage: (shopImage)!, forKey: "shop_picture")
            dataBody = createDataBody(withParameters: parameters, media: [mediaImage!], boundary: boundary)
        } else if (shopLogo != nil) {
            let mediaLogo = Media(withImage: (shopLogo)!, forKey: "shop_logo")
            dataBody = createDataBody(withParameters: parameters, media: [mediaLogo!], boundary: boundary)
        }
        
        request.httpBody = dataBody as Data
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            if let response = response {
                print(response)
            }
            
            if let data = data {
                do {
                    let json = try JSONSerialization.jsonObject(with: data, options: [])
                    print(json)
                    
                    OperationQueue.main.addOperation {
                        let storyboard = UIStoryboard(name: "ShopOwner", bundle: nil)
                        let shopsView = storyboard.instantiateViewController(withIdentifier: "MainController") as! UINavigationController
                        caller.present(shopsView, animated: true, completion: nil)
                    }
                    
                } catch {
                    print(error)
                }
            }
        }.resume()
    }
    
    static func createStore(name: String, description: String, latitude: String, longitude: String, shopImage: UIImage?, caller: UIViewController, shopLogo: UIImage?, placeId: String){
        
        APIController.activityIndicator(title: "Creating shop!", view: caller.view)
        
        let link = Config.HBUrl + "shops"
        let url = URLComponents(string: link)!
        
        var request = URLRequest(url: url.url!)
        request.httpMethod = "POST"
        
        
        let boundary = generateBoundary()
        
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let parameters = [
            "title": "\(name)",
            "description": "\(description)",
            "latitude": "\(latitude)",
            "longitude": "\(longitude)",
            "place_id": "\(placeId)"
        ]
        
        var dataBody = createDataBody(withParameters: parameters, media: nil, boundary: boundary)
        
        if (shopImage != nil && shopLogo != nil) {
            let mediaImage = Media(withImage: (shopImage)!, forKey: "shop_picture")
            let mediaLogo = Media(withImage: (shopLogo)!, forKey: "shop_logo")
            dataBody = createDataBody(withParameters: parameters, media: [mediaImage!, mediaLogo!], boundary: boundary)
        } else if (shopImage != nil) {
            let mediaImage = Media(withImage: (shopImage)!, forKey: "shop_picture")
            dataBody = createDataBody(withParameters: parameters, media: [mediaImage!], boundary: boundary)
        } else if (shopLogo != nil) {
            let mediaLogo = Media(withImage: (shopLogo)!, forKey: "shop_logo")
            dataBody = createDataBody(withParameters: parameters, media: [mediaLogo!], boundary: boundary)
        }
        
        request.httpBody = dataBody as Data
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            if let response = response {
                print(response)
            }
            
            if let data = data {
                do {
                    let json = try JSONSerialization.jsonObject(with: data, options: [])
                    print(json)
                    
                    OperationQueue.main.addOperation {
                        let storyboard = UIStoryboard(name: "ShopOwner", bundle: nil)
                        let shopsView = storyboard.instantiateViewController(withIdentifier: "MainController") as! UINavigationController
                        caller.present(shopsView, animated: true, completion: nil)
                    }
                    
                } catch {
                    print(error)
                }
            }
        }.resume()
    }
    
    static func generateBoundary() -> String {
        return "----WEBKitBoundary\(NSUUID().uuidString)"
    }
    
    static func createDataBody(withParameters params: Parameters?, media: [Media]?, boundary: String) -> NSData {
        
        let lineBreak = "\r\n"
        let body = NSMutableData()
        
        if let parameters = params {
            for (key, value) in parameters {
                body.appendString(string: "--\(boundary + lineBreak)")
                body.appendString(string: "Content-Disposition: form-data; name=\"\(key)\"\(lineBreak + lineBreak)")
                body.appendString(string: "\(value + lineBreak)")
            }
        }
        
        if let media = media {
            for photo in media {
                body.appendString(string: "--\(boundary + lineBreak)")
                body.appendString(string: "Content-Disposition: form-data; name=\"\(photo.key)\"; filename=\"\(photo.filename)\"\(lineBreak)")
                body.appendString(string: "Content-Type: \(photo.mimeType + lineBreak + lineBreak)")
                body.append(photo.data)
                body.appendString(string: lineBreak)
            }
        }
        body.appendString(string: "--\(boundary)--\(lineBreak)")
        return body
    }
    
    static func loginAuth(token: String, provider: String, caller: UIViewController) {
        
        self.activityIndicator(title: "Login user", view: caller.view)
        
        let urlString = Config.HBUrl + "omniauth/\(provider)"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(token, forHTTPHeaderField: "Authorization")
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            
            if let response = response as? HTTPURLResponse {
                print(response)
                switch response.statusCode {
                case 200:
                    if let data = data {
                        
                        do{
                            let json = try JSONSerialization.jsonObject(with: data, options: []) as! [String: AnyObject]
                            if let key = json["auth_token"] as? String{
                                _ = KeychainWrapper.standard.set(key, forKey: "authToken")
                                API_KEY = key
                                valid = true
                                role = (json["user"]!["role"] as? String ?? "none")!
                                print(role)
                                
                                OperationQueue.main.addOperation {
                                    let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                    let mainTab = storyboard.instantiateViewController(withIdentifier: "MainTab") as! MainTab
                                    caller.present(mainTab, animated: true, completion: nil)
                                }
                                
                                defer{
                                    //self.updateUser()
                                }
                            }
                            print(json)
                        } catch {
                            print(error)
                        }
                    }
                    break
                case 401:
                    OperationQueue.main.addOperation {
                        let alert = UIAlertController(title: "Wrong credentials", message: "Verify your email or password", preferredStyle: UIAlertControllerStyle.alert)
                        alert.addAction(UIAlertAction(title: "Dimiss", style: UIAlertActionStyle.default, handler: nil))
                        caller.present(alert, animated: true, completion: nil)
                    }
                    break
                default:
                    self.activityIndicator(title: "Something happened, try again later", view: caller.view)
                }
                
                self.removeActivityIndicator()
            }
            
            
            }.resume()
        
    }
    
    
    static func loginUser(email: String, password: String, caller: UIViewController){
        
        self.activityIndicator(title: "Login user", view: caller.view)
        
        let parameters = ["email":"\(email)","password":"\(password)"]
        
        let urlString = Config.HBUrl + "users/token"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        
        guard let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: []) else {return}
        
        request.httpBody = httpBody
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            
            if let response = response as? HTTPURLResponse {
                switch response.statusCode {
                case 200:
                    if let data = data {
                        
                        do{
                            let json = try JSONSerialization.jsonObject(with: data, options: []) as! [String: AnyObject]
                            if let key = json["auth_token"] as? String{
                                _ = KeychainWrapper.standard.set(key, forKey: "authToken")
                                print(key)
                                API_KEY = key
                                valid = true
                                
                                OperationQueue.main.addOperation {
                                    let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                    let mainTab = storyboard.instantiateViewController(withIdentifier: "MainTab") as! MainTab
                                    caller.present(mainTab, animated: true, completion: nil)
                                }
                                
                                defer{
                                    //self.updateUser()
                                }
                            }
                            print(json)
                        } catch {
                            print(error)
                        }
                    }
                    break
                case 401:
                    OperationQueue.main.addOperation {
                    let alert = UIAlertController(title: "Wrong credentials", message: "Verify your email or password", preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "Dimiss", style: UIAlertActionStyle.default, handler: nil))
                    caller.present(alert, animated: true, completion: nil)
                    }
                    break
                default:
                    self.activityIndicator(title: "Something happened, try again later", view: caller.view)
                }
                
                self.removeActivityIndicator()
            }
            
            
        }.resume()
        
        /*
         // Prepare user to save
         
         // Pick from
         DispatchQueue.main.async {
         let dateFormatter = DateFormatter()
         dateFormatter.dateFormat = "YYYY-MM-dd"
         //let date = dateFormatter.string(from: self.datePickerView.date)
         
         
         //self.user = JSONUser(firstName: self.nameTextField.text!, lastName: self.lastNameTextField.text!, birthDate: date, sex: self.userSex, zip: "", phoneNumber: "")
         }
         */
    }
}

extension NSMutableData {
    
    func appendString(string: String) {
        let data = string.data(using: String.Encoding.utf8, allowLossyConversion: true)
        append(data!)
    }
}
