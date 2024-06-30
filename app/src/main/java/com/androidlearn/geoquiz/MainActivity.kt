package com.androidlearn.geoquiz

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.androidlearn.geoquiz.ui.theme.GeoQuizTheme

class MainActivity : ComponentActivity() {
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) ScreenPortrait()
                    else ScreenLandscape()
                }
            }
        }
    }

    @SuppressLint("ShowToast")
    @Composable
    fun ScreenPortrait() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = quizViewModel.currentQuestionText))
            Row {
                TextButton(
                    enabled = quizViewModel.enabledButtons.value,
                    onClick = {
                        checkAnswer(quizViewModel.currentQuestionAnswer, true)
                        quizViewModel.enabledButtons.value = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_true),
                        color = if (quizViewModel.enabledButtons.value) Color.Green else Color.Gray
                    )
                }
                TextButton(
                    enabled = quizViewModel.enabledButtons.value,
                    onClick = {
                        checkAnswer(quizViewModel.currentQuestionAnswer, false)
                        quizViewModel.enabledButtons.value = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_false),
                        color = if (quizViewModel.enabledButtons.value) Color.Red else Color.Gray
                    )
                }
            }
            Row {
                Button(onClick = {
                    if (quizViewModel.moveToPrevious()) quizViewModel.enabledButtons.value = true
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                    Text(text = stringResource(id = R.string.button_previous))
                }
                Button(onClick = {
                    quizViewModel.enabledButtons.value = true
                    if (!quizViewModel.moveToNext()) {
                        quizViewModel.enabledButtons.value = false
                        checkResults()
                    }
                }) {
                    Text(text = stringResource(id = R.string.button_next))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
    }

    @SuppressLint("ShowToast")
    @Composable
    fun ScreenLandscape() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(id = quizViewModel.currentQuestionText),
                modifier = Modifier.padding(top = 48.dp)
            )
            Row {
                TextButton(
                    enabled = quizViewModel.enabledButtons.value,
                    onClick = {
                        checkAnswer(quizViewModel.currentQuestionAnswer, true)
                        quizViewModel.enabledButtons.value = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_true),
                        color = if (quizViewModel.enabledButtons.value) Color.Green else Color.Gray
                    )
                }
                TextButton(
                    enabled = quizViewModel.enabledButtons.value,
                    onClick = {
                        checkAnswer(quizViewModel.currentQuestionAnswer, false)
                        quizViewModel.enabledButtons.value = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_false),
                        color = if (quizViewModel.enabledButtons.value) Color.Red else Color.Gray
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize(0.5f)
            ) {
                Button(onClick = {
                    if (quizViewModel.moveToPrevious()) quizViewModel.enabledButtons.value = true
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                    Text(text = stringResource(id = R.string.button_previous))
                }
                Button(onClick = {
                    quizViewModel.enabledButtons.value = true
                    if (!quizViewModel.moveToNext()) {
                        quizViewModel.enabledButtons.value = false
                        checkResults()
                    }
                }) {
                    Text(text = stringResource(id = R.string.button_next))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
    }

    private fun checkAnswer(answer: Boolean, userAnswer: Boolean) {
        quizViewModel.setRes(userAnswer)

        var messageResId = R.string.toast_incorrect
        if (userAnswer == answer) messageResId = R.string.toast_correct

        Toast.makeText(applicationContext, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun checkResults() {
        var result = 0
        val results = quizViewModel.results
        for (i in 0 until quizViewModel.questionsSize) {
            if (results[i] == quizViewModel.question(i)) result++
        }
        Toast.makeText(applicationContext, "$result / ${quizViewModel.questionsSize}", Toast.LENGTH_SHORT).show()
    }

}