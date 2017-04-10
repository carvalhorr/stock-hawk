# Stock Hawk

This is the code for project 3 in Udacity's [Android Developer Nanodegree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801). 

The base code was provided and some bugs needed to be fixed and new functionality was required to be added. 

# Changes made
ssdsdsdsdsd## The app crashed when the user entered a non existing stock ccode.

To fix this problem all the layers in the app where changed to provide a good user experince instead of just preventing the crash from happenning. 

Here are the changes made:
* A new column was added to the database to store whether the code exists or not
* The sync job was also changed to detect the non exsiting stock code and store this information in the database
* The user interface was changed to show the non existing code with a specific style (alowing the user to remove the code and add it again

## Display the stock history

This was accomplished by using a third party library for displaying charts (MPAndroidChart).

## Collection widget for the home screen

A collection widget was created to display the stocks on the home screen. It is updated whenever the sync job get new data or the user add or remove stock codes in the main activity.

## Right to left support

Right to left support was added in order to make the app work on other locates that use right to leftxs

## Accessibility

Content description was added so that the app works correctly with screen readers.
