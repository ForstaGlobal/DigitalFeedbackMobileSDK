import ConfirmitMobileSDK
import Foundation
import UIKit

class TextboxController: UIViewController {
  @IBOutlet var statusBar: UIView!
  @IBOutlet var navBar: UINavigationBar!
  @IBOutlet var textView: UITextView!
  @IBOutlet var btnSave: UIButton!

  weak var delegate: GeneralDialogDelegate?

  private var key = ""
  private var selectedValue = ""

  override func viewDidLoad() {
    super.viewDidLoad()

    view.backgroundColor = AppColor.background
    AppStyle.styleNavBar(navBar)
    AppStyle.styleStatusBar(statusBar)
    AppStyle.styleOutlinedButton(btnSave)
    AppStyle.styleTextview(textView)

    navBar.topItem?.title = title

    textView.text = selectedValue
  }

  override var preferredStatusBarStyle: UIStatusBarStyle {
    return .default
  }

  func initialize(key: String, title: String, selectedValue: String) {
    self.key = key
    self.title = title
    self.selectedValue = selectedValue
  }

  @IBAction func onCloseClicked(_ sender: Any) {
    dismissKeyboard()
    dismiss(animated: true, completion: nil)
  }

  @IBAction func onSaveClicked(_ sender: Any) {
    delegate?.onValueChanged(key: key, newValue: textView.text)
    DispatchQueue.main.async {
      self.dismiss(animated: true, completion: nil)
    }
  }
}
