import ConfirmitMobileSDK
import Foundation
import UIKit

class DefaultLayoutController: SurveyLayoutController {
  @IBOutlet var navBar: UINavigationBar!
  @IBOutlet var statusBar: UIView!
  @IBOutlet var tblSurvey: UITableView!
  @IBOutlet var btnNext: UIButton!
  @IBOutlet var btnOk: UIButton!
  @IBOutlet var btnBack: UIButton!
  @IBOutlet var containerSystem: UIView!
  @IBOutlet var imgSystem: UIImageView!
  @IBOutlet var lblSystem: UILabel!
  @IBOutlet var titleSystem: UILabel!

  var layoutSections: [DefaultLayoutSection] = []

  override func viewDidLoad() {
    super.viewDidLoad()

    hideKeyboardWhenTap()

    containerSystem.isHidden = true

    DefaultLayoutStyle.styleNavBar(navBar: navBar, statusBar: statusBar, title: surveyModel.survey.name)
    DefaultLayoutStyle.styleTextButton(btnNext)
    DefaultLayoutStyle.styleTextButton(btnOk)
    DefaultLayoutStyle.styleTextButton(btnBack)
    titleSystem.tintColor = DefaultLayoutColor.font
    titleSystem.font = UIFont.boldSystemFont(ofSize: 16)
    lblSystem.tintColor = DefaultLayoutColor.font
    lblSystem.font = UIFont.boldSystemFont(ofSize: 16)

    registerNib(DefaultLayoutLabelCell.self)
    registerNib(DefaultLayoutIconLabelCell.self)
    registerNib(DefaultLayoutTextBoxCell.self)
    registerNib(DefaultLayoutClickLabelCell.self)
    registerHeaderOrFooter(DefaultLayoutSectionHeaderView.self)

    tblSurvey.dataSource = self
    tblSurvey.delegate = self
    tblSurvey.rowHeight = UITableView.automaticDimension
    tblSurvey.estimatedRowHeight = 140
    tblSurvey.backgroundColor = DefaultLayoutColor.white

    loadFrame(surveyFrameDelegate: self)
  }

  @IBAction func onCloseClicked(_ sender: Any) {
    _ = surveyFrame.quit(upload: true)
  }

  @IBAction func onOkClicked(_ sender: Any) {
    closeSurvey()
  }

  @IBAction func onBackClicked(_ sender: Any) {
    _ = surveyFrame.back()
  }

  @IBAction func onNextClicked(_ sender: Any) {
    _ = surveyFrame.next()
  }

  private func registerNib<T: UITableViewCell>(_ type: T.Type) {
    let className = String(describing: type)
    let nib = UINib(nibName: className, bundle: Bundle.main)
    tblSurvey.register(nib, forCellReuseIdentifier: className)
  }

  private func registerHeaderOrFooter<T: UITableViewHeaderFooterView>(_ type: T.Type) {
    let className = String(describing: type)
    let nib = UINib(nibName: className, bundle: Bundle.main)
    tblSurvey.register(nib, forHeaderFooterViewReuseIdentifier: className)
  }
}

extension DefaultLayoutController: SurveyFrameDelegate {
  func onSurveyPageReady(page: SurveyPage) {
    var sections = [DefaultLayoutSection]()
    if !page.title.isEmpty {
      sections.append(DefaultLayoutPageHeaderSection(page: page))
    }

    for question in page.questions {
      sections.append(DefaultLayoutQuestionSection(question: question))
    }
    layoutSections = sections

    tblSurvey.reloadData()
    DispatchQueue.main.async {
      self.tblSurvey.scrollToRow(at: IndexPath(row: 0, section: 0), at: .top, animated: false)
    }
    updateNavigationButton()
  }

  func onSurveyErrored(page: SurveyPage, values: [String: String?], error: Error) {
    layoutSections = []
    tblSurvey.reloadData()
    updateNavigationSystemButton()
    containerSystem.isHidden = false
    titleSystem.text = surveyFrame.page!.title.get()
    lblSystem.text = surveyFrame.page!.text!.get()
    imgSystem.tintColor = DefaultLayoutColor.error
    imgSystem.image = UIImage(named: "outline_sentiment_very_dissatisfied_black_48pt")
  }

