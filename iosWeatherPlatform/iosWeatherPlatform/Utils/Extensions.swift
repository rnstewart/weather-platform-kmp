//
//  Extensions.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 1/20/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import sharedWeatherPlatform
import UIKit

extension String {
    func localizedStringFromKey(comment: String = "") -> String {
        return NSLocalizedString(self, comment: comment)
    }
}

extension WeatherDataResponse {
    func getCurrentTempStr() -> String {
        let temp = currentTempFahrenheit
        if !temp.isEmpty {
            return String(
                format: "temperature_x".localizedStringFromKey(),
                temp
            )
        } else {
            return ""
        }
    }
    
    func getWindStr() -> String {
        if let wind = windWithDirection {
            let speed = String(wind.first ?? "")
            let dir = String(wind.second ?? "")
            if (dir.isEmpty) {
                return String(
                    format: "wind_info".localizedStringFromKey(),
                    speed
                )
            } else {
                return String(
                    format: "wind_info_direction".localizedStringFromKey(),
                    dir,
                    speed
                )
            }
        } else {
            return ""
        }
    }
}

extension UIApplication {
    func endEditing() {
        sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}
