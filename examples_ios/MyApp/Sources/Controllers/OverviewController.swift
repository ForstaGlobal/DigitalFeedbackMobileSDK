import Foundation
import MobileSdk
import UIKit

enum OverviewOptionType: String {
    case language
    case surveyLayout
    case respondentValue
    case removeSurvey
}

class OverviewController: UIViewController {
    @IBOutlet var tableView: UITableView!
    
    private var viewModel: OverviewViewModel!
    private var initialized: Bool = false
    private let options: [OverviewOptionType] = [.language, .surveyLayout, .respondentValue, .removeSurvey]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if !initialized {
            navigationController?.popViewController(animated: true)
            return
        }
        
        extendedLayoutIncludesOpaqueBars = true
        
        AppStyle.styleNavBar(navigationController?.navigationBar)
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView(frame: .zero)
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = 140
        tableView.backgroundColor = AppColor.background
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }
    
    func initialize(serverId: String, surveyId: String) {
        do {
            viewModel = try OverviewViewModel(serverId: serverId, surveyId: surveyId)
            initialized = true
        } catch {}
    }
}

extension OverviewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, willSelectRowAt indexPath: IndexPath) -> IndexPath? {
        if indexPath.section == 0 {
            return nil
        }
        
        return indexPath
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        
        let cell = tableView.cellForRow(at: indexPath)
        switch cell {
        case let optionCell as OverviewOptionCell:
            onOptionSelected(survey: viewModel.survey, type: optionCell.type)
        case let buttonCell as OverviewButtonCell:
            onOptionSelected(survey: viewModel.survey, type: buttonCell.type)
        default:
            break
        }
    }
}

extension OverviewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 1
        case 1:
            return options.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "OverviewDetailCell") as! OverviewDetailCell
            cell.initialize(survey: viewModel.survey)
            cell.delegate = self
            return cell
        } else {
            let type = options[indexPath.row]
            
            switch type {
            case .removeSurvey:
                let cell = tableView.dequeueReusableCell(withIdentifier: "OverviewButtonCell") as! OverviewButtonCell
                cell.initialize(survey: viewModel.survey, type: options[indexPath.row])
                return cell
            default:
                let cell = tableView.dequeueReusableCell(withIdentifier: "OverviewOptionCell") as! OverviewOptionCell
                cell.initialize(survey: viewModel.survey, type: options[indexPath.row])
                return cell
            }
        }
    }
}

extension OverviewController: SurveyLayoutControllerDelegate {
    func onCloseError(error: String) {
        Utils.showError(controller: self, error: error)
    }
}

extension OverviewController: OverviewDetailCellDelegate {
    func onStartClicked(survey: SurveyModel) {
        SurveyManager().launchSurvey(controller: self, layoutDelegate: self, survey: survey)
    }
}

extension OverviewController: GeneralDialogDelegate {
    private func showSelection(type: OverviewOptionType, title: String, labels: [String], values: [String], selectedValue: String) {
        let controller = SelectionController(nibName: "SelectionController", bundle: nil)
        controller.initialize(key: type.rawValue, title: title, labels: labels, values: values, selectedValue: selectedValue)
        controller.delegate = self
        present(controller, animated: true, completion: nil)
    }
    
    private func showTextbox(type: OverviewOptionType, title: String, selectedValue: String) {
        let controller = TextboxController(nibName: "TextboxController", bundle: nil)
        controller.initialize(key: type.rawValue, title: title, selectedValue: selectedValue)
        controller.delegate = self
        present(controller, animated: true, completion: nil)
    }
    
