package com.example.httpchatclient.startpage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.httpchatclient.R
import com.example.httpchatserver.database.user.User
import kotlinx.android.synthetic.main.fragment_start_page.*
import kotlinx.android.synthetic.main.fragment_start_page.view.*

class StartPageFragment : Fragment(), StartPageContract.View {
    private val presenter = StartPagePresenterImpl()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_page, container, false)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        val savedUserName = sharedPref?.getString("userName", null)
        if (savedUserName != null) {
            presenter.loadUser(savedUserName, onUserLoad)
        }

        view.startButton.setOnClickListener {
            val userName = userNameFieldStart.text.toString()
            val whatIDo = whatIDoFieldStart.text.toString()
            if (userName.isEmpty()) {
                Toast.makeText(context, "Enter your nickname", Toast.LENGTH_SHORT).show()
            } else if (whatIDo.isEmpty()) {
                Toast.makeText(context, "Enter what you do", Toast.LENGTH_SHORT).show()
            } else {
                presenter.loadUser(userName, onUserLoad)
            }

        }
        return view
    }

    override fun showLoad() {
        TODO("Not yet implemented")
    }

    override fun hideLoad() {
        TODO("Not yet implemented")
    }

    private val onUserLoad: (User) -> Any = {
        with(sharedPref?.edit()) {
            this?.putString("userName", it.userName)
            this?.commit()
        }
        val args = Bundle()
        args.putParcelable("user", it)
        findNavController().navigate(R.id.action_startPageFragment_to_chatHistoryFragment, args)
    }

}