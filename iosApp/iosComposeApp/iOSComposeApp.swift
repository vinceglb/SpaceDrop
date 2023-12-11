//
//  iosComposeAppApp.swift
//  iosComposeApp
//
//  Created by Vincent Guillebaud on 24/11/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseCore
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
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
