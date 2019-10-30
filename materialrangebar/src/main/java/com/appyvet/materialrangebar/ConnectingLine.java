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

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the blue connecting line between the two thumbs.
 */
public class ConnectingLine {

    // Member Variables ////////////////////////////////////////////////////////

    private final int[] colors;
    private final float[] positions;
    private final Paint paint = new Paint();

    private final float mY;

    // Constructor /////////////////////////////////////////////////////////////

    /**
     * Constructor for connecting line
     *
     * @param y                    the y co-ordinate for the line
     * @param connectingLineWeight the weight of the line
     * @param connectingLineColors the color of the line
     */
    public ConnectingLine(float y, float connectingLineWeight,
                          ArrayList<Integer> connectingLineColors) {

        //Need two colors
        if (connectingLineColors.size() == 1) {
            connectingLineColors.add(connectingLineColors.get(0));
        }

        colors = new int[connectingLineColors.size()];
        positions = new float[connectingLineColors.size()];
        for (int index = 0; index < connectingLineColors.size(); index++) {
            colors[index] = connectingLineColors.get(index);

            positions[index] = (float) index / (connectingLineColors.size() - 1);
        }

        paint.setStrokeWidth(connectingLineWeight);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        mY = y;
    }

    private LinearGradient getLinearGradient(float startX, float endX, float height) {

        return new LinearGradient(startX, height, endX, height,
                colors,
                positions,
                Shader.TileMode.REPEAT);
    }


    /**
     * Draw the connecting line between the two thumbs in rangebar.
     *
     * @param canvas     the Canvas to draw to
     * @param leftThumb  the left thumb
     * @param rightThumb the right thumb
     */
    public void draw(Canvas canvas, PinView leftThumb, PinView rightThumb) {
        paint.setShader(getLinearGradient(0, canvas.getWidth(), mY));

        canvas.drawLine(leftThumb.getX(), mY, rightThumb.getX(), mY, paint);

    }

    /**
     * Draw the connecting line between for single slider.
     *
     * @param canvas     the Canvas to draw to
     * @param rightThumb the right thumb
     * @param leftMargin the left margin
     */
    public void draw(Canvas canvas, float leftMargin, PinView rightThumb) {
        paint.setShader(getLinearGradient(0, canvas.getWidth(), mY));

        canvas.drawLine(leftMargin, mY, rightThumb.getX(), mY, paint);
    }
}
