# WannaGo Android App


## Description
WannaGo is an Android app built using Kotlin as part of my Android App Development course for my MS in Computer and Information Science degree at IUPUI. The app allows users to create a list of locations they wish to visit by selecting locations on a Google Map and storing them in Firestore for real-time data storage. With a Navigation Drawer for easy navigation and a RecyclerView to display saved locations, the app provides a smooth, interactive experience. This project helped me gain hands-on experience in Android development concepts such as Fragments, Firestore integration, and ViewModel architecture, allowing me to build a fully functional mobile app with real-time data handling and a dynamic user interface.
## Features
#### o	Google Maps Integration:
The app integrates Google Maps to allow users to interact with the map, tapping on any location to save its latitude and longitude. This provides a seamless and intuitive user experience for location tracking. 
#### o	Firestore Database:
Locations are saved in Firestore in real-time, making it easy to store and retrieve data across sessions. The app automatically updates the saved list of locations every time a new one is added, ensuring data consistency across devices. 
#### o	Navigation Drawer:
The app uses a Navigation Drawer for smooth, organized navigation between the map view and the list of saved locations. This enhances the user interface by providing a clean and easy-to-access layout for multiple screens. 
#### o	RecyclerView for Saved Locations:
A RecyclerView dynamically displays the list of locations saved by the user. It allows easy browsing of all the places they want to visit, displaying each location's latitude and longitude, and updating in real-time as new locations are added. 
#### o	Real-Time Updates:
The app supports real-time updates using Firestore, meaning that when a user adds a new location, the list is instantly updated without needing to refresh the app. This provides a seamless, interactive experience that ensures users always have the latest data. 
#### o	Location-Based Markers:
When a location is tapped on the map, a marker is placed at the coordinates, and the marker's details are automatically added to the RecyclerView. This visual feedback enhances user interaction and makes it easy to see the locations on both the map and the list.
