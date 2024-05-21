import ConfirmitMobileSDK
import Foundation
import UIKit

class SurveyManager {
  func displaySurvey(controller: UIViewController, survey: SurveyModel, surveyFrameConfig: SurveyFrameConfig?) {
    let surveyLayout = DefaultLayoutController(nibName: "DefaultLayoutController", bundle: nil)
    surveyLayout.initialize(surveyModel: survey, surveyFrameConfig: surveyFrameConfig)
    controller.present(surveyLayout, animated: true, completion: nil)
  }
}
