import ConfirmitMobileSDK
import Foundation

class SurveyModel {
  private(set) var survey: Survey
  var selectedLanguage: SurveyLanguage

  convenience init(serverId: String, surveyId: String) throws {
    guard let survey = try SurveySDK.getSurvey(serverId: serverId, surveyId: surveyId) else {
      throw AppError.general("Survey is not available")
    }

    try self.init(survey: survey)
  }

  init(survey: Survey) throws {
    self.survey = survey
    selectedLanguage = try survey.getDefaultLanguage()
  }

  func update(survey: Survey) {
    self.survey = survey
  }
}