  func onSurveyFinished(page: SurveyPage, values: [String: String?]) {
    layoutSections = []
    tblSurvey.reloadData()
    updateNavigationSystemButton()
    containerSystem.isHidden = false
    titleSystem.text = surveyFrame.page!.title.get()
    lblSystem.text = surveyFrame.page!.text!.get()
    imgSystem.tintColor = DefaultLayoutColor.button
    imgSystem.image = UIImage(named: "outline_thumb_up_alt_black_48pt")
  }

  func onSurveyQuit(values: [String: String?]) {
    closeSurvey()
  }

  private func updateNavigationSystemButton() {
    btnBack.isHidden = true
    btnNext.isHidden = true
    btnOk.isHidden = false
  }

  private func updateNavigationButton() {
    btnBack.isHidden = !surveyFrame.page!.showBackward
    btnNext.isHidden = !surveyFrame.page!.showForward
    btnOk.isHidden = !surveyFrame.page!.showOk
    btnBack.setTitle(surveyFrame.page!.backwardText, for: .normal)
    btnNext.setTitle(surveyFrame.page!.forwardText, for: .normal)
    btnOk.setTitle(surveyFrame.page!.okText, for: .normal)
  }
}

extension DefaultLayoutController: DefaultLayoutQuestionSectionDelegate {
  func onRefreshRow(add: [IndexPath], refresh: [IndexPath], remove: [IndexPath]) {
    tblSurvey.beginUpdates()
    tblSurvey.deleteRows(at: remove, with: .automatic)
    tblSurvey.insertRows(at: add, with: .automatic)
    tblSurvey.reloadRows(at: refresh, with: .none)
    tblSurvey.endUpdates()
  }
}

extension DefaultLayoutController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
    return tableView.dequeueReusableHeaderFooterView(withIdentifier: "DefaultLayoutSectionHeaderView")
  }

  func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
    return 48
  }

  func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
    return CGFloat.leastNonzeroMagnitude
  }
}

extension DefaultLayoutController: UITableViewDataSource {
  func numberOfSections(in tableView: UITableView) -> Int {
    return layoutSections.count
  }

  func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    let layoutSection = layoutSections[section]
    return layoutSection.items.count
  }

  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    let layoutSection = layoutSections[indexPath.section]
    layoutSection.load(delegate: self, section: indexPath.section)
    let layoutItem = layoutSection.items[indexPath.row]

    switch layoutItem {
    case let item as DefaultLayoutLabel:
      let cell = tableView.dequeueReusableCell(withIdentifier: "DefaultLayoutLabelCell") as! DefaultLayoutLabelCell
      cell.setItem(item: item)
      return cell
    case let item as DefaultLayoutIconLabel:
      let cell = tableView.dequeueReusableCell(withIdentifier: "DefaultLayoutIconLabelCell") as! DefaultLayoutIconLabelCell
      cell.setItem(item: item)
      return cell
    case let item as DefaultLayoutTextBox:
      let cell = tableView.dequeueReusableCell(withIdentifier: "DefaultLayoutTextBoxCell") as! DefaultLayoutTextBoxCell
      cell.delegate = layoutSection
      cell.setItem(item: item)
      return cell
    case let item as DefaultLayoutClickLabel:
      let cell = tableView.dequeueReusableCell(withIdentifier: "DefaultLayoutClickLabelCell") as! DefaultLayoutClickLabelCell
      cell.delegate = layoutSection
      cell.setItem(item: item)
      return cell
    default:
      let cell = tableView.dequeueReusableCell(withIdentifier: "DefaultLayoutIconLabelCell") as! DefaultLayoutIconLabelCell
      cell.setItem(item: DefaultLayoutIconLabel(value: QuestionText(rawText: "Invalid question layout"), iconName: "outline_error_outline_black_24pt"))
      return cell
    }
  }
}
