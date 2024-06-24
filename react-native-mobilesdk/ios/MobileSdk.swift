import ConfirmitMobileSDK
import Foundation

@objc(MobileSdk)
class MobileSdk: NSObject {
  @objc(injectWebView)
  func injectWebView() {
    // placeholder so it is identical to Android
  }
  
  // Confirmit
  @objc(initSdk:withRejecter:)
  func initSdk(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    ConfirmitSDK.Setup().configure()
    resolve(nil)
  }
  
  @objc(enableLog:)
  func enableLog(enable: Bool) {
    ConfirmitSDK.enableLog(enable: true)
  }
  
  // Trigger
  @objc(triggerDownload:withProgramKey:withResolver:withRejecter:)
  func triggerDownload(serverId: String, programKey: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    TriggerSDK.download(serverId: serverId, programKey: programKey) { result, error in
      if result {
        resolve(result)
      } else {
        reject("trigger", "failed to download program", error)
      }
    }
  }
  
  @objc(deleteProgram:withProgramKey:withDeleteCustomData:withResolver:withRejecter:)
  func deleteProgram(serverId: String, programKey: String, deleteCustomData: Bool, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try TriggerSDK.deleteProgram(serverId: serverId, programKey: programKey, deleteCustomData: deleteCustomData)
      resolve(nil)
    } catch {
      reject("trigger", "failed to delete program \(programKey)", error)
    }
  }
  
  @objc(deleteAll:withResolver:withRejecter:)
  func deleteAll(deleteCustomData: Bool, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try TriggerSDK.deleteAll(deleteCustomData: deleteCustomData)
      resolve(nil)
    } catch {
      reject("trigger", "failed to delete all programs", error)
    }
  }
  
  @objc(setCallback:withProgramKey:)
  func setCallback(serverId: String, programKey: String) {
    TriggerSDK.setCallback(serverId: serverId, programKey: programKey, callback: MobileTriggerCallback(serverId: serverId, programKey: programKey))
  }
  
  @objc(removeCallback:withProgramKey:)
  func removeCallback(serverId: String, programKey: String) {
    TriggerSDK.removeCallback(serverId: serverId, programKey: programKey)
  }
  
  @objc(notifyEventWithData:withData:)
  func notifyEventWithData(event: String, data: NSDictionary) {
    DispatchQueue.main.async {
      TriggerSDK.notifyEvent(event: event, data: data.swiftDictionary)
    }
  }
  
  @objc(notifyEvent:)
  func notifyEvent(event: String) {
    DispatchQueue.main.async {
      TriggerSDK.notifyEvent(event: event)
    }
  }

  @objc(notifyAppForeground:)
  func notifyAppForeground(data: NSDictionary) {
    TriggerSDK.notifyAppForeground(data: data.swiftDictionary)
  }
  
  @objc(addJourney:)
  func addJourney(data: NSDictionary) {
    TriggerSDK.addJourneyLog(data: data.swiftDictionary)
  }
  
  @objc(addJourneyLogWithServer:withProgramKey:withData:)
  func addJourneyLogWithServer(serverId: String, programKey: String, data: NSDictionary) {
    TriggerSDK.addJourneyLog(serverId: serverId, programKey: programKey, data: data.swiftDictionary)
  }
  
  // Servers
  @objc(getUs:withRejecter:)
  func getUs(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    guard let server = ConfirmitServer.us else {
      reject("server", "US is not configured", nil)
      return
    }
    resolve(transformServer(server: server))
  }
  
  @objc(getUk:withRejecter:)
  func getUk(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    guard let server = ConfirmitServer.uk else {
      reject("server", "UK is not configured", nil)
      return
    }
    resolve(transformServer(server: server))
  }
  
  @objc(getAustralia:withRejecter:)
  func getAustralia(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    guard let server = ConfirmitServer.australia else {
      reject("server", "Australia is not configured", nil)
      return
    }
    resolve(transformServer(server: server))
  }
  
  @objc(getCanada:withRejecter:)
  func getCanada(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    guard let server = ConfirmitServer.canada else {
      reject("server", "Canada is not configured", nil)
      return
    }
    resolve(transformServer(server: server))
  }
  
  @objc(getGermany:withRejecter:)
  func getGermany(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    guard let server = ConfirmitServer.germany else {
      reject("server", "Germany is not configured", nil)
      return
    }
    resolve(transformServer(server: server))
  }
  
  @objc(getHxPlatform:withRejecter:)
  func getHxPlatform(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    guard let server = ConfirmitServer.hxPlatform else {
      reject("server", "HX Platform is not configured", nil)
      return
    }
    resolve(transformServer(server: server))
  }
  
  @objc(getHxAustralia:withRejecter:)
  func getHxAustralia(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    guard let server = ConfirmitServer.hxAustralia else {
      reject("server", "HX Australia is not configured", nil)
      return
    }
    resolve(transformServer(server: server))
  }
  
  @objc(configureUs:withClientSecret:withResolver:withRejecter:)
  func configureUs(clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try ConfirmitServer.configureUS(clientId: clientId, clientSecret: clientSecret)
      resolve(nil)
    } catch {
      reject("server", "Failed to configure US", error)
    }
  }
  
  @objc(configureUk:withClientSecret:withResolver:withRejecter:)
  func configureUk(clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try ConfirmitServer.configureUK(clientId: clientId, clientSecret: clientSecret)
      resolve(nil)
    } catch {
      reject("server", "Failed to configure UK", error)
    }
  }
  
  @objc(configureAustralia:withClientSecret:withResolver:withRejecter:)
  func configureAustralia(clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try ConfirmitServer.configureAustralia(clientId: clientId, clientSecret: clientSecret)
      resolve(nil)
    } catch {
      reject("server", "Failed to configure Australia", error)
    }
  }
  
  @objc(configureCanada:withClientSecret:withResolver:withRejecter:)
  func configureCanada(clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try ConfirmitServer.configureCanada(clientId: clientId, clientSecret: clientSecret)
      resolve(nil)
    } catch {
      reject("server", "Failed to configure Canada", error)
    }
  }
  
  @objc(configureGermany:withClientSecret:withResolver:withRejecter:)
  func configureGermany(clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try ConfirmitServer.configureGermany(clientId: clientId, clientSecret: clientSecret)
      resolve(nil)
    } catch {
      reject("server", "Failed to configure Germany", error)
    }
  }
  
  @objc(configureHxPlatform:withClientSecret:withResolver:withRejecter:)
  func configureHxPlatform(clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try ConfirmitServer.configureHxPlatform(clientId: clientId, clientSecret: clientSecret)
      resolve(nil)
    } catch {
      reject("server", "Failed to configure Hx Platform", error)
    }
  }
  
  @objc(configureHxAustralia:withClientSecret:withResolver:withRejecter:)
  func configureHxAustralia(clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      try ConfirmitServer.configureHxAustralia(clientId: clientId, clientSecret: clientSecret)
      resolve(nil)
    } catch {
      reject("server", "Failed to configure Hx Australia", error)
    }
  }
  
  @objc(configureServer:withHost:withClientId:withClientSecret:withResolver:withRejecter:)
  func configureServer(name: String, host: String, clientId: String, clientSecret: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      let server = try ConfirmitServer.configure(name: name, host: host, clientId: clientId, clientSecret: clientSecret)
      resolve(transformServer(server: server))
    } catch {
      reject("server", "failed to configure srvers", error)
    }
  }
  
  @objc(getServer:withResolver:withRejecter:)
  func getServer(serverId: String, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      if let server = try ConfirmitServer.getServer(serverId: serverId) {
        resolve(transformServer(server: server))
      }
    } catch {
      reject("server", "failed to get servers", error)
    }
  }
  
  @objc(getServers:withRejecter:)
  func getServers(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    do {
      let servers = try ConfirmitServer.getServers()
      var result: [[String: String]] = []
      for server in servers {
        result.append(transformServer(server: server))
      }
      resolve(result)
    } catch {
      reject("server", "failed to get servers", error)
    }
  }
  
  private func transformServer(server: Server) -> [String: String] {
    return [
      "host": server.host,
      "name": server.name,
      "serverId": server.serverId
    ]
  }
}
