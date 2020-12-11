import Foundation
import XCTest

extension XCUIElement {
  func replaceText(_ newText: String? = nil) {
    press(forDuration: 2.0)
    let selectAll = XCUIApplication().menuItems["Select All"]
    // For empty fields there will be no "Select All", so we need to check
    if selectAll.waitForExistence(timeout: 0.5), selectAll.exists {
      selectAll.tap()
      typeText(String(repeating: XCUIKeyboardKey.delete.rawValue, count: (value as! String).count))
    }
    if let newVal = newText { typeText(newVal) }
  }

  func tapAndText(_ newText: String) {
    tap()
    sleep(1)
    typeText(newText)
  }

  func waitThrow(_: Double = 25) {
    let result = waitForExistence(timeout: 25)
    if !result {
      print(debugDescription)
      XCTAssertTrue(false, "Element not exist")
    }
  }
}

func findElement(_ identifier: String, _ timeout: Double = 25) -> XCUIElement {
  let element = XCUIApplication().descendants(matching: .any).matching(NSPredicate(format: "identifier == '\(identifier)'")).firstMatch
  element.waitThrow(timeout)
  return element
}

func findElementByLabel(_ label: String, _ timeout: Double = 25) -> XCUIElement {
  let element = XCUIApplication().descendants(matching: .any).matching(NSPredicate(format: "label == '\(label)'")).firstMatch
  element.waitThrow(timeout)
  return element
}

func findTableElement(_ cell: String, _ identifier: String, _ timeout: Double = 25) -> XCUIElement {
  let app = XCUIApplication()
  let cell = app.tables.cells[cell].firstMatch
  cell.waitThrow(timeout)

  let element = cell.descendants(matching: .any).matching(NSPredicate(format: "identifier == '\(identifier)'")).firstMatch
  element.waitThrow(timeout)
  return element
}
