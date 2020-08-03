import Foundation
import MobileSdk

enum DefaultLayoutLabelType {
    case text
    case instruction
    case pageTitle
}

class DefaultLayoutLabel: DefaultLayoutItem {
    let value: QuestionText
    let labelType: DefaultLayoutLabelType
    let indent: Int

    init(type: DefaultLayoutLabelType, value: QuestionText, indent: Int = 0) {
        self.labelType = type
        self.value = value
        self.indent = indent
    }
}
