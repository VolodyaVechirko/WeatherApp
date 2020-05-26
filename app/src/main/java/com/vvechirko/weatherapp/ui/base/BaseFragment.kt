package com.vvechirko.weatherapp.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    interface Interaction {
        fun navigate(fragment: BaseFragment)
        fun back()
    }

    private var interaction: Interaction? = null

    abstract val layoutResId: Int

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Interaction) {
            interaction = context
        } else {
            throw IllegalStateException("$context should implements BaseFragment.Interaction")
        }
    }

    override fun onDetach() {
        super.onDetach()
        interaction = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    protected fun navigate(fragment: BaseFragment) {
        interaction?.navigate(fragment)
    }

    protected fun back() {
        interaction?.back()
    }

    protected fun showError(e: AppException) {
//        interaction?.showError(e)
    }

    protected fun showSnackbar(resId: Int) {
        view?.let {
            Snackbar.make(it, resId, Snackbar.LENGTH_SHORT).show()
        }
    }
}