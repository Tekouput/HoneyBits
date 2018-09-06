//
//  StoreListController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 7/27/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class StoreListController: UITableViewController {
    
    var shops: [ShopController.Shop]!

    @IBOutlet var container: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        container.separatorStyle = .none
        getShopsInfo()
        // Do any additional setup after loading the view.
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = StoreItem(style: UITableViewCellStyle.default, reuseIdentifier: "storeContainer")
        let shop = shops[indexPath.row]
        
        cell.shopName.font = UIFont.fontAwesome(ofSize: 18)
        cell.shopName.text = shop.name ?? ""
        cell.shopSecondText.font = UIFont.fontAwesome(ofSize: 18)
        cell.shopSecondText.text = String.fontAwesomeIcon(name: .mapMarker) + " Location"
        cell.thumbImage.downloadedFrom(link: Config.HBUrl + shop.shop_logo.thumb.absoluteString)
        cell.shopDescription.text = shop.description
        cell.shopMainImage.downloadedFrom(link: Config.HBUrl + shop.shop_picture.big.absoluteString)
        
        cell.actionButton1.tag = shop.id
        cell.actionButton1.addTarget(self, action: #selector(showStoreInfo(_:)), for: .touchUpInside)
        
        cell.actionButton2.tag = shop.id
        cell.actionButton2.addTarget(self, action: #selector(showStoreUpdate(_:)), for: .touchUpInside)
        
        return cell
    }
    
    @objc func showStoreInfo(_ sender: UIButton){
        let storyBoard = UIStoryboard(name: "ShopOwner", bundle: nil)
        let vc = storyBoard.instantiateViewController(withIdentifier: "shopDefault") as! ShopManagerController
        vc.shopId = "\(sender.tag)"
        navigationController?.show(vc, sender: self)
    }
    
    @objc func showStoreUpdate(_ sender: UIButton){
        let storyBoard = UIStoryboard(name: "ShopOwner", bundle: nil)
        let vc = storyBoard.instantiateViewController(withIdentifier: "shopUpdate") as! ShopUpdaterController
        vc.shopId = "\(sender.tag)"
        navigationController?.show(vc, sender: self)
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return shops?.count ?? 0
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getShopsInfo(){
        APIController.activityIndicator(title: "Getting stores", view: self.view)
        let urlString = Config.HBUrl + "users/shops"
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
                            self.shops = try JSONDecoder().decode([ShopController.Shop].self, from: data)
                            self.showStores()
                            APIController.removeActivityIndicator()
                        }
                    }
                    
                }
            } catch {
                print(error)
            }
        }.resume()
    }
    
    func showStores(){
        OperationQueue.main.addOperation {
            let range = NSMakeRange(0, self.container.numberOfSections)
            let sections = NSIndexSet(indexesIn: range)
            self.container.reloadSections(sections as IndexSet, with: .left)
        }
    }
    
    
    
}

extension UIImageView {
    func downloadedFrom(url: URL, contentMode mode: UIViewContentMode = .scaleAspectFit) -> UIImageView {
        contentMode = mode
        URLSession.shared.dataTask(with: url) { data, response, error in
            guard
                let httpURLResponse = response as? HTTPURLResponse, httpURLResponse.statusCode == 200,
                let mimeType = response?.mimeType, mimeType.hasPrefix("image"),
                let data = data, error == nil,
                let image = UIImage(data: data)
                else { return }
            DispatchQueue.main.async() {
                self.image = image
                self.contentMode = UIViewContentMode.scaleAspectFill
            }
        }.resume()
        return self
    }
    func downloadedFrom(link: String, contentMode mode: UIViewContentMode = .scaleAspectFit) -> UIImageView{
        guard let url = URL(string: link) else { return self }
        return downloadedFrom(url: url, contentMode: mode)
    }
}
