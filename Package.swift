// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "ConfirmitMobileSDK",
    platforms: [
        .iOS(.v12)
    ],
    products: [
        .library(
            name: "ConfirmitMobileSDK", 
            targets: ["ConfirmitMobileSDK"])
    ],
    targets: [
        .binaryTarget(name: "ConfirmitMobileSDK", path: "cocoapod/ConfirmitMobileSDK.xcframework")
    ],
    swiftLanguageVersions: [.v5])
