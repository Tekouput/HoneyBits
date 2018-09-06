//
//  ShopProfileViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 6/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import XLPagerTabStrip
import GooglePlaces
import GooglePlacePicker

class ShopProfileViewController: UIViewController, IndicatorInfoProvider {

    @IBOutlet weak var shopImage: UIImageView!
    @IBOutlet weak var shopName: UILabel!
    @IBOutlet weak var shopLogo: UIImageView!
    @IBOutlet weak var shopAddress: UILabel!
    @IBOutlet weak var shopSales: UILabel!
    @IBOutlet weak var shopDescription: UITextView!
    @IBOutlet weak var collectionView: UICollectionView!
    
    var storeId: String!
    var store: ShopController.Shop!
    var productCollection: ProductCollection!
    var multiplier = 0

    override func viewDidLoad() {
        super.viewDidLoad()

        productCollection = ProductCollection()
        productCollection.vc = self
        collectionView.dataSource = productCollection
        collectionView.delegate = productCollection
        getStoreInfo()
        loadProducts()

    }
    
    func loadProducts(){
        let urlString = "\(Config.HBUrl)shops/latest_products?id=\(storeId!)&offset=\(4*multiplier)&limit=\(4)"
        print(urlString)
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
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
                            self.productCollection.products.append(contentsOf: try JSONDecoder().decode([Product].self, from: data))
                            APIController.removeActivityIndicator()
                            OperationQueue.main.addOperation {
                                self.collectionView.reloadData()
                            }
                        }
                    }
                }
            } catch {
                print(error)
            }
            }.resume()
    }
    
    @IBAction func loadMore(_ sender: Any) {
        multiplier += 1
        loadProducts()
    }
    
    func setInfo(){
        shopImage.downloadedFrom(link: Config.HBUrl + store.shop_logo.medium.absoluteString).makeRounded()
        shopName.text = store.name
        let testclient: GMSPlacesClient = GMSPlacesClient()
        testclient.lookUpPlaceID(store.map_location.place_id!, callback: {(place, error) -> Void in
            if let error = error {
                print("Error getting info from given placeID!!:: \(error.localizedDescription)")
                return
            }

            guard let place = place else {
                print("No place details for given placeID")
                return
            }

            let components = place.addressComponents

            self.shopAddress.text = "\(self.getNeededPart(type: "administrative_area_level_1", components: components!)), \(self.getNeededPart(type: "administrative_area_level_2", components: components!))"
        })
        
        shopSales.text = "\(store.sales_count ?? 0) Sales"
        shopDescription.text = store.description
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

    func getStoreInfo(){
        if ShopProfileRouterViewController.store == nil {
            let urlString = Config.HBUrl + "shops/single?id=\(storeId)"
            guard let url = URL(string: urlString) else {return}
            var request = URLRequest(url: url)
            request.httpMethod = "GET"
            request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")

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
                                ShopProfileRouterViewController.store = try JSONDecoder().decode(ShopController.Shop.self, from: data)
                                
                                self.setInfo()
                                print(self.store)
                            }
                        }
                    }
                } catch {
                    print(error)
                }
                }.resume()
        } else {
            store = ShopProfileRouterViewController.store
            setInfo()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func indicatorInfo(for pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return IndicatorInfo(title: "Shop")
    }

}
