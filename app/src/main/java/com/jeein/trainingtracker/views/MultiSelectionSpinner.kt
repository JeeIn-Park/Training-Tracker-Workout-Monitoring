package com.jeein.trainingtracker.views

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import java.util.Arrays

class MultiSelectionSpinner : AppCompatSpinner, DialogInterface.OnMultiChoiceClickListener {
    private var _items: Array<String>? = null
    private var mSelection: BooleanArray? = null
    private val simpleAdapter: ArrayAdapter<String>

    constructor(context: Context) : super(context) {
        simpleAdapter = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            android.R.id.text1
        ) {
            override fun getView(
                position: Int,
                convertView: android.view.View?,
                parent: android.view.ViewGroup
            ): android.view.View {
                val view = super.getView(position, convertView, parent) as TextView
                view.textSize = 12f // Adjust text size for the spinner view
                return view
            }
        }
        super.setAdapter(simpleAdapter)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        simpleAdapter = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            android.R.id.text1
        ) {
            override fun getView(
                position: Int,
                convertView: android.view.View?,
                parent: android.view.ViewGroup
            ): android.view.View {
                val view = super.getView(position, convertView, parent) as TextView
                view.textSize = 12f // Adjust text size for the spinner view
                return view
            }
        }
        super.setAdapter(simpleAdapter)
    }


    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        mSelection?.let {
            if (which < it.size) {
                it[which] = isChecked
                simpleAdapter.clear()
                simpleAdapter.add(buildSelectedItemString())
            } else {
                throw IllegalArgumentException("Argument 'which' is out of bounds.")
            }
        }
    }

    override fun performClick(): Boolean {
        AlertDialog.Builder(context).apply {
            _items?.let { items ->
                setMultiChoiceItems(items, mSelection, this@MultiSelectionSpinner)
                show()
            }
        }
        return true
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException("setAdapter is not supported by MultiSelectSpinner.")
    }

    fun setItems(items: Array<String>) {
        _items = items
        mSelection = BooleanArray(items.size)
        simpleAdapter.clear()
        simpleAdapter.add(items[0])
        Arrays.fill(mSelection!!, false)
    }

    fun setItems(items: List<String>) {
        _items = items.toTypedArray()
        mSelection = BooleanArray(_items!!.size)
        simpleAdapter.clear()
        simpleAdapter.add(_items!![0])
        Arrays.fill(mSelection!!, false)
    }

    fun setSelection(selection: Array<String>) {
        _items?.forEachIndexed { index, item ->
            mSelection!![index] = selection.contains(item)
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    fun setSelection(selection: List<String>) {
        Arrays.fill(mSelection!!, false)
        selection.forEach { sel ->
            val index = _items?.indexOf(sel) ?: -1
            if (index >= 0) mSelection!![index] = true
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    override fun setSelection(index: Int) {
        Arrays.fill(mSelection!!, false)
        if (index in _items!!.indices) {
            mSelection!![index] = true
        } else {
            throw IllegalArgumentException("Index $index is out of bounds.")
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    fun setSelection(selectedIndices: IntArray) {
        Arrays.fill(mSelection!!, false)
        selectedIndices.forEach { index ->
            if (index in _items!!.indices) {
                mSelection!![index] = true
            } else {
                throw IllegalArgumentException("Index $index is out of bounds.")
            }
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    fun getSelectedStrings(): List<String> {
        return _items?.withIndex()?.filter { mSelection!![it.index] }?.map { it.value } ?: listOf()
    }

    fun getSelectedIndices(): List<Int> {
        return _items?.indices?.filter { mSelection!![it] } ?: listOf()
    }

    private fun buildSelectedItemString(): String {
        return _items?.withIndex()?.filter { mSelection!![it.index] }
            ?.joinToString(", ") { it.value } ?: ""
    }

    fun getSelectedItemsAsString(): String {
        return buildSelectedItemString()
    }
}
