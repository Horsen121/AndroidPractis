package com.androidlearn.geoquiz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class QuizViewModel: ViewModel() {
    private val questions = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var currentQuestion = mutableStateOf(questions[0])
    private val res = List(questionsSize) { false }.toMutableList()
    var enabledButtons = mutableStateOf(true)

    val currentQuestionAnswer: Boolean
        get() = currentQuestion.value.answer
    val currentQuestionText: Int
        get() = currentQuestion.value.textRes
    val questionsSize: Int
        get() = questions.size
    val results: List<Boolean>
        get() = res.toList()
    fun moveToPrevious(): Boolean {
        currentQuestion.value = if (currentIndex > 0) {
            currentIndex -= 1
            questions[currentIndex]
        } else currentQuestion.value
        return currentIndex != 0
    }
    fun moveToNext(): Boolean {
        currentQuestion.value = if (currentIndex < questionsSize-1) {
            currentIndex += 1
            questions[currentIndex]
        } else {
            currentIndex = questionsSize
            currentQuestion.value
        }
        return currentIndex != questionsSize
    }
    fun setRes(answer: Boolean) {
        res[currentIndex] = answer
    }
    fun question(index: Int): Boolean = questions[index].answer
}