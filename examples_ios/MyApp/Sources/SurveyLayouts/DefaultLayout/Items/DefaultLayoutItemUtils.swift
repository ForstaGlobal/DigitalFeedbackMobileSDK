import Foundation
import ConfirmitMobileSDK

enum DefaultLayoutItemUtils {
    static func getSingleAnswerIndexList(section: Int, question: SingleQuestion, excludeCode: String) -> [IndexPath] {
        var startIndex = 0
        if !question.title.isEmpty { startIndex += 1 }
        if !question.text.isEmpty { startIndex += 1 }
        if !question.instruction.isEmpty { startIndex += 1 }
        if question.errors.count > 0 { startIndex += 1 }
        
        var result = [IndexPath]()
        for answer in question.answers {
            if !answer.isHeader && answer.code != excludeCode {
                result.append(IndexPath(row: startIndex, section: section))
            }
            
            if answer.isHeader {
                if !answer.text.isEmpty {
                    startIndex += 1
                }
            } else {
                startIndex += 1
            }
            
            for _ in answer.answers {
                if answer.code != excludeCode {
                    result.append(IndexPath(row: startIndex, section: section))
                }
                
                startIndex += 1
            }
        }
        
        return result
    }
    
    static func getValidationIndex(section: Int, question: DefaultQuestion) -> IndexPath {
        var startIndex = 0
        if !question.title.isEmpty { startIndex += 1 }
        if !question.text.isEmpty { startIndex += 1 }
        if !question.instruction.isEmpty { startIndex += 1 }
        return IndexPath(row: startIndex, section: section)
    }
}
