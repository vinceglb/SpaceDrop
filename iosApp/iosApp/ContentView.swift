import Shared
import SwiftUI

struct ContentView: View {
    let viewModel = MainViewModel()

    @State
    var state: String = ""

    var body: some View {

        VStack {
            Image(systemName: "swift")
                    .imageScale(.large)
                    .foregroundColor(.accentColor)
            Text("SwiftUI \(state)")
            NavigationLink("salut") {
                ViewTest()
            }
        }
                .padding()
                .task {
                    for await s in viewModel.state {
                        state = s
                    }
                }
                .onAppear {
                    print("appear")
                }
                .onDisappear {
                    print("disappear")
                }
    }

}

struct ViewTest: View {
    var body: some View {
        Text("plop")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
