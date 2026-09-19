package com.example.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BmDarkSurface
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmElectricBlue

@Composable
fun BMBigLogoBadge(
    modifier: Modifier = Modifier,
    logoSize: Dp = 80.dp,
    showTagline: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BmDarkSurfaceHighlight.copy(alpha = 0.6f),
                        BmDarkSurface
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        BmElectricBlue.copy(alpha = 0.4f),
                        Color.Transparent,
                        BmElectricBlue.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // BM Emblem with Play Button and Note
            Box(
                modifier = Modifier
                    .size(logoSize)
                    .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = BmElectricBlue)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0C101A)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bm_logo),
                    contentDescription = "BM Player Logo",
                    modifier = Modifier.size(logoSize)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // BM PLAYER Title
            Text(
                text = "BM PLAYER",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp,
                    fontSize = 20.sp,
                    color = Color.White
                )
            )

            if (showTagline) {
                Spacer(modifier = Modifier.height(4.dp))

                // Tagline: YOUR AUDIO. YOUR WAY.
                Text(
                    text = "YOUR AUDIO. YOUR WAY.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.5.sp,
                        fontSize = 10.sp,
                        color = BmElectricBlue.copy(alpha = 0.85f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Equalizer wave graphic accent
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(20.dp).height(1.dp).background(BmElectricBlue.copy(alpha = 0.4f)))
                    Box(modifier = Modifier.width(3.dp).height(8.dp).clip(RoundedCornerShape(2.dp)).background(BmElectricBlue))
                    Box(modifier = Modifier.width(3.dp).height(14.dp).clip(RoundedCornerShape(2.dp)).background(com.example.ui.theme.SonicTertiaryTint))
                    Box(modifier = Modifier.width(3.dp).height(20.dp).clip(RoundedCornerShape(2.dp)).background(BmElectricBlue))
                    Box(modifier = Modifier.width(3.dp).height(12.dp).clip(RoundedCornerShape(2.dp)).background(com.example.ui.theme.SonicTertiaryTint))
                    Box(modifier = Modifier.width(3.dp).height(6.dp).clip(RoundedCornerShape(2.dp)).background(BmElectricBlue))
                    Box(modifier = Modifier.width(20.dp).height(1.dp).background(BmElectricBlue.copy(alpha = 0.4f)))
                }
            }
        }
    }
}
