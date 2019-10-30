/*******************************************************************************
 * Copyright 2013 Gabriele Mariotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.appyvet.rangebarsample.colorpicker;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

import com.appyvet.rangebarsample.R;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class Utils {


    /**
     * Utility class for colors
     *
     * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
     */
    public static class ColorUtils {

        /**
         * Create an array of int with colors
         *
         * @param context
         * @return
         */
        public static int[] colorChoice(Context context) {

            int[] mColorChoices = null;
            String[] color_array = context.getResources().getStringArray(R.array.default_color_choice_values);

            if (color_array != null && color_array.length > 0) {
                mColorChoices = new int[color_array.length];
                for (int i = 0; i < color_array.length; i++) {
                    mColorChoices[i] = Color.parseColor(color_array[i]);
                }
            }
            return mColorChoices;
        }

        /**
         * Parse whiteColor
         *
         * @return
         */
        public static int parseWhiteColor() {
            return Color.parseColor("#FFFFFF");
        }

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
