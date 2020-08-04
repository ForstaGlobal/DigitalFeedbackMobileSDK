import Foundation
import ConfirmitMobileSDK
import UIKit

protocol AddTriggerControllerDelegate: class {
    func onDigitialFeedbackAdded(serverId: String, programKey: String)
}

class AddTriggerController: UIViewController {
    @IBOutlet var navBar: UINavigationBar!
    @IBOutlet var statusBar: UIView!
    @IBOutlet var containerMain: UIView!
    @IBOutlet var txtSurveyId: UITextField!
    @IBOutlet var btnHostSelect: UIButton!
    @IBOutlet var btnCommit: UIButton!
    
    private var selectedServer: String = ""
    private var serverPair: [(key: String, label: String)] = []
    
    weak var delegate: AddTriggerControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        AppStyle.styleNavBar(navBar)
        AppStyle.styleStatusBar(statusBar)
        AppStyle.styleCard(containerMain)
        AppStyle.styleOutlinedDropdown(btnHostSelect)
        AppStyle.styleOutlinedButton(btnCommit)
        view.backgroundColor = AppColor.background
        
        do {
            let servers = try ConfirmitServer.getServers()
            for server in servers {
                serverPair.append((key: server.serverId, label: server.name))
            }
            
            setSelectedHost(serverId: servers.first?.serverId ?? "")
        } catch {
            // failed to grab servers
        }
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }
    
    private func setSelectedHost(serverId: String) {
        selectedServer = serverId
        
        for entry in try! ConfirmitServer.getServers() {
            if selectedServer == entry.serverId {
                btnHostSelect.setTitle(entry.name, for: .normal)
                return
            }
        }
    }
    
    @IBAction func onHostSelectClicked(_ sender: Any) {
        Utils.showSelector(controller: self, title: "Select Host", source: serverPair) { selected in
            self.setSelectedHost(serverId: selected)
        }
    }
    
    @IBAction func onAddClicked(_ sender: Any) {
        let programKey = txtSurveyId.text
        if let programKey = programKey {
            dismiss(animated: true) {
                self.delegate?.onDigitialFeedbackAdded(serverId: self.selectedServer, programKey: programKey)
            }
        }
    }
    
    @IBAction func onCloseClicked(_ sender: Any) {
        dismissKeyboard()
        dismiss(animated: true, completion: nil)
    }
}
