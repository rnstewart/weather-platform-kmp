import SwiftUI
import sharedWeatherPlatform

struct ContentView: View {
    @StateObject var mainScreenState: ObservableMainScreenStateMachine = ObservableMainScreenStateMachine()
    @StateObject var locationManager = LocationManager()
    @State var searchQuery: String = ""
    
    var body: some View {
        let isLoading = (mainScreenState.state is MainScreenStateWeatherDataLoading)
        
        VStack {
            HStack {
                TextField(
                    "",
                    text: $searchQuery
                ).padding(.bottom, 8)
                    .padding(.top, 16)
                    .textFieldStyle(.roundedBorder)
                
                if (!searchQuery.isEmpty) {
                    Image(systemName: "list.bullet")
                        .padding(6)
                        .onTapGesture {
                            mainScreenState.onLocationSearch(input: searchQuery)
                            searchQuery = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
                        }.disabled(isLoading)
                    
                    Image(systemName: "magnifyingglass")
                        .padding(6)
                        .onTapGesture {
                            mainScreenState.searchWeatherByName(query: searchQuery)
                            searchQuery = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
                        }.disabled(isLoading)
                } else {
                    Image(systemName: "location")
                        .padding(6)
                        .onTapGesture {
                            locationManager.updateLocation()
                            searchQuery = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
                        }.disabled(isLoading)
                }
            }
            
            if let autocompletePredictions = (mainScreenState.state as? MainScreenStateAutocompleteLoaded)?.places {
                LocationAutocompleteResultsView(
                    autocompletePredictions: autocompletePredictions
                ) { location in
                    mainScreenState.onLocationSelected(location: location)
                }
            } else if let data = (mainScreenState.state as? MainScreenStateWeatherData)?.data {
                WeatherDataView(data: data)
            } else if let data = (mainScreenState.state as? MainScreenStateWeatherDataLoading)?.data {
                ZStack {
                    WeatherDataView(data: data)
                    ProgressView()
                        .padding(6)
                }
            } else if let error = (mainScreenState.state as? MainScreenStateError)?.error, !error.isEmpty {
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
