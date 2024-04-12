package com.example.trainingtracker.ui.exerciseCard

import android.content.Context
import com.example.trainingtracker.ui.exerciseLog.ExerciseLog
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.muscles.MuscleStatus
import com.example.trainingtracker.ui.muscles.MuscleStorage
import com.example.trainingtracker.ui.tag.Tag
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

object CardStorage {
    private const val FILE_NAME = "exercise_cards.dat"

    private fun saveCards(context: Context, cards : List<ExerciseCard>) {
        try {
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(cards)
            }
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

    private fun loadCards(context: Context) : List<ExerciseCard> {
        try {
            ObjectInputStream(context.openFileInput(FILE_NAME)).use {
                return it.readObject() as? List<ExerciseCard> ?: emptyList()
            }
        } catch (e : FileNotFoundException) {
            return emptyList()
        } catch (e : IOException) {
            e.printStackTrace()
            return emptyList()
        } catch (e : ClassNotFoundException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun addCard(context: Context, card: ExerciseCard) {
        val currentCards = loadCards(context)
        val updatedCards = listOf(card) + currentCards
        saveCards(context, updatedCards)
    }

    fun removeCard(context: Context, card: ExerciseCard) {
        val currentCards = loadCards(context).toMutableList()
        currentCards.remove(card)
        saveCards(context, currentCards)
    }

    //TODO : remove all the log data

    //TODO : study how edit card works
    fun editCard(context: Context, oldCard: ExerciseCard, newCard: ExerciseCard) {
        val currentCards = loadCards(context).toMutableList()
        val index = currentCards.indexOfFirst { it == oldCard }
        if (index != -1) {
            currentCards[index] = newCard
            saveCards(context, currentCards)
        }
    }

    fun updateCard(context: Context, log: ExerciseLog) {
        val card = getCard(context, log.exerciseCard)
        editCard(
            context,
            card,
            ExerciseCardFactory.updateExerciseCard(card, log))
        // TODO : muscle factory
        MuscleStatus.updateMuscle(context, card)
    }

    fun isIdInUse(context: Context, id: UUID): Boolean {
        val currentCards = loadCards(context)
        return currentCards.any { it.id == id }
    }

    fun getCard(context: Context, id: UUID): ExerciseCard{
        val cards  = loadCards(context)
        val index = cards.indexOfFirst { it.id == id }
        if (index != -1) {
            return cards[index]
        }
        return ExerciseCardFactory.createExerciseCard(context, "")
    }

    fun getSelectedCard(context: Context, tags: List<Tag>) : List<ExerciseCard> {
        val cards : MutableList<ExerciseCard> = loadCards(context).toMutableList()
        if (tags.isEmpty()) {
            return cards
        } else {
            val updateCards : MutableList<ExerciseCard> = cards.toMutableList()
            for (card in cards) {
                var selected = false
                for (tag in tags) {
                    if (card.tag.contains(tag)) {
                        selected = true
                    }
                }
                if (!selected) {
                    updateCards.remove(card)
                }
            }
            return updateCards
        }
    }


}