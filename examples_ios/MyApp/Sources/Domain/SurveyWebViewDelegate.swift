import Foundation
import MobileSdk

class SurveyWebViewDelegate: SurveyWebViewControllerDelegate {
    func onWebViewSurveyError(serverId: String, projectId: String, error: Error) {
        let alert = UIAlertController(title: "WebView Survey Failed", message: error.localizedDescription, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Close", style: .cancel, handler: nil))
        
        DispatchQueue.main.async {
            if let controller = Utils.getTopController() {
                controller.present(alert, animated: true)
            }
        }
    }
    
    func onWebViewSurveyFinished(serverId: String, projectId: String) {
    }
    
    func onWebViewSurveyQuit(serverId: String, projectId: String) {
    }
    
}
