//
//  LocationAutocompleteResultsView.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 2/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import sharedWeatherPlatform

struct LocationAutocompleteResultsView: View {
    let autocompletePredictions: [AutocompletePlacesData.Prediction]
    let onLocationSelected: (AutocompletePlacesData.Prediction) -> Void
    
    var body: some View {
        List {
            ForEach(autocompletePredictions, id: \.self) { prediction in
                HStack {
                    Text(prediction.name)
                        .font(.system(size: 16))
                    
                    Spacer()
                }.padding(8).onTapGesture {
                    onLocationSelected(prediction)
                }
            }
        }
    }
}
