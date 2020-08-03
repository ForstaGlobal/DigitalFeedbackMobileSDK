import Foundation
import MobileSdk
import UIKit

class TriggerListController: UIViewController {
    @IBOutlet var tblContainer: UITableView!
    @IBOutlet var btnAdd: UIBarButtonItem!
    @IBOutlet var lblNoSurvey: UILabel!
    @IBOutlet var imgNoSurvey: UIImageView!
    @IBOutlet var btnDownload: UIButton!
    @IBOutlet var containerNoProgram: UIView!

    private let refreshControl = UIRefreshControl()
    private var viewModel: TriggerListViewModel!
    private var triggerManager = TriggerManager()

    override func viewDidLoad() {
        super.viewDidLoad()

        viewModel = TriggerListViewModel()

        extendedLayoutIncludesOpaqueBars = true

        AppStyle.styleNavBar(navigationController?.navigationBar)

        btnAdd.target = self
        btnAdd.action = #selector(onAddClicked(sender:))
        tblContainer.dataSource = self
        tblContainer.delegate = self
        tblContainer.tableFooterView = UIView(frame: .zero)
        tblContainer.rowHeight = UITableView.automaticDimension
        tblContainer.estimatedRowHeight = 140
        tblContainer.backgroundColor = AppColor.background
        lblNoSurvey.textColor = AppColor.font
        imgNoSurvey.tintColor = AppColor.font
        AppStyle.styleOutlinedButton(btnDownload)

        if #available(iOS 10.0, *) {
            tblContainer.refreshControl = refreshControl
        } else {
            tblContainer.addSubview(refreshControl)
        }

        refreshControl.addTarget(self, action: #selector(onSyncTriggered(_:)), for: .valueChanged)
        refreshControl.tintColor = AppColor.button

        containerNoProgram.isHidden = viewModel.programs.count > 0
    }

    override func viewDidAppear(_ animated: Bool) {
        viewModel.reloadPrograms()
        updateList()
    }

    @objc func onAddClicked(sender: UIBarButtonItem) {
        showDownload()
    }

    @IBAction func onDownloadClick(_ sender: Any) {
        showDownload()
    }

    @objc private func onSyncTriggered(_ sender: Any) {
        viewModel.downloadAllProgram {
            DispatchQueue.main.async {
                self.refreshControl.endRefreshing()
            }
            self.updateList()
        }
    }

    private func showDownload() {
        let modal = AddTriggerController(nibName: "AddTriggerController", bundle: nil)
        modal.delegate = self
        present(modal, animated: true, completion: nil)
    }

    private func updateList() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            self.tblContainer.reloadSections(IndexSet(integer: 0), with: .automatic)
            self.containerNoProgram.isHidden = self.viewModel.programs.count > 0
        }
    }

    private func registerHeaderOrFooter<T: UITableViewHeaderFooterView>(_ type: T.Type) {
        let className = String(describing: type)
        let nib = UINib(nibName: className, bundle: Bundle.main)
        tblContainer.register(nib, forHeaderFooterViewReuseIdentifier: className)
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        guard let program = viewModel.selectedProgram else {
            return
        }

        if segue.identifier == "TriggerListToOverview" {
            if let controller = segue.destination as? TriggerOverviewController {
                controller.extendedLayoutIncludesOpaqueBars = true
                controller.initialize(program: program.program)
            }
        }
    }
}

extension TriggerListController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        viewModel.selectedProgram = viewModel.programs[indexPath.row]
        performSegue(withIdentifier: "TriggerListToOverview", sender: self)
    }
}

extension TriggerListController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return viewModel.programs.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TriggerListCell") as! TriggerListCell
        let program = viewModel.programs[indexPath.row]

        cell.setCell(label: "\(viewModel.getServerName(serverId: program.program.serverId))\(program.program.programKey)")
        cell.selectionStyle = .none
        return cell
    }
}

extension TriggerListController: AddTriggerControllerDelegate {
    func onDigitialFeedbackAdded(serverId: String, programKey: String) {
        triggerManager.setDelegate(serverId: serverId, programKey: programKey)
        viewModel.downloadProgram(serverId: serverId, programKey: programKey) {
            self.updateList()
        }
    }
}

class TriggerListCell: UITableViewCell {
    typealias ClickEvent = (_ sender: UIButton, _ event: UIEvent) -> Void
    @IBOutlet var lblLabel: UILabel!
    var onClick: ClickEvent?

    func setCell(label: String) {
        lblLabel.text = label

        AppStyle.styleCaptionLabel(lblLabel)
    }

    @IBAction func onClick(_ sender: UIButton, forEvent event: UIEvent) {
        if let onClick = self.onClick {
            onClick(sender, event)
        }
    }
}
