import Foundation
import MobileSdk

protocol DefaultLayoutItem {}

protocol DefaultLayoutSection: DefaultLayoutTextBoxCellDelegate, DefaultLayoutClickLabelCellDelegate {
    var items: [DefaultLayoutItem] { get }
    func load(delegate: DefaultLayoutQuestionSectionDelegate, section: Int)
}

protocol DefaultLayoutQuestionSectionDelegate: class {
    func onRefreshRow(add: [IndexPath], refresh: [IndexPath], remove: [IndexPath])
}

class DefaultLayoutPageHeaderSection: DefaultLayoutSection {
    private(set) var items: [DefaultLayoutItem] = []
    let page: SurveyPage

    init(page: SurveyPage) {
        self.page = page
        self.items = self.createItems(page: page)
    }

    private func createItems(page: SurveyPage) -> [DefaultLayoutItem] {
        if !page.title.isEmpty {
            return DefaultLayoutItemFactory.createPageHeader(page: page)
        }

        return []
    }

    func load(delegate: DefaultLayoutQuestionSectionDelegate, section: Int) {}
    func onTextUpdated(text: String) {}
    func onRadioClicked(answer: QuestionAnswer) {}
    func onCheckClicked(answer: QuestionAnswer, selected: Bool) {}
    func getSelected(answer: QuestionAnswer) -> Bool { return false }
}

class DefaultLayoutQuestionSection: DefaultLayoutSection {
    private(set) var items: [DefaultLayoutItem] = []
    let question: Question
    var section: Int = -1

    weak var delegate: DefaultLayoutQuestionSectionDelegate?

    init(question: Question) {
        self.question = question
        self.items = self.createItems(question: self.question)
    }

    func load(delegate: DefaultLayoutQuestionSectionDelegate, section: Int) {
        self.delegate = delegate
        self.section = section
    }

    private func createItems(question: Question) -> [DefaultLayoutItem] {
        switch question {
        case let q as InfoQuestion:
            return DefaultLayoutItemFactory.createInfo(question: q)
        case let q as TextQuestion:
            return DefaultLayoutItemFactory.createText(question: q)
        case let q as NumericQuestion:
            return DefaultLayoutItemFactory.createNumeric(question: q)
        case let q as SingleQuestion:
            return DefaultLayoutItemFactory.createSingle(question: q)
        case let q as MultiQuestion:
            return DefaultLayoutItemFactory.createMulti(question: q)
        default:
            return DefaultLayoutItemFactory.createNotSupported()
        }
    }

    private func updateValidation(question: DefaultQuestion, errors: [ValidationQuestionError]) {
        let validationIndex = DefaultLayoutItemUtils.getValidationIndex(section: section, question: question)
        guard let item = items[optional: validationIndex.row] else {
            return // Item not exist. Invalid question.
        }

        guard let iconLabelItem = item as? DefaultLayoutIconLabel else {
            if errors.count > 0 {
                // Validation not exist. Insert new error
                self.items.insert(DefaultLayoutIconLabel(errors: errors), at: validationIndex.row)
                self.delegate?.onRefreshRow(add: [validationIndex], refresh: [], remove: [])
            }
            return
        }

        var addRow: [IndexPath] = []
        var removeRow: [IndexPath] = []
        var refreshRow: [IndexPath] = []
        if iconLabelItem.errors.count > 0 { // Validation already exist
            if errors.count == 0 { // No more error. Clear validation
                self.items.remove(at: validationIndex.row)
                removeRow.append(validationIndex)
            } else { // Update errors
                iconLabelItem.errors = errors
                refreshRow.append(validationIndex)
            }
        } else { // Validation not exist. Insert new error
            self.items.insert(DefaultLayoutIconLabel(errors: errors), at: validationIndex.row)
            addRow.append(validationIndex)
        }

        self.delegate?.onRefreshRow(add: addRow, refresh: refreshRow, remove: removeRow)
    }

    func onTextUpdated(text: String) {
        if let question = question as? TextQuestion {
            question.setValue(value: text)

            self.validate(question: question)
        }

        if let question = question as? NumericQuestion {
            question.setValue(value: Double(text))

            self.validate(question: question)
        }
    }

    func onRadioClicked(answer: QuestionAnswer) {
        if let question = question as? SingleQuestion {
            question.select(answer: answer)

            self.validate(question: question)

            self.delegate?.onRefreshRow(add: [],
                                        refresh: DefaultLayoutItemUtils.getSingleAnswerIndexList(section: self.section, question: question, excludeCode: answer.code),
                                        remove: [])
        }
    }

    func onCheckClicked(answer: QuestionAnswer, selected: Bool) {
        if let question = question as? MultiQuestion {
            question.set(answer: answer, select: selected)

            self.validate(question: question)
        }
    }

    func getSelected(answer: QuestionAnswer) -> Bool {
        if let question = question as? SingleQuestion {
            if let selected = question.selected() {
                return selected.code == answer.code
            }
            return false
        }

        if let question = question as? MultiQuestion {
            return question.get(answer: answer)
        }

        return false
    }

    private func validate(question: DefaultQuestion) {
        if AppConfigs.validateOnAnswerChange {
            // Quick implementation. do / catch is better practice
            let errors = try! question.validate()
            self.updateValidation(question: question, errors: errors)
        }
    }
}
