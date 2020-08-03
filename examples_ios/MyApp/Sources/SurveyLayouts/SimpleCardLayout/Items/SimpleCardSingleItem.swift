import Foundation
import MobileSdk
import UIKit

class SimpleCardSingleItem: SimpleCardItem {
    @IBOutlet var stackView: UIStackView!
    @IBOutlet var cstrStackHeight: NSLayoutConstraint!
    
    private var question: SingleQuestion!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func setupQuestion(question: SingleQuestion) {
        self.question = question
        lblHeader.text = question.text.get()
        
        createAnswers(question: question)
        
        handleValidation(errors: question.errors)
    }
    
    private func createAnswers(question: SingleQuestion) {
        let selectedAnswer = question.selected()
        for answer in question.answers {
            let selectItem: SimpleCardSelectItem = Utils.fromNib()
            selectItem.setupItem(checkbox: false, answer: answer, selected: answer.code == selectedAnswer?.code)
            selectItem.delegate = self
            stackView.addArrangedSubview(selectItem)
        }
        cstrStackHeight.constant = CGFloat(question.answers.count) * 36
    }
    
    override func onNext() -> Bool {
        return validate(question: question)
    }
    
    override func onBack() -> Bool {
        return true
    }
}

extension SimpleCardSingleItem: SimpleCardSelectItemDelegate {
    func onRadioClicked(answer: QuestionAnswer) {
        question.select(answer: answer)
        for subView in stackView.arrangedSubviews {
            if let selectItem = subView as? SimpleCardSelectItem {
                selectItem.updateSelected(selected: answer.code == selectItem.answer.code)
            }
        }
    }
    
    func onCheckClicked(answer: QuestionAnswer, selected: Bool) {
        // Nothing to do
    }
}
