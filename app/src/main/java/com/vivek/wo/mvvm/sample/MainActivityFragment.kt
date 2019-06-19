package com.vivek.wo.mvvm.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vivek.wo.mvvm.sample.databinding.FragmentMainBinding

class MainActivityFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding

    lateinit var mainViewModel: MainViewModel

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
    }
}
