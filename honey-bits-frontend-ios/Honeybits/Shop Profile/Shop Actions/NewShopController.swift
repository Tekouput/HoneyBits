//
//  NewShopController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 6/24/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit
import SkyFloatingLabelTextField
import GooglePlaces
import GooglePlacePicker
import AwesomeEnum
import BFPaperButton
import FontAwesome_swift

class NewShopController: UIViewController, GMSPlacePickerViewControllerDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    @IBOutlet weak var nameSpace: CustomTextEditor!
    @IBOutlet weak var descriptionShop: CustomTextEditor!
    @IBOutlet weak var location: CustomTextEditor!
    @IBOutlet weak var cameraButton: BFPaperButton!
    @IBOutlet weak var getImageButton: BFPaperButton!
    @IBOutlet weak var shopImageView: UIImageView!
    @IBOutlet weak var logoImage: UIImageView!
    
    var imagePicker: UIImagePickerController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        nameSpace.iconImage = Awesome.solid.shoppingBag.asImage(size: 20)
        descriptionShop.iconImage = Awesome.solid.alignJustify.asImage(size: 20)
        location.iconImage = Awesome.solid.mapMarker.asImage(size: 20)
        
        cameraButton.titleLabel?.font = UIFont.fontAwesome(ofSize: 30)
        cameraButton.setTitle(String.fontAwesomeIcon(name: .camera), for: .normal)
        
        getImageButton.titleLabel?.font = UIFont.fontAwesome(ofSize: 30)
        getImageButton.setTitle(String.fontAwesomeIcon(name: .pictureO), for: .normal)
        shopImageView.cropExtra()
        
        logoImage.makeRounded()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func importLogo(_ sender: Any) {
        imagePicker = UIImagePickerController()
        imagePicker.view.tag = 1;
        imagePicker.delegate = self
        imagePicker.sourceType = .photoLibrary
        
        present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func importImage(_ sender: Any){
        imagePicker = UIImagePickerController()
        imagePicker.view.tag = 0;
        imagePicker.delegate = self
        imagePicker.sourceType = .photoLibrary
        
        present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func takePhoto(_ sender: Any) {
        imagePicker = UIImagePickerController()
        imagePicker.view.tag = 0;
        imagePicker.delegate = self
        imagePicker.sourceType = .camera
        
        present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func createShop(_ sender: Any) {
        APIController.createStore(name: nameSpace.text!, description: descriptionShop.text!, latitude: self_latitude, longitude: self_longitude, shopImage: self_image, caller: self, shopLogo: self_logo, placeId: self_place_id)
    }
    
    var self_logo: UIImage!
    var self_image: UIImage!
    var self_place_id: String = ""
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        switch picker.view.tag {
        case 0:
            imagePicker.dismiss(animated: true, completion: nil)
            self_image = (info[UIImagePickerControllerOriginalImage] as? UIImage)!
            shopImageView.image = self_image
            shopImageView.cropExtra()
        default:
            imagePicker.dismiss(animated: true, completion: nil)
            self_logo = (info[UIImagePickerControllerOriginalImage] as? UIImage)!
            logoImage.image = self_logo
            logoImage.makeRounded()
        }
    }
    
    @IBAction func pickPlace(_ sender: Any) {
        let config = GMSPlacePickerConfig(viewport: nil)
        let placePicker = GMSPlacePickerViewController(config: config)
        placePicker.delegate = self
        present(placePicker, animated: true, completion: nil)
    }
    
    var self_longitude: String = ""
    var self_latitude: String = ""
    
    func placePicker(_ viewController: GMSPlacePickerViewController, didPick place: GMSPlace) {
        
        viewController.dismiss(animated: true, completion: nil)
        print("PLace name \(place.name)")
        location.text = "\(place.coordinate.latitude), \(place.coordinate.longitude)"
        self_latitude = "\(place.coordinate.latitude)"
        self_longitude = "\(place.coordinate.longitude)"
        self_place_id = "\(place.placeID)"
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        viewController.dismiss(animated: true, completion: nil)
        print("No place selected")
    }
}
