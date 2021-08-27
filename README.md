# RSSI Logger
RSSI Logger is a simple app that can read out RSSI values over time and store them on your Android smartphone. The app is not on the Google Play Store and can only be downloaded from the releases pages.

Build the app yourself using [Android Studio](https://developer.android.com/studio). Its recommended to use Android Studio 2020.3.1 and Java 1.8+. Since the newest Java 11 did not pass Google Play Protect and did not install properly, we accept any Pull Request for developers who know how to fix this problem.

## The App
### Installation
[Download v1.1 August 2021 edition from Releases](https://github.com/iamphu/RSSI_Logger/releases/tag/v1.1)

[Download v1.0 September 2020 edition from Releases](https://github.com/iamphu/RSSI_Logger/releases/tag/v1.0) - this is the old version!

After downloading the apk file on your smartphone, you can start with installing the app. If this is your first time downloading an apk outside of the Google Play Store, you will get a warning that the install is blocked. If this is the case, then you have to go to **Settings** -> **Security**, and allow the installation of apps from unknown sources. After downloading the app you can turn this off again. As the app is not on the Google Play Store, the Play Protect may kick in as it is does not recognize this app. Once you approve the installation the app should be ready to use.

This is another way to turn off Google Play Protect. Make sure to re-enable it after you're done using this app!
https://www.technipages.com/how-to-enable-disable-google-play-protect-in-android

### Permissions
RSSI Logger only requires two permissions, namely:
* `android.permission.ACCESS_WIFI_STATE` - Allows the app to have access to information about Wi-Fi networks
* `android.permission.WRITE_EXTERNAL_STORAGE` - Allows the app to write the data to the external storage for easier file transfers

When the app is first opened, the app requests permission to write to the external storage. Approving it will allow the app to function normally, refusing the permission means that the app is unable to store data and it will close itself to avoid any conflicts later on.

## First Use
The UI is straightforward; start measuring, saving the data and clear data. Clicking the start measuring button will start a scheduler to read out the RSSI value every **0.5 seconds** (feel free to change this in `measurePeriod` (milliseconds) in `MainActivity`, but beware that RSSI might not change very fast.). 
The app will continue measuring until the user stops the measurement. The user can give a description of the measurement to the filename, after which it will save in the default external directory. A possible path to the saved data is: `/Android/data/com.adhocnetworks.rssi_logger/files/`. Another way to find the path is to search for the filename which will shortly show up on the bottom of the screen right after saving the file. After everything has been saved, the clear button can be used to clear all old data and start a new measurement.

<img src="https://user-images.githubusercontent.com/6005355/131145253-400a7b8d-ab9b-4429-be6b-93862954a3a7.png" alt="RSSI Logger app" width="40%"/>


> **This app is made for students following the course ET4388 Ad-hoc Networks 2020-2022 at the Delft University of Technology**

## Author 
The main author is Phu Nguyen  and it's been slightly refactored by David Zwart.
