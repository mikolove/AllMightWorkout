package com.mikolove.core.presentation.designsystem.components


import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.ContinueWithGoogleIcon
import com.mikolove.core.presentation.designsystem.EyeIcon
import com.mikolove.core.presentation.designsystem.R


@Composable
fun AmwActionButton(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(15.dp),
                    strokeWidth = 1.5.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }else{
                Text(
                    text = text,
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium
                )

            }
        }
    }
}

@Composable
fun AmwOutlinedActionButton(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(15.dp),
                    strokeWidth = 1.5.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }else{
                Text(
                    text = text,
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium
                )

            }
        }
    }
}

@Composable
fun AmwActionButtonIcon(
    text: String,
    imageVector : ImageVector,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {

            Icon(
                imageVector = imageVector,
                contentDescription = null,
            )
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(15.dp),
                    strokeWidth = 1.5.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }else{
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium)

            }
        }
    }
}



@Preview
@Composable
private fun AmwActionButtonPreview() {
    AmwTheme {
        AmwActionButton(
            text = "Login",
            isLoading = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun AmwOutlinedActionButtonPreview() {
    AmwTheme {
        AmwOutlinedActionButton(
            text = "Login",
            isLoading = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun AmwButtonIconPreview() {
    AmwTheme {
        AmwActionButtonIcon(
            text = "Test preview text",
            imageVector = EyeIcon,
            isLoading = true,
            onClick = {}
        )
    }
}

