package vk.cheysoff.keychain.presentation.screens.passwordsscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import vk.cheysoff.keychain.R
import vk.cheysoff.keychain.domain.model.PasswordModel


@Composable
fun ShowPasswordCard(passwordModel: PasswordModel, onClick: (id: Long?) -> Unit, imageLoader: ImageLoader) {
    Row(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick.invoke(passwordModel.id) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Coil.setImageLoader(imageLoader)

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=${passwordModel.websiteUrl}&size=128")
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                placeholder = painterResource(id = R.drawable.website_placeholder_icon),
                error = painterResource(id = R.drawable.website_placeholder_icon)
            )
        }
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "website: ${passwordModel.websiteUrl}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "login: ${passwordModel.login}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "password: ${hidePasswordString(passwordModel.password)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}

//@Composable
//fun AsyncImage(
//    imageUrl: String,
//    contentDescription: String?,
//    modifier: Modifier = Modifier,
//    placeholder: Painter? = null,
//    error: Painter? = null
//) {
//    val painter = rememberImagePainter(
//        data = imageUrl,
//        builder = {
//            crossfade(true) // Enable crossfade animation
//        }
//    )
//
//    // If a placeholder is provided, display it until the image loads
//    CoilImage(
//        painter = painter,
//        contentDescription = contentDescription,
//        modifier = modifier.placeholder(
//            visible = placeholder != null,
//            highlight = Placeholder.Highlight.shimmer()
//        ),
//        error = {
//            // If an error occurs, display the error placeholder or a default placeholder
//            placeholder?.let { painter(it) } ?: error?.let { painter(it) }
//        }
//    )
//}

private fun hidePasswordString(password: String): String {
    return "\u25CF".repeat(password.length)
}