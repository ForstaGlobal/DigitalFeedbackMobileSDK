import Foundation

@objc(SdkEmitter)
class SdkEmitter: RCTEventEmitter {
  private static var instance: SdkEmitter?
  static var shared: SdkEmitter {
    if let instance = instance {
      return instance
    }

    fatalError("Trigger callback haven't been initialized yet")
  }

  override class func requiresMainQueueSetup() -> Bool {
    return true
  }

  override init() {
    super.init()
    SdkEmitter.instance = self
  }

  override func supportedEvents() -> [String]! {
    return ["__mobileOnWebSurveyStart", "__mobileOnSurveyStart", "__mobileOnScenarioError", "__mobileOnScenarioLoad", "__mobileOnSurveyClosed", "__mobileOnSurveyErrored", "__mobileOnSurveyFinished", "__mobileOnSurveyPageReady", "__mobileOnSurveyQuit"]
  }
}
