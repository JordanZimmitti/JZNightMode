# JZNightMode
Library That Handles The Device Theme Based On The Sunrise And Sunset

#### How To Access JZNightMode In Your Android App

Add these repositories to the android part of your app build.gradle

    // Repositories For App//
    repositories {

        // Github Repository//
        mavenCentral()

        // Maven Repository Url//
        maven { url 'https://jitpack.io' }
    }
    
Add This line of code to the dependencies in your build.gradle (replace x.x.x with newest version below in green)

    implementation 'com.github.JordanZimmitti:JZNightMode:x.x.x'
[![](https://jitpack.io/v/JordanZimmitti/JZNightMode.svg)](https://jitpack.io/#JordanZimmitti/JZNightMode)


#### Setting Up JZNightMode
JZNightMode can be set up in two ways, the first way is

     // Define And Instantiates JZNightMode Value//
     val jzNightMode = JZNightMode(this, R.style.DayTheme, R.style.NightTheme)
     
The second way is

    // Define And Instantiates JZNightMode Value//
    val jzNightMode = JZNightMode(this)
       
    // Sets The Day Theme And Night Theme//
    jzNightMode.dayTheme   = R.style.DayTheme
    jzNightMode.nightTheme = R.style.NightTheme
    
    
#### Getting The Day And Night Theme

    // Gets The Day And Night Themes//
    jzNightMode.dayTheme
    jzNightMode.nightTheme
    
    
#### Use JZNightMode To Get The Day-Theme Or Night-Theme

    // Gets The Theme Based On The Time Of Day//
    val theme = jzNightMode.getTheme()
    
    // Sets The Theme//
    setTheme(theme)


#### Getting The Sunrise And Sunset Times

    // Gets The Sunrise And Sunset Times In 'HH:MM AA'//
    jzNightMode.getSunrise()
    jzNightMode.getSunset()
    
#### Check Whether It Is Night Or Not

    // Returns True If It Is Night//
    jzNightMode.isNight()
