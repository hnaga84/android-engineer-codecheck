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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoriesBinding
import jp.co.yumemi.android.code_check.model.Repository
import jp.co.yumemi.android.code_check.view_model.RepositoriesViewModel

class RepositoriesFragment : Fragment() {

    val viewModel by viewModels<RepositoriesViewModel>()

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

        val adapter = RepositoryListAdapter(object : RepositoryListAdapter.OnItemClickListener {
            override fun itemClick(item: Repository) {
                gotoRepositoryFragment(item)
            }
        })

        viewModel.repositories.observe(viewLifecycleOwner) { it ->
            it?.let { adapter.submitList(it) }
        }
        viewModel.searchError.observe(viewLifecycleOwner) {
            it?.let {
                AlertDialog.Builder(requireContext()) // FragmentではActivityを取得して生成
                    .setTitle(viewModel.alertTitle)
                    .setMessage(viewModel.alertMessage)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        setDivider()
        _binding.recyclerView.adapter = adapter
        _binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    viewModel.search(inputText = editText.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun gotoRepositoryFragment(repository: Repository) {
        val action = RepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repository = repository)
        findNavController().navigate(action)
    }

    private fun setDivider() {
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)

        _binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
        }
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

class RepositoryListAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Repository, RepositoryListAdapter.ViewHolder>(diffUtil) {

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
