import ActivityKit
import KMPLiveActivities
import SwiftUI
import WidgetKit

struct DeliveryStateDTO: Codable {
    let status: String
    let etaMinutes: Int32
    let driverName: String?
}

extension KMPLiveActivityAttributes.ContentState {
    var delivery: DeliveryStateDTO {
        (try? JSONDecoder().decode(DeliveryStateDTO.self, from: Data(payload.utf8)))
            ?? DeliveryStateDTO(status: "preparing", etaMinutes: 0, driverName: nil)
    }
}

private func headline(_ s: DeliveryStateDTO) -> String {
    switch s.status {
    case "preparing": return "Preparing your order"
    case "on_the_way":
        let driver = s.driverName.map { " · \($0)" } ?? ""
        return "On the way · ETA \(s.etaMinutes) min\(driver)"
    case "arriving": return "Arriving · ETA \(s.etaMinutes) min"
    case "delivered": return "Delivered — enjoy!"
    default: return s.status
    }
}

@available(iOS 16.2, *)
struct PizzaLiveActivityWidget: Widget {
    var body: some WidgetConfiguration {
        KMPLiveActivityWidget.configuration { context in
            LockScreenView(state: context.state.delivery)
                .padding()
                .activityBackgroundTint(.black.opacity(0.8))
                .activitySystemActionForegroundColor(.white)
        } dynamicIsland: { context in
            let s = context.state.delivery
            return DynamicIsland {
                DynamicIslandExpandedRegion(.leading) {
                    Label("Demo Pizzeria", systemImage: "fork.knife")
                        .font(.caption).foregroundStyle(.white)
                }
                DynamicIslandExpandedRegion(.trailing) {
                    Text("\(s.etaMinutes) min").font(.caption).foregroundStyle(.white)
                }
                DynamicIslandExpandedRegion(.bottom) {
                    VStack(alignment: .leading) {
                        Text(headline(s)).font(.subheadline).foregroundStyle(.white)
                        if let driver = s.driverName {
                            Text("Driver: \(driver)").font(.caption2).foregroundStyle(.white.opacity(0.7))
                        }
                    }
                }
            } compactLeading: {
                Image(systemName: "fork.knife.circle.fill").foregroundStyle(.orange)
            } compactTrailing: {
                Text("\(s.etaMinutes)m").font(.caption2).bold()
            } minimal: {
                Text("\(s.etaMinutes)m").font(.caption2).bold()
            }
        }
    }
}

@available(iOS 16.2, *)
private struct LockScreenView: View {
    let state: DeliveryStateDTO

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: "fork.knife.circle.fill")
                .font(.largeTitle)
                .foregroundStyle(.orange)
            VStack(alignment: .leading, spacing: 2) {
                Text("Demo Pizzeria").font(.headline).foregroundStyle(.white)
                Text(headline(state))
                    .font(.subheadline)
                    .foregroundStyle(.white.opacity(0.85))
            }
            Spacer()
            VStack(alignment: .trailing) {
                Text("\(state.etaMinutes)").font(.title).bold().foregroundStyle(.white)
                Text("min").font(.caption2).foregroundStyle(.white.opacity(0.7))
            }
        }
    }
}
