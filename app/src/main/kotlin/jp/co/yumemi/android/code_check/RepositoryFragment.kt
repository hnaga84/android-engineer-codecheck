/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoryBinding

class RepositoryFragment : Fragment() {

    private val args: RepositoryFragmentArgs by navArgs()

    private var binding: FragmentRepositoryBinding? = null
    private val _binding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        val repository = args.repository
        
        _binding.ownerIconView.load(repository.ownerIconUrl);
        _binding.fullNameView.text = repository.name;
        _binding.languageView.text = getString(R.string.written_language, repository.language)
        _binding.starsView.text = getString(R.string.stars_count, repository.stargazersCount.toString())
        _binding.watchersView.text =getString(R.string.watchers_count, repository.watchersCount.toString())
        _binding.forksView.text = getString(R.string.forks_count, repository.forksCount.toString())
        _binding.openIssuesView.text = getString(R.string.open_issues_count, repository.openIssuesCount.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
