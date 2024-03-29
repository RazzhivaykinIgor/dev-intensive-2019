package ru.skillbranch.devintensive.models

import android.util.Log

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {
    fun askQuestion(): String = when (question){
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question



    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>>{
        return if(!validation(answer)){
            Log.d("M_Bender", answer)
            "${question.validateError()}\n${question.question}" to status.color
        } else {
            when(question){
                Question.IDLE -> question.question to status.color
                else -> "${checkAnswer(answer)}\n${question.question}" to status.color
            }
        }
    }

    private fun checkAnswer(answer: String): String {
        return if (question.answers.contains(answer)) {
            question = question.nextQuestion()
            "Отлично - ты справился"
        }
        else {
            if (status == Status.CRITICAL){
                resetStates()
                "Это неправильный ответ. Давай все по новой"
            }
            else{
                status = status.nextStatus()
                "Это неправильный ответ"
            }
        }
    }

    private fun resetStates() {
        status = Status.NORMAL
        question = Question.NAME
    }

    private fun validation(answer: String) : Boolean{
        return when(question){
            Question.NAME -> answer.trim().firstOrNull()?.isUpperCase() ?: false
            Question.PROFESSION -> answer.trim().firstOrNull()?.isLowerCase() ?: false
            Question.MATERIAL -> answer.trim().contains(Regex("\\d")).not()
            Question.BDAY -> answer.trim().contains(Regex("^[0-9]*$"))
            Question.SERIAL -> answer.trim().contains(Regex("^[0-9]{7}$"))
            Question.IDLE -> true
        }
    }


    enum class Status(val color : Triple<Int, Int, Int>){
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus() : Status{
            return if(this.ordinal < values().lastIndex){
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }
    enum class Question(val question: String, val answers: List<String>){
        NAME("Как меня зовут?", listOf("бендер", "bender")){
            override fun validateError(): String = "Имя должно начинаться с заглавной буквы"
            override fun nextQuestion(): Question = PROFESSION

        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")){
            override fun validateError(): String = "Профессия должна начинаться со строчной буквы"
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")){
            override fun validateError(): String = "Материал не должен содержать цифр"
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")){
            override fun validateError(): String = "Год моего рождения должен содержать только цифры"
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun validateError(): String = "Серийный номер содержит только цифры, и их 7"
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()){
            override fun validateError(): String = ""
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
        abstract fun validateError() : String
    }
}