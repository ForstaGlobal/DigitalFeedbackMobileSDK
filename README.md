<br/>
<p align="center">
  <img src="https://forstaglobal.github.io/DigitalFeedbackMobileSDK/assets/logo.png">
</p>

# Digital Feedback Mobile SDK

The Forsta Digital Feedback Mobile SDK provides seamless in-app feedback for any touch point within your mobile application. 

<br/>

## Documentation

Everything you need to get started with integration can be found in the [repository wiki](https://github.com/ForstaGlobal/DigitalFeedbackMobileSDK/wiki).

<br/>

## Setup (Android)

### Prerequisites

* Android API Level 21 or higher (Android 5.0 and above)
* [AndroidX](https://developer.android.com/jetpack/androidx/) enabled
    * More information about setup and migration for AndroidX can be found [here](https://developer.android.com/jetpack/androidx/)
* Dependencies
    
    > These dependencies will be automatically downloaded by Gradle sync
    
    * AndroidX
        * [Room](https://developer.android.com/jetpack/androidx/releases/room) (2.4.1)
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
    implementation 'com.confirmit.mobilesdk:mobilesdk:3.7.1'
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

* Xcode 13 or higher
* Target of iOS 12 or higher
  * For earlier iOS version, please use [v3.5.0](https://github.com/ForstaGlobal/DigitalFeedbackMobileSDK/tree/3.5.0)
* Swift 5.5 or higher

### Installation

#### 1. Using Swift Package Manager

**Step 1.** Add package URL and dependency to `Package.swift`
```swift
// swift-tools-version:5.5

import PackageDescription

let package = Package(
    name: "<Your Product Name>",
    dependencies: [
		.package(url: "https://github.com/FortaGlobal/DigitalFeedbackMobileSDK.git", .upToNextMajor(from: "3.7.1"))
    ],
    targets: [
        .target(
		name: "<Your Target Name>",
		dependencies: ["ConfirmitMobileSDK"]),
    ]
)
```

**Step 2.** Run `swift package resolve`

#### 2. Using CocoaPods

> Requires CocoaPods 1.10.0 or higher

**Step 1.** Add local pod path to the `Podfile`.
```ruby
use_frameworks!
platform :ios, '12.0'

# Your target
target 'MyApp' do
  # ... others pods
  pod 'ConfirmitMobileSDK', '3.7.1'
end
```

**Step 2.** Run `pod update`.

#### 3. Manual Method

**Step 1.** Clone Github repository.

**Step 2.** If the Framework folder doesn't already exist, right-click on your project in the project navigator (top-most entry), and select “New Group”. Name the new group `Frameworks`.

**Step 3.** Drag and drop it from Finder into the Frameworks folder. Make sure that the destination is just under the Frameworks folder before dropping.

**Step 4.** Then, make sure the following options are selected for adding files:
* Both “Copy items if needed” and “Create groups” should be checked and selected. 
* Click Finish.

<br/>