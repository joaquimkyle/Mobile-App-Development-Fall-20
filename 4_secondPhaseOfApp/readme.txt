Kyle Joaquim
Kyle_Joaquim@student.uml.edu
Assignment 4_secondPhaseOfApp

I built this app off of Android Studio's provided Navigation Activity, seperating fragments of the app with a navigation drawer. The fragments make us of relative layouts, constraint layouts, and scroll views. The Weather page uses OpenWeatherMaps API to fetch current weather data for a few locations including the current location of the device. The Portfolios page uses Finnhub Stock API to fetch current share data. These fragments use MVVM design, and their ViewModels hold LiveData classes that are populated by a Google Cloud Firestore.

I've included my resume in a zoomable Scroll View as I'm not quite sure what sort of UI to make sense to display that. I looked into launching a pdf reader activity using intent, but didn't get a chance to implement anything like this.

Most important lessons here were how to use Firestore and MVVM, as well as how to use them together. The amount of boilerplate involved makes me think Kotlin is a must moving forward. The Navigation Drawer implementation also took a fair amount of deciphering. 

