import Foundation
import ConfirmitMobileSDK

class MobileSdkSurveyWrapper {
  var surveyFrame: SurveyFrame? = SurveyFrame()
  private let config: SurveyFrameConfig
  private let surveyManager = MobileSdkSurveyManager()
  
  init(config: SurveyFrameConfig) {
    self.config = config
  }
  
  func startSurvey(customData: [String:String], respondentValues: [String: String]) throws -> SurveyFrameActionResult? {
    for data in customData {
      config.customData[data.key] = data.value
    }
    
    config.respondentValues = respondentValues
    surveyFrame?.delegate = self
    try surveyFrame?.load(config: config)
    return surveyFrame?.start()
  }
}

extension MobileSdkSurveyWrapper: SurveyFrameDelegate {
  func onSurveyPageReady(page: ConfirmitMobileSDK.SurveyPage) {
    SdkEmitter.shared.sendEvent(withName: "__mobileOnSurveyPageReady", body: [
      "serverId": config.serverId,
      "programKey": config.programKey,
      "surveyId": config.surveyId,
      "forwardText": page.forwardText,
      "backwardText": page.backwardText,
      "okText": page.okText,
      "showForward": page.showForward,
      "showBackward": page.showBackward
    ])
  }
  
  func onSurveyErrored(page: ConfirmitMobileSDK.SurveyPage, values: [String : String?], error: any Error) {
    SdkEmitter.shared.sendEvent(withName: "__mobileOnSurveyErrored", body: [
      "forwardText": page.forwardText,
      "backwardText": page.backwardText,
      "okText": page.okText,
      "showForward": page.showForward,
      "showBackward": page.showBackward,
      "error": [
        "message": error.localizedDescription
      ],
      "values": values
    ])
  }
  
  func onSurveyFinished(page: ConfirmitMobileSDK.SurveyPage, values: [String : String?]) {
    SdkEmitter.shared.sendEvent(withName: "__mobileOnSurveyFinished", body: [
      "forwardText": page.forwardText,
      "backwardText": page.backwardText,
      "okText": page.okText,
      "showForward": page.showForward,
      "showBackward": page.showBackward,
      "values": values
    ])
    
    cleanupSurvey()
  }
  
  func onSurveyQuit(values: [String : String?]) {
    SdkEmitter.shared.sendEvent(withName: "__mobileOnSurveyQuit", body: [
      "values": values
    ])
    
    cleanupSurvey()
  }

  private func cleanupSurvey() {
    surveyFrame?.delegate = nil
    surveyFrame = nil
    surveyManager.removeSurvey(serverId: config.serverId, programKey: config.programKey ?? "", surveyId: config.surveyId)
  }
}
