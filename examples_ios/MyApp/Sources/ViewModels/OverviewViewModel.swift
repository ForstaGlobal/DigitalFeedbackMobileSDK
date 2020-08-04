import Foundation
import ConfirmitMobileSDK

class OverviewViewModel {
    private(set) var survey: SurveyModel!
    
    init(serverId: String, surveyId: String) throws {
        survey = try SurveyModel(serverId: serverId, surveyId: surveyId)
    }
    
    func updateSelectedLanguage(langId: Int) {
        if let language = self.survey.survey.languages.first(where: { $0.id == langId }) {
            survey.selectedLanguage = language
        }
    }
    
    func removeSurvey() -> Bool {
        do {
            try SurveySDK.deleteSurvey(serverId: survey.survey.serverId, surveyId: survey.survey.surveyId)
            return true
        } catch {
            return false
        }
    }
}
