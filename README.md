# Smart Loader Library
### A simple library to load & cache remote image resources.


Overview
--------

SmartLoader automatically downloads & caches images in background. It's light weight. It cancels all download requests once activty/fragment is destroyed. It manages all needs for loading images into ImageView. It can also support large list where multiple images are requested at once.

![Image1](pictures/demo.gif.gif)

Features
--------

1. Load images into ImageView
2. Parallel download of images, cancellation of downloads
3. Supports configurable InMemory Cache


Prerequisites
-------------

Images that are loaded from remote server requires internet permission, make sure it's added in manifest.

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

Setup
-------------

## Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
 }
}
```

## Step 2. Add the dependency

```groovy
dependencies {
   implementation 'com.github.umair13adil:'
}
```

Usage
-----

To load image to 'ImageView' simply add this:

```kotlin
    SmartLoader().withContext(context).load("ADD_URL_HERE").into(imageView)
```

## Add Configuration (Optional)

Add following implementation in your Application class. You can setup cache & network configurations.

```kotlin
class MainApplication : Application() {

        override fun onCreate() {
            super.onCreate()

             val configuration = SmartConfiguration()
                            .setCacheSize(20 * 1024 * 1024) //20 MiB
                            .setMaxNoOfRequest(1)
                            .setDebuggable(true)
                            .build()

            //Initialize Smart Loader with custom configurations
            SmartLoader().initWithConfiguration(configuration)
    }

}
```

TODO
----

1. Add File loading functionality

## MIT License

##### Copyright (c) 2018 Muhammad Umair Adil

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
