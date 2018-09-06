//
//  CreateProductViewController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/17/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class CreateProductViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UITextFieldDelegate {
    
    static var images: [UIImage]! = []
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var categories: UITextView!
    @IBOutlet weak var descriptionField: UITextView!
    @IBOutlet weak var currencyTextField: UITextField!
    @IBOutlet weak var quantityField: UITextField!
    @IBOutlet weak var titleField: UITextField!
    @IBOutlet weak var sku: UITextField!
    
    var shop: String!
    var cv: UICollectionView!
    var vc: ProductsManager!
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return CreateProductViewController.images.count + 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ImageLayout", for: indexPath) as! ProductImageViewCell
        
        cell.parentVC = self
        
        if (CreateProductViewController.images.count == indexPath.row){
            cell.removeButton.isHidden = true
        } else {
            cell.removeButton.isHidden = false
            cell.image.image = CreateProductViewController.images[indexPath.row]
            cell.image.cropExtra()
            cell.image.contentMode = .scaleAspectFill
            cell.card.removeTarget(nil, action: nil, for: .allEvents)
        }
        
        return cell
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()

        collectionView.dataSource = self
        collectionView.delegate = self
        
        categories.addBorders(placeHolder: "Separate each categorie with a comma")
        descriptionField.addBorders(placeHolder: "Write a description for this product")
        
        currencyTextField.addTarget(self, action: #selector(myTextFieldDidChange), for: .editingChanged)
        quantityField.delegate = self
    }
    
    @objc func myTextFieldDidChange(_ textField: UITextField) {
        
        if let amountString = textField.text?.currencyInputFormatting() {
            textField.text = amountString
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    let MAX_LENGTH_PHONENUMBER = 15
    let ACCEPTABLE_NUMBERS     = "0123456789"
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        let newLength: Int = textField.text!.characters.count + string.characters.count - range.length
        let numberOnly = NSCharacterSet.init(charactersIn: ACCEPTABLE_NUMBERS).inverted
        let strValid = string.rangeOfCharacter(from: numberOnly) == nil
        return (strValid && (newLength <= MAX_LENGTH_PHONENUMBER))
    }
    
    struct RequestPayload_Product: Codable {
        var product: PayloadProduct!
        var categories: [String]
    }
    
    struct PayloadProduct: Codable {
        var name: String
        var description: String
        var price: String
        var shop_id: String
    }
    
    var imageCounter = 0
    func addImage(id: String){
        APIController.activityIndicator(title: "Uploading \(imageCounter + 1)", view: self.view)
        let link = Config.HBUrl + "products/image"
        let url = URLComponents(string: link)!
        
        var request = URLRequest(url: url.url!)
        request.httpMethod = "POST"
        let boundary = APIController.generateBoundary()
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let parameters = [
            "product_id": "\(id)"
        ]
        
        let mediaImage = Media(withImage: CreateProductViewController.images[imageCounter], forKey: "image")
        let dataBody = APIController.createDataBody(withParameters: parameters, media: [mediaImage!], boundary: boundary)
        
        request.httpBody = dataBody as Data
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            
            if let response = response as? HTTPURLResponse {
                if (response.statusCode == 401) {
                    OperationQueue.main.addOperation {
                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                        let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                        popup.modalPresentationStyle = .overCurrentContext
                        self.present(popup, animated: true, completion: nil)
                    }
                } else {
                    if data != nil {
                        self.imageCounter += 1
                        if(self.imageCounter < CreateProductViewController.images.count){
                            self.addImage(id: id)
                        } else {
                            OperationQueue.main.addOperation {
                                APIController.removeActivityIndicator()
                                self.vc.loadProducts(view: self.vc.view, collection: self.cv, id: self.shop)
                                self.dismiss(animated: true, completion: nil)
                            }
                        }
                    }
                }
            }
        }.resume()
    }
    
    @IBAction func create(_ sender: Any) {
        APIController.activityIndicator(title: "Creating", view: self.view)
        
        let innerInfo: PayloadProduct = PayloadProduct(
            name: titleField.text!,
            description: descriptionField.text,
            price: currencyTextField.text!.currencyClean(),
            shop_id: shop
        )
        
        let outterInfo: RequestPayload_Product = RequestPayload_Product(
            product: innerInfo,
            categories: categories.text.split{$0 == ","}.map(String.init)
        )
        
        let urlString = Config.HBUrl + "products"
        
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let jsonEncoder = JSONEncoder()
        guard let httpBody = try? jsonEncoder.encode(outterInfo) else {return}
        
        request.httpBody = httpBody
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            
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
                        do{
                            APIController.removeActivityIndicator()
                            let product = try JSONDecoder().decode(Product.self, from: data)
                            if(self.imageCounter < CreateProductViewController.images.count){
                                self.addImage(id: String(product.id))
                            }
                        } catch {
                            print(error)
                        }
                    }
                }
            }
        }.resume()
    }
    
}

extension UITextView {
    func addBorders(placeHolder: String){
        self.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).cgColor
        self.layer.borderWidth = 1.0
        self.toolbarPlaceholder = placeHolder
    }
}

extension String {
    
    func currencyClean() -> String {
        return self.trimmingCharacters(in: [",", "$"])
    }
    
    // formatting text for currency textField
    func currencyInputFormatting() -> String {
        
        var number: NSNumber!
        let formatter = NumberFormatter()
        formatter.numberStyle = .currencyAccounting
        formatter.currencySymbol = "$"
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 2
        
        var amountWithPrefix = self
        
        // remove from String: "$", ".", ","
        let regex = try! NSRegularExpression(pattern: "[^0-9]", options: .caseInsensitive)
        amountWithPrefix = regex.stringByReplacingMatches(in: amountWithPrefix, options: NSRegularExpression.MatchingOptions(rawValue: 0), range: NSMakeRange(0, self.characters.count), withTemplate: "")
        
        let double = (amountWithPrefix as NSString).doubleValue
        number = NSNumber(value: (double / 100))
        
        // if first number is 0 or all numbers were deleted
        guard number != 0 as NSNumber else {
            return ""
        }
        
        return formatter.string(from: number)!
    }
}
