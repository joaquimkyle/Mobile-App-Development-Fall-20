Kyle Joaquim
Kyle_Joaquim@student.uml.edu
Assignment 4_secondPhaseOfApp

I built this app off of Android Studio's provided Navigation Activity, seperating fragments of the app with a navigation drawer. The main fragment uses a relative view nested inside of a scroll view, while the other fragments use constraint layouts. The Weather page uses OpenWeatherMaps API to fetch current weather data for a few locations including the current location of the device. This fragment uses MVVM design, and the ViewModel is populated by a Google Cloud Firestore.

There's not much of a UI on the Portfolio page, but it uses Finnhub Stock API to fetch the latest share data.