//
//  ProfileController.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 7/31/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import UIKit

class ProfileController: UIViewController {

    @IBOutlet weak var userImage: UIImageView!
    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var followersCount: UIButton!
    @IBOutlet weak var followingCount: UIButton!
    @IBOutlet weak var favoritesCount: UIButton!
    @IBOutlet weak var joinedLabel: UILabel!
    
    var myFavorites = MyFavorites()
    @IBOutlet weak var collection: UICollectionView!
    
    var user: User!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getUserInformation()
        
        myFavorites.vc = self
        collection.delegate = myFavorites
        collection.dataSource = myFavorites
        myFavorites.loadFavorites(view: self.view, collection: collection)

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getUserInformation(){
        APIController.activityIndicator(title: "Getting user", view: self.view)
        let urlString = Config.HBUrl + "users"
        guard let url = URL(string: urlString) else {return}
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue(APIController.API_KEY, forHTTPHeaderField: "Authorization")
        
        let session = URLSession.shared
        session.dataTask(with: request) { (data, response, error) in
            do {
                if let response = response as? HTTPURLResponse{
                    if (response.statusCode == 401) {
                        OperationQueue.main.addOperation {
                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                            let popup = storyboard.instantiateViewController(withIdentifier: "SignInPopUp") as! SignInPopUpViewController
                            popup.modalPresentationStyle = .overCurrentContext
                            self.present(popup, animated: true, completion: nil)
                        }
                    } else {
                        if let data = data {
                            _ = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any]
                            let decoder = JSONDecoder()
                            decoder.dateDecodingStrategy = .formatted(Formatter.iso8601)
                            self.user = try decoder.decode(User.self, from: data)
                            self.updateInfo()
                            APIController.removeActivityIndicator()
                        }
                    }
                }
            } catch {
                print(error)
            }
        }.resume()
    }
    
    func updateInfo(){
        OperationQueue.main.addOperation {
            let user = self.user!
            self.userImage.downloadedFrom(link: user.profile_pic!.absoluteString).makeRounded()
            self.userName.text = "\(user.first_name!) \(user.first_name!)"
            self.favoritesCount.setTitle("\(user.favorites!)", for: .normal)
            self.followersCount.setTitle("\(user.followers!)", for: .normal)
            self.followingCount.setTitle("\(user.following!)", for: .normal)
            
            let formatter = DateFormatter.self
            let joined = formatter.localizedString(from: user.date_joined!, dateStyle: DateFormatter.Style.long, timeStyle: DateFormatter.Style.short)
            
            self.joinedLabel.text = "Joined: \(joined)"
            
        }
    }
    
}
