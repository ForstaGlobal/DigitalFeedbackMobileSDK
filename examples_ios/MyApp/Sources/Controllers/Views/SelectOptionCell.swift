import Foundation
import UIKit

class SelectOptionCell: UITableViewCell {
    private static let radioChecked = UIImage(named: "outline_radio_button_checked_black_36pt")!
    private static let radioUnchecked = UIImage(named: "outline_radio_button_unchecked_black_36pt")!

    @IBOutlet var lblLabel: UILabel!
    @IBOutlet var imgRadio: UIImageView!

    func set(selected: Bool) {
        if selected {
            imgRadio.tintColor = AppColor.button
            imgRadio.image = SelectOptionCell.radioChecked
        } else {
            imgRadio.tintColor = AppColor.font
            imgRadio.image = SelectOptionCell.radioUnchecked
        }
    }
}
