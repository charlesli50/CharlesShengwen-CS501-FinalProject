package com.example.charlesshengwen_finalproject

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.example.charlesshengwen_finalproject.auth.signOut
import com.example.charlesshengwen_finalproject.db.FoodItemViewer
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


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
fun HomeScreen(auth: FirebaseAuth, googleSignInClient: GoogleSignInClient, modifier: Modifier = Modifier, onSignOut: () -> Unit){
    var viewingFoodItems by remember { mutableStateOf(false) }

    if(viewingFoodItems){
        FoodItemViewer()
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChartCirclePie(
                modifier = modifier,
                charts = charts
            )
            SignOutButton("Sign Out of Google", onClicked = {
                signOut(googleSignInClient, auth, onSignOut)
            })
//        should use navigation system later.
            Button(
                onClick = {
                    viewingFoodItems = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "View Food Items!")
            }
        }
    }
}

@Composable
private fun SignOutButton(
    text: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClicked() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = text
        )
    }
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
