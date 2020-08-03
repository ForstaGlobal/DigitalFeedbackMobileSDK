import Foundation
import UIKit

enum AppError: Error {
    case general(String)
}

enum SurveyLayout: String {
    case `default` = "Default"
    case card = "Card"
    case simpleFeedback = "Simple Feedback"

    static func toStringArray() -> [String] {
        return [
            SurveyLayout.default.rawValue,
            SurveyLayout.card.rawValue,
            SurveyLayout.simpleFeedback.rawValue
        ]
    }
}

enum ConfigKey: String {
    case onDemandDownloadPackage
    case validateOnAnswerChange
    case uniqueId
    case htmlText
    case uploadIncomplete
    case surveyLayout
    case respondentValue
    case triggerTestMode
    case customData
}

enum ScreenType: String {
  case phone
  case tablet
}
