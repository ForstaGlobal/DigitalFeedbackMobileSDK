import Foundation
import ConfirmitMobileSDK
import UIKit

protocol SurveyLayoutControllerDelegate: class {
    func onCloseError(error: String)
}

class SurveyLayoutController: UIViewController {
    let surveyFrame = SurveyFrame()
    
    var surveyModel: SurveyModel!
    var surveyFrameConfig: SurveyFrameConfig?
    
    weak var layoutDelegate: SurveyLayoutControllerDelegate?
    
    func initialize(surveyModel: SurveyModel, surveyFrameConfig: SurveyFrameConfig? = nil) {
        self.surveyModel = surveyModel
        self.surveyFrameConfig = surveyFrameConfig
    }
    
    func loadFrame(surveyFrameDelegate: SurveyFrameDelegate) {
        do {
            if surveyFrameConfig == nil {
                surveyFrameConfig = SurveyFrameConfig(serverId: surveyModel.survey.serverId, surveyId: surveyModel.survey.surveyId)
            }
            
            surveyFrameConfig!.languageId = surveyModel.selectedLanguage.id
            surveyFrameConfig!.respondentValues = surveyModel.getRespondentValues()
            
            try surveyFrame.load(config: surveyFrameConfig!)
            surveyFrame.delegate = surveyFrameDelegate
            _ = surveyFrame.start()
        } catch let error {
            errorCloseSurvey(error: error)
        }
    }
    
    func closeSurvey() {
        dismissKeyboard()
        dismiss(animated: true)
    }
    
    func errorCloseSurvey(message: String) {
        DispatchQueue.main.async {
            self.dismiss(animated: true) {
                self.layoutDelegate?.onCloseError(error: message)
            }
        }
    }
    
    func errorCloseSurvey(error: Error) {
        DispatchQueue.main.async {
            self.dismiss(animated: true) {
                self.layoutDelegate?.onCloseError(error: "\(error)")
            }
        }
    }
}
