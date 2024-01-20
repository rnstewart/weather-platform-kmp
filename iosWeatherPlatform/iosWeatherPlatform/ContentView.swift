import SwiftUI
import sharedWeatherPlatform
import Kingfisher

struct ContentView: View {
    @StateObject var googleMapsInterface: ObservableGoogleMapsInterface = ObservableGoogleMapsInterface()
    @StateObject var weatherInterface: ObservableWeatherInterface = ObservableWeatherInterface()
    @StateObject var locationManager = LocationManager()
    @State var searchQuery: String = ""
    
    var body: some View {
        let weatherData = weatherInterface.data
        let googleMapsData = googleMapsInterface.data
        let isLoading: Bool = googleMapsInterface.data.loading || weatherInterface.data.loading
        
        VStack {
            HStack {
                TextField(
                    "",
                    text: $searchQuery
                ).padding(.bottom, 8)
                    .padding(.top, 16)
                    .textFieldStyle(.roundedBorder)
                
                if (isLoading) {
                    ProgressView()
                        .padding(6)
                } else if (!searchQuery.isEmpty) {
                    Image(systemName: "list.bullet")
                        .padding(6)
                        .onTapGesture {
                            googleMapsInterface.placesAutoComplete(input: searchQuery, latitude: nil, longitude: nil)
                        }
                    
                    Image(systemName: "magnifyingglass")
                        .padding(6)
                        .onTapGesture {
                            weatherInterface.searchWeather(query: searchQuery, latitude: nil, longitude: nil)
                        }
                } else {
                    Image(systemName: "location")
                        .padding(6)
                        .onTapGesture {
                            locationManager.updateLocation()
                        }
                }
            }
            
            let autocompletePredictions = googleMapsData.autocompletePredictions
            if !autocompletePredictions.isEmpty {
                List {
                    ForEach(autocompletePredictions, id: \.self) { prediction in
                        HStack {
                            Text(prediction.name)
                                .font(.system(size: 16))
                            
                            Spacer()
                        }.padding(8).onTapGesture {
                            googleMapsInterface.autocompleteResultSelected(location: prediction)
                        }
                    }
                }
            } else if let data = weatherData.data {
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
            }
            Spacer()
        }.padding(8).onAppear {
            googleMapsInterface.setup()
            weatherInterface.setup()
            locationManager.setLocationCallback { location in
                if let location = location {
                    weatherInterface.searchWeather(
                        query: "",
                        latitude: KotlinDouble(value: location.coordinate.latitude),
                        longitude: KotlinDouble(value: location.coordinate.longitude)
                    )
                }
            }
        }
    }
}
