//
//  CartViewController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/21/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class CartViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var productsTableView: UITableView!
    @IBOutlet weak var emptyHolder: UIStackView!
    @IBOutlet weak var checkOutButton: UIButton!
    
    struct Cart: Codable {
        var products: [Product]? = []
        var Address: Address?
    }
    
    struct Address: Codable {
        var primary_address: String?
    }
    
    var cart: Cart!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        cart = Cart()
        productsTableView.delegate = self
        productsTableView.dataSource = self
        loadData()
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            cart.products!.remove(at: indexPath.row)
            productsTableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
        }
    }
    
    func loadData(){
        let urlString = "\(Config.HBUrl)carts"
        print(urlString)
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            if let response = response as? HTTPURLResponse {
                do {
                    if (response.statusCode == 401) {
                        OperationQueue.main.addOperation {
                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                            let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                            popup.modalPresentationStyle = .overCurrentContext
                            self.present(popup, animated: true, completion: nil)
                        }
                    } else {
                        if let data = data {
                            self.cart = try JSONDecoder().decode(Cart.self, from: data)
                            OperationQueue.main.addOperation {
                                self.productsTableView.reloadData()
                                if (self.cart.products!.count > 0){
                                    self.emptyHolder.isHidden = true
                                    self.checkOutButton.setTitle("Pay $\(self.getTotalCost())", for: .normal)
                                }
                            }
                        }
                    }
                } catch {
                    print(error)
                }
            }
            }.resume()
    }
    
    func getTotalCost() -> String{
        var cost: Double = 0
        for product in cart.products! {
            cost += Double(product.price.raw)!
        }
        return String(format: "%.2f", cost)
    }

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return cart.products!.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProductCartCell", for: indexPath) as! ProductTableViewCell
        cell.productName.text = cart.products![indexPath.row].name
        cell.productPrice.text = cart.products![indexPath.row].price.formatted
        cell.productStore.text = cart.products![indexPath.row].shop.name
        
        var urlImages = "\(Config.HBUrl)images/honey.jpg";
        if ((cart.products![indexPath.row].pictures?.count)! > 0){
            urlImages = "\(Config.HBUrl)\(cart.products![indexPath.row].pictures![0].urls.thumb.absoluteURL)"
        }
        
        cell.productImage.downloadedFrom(link: urlImages).layer.cornerRadius = 15.0
        
        return cell;
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
