/* Copyright 2023 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tv.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.coroutines.launch

// Convenience wrapper to mimic LazyColumn
@Composable
fun TVLazyColumn(
    content: LazyListScope.(
        onItemGloballyPositioned: (LayoutCoordinates) -> Unit,
        scrollToItem: (Int) -> Unit,
    ) -> Unit
) = TVLazyList(isVertical = true, content)

// Convenience wrapper to mimic LazyRow
@Composable
fun TVLazyRow(
    content: LazyListScope.(
        onItemGloballyPositioned: (LayoutCoordinates) -> Unit,
        scrollToItem: (Int) -> Unit
    ) -> Unit
) = TVLazyList(isVertical = false, content)

@Composable
private fun TVLazyList(
    isVertical: Boolean,
    content: LazyListScope.(
        onItemGloballyPositioned: (LayoutCoordinates) -> Unit,
        scrollToItem: (Int) -> Unit
    ) -> Unit
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // We keep track of the length of any given item, in order to perform calculations
    // on how far to scroll.  Note that this will only work correctly if every item in
    // your LazyList has the same size.  Otherwise, you will need to maintain a map of
    // item lengths keyed off of the index.
    var itemLength by remember { mutableStateOf(0) }

    // Obtain the screen width or height based off of the current configuration
    val screenLength = with(LocalConfiguration.current) {
        val lengthInDp = if (isVertical) screenHeightDp else screenWidthDp
        lengthInDp * (densityDpi / 160f)
    }

    // This must be passed to Modifier.onGloballyPositioned() for the layout composable
    // for each TVLazyList item.  Note that the usage of Modifier.onGloballyPositioned()
    // necessitates a second composition and is generally not recommended to use unless
    // absolutely needed (such as in this case)
    val onItemGloballyPositioned = { coordinates: LayoutCoordinates ->
        with(coordinates.size) {
            itemLength = if (isVertical) height else width
        }
    }

    // This must be called whenever an item receives focus, so
    // that the item can be scrolled to the center of the screen.
    val scrollToItem = { index: Int ->
        if (index >= 0) {
            scope.launch {
                val offset = (screenLength - itemLength) / 2
                state.animateScrollToItem(index, -offset.toInt())
            }
        }
    }

    // Finally, call the underlying LazyColumn or LazyRow

    val newContent: LazyListScope.() -> Unit = {
        content(onItemGloballyPositioned, scrollToItem)
    }

    if (isVertical) {
        LazyColumn(
            state = state,
            content = newContent
        )
    } else {
        LazyRow(
            state = state,
            content = newContent
        )
    }
}
