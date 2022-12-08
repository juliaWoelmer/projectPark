## Arbor Parker: Privacy Policy 
Welcome to Arbor Parker for Android!

This is an Android app developed by the Arbor Parker team and will be available on Google Play.

The app must store user information regarding their username, password and what parking spot they are occupying if they are in one.

The user has the option to customize their profile with information that will be stored in the databse such as their first name, last name, email, whether they prefer dark mode enabled in the app and whether they need van accessible parking. 

The list of permissions that the app will use are listed below:

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
    
    
| Permission | Why it is required |
| :---: | --- |
| `android.permission.INTERNET` | This permission is required in order for the user to login to the app, create an account, search a destination or navigate to a destination. The user needs internet access in order to use several functions of the app. |
| `android.permission.ACCESS_COARSE_LOCATION` | This permission is required in order for the app to use the users location in navigation. It provides a rough estimate of where the user is located. The user has the option to decline the app using their location but the navigation system will not start where the user is located.|
| `android.permission.ACCESS_FINE_LOCATION` | This permission is required in order for the app to use the users location in navigation. It provides a more accurate estimate of where the user is located. The user has the option to decline the app using their location but the navigation system will not start where the user is located.|
| `android.permission.ACCESS_BACKGROUND_LOCATION` | This permission is required in order for the app to use the users location in navigation while the app is running in the background. It makes sure the users location is being updated even if they open another application. The user has the option to decline the app using their location but the navigation system will not start where the user is located.|
