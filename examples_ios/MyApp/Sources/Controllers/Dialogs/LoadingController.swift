import Foundation
import UIKit

class LoadingController: UIViewController {
    @IBOutlet var containerView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        containerView.layer.borderWidth = 1.0
        containerView.layer.borderColor = containerView.backgroundColor?.cgColor
        containerView.layer.cornerRadius = 12
    }
}
