//
//  User.swift
//  Honeybits
//
//  Created by Hector Andres Acosta Pozo on 7/31/18.
//  Copyright © 2018 Jhonny Bill Mena. All rights reserved.
//

import Foundation

class User: Codable {
    
    var id: Int = -1
    var first_name: String?
    var last_name: String?
    var profile_pic: URL?
    var sex: String?
    var birth_day: Date?
    var email: String?
    var role: String?
    var followers: Int?
    var following: Int?
    var favorites: Int?
    var date_joined: Date?
    

}

extension Formatter {
    static let iso8601: DateFormatter = {
        let formatter = DateFormatter()
        formatter.calendar = Calendar(identifier: .iso8601)
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX"
        return formatter
    }()
}
