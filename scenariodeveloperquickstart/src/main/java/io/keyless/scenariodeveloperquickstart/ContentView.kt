package io.keyless.scenariodeveloperquickstart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.keyless.scenariodeveloperquickstart.ui.theme.DeveloperQuickstartTheme

@Composable
fun ContentView(modifier: Modifier = Modifier, contentViewModel: ContentViewModel = viewModel()) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val indicatorsState by contentViewModel.uiIndicatorState.collectAsState()
        val buttonsState by contentViewModel.uiButtonState.collectAsState()


        ButtonStatus(
            onClick = contentViewModel::setup, enabled = buttonsState.setup, text =
            "Setup", apiState = indicatorsState.setup
        )

        ButtonStatus(
            onClick = contentViewModel::enroll, enabled = buttonsState.enroll, text =
            "Enroll", apiState = indicatorsState.enroll
        )

        ButtonStatus(
            onClick = contentViewModel::authenticate, enabled = buttonsState.auth, text =
            "Authenticate", apiState = indicatorsState.auth
        )

        ButtonStatus(
            onClick = contentViewModel::deEnroll, enabled = buttonsState.deEnroll, text =
            "DeEnroll", apiState = indicatorsState.deEnroll
        )

        Button(onClick = contentViewModel::reset) {
            Text("Reset")
        }
    }
}

@Composable
fun ButtonStatus(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean,
    apiState: ApiState
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onClick, enabled = enabled, modifier = modifier.padding(end = 8.dp)) {
            Text(text = text)
        }
        when (apiState) {
            is ApiState.Idle -> Unit
            is ApiState.Loading -> CircularProgressIndicator()
            is ApiState.Success ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Green, CircleShape)
                )

            ApiState.Error -> Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Red, CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentViewPreview() {
    DeveloperQuickstartTheme {
        ContentView()
    }
}
