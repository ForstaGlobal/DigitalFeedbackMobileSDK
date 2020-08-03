import Foundation
import MobileSdk
import UIKit

class SimpleCardMultiItem: SimpleCardItem {
    @IBOutlet var stackView: UIStackView!
    @IBOutlet var cstrStackHeight: NSLayoutConstraint!
    
    private var question: MultiQuestion!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func setupQuestion(question: MultiQuestion) {
        self.question = question
        lblHeader.text = question.text.get()
        
        createAnswers(question: question)
        
        handleValidation(errors: question.errors)
    }
    
    private func createAnswers(question: MultiQuestion) {
        for answer in question.answers {
            let selectItem: SimpleCardSelectItem = Utils.fromNib()
            selectItem.setupItem(checkbox: true, answer: answer, selected: question.get(answer: answer))
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

extension SimpleCardMultiItem: SimpleCardSelectItemDelegate {
    func onRadioClicked(answer: QuestionAnswer) {
        // Nothing to do
    }
    
    func onCheckClicked(answer: QuestionAnswer, selected: Bool) {
        question.set(answer: answer, select: selected)
    }
}
