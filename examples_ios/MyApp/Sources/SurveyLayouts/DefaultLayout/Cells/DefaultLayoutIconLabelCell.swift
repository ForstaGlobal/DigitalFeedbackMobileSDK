import ConfirmitMobileSDK
import Foundation
import UIKit

class DefaultLayoutIconLabelCell: UITableViewCell {
  @IBOutlet var lblText: UILabel!
  @IBOutlet var imgIcon: UIImageView!

  override func awakeFromNib() {
    super.awakeFromNib()
  }

  func setItem(item: DefaultLayoutIconLabel) {
    imgIcon.image = UIImage(named: item.iconName)
    if item.errors.count > 0 {
      lblText.text = getErrorMessage(errors: item.errors)
      lblText.font = UIFont.systemFont(ofSize: 16)
      lblText.textColor = DefaultLayoutColor.error
      imgIcon.tintColor = DefaultLayoutColor.error
    } else {
      lblText.textColor = DefaultLayoutColor.font
      lblText.font = UIFont.boldSystemFont(ofSize: 16)
      imgIcon.tintColor = DefaultLayoutColor.font
      if item.value.hasStyle {
        lblText.attributedText = Utils.getAttributedString(html: item.value.getHtml(size: 16, color: DefaultLayoutColor.fontString))
      } else {
        lblText.text = item.value.get()
      }
    }
  }

  private func getErrorMessage(errors: [ValidationQuestionError]) -> String {
    var result = ""
    for error in errors {
      result += error.message + "\n"
    }

    return String(result.dropLast())
  }
}
