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
/*
 * Copyright 2015, Appyvet, Inc.
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

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The MaterialRangeBar is a single or double-sided version of a {@link android.widget.SeekBar}
 * with discrete values. Whereas the thumb for the SeekBar can be dragged to any
 * position in the bar, the RangeBar only allows its thumbs to be dragged to
 * discrete positions (denoted by tick marks) in the bar. When released, a
 * RangeBar thumb will snap to the nearest tick mark.
 * This version is forked from edmodo range bar
 * https://github.com/edmodo/range-bar.git
 * Clients of the RangeBar can attach a
 * {@link OnRangeBarChangeListener} to be notified when the pins
 * have
 * been moved.
 */
public class RangeBar extends View {

    // Member Variables ////////////////////////////////////////////////////////

    private static final String TAG = "RangeBar";

    // Default values for variables
    private static final float DEFAULT_TICK_START = 0;

    private static final float DEFAULT_TICK_END = 5;

    private static final float DEFAULT_TICK_INTERVAL = 1;

    private static final float DEFAULT_TICK_HEIGHT_DP = 1;

    private static final float DEFAULT_PIN_PADDING_DP = 16;

    public static final float DEFAULT_MIN_PIN_FONT_SP = 8;

    public static final float DEFAULT_MAX_PIN_FONT_SP = 24;

    private static final float DEFAULT_BAR_WEIGHT_DP = 2;

    private static final float DEFAULT_CIRCLE_BOUNDARY_SIZE_DP = 0;

    private static final int DEFAULT_BAR_COLOR = Color.LTGRAY;

    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private static final int DEFAULT_TICK_COLOR = Color.BLACK;

    private static final int DEFAULT_TICK_LABEL_COLOR = Color.LTGRAY;

    private static final int DEFAULT_TICK_LABEL_SELECTED_COLOR = Color.BLACK;

    private static final String DEFAULT_TICK_LABEL = "";

    public static final float DEFAULT_TICK_LABEL_FONT_SP = 4;

    // Corresponds to material indigo 500.
    private static final int DEFAULT_PIN_COLOR = 0xff3f51b5;

    private static final float DEFAULT_CONNECTING_LINE_WEIGHT_DP = 4;

    // Corresponds to material indigo 500.
    private static final int DEFAULT_CONNECTING_LINE_COLOR = 0xff3f51b5;

    private static final float DEFAULT_EXPANDED_PIN_RADIUS_DP = 12;

    private static final float DEFAULT_CIRCLE_SIZE_DP = 5;

    private static final float DEFAULT_BAR_PADDING_BOTTOM_DP = 24;

    // Instance variables for all of the customizable attributes

    private float mTickHeight = DEFAULT_TICK_HEIGHT_DP;

    private float mTickStart = DEFAULT_TICK_START;

    private float mTickEnd = DEFAULT_TICK_END;

    private float mTickInterval = DEFAULT_TICK_INTERVAL;

    private float mBarWeight = DEFAULT_BAR_WEIGHT_DP;

    private boolean mIsBarRounded = false;

    private int mBarColor = DEFAULT_BAR_COLOR;

    private int mPinColor = DEFAULT_PIN_COLOR;

    private int mTextColor = DEFAULT_TEXT_COLOR;

    private float mConnectingLineWeight = DEFAULT_CONNECTING_LINE_WEIGHT_DP;

    private ArrayList<Integer> mConnectingLineColors = new ArrayList<>();

    private float mThumbRadiusDP = DEFAULT_EXPANDED_PIN_RADIUS_DP;

    private int mTickColor = DEFAULT_TICK_COLOR;

    private int mTickLabelColor = DEFAULT_TICK_LABEL_COLOR;

    private int mTickLabelSelectedColor = DEFAULT_TICK_LABEL_SELECTED_COLOR;

    private int mActiveTickLabelColor;

    private int mActiveTickLabelSelectedColor;

    private float mTickLabelSize = DEFAULT_TICK_LABEL_FONT_SP;

    private CharSequence[] mTickBottomLabels;

    private CharSequence[] mTickTopLabels;

    private String mTickDefaultLabel = DEFAULT_TICK_LABEL;

    private float mExpandedPinRadius = DEFAULT_EXPANDED_PIN_RADIUS_DP;

    private int mCircleColor = DEFAULT_CONNECTING_LINE_COLOR;

    private int mCircleBoundaryColor = DEFAULT_CONNECTING_LINE_COLOR;

    private float mCircleBoundarySize = DEFAULT_CIRCLE_BOUNDARY_SIZE_DP;

    private float mCircleSize = DEFAULT_CIRCLE_SIZE_DP;

    private float mMinPinFont = DEFAULT_MIN_PIN_FONT_SP;

    private float mMaxPinFont = DEFAULT_MAX_PIN_FONT_SP;

    // setTickCount only resets indices before a thumb has been pressed or a
    // setThumbIndices() is called, to correspond with intended usage
    private boolean mFirstSetTickCount = true;

    private final DisplayMetrics mDisplayMetrices = getResources().getDisplayMetrics();

