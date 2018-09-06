//
//  CustomStoryBoardElements.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/26/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class CustomStoryBoardElements: NSObject {

}

extension UIView {
    
    @IBInspectable var cornerRadius: CGFloat {
        get {
            return layer.cornerRadius
        }
        set {
            layer.cornerRadius = newValue
            layer.masksToBounds = newValue > 0
        }
    }
    
    @IBInspectable var borderWidth: CGFloat {
        get {
            return layer.borderWidth
        }
        set {
            layer.borderWidth = newValue
        }
    }
    
    @IBInspectable var borderColor: UIColor? {
        get {
            return UIColor(cgColor: layer.borderColor!)
        }
        set {
            layer.borderColor = newValue?.cgColor
        }
    }
    
    @IBInspectable var dropShadow: CBool {
        get {
            return false
        }
        set {
            if (newValue){
                layer.masksToBounds = false
                layer.shadowColor = UIColor.black.cgColor
                layer.shadowOpacity = 0.5
                layer.shadowOffset = CGSize(width: -1, height: 1)
                layer.shadowRadius = 5
                
                layer.shadowPath = UIBezierPath(rect: bounds).cgPath
                layer.shouldRasterize = true
                layer.rasterizationScale = UIScreen.main.scale
            }
        }
    }
}
