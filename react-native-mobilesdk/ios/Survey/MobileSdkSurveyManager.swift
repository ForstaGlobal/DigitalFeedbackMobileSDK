import Foundation

class MobileSdkSurveyManager {
  private static var activeSurvey: [String: MobileSdkSurveyWrapper] = [:]
  
  func getSurvey(serverId: String, programKey: String, surveyId: String) -> MobileSdkSurveyWrapper? {
    return MobileSdkSurveyManager.activeSurvey[getKey(serverId: serverId, programKey: programKey, surveyId: surveyId)]
  }
  
  func addSurvey(serverId: String, programKey: String, surveyId: String, survey: MobileSdkSurveyWrapper) {
    MobileSdkSurveyManager.activeSurvey[getKey(serverId: serverId, programKey: programKey, surveyId: surveyId)] = survey
  }
  
  func removeSurvey(serverId: String, programKey: String, surveyId: String) {
    MobileSdkSurveyManager.activeSurvey.removeValue(forKey: getKey(serverId: serverId, programKey: programKey, surveyId: surveyId))
  }
  
  private func getKey(serverId: String, programKey: String, surveyId: String) -> String {
    return "\(serverId)_\(programKey)_\(surveyId)"
  }
}
