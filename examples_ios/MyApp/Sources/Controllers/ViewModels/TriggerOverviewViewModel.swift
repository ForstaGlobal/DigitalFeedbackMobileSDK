import ConfirmitMobileSDK
import Foundation

class TriggerOverviewViewModel {
  private(set) var program: ProgramModel!

  init(program: Program) throws {
    self.program = try ProgramModel(program: program)
  }

  func notifyEvent(event: String, data: [String: String]) {
    TriggerSDK.notifyEvent(serverId: program.program.serverId, programKey: program.program.programKey, event: event, data: data)
  }

  func removeProgram() -> Bool {
    do {
      try TriggerSDK.deleteProgram(serverId: program.program.serverId, programKey: program.program.programKey, deleteCustomData: true)
      TriggerSDK.removeCallback(serverId: program.program.serverId, programKey: program.program.programKey)
      return true
    } catch {
      return false
    }
  }

  func downloadCounters() {
    TriggerSDK.downloadCounters(serverId: program.program.serverId, programKey: program.program.programKey) { _, _ in }
  }

  func downloadProgram(completion: @escaping () -> Void) {
    TriggerSDK.download(serverId: program.program.serverId, programKey: program.program.programKey) { _, _ in
      completion()
    }
  }
}
