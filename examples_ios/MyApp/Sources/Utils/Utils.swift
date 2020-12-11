import Foundation
import UIKit

enum Utils {
  static func fromNib<T: UIView>() -> T {
    return Bundle.main.loadNibNamed(String(describing: T.self), owner: nil, options: nil)![0] as! T
  }

  static func showLoading(controller: UIViewController, block: (LoadingController) -> Void) {
    let loading = LoadingController(nibName: "LoadingController", bundle: nil)
    loading.modalPresentationStyle = .overFullScreen
    controller.present(loading, animated: false, completion: nil)
    block(loading)
  }

  static func showSelector(controller: UIViewController, title: String, source: [(key: String, label: String)], block: @escaping (String) -> Void) {
    let alert = UIAlertController(title: title, message: nil, preferredStyle: .actionSheet)
    for (key, label) in source {
      alert.addAction(UIAlertAction(title: label, style: .default, handler: { _ in
        block(key)
            }))
    }

    if Utils.isIPad() {
      if let popoverController = alert.popoverPresentationController {
        popoverController.sourceView = controller.view
        popoverController.sourceRect = CGRect(x: controller.view.bounds.midX, y: controller.view.bounds.midY, width: 0, height: 0)
        popoverController.permittedArrowDirections = []
      }
    }
    controller.present(alert, animated: true)
  }

  static func showError(controller: UIViewController, error: String, completion: (() -> Void)? = nil) {
    let alertController = UIAlertController(title: "Survey Error", message: "\(error)", preferredStyle: UIAlertController.Style.alert)
    alertController.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))

    controller.present(alertController, animated: true, completion: completion)
  }

  static func getSetting<T>(_ key: String, _ def: T) -> T {
    if let config = UserDefaults.standard.object(forKey: key) as? T {
      return config
    }

    setSeting(key, def)
    return def
  }

  static func setSeting<T>(_ key: String, _ value: T) {
    UserDefaults.standard.set(value, forKey: key)
  }

  static func isIPad() -> Bool {
    var screenSize = ScreenType.phone
    let idiom = UIDevice.current.userInterfaceIdiom
    if idiom == UIUserInterfaceIdiom.pad {
      screenSize = ScreenType.tablet
    }

    return screenSize == .tablet
  }

  static func getTopController() -> UIViewController? {
    if var topController = UIApplication.shared.keyWindow?.rootViewController {
      while let presentedViewController = topController.presentedViewController {
        topController = presentedViewController
      }
      return topController
    }

    return nil
  }
}
