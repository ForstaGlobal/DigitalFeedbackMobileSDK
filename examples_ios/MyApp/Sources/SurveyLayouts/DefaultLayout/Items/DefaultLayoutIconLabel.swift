import Foundation
import MobileSdk

class DefaultLayoutIconLabel: DefaultLayoutItem {
    let value: QuestionText
    let iconName: String
    var errors: [ValidationQuestionError]

    init(value: QuestionText, iconName: String) {
        self.value = value
        self.iconName = iconName
        self.errors = []
    }

    init(errors: [ValidationQuestionError]) {
        self.value = QuestionText(rawText: "")
        self.iconName = "outline_sentiment_very_dissatisfied_black_24pt"
        self.errors = errors
    }
}
