import Foundation
import UIKit

class DefaultLayoutSectionHeaderView: UITableViewHeaderFooterView {
  @IBOutlet var viewDash: UIView!
  @IBOutlet var viewBackground: UIView!

  override func awakeFromNib() {
    super.awakeFromNib()

    backgroundView = viewBackground
    viewDash.backgroundColor = DefaultLayoutColor.button
    viewDash.layer.cornerRadius = 5
  }
}
