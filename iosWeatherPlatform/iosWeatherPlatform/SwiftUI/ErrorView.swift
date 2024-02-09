//
//  ErrorView.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 2/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import sharedWeatherPlatform

struct ErrorView: View {
    let error: String
    
    var body: some View {
        HStack {
            Spacer()

            Image(systemName: "exclamationmark.triangle.fill")
            
            Text(error)
                .font(.system(size: 20))
                .fontWeight(.semibold)
                .padding(.leading, 6)
            
            Spacer()
        }
    }
}
