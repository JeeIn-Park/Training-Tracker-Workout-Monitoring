package com.example.trainingtracker.ui.setting

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.trainingtracker.R
import com.example.trainingtracker.databinding.FragmentSettingPrivacyPolicyBinding


class PrivacyPolicyFragment : Fragment() {

    private var _binding: FragmentSettingPrivacyPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingPrivacyPolicyBinding.inflate(inflater, container, false)

        val textView: TextView = binding.SettingPrivacyPolicyPrivacyPolicyTextView
        textView.text = Html.fromHtml(getString(R.string.terms_and_conditions_html), Html.FROM_HTML_MODE_LEGACY);


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid leaking the binding
    }
}
