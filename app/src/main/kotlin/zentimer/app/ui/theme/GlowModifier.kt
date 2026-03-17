package zentimer.app.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Рисует неоновое свечение в центре области.
 * Используется для кольца таймера и точек созвездия.
 */
fun Modifier.glow(
    color: Color,
    radius: Dp = 40.dp,
    alpha: Float = 0.25f
): Modifier = this.then(
    drawBehind {
        drawGlow(color, radius.toPx(), alpha)
    }
)

private fun DrawScope.drawGlow(
    color: Color,
    radius: Float,
    alpha: Float
) {
    val glowColor = color.copy(alpha = alpha)
    for (i in 3 downTo 1) {
        val layerRadius = radius * i * 0.5f
        val layerAlpha = alpha * (1f - (i - 1) * 0.15f) / 3f
        drawCircle(
            color = glowColor.copy(alpha = layerAlpha),
            radius = layerRadius,
            center = Offset(size.width / 2f, size.height / 2f),
            blendMode = BlendMode.Screen
        )
    }
}
