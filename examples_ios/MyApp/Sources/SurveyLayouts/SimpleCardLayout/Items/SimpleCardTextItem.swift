import Foundation
import ConfirmitMobileSDK
import UIKit

class SimpleCardTextItem: SimpleCardItem {
    @IBOutlet var contentContainer: UIView!
    @IBOutlet var txtContent: UITextView!
    
    var question: TextQuestion!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        contentContainer.layer.cornerRadius = 12
        contentContainer.layer.borderColor = SimpleCardColor.border.cgColor
        contentContainer.layer.borderWidth = 1
        txtContent.tintColor = SimpleCardColor.button
        txtContent.font = UIFont.systemFont(ofSize: 16)
    }
    
    func setupQuestion(question: TextQuestion) {
        self.question = question
        lblHeader.text = question.text.get()
        txtContent.text = question.getValue()
        
        handleValidation(errors: question.errors)
    }
    
    override func onShow() -> Bool {
        txtContent.becomeFirstResponder()
        return false
    }
    
    override func onNext() -> Bool {
        question.setValue(value: txtContent.text)
        return validate(question: question)
    }
    
    override func onBack() -> Bool {
        question.setValue(value: txtContent.text)
        return true
    }
}
