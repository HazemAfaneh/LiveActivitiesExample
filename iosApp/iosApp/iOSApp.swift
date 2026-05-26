import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        if #available(iOS 16.2, *) {
            LiveActivityManager.shared.register(bridge: LiveActivityKitBridge())
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
