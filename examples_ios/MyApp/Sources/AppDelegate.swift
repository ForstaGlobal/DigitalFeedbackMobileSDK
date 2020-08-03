import MobileSurveySdk
import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    private var feedbackManager: TriggerManager = TriggerManager()
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        window?.backgroundColor = AppColor.background

        ConfirmitSDK.enableLog(enable: true)
        ConfirmitSDK.Setup().rootPath(path: nil).configure()
        SurveySDK.setUniqueIdProvider(provider: AppUniqueDeviceIdProvider())

        // TODO: Please enter your Client ID and Client Secret keys
        // Confirmit has a number of Horizons Servers. Please check your server, and initialize configuration
        // Example: UK Server
        try! ConfirmitServer.configureUK(clientId: "<Client ID>", clientSecret: "<Client Secret>")

        feedbackManager.setAllDelegate()
        return true
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        TriggerSDK.notifyAppForeground(data: [:])
    }
}

class AppUniqueDeviceIdProvider: UniqueIdProvider {
    func getUniqueId() -> String {
        return AppConfigs.uniqueId
    }
}
