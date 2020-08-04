import Foundation
import ConfirmitMobileSDK
import UIKit

enum TriggerOverviewOptionType: String {
    case testEvent
    case customData
    case removeProgram
}

class TriggerOverviewController: UIViewController {
    @IBOutlet var tableView: UITableView!

    private var viewModel: TriggerOverviewViewModel!
    private var initialized: Bool = false

    private let options: [TriggerOverviewOptionType] = [.testEvent, .customData, .removeProgram]

    private let refreshControl = UIRefreshControl()

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

        if #available(iOS 10.0, *) {
            tableView.refreshControl = refreshControl
        } else {
            tableView.addSubview(refreshControl)
        }

        refreshControl.addTarget(self, action: #selector(onSyncTriggered(_:)), for: .valueChanged)
        refreshControl.tintColor = AppColor.button

        viewModel.downloadCounters()
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }

    func initialize(program: Program) {
        do {
            viewModel = try TriggerOverviewViewModel(program: program)
            initialized = true
        } catch {}
    }

    @objc private func onSyncTriggered(_ sender: Any) {
        viewModel.downloadProgram {
            DispatchQueue.main.async {
                self.refreshControl.endRefreshing()
            }
        }
    }
}

extension TriggerOverviewController: UITableViewDelegate {
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
        case let optionCell as TriggerOverviewOptionCell:
            onOptionSelected(program: viewModel.program, type: optionCell.type)
        case let buttonCell as TriggerOverviewButtonCell:
            onOptionSelected(program: viewModel.program, type: buttonCell.type)
        default:
            break
        }
    }
}

extension TriggerOverviewController: UITableViewDataSource {
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
            let cell = tableView.dequeueReusableCell(withIdentifier: "TriggerOverviewDetailCell") as! TriggerOverviewDetailCell
            cell.initialize(program: viewModel.program)
            return cell
        } else {
            let type = options[indexPath.row]

            switch type {
            case .removeProgram, .testEvent:
                let cell = tableView.dequeueReusableCell(withIdentifier: "TriggerOverviewButtonCell") as! TriggerOverviewButtonCell
                cell.initialize(program: viewModel.program, type: options[indexPath.row])
                return cell
            default:
                let cell = tableView.dequeueReusableCell(withIdentifier: "TriggerOverviewOptionCell") as! TriggerOverviewOptionCell
                cell.initialize(program: viewModel.program, type: options[indexPath.row])
                return cell
            }
        }
    }
}

extension TriggerOverviewController: GeneralDialogDelegate {
    private func showSelection(type: OverviewOptionType, title: String, labels: [String], values: [String], selectedValue: String) {
        let controller = SelectionController(nibName: "SelectionController", bundle: nil)
        controller.initialize(key: type.rawValue, title: title, labels: labels, values: values, selectedValue: selectedValue)
        controller.delegate = self
        present(controller, animated: true, completion: nil)
    }

    private func showTextbox(type: TriggerOverviewOptionType, title: String, selectedValue: String) {
        let controller = TextboxController(nibName: "TextboxController", bundle: nil)
        controller.initialize(key: type.rawValue, title: title, selectedValue: selectedValue)
        controller.delegate = self
        present(controller, animated: true, completion: nil)
    }

    func onOptionSelected(program: ProgramModel, type: TriggerOverviewOptionType) {
        switch type {
        case .customData:
            let selectedValue = program.configs.customData
            let title = "Enter Custom Data Values"
            showTextbox(type: type, title: title, selectedValue: selectedValue)
        case .removeProgram:
            if viewModel.removeProgram() {
                navigationController?.popViewController(animated: true)
            } else {
                Utils.showError(controller: self, error: "Failed to remove program")
            }
        case .testEvent:
            viewModel.notifyEvent(event: "onTestEvent", data: program.getCustomDataValues())
        }
    }

    func onValueChanged(key: String, newValue: String) {
        guard let type = TriggerOverviewOptionType(rawValue: key) else {
            return
        }

        switch type {
        case .customData:
            viewModel.program.configs.customData = newValue
        default:
            break
        }

        tableView.reloadData()
    }
}

class TriggerOverviewDetailCell: UITableViewCell {
    @IBOutlet var lblName: UILabel!

    private var program: ProgramModel!

    override func awakeFromNib() {
        super.awakeFromNib()
    }

    func initialize(program: ProgramModel) {
        self.program = program
        lblName.text = program.program.programKey
    }
}

class TriggerOverviewOptionCell: UITableViewCell {
    @IBOutlet var imgIcon: UIImageView!
    @IBOutlet var lblTitle: UILabel!
    @IBOutlet var lblSelected: UILabel!

    private var program: ProgramModel!
    var type: TriggerOverviewOptionType!

    override func awakeFromNib() {
        super.awakeFromNib()

        AppStyle.styleCaptionLabel(lblSelected)
        AppStyle.styleCaptionImage(imgIcon)
    }

    func initialize(program: ProgramModel, type: TriggerOverviewOptionType) {
        self.program = program
        self.type = type

        var title = ""
        var selected = ""
        switch type {
        case .customData:
            let cutomData = program.configs.customData
            title = "Custom Data Value (eg. <key>=<value>;)"
            selected = cutomData.isEmpty ? "(Not Set)" : cutomData
        default:
            break
        }

        lblTitle.text = title
        lblSelected.text = selected
    }
}

class TriggerOverviewButtonCell: UITableViewCell {
    @IBOutlet var lblTitle: UILabel!

    private var program: ProgramModel!
    var type: TriggerOverviewOptionType!

    override func awakeFromNib() {
        super.awakeFromNib()
    }

    func initialize(program: ProgramModel, type: TriggerOverviewOptionType) {
        self.program = program
        self.type = type

        var title = ""

        switch type {
        case .testEvent:
            title = "Trigger onTestEvent"
            lblTitle.textColor = AppColor.font
            accessibilityIdentifier = "btnTestEvent"
        case .removeProgram:
            title = "Remove Program"
            lblTitle.textColor = AppColor.error
        default:
            lblTitle.textColor = AppColor.font
        }

        lblTitle.text = title
    }
}
