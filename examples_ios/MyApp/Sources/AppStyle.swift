import Foundation
import UIKit

// Opacity: 87% = DE, 54% = 8A, 38% = 61, 12% = 1F, 100% = FF, 70% = B3, 50% = 80
enum AppColor {
    static let navBar = UIColor(hexString: "#34393f")!
    static let white = UIColor.white
    static let font = UIColor(hexString: "#000000DE")!
    static let fontGrey = UIColor(hexString: "#1218218A")!
    static let background = white
    static let contentBackground = AppColor.white
    static let button = UIColor(hexString: "#0892ce")!
    static let backdrop = UIColor(hexString: "#0000008A")!
    static let error = UIColor(hexString: "#c51162")!
    static let separator = UIColor(hexString: "#f0f0f0")!
}

enum AppStyle {
    static func styleNavBar(_ navBar: UINavigationBar?) {
        guard let navBar = navBar else {
            return
        }
        
        navBar.barTintColor = AppColor.white
        navBar.tintColor = AppColor.button
        navBar.shadowImage = UIImage()
        navBar.titleTextAttributes = [NSAttributedString.Key.foregroundColor: AppColor.font]
    }
    
    static func styleStatusBar(_ view: UIView) {
        view.backgroundColor = AppColor.white
    }
    
    static func styleCard(_ view: UIView) {
        view.backgroundColor = AppColor.contentBackground
    }
    
    private static func styleTitleButton(_ view: UIButton) {
        view.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15)
    }
    
    static func styleTextButton(_ view: UIButton) {
        AppStyle.styleTitleButton(view)
        view.backgroundColor = AppColor.contentBackground
        view.setTitleColor(AppColor.button, for: .normal)
    }
    
    static func styleContainedButton(_ view: UIButton) {
        AppStyle.styleTitleButton(view)
        view.backgroundColor = AppColor.button
        view.setTitleColor(AppColor.white, for: .normal)
        view.layer.cornerRadius = 4
    }
    
    static func styleOutlinedButton(_ view: UIButton) {
        AppStyle.styleTitleButton(view)
        view.backgroundColor = AppColor.contentBackground
        view.setTitleColor(AppColor.button, for: .normal)
        view.layer.cornerRadius = 4
        view.layer.borderWidth = 1
        view.layer.borderColor = AppColor.button.cgColor
    }
    
    static func styleTextview(_ view: UITextView) {
        view.font = UIFont.systemFont(ofSize: 15)
        view.backgroundColor = AppColor.contentBackground
        view.textColor = AppColor.font
        view.tintColor = AppColor.button
        view.layer.cornerRadius = 4
        view.layer.borderWidth = 1
        view.layer.borderColor = AppColor.separator.cgColor
    }
    
    static func styleOutlinedDropdown(_ view: UIButton) {
        view.titleLabel?.font = UIFont.systemFont(ofSize: 15)
        view.backgroundColor = AppColor.contentBackground
        view.setTitleColor(AppColor.font, for: .normal)
        view.layer.cornerRadius = 4
        view.layer.borderWidth = 1
        view.layer.borderColor = AppColor.separator.cgColor
    }
    
    static func styleCaptionImage(_ view: UIImageView) {
        view.tintColor = AppColor.fontGrey
    }
    
    static func styleCaptionLabel(_ view: UILabel) {
        view.textColor = AppColor.fontGrey
    }
}
