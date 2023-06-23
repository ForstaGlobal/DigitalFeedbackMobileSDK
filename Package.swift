// swift-tools-version:5.5
import PackageDescription

let package = Package(
    name: "DigitalFeedbackMobileSDK",
    platforms: [
        .iOS(.v12)
    ],
    products: [
        .library(
            name: "ConfirmitMobileSDK", 
            targets: ["ConfirmitMobileSDK"]
        )
    ],
    targets: [
        .binaryTarget(name: "ConfirmitMobileSDK", path: "cocoapod/ConfirmitMobileSDK.xcframework")
    ],
    swiftLanguageVersions: [.v5]
)
