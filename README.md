# Pizza Delivery Tracker

A KMP + Compose Multiplatform sample (Android + iOS) for the
[`io.github.hazemafaneh.liveactivities:live-activities`](https://github.com/hazemafaneh/kmp-live-activities/)
library — a single API for iOS Live Activities (Lock Screen + Dynamic Island) and Android 16+
Live Updates (`Notification.ProgressStyle`).

## What it does

Tap **Start Order** to launch a Live Activity for order `PIZZA-12345` from `Demo Pizzeria`. The four
status buttons advance the delivery through **Preparing → On the way (ETA 12, Ahmad) →
Arriving (ETA 3) → Delivered**, each calling `LiveActivityManager.update`. **End** dismisses it
with `DismissalPolicy.Immediate`. The APNs push token (requested via `PushType.Token`) is logged
to logcat / the Xcode console as it arrives.

## Project layout

| Path | Contents |
| --- | --- |
| `shared/src/commonMain/kotlin/.../Delivery.kt` | `DeliveryAttributes`, `DeliveryState`, helpers |
| `shared/src/commonMain/kotlin/.../App.kt`      | Compose UI used by both platforms |
| `androidApp/src/main/kotlin/.../PizzaApp.kt`   | `Application` — calls `init()` + `registerRenderer()` |
| `androidApp/src/main/kotlin/.../MainActivity.kt` | Hosts `App()`, requests `POST_NOTIFICATIONS` on Tiramisu+ |
| `androidApp/src/main/res/drawable/ic_live_activity.xml` | Status-bar icon (default `androidSmallIconResName`) |
| `iosApp/iosApp/iOSApp.swift`                   | Registers `LiveActivityKitBridge` at launch |
| `iosApp/iosApp/LiveActivityKitBridge.swift`    | Conforms the Swift `KMPLiveActivityController` to the Kotlin `LiveActivityBridge` |
| `iosApp/PizzaWidgets/`                         | Widget Extension sources — Lock Screen + Dynamic Island |

## Prerequisites

The library is consumed from **Maven Local**. Republish from the library repo if needed:

```bash
cd <kmp-liveactivities>
./gradlew :library:publishToMavenLocal
```

The companion Swift package lives at `<kmp-liveactivities>/ios-swift-package/`
(SwiftPM product: `KMPLiveActivities`).

## Running on Android

```bash
./gradlew :androidApp:assembleDebug      # or run from Android Studio
./gradlew :androidApp:installDebug
```

You'll see an ongoing notification with a progress bar, title `Demo Pizzeria · order PIZZA-12345`,
and body text reflecting the current state. On Android 16+ this uses
`Notification.ProgressStyle`; earlier versions fall back to a standard ongoing notification.

> On Android 13+ the app requests `POST_NOTIFICATIONS` at launch. The library merges
> `POST_NOTIFICATIONS` and `FOREGROUND_SERVICE` into the final manifest automatically.

## Running on iOS — manual Xcode steps

The Kotlin shared framework + Swift sources are in place, but a few things must be wired in
Xcode by hand (these can't be safely scripted into `project.pbxproj`):

### 1. Add the local Swift package

Open `iosApp/iosApp.xcodeproj` in Xcode, then:

`File → Add Package Dependencies… → Add Local…` → select
`<kmp-liveactivities>/ios-swift-package` → add the `KMPLiveActivities` product to the
**iosApp** target.

### 2. Create the Widget Extension target

`File → New → Target… → Widget Extension`.

- Product Name: **PizzaWidgets**
- Bundle ID: `com.hazemafaneh.liveactivitiesexample.LiveActivitiesExample.PizzaWidgets`
- Uncheck *Include Live Activity* and *Include Configuration App Intent* (we provide our own
  bundle file).
- Embed in the **iosApp** target when prompted.

When the target is created, Xcode generates `PizzaWidgets/PizzaWidgets.swift` etc. — **delete
those generated files** and then **add the existing `iosApp/PizzaWidgets/` directory** to the
new target (`File → Add Files to "iosApp"… → check "PizzaWidgets" target only`).

### 3. Link `KMPLiveActivities` to the Widget Extension

Select the **PizzaWidgets** target → `General` → `Frameworks and Libraries` → `+` →
`KMPLiveActivities`.

### 4. Run the iOS app

Pick the `iosApp` scheme and a physical device or iOS 16.2+ simulator. Tap **Start Order**;
the Live Activity appears on the Lock Screen and Dynamic Island. Updates change the ETA and
driver name; **End** dismisses it.

The push token prints to the Xcode console:

```
Live Activity push token: <hex>
```

## Definition of done

- [x] `./gradlew :androidApp:assembleDebug` succeeds
- [x] `./gradlew :shared:compileKotlinIosSimulatorArm64` succeeds
- [x] Android: tapping Start shows an ongoing notification; the four buttons advance the
      status/ETA/progress; End removes it
- [ ] iOS: after the Xcode steps above, tapping Start shows a Live Activity on the Lock Screen
      and Dynamic Island; updates change ETA/driver; End dismisses it
- [x] The APNs push token is printed to logcat / Xcode console after Start
- [x] Library resolves from `mavenLocal()` on a fresh clone
