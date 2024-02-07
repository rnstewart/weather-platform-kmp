import SwiftUI
import sharedWeatherPlatform
import Kingfisher

struct ContentView: View {
    @StateObject var mainScreenState: ObservableMainScreenStateMachine = ObservableMainScreenStateMachine()
    @StateObject var locationManager = LocationManager()
    @State var searchQuery: String = ""
    
    var body: some View {
        let weatherData = (mainScreenState.state as? MainScreenStateMachineStateWeatherLoaded)?.data
        let autocompletePredictions = (mainScreenState.state as? MainScreenStateMachineStateAutocompleteLoaded)?.places ?? []
        let isLoading = (mainScreenState.state is MainScreenStateMachineStateLoading)
        let error = (mainScreenState.state as? MainScreenStateMachineStateError)?.error ?? ""
        
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
            
            if (isLoading) {
                ProgressView()
                    .padding(6)
            } else if !autocompletePredictions.isEmpty {
                List {
                    ForEach(autocompletePredictions, id: \.self) { prediction in
                        HStack {
                            Text(prediction.name)
                                .font(.system(size: 16))
                            
                            Spacer()
                        }.padding(8).onTapGesture {
                            mainScreenState.onLocationSelected(location: prediction)
                        }
                    }
                }
            } else if let data = weatherData {
                HStack {
                    Spacer()
                    Text(data.name ?? "")
                        .font(.system(size: 24))
                    Spacer()
                }
                
                HStack(alignment: .center) {
                    VStack(alignment: .leading) {
                        Text(data.getCurrentTempStr())
                            .font(.system(size: 20))
                        
                        let currentWeatherCondition = data.currentWeatherCondition
                        if !currentWeatherCondition.isEmpty {
                            Text(currentWeatherCondition)
                                .font(.system(size: 18))
                        }
                    }
                    
                    let urlString = (data.getIconUrl(density: 2))
                    if let iconUrl = URL(string: urlString) {
                        KFImage.url(iconUrl)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 40, height: 40)
                    }
                    Spacer()
                }.padding(.top, 16)
                
                let windString = data.getWindStr()
                if (!windString.isEmpty) {
                    HStack {
                        Spacer()
                        Image("IconWind")
                            .padding(6)
                        Text(windString)
                        Spacer()
                    }.padding(.vertical, 16)
                }
                
                let sunriseStr = data.sunriseStr
                let sunsetStr = data.sunsetStr
                if (!sunriseStr.isEmpty && !sunsetStr.isEmpty) {
                    HStack {
                        Spacer()
                        Image("IconSunrise")
                            .padding(6)
                        Text(sunriseStr)
                        
                        Spacer()
                        
                        Image("IconSunset")
                            .padding(6)
                        Text(sunsetStr)
                        Spacer()
                    }.padding(.vertical, 16)
                }
                
                if (!error.isEmpty) {
                    ErrorMessage(error: error)
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

struct ErrorMessage: View {
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
