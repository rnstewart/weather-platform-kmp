//
//  LocationManager.swift
//  iosWeatherPlatform
//
//  Created by Russell Stewart on 1/20/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import CoreLocation

class LocationManager: NSObject, ObservableObject, CLLocationManagerDelegate {
    private let locationManager = CLLocationManager()
    @Published var authorizationStatus: CLAuthorizationStatus?
    @Published var lastLocation: CLLocation?
    var onLocationUpdated: (CLLocation?) -> Void = { _ in }
    
    override init() {
        super.init()
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }

    func setLocationCallback(callback: @escaping (CLLocation?) -> Void) {
        onLocationUpdated = callback
    }
    
    func updateLocation() {
        if (authorizationStatus == .notDetermined) {
            locationManager.requestWhenInUseAuthorization()
        } else if (authorizationStatus == .authorizedAlways || authorizationStatus == .authorizedWhenInUse) {
            locationManager.requestLocation()
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        authorizationStatus = status
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        lastLocation = location
        onLocationUpdated(lastLocation)
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Location error: \(error)")
    }
}
