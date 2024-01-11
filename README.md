# MediaFile
**Welcome to this MediaFile !**

MediaFile will help you to get Images, GIF, Videos from the internal or external storage. It will help you to capture a image or record a video as well.
You don't need to write any code to ask permission. Just call the predefined method to ask permission from user.

**To Use this library just follow the simple steps given below -**

## Step : 1 - 
Add `jitpack.io` in the Project Level _build.gradle_ file
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add `dependencies` in the Module Level _build.gradle_ file 
( **Use Current Version** - [![](https://jitpack.io/v/WackyCodes/MediaFile.svg)](https://jitpack.io/#WackyCodes/MediaFile) )
```groovy
dependencies {
       implementation 'com.github.WackyCodes:MediaFile:v1.0.1'
}
```

## Step : 2 - You can extends any components metioned below - 

### Option 1 - For Image

extends `ActivityMyFile` or `FragmentMyFile` and call `queryToSelectImage()` or `queryToCaptureImage()` method.
You have to pass refence of `OnGetImageListener` as an argument. The ModelImage object will hold the diffent information you need.
```java
    public interface OnGetImageListener {
        void onGetImage(@Nullable ModelImage modelImage, @Nullable String message);
    }
```
