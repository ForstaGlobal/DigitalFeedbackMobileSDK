import Foundation
import MobileSdk

class SurveyModel {
    private(set) var survey: Survey
    var selectedLanguage: SurveyLanguage
    private(set) var configs: SurveyConfigs
    
    convenience init(serverId: String, surveyId: String) throws {
        guard let survey = try SurveySDK.getSurvey(serverId: serverId, surveyId: surveyId) else {
            throw AppError.general("Survey is not available")
        }
        
        try self.init(survey: survey)
    }
    
    init(survey: Survey) throws {
        self.survey = survey
        self.selectedLanguage = try survey.getDefaultLanguage()
        self.configs = SurveyConfigs(serverId: survey.serverId, surveyId: survey.surveyId)
    }
    
    func update(survey: Survey) {
        self.survey = survey
    }
    
    func getRespondentValues() -> [String: String] {
        var values = [String: String]()
        let raw = configs.respondentValue
        for pair in raw.split(separator: ";") {
            let keyValue = pair.split(separator: "=")
            if keyValue.count == 2 {
                let key = String(keyValue[0])
                let value = String(keyValue[1])
                values[key] = value
            }
        }
        return values
    }
}
