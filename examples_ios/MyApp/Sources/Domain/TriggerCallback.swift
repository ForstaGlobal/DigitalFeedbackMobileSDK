import ConfirmitMobileSDK
import Foundation
import UIKit

class TriggerCallback: ProgramCallback {
  func onScenarioLoad(triggerInfo: TriggerInfo, error: Error?) {
    // TODO:
  }

  func onSurveyDownloadCompleted(triggerInfo: TriggerInfo, surveyId: String, error: Error?) {
    if let error = error {
      let alert = UIAlertController(title: "Survey \(surveyId) Downloaded Failed", message: error.localizedDescription, preferredStyle: .alert)
      alert.addAction(UIAlertAction(title: "Close", style: .cancel, handler: nil))

      DispatchQueue.main.async {
        if let controller = Utils.getTopController() {
          controller.present(alert, animated: true)
        }
      }
    }
  }

  func onAppFeedback(triggerInfo: TriggerInfo, data: [String: String?]) {
    let alert = UIAlertController(title: "Write a review!", message: "Would you like to write a review for our App?", preferredStyle: .alert)
    alert.addAction(UIAlertAction(title: "Remind Me Later", style: .cancel, handler: nil))
    DispatchQueue.main.async {
      if let controller = Utils.getTopController() {
        controller.present(alert, animated: true)
      }
    }
  }

  func onScenarioStart(triggerInfo: TriggerInfo, error: Error?) {
    if let error = error {
      let alert = UIAlertController(title: "Scenario Failed to Start", message: error.localizedDescription, preferredStyle: .alert)
      alert.addAction(UIAlertAction(title: "Close", style: .cancel, handler: nil))

      DispatchQueue.main.async {
        if let controller = Utils.getTopController() {
          controller.present(alert, animated: true)
        }
      }
    }
  }

  func onScenarioError(triggerInfo: TriggerInfo, error: Error) {
    let alert = UIAlertController(title: "Scenario Failed", message: error.localizedDescription, preferredStyle: .alert)
    alert.addAction(UIAlertAction(title: "Close", style: .cancel, handler: nil))

    DispatchQueue.main.async {
      if let controller = Utils.getTopController() {
        controller.present(alert, animated: true)
      }
    }
  }

  func onSurveyStart(config: SurveyFrameConfig) {
    do {
      let survey = try SurveyModel(serverId: config.serverId, surveyId: config.surveyId)
      let appDelegate = UIApplication.shared.delegate as! AppDelegate
      let navigationController = appDelegate.window!.rootViewController as! UINavigationController
      let controller = navigationController.topViewController
      DispatchQueue.main.async {
        if let controller = controller {
          SurveyManager().launchSurvey(controller: controller, survey: survey, surveyFrameConfig: config)
        }
      }
    } catch {}
  }

  func onWebSurveyStart(surveyWebView: SurveyWebViewController) {
    surveyWebView.getSurveyUrl { url, _ in
      if let url = url {
        // NOTE: Your Survey URL = Use this for custom webview
        print("\(url.url) // \(url.token)")
      }
    }

    if let controller = Utils.getTopController() {
      surveyWebView.delegate = SurveyWebViewDelegate()
      controller.present(surveyWebView, animated: true)
    }
  }
}
