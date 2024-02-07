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
    private let repositories: Repositories = Repositories()

    private let stateMachine: MainScreenStateMachine
    
    @Published
    private(set) var state: MainScreenStateMachineState
    
    init() {
        let sharedRepositories: SharedRepositories = repositories.sharedRepositories
        self.stateMachine = MainScreenStateMachine(
            scope: nil,
            sharedRepositories: sharedRepositories
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
            intent: MainScreenStateMachineIntentSearchLocation(query: input)
        )
    }
    
    func onLocationSelected(location: AutocompletePlacesData.Prediction) {
        stateMachine.process(
            intent: MainScreenStateMachineIntentSelectLocation(location: location)
        )
    }
    
    func searchWeatherByLocation(latitude: KotlinDouble?, longitude: KotlinDouble?) {
        stateMachine.process(
            intent: MainScreenStateMachineIntentSearchWeatherByLocation(latitude: latitude, longitude: longitude)
        )
    }
    
    func searchWeatherByName(query: String) {
        stateMachine.process(
            intent: MainScreenStateMachineIntentSearchWeatherByName(query: query)
        )
    }
}
