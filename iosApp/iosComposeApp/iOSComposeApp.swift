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
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        // Connect to APNs
        application.registerForRemoteNotifications()

        // Firebase
        FirebaseApp.configure()

        // Delegate
        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self

        return true
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        // Link APNs token to Firebase
        Messaging.messaging().apnsToken = deviceToken
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        if let token = fcmToken {
            MessagingUtil.shared.updateFcmToken(token: token)
        }
    }

    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse) async {
        let userInfo = response.notification.request.content.userInfo
        print("userNotificationCenter - Receive notification \(userInfo)")

        // Tel Firebase that we receive the message
        Messaging.messaging().appDidReceiveMessage(userInfo)
    }

    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
        print("application - Receive notification \(userInfo)")

        // Tel Firebase that we receive the message
        Messaging.messaging().appDidReceiveMessage(userInfo)

        return UIBackgroundFetchResult.newData
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
