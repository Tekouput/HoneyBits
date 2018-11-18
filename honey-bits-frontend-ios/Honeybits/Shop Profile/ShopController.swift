//
//  ShopController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 6/24/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ShopController: UIViewController {

    struct Shop: Codable {
        var id: Int
        var name: String?
        var description: String?
        var shop_picture: Image
        var shop_logo: Image
        var map_location: MapLocation
        var policy: String?
        var raiting: Float? = 0
        var is_favorite: Bool
        var sales_count: Int? = 0
    }
    
    struct Image: Codable {
        var big: URL
        var medium: URL
        var thumb: URL
    }
    
    struct MapLocation: Codable {
        var place_id: String?
        var latitude: Float? = 0
        var longitude: Float? = 0
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        checkRole()
    }
    
    func checkRole(){
        APIController.activityIndicator(title: "Refreshing", view: self.view)
        
        let urlString = Config.HBUrl + "users/role"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            
            if let response = response as? HTTPURLResponse {
                if response.statusCode == 200 {
                    do {
                        APIController.removeActivityIndicator()
                        let json = try JSONSerialization.jsonObject(with: data!, options: []) as! [String: AnyObject]
                        let role = (json["role"] as? String ?? "none")!
                        
                        switch role {
                        case "none":
                            let storyboard = UIStoryboard(name: "You", bundle: nil)
                            let mainTab = storyboard.instantiateViewController(withIdentifier: "noSeller") as! ProfileViewController
                            self.present(mainTab, animated: true, completion: nil)
                            break
                        default:
                            break
                        }
                    } catch {
                        print(error)
                    }
                    
                } else if (response.statusCode == 401) {
                    OperationQueue.main.addOperation {
                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                        let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                        popup.modalPresentationStyle = .overCurrentContext
                        self.present(popup, animated: true, completion: nil)
                    }
                }
            }
            }.resume()
    }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
