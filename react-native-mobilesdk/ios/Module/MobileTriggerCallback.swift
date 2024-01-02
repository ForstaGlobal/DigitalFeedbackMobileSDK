import ConfirmitMobileSDK
import Foundation

class MobileTriggerCallback: ProgramCallback {
  private let serverId: String
  private let programKey: String
  
  init(serverId: String, programKey: String) {
    self.serverId = serverId
    self.programKey = programKey
  }
  
  func onSurveyDownloadCompleted(triggerInfo: ConfirmitMobileSDK.TriggerInfo, surveyId: String, error: Error?) {}
  
  func onSurveyStart(config: ConfirmitMobileSDK.SurveyFrameConfig) {}
  
  func onScenarioLoad(triggerInfo: ConfirmitMobileSDK.TriggerInfo, error: Error?) {
    SdkEmitter.shared.sendEvent(withName: "__mobileOnScenarioLoad", body: [
      "serverId": serverId,
      "programKey": programKey,
      "error": error?.localizedDescription ?? ""
    ])
  }
  
  func onScenarioError(triggerInfo: ConfirmitMobileSDK.TriggerInfo, error: Error) {
    SdkEmitter.shared.sendEvent(withName: "__mobileOnScenarioError", body: [
      "serverId": serverId,
      "programKey": programKey,
      "error": error.localizedDescription
    ])
  }
  
  func onAppFeedback(triggerInfo: ConfirmitMobileSDK.TriggerInfo, data: [String: String?]) {}
  
  func onWebSurveyStart(surveyWebView: ConfirmitMobileSDK.SurveyWebViewController) {
    surveyWebView.getSurveyUrl { url, _ in
      guard let token = url?.token, let url = url?.url else {
        return
      }
      
      SdkEmitter.shared.sendEvent(withName: "__mobileOnWebSurveyStart", body: [
        "serverId": self.serverId,
        "programKey": self.programKey,
        "token": token,
        "url": url
      ])
    }
  }
}
