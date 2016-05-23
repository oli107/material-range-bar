
package com.appyvet.rangebarsample;

import com.appyvet.rangebar.RangeBar;
import com.appyvet.rangebarsample.colorpicker.ColorPickerDialog;
import com.appyvet.rangebarsample.colorpicker.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
        ColorPickerDialog.OnColorSelectedListener {

    // Sets the initial values such that the image will be drawn
    private static final int INDIGO_500 = 0xff3f51b5;

    // Sets variables to save the colors of each attribute
    private int mBarColor;

    private int mConnectingLineColor;

    private int mPinColor;
    private int mTextColor;

    private int mTickColor;

    // Initializes the RangeBar in the application
    private RangeBar rangebar;

    private int mSelectorColor;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("BAR_COLOR", mBarColor);
        bundle.putInt("CONNECTING_LINE_COLOR", mConnectingLineColor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Removes title bar and sets content view
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Sets fonts for all
//        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
////        ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
////        setFont(root, font);

        // Gets the buttons references for the buttons
        final TextView barColor = (TextView) findViewById(R.id.barColor);
        final TextView connectingLineColor = (TextView) findViewById(R.id.connectingLineColor);
        final TextView pinColor = (TextView) findViewById(R.id.pinColor);
        final TextView pinTextColor = (TextView) findViewById(R.id.textColor);
        final TextView tickColor = (TextView) findViewById(R.id.tickColor);
        final TextView selectorColor = (TextView) findViewById(R.id.selectorColor);
        final TextView indexButton = (TextView) findViewById(R.id.setIndex);
        final TextView valueButton = (TextView) findViewById(R.id.setValue);
        final TextView rangeButton = (TextView) findViewById(R.id.enableRange);
        final TextView disabledButton = (TextView) findViewById(R.id.disable);

        //Sets the buttons to bold.
//        barColor.setTypeface(font, Typeface.BOLD);
//        connectingLineColor.setTypeface(font, Typeface.BOLD);
//        pinColor.setTypeface(font, Typeface.BOLD);

        // Gets the RangeBar
        rangebar = (RangeBar) findViewById(R.id.rangebar1);

        rangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rangebar.setRangeBarEnabled(!rangebar.isRangeBar());
            }
        });
        disabledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rangebar.setEnabled(!rangebar.isEnabled());
            }
        });

        // Setting Index Values -------------------------------

        // Gets the index value TextViews
        final EditText leftIndexValue = (EditText) findViewById(R.id.leftIndexValue);
        final EditText rightIndexValue = (EditText) findViewById(R.id.rightIndexValue);

        // Sets the display values of the indices
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                    int rightPinIndex,
                    String leftPinValue, String rightPinValue) {
                leftIndexValue.setText("" + leftPinIndex);
                rightIndexValue.setText("" + rightPinIndex);
            }

        });

        rangebar.setThumbMoveListener(new RangeBar.OnThumbMoveListener() {
            @Override
            public void onThumbMovingStart(RangeBar rangeBar, boolean isLeftThumb) {
                String toast = (isLeftThumb ? "Left" : "Right") + " thumb starts moving";
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onThumbMovingStop(RangeBar rangeBar, boolean isLeftThumb) {
                String toast = (isLeftThumb ? "Left" : "Right") + " thumb stops moving";
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });

        // Sets the indices themselves upon input from the user
        indexButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Gets the String values of all the texts
                String leftIndex = leftIndexValue.getText().toString();
                String rightIndex = rightIndexValue.getText().toString();

                // Catches any IllegalArgumentExceptions; if fails, should throw
                // a dialog warning the user
                try {
                    if (!leftIndex.isEmpty() && !rightIndex.isEmpty()) {
                        int leftIntIndex = Integer.parseInt(leftIndex);
                        int rightIntIndex = Integer.parseInt(rightIndex);
                        rangebar.setRangePinsByIndices(leftIntIndex, rightIntIndex);
                    }
                } catch (IllegalArgumentException e) {
                }
            }
        });

        // Sets the indices by values based upon input from the user
        valueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Gets the String values of all the texts
                String leftValue = leftIndexValue.getText().toString();
                String rightValue = rightIndexValue.getText().toString();

                // Catches any IllegalArgumentExceptions; if fails, should throw
                // a dialog warning the user
                try {
                    if (!leftValue.isEmpty() && !rightValue.isEmpty()) {
                        float leftIntIndex = Float.parseFloat(leftValue);
                        float rightIntIndex = Float.parseFloat(rightValue);
                        rangebar.setRangePinsByValue(leftIntIndex, rightIntIndex);
                    }
                } catch (IllegalArgumentException e) {
                }
            }
        });

        // Setting Number Attributes -------------------------------

        // Sets tickStart
        final TextView tickStart = (TextView) findViewById(R.id.tickStart);
        SeekBar tickStartSeek = (SeekBar) findViewById(R.id.tickStartSeek);
        tickStartSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar tickCountSeek, int progress, boolean fromUser) {
                try {
                    rangebar.setTickStart(progress);
                } catch (IllegalArgumentException e) {
                }
                tickStart.setText("tickStart = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets tickEnd
        final TextView tickEnd = (TextView) findViewById(R.id.tickEnd);
        SeekBar tickEndSeek = (SeekBar) findViewById(R.id.tickEndSeek);
        tickEndSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar tickCountSeek, int progress, boolean fromUser) {
                try {
                    rangebar.setTickEnd(progress);
                } catch (IllegalArgumentException e) {
                }
                tickEnd.setText("tickEnd = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets tickInterval
        final TextView tickInterval = (TextView) findViewById(R.id.tickInterval);
        SeekBar tickIntervalSeek = (SeekBar) findViewById(R.id.tickIntervalSeek);
        tickIntervalSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar tickCountSeek, int progress, boolean fromUser) {
                try {
                    rangebar.setTickInterval(progress / 10.0f);
                } catch (IllegalArgumentException e) {
                }
                tickInterval.setText("tickInterval = " + progress / 10.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets barWeight
        final TextView barWeight = (TextView) findViewById(R.id.barWeight);
        SeekBar barWeightSeek = (SeekBar) findViewById(R.id.barWeightSeek);
        barWeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar barWeightSeek, int progress, boolean fromUser) {
                rangebar.setBarWeight(progress);
                barWeight.setText("barWeight = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets connectingLineWeight
        final TextView connectingLineWeight = (TextView) findViewById(R.id.connectingLineWeight);
        SeekBar connectingLineWeightSeek = (SeekBar) findViewById(R.id.connectingLineWeightSeek);
        connectingLineWeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar connectingLineWeightSeek, int progress,
                    boolean fromUser) {
                rangebar.setConnectingLineWeight(progress);
                connectingLineWeight.setText("connectingLineWeight = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets thumbRadius
        final TextView thumbRadius = (TextView) findViewById(R.id.thumbRadius);
        SeekBar thumbRadiusSeek = (SeekBar) findViewById(R.id.thumbRadiusSeek);
        thumbRadiusSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar thumbRadiusSeek, int progress, boolean fromUser){
                    rangebar.setPinRadius(progress);
                    thumbRadius.setText("Pin Radius = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Setting Color Attributes---------------------------------

        // Sets barColor
        barColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.BAR_COLOR, mBarColor, mBarColor);
            }
        });

        // Sets connectingLineColor
        connectingLineColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.CONNECTING_LINE_COLOR, mConnectingLineColor,
                        mConnectingLineColor);
            }
        });

        // Sets pinColor
        pinColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.PIN_COLOR, mPinColor, mPinColor);
            }
        });
        // Sets pinTextColor
        pinTextColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.TEXT_COLOR, mTextColor, mTextColor);
            }
        });
        // Sets tickColor
        tickColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.TICK_COLOR, mTickColor, mTickColor);
            }
        });
        // Sets selectorColor
        selectorColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.SELECTOR_COLOR, mSelectorColor, mSelectorColor);
            }
        });

    }

    /**
     * Sets the changed color using the ColorPickerDialog.
     *
     * @param component Component specifying which input is being used
     * @param newColor  Integer specifying the new color to be selected.
     */

    @Override
    public void onColorSelected(int newColor, Component component) {
        Log.d("Color selected"," new color = "+newColor+ ",compoment = "+component);
        String hexColor = String.format("#%06X", (0xFFFFFF & newColor));

        switch (component) {
            case BAR_COLOR:
                mBarColor = newColor;
                rangebar.setBarColor(newColor);
                final TextView barColorText = (TextView) findViewById(R.id.barColor);
                barColorText.setText("barColor = " + hexColor);
                barColorText.setTextColor(newColor);
                break;
            case TEXT_COLOR:
                mTextColor = newColor;
                rangebar.setPinTextColor(newColor);
                final TextView textColorText = (TextView) findViewById(R.id.textColor);
                textColorText.setText("textColor = " + hexColor);
                textColorText.setTextColor(newColor);
                break;

            case CONNECTING_LINE_COLOR:
                mConnectingLineColor = newColor;
                rangebar.setConnectingLineColor(newColor);
                final TextView connectingLineColorText = (TextView) findViewById(
                        R.id.connectingLineColor);
                connectingLineColorText.setText("connectingLineColor = " + hexColor);
                connectingLineColorText.setTextColor(newColor);
                break;

            case PIN_COLOR:
                mPinColor = newColor;
                rangebar.setPinColor(newColor);
                final TextView pinColorText = (TextView) findViewById(R.id.pinColor);
                pinColorText.setText("pinColor = " + hexColor);
                pinColorText.setTextColor(newColor);
                break;
            case TICK_COLOR:
                mTickColor = newColor;
                rangebar.setTickColor(newColor);
                final TextView tickColorText = (TextView) findViewById(R.id.tickColor);
                tickColorText.setText("tickColor = " + hexColor);
                tickColorText.setTextColor(newColor);
                break;
            case SELECTOR_COLOR:
                mSelectorColor = newColor;
                rangebar.setSelectorColor(newColor);
                final TextView selectorColorText = (TextView) findViewById(R.id.selectorColor);
                selectorColorText.setText("selectorColor = " + hexColor);
                selectorColorText.setTextColor(newColor);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Initiates the colorPicker from within a button function.
     *
     * @param component    Component specifying which input is being used
     * @param initialColor Integer specifying the initial color choice. *
     * @param defaultColor Integer specifying the default color choice.
     */
    private void initColorPicker(Component component, int initialColor, int defaultColor) {
        ColorPickerDialog colorPicker = ColorPickerDialog
                .newInstance(R.string.colorPickerTitle, Utils.ColorUtils.colorChoice(this),
                        initialColor, 4, ColorPickerDialog.SIZE_SMALL, component);
        colorPicker.setOnColorSelectedListener(this);
        colorPicker.show(getFragmentManager(), "color");
    }
}
