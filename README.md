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
6. Change the emulator's location to somewhere in Ann Arbor by following this tutorial: 
https://stackoverflow.com/questions/47528006/how-to-set-the-location-manually-in-android-studio-emulator. 
Because setting the GPS location of an emulator in Android Studio can be buggy, here are some tips.
If a route generated does not start and end in Ann Arbor or if clicking on Get Directions takes a 
very long time to load then that likely means that the emulator is not properly set to an 
Ann Arbor location. A good practice is to open maps on the emulator and have it navigate to the 
emulator's current location before launching the app in order to make sure the emulator location 
is properly set. If you search for a route and the route does not begin and end in ann arbor 
then close the Arbor Parker app, reset the emulator location, check that the location is correct 
on the maps app and then relaunch the Arbor Parker app.
7. If the app fails to download due to the emulator not having enough space then close the emulator,
click on the three dots next to the emulator in the device manager, and wipe data.

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


