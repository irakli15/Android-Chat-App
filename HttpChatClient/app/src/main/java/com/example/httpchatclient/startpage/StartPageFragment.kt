package com.example.httpchatclient.startpage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.httpchatclient.R
import com.example.httpchatserver.database.user.User
import kotlinx.android.synthetic.main.fragment_start_page.*
import kotlinx.android.synthetic.main.fragment_start_page.view.*
import java.io.ByteArrayOutputStream
import java.io.IOException


class StartPageFragment : Fragment(), StartPageContract.View {
    private val presenter = StartPagePresenterImpl()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var profileImageStart: ImageView
    private var imageString = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_page, container, false)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        val savedUserName = sharedPref?.getString("userName", null)
        if (savedUserName != null) {
            presenter.loadUser(savedUserName, imageString, onUserLoad)
        }
        view.startButton.setOnClickListener {
            val userName = userNameFieldStart.text.toString()
            val whatIDo = whatIDoFieldStart.text.toString()
            if (userName.isEmpty()) {
                Toast.makeText(context, "Enter your nickname", Toast.LENGTH_SHORT).show()
            } else if (whatIDo.isEmpty()) {
                Toast.makeText(context, "Enter what you do", Toast.LENGTH_SHORT).show()
            } else {
                presenter.loadUser(userName, imageString, onUserLoad)
            }

        }

        profileImageStart = view.profileImageStart

        profileImageStart.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }


        return view
    }

    val GALLERY_REQUEST = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            GALLERY_REQUEST -> {
                val selectedImage: Uri? = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        selectedImage
                    )
                    profileImageStart.setImageBitmap(bitmap)

                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    imageString = Base64.encodeToString(byteArray, Base64.DEFAULT)

                } catch (e: IOException) {
                    Log.i("TAG", "Some exception $e")
                }
            }
        }
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