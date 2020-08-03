import Foundation

class AppConfigs {
    static var onDemandDownloadPackage: Bool {
        get {
            return get(.onDemandDownloadPackage, true)
        }
        set {
            set(.onDemandDownloadPackage, newValue)
        }
    }
    
    static var validateOnAnswerChange: Bool {
        get {
            return get(.validateOnAnswerChange, false)
        }
        set {
            set(.validateOnAnswerChange, newValue)
        }
    }
    
    static var uniqueId: String {
        get {
            return get(.uniqueId, UUID().uuidString)
        }
        set {
            set(.uniqueId, newValue)
        }
    }
    
    static var htmlText: Bool {
        get {
            return get(.htmlText, false)
        }
        set {
            set(.htmlText, newValue)
        }
    }
    
    static var uploadImcomplete: Bool {
        get {
            return get(.uploadIncomplete, true)
        }
        set {
            set(.uploadIncomplete, newValue)
        }
    }
    
    static var triggerTestMode: Bool {
        get {
            return get(.triggerTestMode, false)
        }
        set {
            set(.triggerTestMode, newValue)
        }
    }
    
    private static func get<T>(_ key: ConfigKey, _ def: T) -> T {
        return Utils.getSetting(key.rawValue, def)
    }
    
    private static func set<T>(_ key: ConfigKey, _ value: T) {
        Utils.setSeting(key.rawValue, value)
    }
}

class SurveyConfigs {
    let serverId: String
    let surveyId: String
    
    init(serverId: String, surveyId: String) {
        self.serverId = serverId
        self.surveyId = surveyId
    }
    
    var surveyLayout: SurveyLayout {
        get {
            return SurveyLayout(rawValue: get(.surveyLayout, SurveyLayout.default.rawValue)) ?? .default
        }
        set {
            set(.surveyLayout, newValue.rawValue)
        }
    }
    
    var respondentValue: String {
        get {
            return get(.respondentValue, "")
        }
        set {
            set(.respondentValue, newValue)
        }
    }
    
    private func get<T>(_ keyEnum: ConfigKey, _ def: T) -> T {
        return Utils.getSetting(key(keyEnum), def)
    }
    
    private func set<T>(_ keyEnum: ConfigKey, _ value: T) {
        Utils.setSeting(key(keyEnum), value)
    }
    
    private func key(_ key: ConfigKey) -> String {
        return "\(serverId)_\(surveyId)_\(key.rawValue)"
    }
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
    
    private func get<T>(_ keyEnum: ConfigKey, _ def: T) -> T {
        return Utils.getSetting(key(keyEnum), def)
    }
    
    private func set<T>(_ keyEnum: ConfigKey, _ value: T) {
        Utils.setSeting(key(keyEnum), value)
    }
    
    private func key(_ key: ConfigKey) -> String {
        return "\(serverId)_\(programKey)_\(key.rawValue)"
    }
}
