/*
 * Copyright 2014, Appyvet, Inc.
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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.View;


/**
 * Represents a thumb in the RangeBar slider. This is the handle for the slider
 * that is pressed and slid.
 */
class PinView extends View {

    // Private Constants ///////////////////////////////////////////////////////

    // The radius (in dp) of the touchable area around the thumb. We are basing
    // this value off of the recommended 48dp Rhythm. See:
    // http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm
    private static final float MINIMUM_TARGET_RADIUS_DP = 24;

    // Sets the default values for radius, normal, pressed if circle is to be
    // drawn but no value is given.
    private static final float DEFAULT_THUMB_RADIUS_DP = 14;

    // Member Variables ////////////////////////////////////////////////////////

    // Radius (in pixels) of the touch area of the thumb.
    private float mTargetRadiusPx;

    // Indicates whether this thumb is currently pressed and active.
    private boolean mIsPressed = false;

    // The y-position of the thumb in the parent view. This should not change.
    private float mY;

    // The current x-position of the thumb in the parent view.
    private float mX;

    // mPaint to draw the thumbs if attributes are selected

    private Paint mTextPaint;

    private Drawable mPin;

    private String mValue;

    // Radius of the new thumb if selected
    private int mPinRadiusPx;

    private float mPinPadding;

    private float mTextYPadding;

    private Rect mBounds = new Rect();

    private Resources mRes;

    private float mDensity;

    private Paint mCirclePaint;

    private Paint mCircleBoundaryPaint;

    private float mCircleRadiusPx;

    private float mCircleBoundaryRadiusPx;

    private IRangeBarFormatter formatter;

    private float mMinPinFont = RangeBar.DEFAULT_MIN_PIN_FONT_SP;

    private float mMaxPinFont = RangeBar.DEFAULT_MAX_PIN_FONT_SP;

    private boolean mPinsAreTemporary;

    private boolean mHasBeenPressed = false;

    private int pinColor;

    // Constructors ////////////////////////////////////////////////////////////

    public PinView(Context context) {
        super(context);
    }

    // Initialization //////////////////////////////////////////////////////////

    public void setFormatter(com.appyvet.materialrangebar.IRangeBarFormatter mFormatter) {
        this.formatter = mFormatter;
    }

