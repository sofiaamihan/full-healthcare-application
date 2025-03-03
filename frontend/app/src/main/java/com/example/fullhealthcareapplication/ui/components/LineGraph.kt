package com.example.fullhealthcareapplication.ui.components

import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class GraphAppearance(
    val graphColor: Color,
    val graphAxisColor: Color,
    val graphThickness: Float,
    val iscolorAreaUnderChart: Boolean,
    val colorAreaUnderChart: Color,
    val isCircleVisible: Boolean,
    val circleColor: Color,
    val backgroundColor: Color
)

//@Composable
//fun Graph(
//    modifier : Modifier,
//    xValues: List<Int>,
//    yValues: List<Int>,
//    points: List<Float>,
//    paddingSpace: Dp,
//    verticalStep: Int,
//    graphAppearance: GraphAppearance
//) {
//    val controlPoints1 = mutableListOf<PointF>()
//    val controlPoints2 = mutableListOf<PointF>()
//    val coordinates = mutableListOf<PointF>()
//    val density = LocalDensity.current
//    val textPaint = remember(density) {
//        Paint().apply {
//            color = graphAppearance.graphAxisColor.toArgb()
//            textAlign = Paint.Align.CENTER
//            textSize = density.run { 12.sp.toPx() }
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .background(graphAppearance.backgroundColor)
//            .padding(horizontal = 8.dp, vertical = 12.dp),
//        contentAlignment = Center
//    ) {
//        Canvas(
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .fillMaxHeight(0.7f),
//        ) {
//            val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
//            val yAxisSpace = size.height / yValues.size
//            /** placing x axis points */
//            for (i in xValues.indices) {
//                drawContext.canvas.nativeCanvas.drawText(
//                    "${xValues[i]}",
//                    xAxisSpace * (i+1),
//                    size.height - 30,
//                    textPaint
//                )
//            }
//            /** placing y axis points */
//            for (i in yValues.indices) {
//                drawContext.canvas.nativeCanvas.drawText(
//                    "${yValues[i]}",
//                    paddingSpace.toPx() / 2f,
//                    size.height - yAxisSpace * (i + 1),
//                    textPaint
//                )
//            }
//            /** placing our x axis points */
//            for (i in points.indices) {
//                val x1 = xAxisSpace * xValues[i]
//                val y1 = size.height - (yAxisSpace * (points[i] / verticalStep.toFloat()))
//                coordinates.add(PointF(x1, y1))
//                /** drawing circles to indicate all the points */
//                if (graphAppearance.isCircleVisible) {
//                    drawCircle(
//                        color = graphAppearance.circleColor,
//                        radius = 10f,
//                        center = Offset(x1, y1)
//                    )
//                }
//            }
//            /** calculating the connection points */
//            for (i in 1 until coordinates.size) {
//                controlPoints1.add(
//                    PointF(
//                        (coordinates[i].x + coordinates[i - 1].x) / 2,
//                        coordinates[i - 1].y
//                    )
//                )
//                controlPoints2.add(
//                    PointF(
//                        (coordinates[i].x + coordinates[i - 1].x) / 2,
//                        coordinates[i].y
//                    )
//                )
//            }
//            /** drawing the path */
//            val stroke = Path().apply {
//                reset()
//                moveTo(coordinates.first().x, coordinates.first().y)
//                for (i in 0 until coordinates.size - 1) {
//                    cubicTo(
//                        controlPoints1[i].x, controlPoints1[i].y,
//                        controlPoints2[i].x, controlPoints2[i].y,
//                        coordinates[i + 1].x, coordinates[i + 1].y
//                    )
//                }
//            }
//
//            /** filling the area under the path */
//            val fillPath = android.graphics.Path(stroke.asAndroidPath())
//                .asComposePath()
//                .apply {
//                    lineTo(xAxisSpace * xValues.last(), size.height - yAxisSpace)
//                    lineTo(xAxisSpace, size.height - yAxisSpace)
//                    close()
//                }
//            if (graphAppearance.iscolorAreaUnderChart) {
//                drawPath(
//                    fillPath,
//                    brush = Brush.verticalGradient(
//                        listOf(
//                            graphAppearance.colorAreaUnderChart,
//                            Color.Transparent,
//                        ),
//                        endY = size.height - yAxisSpace
//                    ),
//                )
//            }
//            drawPath(
//                stroke,
//                color = graphAppearance.graphColor,
//                style = Stroke(
//                    width = graphAppearance.graphThickness,
//                    cap = StrokeCap.Round
//                )
//            )
//        }
//    }
//}
@Composable
fun Graph(
    modifier: Modifier,
    xValues: List<Int>,
    yValues: List<Int>,
    points: List<Float>,
    paddingSpace: Dp,
    verticalStep: Int,
    graphAppearance: GraphAppearance
) {
    val controlPoints1 = mutableListOf<PointF>()
    val controlPoints2 = mutableListOf<PointF>()
    val coordinates = mutableListOf<PointF>()
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = graphAppearance.graphAxisColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(graphAppearance.backgroundColor)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(200.dp),
        ) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
            val yAxisSpace = size.height / (yValues.size - 1)

            // Draw x-axis at the center
            drawLine(
                color = graphAppearance.graphAxisColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 4f
            )


            /** placing x axis points */
            for (i in xValues.indices) {
                val label = when (i) {
                    0 -> "X"
                    1 -> "Y"
                    2 -> "Z"
                    else -> ""
                }

                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    xAxisSpace * (i + 1),
                    size.height - 30,
                    textPaint
                )
            }

            /** placing y axis points */
            for (i in yValues.indices) {
                val yPosition = size.height - (yAxisSpace * i)
                if(i%5 == 0){
                    drawContext.canvas.nativeCanvas.drawText(
                        "${yValues[i]}",
                        paddingSpace.toPx() / 2f,
                        yPosition,
                        textPaint
                    )
                }
            }

            /** placing data points */
            for (i in points.indices) {
                val x1 = xAxisSpace * xValues[i]
                val y1 = size.height / 2 - (yAxisSpace * (points[i] / verticalStep.toFloat()))
                coordinates.add(PointF(x1, y1))

                // Draw points as circles
                if (graphAppearance.isCircleVisible) {
                    drawCircle(
                        color = graphAppearance.circleColor,
                        radius = 10f,
                        center = Offset(x1, y1)
                    )
                }
            }

            /** Drawing smooth curve */
            for (i in 1 until coordinates.size) {
                controlPoints1.add(
                    PointF(
                        (coordinates[i].x + coordinates[i - 1].x) / 2,
                        coordinates[i - 1].y
                    )
                )
                controlPoints2.add(
                    PointF(
                        (coordinates[i].x + coordinates[i - 1].x) / 2,
                        coordinates[i].y
                    )
                )
            }

            /** Draw path */
            val stroke = Path().apply {
                reset()
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
                    cubicTo(
                        controlPoints1[i].x, controlPoints1[i].y,
                        controlPoints2[i].x, controlPoints2[i].y,
                        coordinates[i + 1].x, coordinates[i + 1].y
                    )
                }
            }

            /** Fill area under curve */
            val fillPath = android.graphics.Path(stroke.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(xAxisSpace * xValues.last(), size.height / 2)
                    lineTo(xAxisSpace, size.height / 2)
                    close()
                }
            if (graphAppearance.iscolorAreaUnderChart) {
                drawPath(
                    fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            graphAppearance.colorAreaUnderChart,
                            Color.Transparent,
                        ),
                        endY = size.height / 2
                    ),
                )
            }
            drawPath(
                stroke,
                color = graphAppearance.graphColor,
                style = Stroke(
                    width = graphAppearance.graphThickness,
                    cap = StrokeCap.Round
                )
            )

            val fillAbovePath = android.graphics.Path(stroke.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(xAxisSpace * xValues.last(), 0f)
                    lineTo(xAxisSpace, 0f)
                    close()
                }
            if (graphAppearance.iscolorAreaUnderChart) {
                drawPath(
                    fillAbovePath,
                    brush = Brush.verticalGradient(
                        listOf(
                            graphAppearance.colorAreaUnderChart.copy(alpha = 0.5f),
                            Color.Transparent,
                        ),
                        endY = 0f
                    ),
                )
            }
        }
    }
}
