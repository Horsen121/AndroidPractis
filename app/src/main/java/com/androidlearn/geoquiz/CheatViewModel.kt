package com.androidlearn.geoquiz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CheatViewModel: ViewModel() {
    var answerIsTrue = false
    var isCheater = false
    var answer = mutableStateOf("")
}