    /**
     * The view is created empty with a default constructor. Use init to set all the initial
     * variables for the pin
     *
     * @param ctx                 Context
     * @param y                   The y coordinate to raw the pin (i.e. the bar location)
     * @param pinRadiusDP         the initial size of the pin
     * @param pinColor            the color of the pin
     * @param textColor           the color of the value text in the pin
     * @param circleRadius        the radius of the selector circle
     * @param circleColor         the color of the selector circle
     * @param circleBoundaryColor The color of the selector circle boundary
     * @param circleBoundarySize  The size of the selector circle boundary line
     * @param minFont             the minimum font size for the pin text
     * @param maxFont             the maximum font size for the pin text
     * @param pinsAreTemporary    whether to show the pin initially or just the circle
     */
    public void init(Context ctx, float y, float pinRadiusDP, int pinColor, int textColor,
                     float circleRadius, int circleColor, int circleBoundaryColor, float circleBoundarySize, float minFont, float maxFont, boolean pinsAreTemporary) {

        mRes = ctx.getResources();
        mPin = ContextCompat.getDrawable(ctx, R.drawable.rotate);

        mDensity = getResources().getDisplayMetrics().density;
        mMinPinFont = minFont / mDensity;
        mMaxPinFont = maxFont / mDensity;
        mPinsAreTemporary = pinsAreTemporary;

        mPinPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                mRes.getDisplayMetrics());
        mCircleRadiusPx = circleRadius;
        mTextYPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.5f,
                mRes.getDisplayMetrics());
        // If one of the attributes are set, but the others aren't, set the
        // attributes to default
        if (pinRadiusDP == -1) {
            mPinRadiusPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_THUMB_RADIUS_DP,
                    mRes.getDisplayMetrics());
        } else {
            mPinRadiusPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pinRadiusDP,
                    mRes.getDisplayMetrics());
        }
        //Set text size in px from dp
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15,
                mRes.getDisplayMetrics());

        // Creates the paint and sets the Paint values
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        // Creates the paint and sets the Paint values
        mCirclePaint = new Paint();
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setAntiAlias(true);

        if (circleBoundarySize != 0) {
            mCircleBoundaryPaint = new Paint();
            mCircleBoundaryPaint.setStyle(Paint.Style.STROKE);
            mCircleBoundaryPaint.setColor(circleBoundaryColor);
            mCircleBoundaryPaint.setStrokeWidth(circleBoundarySize);
            mCircleBoundaryPaint.setAntiAlias(true);

            mCircleBoundaryRadiusPx = mCircleRadiusPx - mCircleBoundaryPaint.getStrokeWidth() / 2;
        }

        this.pinColor = pinColor;

        // Sets the minimum touchable area, but allows it to expand based on
        // image size
        int targetRadius = (int) Math.max(MINIMUM_TARGET_RADIUS_DP, mPinRadiusPx);

        mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, targetRadius,
                mRes.getDisplayMetrics());
        mY = y;
    }

    /**
     * Set the x value of the pin
     *
     * @param x set x value of the pin
     */
    @Override
    public void setX(float x) {
        mX = x;
    }


    /**
     * Get the x value of the pin
     *
     * @return x float value of the pin
     */
    @Override
    public float getX() {
        return mX;
    }


    /**
     * Set the value of the pin
     *
     * @param x String value of the pin
     */
    public void setXValue(String x) {
        mValue = x;
    }

    /**
     * Determine if the pin is pressed
     *
     * @return true if is in pressed state
     * false otherwise
     */
    @Override
    public boolean isPressed() {
        return mIsPressed;
    }

    /**
     * Sets the state of the pin to pressed
     */
    public void press() {
        mIsPressed = true;
        mHasBeenPressed = true;
    }

    /**
     * Set size of the pin and padding for use when animating pin enlargement on press
     *
     * @param size    the size of the pin radius
     * @param padding the size of the padding
     */
    public void setSize(float size, float padding) {
        mPinPadding = (int) padding;
        mPinRadiusPx = (int) size;
        invalidate();
    }

    /**
     * Release the pin, sets pressed state to false
     */
    public void release() {
        mIsPressed = false;
    }

    /**
     * Determines if the input coordinate is close enough to this thumb to
     * consider it a press.
     *
     * @param x the x-coordinate of the user touch
     * @param y the y-coordinate of the user touch
     * @return true if the coordinates are within this thumb's target area;
     * false otherwise
     */
    public boolean isInTargetZone(float x, float y) {
        return (Math.abs(x - mX) <= mTargetRadiusPx
                && Math.abs(y - mY + mPinPadding) <= mTargetRadiusPx);
    }

    //Draw the circle regardless of pressed state. If pin size is >0 then also draw the pin and text
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mX, mY, mCircleRadiusPx, mCirclePaint);

        //Draw the circle boundary only if mCircleBoundaryPaint was initialized
        if (mCircleBoundaryPaint != null)
            canvas.drawCircle(mX, mY, mCircleBoundaryRadiusPx, mCircleBoundaryPaint);

        //Draw pin if pressed
        if (mPinRadiusPx > 0 && (mHasBeenPressed || !mPinsAreTemporary)) {
            mBounds.set((int) mX - mPinRadiusPx,
                    (int) mY - (mPinRadiusPx * 2) - (int) mPinPadding,
                    (int) mX + mPinRadiusPx, (int) mY - (int) mPinPadding);
            mPin.setBounds(mBounds);
            String text = mValue;

            if (this.formatter != null) {
                text = formatter.format(text);
            }

            calibrateTextSize(mTextPaint, text, mBounds.width());
            mTextPaint.getTextBounds(text, 0, text.length(), mBounds);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            DrawableCompat.setTint(mPin, pinColor);
            mPin.draw(canvas);
            canvas.drawText(text,
                    mX, mY - mPinRadiusPx - mPinPadding + mTextYPadding,
                    mTextPaint);
        }
        super.draw(canvas);
    }

    // Private Methods /////////////////////////////////////////////////////////////////

    //Set text size based on available pin width.
    private void calibrateTextSize(Paint paint, String text, float boxWidth) {
        paint.setTextSize(10);

        float textSize = paint.measureText(text);
        float estimatedFontSize = boxWidth * 8 / textSize / mDensity;

        if (estimatedFontSize < mMinPinFont) {
            estimatedFontSize = mMinPinFont;
        } else if (estimatedFontSize > mMaxPinFont) {
            estimatedFontSize = mMaxPinFont;
        }
        paint.setTextSize(estimatedFontSize * mDensity);
    }
}
