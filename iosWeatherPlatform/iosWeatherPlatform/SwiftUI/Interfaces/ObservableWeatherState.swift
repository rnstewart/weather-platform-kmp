//
//  ObservableWeatherInterface.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 1/20/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import sharedWeatherPlatform

class ObservableWeatherState: ObservableObject, WeatherInterface {
    private let repositories: Repositories = Repositories()
    
    private let weatherState: WeatherRepositoryState
    
    @Published
    private(set) var data: WeatherData
    
    @Published
    private(set) var loading: Bool
    
    init() {
        let sharedRepositories: SharedRepositories = repositories.sharedRepositories
        let repo = sharedRepositories.weatherRepository
        self.data = repo.data.value
        self.loading = false
        self.weatherState = WeatherRepositoryState(
            scope: nil,
            sharedRepositories: sharedRepositories
        )
    }
    
    func setup() {
        Task.detached {
            for await dataFlow in self.repositories.sharedRepositories.weatherRepository.data {
                DispatchQueue.main.async {
                    self.data = dataFlow
                }
            }
            for await loading in self.weatherState.loading {
                DispatchQueue.main.async {
                    self.loading = loading.boolValue
                }
            }
        }
    }
    
    func searchWeatherByName(query: String) {
        weatherState.searchWeatherByName(query: query)
    }
    
    func searchWeatherByLocation(latitude: KotlinDouble?, longitude: KotlinDouble?) {
        weatherState.searchWeatherByLocation(latitude: latitude, longitude: longitude)
    }
}
