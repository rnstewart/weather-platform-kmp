//
//  ObservableGoogleMapsInterface.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 1/20/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import sharedWeatherPlatform

class ObservableGoogleMapsState: ObservableObject, GoogleMapsInterface {
    private let repositories: Repositories = Repositories()
    
    private let googleMapsState: GoogleMapsRepositoryState
    
    @Published
    private(set) var data: GoogleMapsData

    @Published
    private(set) var loading: Bool
    
    init() {
        let sharedRepositories: SharedRepositories = repositories.sharedRepositories
        let repo = sharedRepositories.googleMapsRepository
        self.data = repo.data.value
        self.loading = false
        self.googleMapsState = GoogleMapsRepositoryState(
            scope: nil,
            sharedRepositories: sharedRepositories
        )
    }

    func setup() {
        Task.detached {
            for await dataFlow in self.repositories.sharedRepositories.googleMapsRepository.data {
                DispatchQueue.main.async {
                    self.data = dataFlow
                }
            }
            for await loading in self.googleMapsState.loading {
                DispatchQueue.main.async {
                    self.loading = loading.boolValue
                }
            }
        }
    }
    
    func autocompleteResultSelected(location: AutocompletePlacesData.Prediction) {
        googleMapsState.autocompleteResultSelected(location: location)
    }
    
    func placeDetails(placeId: String, fields: String?) {
        googleMapsState.placeDetails(placeId: placeId, fields: fields)
    }
    
    func placesAutoComplete(input: String, latitude: KotlinDouble?, longitude: KotlinDouble?) {
        googleMapsState.placesAutoComplete(input: input, latitude: latitude, longitude: longitude)
    }
}
