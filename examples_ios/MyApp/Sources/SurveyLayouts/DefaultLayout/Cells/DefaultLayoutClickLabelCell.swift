import ConfirmitMobileSDK
import Foundation
import UIKit

protocol DefaultLayoutClickLabelCellDelegate: class {
  func onRadioClicked(answer: QuestionAnswer)
  func onCheckClicked(answer: QuestionAnswer, selected: Bool)
  func getSelected(answer: QuestionAnswer) -> Bool
}

class DefaultLayoutClickLabelCell: UITableViewCell {
  @IBOutlet var lblText: UILabel!
  @IBOutlet var imgIcon: UIImageView!
  @IBOutlet var cstrLeftMargin: NSLayoutConstraint!

  weak var delegate: DefaultLayoutClickLabelCellDelegate?

  private(set) var answer: QuestionAnswer!
  private var itemSelected: Bool = false
  private var clickLabelType: DefaultLayoutClickLabelType = .checkbox

  override func awakeFromNib() {
    super.awakeFromNib()
  }

  func setItem(item: DefaultLayoutClickLabel) {
    answer = item.answer
    clickLabelType = item.clickLabelType
    lblText.textColor = DefaultLayoutColor.font
    lblText.font = UIFont.systemFont(ofSize: 16)
    if item.answer.text.hasStyle {
      lblText.attributedText = Utils.getAttributedString(html: item.answer.text.getHtml(size: 16, color: DefaultLayoutColor.fontString))
    } else {
      lblText.text = item.answer.text.get()
    }
    cstrLeftMargin.constant = 24.0 + (CGFloat(item.indent) * 24.0)
    itemSelected = delegate?.getSelected(answer: answer) ?? false

    setIcon()
  }

  func setIcon() {
    var imageName = ""
    switch clickLabelType {
    case .checkbox:
      imageName = itemSelected ? "outline_check_box_black_36pt" : "outline_check_box_outline_blank_black_36pt"
    case .radio:
      imageName = itemSelected ? "outline_radio_button_checked_black_36pt" : "outline_radio_button_unchecked_black_36pt"
    }

    imgIcon.image = UIImage(named: imageName)
    imgIcon.tintColor = itemSelected ? DefaultLayoutColor.button : DefaultLayoutColor.font
  }

  @IBAction func onCellClicked(_ sender: Any) {
    if itemSelected, clickLabelType == .radio {
      return
    }

    itemSelected = !itemSelected
    setIcon()
    switch clickLabelType {
    case .checkbox:
      delegate?.onCheckClicked(answer: answer, selected: itemSelected)
    case .radio:
      delegate?.onRadioClicked(answer: answer)
    }
  }
}