    private int mDefaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, mDisplayMetrices);

    private int mDefaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, mDisplayMetrices);

    private int mTickCount = (int) ((mTickEnd - mTickStart) / mTickInterval) + 1;

    private PinView mLeftThumb;

    private PinView mRightThumb;

    private Bar mBar;

    private ConnectingLine mConnectingLine;

    private OnRangeBarChangeListener mListener;

    private OnRangeBarTextListener mPinTextListener;

    private HashMap<Float, String> mTickMap;

    private int mLeftIndex;

    private int mRightIndex;

    private boolean mIsRangeBar = true;

    private float mPinPadding = DEFAULT_PIN_PADDING_DP;

    private float mBarPaddingBottom = DEFAULT_BAR_PADDING_BOTTOM_DP;

    private int mActiveConnectingLineColor;

    private int mActiveBarColor;

    private int mActiveTickColor;

    private int mActiveCircleColor;

    //Used for ignoring vertical moves
    private int mDiffX;

    private int mDiffY;

    private float mLastX;

    private float mLastY;

    private IRangeBarFormatter mFormatter;

    private boolean drawTicks = true;

    private boolean mArePinsTemporary = true;

    private boolean mOnlyOnDrag = false;

    private boolean mPinsEnabled = true;

    private PinTextFormatter mPinTextFormatter = new PinTextFormatter() {
        @Override
        public String getText(String value) {
            if (value.length() > 4) {
                return value.substring(0, 4);
            } else {
                return value;
            }
        }
    };

    // Constructors ////////////////////////////////////////////////////////////

    public RangeBar(Context context) {
        super(context);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        rangeBarInit(context, attrs);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        rangeBarInit(context, attrs);
    }

    // View Methods ////////////////////////////////////////////////////////////

    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();

        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        bundle.putInt("TICK_COUNT", mTickCount);
        bundle.putFloat("TICK_START", mTickStart);
        bundle.putFloat("TICK_END", mTickEnd);
        bundle.putFloat("TICK_INTERVAL", mTickInterval);
        bundle.putInt("TICK_COLOR", mTickColor);
        bundle.putInt("TICK_LABEL_COLOR", mTickLabelColor);
        bundle.putInt("TICK_LABEL_SELECTED_COLOR", mTickLabelSelectedColor);
        bundle.putCharSequenceArray("TICK_TOP_LABELS", mTickTopLabels);
        bundle.putCharSequenceArray("TICK_BOTTOM_LABELS", mTickBottomLabels);
        bundle.putString("TICK_DEFAULT_LABEL", mTickDefaultLabel);

        bundle.putFloat("TICK_HEIGHT_DP", mTickHeight);
        bundle.putFloat("BAR_WEIGHT", mBarWeight);
        bundle.putBoolean("BAR_ROUNDED", mIsBarRounded);
        bundle.putInt("BAR_COLOR", mBarColor);
        bundle.putFloat("CONNECTING_LINE_WEIGHT", mConnectingLineWeight);
        bundle.putIntegerArrayList("CONNECTING_LINE_COLOR", mConnectingLineColors);

        bundle.putFloat("CIRCLE_SIZE", mCircleSize);
        bundle.putInt("CIRCLE_COLOR", mCircleColor);
        bundle.putInt("CIRCLE_BOUNDARY_COLOR", mCircleBoundaryColor);
        bundle.putFloat("CIRCLE_BOUNDARY_WIDTH", mCircleBoundarySize);
        bundle.putFloat("THUMB_RADIUS_DP", mThumbRadiusDP);
        bundle.putFloat("EXPANDED_PIN_RADIUS_DP", mExpandedPinRadius);
        bundle.putFloat("PIN_PADDING", mPinPadding);
        bundle.putFloat("BAR_PADDING_BOTTOM", mBarPaddingBottom);
        bundle.putBoolean("IS_RANGE_BAR", mIsRangeBar);
        bundle.putBoolean("IS_ONLY_ON_DRAG", mOnlyOnDrag);
        bundle.putBoolean("ARE_PINS_TEMPORARY", mArePinsTemporary);
        bundle.putInt("LEFT_INDEX", mLeftIndex);
        bundle.putInt("RIGHT_INDEX", mRightIndex);

        bundle.putBoolean("FIRST_SET_TICK_COUNT", mFirstSetTickCount);

        bundle.putFloat("MIN_PIN_FONT", mMinPinFont);
        bundle.putFloat("MAX_PIN_FONT", mMaxPinFont);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {

            Bundle bundle = (Bundle) state;

            mTickCount = bundle.getInt("TICK_COUNT");
            mTickStart = bundle.getFloat("TICK_START");
            mTickEnd = bundle.getFloat("TICK_END");
            mTickInterval = bundle.getFloat("TICK_INTERVAL");
            mTickColor = bundle.getInt("TICK_COLOR");
            mTickLabelColor = bundle.getInt("TICK_LABEL_COLOR");
            mTickLabelSelectedColor = bundle.getInt("TICK_LABEL_SELECTED_COLOR");
            mTickTopLabels = bundle.getCharSequenceArray("TICK_TOP_LABELS");
            mTickBottomLabels = bundle.getCharSequenceArray("TICK_BOTTOM_LABELS");
            mTickDefaultLabel = bundle.getString("TICK_DEFAULT_LABEL");
            mTickHeight = bundle.getFloat("TICK_HEIGHT_DP");
            mBarWeight = bundle.getFloat("BAR_WEIGHT");
            mIsBarRounded = bundle.getBoolean("BAR_ROUNDED", false);
            mBarColor = bundle.getInt("BAR_COLOR");
            mCircleSize = bundle.getFloat("CIRCLE_SIZE");
            mCircleColor = bundle.getInt("CIRCLE_COLOR");
            mCircleBoundaryColor = bundle.getInt("CIRCLE_BOUNDARY_COLOR");
            mCircleBoundarySize = bundle.getFloat("CIRCLE_BOUNDARY_WIDTH");
            mConnectingLineWeight = bundle.getFloat("CONNECTING_LINE_WEIGHT");
            mConnectingLineColors = bundle.getIntegerArrayList("CONNECTING_LINE_COLOR");

            mThumbRadiusDP = bundle.getFloat("THUMB_RADIUS_DP");
            mExpandedPinRadius = bundle.getFloat("EXPANDED_PIN_RADIUS_DP");
            mPinPadding = bundle.getFloat("PIN_PADDING");
            mBarPaddingBottom = bundle.getFloat("BAR_PADDING_BOTTOM");
            mIsRangeBar = bundle.getBoolean("IS_RANGE_BAR");
            mOnlyOnDrag = bundle.getBoolean("IS_ONLY_ON_DRAG");
            mArePinsTemporary = bundle.getBoolean("ARE_PINS_TEMPORARY");

            mLeftIndex = bundle.getInt("LEFT_INDEX");
            mRightIndex = bundle.getInt("RIGHT_INDEX");
            mFirstSetTickCount = bundle.getBoolean("FIRST_SET_TICK_COUNT");

            mMinPinFont = bundle.getFloat("MIN_PIN_FONT");
            mMaxPinFont = bundle.getFloat("MAX_PIN_FONT");

            setRangePinsByIndices(mLeftIndex, mRightIndex);
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));

        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width;
        int height;

        // Get measureSpec mode and size values.
        final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        // The RangeBar width should be as large as possible.
        if (measureWidthMode == MeasureSpec.AT_MOST) {
            width = measureWidth;
        } else if (measureWidthMode == MeasureSpec.EXACTLY) {
            width = measureWidth;
        } else {
            width = mDefaultWidth;
        }

        // The RangeBar height should be as small as possible.
        if (measureHeightMode == MeasureSpec.AT_MOST) {
            height = Math.min(mDefaultHeight, measureHeight);
        } else if (measureHeightMode == MeasureSpec.EXACTLY) {
            height = measureHeight;
        } else {
            height = mDefaultHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        final Context ctx = getContext();

        // This is the initial point at which we know the size of the View.

        // Create the two thumb objects and position line in view
        float density = mDisplayMetrices.density;
        float expandedPinRadius = mExpandedPinRadius / density;

        final float yPos = h - mBarPaddingBottom;
        if (mIsRangeBar) {
            mLeftThumb = new PinView(ctx);
            mLeftThumb.setFormatter(mFormatter);
            mLeftThumb.init(ctx, yPos, expandedPinRadius, mPinColor, mTextColor, mCircleSize,
                    mCircleColor, mCircleBoundaryColor, mCircleBoundarySize, mMinPinFont, mMaxPinFont, mArePinsTemporary, mPinsEnabled);
        }
        mRightThumb = new PinView(ctx);
        mRightThumb.setFormatter(mFormatter);
        mRightThumb.init(ctx, yPos, expandedPinRadius, mPinColor, mTextColor, mCircleSize,
                mCircleColor, mCircleBoundaryColor, mCircleBoundarySize, mMinPinFont, mMaxPinFont, mArePinsTemporary, mPinsEnabled);

        // Create the underlying bar.
        final float marginLeft = Math.max(mExpandedPinRadius, mCircleSize);

        final float barLength = w - (2 * marginLeft);
        mBar = new Bar(ctx, marginLeft, yPos, barLength, mTickCount, mTickHeight, mTickColor,
                mBarWeight, mBarColor, mIsBarRounded, mTickLabelColor, mTickLabelSelectedColor,
                mTickTopLabels, mTickBottomLabels, mTickDefaultLabel, mTickLabelSize);

        // Initialize thumbs to the desired indices
        if (mIsRangeBar) {
            mLeftThumb.setX(marginLeft + (mLeftIndex / (float) (mTickCount - 1)) * barLength);
            mLeftThumb.setXValue(getPinValue(mLeftIndex));
        }
        mRightThumb.setX(marginLeft + (mRightIndex / (float) (mTickCount - 1)) * barLength);
        mRightThumb.setXValue(getPinValue(mRightIndex));

        // Set the thumb indices.
        final int newLeftIndex = mIsRangeBar ? mBar.getNearestTickIndex(mLeftThumb) : 0;
        final int newRightIndex = mBar.getNearestTickIndex(mRightThumb);

        // Call the listener.
        if (newLeftIndex != mLeftIndex || newRightIndex != mRightIndex) {
            if (mListener != null) {
                mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                        getPinValue(mLeftIndex),
                        getPinValue(mRightIndex));
            }
        }

        // Create the line connecting the two thumbs.
        mConnectingLine = new ConnectingLine(yPos, mConnectingLineWeight,
                mConnectingLineColors);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        mBar.draw(canvas);
        if (mIsRangeBar) {
            mConnectingLine.draw(canvas, mLeftThumb, mRightThumb);
            if (drawTicks) {
                mBar.drawTicks(canvas, mExpandedPinRadius, mRightThumb, mLeftThumb);
            }
            mLeftThumb.draw(canvas);
        } else {
            mConnectingLine.draw(canvas, getMarginLeft(), mRightThumb);
            if (drawTicks) {
                mBar.drawTicks(canvas, mExpandedPinRadius, mRightThumb);
            }
        }
        mRightThumb.draw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // If this View is not enabled, don't allow for touch interactions.
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mDiffX = 0;
                mDiffY = 0;

                mLastX = event.getX();
                mLastY = event.getY();
                onActionDown(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_MOVE:
                onActionMove(event.getX());
                this.getParent().requestDisallowInterceptTouchEvent(true);
                final float curX = event.getX();
                final float curY = event.getY();
                mDiffX += Math.abs(curX - mLastX);
                mDiffY += Math.abs(curY - mLastY);
                mLastX = curX;
                mLastY = curY;

                if (mDiffX < mDiffY) {
                    //vertical touch
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else {
                    //horizontal touch (do nothing as it is needed for RangeBar)
                }
                return true;

            default:
                return false;
        }
    }

    // Public Methods //////////////////////////////////////////////////////////


    /**
     * Sets if the pins works only when drag it.
     *
     * @param onlyOnDrag boolean specifying if the onlyOnDrag is enabled
     */
    public void setOnlyOnDrag(boolean onlyOnDrag) {
        mOnlyOnDrag = onlyOnDrag;
    }

    /**
     * Sets a listener to receive notifications of changes to the RangeBar. This
     * will overwrite any existing set listeners.
     *
     * @param listener the RangeBar notification listener; null to remove any
     *                 existing listener
     */
    public void setOnRangeBarChangeListener(OnRangeBarChangeListener listener) {
        mListener = listener;
    }

    /**
     * Sets a listener to modify the text
     *
     * @param mPinTextListener the RangeBar pin text notification listener; null to remove any
     *                         existing listener
     */
    public void setPinTextListener(OnRangeBarTextListener mPinTextListener) {
        this.mPinTextListener = mPinTextListener;
    }


    public void setFormatter(IRangeBarFormatter formatter) {
        if (mLeftThumb != null) {
            mLeftThumb.setFormatter(formatter);
        }

        if (mRightThumb != null) {
            mRightThumb.setFormatter(formatter);
        }

        mFormatter = formatter;
    }

    public void setDrawTicks(boolean drawTicks) {
        this.drawTicks = drawTicks;
    }

    /**
     * Sets the start tick in the RangeBar.
     *
     * @param tickStart Integer specifying the number of ticks.
     */
    public void setTickStart(float tickStart) {
        int tickCount = (int) ((mTickEnd - tickStart) / mTickInterval) + 1;
        if (isValidTickCount(tickCount)) {
            mTickCount = tickCount;
            mTickStart = tickStart;

            // Prevents resetting the indices when creating new activity, but
            // allows it on the first setting.
            if (mFirstSetTickCount) {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex),
                            getPinValue(mRightIndex));
                }
            }
            if (indexOutOfRange(mLeftIndex, mRightIndex)) {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex),
                            getPinValue(mRightIndex));
                }
            }

            createBar();
            createPins();
        } else {
            Log.e(TAG, "tickCount less than 2; invalid tickCount.");
            throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
        }
    }

    /**
     * Sets the start tick in the RangeBar.
     *
     * @param tickInterval Integer specifying the number of ticks.
     */
    public void setTickInterval(float tickInterval) {
        int tickCount = (int) ((mTickEnd - mTickStart) / tickInterval) + 1;
        if (isValidTickCount(tickCount)) {
            mTickCount = tickCount;
            mTickInterval = tickInterval;

            // Prevents resetting the indices when creating new activity, but
            // allows it on the first setting.
            if (mFirstSetTickCount) {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex), getPinValue(mRightIndex));
                }
            }
            if (indexOutOfRange(mLeftIndex, mRightIndex)) {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex), getPinValue(mRightIndex));
                }
            }

            createBar();
            createPins();
        } else {
            Log.e(TAG, "tickCount less than 2; invalid tickCount.");
            throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
        }
    }

    /**
     * Sets the end tick in the RangeBar.
     *
     * @param tickEnd Integer specifying the number of ticks.
     */
    public void setTickEnd(float tickEnd) {
        int tickCount = (int) ((tickEnd - mTickStart) / mTickInterval) + 1;
        if (isValidTickCount(tickCount)) {
            mTickCount = tickCount;
            mTickEnd = tickEnd;

            // Prevents resetting the indices when creating new activity, but
            // allows it on the first setting.
            if (mFirstSetTickCount) {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex), getPinValue(mRightIndex));
                }
            }
            if (indexOutOfRange(mLeftIndex, mRightIndex)) {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex), getPinValue(mRightIndex));
                }
            }

            createBar();
            createPins();
        } else {
            Log.e(TAG, "tickCount less than 2; invalid tickCount.");
            throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
        }
    }

    /**
     * Sets the height of the ticks in the range bar.
     *
     * @param tickHeight Float specifying the height of each tick mark in dp.
     */
    public void setTickHeight(float tickHeight) {

        mTickHeight = tickHeight;
        createBar();
    }

    /**
     * Set the weight of the bar line and the tick lines in the range bar.
     *
     * @param barWeight Float specifying the weight of the bar and tick lines in
     *                  DP.
     */
    public void setBarWeight(float barWeight) {

        mBarWeight = barWeight;
        createBar();
    }

    public boolean isBarRounded() {
        return mIsBarRounded;
    }

    /**
     * set the bar with rounded corners
     *
     * @param isBarRounded flag
     */
    public void setBarRounded(boolean isBarRounded) {
        this.mIsBarRounded = isBarRounded;
        createBar();
    }

    /**
     * Set the color of the bar line and the tick lines in the range bar.
     *
     * @param barColor Integer specifying the color of the bar line.
     */
    public void setBarColor(int barColor) {
        mBarColor = barColor;
        createBar();
    }

    /**
     * Set the color of the pins.
     *
     * @param pinColor Integer specifying the color of the pin.
     */
    public void setPinColor(int pinColor) {
        mPinColor = pinColor;
        createPins();
    }

    /**
     * Set the color of the text within the pin.
     *
     * @param textColor Integer specifying the color of the text in the pin.
     */
    public void setPinTextColor(int textColor) {
        mTextColor = textColor;
        createPins();
    }

    /**
     * Set if the view is a range bar or a seek bar.
     *
     * @param isRangeBar Boolean - true sets it to rangebar, false to seekbar.
     */
    public void setRangeBarEnabled(boolean isRangeBar) {
        mIsRangeBar = isRangeBar;
        invalidate();
    }


    /**
     * Set if the pins should dissapear after released
     *
     * @param arePinsTemporary Boolean - true if pins shoudl dissapear after released, false to
     *                         stay
     *                         drawn
     */
    public void setTemporaryPins(boolean arePinsTemporary) {
        mArePinsTemporary = arePinsTemporary;
        invalidate();
    }


    /**
     * Set the color of the ticks.
     *
     * @param tickColor Integer specifying the color of the ticks.
     */
    public void setTickColor(int tickColor) {
        mTickColor = tickColor;
        createBar();
    }

    public void setTickLabelColor(int tickLabelColor) {
        mTickLabelColor = tickLabelColor;
        createBar();
    }

    public void setTickLabelSelectedColor(int tickLabelSelectedColor) {
        mTickLabelSelectedColor = tickLabelSelectedColor;
        createBar();
    }

    public void setTickTopLabels(CharSequence[] tickLabels) {
        mTickTopLabels = tickLabels;
        createBar();
    }

    public void setTickBottomLabels(CharSequence[] tickLabels) {
        mTickBottomLabels = tickLabels;
        createBar();
    }

    /**
     * Set the color of the selector.
     *
     * @param selectorColor Integer specifying the color of the ticks.
     */
    public void setSelectorColor(int selectorColor) {
        mCircleColor = selectorColor;
        createPins();
    }

    /**
     * Set the color of the selector Boundary.
     *
     * @param selectorBoundaryColor Integer specifying the boundary color of the ticks.
     */
    public void setSelectorBoundaryColor(int selectorBoundaryColor) {
        mCircleBoundaryColor = selectorBoundaryColor;
        createPins();
    }

    /**
     * Set the size of the selector Boundary.
     *
     * @param selectorBoundarySize Integer specifying the boundary size of ticks.
     *                             Value should be in DP
     */
    public void setSelectorBoundarySize(int selectorBoundarySize) {
        mCircleBoundarySize = selectorBoundarySize;
        createPins();
    }

    /**
     * Set the weight of the connecting line between the thumbs.
     *
     * @param connectingLineWeight Float specifying the weight of the connecting
     *                             line. Value should be in DP
     */
    public void setConnectingLineWeight(float connectingLineWeight) {

        mConnectingLineWeight = connectingLineWeight;
        createConnectingLine();
    }

    /**
     * Set the color of the connecting line between the thumbs.
     *
     * @param connectingLineColor Integer specifying the color of the connecting
     *                            line.
     */
    public void setConnectingLineColor(int connectingLineColor) {

        mConnectingLineColors.clear();
        mConnectingLineColors.add(connectingLineColor);
        createConnectingLine();
    }

    public void setConnectingLineColors(ArrayList<Integer> connectingLineColors) {
        mConnectingLineColors = connectingLineColors;
        createConnectingLine();
    }

    /**
     * If this is set, the thumb images will be replaced with a circle of the
     * specified radius. Default width = 12dp.
     *
     * @param pinRadius Float specifying the radius of the thumbs to be drawn. Value should be in DP
     */
    public void setPinRadius(float pinRadius) {
        mExpandedPinRadius = pinRadius;
        createPins();
    }

    /**
     * Gets the start tick.
     *
     * @return the start tick.
     */
    public float getTickStart() {
        return mTickStart;
    }

    /**
     * Gets the end tick.
     *
     * @return the end tick.
     */
    public float getTickEnd() {
        return mTickEnd;
    }

    /**
     * Gets the tick count.
     *
     * @return the tick count
     */
    public int getTickCount() {
        return mTickCount;
    }

    /**
     * Gets the tick top labels.
     *
     * @return the tick top labels
     */
    public CharSequence[] getTickTopLabels() {
        return mTickTopLabels;
    }

    /**
     * Gets the tick bottom labels.
     *
     * @return the tick bottom labels
     */
    public CharSequence[] getTickBottomLabels() {
        return mTickBottomLabels;
    }

    /**
     * Sets the location of the pins according by the supplied index.
     * Numbered from 0 to mTickCount - 1 from the left.
     *
     * @param leftPinIndex  Integer specifying the index of the left pin
     * @param rightPinIndex Integer specifying the index of the right pin
     */
    public void setRangePinsByIndices(int leftPinIndex, int rightPinIndex) {
        if (indexOutOfRange(leftPinIndex, rightPinIndex)) {
            Log.e(TAG,
                    "Pin index left " + leftPinIndex + ", or right " + rightPinIndex
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + mTickStart + ") and less than the maximum value ("
                            + mTickEnd + ")");
            throw new IllegalArgumentException(
                    "Pin index left " + leftPinIndex + ", or right " + rightPinIndex
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + mTickStart + ") and less than the maximum value ("
                            + mTickEnd + ")");
        } else {

            if (mFirstSetTickCount) {
                mFirstSetTickCount = false;
            }
            mLeftIndex = leftPinIndex;
            mRightIndex = rightPinIndex;
            createPins();

            if (mListener != null) {
                mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                        getPinValue(mLeftIndex), getPinValue(mRightIndex));
            }
        }

        invalidate();
        requestLayout();
    }

    /**
     * Sets the location of pin according by the supplied index.
     * Numbered from 0 to mTickCount - 1 from the left.
     *
     * @param pinIndex Integer specifying the index of the seek pin
     */
    public void setSeekPinByIndex(int pinIndex) {
        if (pinIndex < 0 || pinIndex > mTickCount) {
            Log.e(TAG,
                    "Pin index " + pinIndex
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + 0 + ") and less than the maximum value ("
                            + mTickCount + ")");
            throw new IllegalArgumentException(
                    "Pin index " + pinIndex
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + 0 + ") and less than the maximum value ("
                            + mTickCount + ")");

        } else {

            if (mFirstSetTickCount) {
                mFirstSetTickCount = false;
            }
            mRightIndex = pinIndex;
            createPins();

            if (mListener != null) {
                mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                        getPinValue(mLeftIndex), getPinValue(mRightIndex));
            }
        }
        invalidate();
        requestLayout();
    }

    /**
     * Sets the location of pins according by the supplied values.
     *
     * @param leftPinValue  Float specifying the index of the left pin
     * @param rightPinValue Float specifying the index of the right pin
     */
    public void setRangePinsByValue(float leftPinValue, float rightPinValue) {
        if (valueOutOfRange(leftPinValue, rightPinValue)) {
            Log.e(TAG,
                    "Pin value left " + leftPinValue + ", or right " + rightPinValue
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + mTickStart + ") and less than the maximum value ("
                            + mTickEnd + ")");
            throw new IllegalArgumentException(
                    "Pin value left " + leftPinValue + ", or right " + rightPinValue
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + mTickStart + ") and less than the maximum value ("
                            + mTickEnd + ")");
        } else {
            if (mFirstSetTickCount) {
                mFirstSetTickCount = false;
            }

            mLeftIndex = (int) ((leftPinValue - mTickStart) / mTickInterval);
            mRightIndex = (int) ((rightPinValue - mTickStart) / mTickInterval);
            createPins();

            if (mListener != null) {
                mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                        getPinValue(mLeftIndex), getPinValue(mRightIndex));
            }
        }
        invalidate();
        requestLayout();
    }

    /**
     * Sets the location of pin according by the supplied value.
     *
     * @param pinValue Float specifying the value of the pin
     */
    public void setSeekPinByValue(float pinValue) {
        if (pinValue > mTickEnd || pinValue < mTickStart) {
            Log.e(TAG,
                    "Pin value " + pinValue
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + mTickStart + ") and less than the maximum value ("
                            + mTickEnd + ")");
            throw new IllegalArgumentException(
                    "Pin value " + pinValue
                            + " is out of bounds. Check that it is greater than the minimum ("
                            + mTickStart + ") and less than the maximum value ("
                            + mTickEnd + ")");

        } else {
            if (mFirstSetTickCount) {
                mFirstSetTickCount = false;
            }
            mRightIndex = (int) ((pinValue - mTickStart) / mTickInterval);
            createPins();

            if (mListener != null) {
                mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                        getPinValue(mLeftIndex), getPinValue(mRightIndex));
            }
        }
        invalidate();
        requestLayout();
    }

    /**
     * Gets the type of the bar.
     *
     * @return true if rangebar, false if seekbar.
     */
    public boolean isRangeBar() {
        return mIsRangeBar;
    }

    /**
     * Gets the value of the left pin.
     *
     * @return the string value of the left pin.
     */
    public String getLeftPinValue() {
        return getPinValue(mLeftIndex);
    }

    /**
     * Gets the value of the right pin.
     *
     * @return the string value of the right pin.
     */
    public String getRightPinValue() {
        return getPinValue(mRightIndex);
    }

    /**
     * Gets the index of the left-most pin.
     *
     * @return the 0-based index of the left pin
     */
    public int getLeftIndex() {
        return mLeftIndex;
    }

    /**
     * Gets the index of the right-most pin.
     *
     * @return the 0-based index of the right pin
     */
    public int getRightIndex() {
        return mRightIndex;
    }

    /**
     * Gets the tick interval.
     *
     * @return the tick interval
     */
    public double getTickInterval() {
        return mTickInterval;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            mBarColor = DEFAULT_BAR_COLOR;
            setConnectingLineColor(DEFAULT_BAR_COLOR);
            mCircleColor = DEFAULT_BAR_COLOR;
            mTickColor = DEFAULT_BAR_COLOR;
            mTickLabelColor = DEFAULT_BAR_COLOR;
            mTickLabelSelectedColor = DEFAULT_BAR_COLOR;
        } else {
            mBarColor = mActiveBarColor;
            setConnectingLineColor(mActiveConnectingLineColor);
            mCircleColor = mActiveCircleColor;
            mTickColor = mActiveTickColor;
            mTickLabelColor = mActiveTickLabelColor;
            mTickLabelSelectedColor = mActiveTickLabelSelectedColor;
        }

        super.setEnabled(enabled);
        createBar();
        createPins();
        createConnectingLine();
    }

    public void setPinTextFormatter(PinTextFormatter pinTextFormatter) {
        this.mPinTextFormatter = pinTextFormatter;
    }

    /**
     * Sets the visibility of pins by the supplied value
     *
     * @param enable boolean specifying whether pins are visible
     */
    public void setPinsEnabled(boolean enable) {
        mPinsEnabled = enable;
        createPins();
    }

    public boolean arePinsEnabled() {
        return mPinsEnabled;
    }

    // Private Methods /////////////////////////////////////////////////////////

    /**
     * Does all the functions of the constructor for RangeBar. Called by both
     * RangeBar constructors in lieu of copying the code for each constructor.
     *
     * @param context Context from the constructor.
     * @param attrs   AttributeSet from the constructor.
     */
    private void rangeBarInit(Context context, AttributeSet attrs) {
        //TODO tick value map
        if (mTickMap == null) {
            mTickMap = new HashMap<Float, String>();
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RangeBar, 0, 0);

        try {

            // Sets the values of the user-defined attributes based on the XML
            // attributes.
            final float tickStart = ta
                    .getFloat(R.styleable.RangeBar_mrb_tickStart, DEFAULT_TICK_START);
            final float tickEnd = ta
                    .getFloat(R.styleable.RangeBar_mrb_tickEnd, DEFAULT_TICK_END);
            final float tickInterval = ta
                    .getFloat(R.styleable.RangeBar_mrb_tickInterval, DEFAULT_TICK_INTERVAL);
            int tickCount = (int) ((tickEnd - tickStart) / tickInterval) + 1;
            if (isValidTickCount(tickCount)) {

                // Similar functions performed above in setTickCount; make sure
                // you know how they interact
                mTickCount = tickCount;
                mTickStart = tickStart;
                mTickEnd = tickEnd;
                mTickInterval = tickInterval;
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex),
                            getPinValue(mRightIndex));
                }

            } else {

                Log.e(TAG, "tickCount less than 2; invalid tickCount. XML input ignored.");
            }

            mTickHeight = ta.getDimension(R.styleable.RangeBar_mrb_tickHeight,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TICK_HEIGHT_DP,
                            mDisplayMetrices)
            );
            mBarWeight = ta.getDimension(R.styleable.RangeBar_mrb_barWeight,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BAR_WEIGHT_DP,
                            mDisplayMetrices)
            );
            mCircleSize = ta.getDimension(R.styleable.RangeBar_mrb_selectorSize,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_SIZE_DP,
                            mDisplayMetrices)
            );
            mCircleBoundarySize = ta.getDimension(R.styleable.RangeBar_mrb_selectorBoundarySize,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BOUNDARY_SIZE_DP,
                            mDisplayMetrices)
            );
            mConnectingLineWeight = ta.getDimension(R.styleable.RangeBar_mrb_connectingLineWeight,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CONNECTING_LINE_WEIGHT_DP,
                            mDisplayMetrices)
            );
            mExpandedPinRadius = ta.getDimension(R.styleable.RangeBar_mrb_pinRadius,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_EXPANDED_PIN_RADIUS_DP,
                            mDisplayMetrices)
            );
            mPinPadding = ta.getDimension(R.styleable.RangeBar_mrb_pinPadding,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PIN_PADDING_DP,
                            mDisplayMetrices)
            );
            mBarPaddingBottom = ta.getDimension(R.styleable.RangeBar_mrb_rangeBarPaddingBottom,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BAR_PADDING_BOTTOM_DP,
                            mDisplayMetrices)
            );

            mBarColor = ta.getColor(R.styleable.RangeBar_mrb_rangeBarColor, DEFAULT_BAR_COLOR);
            mTextColor = ta.getColor(R.styleable.RangeBar_mrb_pinTextColor, DEFAULT_TEXT_COLOR);
            mPinColor = ta.getColor(R.styleable.RangeBar_mrb_pinColor, DEFAULT_PIN_COLOR);
            mActiveBarColor = mBarColor;


            mCircleColor = ta.getColor(R.styleable.RangeBar_mrb_selectorColor,
                    DEFAULT_CONNECTING_LINE_COLOR);
            mCircleBoundaryColor = ta.getColor(R.styleable.RangeBar_mrb_selectorBoundaryColor,
                    DEFAULT_CONNECTING_LINE_COLOR);


            mActiveCircleColor = mCircleColor;
            mTickColor = ta.getColor(R.styleable.RangeBar_mrb_tickColor, DEFAULT_TICK_COLOR);
            mActiveTickColor = mTickColor;

            mTickLabelColor = ta.getColor(R.styleable.RangeBar_mrb_tickLabelColor, DEFAULT_TICK_LABEL_COLOR);
            mActiveTickLabelColor = mTickLabelColor;
            mTickLabelSelectedColor = ta.getColor(R.styleable.RangeBar_mrb_tickLabelSelectedColor, DEFAULT_TICK_LABEL_SELECTED_COLOR);
            mActiveTickLabelSelectedColor = mTickLabelSelectedColor;

            mTickBottomLabels = ta.getTextArray(R.styleable.RangeBar_mrb_tickBottomLabels);
            mTickTopLabels = ta.getTextArray(R.styleable.RangeBar_mrb_tickTopLabels);
            mTickDefaultLabel = ta.getString(R.styleable.RangeBar_mrb_tickDefaultLabel);
            mTickDefaultLabel = (mTickDefaultLabel != null) ? mTickDefaultLabel : DEFAULT_TICK_LABEL;

            int mConnectingLineColor = ta.getColor(R.styleable.RangeBar_mrb_connectingLineColor,
                    DEFAULT_CONNECTING_LINE_COLOR);
            mActiveConnectingLineColor = mConnectingLineColor;

            CharSequence[] colors = ta.getTextArray(R.styleable.RangeBar_mrb_connectingLineColors);
            if (colors != null && colors.length > 0) {
                for (CharSequence colorHex : colors) {
                    String hexString = colorHex.toString();

                    if(hexString.length() == 4)
                        hexString += "000";

                    mConnectingLineColors.add(Color.parseColor(hexString));
                }
            } else {
                mConnectingLineColors.add(mConnectingLineColor);
            }

            mIsRangeBar = ta.getBoolean(R.styleable.RangeBar_mrb_rangeBar, true);
            mArePinsTemporary = ta.getBoolean(R.styleable.RangeBar_mrb_temporaryPins, true);
            mIsBarRounded = ta.getBoolean(R.styleable.RangeBar_mrb_rangeBar_rounded, false);

            float density = mDisplayMetrices.density;
            mMinPinFont = ta.getDimension(R.styleable.RangeBar_mrb_pinMinFont,
                    DEFAULT_MIN_PIN_FONT_SP * density);
            mMaxPinFont = ta.getDimension(R.styleable.RangeBar_mrb_pinMaxFont,
                    DEFAULT_MAX_PIN_FONT_SP * density);
            mTickLabelSize = ta.getDimension(R.styleable.RangeBar_mrb_tickLabelSize,
                    DEFAULT_TICK_LABEL_FONT_SP * density);

            mIsRangeBar = ta.getBoolean(R.styleable.RangeBar_mrb_rangeBar, true);

            mOnlyOnDrag = ta.getBoolean(R.styleable.RangeBar_mrb_onlyOnDrag, false);
            mPinsEnabled = ta.getBoolean(R.styleable.RangeBar_mrb_pinsEnabled, true);
        } finally {
            ta.recycle();
        }
    }

    /**
     * Creates a new mBar
     */
    private void createBar() {
        mBar = new Bar(getContext(),
                getMarginLeft(),
                getYPos(),
                getBarLength(),
                mTickCount,
                mTickHeight,
                mTickColor,
                mBarWeight,
                mBarColor,
                mIsBarRounded,
                mTickLabelColor,
                mTickLabelSelectedColor,
                mTickTopLabels,
                mTickBottomLabels,
                mTickDefaultLabel,
                mTickLabelSize);
        invalidate();
    }

    /**
     * Creates a new ConnectingLine.
     */
    private void createConnectingLine() {

        mConnectingLine = new ConnectingLine(getYPos(),
                mConnectingLineWeight,
                mConnectingLineColors);
        invalidate();
    }

    /**
     * Creates two new Pins.
     */
    private void createPins() {
        Context ctx = getContext();
        float yPos = getYPos();

        float expandedPinRadius = 0.0f;
        if (isEnabled()) {
            expandedPinRadius = mExpandedPinRadius / mDisplayMetrices.density;
        }

        if (mIsRangeBar) {
            mLeftThumb = new PinView(ctx);
            mLeftThumb.init(ctx, yPos, expandedPinRadius, mPinColor, mTextColor, mCircleSize, mCircleColor, mCircleBoundaryColor, mCircleBoundarySize,
                    mMinPinFont, mMaxPinFont, mArePinsTemporary, mPinsEnabled);
        }
        mRightThumb = new PinView(ctx);
        mRightThumb
                .init(ctx, yPos, expandedPinRadius, mPinColor, mTextColor, mCircleSize, mCircleColor, mCircleBoundaryColor, mCircleBoundarySize
                        , mMinPinFont, mMaxPinFont, mArePinsTemporary, mPinsEnabled);

        float marginLeft = getMarginLeft();
        float barLength = getBarLength();

        // Initialize thumbs to the desired indices
        if (mIsRangeBar) {
            mLeftThumb.setX(marginLeft + (mLeftIndex / (float) (mTickCount - 1)) * barLength);
            mLeftThumb.setXValue(getPinValue(mLeftIndex));
        }
        mRightThumb.setX(marginLeft + (mRightIndex / (float) (mTickCount - 1)) * barLength);
        mRightThumb.setXValue(getPinValue(mRightIndex));

        invalidate();
    }

    /**
     * Get marginLeft in each of the public attribute methods.
     *
     * @return float marginLeft
     */
    private float getMarginLeft() {
        return Math.max(mExpandedPinRadius, mCircleSize);
    }

    /**
     * Get yPos in each of the public attribute methods.
     *
     * @return float yPos
     */
    private float getYPos() {
        return (getHeight() - mBarPaddingBottom);
    }

    /**
     * Get barLength in each of the public attribute methods.
     *
     * @return float barLength
     */
    private float getBarLength() {
        return (getWidth() - 2 * getMarginLeft());
    }

    /**
     * Returns if either index is outside the range of the tickCount.
     *
     * @param leftThumbIndex  Integer specifying the left thumb index.
     * @param rightThumbIndex Integer specifying the right thumb index.
     * @return boolean If the index is out of range.
     */
    private boolean indexOutOfRange(int leftThumbIndex, int rightThumbIndex) {
        return (leftThumbIndex < 0 || leftThumbIndex >= mTickCount
                || rightThumbIndex < 0
                || rightThumbIndex >= mTickCount);
    }

    /**
     * Returns if either value is outside the range of the tickCount.
     *
     * @param leftThumbValue  Float specifying the left thumb value.
     * @param rightThumbValue Float specifying the right thumb value.
     * @return boolean If the index is out of range.
     */
    private boolean valueOutOfRange(float leftThumbValue, float rightThumbValue) {
        return (leftThumbValue < mTickStart || leftThumbValue > mTickEnd
                || rightThumbValue < mTickStart || rightThumbValue > mTickEnd);
    }

    /**
     * If is invalid tickCount, rejects. TickCount must be greater than 1
     *
     * @param tickCount Integer
     * @return boolean: whether tickCount > 1
     */
    private boolean isValidTickCount(int tickCount) {
        return (tickCount > 1);
    }

    /**
     * Gets the distance between x and the left pin. If the left and right pins are equal, this
     * returns 0 if x is < the pins' position. Also returns 0 if the bar is not a range bar.
     *
     * @param x the x-coordinate to be checked
     * @return the distance between x and the left pin, or 0 if the pins are equal and x is to the left.
     * Also returns 0 if the bar is not a range bar.
     */
    private float getLeftThumbXDistance(float x) {
        if (isRangeBar()) {
            float leftThumbX = mLeftThumb.getX();
            return (leftThumbX == mRightThumb.getX() && x < leftThumbX) ? 0 : Math.abs(leftThumbX - x);
        } else {
            return 0;
        }
    }

    /**
     * Gets the distance between x and the right pin
     *
     * @param x the x-coordinate to be checked
     * @return the distance between x and the right pin
     */
    private float getRightThumbXDistance(float x) {
        return Math.abs(mRightThumb.getX() - x);
    }

    /**
     * Handles a {@link android.view.MotionEvent#ACTION_DOWN} event.
     *
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     */
    private void onActionDown(float x, float y) {
        if (mIsRangeBar) {
            if (!mRightThumb.isPressed() && mLeftThumb.isInTargetZone(x, y)) {

                pressPin(mLeftThumb);

            } else if (!mLeftThumb.isPressed() && mRightThumb.isInTargetZone(x, y)) {

                pressPin(mRightThumb);
            }
        } else {
            if (mRightThumb.isInTargetZone(x, y)) {
                pressPin(mRightThumb);
            }
        }
    }

    /**
     * Handles a {@link android.view.MotionEvent#ACTION_UP} or
     * {@link android.view.MotionEvent#ACTION_CANCEL} event.
     *
     * @param x the x-coordinate of the up action
     * @param y the y-coordinate of the up action
     */
    private void onActionUp(float x, float y) {
        if (mIsRangeBar && mLeftThumb.isPressed()) {

            releasePin(mLeftThumb);

        } else if (mRightThumb.isPressed()) {

            releasePin(mRightThumb);

        } else if (!mOnlyOnDrag) {
            float leftThumbXDistance = getLeftThumbXDistance(x);
            float rightThumbXDistance = getRightThumbXDistance(x);
            //move if is rangeBar and left index is lower of right one
            //if is not range bar leftThumbXDistance is always 0
            if (leftThumbXDistance < rightThumbXDistance && mIsRangeBar) {
                mLeftThumb.setX(x);
                releasePin(mLeftThumb);
            } else {
                mRightThumb.setX(x);
                releasePin(mRightThumb);
            }

            // Get the updated nearest tick marks for each thumb.
            final int newLeftIndex = mIsRangeBar ? mBar.getNearestTickIndex(mLeftThumb) : 0;
            final int newRightIndex = mBar.getNearestTickIndex(mRightThumb);
            // If either of the indices have changed, update and call the listener.
            if (newLeftIndex != mLeftIndex || newRightIndex != mRightIndex) {

                mLeftIndex = newLeftIndex;
                mRightIndex = newRightIndex;

                if (mListener != null) {
                    mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                            getPinValue(mLeftIndex),
                            getPinValue(mRightIndex));
                }
            }
        }
    }

    /**
     * Handles a {@link android.view.MotionEvent#ACTION_MOVE} event.
     *
     * @param x the x-coordinate of the move event
     */
    private void onActionMove(float x) {

        // Move the pressed thumb to the new x-position.
        if (mIsRangeBar && mLeftThumb.isPressed()) {
            movePin(mLeftThumb, x);
        } else if (mRightThumb.isPressed()) {
            movePin(mRightThumb, x);
        }

        // If the thumbs have switched order, fix the references.
        if (mIsRangeBar && mLeftThumb.getX() > mRightThumb.getX()) {
            final PinView temp = mLeftThumb;
            mLeftThumb = mRightThumb;
            mRightThumb = temp;
        }

        // Get the updated nearest tick marks for each thumb.
        int newLeftIndex = mIsRangeBar ? mBar.getNearestTickIndex(mLeftThumb) : 0;
        int newRightIndex = mBar.getNearestTickIndex(mRightThumb);

        final int componentLeft = getPaddingLeft();
        final int componentRight = getRight() - getPaddingRight() - componentLeft;

        if (x <= componentLeft) {
            newLeftIndex = 0;
            movePin(mLeftThumb, mBar.getLeftX());
        } else if (x >= componentRight) {
            newRightIndex = getTickCount() - 1;
            movePin(mRightThumb, mBar.getRightX());
        }
        /// end added code
        // If either of the indices have changed, update and call the listener.
        if (newLeftIndex != mLeftIndex || newRightIndex != mRightIndex) {

            mLeftIndex = newLeftIndex;
            mRightIndex = newRightIndex;
            if (mIsRangeBar) {
                mLeftThumb.setXValue(getPinValue(mLeftIndex));
            }
            mRightThumb.setXValue(getPinValue(mRightIndex));

            if (mListener != null) {
                mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex,
                        getPinValue(mLeftIndex),
                        getPinValue(mRightIndex));
            }
        }
    }

    /**
     * Set the thumb to be in the pressed state and calls invalidate() to redraw
     * the canvas to reflect the updated state.
     *
     * @param thumb the thumb to press
     */
    private void pressPin(final PinView thumb) {
        if (mFirstSetTickCount) {
            mFirstSetTickCount = false;
        }
        if (mArePinsTemporary) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, mExpandedPinRadius);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mThumbRadiusDP = (Float) (animation.getAnimatedValue());
                    thumb.setSize(mThumbRadiusDP, mPinPadding * animation.getAnimatedFraction());
                    invalidate();
                }
            });
            animator.start();
        }

        thumb.press();
    }

    /**
     * Set the thumb to be in the normal/un-pressed state and calls invalidate()
     * to redraw the canvas to reflect the updated state.
     *
     * @param thumb the thumb to release
     */
    private void releasePin(final PinView thumb) {

        final float nearestTickX = mBar.getNearestTickCoordinate(thumb);
        thumb.setX(nearestTickX);
        int tickIndex = mBar.getNearestTickIndex(thumb);
        thumb.setXValue(getPinValue(tickIndex));

        if (mArePinsTemporary) {
            ValueAnimator animator = ValueAnimator.ofFloat(mExpandedPinRadius, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mThumbRadiusDP = (Float) (animation.getAnimatedValue());
                    thumb.setSize(mThumbRadiusDP,
                            mPinPadding - (mPinPadding * animation.getAnimatedFraction()));
                    invalidate();
                }
            });
            animator.start();
        } else {
            invalidate();
        }

        thumb.release();
    }

    /**
     * Set the value on the thumb pin, either from map or calculated from the tick intervals
     * Integer check to format decimals as whole numbers
     *
     * @param tickIndex the index to set the value for
     */
    private String getPinValue(int tickIndex) {
        if (mPinTextListener != null) {
            return mPinTextListener.getPinValue(this, tickIndex);
        }
        float tickValue = (tickIndex == (mTickCount - 1))
                ? mTickEnd
                : (tickIndex * mTickInterval) + mTickStart;
        String xValue = mTickMap.get(tickValue);
        if (xValue == null) {
            if (tickValue == Math.ceil(tickValue)) {
                xValue = String.valueOf((int) tickValue);
            } else {
                xValue = String.valueOf(tickValue);
            }
        }
        return mPinTextFormatter.getText(xValue);
    }

    /**
     * Moves the thumb to the given x-coordinate.
     *
     * @param thumb the thumb to move
     * @param x     the x-coordinate to move the thumb to
     */
    private void movePin(PinView thumb, float x) {

        // If the user has moved their finger outside the range of the bar,
        // do not move the thumbs past the edge.
        if (x < mBar.getLeftX() || x > mBar.getRightX()) {
            // Do nothing.
        } else if (thumb != null) {
            thumb.setX(x);
            invalidate();
        }
    }

    // Inner Classes ///////////////////////////////////////////////////////////

    /**
     * A callback that notifies clients when the RangeBar has changed. The
     * listener will only be called when either thumb's index has changed - not
     * for every movement of the thumb.
     */
    public interface OnRangeBarChangeListener {

        void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                   int rightPinIndex, String leftPinValue, String rightPinValue);
    }

    public interface PinTextFormatter {

        String getText(String value);
    }

    /**
     * @author robmunro
     * A callback that allows getting pin text exernally
     */
    public interface OnRangeBarTextListener {

        String getPinValue(RangeBar rangeBar, int tickIndex);
    }


}
