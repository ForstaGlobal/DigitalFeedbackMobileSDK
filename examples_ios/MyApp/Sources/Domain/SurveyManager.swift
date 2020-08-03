import Foundation
import MobileSdk
import UIKit

class SurveyManager {
    func launchSurvey(controller: UIViewController,
                      layoutDelegate: SurveyLayoutControllerDelegate,
                      survey: SurveyModel,
                      surveyFrameConfig: SurveyFrameConfig? = nil) {
        if AppConfigs.onDemandDownloadPackage {
            Utils.showLoading(controller: controller) { loading in
                SurveySDK.downloadSurvey(serverId: survey.survey.serverId, surveyId: survey.survey.surveyId) { result, _ in
                    DispatchQueue.main.async {
                        loading.dismiss(animated: false) {
                            if result {
                                self.displaySurvey(controller: controller, layoutDelegate: layoutDelegate, survey: survey, surveyFrameConfig: surveyFrameConfig)
                            } else {
                                Utils.showError(controller: controller, error: "Failed to update survey")
                            }
                        }
                    }
                }
            }
        } else {
            displaySurvey(controller: controller, layoutDelegate: layoutDelegate, survey: survey, surveyFrameConfig: surveyFrameConfig)
        }
    }
    
    private func displaySurvey(controller: UIViewController, layoutDelegate: SurveyLayoutControllerDelegate, survey: SurveyModel, surveyFrameConfig: SurveyFrameConfig?) {
        var surveyLayout: SurveyLayoutController?
        switch survey.configs.surveyLayout {
        case .default:
            surveyLayout = DefaultLayoutController(nibName: "DefaultLayoutController", bundle: nil)
        case .card:
            surveyLayout = SimpleCardController(nibName: "SimpleCardController", bundle: nil)
            surveyLayout?.modalPresentationStyle = .overFullScreen
        case .simpleFeedback:
            surveyLayout = SimpleFbController(nibName: "SimpleFbController", bundle: nil)
        }
        
        if let surveyLayout = surveyLayout {
            surveyLayout.initialize(surveyModel: survey, surveyFrameConfig: surveyFrameConfig)
            surveyLayout.layoutDelegate = layoutDelegate
            controller.present(surveyLayout, animated: true, completion: nil)
        }
    }
}
