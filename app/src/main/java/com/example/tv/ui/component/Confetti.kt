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

package com.example.tv.ui.component

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@Composable
fun EmojiConfetti() {
    val shapes by remember {
        mutableStateOf(
            emojiList.map { emoji ->
                Shape.DrawableShape(
                    drawable = TextDrawable(
                        text = emoji,
                        color = Color.BLACK,
                        size = 100f,
                    ),
                    tint = false,
                    applyAlpha = false,
                )
            }
        )
    }

    KonfettiView(
        modifier = Modifier.fillMaxSize(),
        parties = listOf(
            Party(
                emitter = Emitter(duration = 1, TimeUnit.SECONDS).perSecond(30),
                shapes = shapes,
                size = listOf(Size(sizeInDp = 100, mass = 9f)),
                rotation = Rotation(enabled = false)
            ),
        ),
    )
}

private val emojiList = listOf(
    "\uD83D\uDE00", "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04",
    "\uD83D\uDE05", "\uD83D\uDE06", "\uD83D\uDE09", "\uD83D\uDE0A", "\uD83D\uDE0B",
    "\uD83D\uDE0E", "\uD83D\uDE0D", "\uD83D\uDE18", "\uD83D\uDE17", "\uD83D\uDE19",
    "\uD83D\uDE1A", "\uD83D\uDE1C", "\uD83D\uDE1D", "\uD83D\uDE1B", "\uD83D\uDE33",
    "\uD83D\uDE07", "\uD83D\uDE0F", "\uD83D\uDE11", "\uD83D\uDE12", "\uD83D\uDE13",
    "\uD83D\uDE14", "\uD83D\uDE15", "\uD83D\uDE16", "\uD83D\uDE1E", "\uD83D\uDE1F",
    "\uD83D\uDE24", "\uD83D\uDE22", "\uD83D\uDE2D", "\uD83D\uDE2A", "\uD83D\uDE25",
    "\uD83D\uDE2B", "\uD83D\uDE29", "\uD83D\uDE2C", "\uD83D\uDE2F", "\uD83D\uDE32",
    "\uD83D\uDE30", "\uD83D\uDE31", "\uD83D\uDE28", "\uD83D\uDE23", "\uD83D\uDE20",
    "\uD83D\uDE21", "\uD83D\uDE37", "\uD83D\uDE34", "\uD83D\uDE35", "\uD83D\uDE36",
    "\uD83D\uDE44", "\uD83D\uDE42", "\uD83D\uDE43", "\uD83D\uDE40", "\uD83D\uDE41",
    "\uD83D\uDE45", "\uD83D\uDE46", "\uD83D\uDE47", "\uD83D\uDE4B", "\uD83D\uDE4C",
    "\uD83D\uDE4D", "\uD83D\uDE4E", "\uD83D\uDE4F", "\uD83E\uDD10", "\uD83E\uDD13",
    "\uD83E\uDD14", "\uD83E\uDD28", "\uD83E\uDD29", "\uD83E\uDD2A", "\uD83E\uDD2B",
    "\uD83E\uDD2D", "\uD83E\uDD23", "\uD83E\uDD17", "\uD83E\uDD1C", "\uD83E\uDD1F",
)
