import Foundation
import UIKit

class TabBarController: UITabBarController {
    override func viewDidLoad() {
        tabBar.tintColor = AppColor.button
        tabBar.barTintColor = AppColor.white
    }
}
