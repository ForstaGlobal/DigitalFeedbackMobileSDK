import Foundation

extension NSDictionary {
  var swiftDictionary: [String: String] {
    var swiftDictionary = [String: String]()

    for key: Any in self.allKeys {
      let stringKey = key as! String
      if let keyValue = self.value(forKey: stringKey) {
        swiftDictionary[stringKey] = "\(keyValue)"
      }
    }

    return swiftDictionary
  }
}
