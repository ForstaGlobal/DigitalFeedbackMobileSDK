import ConfirmitMobileSDK
import Foundation

class TriggerManager {
  func getPrograms() -> [Program] {
    var programs: [Program] = []
    let servers = try! ConfirmitServer.getServers()
    for server in servers {
      do {
        let program = try TriggerSDK.getPrograms(serverId: server.serverId)
        programs += program
      } catch {
        // server doesn't exist
      }
    }
    return programs.sorted(by: { (p1, p2) -> Bool in
      p1.serverId > p2.serverId
        })
  }

  func setAllDelegate() {
    for program in getPrograms() {
      setDelegate(serverId: program.serverId, programKey: program.programKey)
    }
  }

  func setDelegate(serverId: String, programKey: String) {
    TriggerSDK.setCallback(serverId: serverId, programKey: programKey, callback: TriggerCallback())
  }
}
