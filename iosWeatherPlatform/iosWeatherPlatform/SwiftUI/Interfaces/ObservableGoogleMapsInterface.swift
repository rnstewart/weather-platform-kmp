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

class ObservableGoogleMapsInterface: ObservableObject, GoogleMapsInterface {
    private let repositories: Repositories = Repositories()
    
    private let sharedGoogleMapsInterface: SharedGoogleMapsInterface
    
    @Published
    private(set) var data: GoogleMapsData

    init() {
        let sharedRepositories: SharedRepositories = repositories.sharedRepositories
        let repo = sharedRepositories.googleMapsRepository
        self.data = repo.data.value
        self.sharedGoogleMapsInterface = SharedGoogleMapsInterface(
            scope: nil,
            sharedRepositories: sharedRepositories
        )
    }

    func setup() {
        attachFlows()
    }
    
    func attachFlows() {
        Task.detached {
            for await dataFlow in self.repositories.sharedRepositories.googleMapsRepository.data {
                DispatchQueue.main.async {
                    self.data = dataFlow
                }
            }
        }
    }
    
    func autocompleteResultSelected(location: AutocompletePlacesData.Prediction) {
        sharedGoogleMapsInterface.autocompleteResultSelected(location: location)
    }
    
    func placeDetails(placeId: String, fields: String?) {
        sharedGoogleMapsInterface.placeDetails(placeId: placeId, fields: fields)
    }
    
    func placesAutoComplete(input: String, latitude: KotlinDouble?, longitude: KotlinDouble?) {
        sharedGoogleMapsInterface.placesAutoComplete(input: input, latitude: latitude, longitude: longitude)
    }
}
