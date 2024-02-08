//
//  ObservableMainScreenStateMachine.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 2/7/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import sharedWeatherPlatform

class ObservableMainScreenStateMachine: ObservableObject {
    private let stateMachine: MainScreenStateMachine
    
    @Published
    private(set) var state: MainScreenState
    
    init() {
        self.stateMachine = MainScreenStateMachine(
            scope: nil,
            sharedRepositories: SharedRepositories()
        )
        self.state = self.stateMachine.state.value
    }
    
    func setup() {
        Task.detached {
            for await stateFlow in self.stateMachine.state {
                DispatchQueue.main.async {
                    self.state = stateFlow
                }
            }
        }
    }
    
    func onLocationSearch(input: String) {
        stateMachine.process(
            intent: MainScreenIntentSearchLocation(query: input)
        )
    }
    
    func onLocationSelected(location: AutocompletePlacesData.Prediction) {
        stateMachine.process(
            intent: MainScreenIntentSelectLocation(location: location)
        )
    }
    
    func searchWeatherByLocation(latitude: KotlinDouble?, longitude: KotlinDouble?) {
        stateMachine.process(
            intent: MainScreenIntentSearchWeatherByLocation(latitude: latitude, longitude: longitude)
        )
    }
    
    func searchWeatherByName(query: String) {
        stateMachine.process(
            intent: MainScreenIntentSearchWeatherByName(query: query)
        )
    }
}
