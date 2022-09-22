/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.content.Intent
import android.net.Uri
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

        _binding.apply {
            ownerIconView.load(repository.ownerIconUrl);
            fullNameView.text = repository.fullName;
            languageView.text = repository.language?.let { getString(R.string.written_language, it) }
            starsView.text = getString(R.string.stars_count, repository.stargazersCount.toString())
            watchersView.text =
                getString(R.string.watchers_count, repository.watchersCount.toString())
            forksView.text = getString(R.string.forks_count, repository.forksCount.toString())
            openIssuesView.text =
                getString(R.string.open_issues_count, repository.openIssuesCount.toString())
            openInBrowser.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.htmlUrl))
                startActivity(intent)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
