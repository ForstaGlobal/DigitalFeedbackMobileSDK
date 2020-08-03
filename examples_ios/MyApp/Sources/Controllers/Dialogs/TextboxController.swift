import Foundation
import MobileSdk
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
        
        self.view.backgroundColor = AppColor.background
        AppStyle.styleNavBar(self.navBar)
        AppStyle.styleStatusBar(self.statusBar)
        AppStyle.styleOutlinedButton(self.btnSave)
        AppStyle.styleTextview(self.textView)
        
        self.navBar.topItem?.title = self.title
        
        self.textView.text = self.selectedValue
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
        self.delegate?.onValueChanged(key: self.key, newValue: self.textView.text)
        DispatchQueue.main.async {
            self.dismiss(animated: true, completion: nil)
        }
    }
}
