import ConfirmitMobileSDK
import Foundation

class ProgramModel {
  private(set) var program: Program
  private(set) var configs: ProgramConfig

  init(program: Program) throws {
    self.program = program
    configs = ProgramConfig(serverId: program.serverId, programKey: program.programKey)
  }

  func update(program: Program) throws {
    self.program = program
  }

  func getCustomDataValues() -> [String: String] {
    var values = [String: String]()
    let raw = configs.customData
    for pair in raw.split(separator: ";") {
      let keyValue = pair.split(separator: "=")
      if keyValue.count == 2 {
        let key = String(keyValue[0])
        let value = String(keyValue[1])
        values[key] = value
      }
    }
    return values
  }
}
