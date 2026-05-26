import ActivityKit
import KMPLiveActivities
import SwiftUI
import WidgetKit

// MARK: - DTOs that mirror the Kotlin Delivery model

struct DeliveryStateDTO: Codable {
    let status: String
    let etaMinutes: Int32
    let driverName: String?
}

struct DeliveryAttributesDTO: Codable {
    let orderId: String
    let restaurantName: String
    let itemHeadline: String
}

extension KMPLiveActivityAttributes.ContentState {
    var delivery: DeliveryStateDTO {
        (try? JSONDecoder().decode(DeliveryStateDTO.self, from: Data(payload.utf8)))
            ?? DeliveryStateDTO(status: "preparing", etaMinutes: 0, driverName: nil)
    }
}

extension KMPLiveActivityAttributes {
    var delivery: DeliveryAttributesDTO {
        (try? JSONDecoder().decode(DeliveryAttributesDTO.self, from: Data(payload.utf8)))
            ?? DeliveryAttributesDTO(
                orderId: "—",
                restaurantName: "Pizza Hut",
                itemHeadline: "1× Large Pepperoni"
            )
    }
}

// MARK: - Tokens that mirror PizzaTheme on the Kotlin side

private enum PizzaColors {
    static let accent       = Color(red: 0xE0 / 255, green: 0x3A / 255, blue: 0x2F / 255)
    static let pizzaYellow  = Color(red: 0xF4 / 255, green: 0xC1 / 255, blue: 0x52 / 255)
    static let surface      = Color(red: 0x14 / 255, green: 0x14 / 255, blue: 0x16 / 255)
    static let inkOnDark    = Color.white
    static let subOnDark    = Color.white.opacity(0.72)
    static let metaOnDark   = Color.white.opacity(0.55)
    static let trackOnDark  = Color.white.opacity(0.12)
}

private func statusLabel(_ s: DeliveryStateDTO) -> String {
    switch s.status {
    case "preparing":  return "Preparing"
    case "on_the_way":
        let driver = s.driverName.map { " · \($0)" } ?? ""
        return "On the way\(driver)"
    case "arriving":   return "Arriving"
    case "delivered":  return "Delivered"
    default:           return s.status
    }
}

private func progressFraction(_ s: DeliveryStateDTO) -> Double {
    switch s.status {
    case "preparing":  return 0.25
    case "on_the_way": return 0.60
    case "arriving":   return 0.85
    case "delivered":  return 1.00
    default:           return 0.00
    }
}

// MARK: - Widget

struct PizzaLiveActivityWidget: Widget {
    var body: some WidgetConfiguration {
        KMPLiveActivityWidget.configuration { context in
            LockScreenView(
                state: context.state.delivery,
                attributes: context.attributes.delivery
            )
            .padding(.horizontal, 16)
            .padding(.vertical, 14)
            .activityBackgroundTint(PizzaColors.surface.opacity(0.92))
            .activitySystemActionForegroundColor(.white)
        } dynamicIsland: { context in
            let s = context.state.delivery
            let a = context.attributes.delivery
            return DynamicIsland {
                DynamicIslandExpandedRegion(.leading) {
                    HStack(spacing: 8) {
                        PizzaSquareIcon(size: 28)
                        VStack(alignment: .leading, spacing: 1) {
                            Text(a.restaurantName)
                                .font(.caption).bold()
                                .foregroundStyle(.white)
                            Text(statusLabel(s))
                                .font(.caption2)
                                .foregroundStyle(PizzaColors.subOnDark)
                        }
                    }
                }
                DynamicIslandExpandedRegion(.trailing) {
                    EtaCluster(eta: s.etaMinutes, alignment: .trailing)
                }
                DynamicIslandExpandedRegion(.bottom) {
                    VStack(spacing: 10) {
                        GradientProgressBar(progress: progressFraction(s))
                        HStack {
                            LockMeta(label: "ORDER", value: a.orderId, alignment: .leading)
                            Spacer()
                            LockMeta(label: "ITEM", value: a.itemHeadline, alignment: .center)
                            Spacer()
                            LockMeta(
                                label: "STATUS",
                                value: "\(Int(progressFraction(s) * 100))%",
                                alignment: .trailing
                            )
                        }
                    }
                }
            } compactLeading: {
                PizzaSquareIcon(size: 18)
            } compactTrailing: {
                Text(s.etaMinutes > 0 ? "\(s.etaMinutes)m" : "✓")
                    .font(.caption2).bold()
                    .foregroundStyle(PizzaColors.accent)
            } minimal: {
                Text(s.etaMinutes > 0 ? "\(s.etaMinutes)" : "✓")
                    .font(.caption2).bold()
                    .foregroundStyle(PizzaColors.accent)
            }
        }
    }
}

