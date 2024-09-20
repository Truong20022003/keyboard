# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#Sdk
-dontwarn com.tencent.bugly.**
-repackageclasses 'com.notebook'
-repackageclasses 'com.fyber.inneractive.sdk.uni'
-repackageclasses 'com.adjust.sdk'
-repackageclasses 'com.applovin.sdk.AppLovebbinSdk'
-keep interface com.notebook.* { *; }
-keep public class com.notebook.* { *; }
-keep interface com.tencent.bugly.**{*;}
-keep public class com.tencent.bugly.**{*;}
-keep interface com.fyber.inneractive.sdk.* { *; }
-keep public class com.fyber.inneractive.sdk.* { *; }
-keep interface com.adjust.sdk.* { *; }
-keep public class com.adjust.sdk.* { *; }
-keep interface com.applovin.sdk.AppLovinSdk.* { *; }
-keep public class com.applovin.sdk.AppLovinSdk.* { *; }
-keep public class com.notebook.** {*;}
-keep public interface com.notebook.** {*;}