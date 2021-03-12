<br/>
<p align="center">
  <img src="https://confirmit.github.io/DigitalFeedbackMobileSDK/assets/logo.png">
</p>
<br/>

# Digital Feedback Mobile SDK

The Confirmit Digital Feedback Mobile SDK provides seamless in-app feedback for any touch point within your mobile application. 

<br/>

## Documentation

Everything you need to get started with integration can be found in the [repository wiki](https://github.com/Confirmit/DigitalFeedbackMobileSDK/wiki).

<br/>

## Setup (Android)

### Prerequisites

* Android API Level 21 or higher (Android 5.0 and above)
* [AndroidX](https://developer.android.com/jetpack/androidx/) enabled
    * More information about setup and migration for AndroidX can be found [here](https://developer.android.com/jetpack/androidx/)
* Dependencies
    
    > These dependencies will be automatically downloaded by Gradle sync
    
    * AndroidX
        * [Room](https://developer.android.com/jetpack/androidx/releases/room) (2.2.5)
        * [Appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat) (1.2.0)
        * [WebKit](https://developer.android.com/jetpack/androidx/releases/webkit) (1.3.0)
    * [Gson](https://github.com/google/gson) (2.8.5)
    * [Mozilla Rhino](https://github.com/mozilla/rhino) (1.7.12)


### Installation

Add the SDK dependency to the application level `build.gradle` file.
```gradle
// build.gradle (Module)
dependencies {
    ...
    implementation 'com.confirmit.mobilesdk:mobilesdk:3.3.0'
}
```

Once you make these two changes, simply refresh / sync your gradle dependencies.


### R8 / ProGuard

If you are using R8, the shrinking and obfuscation rules will be included automatically.

ProGuard users must manually add following rules.

```gradle
# Rhino
-keep class org.mozilla.javascript.** { public *; }
-dontwarn org.mozilla.javascript.**
```

<br/>

## Setup (iOS)

### Prerequisites

* Xcode 11 or higher
* Target of iOS 10 or higher
* Swift 5.1 or higher

* Dependencies

    > These dependencies will be installed during CocoaPods install. If your project doesn't use CocoaPods, please install dependencies manually
    
    * [SSZipArchive](https://github.com/ZipArchive/ZipArchive) (2.2.2)


### Installation

#### 1. Using CocoaPods

> Requires CocoaPods 1.10.0 or higher

**Step 1.** Add local pod path to the `Podfile`.
```ruby
use_frameworks!
platform :ios, '10.0'

# Your target
target 'MyApp' do
  # ... others pods
  pod 'ConfirmitMobileSDK', '3.3.0'
end
```


**Step 2.** Run `pod update`.

#### 2. Manual Method

**Step 1.** Clone Github repository.

**Step 2.** If the Framework folder doesn't already exist, right-click on your project in the project navigator (top-most entry), and select “New Group”. Name the new group `Frameworks`.

**Step 3.** Drag and drop it from Finder into the Frameworks folder. Make sure that the destination is just under the Frameworks folder before dropping.

**Step 4.** Then, make sure the following options are selected for adding files:
* Both “Copy items if needed” and “Create groups” should be checked and selected. 
* Click Finish.

<br/>