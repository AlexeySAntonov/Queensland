package com.alan.queensland.game.impl.ui

import androidx.compose.runtime.Composable
import com.alan.queensland.game.api.PieceType
import org.jetbrains.compose.resources.stringResource
import queensland.game_impl.generated.resources.Res
import queensland.game_impl.generated.resources.piece_type_bishop
import queensland.game_impl.generated.resources.piece_type_bishop_short
import queensland.game_impl.generated.resources.piece_type_knight
import queensland.game_impl.generated.resources.piece_type_knight_short
import queensland.game_impl.generated.resources.piece_type_rook
import queensland.game_impl.generated.resources.piece_type_rook_short

@Composable
internal fun PieceType.label(): String = when (this) {
    PieceType.KNIGHT -> stringResource(Res.string.piece_type_knight)
    PieceType.BISHOP -> stringResource(Res.string.piece_type_bishop)
    PieceType.ROOK -> stringResource(Res.string.piece_type_rook)
}

@Composable
internal fun PieceType.shortLabel(): String = when (this) {
    PieceType.KNIGHT -> stringResource(Res.string.piece_type_knight_short)
    PieceType.BISHOP -> stringResource(Res.string.piece_type_bishop_short)
    PieceType.ROOK -> stringResource(Res.string.piece_type_rook_short)
}
