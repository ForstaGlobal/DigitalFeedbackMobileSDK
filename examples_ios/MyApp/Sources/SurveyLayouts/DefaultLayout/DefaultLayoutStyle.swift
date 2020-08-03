import Foundation
import UIKit

// Opacity: 87% = DE, 54% = 8A, 38% = 61, 12% = 1F, 100% = FF, 70% = B3, 50% = 80
enum DefaultLayoutColor {
    static let navBar = UIColor.white
    static let white = UIColor.white
    static let font = UIColor(hexString: "#000000DE")!
    static let fontString = "#000000DE"
    static let fontGrey = UIColor(hexString: "#1218218A")!
    static let background = DefaultLayoutColor.white
    static let button = UIColor(hexString: "#009688")!
    static let border = UIColor(hexString: "#E0E0E0")!
    static let error = UIColor(hexString: "#F44336")!
}

enum DefaultLayoutStyle {
    static func styleNavBar(navBar: UINavigationBar, statusBar: UIView, title: String) {
        navBar.barTintColor = DefaultLayoutColor.white
        navBar.tintColor = DefaultLayoutColor.button
        navBar.isTranslucent = false
        navBar.titleTextAttributes = [NSAttributedString.Key.foregroundColor: DefaultLayoutColor.font]

        statusBar.backgroundColor = DefaultLayoutColor.white

        navBar.topItem?.title = title
    }

    private static func styleTitleButton(_ view: UIButton) {
        view.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15)
    }

    static func styleTextButton(_ view: UIButton) {
        DefaultLayoutStyle.styleTitleButton(view)
        view.backgroundColor = DefaultLayoutColor.background
        view.setTitleColor(DefaultLayoutColor.button, for: .normal)
    }
}
