import ConfirmitMobileSDK
import Foundation

enum DefaultLayoutItemFactory {
  private static func buildCommon(question: DefaultQuestion) -> [DefaultLayoutItem] {
    var result = [DefaultLayoutItem]()
    if !question.title.isEmpty {
      result.append(DefaultLayoutIconLabel(value: question.title, iconName: "outline_assignment_black_24pt"))
    }
    if !question.text.isEmpty {
      result.append(DefaultLayoutLabel(type: .text, value: question.text))
    }

    if !question.instruction.isEmpty {
      result.append(DefaultLayoutLabel(type: .instruction, value: question.instruction))
    }

    if question.errors.count > 0 {
      result.append(DefaultLayoutIconLabel(errors: question.errors))
    }

    return result
  }

  static func createPageHeader(page: SurveyPage) -> [DefaultLayoutItem] {
    var result = [DefaultLayoutItem]()
    if !page.title.isEmpty {
      result.append(DefaultLayoutLabel(type: .pageTitle, value: page.title))
    }

    return result
  }

  static func createInfo(question: InfoQuestion) -> [DefaultLayoutItem] {
    var result = [DefaultLayoutItem]()
    if !question.title.isEmpty {
      result.append(DefaultLayoutIconLabel(value: question.title, iconName: "outline_assignment_black_24pt"))
    }
    if !question.text.isEmpty {
      result.append(DefaultLayoutLabel(type: .text, value: question.text))
    }

    if !question.instruction.isEmpty {
      result.append(DefaultLayoutLabel(type: .instruction, value: question.instruction))
    }

    return result
  }

  static func createText(question: TextQuestion) -> [DefaultLayoutItem] {
    var result = buildCommon(question: question)
    result.append(DefaultLayoutTextBox(type: .text, initialValue: question.getValue() ?? ""))
    return result
  }

  static func createNumeric(question: NumericQuestion) -> [DefaultLayoutItem] {
    var result = buildCommon(question: question)
    result.append(DefaultLayoutTextBox(type: .numeric, initialValue: question.getValue() ?? ""))
    return result
  }

  static func createNotSupported() -> [DefaultLayoutItem] {
    var result = [DefaultLayoutItem]()
    result.append(DefaultLayoutIconLabel(value: QuestionText(rawText: "Question is not supported"), iconName: "outline_error_outline_black_24pt"))
    return result
  }

  static func createSingle(question: SingleQuestion) -> [DefaultLayoutItem] {
    var result = buildCommon(question: question)

    var selectedCode = ""
    if let selectedAnswer = question.selected() {
      selectedCode = selectedAnswer.code
    }

    for answer in question.answers {
      if answer.isHeader {
        if !answer.text.isEmpty {
          result.append(DefaultLayoutLabel(type: .text, value: answer.text, indent: 1))
        }
      } else {
        result.append(DefaultLayoutClickLabel(type: .radio,
                                              initial: selectedCode == answer.code,
                                              indent: 1,
                                              answer: answer))
      }

      // Nested answer always not group header
      for nestedAnswer in answer.answers {
        result.append(DefaultLayoutClickLabel(type: .radio,
                                              initial: selectedCode == nestedAnswer.code,
                                              indent: 2,
                                              answer: nestedAnswer))
      }
    }

    return result
  }

  static func createMulti(question: MultiQuestion) -> [DefaultLayoutItem] {
    var result = buildCommon(question: question)

    for answer in question.answers {
      if answer.isHeader {
        if !answer.text.isEmpty {
          result.append(DefaultLayoutLabel(type: .text, value: answer.text, indent: 1))
        }
      } else {
        result.append(DefaultLayoutClickLabel(type: .checkbox,
                                              initial: question.get(answer: answer),
                                              indent: 1,
                                              answer: answer))
      }

      // Nested answer always not group header
      for nestedAnswer in answer.answers {
        result.append(DefaultLayoutClickLabel(type: .checkbox,
                                              initial: question.get(answer: answer),
                                              indent: 2,
                                              answer: nestedAnswer))
      }
    }

    return result
  }
}
