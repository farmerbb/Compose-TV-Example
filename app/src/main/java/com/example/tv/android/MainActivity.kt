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

package com.example.tv.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.example.tv.ui.ComposeTVApp
import com.example.tv.util.EventThrottler
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    private val horizontalThrottler = EventThrottler(75.milliseconds)
    private val verticalThrottler = EventThrottler(200.milliseconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ComposeTVApp()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Listen for D-Pad keypresses, and check to see if they need to be throttled.
        // This helps reign in Compose's default scrolling behavior which is too fast
        // for most devices to keep up with.

        // We only care about throttling ACTION_DOWN events
        if (event.action != KeyEvent.ACTION_DOWN) return super.dispatchKeyEvent(event)

        val isThrottled = when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                horizontalThrottler.throttleEvent()
            }

            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                verticalThrottler.throttleEvent()
            }

            else -> false
        }

        return isThrottled || super.dispatchKeyEvent(event)
    }
}
