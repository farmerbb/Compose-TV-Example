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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tv.R

@Composable
fun ComposeTVApp() {

    // Maintain a map of the last focused row, as well as the last focused item in each row.
    // This allows us to reign in a bit of control over Compose's focus system when navigating
    // between rows.
    var lastFocusedRow by remember { mutableStateOf(0) }
    val lastFocusedItems = remember { mutableStateMapOf<Int, Int>() }

    // You should always choose one item on each screen to be focused by default.
    // Here, we select the first item in the first row, to keep things simple.
    val firstItemFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        firstItemFocus.requestFocus()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
    ) {
        // We use a wrapper around LazyList to keep the focused item centered on screen
        TVLazyColumn { onRowGloballyPositioned, scrollToRow ->
            items(10) { rowIndex ->
                Column(
                    // onGloballyPositioned should always be set on the first layout composable
                    // in a TVLazyColumn or TVLazyRow item
                    modifier = Modifier.onGloballyPositioned(onRowGloballyPositioned)
                ) {
                    // "Season" header
                    Text(
                        color = Color.White,
                        style = MaterialTheme.typography.h6,
                        text = stringResource(id = R.string.season, rowIndex + 1),
                        modifier = Modifier
                            // Note that this header is the same height as the spacer below
                            // to ensure that the row is perfectly centered on screen
                            .height(32.dp)
                            .padding(horizontal = 12.dp)
                    )

                    TVLazyRow { onItemGloballyPositioned, scrollToItem ->
                        items(20) { itemIndex ->
                            EpisodeItem(
                                index = itemIndex,

                                // Allow all items in the currently focused row to be focusable,
                                // otherwise, only allow the last focused item to be focusable.
                                isFocusable = lastFocusedRow == rowIndex || (lastFocusedItems[rowIndex] ?: 0) == itemIndex,

                                // If this is the default item, set the focus requester
                                focusRequester = if (rowIndex == 0 && itemIndex == 0) firstItemFocus else null,

                                onItemGloballyPositioned = onItemGloballyPositioned,
                                onFocused = {
                                    // Record the last focused row + last focused item
                                    lastFocusedRow = rowIndex
                                    lastFocusedItems[rowIndex] = itemIndex

                                    // Tell the parent TVLazyLists to scroll
                                    // whenever a new item receives focus
                                    scrollToRow(rowIndex)
                                    scrollToItem(itemIndex)
                                }
                            )
                        }
                    }

                    // Spacers should be used instead of padding on the layout composable
                    // with the onGloballyPositioned modifier
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
