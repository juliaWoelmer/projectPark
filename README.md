## Intro  
This program runs an app that will allow users to search for their destination with accessible parking.

## SetUp:  
1. Clone git repository
```
git clone https://github.com/juliaWoelmer/projectPark.git
```
2. Open projectPark folder in Android Studio
3. Add line to local.properties to set up Maps API Key:  
```
MAPS_API_KEY=AIzaSyCsSSHUihBM385wF3MPqzWtaJtmFGuk5nA
```
4. On the top of Android Studio, go to Build and click Make Project. This may take a while.
5. Once the project is done building, go to Run and click Run 'app'. If this is the first time you are using the emulator, the run may timeout. If this happens, click Run 'app' again.
6. To change the emulator's location, follow this tutorial: https://stackoverflow.com/questions/47528006/how-to-set-the-location-manually-in-android-studio-emulator

## Resources:  
1. Use this tutorial link to setup and clone the GitHub repository on VS Code  
   https://code.visualstudio.com/docs/sourcecontrol/github

2. Use this tutorial to set up the live share collaboration feature.  
   https://code.visualstudio.com/learn/collaboration/live-share

3. North Campus Parking Lots for Permits and Visitors  
  https://ltp.umich.edu/parking/locations-and-enforcement/north-campus/

## How to edit the GeoJson Map:  
Upload the most up to date map from the GitHub.  

Lot: What is the lot number  
Spot: Label each spot with their own number starting from 1.  
Tier: What is the permit required to park here. This is listed on the website
listed above for every parking lot. If the spot is a pay to park then the tier
is none. If the spot tier is restricted, write restricted.  
Van_access: Does the parking spot sign say van accessible.  
UofM_permit_req: Does the sign say this spot is enforced by additional u of m
requirements with two symbols. Almost all spots that have a tier will have this
requirement. The parking spot will usually not have additional writing if there
is no requirement.  
Marker: Each spot is marked with one marker.  

## Server Repository
The repo that stores the code we wrote for the server is linked below.
https://github.com/juliaWoelmer/arborParkerBackend

The code in the repo is pushed up to Heroku so you do not need to do anything with this repo 
in order to run the app. It is simply linked here to show the code

The server API endpoints can be reached using this base url
https://arbor-parker-heroku.herokuapp.com


