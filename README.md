# WeatherApp
Simple WeatherApp client

master branch contains sample with coroutines usage

rxjava-usage branch - sample with rxjava

Application uses user location for loading weather forecast for current point (city). 
Also user can add other cities to see their current weather.
By clicking on city user see another screen with detailed forecast for a few days.
As weather provider api.openweathermap.org is used.

Data layer is built using Repository pattern.
There are RemoteDataSource (impl is Retrofit client) and LocalDataSource (impl is Room daos). 
There are database observing through LiveData.
Application provides limited support for offline mode.

Presentation layer is built (MVVM with one way databinding) using ViewModel (arch comp) and data observing through LiveData.
Koin is used as a dependency injection tool.

ViewModels are covered with unit tests.

What are not included:
Android data binding (xml),
Android navigation (arch comp)
