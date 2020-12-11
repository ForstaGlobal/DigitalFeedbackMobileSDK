import ConfirmitMobileSDK
import Foundation

struct ProgramTriggerResult {
  var success: Bool
  var status: Bool
}

class TriggerListViewModel {
  private let feedbackManager: TriggerManager
  var programs: [ProgramModel]
  var selectedProgram: ProgramModel?

  init() {
    feedbackManager = TriggerManager()
    programs = []
    do {
      for program in feedbackManager.getPrograms() {
        programs.append(try ProgramModel(program: program))
      }
    } catch {}
  }

  func reloadPrograms() {
    var exists = [String]()
    let curPrograms = feedbackManager.getPrograms()
    for program in curPrograms {
      exists.append("\(program.serverId)_\(program.programKey)")
    }

    programs.removeAll {
      let key = "\($0.program.serverId)_\($0.program.programKey)"
      return !exists.contains(key)
    }

    // update
    processProgram(updatePrograms: curPrograms)
  }

  func downloadAllProgram(completion: @escaping () -> Void) {
    let dispatchGroup = DispatchGroup()
    for program in programs {
      dispatchGroup.enter()
      TriggerSDK.download(serverId: program.program.serverId, programKey: program.program.programKey) { _, _ in
        do {
          let program = try TriggerSDK.getPrograms(serverId: program.program.serverId)
          self.processProgram(updatePrograms: program)
        } catch {}
        dispatchGroup.leave()
      }
    }
    dispatchGroup.notify(queue: .main, work: DispatchWorkItem(block: {
      completion()
        }))
  }

  func downloadProgram(serverId: String, programKey: String, completion: @escaping () -> Void) {
    TriggerSDK.download(serverId: serverId, programKey: programKey) { _, _ in
      do {
        let program = try TriggerSDK.getPrograms(serverId: serverId)
        self.processProgram(updatePrograms: program)
      } catch {}
      completion()
    }
  }

  func getServerName(serverId: String) -> String {
    do {
      guard let server = try ConfirmitServer.getServer(serverId: serverId) else {
        return ""
      }

      return "\(server.name) - "
    } catch {
      return ""
    }
  }

  private func processProgram(updatePrograms: [Program]) {
    for program in updatePrograms {
      do {
        if let exist = programs.firstIndex(where: { $0.program.serverId == program.serverId && $0.program.programKey == program.programKey }) {
          programs[exist] = try ProgramModel(program: program)
        } else {
          programs.append(try ProgramModel(program: program))
        }
      } catch {}
    }
  }
}
