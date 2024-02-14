//
//  WeatherDataView.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 2/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import sharedWeatherPlatform
import Kingfisher

struct WeatherDataView: View {
    let data: WeatherDataResponse?
    
    var body: some View {
        VStack {
            if let data = data {
                HStack {
                    Spacer()
                    Text(data.name ?? "")
                        .font(.system(size: 24))
                    Spacer()
                }
                
                HStack(alignment: .center) {
                    VStack(alignment: .leading) {
                        Text(data.getCurrentTempStr())
                            .font(.system(size: 20))
                        
                        let currentWeatherCondition = data.currentWeatherCondition
                        if !currentWeatherCondition.isEmpty {
                            Text(currentWeatherCondition)
                                .font(.system(size: 18))
                        }
                    }
                    
                    let urlString = (data.getIconUrl(density: 2))
                    if let iconUrl = URL(string: urlString) {
                        KFImage.url(iconUrl)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 40, height: 40)
                    }
                    Spacer()
                }.padding(.top, 16)
                
                let windString = data.getWindStr()
                if (!windString.isEmpty) {
                    HStack {
                        Spacer()
                        Image("IconWind")
                            .padding(6)
                        Text(windString)
                        Spacer()
                    }.padding(.vertical, 16)
                }
                
                let sunriseStr = data.sunriseStr
                let sunsetStr = data.sunsetStr
                if (!sunriseStr.isEmpty && !sunsetStr.isEmpty) {
                    HStack {
                        Spacer()
                        Image("IconSunrise")
                            .padding(6)
                        Text(sunriseStr)
                        
                        Spacer()
                        
                        Image("IconSunset")
                            .padding(6)
                        Text(sunsetStr)
                        Spacer()
                    }.padding(.vertical, 16)
                }
            }
        }
    }
}
