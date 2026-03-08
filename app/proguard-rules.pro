# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.ecotracker.data.model.** { *; }
-keep class com.ecotracker.data.remote.** { *; }
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod
