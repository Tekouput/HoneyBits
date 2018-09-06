//
//  ProductImageViewCell.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/17/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import MaterialComponents

class ProductImageViewCell: UICollectionViewCell {
    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var removeButton: UIButton!
    var parentVC: CreateProductViewController!
    @IBOutlet weak var card: MDCCard!
    
    @IBAction func openPopup(_ sender: Any) {
        
        let storyboard = UIStoryboard(name: "ShopOwner", bundle: nil)
        let popup = storyboard.instantiateViewController(withIdentifier: "ChoosePictureSource") as! PopUpViewController
        popup.collectionView = parentVC.collectionView
        popup.modalPresentationStyle = .overCurrentContext
        parentVC.present(popup, animated: true, completion: nil)
    }
}
