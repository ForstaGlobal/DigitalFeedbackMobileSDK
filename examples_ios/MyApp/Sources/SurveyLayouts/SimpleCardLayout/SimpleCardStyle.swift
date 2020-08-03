import Foundation
import UIKit

// Opacity: 87% = DE, 54% = 8A, 38% = 61, 12% = 1F, 100% = FF, 70% = B3, 50% = 80
enum SimpleCardColor {
    static let navBar = UIColor.white
    static let white = UIColor.white
    static let font = UIColor(hexString: "#000000DE")!
    static let fontGrey = UIColor(hexString: "#1218218A")!
    static let background = white
    static let button = UIColor(hexString: "#0288D1")!
    static let border = UIColor(hexString: "#E0E0E0")!
    static let error = UIColor(hexString: "#F44336")!
}

enum SimpleCardStyle {
    static func styleCard(_ view: UIView, _ root: UIView) {
        view.backgroundColor = SimpleCardColor.background

        view.clipsToBounds = true
        view.layer.cornerRadius = 20
        root.layer.cornerRadius = 20
        root.layer.borderWidth = 1
        root.layer.borderColor = UIColor.clear.cgColor
        root.layer.shadowOffset = CGSize(width: 0, height: 4)
        root.layer.shadowRadius = 12
        root.layer.shadowOpacity = 0.5
    }

    static func styleLabelTitle(_ view: UILabel) {
        view.font = UIFont.boldSystemFont(ofSize: 16)
        view.textColor = SimpleCardColor.white
    }

    static func styleValidation(_ label: UILabel, _ view: UIView) {
        label.font = UIFont.boldSystemFont(ofSize: 14)
        label.textColor = SimpleCardColor.white
        view.backgroundColor = SimpleCardColor.error
        view.layer.cornerRadius = 8
        view.layer.borderWidth = 1
        view.layer.borderColor = SimpleCardColor.error.cgColor
    }

    static func styleOutlinedButton(_ view: UIButton) {
        view.titleLabel?.font = UIFont.boldSystemFont(ofSize: 14)
        view.backgroundColor = SimpleCardColor.background
        view.tintColor = SimpleCardColor.button
        view.setTitleColor(SimpleCardColor.button, for: .normal)
        view.layer.cornerRadius = 12
        view.layer.borderWidth = 1
        view.layer.borderColor = SimpleCardColor.button.cgColor
    }

    static func styleTextButton(_ view: UIButton) {
        view.titleLabel?.font = UIFont.boldSystemFont(ofSize: 14)
        view.backgroundColor = UIColor.clear
        view.tintColor = SimpleCardColor.white
        view.setTitleColor(SimpleCardColor.button, for: .normal)
    }
}
