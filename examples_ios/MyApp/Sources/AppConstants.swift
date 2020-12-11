import Foundation
import UIKit

enum AppError: Error {
  case general(String)
}

enum ScreenType: String {
  case phone
  case tablet
}
