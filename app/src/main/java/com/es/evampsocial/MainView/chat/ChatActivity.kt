package com.es.evampsocial.MainView.chat

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.es.evampsocial.AppConstants
import com.es.evampsocial.MainView.MainActivity
import com.es.evampsocial.R
import com.es.evampsocial.Util.FireStoreUtil
import com.es.evampsocial.Util.StorageUtil
import com.es.evampsocial.model.ImageMessage
import com.es.evampsocial.model.TextMessage
import com.es.evampsocial.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

private const val RC_SELECT_IMAGE = 2

class ChatActivity : AppCompatActivity() {

    lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldinitRecyclerView = true
    lateinit var messageSection: Section

    private lateinit var currentUser: User
    private lateinit var otherUserId: String
    private lateinit var currentChannelId: String


    private var mFilename: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mFilename = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        chat_tool_bar.setTitle(intent.getStringExtra(AppConstants.USER_NAME))
        FireStoreUtil.getCurrentUser {
            currentUser = it
        }

        chat_tool_bar.setOnClickListener {
            val intent = Intent(this@ChatActivity, MainActivity::class.java)
            startActivity(intent)
        }
        otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FireStoreUtil.getorCreateChatChannel(otherUserId) { channelId ->
            currentChannelId = channelId

            messagesListenerRegistration =
                    FireStoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)
                imageview_send.setOnClickListener {
                val messagetoSend =
                        TextMessage(edittext_yourmessage.text.toString(), Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid, otherUserId, currentUser.fName)
                edittext_yourmessage.setText("")
                FireStoreUtil.sendMessage(messagetoSend, channelId)

            }
            fab_send_image.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "video/*"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image/Video"), RC_SELECT_IMAGE)
            }

            sendvoice.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> startRecording()

                        MotionEvent.ACTION_UP -> stopRecording()
                    }

                    return v?.onTouchEvent(event) ?: true
                }
            })
        }
    }

    private fun startRecording()
    {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(mFilename)
        try {
            mediaRecorder?.prepare()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        mediaRecorder?.start()
        state = true
        Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
    }

    private fun stopRecording()
    {
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
        mediaRecorder = null
        Toast.makeText(this,"Recording Stopped",Toast.LENGTH_SHORT).show()
        StorageUtil.uploadAudio(mFilename)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                val messageToSend =
                        ImageMessage(imagePath, Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid,
                                otherUserId, currentUser.fName)
                FireStoreUtil.sendMessage(messageToSend, currentChannelId)
            }

        }
    }

    fun updateRecyclerView(messages: List<Item>)
    {
        fun init() {
            recyclerview_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messageSection = Section(messages)
                    this.add(messageSection)

                    setOnItemClickListener(onItemClick)

                }
            }
            shouldinitRecyclerView = false
        }

        fun updateItems() = messageSection.update(messages)
        if (shouldinitRecyclerView)
            init()
        else
            updateItems()
        recyclerview_messages.scrollToPosition(recyclerview_messages.adapter!!.itemCount - 1)
    }

    private val onItemClick = OnItemClickListener { item, view ->

        delete_btn.visibility = View.VISIBLE

        delete_btn.setOnClickListener {

            FireStoreUtil.deletemessage(item as Item, currentChannelId) { channelId ->
                currentChannelId = channelId


            }
        }

    }
}
