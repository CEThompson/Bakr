# Baker

A Udacity project for viewing baking recipes. This project illustrates finding and handling error cases, adding accessibility features, allowing for localization, adding a widget, and utilizing third-party libraries.

## Installation

Clone this repository and import into **Android Studio**

`git clone https://github.com/CEThompson/udacity-baker.git`

## Notable examples

### Espresso

This project utilizes Espresso to handle testing Retrofit 2 network calls with idling resources. The idling resource is defined [here](https://github.com/CEThompson/udacity-baker/blob/master/app/src/main/java/com/example/android/baking/test/SimpleIdlingResource.java) and usage may be examined in the [test](https://github.com/CEThompson/udacity-baker/blob/master/app/src/androidTest/java/com/example/android/baking/IdlingSelectRecipeScreenTest.java). 

Example usage in production code: https://github.com/CEThompson/udacity-baker/blob/4f95d8faee1d489e36eb76f7918b5497375a123a/app/src/main/java/com/example/android/baking/fragments/SelectRecipeFragment.java#L153-L179

### Usage of MediaPlayer / Exoplayer

This project uses Exoplayer to display steps in baking recipes. 

### Widget

This project includes implementation of a widget.

### Usage of third-party libraries

The following libraries were used in executing the project:

* Butterknife for view binding
* Picasso for image loading
* Retrofit 2 to retrieve JSON formatted baking recipes
* Exoplayer to display videos
* Logger for debug logging
* Gson for JSON object deserialization

### Fragment

The application uses fragments to manage UI implementation.









