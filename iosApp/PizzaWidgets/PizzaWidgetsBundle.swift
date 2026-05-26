import SwiftUI
import WidgetKit

@main
struct PizzaWidgetsBundle: WidgetBundle {
    var body: some Widget {
        if #available(iOS 16.2, *) {
            PizzaLiveActivityWidget()
        }
    }
}
