import ConfirmitMobileSDK
import Foundation

class DefaultLayoutIconLabel: DefaultLayoutItem {
  let value: QuestionText
  let iconName: String
  var errors: [ValidationQuestionError]

  init(value: QuestionText, iconName: String) {
    self.value = value
    self.iconName = iconName
    errors = []
  }

  init(errors: [ValidationQuestionError]) {
    value = QuestionText(rawText: "")
    iconName = "outline_sentiment_very_dissatisfied_black_24pt"
    self.errors = errors
  }
}
