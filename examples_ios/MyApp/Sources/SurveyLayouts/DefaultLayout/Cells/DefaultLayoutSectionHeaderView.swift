import Foundation
import UIKit

class DefaultLayoutSectionHeaderView: UITableViewHeaderFooterView {
    @IBOutlet var viewDash: UIView!
    @IBOutlet var viewBackground: UIView!

    override func awakeFromNib() {
        super.awakeFromNib()

        self.backgroundView = viewBackground
        self.viewDash.backgroundColor = DefaultLayoutColor.button
        self.viewDash.layer.cornerRadius = 5
    }
}
