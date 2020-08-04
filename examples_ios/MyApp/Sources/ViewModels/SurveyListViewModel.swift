import Foundation
import ConfirmitMobileSDK

typealias TableDataProcessCompletion = (_ hasUpdate: Bool) -> Void

class SurveyListViewModel {
    var surveys: [SurveyModel] = []
    var selectedSurvey: SurveyModel?
    
    init() {
        do {
            for survey in try SurveySDK.getSurveyList() {
                surveys.append(try SurveyModel(survey: survey))
            }
        } catch {}
    }
    
    func reloadSurveys() {
        do {
            var exists = [String]()
            let curSurveys = try SurveySDK.getSurveyList()
            for survey in curSurveys {
                exists.append("\(survey.serverId)_\(survey.surveyId)")
            }
            
            surveys.removeAll {
                let key = "\($0.survey.serverId)_\($0.survey.surveyId)"
                return !exists.contains(key)
            }
            
            processSurveys(updatedSurvey: curSurveys) { _ in }
        } catch {}
    }
    
    func downloadAll(completion: @escaping TableDataProcessCompletion) {
        let dispatchGroup = DispatchGroup()
        
        var updatedSurveys = [Survey]()
        for survey in surveys {
            dispatchGroup.enter()
            let serverId = survey.survey.serverId
            let surveyId = survey.survey.surveyId
            SurveySDK.downloadSurvey(serverId: serverId, surveyId: surveyId) { result, _ in
                if result {
                    do {
                        if let updated = try SurveySDK.getSurvey(serverId: serverId, surveyId: surveyId) {
                            updatedSurveys.append(updated)
                        }
                    } catch {
                        // Log
                    }
                }
                dispatchGroup.leave()
            }
        }
        
        dispatchGroup.notify(queue: .main, work: DispatchWorkItem(block: {
            self.processSurveys(updatedSurvey: updatedSurveys, completion: completion)
        }))
    }
    
    func download(serverId: String, surveyId: String, completion: @escaping TableDataProcessCompletion) {
        SurveySDK.downloadSurvey(serverId: serverId, surveyId: surveyId) { result, _ in
            if result {
                do {
                    if let updated = try SurveySDK.getSurvey(serverId: serverId, surveyId: surveyId) {
                        self.processSurveys(updatedSurvey: [updated], completion: completion)
                    }
                } catch {
                    // Log
                }
            }
        }
    }
    
    private func processSurveys(updatedSurvey: [Survey], completion: @escaping TableDataProcessCompletion) {
        for survey in updatedSurvey {
            if let existIndex = self.surveys.firstIndex(where: { (model) -> Bool in
                survey.serverId == model.survey.serverId && survey.surveyId == model.survey.surveyId
            }) {
                surveys[existIndex].update(survey: survey)
            } else {
                do {
                    surveys.append(try SurveyModel(survey: survey))
                } catch {}
            }
        }
        
        completion(updatedSurvey.count > 0)
    }
}
