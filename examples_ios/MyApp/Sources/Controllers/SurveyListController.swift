import MobileSdk
import UIKit

class SurveyListController: UIViewController {
    @IBOutlet var btnSync: UIBarButtonItem!
    @IBOutlet var btnAdd: UIBarButtonItem!
    @IBOutlet var tableView: UITableView!
    @IBOutlet var containerNoSurvey: UIView!
    @IBOutlet var lblNoSurvey: UILabel!
    @IBOutlet var imgNoSurvey: UIImageView!
    @IBOutlet var btnDownload: UIButton!
    
    private let refreshControl = UIRefreshControl()
    private var viewModel: SurveyListViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = SurveyListViewModel()
        
        extendedLayoutIncludesOpaqueBars = true
        
        AppStyle.styleNavBar(navigationController?.navigationBar)
        
        btnAdd.target = self
        btnAdd.action = #selector(onAddClicked(sender:))
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView(frame: .zero)
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = 140
        tableView.backgroundColor = AppColor.background
        lblNoSurvey.textColor = AppColor.font
        imgNoSurvey.tintColor = AppColor.font
        AppStyle.styleOutlinedButton(btnDownload)
        
        if #available(iOS 10.0, *) {
            tableView.refreshControl = refreshControl
        } else {
            tableView.addSubview(refreshControl)
        }
        refreshControl.addTarget(self, action: #selector(onSyncTriggered(_:)), for: .valueChanged)
        refreshControl.tintColor = AppColor.button
        
        containerNoSurvey.isHidden = viewModel.surveys.count > 0
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewModel.reloadSurveys()
        updateSurveyList(wait: false)
        downloadAll()
    }
    
    func downloadSurvey(serverId: String, surveyId: String) {
        viewModel.download(serverId: serverId, surveyId: surveyId) { _ in
            self.updateSurveyList()
        }
    }
    
    private func showDownload() {
        let modal = AddSurveyController(nibName: "AddSurveyController", bundle: nil)
        modal.delegate = self
        present(modal, animated: true, completion: nil)
    }
    
    private func downloadAll(pulled: Bool = false) {
        viewModel.downloadAll { _ in
            DispatchQueue.main.async {
                self.refreshControl.endRefreshing()
            }
            self.updateSurveyList()
        }
    }
    
    @objc private func onSyncTriggered(_ sender: Any) {
        downloadAll(pulled: true)
    }
    
    @IBAction func onDownloadClick(_ sender: Any) {
        showDownload()
    }
    
    @objc func onAddClicked(sender: UIBarButtonItem) {
        showDownload()
    }
    
    func updateSurveyList(wait: Bool = true) {
        if wait {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                self.tableView.reloadSections(IndexSet(integer: 0), with: .automatic)
                self.containerNoSurvey.isHidden = self.viewModel.surveys.count > 0
            }
        } else {
            DispatchQueue.main.async {
                self.tableView.reloadSections(IndexSet(integer: 0), with: .automatic)
                self.containerNoSurvey.isHidden = self.viewModel.surveys.count > 0
            }
        }
    }
    
    func getSurveyKey(host: String, surveyId: String) -> String {
        return "\(host)_\(surveyId)"
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "SurveyListToOverview" {
            guard let survey = viewModel.selectedSurvey else {
                return
            }
            
            if let controller = segue.destination as? OverviewController {
                controller.extendedLayoutIncludesOpaqueBars = true
                controller.initialize(serverId: survey.survey.serverId, surveyId: survey.survey.surveyId)
            }
        }
    }
}

extension SurveyListController: SurveyLayoutControllerDelegate {
    func onCloseError(error: String) {
        Utils.showError(controller: self, error: error)
    }
}

extension SurveyListController: AddSurveyControllerDelegate {
    func onSurveyAdded(serverId: String, surveyId: String) {
        viewModel.download(serverId: serverId, surveyId: surveyId) { _ in
            self.updateSurveyList()
        }
    }
}

extension SurveyListController: SurveyListCellDelegate {
    func onStartClicked(survey: SurveyModel) {
        SurveyManager().launchSurvey(controller: self, layoutDelegate: self, survey: survey)
    }
}

extension SurveyListController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        viewModel.selectedSurvey = viewModel.surveys[indexPath.row]
        performSegue(withIdentifier: "SurveyListToOverview", sender: self)
    }
}

extension SurveyListController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return viewModel.surveys.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SurveyListCell") as! SurveyListCell
        let survey = viewModel.surveys[indexPath.row]
        cell.initialize(survey: survey)
        cell.delegate = self
        cell.btnStart.accessibilityIdentifier = "btnStart_\(survey.survey.surveyId)"
        return cell
    }
}

protocol SurveyListCellDelegate: class {
    func onStartClicked(survey: SurveyModel)
}

class SurveyListCell: UITableViewCell {
    @IBOutlet var lblName: UILabel!
    @IBOutlet var imgSurveyId: UIImageView!
    @IBOutlet var lblSurveyId: UILabel!
    @IBOutlet var btnStart: UIButton!
    
    weak var delegate: SurveyListCellDelegate?
    
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
    }
    
    @IBAction func onStartClicked(_ sender: Any) {
        delegate?.onStartClicked(survey: survey)
    }
}
