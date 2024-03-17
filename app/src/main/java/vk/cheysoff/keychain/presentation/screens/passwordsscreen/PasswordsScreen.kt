package vk.cheysoff.keychain.presentation.screens.passwordsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import vk.cheysoff.keychain.R
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.ShowErrorText
import vk.cheysoff.keychain.presentation.screens.passwordsscreen.components.CustomSearchBar
import vk.cheysoff.keychain.presentation.screens.passwordsscreen.components.ShowPasswordCard

@Composable
fun ShowPasswordsScreen(
    state: PasswordsScreenState,
    onIntentReceived: (PasswordsScreenIntent) -> Unit,
) {

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .respectCacheHeaders(false)
        .build()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
            ),
    ) {
        Column {
            CustomSearchBar(
                query = state.searchText,
                onQueryChange = { text ->
                    onIntentReceived(PasswordsScreenIntent.ChangeSearchBarTextIntent(text))
                },
                onSearch = { onIntentReceived(PasswordsScreenIntent.SearchIntent) },
                active = false,
                onActiveChange = { onIntentReceived(PasswordsScreenIntent.OnToggleSearchIntent) },
                placeholderText = "Search for password website urls",
                onClear = { onIntentReceived(PasswordsScreenIntent.ClearSearchBarIntent) }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp,
                    )
            ) {
                if (state.loading) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { passwordModel ->
                            ShowPasswordCard(
                                passwordModel = passwordModel,
                                onClick = { id ->
                                    onIntentReceived(PasswordsScreenIntent.OpenPasswordInfoIntent(id = id))
                                },
                                imageLoader = imageLoader
                            )
                        }
                        item { Spacer(modifier = Modifier.padding(100.dp)) }
                    }
                    state.error?.let { text ->
                        ShowErrorText(text)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                end = 12.dp,
                                start = 12.dp,
                                bottom = 12.dp
                            )
                            .align(Alignment.BottomEnd),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .size(80.dp),
                            onClick = {
                                onIntentReceived(
                                    PasswordsScreenIntent.OpenSettingsIntent
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.inverseSurface
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(0.55f),
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                        Button(
                            modifier = Modifier
                                .size(80.dp),
                            onClick = {
                                onIntentReceived(
                                    PasswordsScreenIntent.OpenPasswordInfoIntent(
                                        0
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.inverseSurface
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(0.55f),
                                painter = painterResource(id = R.drawable.plus_icon),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                    }

                }
            }
        }

    }


}