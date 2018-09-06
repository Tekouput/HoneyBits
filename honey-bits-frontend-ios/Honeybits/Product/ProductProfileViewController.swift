//
//  ProductProfileViewController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/18/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import ImageSlideshow
import GooglePlaces
import GooglePlacePicker
import Alamofire

class ProductProfileViewController: UIViewController {

    @IBOutlet weak var storeLogo: UIImageView!
    @IBOutlet weak var storeName: UILabel!
    @IBOutlet weak var imageSlider: ImageSlideshow!
    @IBOutlet weak var productTitle: UILabel!
    @IBOutlet weak var productPrice: UILabel!
    @IBOutlet weak var productQuantity: UILabel!
    @IBOutlet weak var addToCart: UIButton!
    @IBOutlet weak var productDescription: UITextView!
    
    var productView: String!
    var productId: String!
    
    @IBAction func addToCart(_ sender: Any) {
        let urlString = "\(Config.HBUrl)carts/product?id=\(productId!)"
        print(urlString)
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            if let response = response as? HTTPURLResponse {
                if(response.statusCode == 200){
                    let storyboard = UIStoryboard(name: "Main", bundle: nil)
                    let mainTab = storyboard.instantiateViewController(withIdentifier: "MainTab") as! MainTab
                    mainTab.selectedIndex = 3
                    self.present(mainTab, animated: true, completion: nil)
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

    override func viewDidLoad() {
        super.viewDidLoad()
        imageSlider.contentScaleMode = .scaleAspectFill
        imageSlider.zoomEnabled = true
        imageSlider.clipsToBounds = true
        
        loadInfo()
        

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func goBack(_ sender: Any){
        self.dismiss(animated: true, completion: nil)
    }
    
    func loadInfo(){
        let urlString = "\(Config.HBUrl)products?id=\(productId!)"
        print(urlString)
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            do {
                if let response = response as? HTTPURLResponse {
                    if (response.statusCode == 401) {
                        OperationQueue.main.addOperation {
                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                            let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                            popup.modalPresentationStyle = .overCurrentContext
                            self.present(popup, animated: true, completion: nil)
                        }
                    } else {
                        if let data = data {
                            _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                            let product = try JSONDecoder().decode(Product.self, from: data)
                            
                            APIController.removeActivityIndicator()
                            OperationQueue.main.addOperation {
                                self.storeLogo.downloadedFrom(link: "\(Config.HBUrl)\(product.shop.shop_logo.thumb)")
                                self.storeLogo.layer.cornerRadius = 15
                                let shadowSize : CGFloat = 5.0
                                let shadowPath = UIBezierPath(rect: CGRect(x: -shadowSize / 2,
                                                                           y: -shadowSize / 2,
                                                                           width: self.storeLogo.frame.size.width + shadowSize,
                                                                           height: self.storeLogo.frame.size.height + shadowSize))
                                self.storeLogo.layer.masksToBounds = false
                                self.storeLogo.layer.shadowColor = UIColor.black.cgColor
                                self.storeLogo.layer.shadowOffset = CGSize(width: 0.0, height: 0.0)
                                self.storeLogo.layer.shadowOpacity = 0.5
                                self.storeLogo.layer.shadowPath = shadowPath.cgPath
                                self.storeLogo.clipsToBounds = true
                                
                                self.storeName.text = product.shop.name
                                
                                var uiImageArray: [InputSource] = []
                                for image in product.pictures! {
                                    uiImageArray.append(AlamofireSource(urlString: "\(Config.HBUrl)\(image.urls.big.absoluteString)")!)
                                    print("\(Config.HBUrl)\(image.urls.big.absoluteString)")
                                }
                                
                                self.imageSlider.setImageInputs(uiImageArray)
                                self.productTitle.text = product.name
                                self.productPrice.text = product.price.formatted
                                self.productDescription.text = product.description
                                self.productQuantity.text = "Only \(150) available"
                                
                            }
                        }
                    }
                }
            } catch {
                print(error)
            }
            }.resume()
    }
    
    
    func getNeededPart(type: String, components: [GMSAddressComponent]) -> String {
        for i in 0..<components.count {
            if(components[i].type == type){
                return getStateCode(state: components[i].name)
            }
        }
        return ""
    }
    
    func getStateCode(state: String) -> String {
        let state = state.lowercased()
        switch state {
        case "alabama": return "AL"
        case "alaska": return "AK"
        case "arizona": return "AZ"
        case "arkansas": return "AR"
        case "california": return "CA"
        case "colorado": return "CO"
        case "connecticut": return "CT"
        case "delaware": return "DE"
        case "district of columbia": return "DC"
        case "florida": return "FL"
        case "georgia": return "GA"
        case "hawaii": return "HI"
        case "idaho": return "ID"
        case "illinois": return "IL"
        case "indiana": return "IN"
        case "iowa": return "IA"
        case "kansas": return "KS"
        case "kentucky": return "KY"
        case "louisiana": return "LA"
        case "maine": return "ME"
        case "maryland": return "MD"
        case "massachusetts": return "MA"
        case "michigan": return "MI"
        case "minnesota": return "MN"
        case "mississippi": return "MS"
        case "missouri": return "MO"
        case "montana": return "MT"
        case "nebraska": return "NE"
        case "nevada": return "NV"
        case "new hampshire": return "NH"
        case "new jersey": return "NJ"
        case "new mexico": return "NM"
        case "new york": return "NY"
        case "north carolina": return "NC"
        case "north dakota": return "ND"
        case "ohio": return "OH"
        case "oklahoma": return "OK"
        case "oregon": return "OR"
        case "pennsylvania": return "PA"
        case "rhode island": return "RI"
        case "south carolina": return "SC"
        case "south dakota": return "SD"
        case "tennessee": return "TN"
        case "texas": return "TX"
        case "utah": return "UT"
        case "vermont": return "VT"
        case "virginia": return "VA"
        case "washington": return "WA"
        case "west virginia": return "WV"
        case "wisconsin": return "WI"
        case "wyoming": return "WY"
        default: return state.capitalized
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.destination is MainTab) {
            
        }
    }

}
