package com.example.trainingtracker

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object CardStorage {
    private const val FILE_NAME = "exercise_cards.dat"

    fun saveCards(context: Context, cards : List<ExerciseCard>) {
        try {
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(cards)
            }
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

    fun loadCards(context: Context) : List<ExerciseCard> {
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
        val currentCards = loadCards(context).toMutableList()
        currentCards.add(card)
        saveCards(context, currentCards)
    }

    fun removeCard(context: Context, card: ExerciseCard) {
        val currentCards = loadCards(context).toMutableList()
        currentCards.remove(card)
        saveCards(context, currentCards)
    }

    //TODO : study how edit card works
    fun editCard(context: Context, oldCard: ExerciseCard, newCard: ExerciseCard) {
        val currentCards = loadCards(context).toMutableList()
        val index = currentCards.indexOfFirst { it == oldCard }
        if (index != -1) {
            currentCards[index] = newCard
            saveCards(context, currentCards)
        }
    }


}