# Baker

A project for viewing baking recipes. This project illustrates finding and handling error cases, adding accessibility features, allowing for localization, adding a widget, and utilizing third-party libraries.

## Installation

Clone this repository and import into **Android Studio**.

`git clone https://github.com/CEThompson/udacity-baker.git`

## Notable examples

#### Testing network calls with Espresso

This project utilizes Espresso to handle testing Retrofit 2 network calls with idling resources. The [idling resource](https://github.com/CEThompson/udacity-baker/blob/master/app/src/main/java/com/example/android/baking/test/SimpleIdlingResource.java) used in the [test](https://github.com/CEThompson/udacity-baker/blob/master/app/src/androidTest/java/com/example/android/baking/IdlingSelectRecipeScreenTest.java) triggers Espresso to wait for the retrofit call to finish before interacting with the UI.  

To see the resource in context view its usage in the [network call](https://github.com/CEThompson/udacity-baker/blob/4f95d8faee1d489e36eb76f7918b5497375a123a/app/src/main/java/com/example/android/baking/fragments/SelectRecipeFragment.java#L153-L179).

`mIdlingResource.setIdleState(true);`

#### Retrieving JSON objects with Retrofit 2
The network call returns a JSON response translated into an array of [Recipe](https://github.com/CEThompson/udacity-baker/blob/master/app/src/main/java/com/example/android/baking/data/Recipe.java) objects. This is handled by a very [simple interface declaration](https://github.com/CEThompson/udacity-baker/blob/master/app/src/main/java/com/example/android/baking/services/GetRecipesService.java) thanks to the use of Retrofit 2.

#### Using Exoplayer to display videos
Recipes are further decomposed into two objects, [Ingredients](https://github.com/CEThompson/udacity-baker/blob/master/app/src/main/java/com/example/android/baking/data/Ingredient.java) and [Steps](https://github.com/CEThompson/udacity-baker/blob/master/app/src/main/java/com/example/android/baking/data/Step.java). Links to video resources extracted from the steps are [used by](https://github.com/CEThompson/udacity-baker/blob/b1f44d60fb5b061bf4a53e4c17321a92119bff55/app/src/main/java/com/example/android/baking/fragments/ViewStepFragment.java#L217-L245) Exoplayer to display content to the viewer.

#### Widget saves a recipe
This project includes implementation of a widget which saves the ingredients from a recipe.

#### Usage of third-party libraries

Third-party libraries were used for implementation of the project:
* Butterknife for view binding 
* Retrofit 2 to retrieve JSON formatted baking recipes
* Exoplayer to display videos
* Logger for debug logging
* Gson for JSON object deserialization

#### Fragments used to handle mobile and tablet layouts
This application uses fragments to implement a master/detail list control flow with layouts defined for both mobile and tablet.

## Acknowledgements
* Thanks to Udacity
* Thanks to Jake Wharton, Retrofit, Exoplayer, and all those who supported the dependencies used in this project









