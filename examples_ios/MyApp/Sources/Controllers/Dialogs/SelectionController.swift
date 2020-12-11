import ConfirmitMobileSDK
import Foundation
import UIKit

protocol GeneralDialogDelegate: class {
  func onValueChanged(key: String, newValue: String)
}

class SelectionController: UIViewController {
  @IBOutlet var statusBar: UIView!
  @IBOutlet var navBar: UINavigationBar!
  @IBOutlet var tableView: UITableView!

  weak var delegate: GeneralDialogDelegate?

  private var key: String = ""
  private var values: [String] = []
  private var labels: [String] = []
  private var selectedValue: String = ""

  override func viewDidLoad() {
    super.viewDidLoad()
    view.backgroundColor = AppColor.background
    AppStyle.styleNavBar(navBar)
    AppStyle.styleStatusBar(statusBar)

    navBar.topItem?.title = title

    registerNib(SelectOptionCell.self)

    tableView.dataSource = self
    tableView.delegate = self
    tableView.tableFooterView = UIView(frame: .zero)
    tableView.rowHeight = UITableView.automaticDimension
    tableView.estimatedRowHeight = 140
  }

  override var preferredStatusBarStyle: UIStatusBarStyle {
    return .default
  }

  func initialize(key: String, title: String, labels: [String], values: [String], selectedValue: String) {
    self.key = key
    self.title = title
    self.labels = labels
    self.values = values
    self.selectedValue = selectedValue
  }

  private func registerNib<T: UITableViewCell>(_ type: T.Type) {
    let className = String(describing: type)
    let nib = UINib(nibName: className, bundle: Bundle.main)
    tableView.register(nib, forCellReuseIdentifier: className)
  }

  @IBAction func onCloseClicked(_ sender: Any) {
    dismissKeyboard()
    dismiss(animated: true, completion: nil)
  }
}

extension SelectionController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    selectedValue = values[indexPath.row]
    delegate?.onValueChanged(key: key, newValue: selectedValue)
    tableView.deselectRow(at: indexPath, animated: true)
    DispatchQueue.main.async {
      self.dismiss(animated: true, completion: nil)
    }
  }
}

extension SelectionController: UITableViewDataSource {
  func numberOfSections(in tableView: UITableView) -> Int {
    return 1
  }

  func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    return labels.count
  }

  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    let cell = tableView.dequeueReusableCell(withIdentifier: "SelectOptionCell") as! SelectOptionCell
    cell.lblLabel.text = labels[indexPath.row]
    cell.set(selected: values[indexPath.row] == selectedValue)
    return cell
  }
}
