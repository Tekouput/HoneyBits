//
//  SearchTableViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 4/26/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import Alamofire
import GooglePlaces
import GooglePlacePicker

class SearchTableViewController: UITableViewController, UISearchResultsUpdating {
    
    private let searchController = UISearchController(searchResultsController: nil)
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("row: \(indexPath.row)")
        
        let listing = listings[indexPath.row]
        if (listing.type == "Product"){
            
            let storyBoard: UIStoryboard = UIStoryboard(name: "Product", bundle: nil)
            let newViewController = storyBoard.instantiateViewController(withIdentifier: "ProductProfileView") as! ProductProfileViewController
            newViewController.productId = listing.id
            self.present(newViewController, animated: true, completion: nil)
        } else {
            let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
            let newViewController = storyBoard.instantiateViewController(withIdentifier: "ShopProfileViews") as! ShopProfileRouterViewController
            newViewController.shopId = listing.id
            newViewController.viewController = self
            self.present(newViewController, animated: true, completion: nil)
        }
    }
    
    struct Listing {
        var image: String
        var title: String
        var type: String
        var id: String
        var shop: ShopListing?
        var product: Product?
    }
    
    struct ShopListing: Codable {
        var id: Int
        var name: String
        var map_location: ShopController.MapLocation
        var logo_thumb: String
        var products_picture: [String]? = [""]
    }
    
    var listings: [Listing]! = []

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        searchController.searchBar.searchBarStyle = .minimal
        searchController.searchBar.backgroundColor = UIColor.white
        self.tableView.tableHeaderView = searchController.searchBar
        configureSearchController()
        searchController.delegate = self as? UISearchControllerDelegate
        searchController.searchResultsUpdater = self
        searchController.obscuresBackgroundDuringPresentation = true
        navigationItem.searchController = searchController
        
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        let imageView = UIImageView(image: UIImage(named: "honey4")!)
        imageView.contentMode = .scaleAspectFill
        imageView.contentMode = .center
        
        self.tableView.backgroundView = imageView

    }
    
    private func configureSearchController() {
        
        searchController.searchBar.tintColor = UIColor(rgb: 0xF3652B)
        searchController.searchBar.scopeButtonTitles = ["Products", "Nearby"]
        searchController.searchBar.barTintColor = UIColor.white
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        terminate = true
        activated = false
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return listings.count
    }

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return sectionId == 0 ? 100.0 : 170
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (sectionId == 0){
            let cell = tableView.dequeueReusableCell(withIdentifier: "ProductCellSearch", for: indexPath) as! ProductTableViewCell
            cell.productName.text = listings[indexPath.row].title
            cell.productPrice.text = listings[indexPath.row].product?.price.formatted
            cell.productStore.text = listings[indexPath.row].product?.shop.name
            cell.productImage.downloadedFrom(link: listings[indexPath.row].image).layer.cornerRadius = 15.0
            return cell
        }else {
            
            let shopListing = listings[indexPath.row]
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "ShopCellSearch", for: indexPath) as! ShopTableViewCell
            cell.storeName.text = shopListing.title
            cell.storeLogo.downloadedFrom(link: shopListing.image).makeRounded()
            
            var strings: [String] = shopListing.shop!.products_picture!
            let limitCount: Int = strings.count
            var images: [String] = ["/images/honey.jpg", "/images/honey.jpg", "/images/honey.jpg"]
            for i in stride(from: 0, to: limitCount, by: 1) {
                images[i] = strings[i]
            }
            
            _ = cell.image1.downloadedFrom(link: "\(Config.HBUrl)\(images[0])")
            _ = cell.image2.downloadedFrom(link: "\(Config.HBUrl)\(images[1])")
            _ = cell.image3.downloadedFrom(link: "\(Config.HBUrl)\(images[2])")
            
            let testclient: GMSPlacesClient = GMSPlacesClient()
            testclient.lookUpPlaceID((shopListing.shop?.map_location.place_id!)!, callback: {(place, error) -> Void in
                if let error = error {
                    print("Error getting info from given placeID!!:: \(error.localizedDescription)")
                    return
                }
                
                guard let place = place else {
                    print("No place details for given placeID")
                    return
                }
                
                let components = place.addressComponents
                cell.storeLocation.text = "\(self.getNeededPart(type: "administrative_area_level_1", components: components!)), \(self.getNeededPart(type: "administrative_area_level_2", components: components!))"
            })
            
            return cell
        }
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
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */
    
    var workItem: DispatchWorkItem?
    var activated: Bool = false
    var searchString: String!
    var onSearch: String!
    var terminate: Bool = false
    var sectionId: Int!
    
    // MARK: - Search methods
    func updateSearchResults(for searchController: UISearchController) {
        sectionId = searchController.searchBar.selectedScopeButtonIndex
        searchString = searchController.searchBar.text
        startSearch()
        
    }
    
    func startSearch(){
        if (!activated){
            workItem = DispatchWorkItem {
                self.activated = true
                sleep(3)
                if (self.onSearch != self.searchString && self.searchString.count > 0){
                    self.onSearch = self.searchString
                    self.queryText()
                }
                
                if(self.terminate == false){
                    DispatchQueue.global().async(execute: self.workItem!)
                }
            }
            DispatchQueue.global().async(execute: self.workItem!)
        }
    }
    
    var iProducts = 0
    var iShops = 0
    
    func queryText(){
        
        let urlString = sectionId == 0 ? "\(Config.HBUrl)products/products_by_name?name=\(searchString!)&offset=\(20*iProducts)" : "\(Config.HBUrl)shops/products/show_listings?name=\(searchString!)&offset=\(20*iShops)"
        print(urlString)
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            do {
                if let data = data {
                    
                    print("\(self.sectionId) ############")
                    
                    self.listings = []
                    if (self.sectionId == 0) {
                        print("Product")
                        let elements: [Product] = try JSONDecoder().decode([Product].self, from: data)
                        for element in elements {
                            if ((element.pictures?.count)! > 0){
                                self.listings.append(Listing(image: "\(Config.HBUrl)\(element.pictures![0].urls.thumb.absoluteURL)", title: element.name, type: "Product", id: String(element.id), shop: nil, product: element))
                            } else {
                                self.listings.append(Listing(image: "\(Config.HBUrl)images/honey.jpg", title: element.name, type: "Product", id: String(element.id), shop: nil, product: element))
                            }
                        }
                        OperationQueue.main.addOperation {
                            self.tableView.reloadData()
                        }
                    } else {
                        let elements: [ShopListing] = try JSONDecoder().decode([ShopListing].self, from: data)
                        for element in elements {
                            let listing = Listing(image: "\(Config.HBUrl)\(element.logo_thumb)", title: element.name, type: "Shop", id: String(element.id), shop: element, product: nil)
                            self.listings.append(listing)
                        }
                        OperationQueue.main.addOperation {
                            self.tableView.reloadData()
                        }
                    }
                }
            } catch {
                print(error)
            }
        }.resume()
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
