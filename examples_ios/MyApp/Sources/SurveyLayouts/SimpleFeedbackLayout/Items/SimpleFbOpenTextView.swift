import Foundation
import MobileSdk
import UIKit

protocol SimpleFbOpenTextViewDelegate: class {
    func onSubmitClicked(question: TextQuestion) -> [ValidationQuestionError]
}

class SimpleFbOpenTextView: UIView {
    @IBOutlet var txtOpen: UITextView!
    @IBOutlet var lblText: UILabel!
    @IBOutlet var lblInst: UILabel!
    @IBOutlet var btnSubmit: UIButton!
    @IBOutlet var textContainer: UIView!
    
    @IBOutlet var lblValidation: UILabel!
    weak var surveyFrame: SurveyFrame!
    weak var textQuestion: TextQuestion!
    weak var delegate: SimpleFbOpenTextViewDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        SimpleFbStyle.styleLabelText(lblInst)
        SimpleFbStyle.styleLabelText(lblValidation)
        SimpleFbStyle.styleLabelTitle(lblText)
        SimpleFbStyle.styleOutlinedButton(btnSubmit)
        
        lblValidation.text = ""
        textContainer.layer.borderWidth = 1
        textContainer.layer.borderColor = SimpleFbColor.font.cgColor
        textContainer.layer.cornerRadius = 16
        txtOpen.font = UIFont.systemFont(ofSize: 16)
        txtOpen.tintColor = SimpleFbColor.backgroundTo
    }
    
    func setup(surveyFrame: SurveyFrame, textQuestion: TextQuestion) {
        self.surveyFrame = surveyFrame
        self.textQuestion = textQuestion
        lblText.text = textQuestion.text.get()
        lblInst.text = textQuestion.instruction.get()
    }
    
    @IBAction func onSubmitClicked(_ sender: Any) {
        textQuestion.setValue(value: txtOpen.text)
        
        if let errors = delegate?.onSubmitClicked(question: textQuestion), errors.count > 0 {
            lblValidation.text = "Wait! we haven't heard you yet!"
        }
    }
}
