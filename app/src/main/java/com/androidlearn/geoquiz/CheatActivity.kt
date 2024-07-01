package com.androidlearn.geoquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.androidlearn.geoquiz.ui.theme.GeoQuizTheme

private const val EXTRA_ANSWER_IS_TRUE = "com.androidlearn.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.androidlearn.geoquiz.answer_shown"


class CheatActivity : ComponentActivity() {
    private val cheatViewModel: CheatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cheatViewModel.answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        setContent {
            GeoQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CheatScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.isCheater)
        }
        setResult(RESULT_OK, data)
    }
    companion object {
        fun newIntent(context: Context, answerIsTrue: Boolean): Intent {
            return Intent(context, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }


    @Composable
    fun CheatScreen() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.warning_text))
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = cheatViewModel.answer.value)
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                cheatViewModel.answer.value = applicationContext.getString(
                    if (cheatViewModel.answerIsTrue) R.string.button_true else R.string.button_false)
                cheatViewModel.isCheater = true

                val data = Intent().apply {
                    putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.isCheater)
                }
                setResult(RESULT_OK, data)
            }) {
                Text(text = stringResource(id = R.string.button_show_answer))
            }
        }
    }
}