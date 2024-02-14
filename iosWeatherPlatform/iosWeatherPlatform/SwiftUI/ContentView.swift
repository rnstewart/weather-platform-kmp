import SwiftUI
import sharedWeatherPlatform

struct ContentView: View {
    @StateObject var mainScreenState: ObservableMainScreenStateMachine = ObservableMainScreenStateMachine()
    @StateObject var locationManager = LocationManager()
    
    var body: some View {
        VStack {
            let _ = print("MainScreenState: mainScreenState.state = \(mainScreenState.state)")
            WeatherSearchBar(
                loading: (mainScreenState.state is MainScreenStateWeatherDataLoading),
                onLocationSearch: { query in
                    mainScreenState.onLocationSearch(input: query)
                },
                searchWeatherByName: { query in
                    mainScreenState.searchWeatherByName(query: query)
                },
                updateLocation: {
                    locationManager.updateLocation()
                }
            )
            
            if let autocompletePredictions = (mainScreenState.state as? MainScreenStateAutocompleteLoaded)?.places {
                let _ = print("MainScreenState: Autocomplete")
                LocationAutocompleteResultsView(
                    autocompletePredictions: autocompletePredictions
                ) { location in
                    mainScreenState.onLocationSelected(location: location)
                }
            } else if let loading = (mainScreenState.state as? MainScreenStateWeatherDataLoading) {
                let _ = print("MainScreenState: Loading")
                ZStack {
                    WeatherDataView(data: loading.data)
                    
                    ProgressView()
                        .padding(6)
                }
            } else if let data = (mainScreenState.state as? MainScreenStateWeatherData)?.data {
                let _ = print("MainScreenState: Loaded")
                WeatherDataView(data: data)
            } else if let error = (mainScreenState.state as? MainScreenStateError)?.error, !error.isEmpty {
                let _ = print("MainScreenState: Error")
                if (!error.isEmpty) {
                    ErrorView(error: error)
                }
            }
            Spacer()
        }.padding(8).onAppear {
            mainScreenState.setup()
            locationManager.setLocationCallback { location in
                if let location = location {
                    mainScreenState.searchWeatherByLocation(
                        latitude: KotlinDouble(value: location.coordinate.latitude),
                        longitude: KotlinDouble(value: location.coordinate.longitude)
                    )
                }
            }
        }
    }
}
