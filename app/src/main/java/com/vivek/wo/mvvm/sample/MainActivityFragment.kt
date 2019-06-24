package com.vivek.wo.mvvm.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vivek.wo.mvvm.sample.databinding.FragmentMainBinding
import com.vivek.wo.mvvm.sample.databinding.ListItemBinding

class MainActivityFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding

    lateinit var mainViewModel: MainViewModel

    var userTaskList: ArrayList<UserTask> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        fragmentMainBinding.viewmodel = mainViewModel

        setHasOptionsMenu(true)
        return fragmentMainBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        for (i in 1..5) {
            val userTask = UserTask("", "Title $i", "2019-06-2$i")
            userTaskList.add(userTask)
        }

        fragmentMainBinding.recycleView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        var listAdapter = ListAdapter(userTaskList)
        fragmentMainBinding.recycleView.layoutManager = LinearLayoutManager(context)
        fragmentMainBinding.recycleView.adapter = listAdapter
    }

    class ListAdapter(val userTaskList: List<UserTask>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(p0.context)
            var listItemBinding = ListItemBinding.inflate(layoutInflater, p0, false)
            return ViewHolder(listItemBinding)
        }

        override fun getItemCount(): Int = userTaskList.size

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            val userTask = userTaskList[p1]
            val mainItemViewModel = MainItemViewModel()
            mainItemViewModel.setUserTask(userTask)

            val binding = p0.binding

            binding.viewmodel = mainItemViewModel
        }

        class ViewHolder(var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}
