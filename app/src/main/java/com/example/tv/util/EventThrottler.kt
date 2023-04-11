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

package com.example.tv.util

import kotlin.time.Duration
import kotlinx.datetime.Clock

class EventThrottler(
    // Keypresses will only fire once every duration
    private val duration: Duration
) {
    // Timestamp for the last time a keypress event was successfully fired
    private var lastEventTime = Clock.System.now()

    fun throttleEvent(): Boolean {
        if (duration.inWholeMilliseconds > 0) {
            val currentTime = Clock.System.now()

            // If the current time is within the duration of the last successful firing
            // of a key event, then just consume the keypress and do nothing.
            if (currentTime < lastEventTime + duration) {
                return true
            }

            // Current time is after the duration window, so set the last event time
            // and do not consume the keypress ourselves.
            lastEventTime = currentTime
        }

        return false
    }
}
