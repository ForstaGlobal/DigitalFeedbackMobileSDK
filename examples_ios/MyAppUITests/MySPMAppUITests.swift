import XCTest

class MyAppUITests: XCTestCase {
  var app: XCUIApplication!
  let smokeTestProgramKey = "h4Uiwh"
  let smokeTestWebViewProgramKey = "CKhYgo"
  let smokeTestBulkDownloadProgramKey = "KK1ff1"
  let smokeTestServer = "MOB2"

  override func setUp() {
    continueAfterFailure = false

    app = XCUIApplication()
    app.launch()
  }

  override func tearDown() {}

  func testTriggeringSPM() {
    basicTriggerTest(programKey: smokeTestProgramKey)
  }

  func testBulkDownloadSPM() {
    basicTriggerTest(programKey: smokeTestBulkDownloadProgramKey)
  }

  func testBasicWebviewSPM() {
    let programKey = smokeTestWebViewProgramKey

    addProgram(programKey: programKey)

    // Navigate to Triggering Tab
    findElementByLabel(getProgramName(programKey: smokeTestWebViewProgramKey)).tap()

    // Trigger Event
    findElement("btnTestEvent").tap()

    let web = app.webViews["survey_webview"]
    _ = web.staticTexts["Smoke Test Value"].waitForExistence(timeout: 25)

    let nextBtn = web.buttons[">>"]
    _ = nextBtn.waitForExistence(timeout: 25)
    nextBtn.tap()

    _ = web.staticTexts["Thank you very much for your time."].waitForExistence(timeout: 25)

    let okBtn = web.buttons["OK"]
    _ = okBtn.waitForExistence(timeout: 25)
    okBtn.tap()

    // wait for webview to dismiss
    sleep(5)

    XCTAssertFalse(app.webViews["survey_webview"].exists)
  }

  private func addProgram(programKey: String) {
    // Navigate to Download programs
    app.navigationBars.buttons.element(boundBy: 0).tap()

    // Select server and enter program key
    findElement("btnServerHost").tap()

    findElementByLabel(smokeTestServer).tap()

    findElement("txtProgramKey").tapAndText(programKey)

    findElement("btnDownload").tap()
  }

  private func basicTriggerTest(programKey: String) {
    addProgram(programKey: programKey)

    // Navigate to Triggering Tab
    findElementByLabel(getProgramName(programKey: programKey)).tap()

    // Set Custom Data
    findElement("btnCustomData").tap()
    findElement("txtTextbox").tapAndText("hello1=test1;hello2=test2")
    findElement("btnSave").tap()

    // Trigger Event
    findElement("btnTestEvent").tap()

    sleep(3)

    // Start Survey
    _ = findElementByLabel("test1;test2")
    findElement("btnNext").tap()

    findElement("txtSurveyText").tapAndText("hello world")
    findElementByLabel("OET").tap()
    findElement("btnNext").tap()

    findElement("txtSurveyText").tapAndText("3")
    findElementByLabel("OEN").tap()
    findElement("btnNext").tap()

    findElementByLabel("answer two").tap()
    findElement("btnNext").tap()

    findElementByLabel("value two").tap()
    findElementByLabel("value three").tap()
    findElement("btnNext").tap()

    findElement("btnOk").tap()

    sleep(3)
  }

  private func getProgramName(programKey: String) -> String {
    return "\(smokeTestServer) - \(programKey)"
  }
}
