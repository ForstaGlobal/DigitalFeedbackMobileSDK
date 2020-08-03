import Foundation
import UIKit

protocol DefaultLayoutTextBoxCellDelegate: class {
    func onTextUpdated(text: String)
}

class DefaultLayoutTextBoxCell: UITableViewCell {
    @IBOutlet var txtText: UITextView!
    @IBOutlet weak var cstrTextHeight: NSLayoutConstraint!
    
    weak var delegate: DefaultLayoutTextBoxCellDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func setItem(item: DefaultLayoutTextBox) {
        txtText.backgroundColor = DefaultLayoutColor.white
        txtText.layer.cornerRadius = 5
        txtText.layer.borderWidth = 1
        txtText.layer.borderColor = DefaultLayoutColor.border.cgColor
        txtText.text = item.initialValue
        txtText.delegate = self
        txtText.tintColor = DefaultLayoutColor.button
        txtText.textContainerInset = UIEdgeInsets(top: 12.0, left: 8.0, bottom: 12.0, right: 8.0)
        
        switch item.textBoxType {
        case .text:
            txtText.keyboardType = .default
            cstrTextHeight.constant = 120
        case .numeric:
            txtText.keyboardType = .numbersAndPunctuation
            cstrTextHeight.constant = 40
        }
    }
}

extension DefaultLayoutTextBoxCell: UITextViewDelegate {
    func textViewDidChange(_ textView: UITextView) {
        delegate?.onTextUpdated(text: textView.text)
    }
}
