import Foundation
import MobileSdk
import UIKit

protocol SimpleCardSelectItemDelegate: class {
    func onRadioClicked(answer: QuestionAnswer)
    func onCheckClicked(answer: QuestionAnswer, selected: Bool)
}

class SimpleCardSelectItem: UIView {
    @IBOutlet var lblText: UILabel!
    @IBOutlet var imgIcon: UIImageView!
    
    private var checkbox: Bool = false
    var answer: QuestionAnswer!
    private var selected: Bool = false
    
    weak var delegate: SimpleCardSelectItemDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func setupItem(checkbox: Bool, answer: QuestionAnswer, selected: Bool) {
        self.checkbox = checkbox
        self.answer = answer
        self.selected = selected
        lblText.text = answer.text.get()
        lblText.textColor = SimpleCardColor.font
        
        setIcon()
    }
    
    private func setIcon() {
        var imageName = ""
        if checkbox {
            imageName = selected ? "outline_check_box_black_36pt" : "outline_check_box_outline_blank_black_36pt"
        } else {
            imageName = selected ? "outline_radio_button_checked_black_36pt" : "outline_radio_button_unchecked_black_36pt"
        }
        
        imgIcon.image = UIImage(named: imageName)
        imgIcon.tintColor = selected ? SimpleCardColor.button : SimpleCardColor.font
    }
    
    func updateSelected(selected: Bool) {
        self.selected = selected
        setIcon()
    }
    
    @IBAction func onItemClicked(_ sender: Any) {
        if selected && !checkbox {
            return
        }
        
        selected = !selected
        setIcon()
        if checkbox {
            delegate?.onCheckClicked(answer: answer, selected: selected)
        } else {
            delegate?.onRadioClicked(answer: answer)
        }
    }
}
