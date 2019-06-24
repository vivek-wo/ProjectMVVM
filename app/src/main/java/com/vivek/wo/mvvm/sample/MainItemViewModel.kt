package com.vivek.wo.mvvm.sample

import android.databinding.Observable
import android.databinding.ObservableField

class MainItemViewModel() {

    var icon = ObservableField<String>()

    var title = ObservableField<String>()

    var date = ObservableField<String>()

    var userTaskObservable = ObservableField<UserTask>()

    init {
        userTaskObservable.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val userTask = userTaskObservable.get()
                if (userTask != null) {
                    icon.set(userTask.icon)
                    title.set(userTask.title)
                    date.set(userTask.date)
                } else {
                    title.set("ERROR")
                }
            }

        })
    }

    fun getImageIcon(): String? {
        return icon.get()
    }

    fun setUserTask(userTask: UserTask) {
        userTaskObservable.set(userTask)
        val task0: UserTask? = userTaskObservable.get()
    }
}