//
//  PopUpViewController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 8/17/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class PopUpViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    var collectionView: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    var imagePicker: UIImagePickerController!
    
    @IBAction func takePhoto(_ sender: Any) {
        imagePicker =  UIImagePickerController()
        imagePicker.delegate = self
        imagePicker.sourceType = .camera
        
        present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func grabPhoto(_ sender: Any) {
        imagePicker =  UIImagePickerController()
        imagePicker.delegate = self
        imagePicker.sourceType = .photoLibrary
        
        present(imagePicker, animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        imagePicker.dismiss(animated: true, completion: nil)
        //targetView.contentMode = .scaleAspectFill
        //targetView.cropExtra()
        CreateProductViewController.images.append(info[UIImagePickerControllerOriginalImage] as! UIImage)
        collectionView.reloadData()
        self.dismiss(animated: true, completion: nil)
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
