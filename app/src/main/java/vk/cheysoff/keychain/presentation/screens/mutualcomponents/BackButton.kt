package vk.cheysoff.keychain.presentation.screens.mutualcomponents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vk.cheysoff.keychain.presentation.screens.editpasswordscreen.EditPasswordScreenIntent

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit){
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "Go back",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
    }
}