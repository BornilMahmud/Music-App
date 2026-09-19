package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Sonic Tangerine Design System Colors
// ==========================================

// Surface Tiers (Atmospheric Dark Base)
val SonicSurface = Color(0xFF121317)
val SonicSurfaceDim = Color(0xFF121317)
val SonicSurfaceBright = Color(0xFF38393D)
val SonicSurfaceContainerLowest = Color(0xFF0D0E12)
val SonicSurfaceContainerLow = Color(0xFF1A1B20)
val SonicSurfaceContainer = Color(0xFF1E1F24)
val SonicSurfaceContainerHigh = Color(0xFF292A2E)
val SonicSurfaceContainerHighest = Color(0xFF343439)
val SonicSecondarySurface = Color(0xFF222530) // Elevated structural elements (pills, search, bars)
val SonicSurfaceVariant = Color(0xFF343439)

// Foreground & Text
val SonicOnSurface = Color(0xFFE3E2E7)
val SonicOnSurfaceVariant = Color(0xFFE0C0AF)
val SonicInverseSurface = Color(0xFFE3E2E7)
val SonicInverseOnSurface = Color(0xFF2F3035)
val SonicWhite = Color(0xFFFFFFFF)
val SonicSlateMuted = Color(0xFF8E92A0)

// Outlines & Dividers
val SonicOutline = Color(0xFFA78B7C)
val SonicOutlineVariant = Color(0xFF584235)
val SonicBorder = Color(0xFF1F222C)

// Primary Accent (Vivid Warm Tangerine / Orange)
val SonicPrimary = Color(0xFFFFB68B)
val SonicOnPrimary = Color(0xFF522300)
val SonicPrimaryContainer = Color(0xFFFF7A00) // Key interactive trigger (#FF7A00)
val SonicOnPrimaryContainer = Color(0xFF5C2800)
val SonicInversePrimary = Color(0xFF994700)
val SonicSurfaceTint = Color(0xFFFFB68B)

// Secondary (Cool Slate & Indigo tones)
val SonicSecondary = Color(0xFFC4C6D4)
val SonicOnSecondary = Color(0xFF2D303B)
val SonicSecondaryContainer = Color(0xFF464955)
val SonicOnSecondaryContainer = Color(0xFFB6B8C6)

// Tertiary (Warm Amber / Peach)
val SonicTertiary = Color(0xFFFFB780)
val SonicOnTertiary = Color(0xFF4E2600)
val SonicTertiaryContainer = Color(0xFFED8625)
val SonicOnTertiaryContainer = Color(0xFF582B00)
val SonicTertiaryTint = Color(0xFFFF9433) // Focused highlights, glowing waveforms

// Fixed Accents
val SonicPrimaryFixed = Color(0xFFFFDBC8)
val SonicPrimaryFixedDim = Color(0xFFFFB68B)
val SonicOnPrimaryFixed = Color(0xFF321200)
val SonicOnPrimaryFixedVariant = Color(0xFF753400)
val SonicSecondaryFixed = Color(0xFFE0E1F1)
val SonicSecondaryFixedDim = Color(0xFFC4C6D4)
val SonicOnSecondaryFixed = Color(0xFF181B26)
val SonicOnSecondaryFixedVariant = Color(0xFF434652)
val SonicTertiaryFixed = Color(0xFFFFDCC4)
val SonicTertiaryFixedDim = Color(0xFFFFB780)
val SonicOnTertiaryFixed = Color(0xFF2F1400)
val SonicOnTertiaryFixedVariant = Color(0xFF6F3800)

// Error Colors
val SonicError = Color(0xFFFFB4AB)
val SonicOnError = Color(0xFF690005)
val SonicErrorContainer = Color(0xFF93000A)
val SonicOnErrorContainer = Color(0xFFFFDAD6)

// Base Background
val SonicBackground = Color(0xFF121317)
val SonicOnBackground = Color(0xFFE3E2E7)

// Ambient Glow
val SonicGlowColor = Color(0x59FF7A00) // rgba(255, 122, 0, 0.35)

// ==========================================
// Backward-Compatibility Aliases for BM Player
// ==========================================
val BmDarkBackground = SonicBackground
val BmDarkSurface = SonicSurface
val BmDarkSurfaceVariant = SonicSurfaceContainerLow
val BmDarkSurfaceHighlight = SonicSecondarySurface
val BmDarkBorder = SonicBorder

// Primary & Accent Mappings
val BmElectricBlue = SonicPrimaryContainer // #FF7A00
val BmElectricBlueBright = SonicTertiaryTint // #FF9433
val BmViolet = SonicTertiaryContainer // #ED8625
val BmVioletLight = SonicTertiary // #FFB780

// Text Mappings
val BmDarkTextPrimary = SonicWhite
val BmDarkTextSecondary = SonicSlateMuted
val BmDarkTextTertiary = SonicSlateMuted.copy(alpha = 0.7f)

// Light Palette (Warm Tangerine Accents)
val BmLightBackground = Color(0xFFFBF8F6)
val BmLightSurface = Color(0xFFFFFFFF)
val BmLightSurfaceVariant = Color(0xFFF5EFEA)
val BmLightBorder = Color(0xFFE8DFD7)
val BmLightTextPrimary = Color(0xFF1C1A19)
val BmLightTextSecondary = Color(0xFF6B6661)
val BmLightTextTertiary = Color(0xFF9E9790)

// Functional Colors
val BmSuccess = Color(0xFF2ECC71)
val BmError = SonicError
val BmWarning = Color(0xFFFFAB00)
val BmFavorite = Color(0xFFFF3366)
