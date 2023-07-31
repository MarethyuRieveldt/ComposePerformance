package com.example.composeperformance

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.absoluteValue

@Composable
fun ComposePerformanceScreen1() {
    Logger.d(
        message = "[0]Recomposing entire Screen",
        filter = LogFilter.Recomposition
    )
    //not recompose with scrolling
    val items = remember {
        Logger.d(
            message = "[1]Recreating item list",
            filter = LogFilter.ReAllocation
        )
        val random = Random(10)
        IntRange(0, 100).map {
            val randomNumber = random.nextInt().absoluteValue % 200
            Item(
                desc = "[$randomNumber] Item with index = $it",
                id = randomNumber
            )
        }
    }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val scrollProgress = remember(scrollState.value, scrollState.maxValue) {
        Logger.d(
            message = "[2]Recalculating scroll progress",
            filter = LogFilter.ReAllocation
        )
        scrollState.value / (scrollState.maxValue * 1f)
    }

    //is not recomposed with scrolling
    val showScrollToTopButton = remember {
        Logger.d(
            message = "[3]Recalculating showScrollToTopButton",
            filter = LogFilter.ReAllocation
        )
        scrollProgress > .5
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ItemList(
            items = items,
            scrollState = scrollState,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ScrollPositionIndicator(progress = scrollProgress)
        Box(
            modifier = Modifier
                .height(64.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            ScrollToTopButton(showScrollToTopButton) {
                scope.launch {
                    scrollState.scrollTo(0)
                }
            }
        }
    }
}

@Composable
private fun ItemList(
    modifier: Modifier = Modifier,
    items: List<Item>,
    scrollState: ScrollState = rememberScrollState()
) {
    Logger.d(
        message = "[4]Recomposing ItemList",
        filter = LogFilter.Recomposition
    )
    Column(
        modifier = modifier
            .verticalScroll(scrollState),
    ) {
        Logger.d(
            message = "[5]Sorting item list",
            filter = LogFilter.ReAllocation
        )
        for (item in items.sorted()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.desc)
            }
        }
    }
}

@Composable
private fun ScrollPositionIndicator(
    modifier: Modifier = Modifier,
    progress: Float
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color.Yellow)
    ) {
        Logger.d(
            message = "[6]Recomposing ScrollPositionIndicator",
            filter = LogFilter.Recomposition
        )

        //is not recomposed with scrolling
        //progressWidth is not calculated again and again
        val progressWidth = remember {
            Logger.d(
                message = "[7]Recalculating progressWidth",
                filter = LogFilter.ReAllocation
            )
            maxWidth - (8.dp)
        }
        Logger.d(
            message = "[8]Recalculating item offset",
            filter = LogFilter.ReAllocation
        )
        val xOffset = with(LocalDensity.current) {
            ((progressWidth - 16.dp).toPx() * progress).toDp() + 4.dp
        }
        Box(
            modifier = Modifier
                .height(1.dp)
                .width(progressWidth)
                .background(Color.Gray)
                .align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .offset(xOffset, 0.dp)
                .size(16.dp)
                .align(Alignment.CenterStart)
                .background(Color.Red)
        )
    }
}

@Composable
private fun ScrollToTopButton(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (!isVisible) return
    Logger.d(
        message = "[9]Recomposing ScrollToTopButton",
        filter = LogFilter.Recomposition
    )
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = "Scroll To Top")
    }
}