import Shared
import SwiftUI

struct ContentView: View {
    let viewModel = MainViewModel()

    @State
    var state: String = ""

    var body: some View {
        NavigationView {
            VStack {
                Image(systemName: "swift")
                        .imageScale(.large)
                        .foregroundColor(.accentColor)
                Text("SwiftUI \(state)")
                NavigationLink("salut plop") {
                    ViewTest2()
                }
            }
                    .padding()
        }
                .task {
                    for await s in viewModel.state {
                        state = s
                    }
                }

    }

}

struct ViewTest2: View {
    var body: some View {
        Text("plop")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
