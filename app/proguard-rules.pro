# Strip verbose and debug logging in release builds to eliminate string formatting and CPU overhead
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}

# Media3 ExoPlayer keep rules
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Room Database keep rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Coil ImageLoader keep rules
-keep class coil.** { *; }
-dontwarn coil.**

# Kotlin Coroutines optimization
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

