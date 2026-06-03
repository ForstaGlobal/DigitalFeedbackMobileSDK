import ConfirmitMobileSDK
import Foundation
import UIKit
import WebKit

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

  func onWebViewSurveyFinished(serverId: String, projectId: String) {}

  func onWebViewSurveyQuit(serverId: String, projectId: String) {}

  func onWebViewError(serverId: String, projectId: String, webview: WKWebView, error: Error) {}

  func onWebViewIntercept(serverId: String, projectId: String, webview: WKWebView, decidePolicyFor navigationAction: WKNavigationAction) -> WKNavigationActionPolicy? {
    return nil
  }
}
