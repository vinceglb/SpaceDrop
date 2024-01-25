//
//  iosComposeAppApp.swift
//  iosComposeApp
//
//  Created by Vincent Guillebaud on 24/11/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseCore
import FirebaseMessaging
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        // Firebase
        FirebaseApp.configure()

        // Delegate
        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self

        // Connect to APNs
        application.registerForRemoteNotifications()

        return true
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        // Link APNs token to Firebase
        Messaging.messaging().apnsToken = deviceToken
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        // Send token to Firebase Cloud Messaging
        if let token = fcmToken {
            MessagingUtil.shared.updateFcmToken(token: token)
        }
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse
    ) async {
        let userInfo = response.notification.request.content.userInfo

        // Tel Firebase that we receive the message
        Messaging.messaging().appDidReceiveMessage(userInfo)

        // Execute event
        if let eventId = userInfo["id"] as? String {
            MessagingUtil.shared.onNotificationEventId(eventId: eventId)
        }
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification
    ) async -> UNNotificationPresentationOptions {
        let userInfo = notification.request.content.userInfo
        let eventId = userInfo["id"]
        print("WillPresent = \(String(describing: eventId))")

        // Tel Firebase that we receive the message
        Messaging.messaging().appDidReceiveMessage(userInfo)

        // Execute event
        if let eventId = userInfo["id"] as? String {
            MessagingUtil.shared.onNotificationEventId(eventId: eventId)
        }

        return [[.sound]]
    }
}

@main
struct iOSComposeApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
        MainViewControllerKt.doInitKoinIOS()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                    .onOpenURL(perform: handleDeepLink)
        }
    }

    func handleDeepLink(_ url: URL) {
        if url.scheme == "login" {
            SupabaseUtil.shared.handleDeeplinks(url: url)
        }
    }
}
