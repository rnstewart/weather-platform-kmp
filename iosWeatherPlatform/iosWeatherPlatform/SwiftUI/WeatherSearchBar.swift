//
//  WeatherSearchBar.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 2/13/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import sharedWeatherPlatform

struct WeatherSearchBar: View {
    let loading: Bool
    @State var searchQuery: String = ""
    let onLocationSearch: (String) -> Void
    let searchWeatherByName: (String) -> Void
    let updateLocation: () -> Void
    
    var body: some View {
        HStack {
            TextField(
                "",
                text: $searchQuery
            ).padding(.bottom, 8)
                .padding(.top, 16)
                .textFieldStyle(.roundedBorder)
            
            if (!searchQuery.isEmpty) {
                Image(systemName: "list.bullet")
                    .padding(6)
                    .onTapGesture {
                        onLocationSearch(searchQuery)
                        searchQuery = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
                    }.disabled(loading)
                
                Image(systemName: "magnifyingglass")
                    .padding(6)
                    .onTapGesture {
                        searchWeatherByName(searchQuery)
                        searchQuery = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
                    }.disabled(loading)
            } else {
                Image(systemName: "location")
                    .padding(6)
                    .onTapGesture {
                        updateLocation()
                        searchQuery = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
                    }.disabled(loading)
            }
        }

    }
}

