//
//  ProductViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/2/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ProductViewController: UIViewController {

    
    @IBOutlet weak var sellerNameLabel: UILabel!
    @IBOutlet weak var sellerImageView: UIImageView!
    @IBOutlet weak var addressIconImageView: UIImageView!
    @IBOutlet weak var sellerAddressLabel: UILabel!
    
    @IBOutlet weak var productImageView: UIImageView!
    @IBOutlet weak var productNameLabel: UILabel!
    @IBOutlet weak var productPriceLabel: UILabel!
    
    @IBOutlet weak var contactSellerButton: UIButton!
    @IBOutlet weak var quantityButton: UIButton!
    @IBOutlet weak var addToCartButton: UIButton!
    
    var product: Product?
    var seller: Shop?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        addToCartButton.layer.cornerRadius = 25
        addToCartButton.clipsToBounds = true
        
        
        // Configure Quantity Button
        
        let buttonsCornerRadius = CGFloat(20)
        let buttonsBorderColor = UIColor.lightGray.cgColor
        let buttonsBorderWidth = CGFloat(1)
        
        quantityButton.layer.cornerRadius = buttonsCornerRadius
        quantityButton.clipsToBounds = true
        quantityButton.layer.borderWidth = buttonsBorderWidth
        quantityButton.layer.borderColor = buttonsBorderColor
        
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
