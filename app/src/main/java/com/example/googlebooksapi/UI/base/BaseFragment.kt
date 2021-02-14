package com.example.googlebooksapi.UI.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.googlebooksapi.extensions.showErrorSnackbar
import com.example.googlebooksapi.extensions.showInfoSnackbar

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected fun showSnack(message: MessageEvent) {
        message.consume()?.let {
            val host = (requireActivity() as SnackbarHost).provideSnackbarLayout()
            when (message.type) {
                MessageEvent.Type.INFO -> host.showInfoSnackbar(getString(it))
                MessageEvent.Type.ERROR -> host.showErrorSnackbar(getString(it))
            }
        }
    }

    protected fun navigateTo(@IdRes destination: Int, bundle: Bundle? = null) =
        findNavController().navigate(destination, bundle)

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun Fragment.requireAppContext(): Context = requireContext().applicationContext
}