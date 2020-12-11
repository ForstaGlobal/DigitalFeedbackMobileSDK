import ConfirmitMobileSDK
import Foundation
import UIKit

class SurveyLayoutController: UIViewController {
  let surveyFrame = SurveyFrame()

  var surveyModel: SurveyModel!
  var surveyFrameConfig: SurveyFrameConfig?

  func initialize(surveyModel: SurveyModel, surveyFrameConfig: SurveyFrameConfig? = nil) {
    self.surveyModel = surveyModel
    self.surveyFrameConfig = surveyFrameConfig
  }

  func loadFrame(surveyFrameDelegate: SurveyFrameDelegate) {
    do {
      if surveyFrameConfig == nil {
        surveyFrameConfig = SurveyFrameConfig(serverId: surveyModel.survey.serverId, surveyId: surveyModel.survey.surveyId)
      }

      // Set languages
      surveyFrameConfig!.languageId = surveyModel.selectedLanguage.id

      try surveyFrame.load(config: surveyFrameConfig!)
      surveyFrame.delegate = surveyFrameDelegate
      _ = surveyFrame.start()
    } catch {
      errorCloseSurvey(error: error)
    }
  }

  func closeSurvey() {
    dismissKeyboard()
    dismiss(animated: true)
  }

  func errorCloseSurvey(message: String) {
    let parentController = parent
    DispatchQueue.main.async {
      self.dismiss(animated: true) {
        if let parentController = parentController {
          Utils.showError(controller: parentController, error: message)
        }
      }
    }
  }

  func errorCloseSurvey(error: Error) {
    let parentController = parent
    DispatchQueue.main.async {
      self.dismiss(animated: true) {
        if let parentController = parentController {
          Utils.showError(controller: parentController, error: "\(error)")
        }
      }
    }
  }
}
