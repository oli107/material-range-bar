# MaterialRangeBar

MaterialRangeBar is a fork of https://github.com/edmodo/range-bar that adds some basic material styling, as well as start and end values, values as floats and some other things. It is aiming to mimic this:

http://www.google.com/design/spec/components/sliders.html

It is similar to an enhanced SeekBar widget, though it doesn't make use of the SeekBar. It provides for the selection of a range of values as well as for a single value. The selectable range values are discrete values designated by tick marks; the pin (handle) will snap to the nearest tick mark. This is my first library project, apologies for poor coding, etc etc.

Supported on API Level 12 and above for animations.
![ScreenShot](https://github.com/oli107/material-range-bar/blob/master/Screenshots/screenshot%20pin%20active.png)
![ScreenShot](https://github.com/oli107/material-range-bar/blob/master/Screenshots/screenshot.png)

# Installation

**build.gradle**

	 maven {
        url 'http://dl.bintray.com/oli107/maven'
    }

	dependencies {
    compile 'com.appyvet:rangebar:0.0.7'
	}


## Custom Attributes (both via XML and programmatically):

### Tick Properties
```
tickStart | float
tickEnd | float
tickInterval | float
tickHeight | dimension
tickColor | color
```

###  Bar Properties
```
rangeBar | boolean
barWeight | dimension
barColor | reference or color
barPaddingBottom | dimension
connectingLineWeight | dimension
connectingLineColor | reference or color
```

### Pin Properties
```
pinPadding | dimension
pinRadius | dimension
```

### Selector Properties
```
selectorColor | reference or color
selectorSize | dimension
```

### Via runtime only (no XML option)
```
pin indices (the location of the thumbs on the RangeBar)
```

## Examples

### Layout XML

This is a rangebar with both a lower and upper value
```xml
<com.appyvet.rangebar.RangeBar
	xmlns:custom="http://schemas.android.com/apk/res-auto"
	android:id="@+id/rangebar"
	android:layout_width="match_parent"
	android:layout_height="72dp"
	custom:tickStart="5"
	custom:tickInterval="1"
	custom:tickEnd="10"
	android:layout_marginLeft="16dp"
	android:layout_marginRight="16dp"/>
```

This is a seekbar with only a single value (note rangeBar=false)
```xml
<com.appyvet.rangebar.RangeBar
	xmlns:custom="http://schemas.android.com/apk/res-auto"
	android:id="@+id/rangebar"
	android:layout_width="match_parent"
	android:layout_height="72dp"
	custom:rangeBar="false"
	android:layout_marginLeft="16dp"
	android:layout_marginRight="16dp"/>
```

### Adding a listener
//TODO add a full example here
Add a listener - rangeBar.setOnRangeBarChangeListener which returns left and right index as well as value.


## TODO

- Better instructions.
- Properly implement Map of strings to each value
- Generally make it a better project!

## Demo

[Get it from the Google Play Store](https://play.google.com/store/apps/details?id=com.appyvet.rangebarsample)

## License

Copyright 2014, AppyVet, Inc. 

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