// MARK: - Lock screen view (mirrors LockScreenPreview composable)

private struct LockScreenView: View {
    let state: DeliveryStateDTO
    let attributes: DeliveryAttributesDTO

    var body: some View {
        VStack(spacing: 12) {
            HStack(spacing: 12) {
                PizzaSquareIcon(size: 36)

                VStack(alignment: .leading, spacing: 1) {
                    Text(attributes.restaurantName)
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundStyle(PizzaColors.inkOnDark)
                    Text(statusLabel(state))
                        .font(.system(size: 11))
                        .foregroundStyle(PizzaColors.subOnDark)
                }

                Spacer(minLength: 8)

                EtaCluster(eta: state.etaMinutes, alignment: .trailing)
            }

            GradientProgressBar(progress: progressFraction(state))

            HStack {
                LockMeta(label: "ORDER", value: attributes.orderId, alignment: .leading)
                Spacer()
                LockMeta(label: "ITEM", value: attributes.itemHeadline, alignment: .center)
                Spacer()
                LockMeta(
                    label: "STATUS",
                    value: "\(Int(progressFraction(state) * 100))%",
                    alignment: .trailing
                )
            }
        }
    }
}

// MARK: - Reusable pieces

private struct PizzaSquareIcon: View {
    let size: CGFloat
    var body: some View {
        RoundedRectangle(cornerRadius: 8, style: .continuous)
            .fill(PizzaColors.accent)
            .frame(width: size, height: size)
            .overlay {
                Text("🍕")
                    .font(.system(size: size * 0.55))
            }
    }
}

private struct EtaCluster: View {
    let eta: Int32
    let alignment: HorizontalAlignment

    var body: some View {
        HStack(alignment: .lastTextBaseline, spacing: 3) {
            Text(eta > 0 ? "\(eta)" : "—")
                .font(.system(size: 24, weight: .regular, design: .serif))
                .foregroundStyle(PizzaColors.inkOnDark)
            if eta > 0 {
                Text("min")
                    .font(.system(size: 11))
                    .foregroundStyle(PizzaColors.subOnDark)
            }
        }
    }
}

private struct GradientProgressBar: View {
    let progress: Double
    var body: some View {
        GeometryReader { geo in
            ZStack(alignment: .leading) {
                Capsule()
                    .fill(PizzaColors.trackOnDark)
                Capsule()
                    .fill(
                        LinearGradient(
                            colors: [PizzaColors.accent, PizzaColors.pizzaYellow],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
                    .frame(width: max(0, geo.size.width * progress))
                    .animation(.easeInOut(duration: 0.4), value: progress)
            }
        }
        .frame(height: 4)
    }
}

private struct LockMeta: View {
    let label: String
    let value: String
    let alignment: HorizontalAlignment

    var body: some View {
        VStack(alignment: alignment, spacing: 1) {
            Text(label)
                .font(.system(size: 9, weight: .semibold))
                .kerning(0.8)
                .foregroundStyle(PizzaColors.metaOnDark)
            Text(value)
                .font(.system(size: 11.5, weight: .medium))
                .foregroundStyle(PizzaColors.inkOnDark.opacity(0.92))
                .lineLimit(1)
                .truncationMode(.tail)
        }
    }
}
