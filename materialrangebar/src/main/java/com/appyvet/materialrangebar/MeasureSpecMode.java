/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package com.appyvet.materialrangebar;

import android.view.View;

/**
 * Helper enum class for transforming a measureSpec mode integer value into a
 * human-readable String. The human-readable String is simply the name of the
 * enum value.
 */
public enum MeasureSpecMode {

    AT_MOST(View.MeasureSpec.AT_MOST),
    EXACTLY(View.MeasureSpec.EXACTLY),
    UNSPECIFIED(View.MeasureSpec.UNSPECIFIED);

    // Member Variables ////////////////////////////////////////////////////////

    private final int mModeValue;

    // Constructor /////////////////////////////////////////////////////////////

    private MeasureSpecMode(int modeValue) {
        mModeValue = modeValue;
    }

    // Public Methods //////////////////////////////////////////////////////////

    /**
     * Gets the int value associated with this mode.
     * 
     * @return the int value associated with this mode
     */
    public int getModeValue() {
        return mModeValue;
    }

    /**
     * Gets the MeasureSpecMode value that corresponds with the given
     * measureSpec int value.
     * 
     * @param measureSpec the measure specification passed by the platform to
     *            {@link android.view.View#onMeasure(int, int)}
     * @return the MeasureSpecMode that matches with that measure spec
     */
    public static MeasureSpecMode getMode(int measureSpec) {

        final int modeValue = View.MeasureSpec.getMode(measureSpec);

        for (MeasureSpecMode mode : MeasureSpecMode.values()) {
            if (mode.getModeValue() == modeValue) {
                return mode;
            }
        }
        return null;
    }
}
