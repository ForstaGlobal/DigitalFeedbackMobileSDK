import ConfirmitMobileSDK
import Foundation

@objc(MobileSdkSurvey)
class MobileSdkSurvey: NSObject {
  private let surveyManager = MobileSdkSurveyManager()
  
  @objc(getTitle:withProgramKey:withSurveyId:withResolver:withRejecter:)
  func getTitle(serverId: String, programKey: String, surveyId: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      resolve(page.title.get())
    }
  }
  
  @objc(getText:withProgramKey:withSurveyId:withResolver:withRejecter:)
  func getText(serverId: String, programKey: String, surveyId: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      resolve(page.text?.get())
    }
  }
  
  @objc(getQuestions:withProgramKey:withSurveyId:withResolver:withRejecter:)
  func getQuestions(serverId: String, programKey: String, surveyId: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var results: [[String: Any]] = []
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      for question in page.questions {
        let nodeType = "\(question.nodeType)"
        var result: [String: Any] = [
          "id": question.id,
          "nodeType": nodeType.uppercased()
        ]
        
        switch question {
        case let question as InfoQuestion:
          result["title"] = question.title.get()
          result["text"] = question.text.get()
          result["instruction"] = question.instruction.get()
        case let question as TextQuestion:
          result["title"] = question.title.get()
          result["text"] = question.text.get()
          result["instruction"] = question.instruction.get()
        case let question as NumericQuestion:
          result["title"] = question.title.get()
          result["text"] = question.text.get()
          result["instruction"] = question.instruction.get()
        case let question as SingleQuestion:
          result["title"] = question.title.get()
          result["text"] = question.text.get()
          result["instruction"] = question.instruction.get()
          result["appearance"] = question.appearance.rawValue
          result["answers"] = getAnswers(questionAnswers: question.answers)
        case let question as MultiQuestion:
          result["title"] = question.title.get()
          result["text"] = question.text.get()
          result["instruction"] = question.instruction.get()
          result["appearance"] = question.appearance.rawValue
          result["answers"] = getAnswers(questionAnswers: question.answers)
        default:
          break
        }
        
        var errors: [[String: String]] = []
        if let question = question as? DefaultQuestion {
          for error in question.errors {
            errors.append([
              "code": "\(error.code)",
              "message": error.message
            ])
          }
        }
        result["errors"] = errors
        results.append(result)
      }
    }
    resolve(results)
  }
  
  private func getAnswers(questionAnswers: [QuestionAnswer]) -> [[String: Any]] {
    var answers: [[String: Any]] = []
    for answer in questionAnswers {
      var result: [String: Any] = [
        "code": answer.code,
        "text": answer.text.get(),
        "isHeader": answer.isHeader
      ]
      var groupAnswer: [[String: Any]] = []
      for innerAnswer in answer.answers {
        groupAnswer.append([
          "code": innerAnswer.code,
          "text": innerAnswer.text.get(),
          "isHeader": innerAnswer.isHeader
        ])
      }
      result["answers"] = groupAnswer
      answers.append(result)
    }
    return answers
  }
  
  // Question Control
  @objc(setText:withProgramKey:withSurveyId:withQuestionId:withAnswer:)
  func setText(serverId: String, programKey: String, surveyId: String, questionId: String, answer: String) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let text = question as? TextQuestion {
        text.setValue(value: answer)
      }
    }
  }
  
  @objc(getText:withProgramKey:withSurveyId:withQuestionId:withResolver:withRejecter:)
  func getText(serverId: String, programKey: String, surveyId: String, questionId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let text = question as? TextQuestion {
        resolve(text.getValue())
      }
    }
  }
  
  @objc(setNumeric:withProgramKey:withSurveyId:withQuestionId:withAnswer:withIsDouble:)
  func setNumeric(serverId: String, programKey: String, surveyId: String, questionId: String, answer: Double, isDouble: Bool) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let numeric = question as? NumericQuestion {
        if isDouble {
          numeric.setValue(value: answer)
        } else {
          numeric.setValue(value: Int(answer))
        }
      }
    }
  }
  
  @objc(getNumeric:withProgramKey:withSurveyId:withQuestionId:withResolver:withRejecter:)
  func getNumeric(serverId: String, programKey: String, surveyId: String, questionId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let numeric = question as? TextQuestion {
        resolve(numeric.getValue())
      }
    }
  }
  
  @objc(setSingle:withProgramKey:withSurveyId:withQuestionId:withCode:)
  func setSingle(serverId: String, programKey: String, surveyId: String, questionId: String, code: String) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let single = question as? SingleQuestion {
        let allAnswers = single.answers.flatMap {
          if $0.isHeader {
            return $0.answers
          }
          return [$0]
        }
        
        if let questionAnswer = allAnswers.first(where: { $0.code == code }) {
          single.select(answer: questionAnswer)
        }
      }
    }
  }
  
  @objc(getSingle:withProgramKey:withSurveyId:withQuestionId:withResolver:withRejecter:)
  func getSingle(serverId: String, programKey: String, surveyId: String, questionId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    var result: [String: Any] = [:]
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let single = question as? SingleQuestion, let selected = single.selected() {
        result["code"] = selected.code
        result["text"] = selected.text
        result["isHeader"] = selected.isHeader
      }
    }
    resolve(result)
  }
  
  @objc(setMulti:withProgramKey:withSurveyId:withQuestionId:withCode:withSelected:)
  func setMulti(serverId: String, programKey: String, surveyId: String, questionId: String, code: String, selected: Bool) {
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let multi = question as? MultiQuestion {
        let allAnswers = multi.answers.flatMap {
          if $0.isHeader {
            return $0.answers
          }
          return [$0]
        }
        
        if let questionAnswer = allAnswers.first(where: { $0.code == code }) {
          multi.set(answer: questionAnswer, select: selected)
        }
      }
    }
  }
  
  @objc(getMulti:withProgramKey:withSurveyId:withQuestionId:withResolver:withRejecter:)
  func getMulti(serverId: String, programKey: String, surveyId: String, questionId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    var result: [[String: Any]] = []
    getSurveyPage(serverId: serverId, programKey: programKey, surveyId: surveyId) { page in
      if let question = page.questions.first(where: { $0.id == questionId }), let multi = question as? MultiQuestion {
        let allAnswers = multi.answers.flatMap {
          if $0.isHeader {
            return $0.answers
          }
          return [$0]
        }
        
        for answer in allAnswers {
          if multi.get(answer: answer) {
            result.append([
              "code": answer.code,
              "text": answer.text.get(),
              "isHeader": answer.isHeader
            ])
          }
        }
      }
    }
    resolve(result)
  }
  
  private func getSurveyPage(serverId: String, programKey: String, surveyId: String, block: (_ page: SurveyPage) -> Void) {
    if let wrapper =  surveyManager.getSurvey(serverId: serverId, programKey: programKey, surveyId: surveyId), let surveyFrame = wrapper.surveyFrame, let page = surveyFrame.page {
      block(page)
    }
  }
    
  // Survey Control
  @objc(startSurvey:withProgramKey:withSurveyId:withData:withRespondentValues:withResolver:withRejecter:)
  func startSurvey(serverId: String, programKey: String, surveyId: String, data: NSDictionary, respondentValues: NSDictionary, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    if let wrapper = surveyManager.getSurvey(serverId: serverId, programKey: programKey, surveyId: surveyId) {
      do {
        if let result = try wrapper.startSurvey(customData: data.swiftDictionary, respondentValues: respondentValues.swiftDictionary) {
          resolve([
            "success": result.success,
            "message": result.message
          ])
        } else {
          reject("survey", "failed to start survey", nil)
        }
      } catch {
        // let reject resolve the
        reject("survey", "failed to start survey", error)
      }
    }
  }
  
  @objc(next:withProgramKey:withSurveyId:withResolver:withRejecter:)
  func next(serverId: String, programKey: String, surveyId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    surveyControlAction(serverId: serverId, programKey: programKey, surveyId: surveyId, resolve: resolve, reject: reject) { surveyFrame in
      surveyFrame.next()
    }
  }
  
  @objc(back:withProgramKey:withSurveyId:withResolver:withRejecter:)
  func back(serverId: String, programKey: String, surveyId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    surveyControlAction(serverId: serverId, programKey: programKey, surveyId: surveyId, resolve: resolve, reject: reject) { surveyFrame in
      surveyFrame.back()
    }
  }
  
  @objc(quit:withProgramKey:withSurveyId:withUpload:withResolver:withRejecter:)
  func quit(serverId: String, programKey: String, surveyId: String, upload: Bool, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    surveyControlAction(serverId: serverId, programKey: programKey, surveyId: surveyId, resolve: resolve, reject: reject) { surveyFrame in
      surveyFrame.quit(upload: upload)
    }
  }
  
  private func surveyControlAction(serverId: String, programKey: String, surveyId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock, block: (_ surveyFrame: SurveyFrame) -> SurveyFrameActionResult) {
    if let wrapper = surveyManager.getSurvey(serverId: serverId, programKey: programKey, surveyId: surveyId), let surveyFrame = wrapper.surveyFrame {
      let result = block(surveyFrame)
      resolve([
        "success": result.success,
        "message": result.message
      ])
    } else {
      reject("survey", "failed to find active survey for serverId: \(serverId) programKey: \(programKey) surveyId: \(surveyId)", nil)
    }
  }
}
