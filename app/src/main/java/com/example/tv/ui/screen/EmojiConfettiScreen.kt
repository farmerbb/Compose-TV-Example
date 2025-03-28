/* Copyright 2025 Braden Farmer
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

package com.example.tv.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tv.R
import com.example.tv.ui.component.EmojiConfetti
import com.example.tv.ui.component.Tile
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun EmojiConfettiScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
    ) {
        var showConfetti by remember { mutableStateOf(false) }
        var counter by remember { mutableIntStateOf(0) }
        var focusedColor by remember { mutableStateOf(randomColor()) }
        val focusRequester = remember { FocusRequester() }

        val buttonText = if (counter > 0) {
            stringResource(id = R.string.clicks, counter)
        } else {
            stringResource(id = R.string.click_me)
        }

        Tile(
            text = buttonText,
            focusedColor = focusedColor,
            isFocusable = true,
            isClickable = true,
            focusRequester = focusRequester,
            onClick = { counter++ },
            modifier = Modifier.offset(x = 24.dp, y = 24.dp)
        )

        if (showConfetti) {
            EmojiConfetti()
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        LaunchedEffect(counter) {
            if (counter > 0) {
                showConfetti = false
                delay(100)
                showConfetti = true

                focusedColor = randomColor()
                focusRequester.requestFocus()
            }
        }
    }
}

private fun randomColor(): Color {
    val hexColor = String.format("%06x", Random.nextInt(0x1000000)).also {
        println("New random color: #$it")
    }

    return Color(hexColor.toLong(16) or 0x00000000FF000000)
}
