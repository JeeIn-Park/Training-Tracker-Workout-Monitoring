package com.jeein.trainingtracker.ui.setting

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.databinding.FragmentSettingPrivacyPolicyBinding


class PrivacyPolicyFragment : Fragment() {

    private var _binding: FragmentSettingPrivacyPolicyBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingPrivacyPolicyBinding.inflate(inflater, container, false)

        val textView: TextView = binding.SettingPrivacyPolicyPrivacyPolicyTextView
        textView.text = Html.fromHtml(getString(R.string.terms_and_conditions_html), Html.FROM_HTML_MODE_LEGACY);


        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid leaking the binding
    }
}
