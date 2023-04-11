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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tv.R

@Composable
fun EpisodeItem(
    index: Int,
    isFocusable: Boolean,
    focusRequester: FocusRequester?,
    onItemGloballyPositioned: (LayoutCoordinates) -> Unit,
    onFocused: () -> Unit
) {
    // We declare an interaction source so that we can receive focus events
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Call the onFocused() function when this item receives focus
    LaunchedEffect(isFocused) {
        if (isFocused) onFocused()
    }

    // Transition animations for when an item is focused
    val transition = updateTransition(isFocused, "EpisodeItem transition")

    @Composable
    fun animate(targetValue: @Composable (state: Boolean) -> Float) =
        transition.animateFloat(
            label = "EpisodeItem animateFloat",
            transitionSpec = { tween(durationMillis = 200, easing = FastOutSlowInEasing) },
            targetValueByState = targetValue
        )

    // Base width and height for a focused item
    val baseWidth = 120f
    val baseHeight = 72f

    // Base padding value used in the calculations below.
    val basePadding = 6f

    // These values are manipulated based on whether or not the item is currently focused.
    val elevation by animate { if (it) 12f else 4f }
    val rgbValue by animate { if (it) Color.LightGray.red else Color.Gray.red }
    val externalPadding by animate { if (it) 0f else basePadding }
    val internalPadding by animate { if (it) basePadding else 0f }

    // Declare a parent box with the maximum possible width + height of an item.
    // We also set onGloballyPositioned here which is passed down from the parent TVLazyList.
    Box(
        modifier = Modifier
            .width((baseWidth + basePadding).dp)
            .height((baseHeight + basePadding).dp)
            .onGloballyPositioned(onItemGloballyPositioned)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),

            // Reconstruct the background color based on the animated value above
            color = Color(red = rgbValue, green = rgbValue, blue = rgbValue),

            elevation = elevation.dp,
            modifier = Modifier
                // "External" padding is applied when an item is not focused
                .padding(externalPadding.dp)
                .align(Alignment.Center)

                // If a focus requester has been passed through (for the default selected item),
                // set it here.
                .thenIf(focusRequester != null) {
                    Modifier.focusRequester(focusRequester!!)
                }

                // Declare this composable as focusable and set the interaction source
                .thenIf(isFocusable) {
                    Modifier.focusable(interactionSource = interactionSource)
                }
        ) {
            Box(
                modifier = Modifier
                    // "Internal" padding is applied when an item has focus
                    .width((baseWidth + internalPadding).dp)
                    .height((baseHeight + internalPadding).dp)
            ) {
                Text(
                    color = Color.Black,
                    style = MaterialTheme.typography.body2,
                    text = stringResource(id = R.string.episode, index + 1),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

// Convenience function for applying modifiers conditionally
private fun Modifier.thenIf(predicate: Boolean, modifier: () -> Modifier) = then(
    if (predicate) modifier() else Modifier
)