    func onOptionSelected(survey: SurveyModel, type: OverviewOptionType) {
        switch type {
        case .language:
            let labels = survey.survey.languages.map { $0.name }
            let values = survey.survey.languages.map { String($0.id) }
            let selectedValue = String(survey.selectedLanguage.id)
            let title = "Select Language"
            showSelection(type: type, title: title, labels: labels, values: values, selectedValue: selectedValue)
        case .surveyLayout:
            let labels = SurveyLayout.toStringArray()
            let values = labels
            let selectedValue = survey.configs.surveyLayout.rawValue
            let title = "Select Layout"
            showSelection(type: type, title: title, labels: labels, values: values, selectedValue: selectedValue)
        case .respondentValue:
            let selectedValue = survey.configs.respondentValue
            let title = "Enter Respondent Values"
            showTextbox(type: type, title: title, selectedValue: selectedValue)
        case .removeSurvey:
            if viewModel.removeSurvey() {
                navigationController?.popViewController(animated: true)
            } else {
                Utils.showError(controller: self, error: "Failed to remove survey")
            }
        }
    }
    
    func onValueChanged(key: String, newValue: String) {
        guard let type = OverviewOptionType(rawValue: key) else {
            return
        }
        
        switch type {
        case .language:
            viewModel.updateSelectedLanguage(langId: Int(newValue)!)
        case .surveyLayout:
            viewModel.survey.configs.surveyLayout = SurveyLayout(rawValue: newValue)!
        case .respondentValue:
            viewModel.survey.configs.respondentValue = newValue
        default:
            break
        }
        
        tableView.reloadData()
    }
}

protocol OverviewDetailCellDelegate: class {
    func onStartClicked(survey: SurveyModel)
}

class OverviewDetailCell: UITableViewCell {
    @IBOutlet var lblName: UILabel!
    @IBOutlet var lblDesc: UILabel!
    @IBOutlet var imgSurveyId: UIImageView!
    @IBOutlet var lblSurveyId: UILabel!
    @IBOutlet var btnStart: UIButton!
    
    weak var delegate: OverviewDetailCellDelegate?
    
    private var survey: SurveyModel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        AppStyle.styleCaptionImage(imgSurveyId)
        AppStyle.styleCaptionLabel(lblSurveyId)
        AppStyle.styleOutlinedButton(btnStart)
    }
    
    func initialize(survey: SurveyModel) {
        self.survey = survey
        lblName.text = survey.survey.name
        lblSurveyId.text = survey.survey.surveyId
        lblDesc.text = survey.survey.description.isEmpty ? "(No description)" : survey.survey.description
    }
    
    @IBAction func onStartClicked(_ sender: Any) {
        delegate?.onStartClicked(survey: survey)
    }
}

class OverviewOptionCell: UITableViewCell {
    @IBOutlet var imgIcon: UIImageView!
    @IBOutlet var lblTitle: UILabel!
    @IBOutlet var lblSelected: UILabel!
    
    private var survey: SurveyModel!
    var type: OverviewOptionType!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        AppStyle.styleCaptionLabel(lblSelected)
        AppStyle.styleCaptionImage(imgIcon)
    }
    
    func initialize(survey: SurveyModel, type: OverviewOptionType) {
        self.survey = survey
        self.type = type
        
        var title = ""
        var selected = ""
        switch type {
        case .language:
            title = "Language"
            selected = "\(survey.selectedLanguage.nativeName) (\(survey.selectedLanguage.name))"
        case .surveyLayout:
            title = "Survey Layout"
            selected = survey.configs.surveyLayout.rawValue
        case .respondentValue:
            let respondentValue = survey.configs.respondentValue
            title = "Respondent Value (eg. <key>=<value>;)"
            selected = respondentValue.isEmpty ? "(Not Set)" : respondentValue
        default:
            break
        }
        
        lblTitle.text = title
        lblSelected.text = selected
    }
}

class OverviewButtonCell: UITableViewCell {
    @IBOutlet var lblTitle: UILabel!
    
    private var survey: SurveyModel!
    var type: OverviewOptionType!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func initialize(survey: SurveyModel, type: OverviewOptionType) {
        self.survey = survey
        self.type = type
        
        var title = ""
        
        switch type {
        case .removeSurvey:
            title = "Remove Survey"
            lblTitle.textColor = AppColor.error
        default:
            lblTitle.textColor = AppColor.font
        }
        
        lblTitle.text = title
    }
}
