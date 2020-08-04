import Foundation
import ConfirmitMobileSDK
import UIKit

class SimpleCardNumericItem: SimpleCardItem {
    @IBOutlet var txtNumeric: UITextField!
    
    var question: NumericQuestion!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        txtNumeric.tintColor = SimpleCardColor.button
        txtNumeric.font = UIFont.systemFont(ofSize: 16)
    }
    
    func setupQuestion(question: NumericQuestion) {
        self.question = question
        lblHeader.text = question.text.get()
        txtNumeric.text = question.getValue()
        
        handleValidation(errors: question.errors)
    }
    
    override func onShow() -> Bool {
        txtNumeric.becomeFirstResponder()
        return false
    }
    
    override func onNext() -> Bool {
        if let double = Double(txtNumeric.text ?? "") {
            question.setValue(value: double)
        }
        return validate(question: question)
    }
    
    override func onBack() -> Bool {
        if let double = Double(txtNumeric.text ?? "") {
            question.setValue(value: double)
        }
        return true
    }
}
