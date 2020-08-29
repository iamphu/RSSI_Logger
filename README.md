# RSSI Logger
RSSI Logger is a simple app that can read out RSSI values over time and store them on your Android smartphone. The app is not on the Google Play Store and can only be downloaded on this Github page or by building the code yourself. You can build the code by using [Android Studio](https://developer.android.com/studio).

## The App
### Installation
The app can be found under the name `RSSI_Logger.apk`. After downloading the apk file on your smartphone, you can start with installing the app. If this is your first time downloading an apk outside of the Google Play Store, you will get a warning that the install is blocked. If this is the case, then you have to go to **Settings** -> **Security**, and allow the installation of apps from unknown sources. After downloading the app you can turn this off again. As the app is not on the Google Play Store, the Play Protect may kick in as it is does not recognize this app. Once you approve the installation the app should be ready to use.

### Permissions
RSSI Logger only requires two permissions, namely:
* `android.permission.ACCESS_WIFI_STATE` - Allows the app to have access to information about Wi-Fi networks
* `android.permission.WRITE_EXTERNAL_STORAGE` - Allows the app to write the data to the external storage for easier file transfers

When the app is first opened, the app requests permission to write to the external storage. Approving it will allow the app to function normally, refusing the permission means that the app is unable to store data and it will close itself to avoid any conflicts later on.

## First Use
The UI is straightforward; start measuring, saving the data and clear data. Clicking the start measuring button will start a scheduler to read out the RSSI value every **0.5 seconds** (this can be changed in the code itself). The app will continue measuring until the user stops the measurement. The user can give a description of the measurement to the filename, after which it will save in the default external directory. A possible path to the saved data is: `/Android/data/com.adhocnetworks.rssi_logger/files/`. Another way to find the path is to search for the filename which will shortly show up on the bottom of the screen right after saving the file. After everything has been saved, the clear button can be used to clear all old data and start a new measurement.



<img src=https://i.imgur.com/ksw3j2z.jpg width="40%">

> **This app is made for students following the course ET4388 Ad-hoc Networks 2020-2021 at the Delft University of Technology**
