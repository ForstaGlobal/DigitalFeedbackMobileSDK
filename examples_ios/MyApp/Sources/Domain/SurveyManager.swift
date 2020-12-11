import ConfirmitMobileSDK
import Foundation
import UIKit

class SurveyManager {
  func launchSurvey(controller: UIViewController,
                    survey: SurveyModel,
                    surveyFrameConfig: SurveyFrameConfig? = nil) {
    Utils.showLoading(controller: controller) { loading in
      SurveySDK.downloadSurvey(serverId: survey.survey.serverId, surveyId: survey.survey.surveyId) { result, _ in
        DispatchQueue.main.async {
          loading.dismiss(animated: false) {
            if result {
              self.displaySurvey(controller: controller, survey: survey, surveyFrameConfig: surveyFrameConfig)
            } else {
              Utils.showError(controller: controller, error: "Failed to update survey")
            }
          }
        }
      }
    }

    // NOTE: Or you can skip download dynamically. Because program script will also download survey by "ctx.downloadSurvey()"
    // And display survey directly
  }

  private func displaySurvey(controller: UIViewController, survey: SurveyModel, surveyFrameConfig: SurveyFrameConfig?) {
    let surveyLayout = DefaultLayoutController(nibName: "DefaultLayoutController", bundle: nil)
    surveyLayout.initialize(surveyModel: survey, surveyFrameConfig: surveyFrameConfig)
    controller.present(surveyLayout, animated: true, completion: nil)
  }
}
