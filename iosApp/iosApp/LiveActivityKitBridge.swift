import Foundation
import KMPLiveActivities
import Shared

@available(iOS 16.2, *)
final class LiveActivityKitBridge: LiveActivityBridge {

    init() {
        let controller = KMPLiveActivityController.shared
        controller.onStartResult = { activityId, success, errorKind in
            LiveActivityManager.shared.notifyStartResult(
                activityId: activityId,
                success: success,
                errorKind: errorKind
            )
        }
        controller.onPushToken = { activityId, token in
            LiveActivityManager.shared.notifyPushToken(activityId: activityId, token: token)
        }
        controller.onStatusChanged = { activityId, status in
            LiveActivityManager.shared.notifyStatusChanged(activityId: activityId, status: status)
        }
    }

    func areActivitiesEnabled() -> Bool {
        KMPLiveActivityController.shared.areActivitiesEnabled
    }

    func start(
        activityId: String,
        attributesTypeName: String,
        attributesJson: String,
        contentStateJson: String,
        staleAfterSeconds: Double,
        requestPushToken: Bool
    ) {
        KMPLiveActivityController.shared.start(
            activityId: activityId,
            attributesTypeName: attributesTypeName,
            attributesJson: attributesJson,
            contentStateJson: contentStateJson,
            staleAfterSeconds: staleAfterSeconds,
            requestPushToken: requestPushToken
        )
    }

    func update(activityId: String, contentStateJson: String, staleAfterSeconds: Double) {
        KMPLiveActivityController.shared.update(
            activityId: activityId,
            contentStateJson: contentStateJson,
            staleAfterSeconds: staleAfterSeconds
        )
    }

    func end(activityId: String, finalContentStateJson: String?, dismissalSeconds: Double) {
        KMPLiveActivityController.shared.end(
            activityId: activityId,
            finalContentStateJson: finalContentStateJson,
            dismissalSeconds: dismissalSeconds
        )
    }
}
