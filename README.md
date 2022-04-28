# PDAPlugin
ccn pda plugin.
#### Plugin安装教程

**1.Import**

Step 1. Add the JitPack repository to your build file.

Add it in your **root** build.gradle at the end of repositories:

```
buildscript {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.2.2"
    }
}

allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

Add it in your **module** build.gradle at the bottom:
```
android {
    ...
}
dependencies {
    ...
    implementation project(path: ':ObjectBoxLibrary')
    debugImplementation "io.objectbox:objectbox-android-objectbrowser:$objectboxVersion"
}
apply plugin: 'io.objectbox'
```

