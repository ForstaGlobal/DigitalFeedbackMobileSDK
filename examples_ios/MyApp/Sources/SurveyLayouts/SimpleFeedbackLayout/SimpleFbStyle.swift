import Foundation
import UIKit

// Opacity: 87% = DE, 54% = 8A, 38% = 61, 12% = 1F, 100% = FF, 70% = B3, 50% = 80
enum SimpleFbColor {
    static let navBar = UIColor.white
    static let white = UIColor.white
    static let font = white
    static let fontGrey = white
    static let backgroundFrom = UIColor(hexString: "#D1C4E9")!
    static let backgroundTo = UIColor(hexString: "#673AB7")!
    static let button = white
    static let border = UIColor(hexString: "#E0E0E0")!
    static let error = UIColor(hexString: "#e57373")!
}

enum SimpleFbStyle {

    static func styleLabelTitle(_ view: UILabel) {
        view.font = UIFont.boldSystemFont(ofSize: 32)
        view.textColor = SimpleFbColor.font
    }
    
    static func styleLabelText(_ view: UILabel) {
        view.font = UIFont.systemFont(ofSize: 24)
        view.textColor = SimpleFbColor.font
    }
    
    static func styleOutlinedButton(_ view: UIButton) {
        view.titleLabel?.font = UIFont.boldSystemFont(ofSize: 20)
        view.backgroundColor = UIColor.clear
        view.setTitleColor(SimpleFbColor.button, for: .normal)
        view.layer.cornerRadius = 16
        view.layer.borderWidth = 2
        view.layer.borderColor = SimpleFbColor.button.cgColor
    }
    
    static func styleCloseButton(_ view: UIButton) {
        view.backgroundColor = UIColor.clear
        view.tintColor = SimpleFbColor.button
    }
}
