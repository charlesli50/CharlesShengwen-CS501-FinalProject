package com.example.charlesshengwen_finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.charlesshengwen_finalproject.ui.theme.CharlesShengwenFinalProjectTheme

import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toArgb

data class ChartPart(
    val value: Float,
    val color: Color,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharlesShengwenFinalProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

val charts = listOf(
    ChartPart(value = 20f, color = Color.Black),
    ChartPart(value = 30f, color = Color.Gray),
    ChartPart(value = 40f, color = Color.Green),
    ChartPart(value = 10f, color = Color.Red),
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
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
            var sweepAngle = 0f

            charts.forEach {

                sweepAngle = (it.value / 100) * 360

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CharlesShengwenFinalProjectTheme {
        Greeting("Android")
    }
}