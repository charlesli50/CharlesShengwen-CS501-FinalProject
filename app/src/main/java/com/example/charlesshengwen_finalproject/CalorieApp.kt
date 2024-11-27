package com.example.charlesshengwen_finalproject

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp


data class ChartPart(
    val value: Float,
    val color: Color,
)


val charts = listOf(
    ChartPart(value = 20f, color = Color.Black),
    ChartPart(value = 30f, color = Color.Gray),
    ChartPart(value = 40f, color = Color.Green),
    ChartPart(value = 10f, color = Color.Red),
)

@Composable
fun CalorieApp(modifier: Modifier = Modifier) {
    ChartCirclePie(
        modifier = modifier,
        charts = charts
    )
}


@Composable
private fun ChartCirclePie(
    modifier: Modifier,
    charts: List<ChartPart>,
    size: Dp = 200.dp,
    strokeWidth: Dp = 16.dp
) {
    Canvas(modifier = modifier
        .size(size)
//        .background(Color.LightGray)
        .padding(12.dp),

        onDraw = {

            var startAngle = 0f

            charts.forEach {

                val sweepAngle = (it.value / 100) * 360

                drawArc(
                    color = it.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                startAngle += sweepAngle
            }

        })

}
