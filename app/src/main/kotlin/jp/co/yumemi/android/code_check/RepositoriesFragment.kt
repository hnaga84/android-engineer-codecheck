/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import io.ktor.client.features.*
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoriesBinding
import jp.co.yumemi.android.code_check.model.Repository

class RepositoriesFragment : Fragment(R.layout.fragment_repositories) {

    private var binding: FragmentRepositoriesBinding? = null
    private val _binding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoriesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = RepositoriesViewModel()

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: Repository) {
                gotoRepositoryFragment(item)
            }
        })
        viewModel.repositories.observe(viewLifecycleOwner, Observer { it ->
            it?.let { adapter.submitList(it) }
        })

        _binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    val imm: InputMethodManager? =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)

                    val inputText = editText.text.toString()
                    if (inputText.isNotEmpty()) {
                        try {
                            viewModel.searchRepositories(inputText)
                        } catch (e: ClientRequestException) {
                            AlertDialog.Builder(requireContext()) // FragmentではActivityを取得して生成
                                .setTitle("通信エラー")
                                .setMessage("時間をおいてから再度お試しください")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        } catch (e: Exception) {
                            AlertDialog.Builder(requireContext()) // FragmentではActivityを取得して生成
                                .setTitle("通信エラー")
                                .setMessage("接続に失敗しました")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        _binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }

    fun gotoRepositoryFragment(repository: Repository) {
        val action = RepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repository = repository)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

val diffUtil = object : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }

}

class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Repository, CustomAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(item: Repository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = getItem(position)

        val repositoryNameView = holder.itemView.findViewById<View>(R.id.repositoryNameView)
        if (repositoryNameView is TextView) repositoryNameView.text = repository.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(repository)
        }
    }
}
