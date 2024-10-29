package com.example.rickmortyapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickmortyapp.R
import com.example.rickmortyapp.models.Result
import com.example.rickmortyapp.services.CharacterService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun CharacterDetailScreen(id: Int, innerPaddingValues: PaddingValues) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var character by remember { mutableStateOf<Result?>(null) }

    LaunchedEffect(key1 = id) {
        scope.launch {
            val BASE_URL = "https://rickandmortyapi.com/api/"
            val characterService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CharacterService::class.java)

            isLoading = true
            character = characterService.getCharacterById(id)
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        character?.let { characterDetail ->
            Column(
                modifier = Modifier
                    .padding(innerPaddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AsyncImage(
                            model = characterDetail.image,
                            contentDescription = characterDetail.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(345.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = characterDetail.name,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFA500)
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            when (characterDetail.status) {
                                "Alive" -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.greenpoint),
                                        contentDescription = "Alive",
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                                else -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.advertencia2),
                                        contentDescription = "Not Alive",
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${characterDetail.status} - ${characterDetail.species} - ${characterDetail.gender}",
                                style = TextStyle(fontSize = 18.sp, color = Color.Black),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Text(
                            text = "Last known location:",
                            style = TextStyle(fontSize = 19.sp, color = Color(0xFFFFA500), fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = characterDetail.location.name,
                            style = TextStyle(fontSize = 18.sp, color = Color.Black),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "First seen in:",
                            style = TextStyle(fontSize = 19.sp, color = Color(0xFFFFA500), fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = characterDetail.origin.name,
                            style = TextStyle(fontSize = 18.sp, color= Color.Black),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Episodes: ${characterDetail.episode.size}",
                            style = TextStyle(fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}