package com.example.fullhealthcareapplication.ui.screens

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullhealthcareapplication.ui.components.AppBar
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.data.entity.Activity
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.viewmodel.DeleteActivityViewModel
import com.example.fullhealthcareapplication.data.viewmodel.EditActivityViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllActivitiesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllCategoriesViewModel
import com.example.fullhealthcareapplication.ui.components.ActivityDisplay
import com.example.fullhealthcareapplication.ui.components.DeleteActivityDialog
import com.example.fullhealthcareapplication.ui.components.EditActivityDialog
import com.example.fullhealthcareapplication.ui.components.StepCounterBarGraph
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PhysicalScreen(
    toHome: () -> Unit,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    tokenDataStore: TokenDataStore

){
    val date = LocalDateTime.now().toString()
    val dateKeyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateKey = LocalDateTime.now().format(dateKeyFormatter)
    val getAllActivitiesViewModel: GetAllActivitiesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getAllCategoriesViewModel : GetAllCategoriesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val state = getAllActivitiesViewModel.state
    val categoryState = getAllCategoriesViewModel.state

    val showEditDialog = remember { mutableStateOf(false) }
    val activityToEdit = remember { mutableStateOf<Activity?>(null) }
    val editActivityViewModel : EditActivityViewModel = viewModel(factory = healthServiceViewModelFactory)
    val editState = editActivityViewModel.state

    val showDeleteDialog = remember { mutableStateOf(false) }
    val deleteActivityViewModel : DeleteActivityViewModel = viewModel(factory = healthServiceViewModelFactory)
    val activityToDelete = remember { mutableStateOf<Activity?>(null) }

    val id = remember { mutableIntStateOf(0) }
    val userId = remember { mutableIntStateOf(0) }
    val activityCategoryId = remember { mutableIntStateOf(0) }
    val timeTaken = remember { mutableStateOf("") }
    val caloriesBurnt = remember { mutableDoubleStateOf(0.0) }
    val stepCount = remember { mutableDoubleStateOf(0.0) }
    val distance = remember { mutableDoubleStateOf(0.0) }
    val walkingSpeed = remember { mutableDoubleStateOf(0.0) }
    val walkingSteadiness = remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(Unit) {
        getAllActivitiesViewModel.getAllActivities(
            tokenDataStore.getId.first()?.toInt() ?: 0,
            dateKey.toString()
        )
        getAllCategoriesViewModel.getAllCategories()
    }

    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )
    val physicalDescription = "Cardio fitness, or cardiorespiratory fitness, is your body's ability to take in, circulate and use oxygen. This process relies on vital parts of the body such as your heart and lungs. Therefore, aerobic exercises such as running or high-intensity interval training are great for your fitness."
    AppBar(
        title = "Physical",
        toHome = {toHome()}
    )
//    Box(
//        modifier = Modifier
//            .padding(top = 110.dp)
//            .clip(RoundedCornerShape(bottomStart = 38.dp, bottomEnd = 38.dp))
//            .height(20.dp)
//            .fillMaxWidth()
//            .background(color = MaterialTheme.colorScheme.secondaryContainer)
//    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card (
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(32.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(horizontalGradientBrush, alpha = 0.5f)
                    .height(100.dp)
                    .fillMaxWidth(0.8f)
            ){
                Column (
                    modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "About Physical",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = physicalDescription,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val stepCounts = listOf(3000, 5000, 7000, 4000, 6000, 8000, 2000)
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 40.dp, end = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Weekly Step Count",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            StepCounterBarGraph(stepCounts = stepCounts, days = daysOfWeek)
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Today",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.cachedActivityList.size) { index ->
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                val cat = remember {  mutableStateOf("") }
                categoryState.cachedCategoryList.forEachIndexed { i, iItem ->
                    if (state.cachedActivityList[index].categoryId == categoryState.cachedCategoryList[i].id){
                        cat.value = categoryState.cachedCategoryList[i].name
                    }
                }
                ActivityDisplay(
                    cat.value,
                    state.cachedActivityList[index].caloriesBurnt,
                    state.cachedActivityList[index].stepCount,
                    state.cachedActivityList[index].distance,
                    state.cachedActivityList[index].walkingSpeed,
                    state.cachedActivityList[index].walkingSteadiness,
                    {
                        activityToEdit.value = state.cachedActivityList[index]
                        showEditDialog.value = true
                    },
                    {
                        activityToDelete.value = state.cachedActivityList[index]
                        showDeleteDialog.value = true
                    }
                )
            }
        }
    }
        activityToEdit.value?.let { activity ->
            if (showEditDialog.value) {
                LaunchedEffect(showEditDialog) {
                    activityCategoryId.intValue = activity.categoryId
                    caloriesBurnt.doubleValue = activity.caloriesBurnt
                    stepCount.doubleValue = activity.stepCount
                    distance.doubleValue = activity.distance
                    walkingSpeed.doubleValue = activity.walkingSpeed
                    walkingSteadiness.doubleValue = activity.walkingSteadiness
                }
                EditActivityDialog(
                    onDismiss = { showEditDialog.value = false },
                    onEditActivity = { id, userId, activityCategoryId, date, caloriesBurnt, stepCount, distance, walkingSpeed, walkingSteadiness ->
                        editActivityViewModel.editActivity( id, userId, activityCategoryId, date, caloriesBurnt, stepCount, distance, walkingSpeed, walkingSteadiness)
                        showEditDialog.value = false
                        toHome()
                    },
                    id = activity.id,
                    userId = activity.userId,
                    date = date,
                    activityCategoryId = activityCategoryId.intValue,
                    onActivityCategoryIdChange = { activityCategoryId.intValue = it },
                    caloriesBurnt = caloriesBurnt.doubleValue,
                    onCaloriesBurntChange = { caloriesBurnt.doubleValue = it },
                    stepCount = stepCount.doubleValue,
                    onStepCountChange = { stepCount.doubleValue = it },
                    distance = distance.doubleValue,
                    onDistanceChange = { distance.doubleValue = it },
                    walkingSpeed = walkingSpeed.doubleValue,
                    onWalkingSpeedChange = { walkingSpeed.doubleValue = it },
                    walkingSteadiness = walkingSteadiness.doubleValue,
                    onWalkingSteadinessChange = { walkingSteadiness.doubleValue = it }
                )
            }
        }
        activityToDelete.value?.let { activity ->
            if(showDeleteDialog.value){
                DeleteActivityDialog(
                    onDismiss = { showDeleteDialog.value = false },
                    onDeleteActivity = { id ->
                        deleteActivityViewModel.deleteActivity(id)
                        showDeleteDialog.value = false
                        toHome()
                    },
                    id = activity.id
                )
            }
        }
}
}
