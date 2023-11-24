//
//  iosComposeAppApp.swift
//  iosComposeApp
//
//  Created by Vincent Guillebaud on 24/11/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

@main
struct iOSComposeApp: App {
    init() {
        MainViewControllerKt.doInitKoinIOS()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
