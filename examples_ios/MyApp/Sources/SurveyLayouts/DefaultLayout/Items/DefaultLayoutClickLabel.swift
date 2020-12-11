import ConfirmitMobileSDK
import Foundation

enum DefaultLayoutClickLabelType {
  case radio
  case checkbox
}

class DefaultLayoutClickLabel: DefaultLayoutItem {
  let clickLabelType: DefaultLayoutClickLabelType
  let indent: Int
  let answer: QuestionAnswer

  init(type: DefaultLayoutClickLabelType, initial: Bool, indent: Int, answer: QuestionAnswer) {
    clickLabelType = type
    self.indent = indent
    self.answer = answer
  }
}
