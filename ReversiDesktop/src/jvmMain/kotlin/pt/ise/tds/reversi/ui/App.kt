package pt.ise.tds.reversi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import pt.isel.tds.reversi.model.BoardStorage
import pt.isel.tds.reversi.ui.BoardView
import pt.isel.tds.reversi.ui.ReversiMenu

@Composable
fun FrameWindowScope.ReversiApp(onExit: () -> Unit, storage: BoardStorage) {
    val scope = rememberCoroutineScope()
    val viewModel = remember { ReversiViewModel(scope, storage) } //The ViewModel
    ReversiMenu(viewModel, onExit)              // The App menu.
    ReversiDialog(viewModel)        // The Dialogs.
    Column {
        BoardView(
            viewModel.game?.board,
            viewModel.targetsOn,
            viewModel::targets,
            viewModel::toFlip,
            onClick = viewModel::play
        )
        StatusBar(viewModel.status, viewModel.game?.board)
    }
}