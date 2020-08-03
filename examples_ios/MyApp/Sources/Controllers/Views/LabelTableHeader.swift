import Foundation
import UIKit

class LabelTableHeader: UITableViewHeaderFooterView {
    @IBOutlet var lblTitle: UILabel!
    @IBOutlet var viewBackground: UIView!

    override func awakeFromNib() {
        super.awakeFromNib()

        backgroundView = viewBackground
    }
}
