//
//  CustomTextEditor.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 7/22/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import SkyFloatingLabelTextField
import AwesomeEnum

class CustomTextEditor: SkyFloatingLabelTextFieldWithIcon {
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.iconType = IconType.image
    }
}
