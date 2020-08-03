import Foundation
import UIKit

class SettingsController: UIViewController {
    @IBOutlet var tblSettings: UITableView!

    override func viewDidLoad() {
        AppStyle.styleNavBar(navigationController?.navigationBar)
        registerHeaderOrFooter(LabelTableHeader.self)

        tblSettings.delegate = self
        tblSettings.dataSource = self
        tblSettings.tableFooterView = UIView(frame: .zero)
        tblSettings.rowHeight = UITableView.automaticDimension
        tblSettings.sectionHeaderHeight = 24
        tblSettings.estimatedRowHeight = 140
        tblSettings.backgroundColor = AppColor.background
    }

    private func registerHeaderOrFooter<T: UITableViewHeaderFooterView>(_ type: T.Type) {
        let className = String(describing: type)
        let nib = UINib(nibName: className, bundle: Bundle.main)
        tblSettings.register(nib, forHeaderFooterViewReuseIdentifier: className)
    }
}

extension SettingsController: GeneralDialogDelegate {
    func onValueChanged(key: String, newValue: String) {
        guard let config = ConfigKey(rawValue: key) else {
            return
        }

        switch config {
        case .uniqueId:
            AppConfigs.uniqueId = newValue
        default:
            break
        }

        tblSettings.reloadData()
    }
}

extension SettingsController: UITableViewDelegate {
    private func showTextbox(key: ConfigKey, title: String, selectedValue: String) {
        let controller = TextboxController(nibName: "TextboxController", bundle: nil)
        controller.initialize(key: key.rawValue, title: title, selectedValue: selectedValue)
        controller.delegate = self
        present(controller, animated: true, completion: nil)
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch indexPath.section {
        case 0: // Application
            switch indexPath.row {
            case 0:
                AppConfigs.onDemandDownloadPackage = !AppConfigs.onDemandDownloadPackage
            case 1:
                AppConfigs.validateOnAnswerChange = !AppConfigs.validateOnAnswerChange
            case 2:
                showTextbox(key: .uniqueId, title: "Unique ID", selectedValue: AppConfigs.uniqueId)
            case 3:
                AppConfigs.htmlText = !AppConfigs.htmlText
            case 4:
                AppConfigs.uploadImcomplete = !AppConfigs.uploadImcomplete
            case 5:
                AppConfigs.triggerTestMode = !AppConfigs.triggerTestMode
            default:
                break
            }
        default:
            break
        }
        tableView.deselectRow(at: indexPath, animated: false)
        tableView.reloadData()
    }

    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = tableView.dequeueReusableHeaderFooterView(withIdentifier: "LabelTableHeader") as! LabelTableHeader
        var title = ""
        switch section {
        case 0:
            title = "Application"
        default:
            break
        }
        view.lblTitle.text = title
        return view
    }

    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 32
    }

    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return CGFloat.leastNonzeroMagnitude
    }
}

extension SettingsController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 5
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 6
        default:
            return 0
        }
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var result: UITableViewCell!

        switch indexPath.section {
        case 0: // Survey
            switch indexPath.row {
            case 0:
                let cell = tableView.dequeueReusableCell(withIdentifier: "SettingsIconItem") as! SettingsIconItem
                cell.setCheckBox(label: "On demand download package", selected: AppConfigs.onDemandDownloadPackage)
                result = cell
            case 1:
                let cell = tableView.dequeueReusableCell(withIdentifier: "SettingsIconItem") as! SettingsIconItem
                cell.setCheckBox(label: "Validate on answer change", selected: AppConfigs.validateOnAnswerChange)
                result = cell
            case 2:
                let cell = tableView.dequeueReusableCell(withIdentifier: "SettingsItem") as! SettingsItem
                cell.setButton(label: "Unique Device ID", detail: AppConfigs.uniqueId)
                result = cell
            case 3:
                let cell = tableView.dequeueReusableCell(withIdentifier: "SettingsIconItem") as! SettingsIconItem
                cell.setCheckBox(label: "Enable HTML Survey Text", selected: AppConfigs.htmlText)
                result = cell
            case 4:
                let cell = tableView.dequeueReusableCell(withIdentifier: "SettingsIconItem") as! SettingsIconItem
                cell.setCheckBox(label: "Upload incomplete respondents", selected: AppConfigs.uploadImcomplete)
                result = cell
            case 5:
                let cell = tableView.dequeueReusableCell(withIdentifier: "SettingsIconItem") as! SettingsIconItem
                cell.setCheckBox(label: "Trigger Test Mode", selected: AppConfigs.triggerTestMode)
                result = cell
            default:
                break
            }
        default:
            break
        }
        result.selectionStyle = .none
        return result
    }
}

class SettingsIconItem: UITableViewCell {
    @IBOutlet var lblLabel: UILabel!
    @IBOutlet var imgIcon: UIImageView!

    func setCheckBox(label: String, selected: Bool) {
        lblLabel.text = label
        imgIcon.image = UIImage(named: selected ? "outline_check_box_black_36pt" : "outline_check_box_outline_blank_black_36pt")
        imgIcon.tintColor = selected ? AppColor.button : AppColor.font
    }

    func setRadio(label: String, selected: Bool) {
        lblLabel.text = label
        imgIcon.image = UIImage(named: selected ? "outline_radio_button_checked_black_36pt" : "outline_radio_button_unchecked_black_36pt")
        imgIcon.tintColor = selected ? AppColor.button : AppColor.font
    }

    func setButton(label: String) {
        lblLabel.text = label
        imgIcon.image = nil
    }
}

class SettingsItem: UITableViewCell {
    @IBOutlet var lblLabel: UILabel!
    @IBOutlet var imgIcon: UIImageView!
    @IBOutlet var lblDetail: UILabel!

    func setButton(label: String, detail: String) {
        lblLabel.text = label
        lblDetail.text = detail
        imgIcon.image = nil
    }
}
