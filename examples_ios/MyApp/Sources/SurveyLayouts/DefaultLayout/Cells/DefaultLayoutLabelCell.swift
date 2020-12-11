import Foundation
import UIKit

class DefaultLayoutLabelCell: UITableViewCell {
  @IBOutlet var lblText: UILabel!
  @IBOutlet var cstrLeftMargin: NSLayoutConstraint!

  override func awakeFromNib() {
    super.awakeFromNib()
  }

  func setItem(item: DefaultLayoutLabel) {
    lblText.textColor = DefaultLayoutColor.font
    lblText.textAlignment = .natural
    cstrLeftMargin.constant = 24.0 + (CGFloat(item.indent) * 24.0)
    var textSize: CGFloat = 16
    switch item.labelType {
    case .text:
      lblText.font = UIFont.systemFont(ofSize: textSize)
    case .instruction:
      textSize = 14
      lblText.font = UIFont.systemFont(ofSize: textSize)
    case .pageTitle:
      lblText.font = UIFont.boldSystemFont(ofSize: textSize)
      lblText.textAlignment = .center
    }
    if item.value.hasStyle {
      lblText.attributedText = item.value.getAttributed(size: textSize, color: DefaultLayoutColor.fontString)
    } else {
      lblText.text = item.value.get()
    }
  }
}
