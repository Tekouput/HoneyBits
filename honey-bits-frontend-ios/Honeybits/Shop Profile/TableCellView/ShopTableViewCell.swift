//
//  Shop.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/19/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ShopTableViewCell: UITableViewCell {

    @IBOutlet weak var image1: UIImageView!
    @IBOutlet weak var image2: UIImageView!
    @IBOutlet weak var image3: UIImageView!
    @IBOutlet weak var storeLogo: UIImageView!
    @IBOutlet weak var storeName: UILabel!
    @IBOutlet weak var storeLocation: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
