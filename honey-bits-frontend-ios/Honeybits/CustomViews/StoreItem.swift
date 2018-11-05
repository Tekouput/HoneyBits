//
//  StoreItem.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 7/25/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import MaterialComponents.MaterialCards

@IBDesignable class StoreItem: UITableViewCell {
    
    @IBOutlet var contentViewSP: UIView!
    @IBOutlet weak var thumbImage: UIImageView!
    @IBOutlet weak var shopName: UILabel!
    @IBOutlet weak var shopSecondText: UILabel!
    @IBOutlet weak var shopMainImage: UIImageView!
    @IBOutlet weak var shopDescription: UILabel!
    @IBOutlet weak var actionButton1: UIButton!
    @IBOutlet weak var actionButton2: UIButton!
    @IBOutlet weak var actionButton3: UIButton!

    override init(style: UITableViewCellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    private func commonInit(){
        Bundle.main.loadNibNamed("StoreItem", owner: self, options: nil)
        let card = MDCCard()
        card.setShadowElevation(ShadowElevation(rawValue: 6), for: .normal)
        
        addSubview(card)
        card.frame = self.bounds
        card.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        card.frame = UIEdgeInsetsInsetRect(card.frame, UIEdgeInsetsMake(15, 20, 15, 20))
        
        card.addSubview(contentViewSP)
        contentViewSP.frame = card.bounds
        contentViewSP.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        
        thumbImage.makeRounded()
        shopMainImage.cropExtra()
    }
}

extension UIImageView {
    func makeRounded() {
        self.layer.borderWidth = 1
        self.layer.masksToBounds = false
        self.layer.borderColor = UIColor.white.cgColor
        self.layer.cornerRadius = self.frame.height/2
        self.clipsToBounds = true
    }
    
    func cropExtra() {
        self.layer.borderWidth = 0
        self.layer.masksToBounds = false
        self.layer.borderColor = UIColor.white.cgColor
        self.layer.cornerRadius = 0
        self.clipsToBounds = true
    }
}
