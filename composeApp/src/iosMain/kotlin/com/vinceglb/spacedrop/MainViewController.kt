import androidx.compose.ui.window.ComposeUIViewController
import com.vinceglb.spacedrop.App
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.initKoin

fun MainViewController() = ComposeUIViewController { App() }

fun initKoinIOS() = initKoin(listOf(composeModule))
