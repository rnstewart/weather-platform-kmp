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

class ObservableWeatherInterface: ObservableObject, WeatherInterface {
    private let repositories: Repositories = Repositories()
    
    private let sharedWeatherInterface: SharedWeatherInterface
    
    @Published
    private(set) var data: WeatherData

    init() {
        let sharedRepositories: SharedRepositories = repositories.sharedRepositories
        let repo = sharedRepositories.weatherRepository
        self.data = repo.data.value
        self.sharedWeatherInterface = SharedWeatherInterface(
            scope: nil,
            sharedRepositories: sharedRepositories
        )
    }

    func setup() {
        attachFlows()
    }
    
    func attachFlows() {
        Task.detached {
            for await dataFlow in self.repositories.sharedRepositories.weatherRepository.data {
                DispatchQueue.main.async {
                    self.data = dataFlow
                }
            }
        }
    }

    func searchWeather(query: String, latitude: KotlinDouble?, longitude: KotlinDouble?) {
        sharedWeatherInterface.searchWeather(query: query, latitude: latitude, longitude: longitude)
    }
}
