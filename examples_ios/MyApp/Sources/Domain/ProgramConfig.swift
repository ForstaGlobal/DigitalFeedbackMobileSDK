import Foundation

enum ProgramConfigKey: String {
  case customData
}

class ProgramConfig {
  let serverId: String
  let programKey: String

  init(serverId: String, programKey: String) {
    self.serverId = serverId
    self.programKey = programKey
  }

  var customData: String {
    get {
      return get(.customData, "")
    }
    set {
      set(.customData, newValue)
    }
  }

  private func get<T>(_ keyEnum: ProgramConfigKey, _ def: T) -> T {
    return Utils.getSetting(key(keyEnum), def)
  }

  private func set<T>(_ keyEnum: ProgramConfigKey, _ value: T) {
    Utils.setSeting(key(keyEnum), value)
  }

  private func key(_ key: ProgramConfigKey) -> String {
    return "\(serverId)_\(programKey)_\(key.rawValue)"
  }
}
