@file:Suppress("NOTHING_TO_INLINE")

package com.example.myapplication.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object Dimens {
    /**
     * Apply for compact layout, margins are 16dp from the left and right edge of the window.
     * [Material 3 specs](https://m3.material.io/foundations/layout/applying-layout/compact#e32a1311-f9ca-4425-9249-081e180fafba)
     */
    val HorizontalSpacing = 16.dp

    /**
     * Minimum spacing between UI elements.
     */
    val ItemSpacing = 8.dp

    /**
     * Maximum spacing between UI elements.
     */
    val WideItemSpacing = 22.dp

    /**
     * Padding from the card's border to it's content.
     * [Material 3 specs](https://m3.material.io/components/cards/specs#9abbced9-d5d3-4893-9a67-031825205f06)
     */
    val CardContentPadding = 16.dp
}

inline fun Modifier.horizontalSpacing() =
    this.padding(horizontal = Dimens.HorizontalSpacing)

inline fun Modifier.itemSpacing() =
    this.padding(Dimens.ItemSpacing)

inline fun Modifier.itemTopSpacing() =
    this.padding(top = Dimens.ItemSpacing)

inline fun Modifier.wideItemTopSpacing() =
    this.padding(top = Dimens.WideItemSpacing)

inline fun Modifier.itemStartSpacing() =
    this.padding(start = Dimens.ItemSpacing)