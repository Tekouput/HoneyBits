//
//  ProductsViewController.swift
//  Honeybits
//
//  Created by Jhonny Bill Mena on 5/1/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ProductsViewController: UIViewController {

    var productsDD = ProductsDD()
    var selectedCategory: HBFavCategories?
    
    @IBOutlet weak var productsCollectionView: UICollectionView!
    
    @IBOutlet weak var categoryImageView: UIImageView!
    
    @IBOutlet weak var categoryNameLabel: UILabel!
    @IBOutlet weak var categoryDescriptionLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        productsDD.productsViewController = self // CAUTION, this can cause a memory loop
        productsCollectionView.dataSource = productsDD
        productsCollectionView.delegate = productsDD
        
        if let selectedCategory = selectedCategory {
            categoryImageView.image = selectedCategory.image ?? UIImage(named: "prod1")
            categoryNameLabel.text = selectedCategory.name
            categoryDescriptionLabel.text = selectedCategory.tag
        }
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.navigationController?.navigationBar.tintColor = .black
        self.navigationController?.navigationItem.backBarButtonItem?.title = ""
        
        self.navigationItem.title = "Favorites"
        
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
