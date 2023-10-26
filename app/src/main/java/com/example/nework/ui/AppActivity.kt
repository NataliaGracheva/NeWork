package com.example.nework.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.example.nework.R
import com.example.nework.auth.AppAuth
import com.example.nework.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.activity_app) {
    @Inject
    lateinit var auth: AppAuth
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.data.observe(this) {
            invalidateOptionsMenu()
        }

//        requestNotificationsPermission()

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)

                menu.let {
                    it.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
                    it.setGroupVisible(R.id.authenticated, viewModel.authenticated)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.signin -> {
//                        findNavController(R.id.nav_host_fragment).navigate(R.id.signInFragment)
                        true
                    }
                    R.id.signup -> {
//                        findNavController(R.id.nav_host_fragment).navigate(R.id.signUpFragment)
                        true
                    }
                    R.id.signout -> {
                        auth.removeAuth()
//                        findNavController(R.id.nav_host_fragment).navigate(R.id.feedFragment)
                        true
                    }
                    else -> false
                }
        })
    }

//    private fun requestNotificationsPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//            return
//        }
//
//        val permission = Manifest.permission.POST_NOTIFICATIONS
//
//        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
//            return
//        }
//
//        requestPermissions(arrayOf(permission), 1)
//    }
}
