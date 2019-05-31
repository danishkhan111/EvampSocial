package com.es.evampsocial.Search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.es.evampsocial.AppConstants
import com.es.evampsocial.MainView.chat.ChatActivity
import com.es.evampsocial.R
import com.es.evampsocial.RecyclerView.PersonItem
import com.es.evampsocial.Util.FireStoreUtil
import com.es.evampsocial.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_search_user.*
import org.jetbrains.anko.startActivity


class SearchUser : AppCompatActivity() {

    lateinit var userListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    lateinit var peopleSection: Section

    private lateinit var current_User: User


    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val UID = currentUser!!.uid

    val documentReference = db.collection("Users")
            .document(UID).collection("SentRequests")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        FireStoreUtil.getCurrentUser {
            current_User = it
        }

        toolbar_search.setTitle("Search User")

        userListenerRegistration = FireStoreUtil.addUersListener(this, this::updateRecyclerView)
    }

    override fun onDestroy() {
        super.onDestroy()
        FireStoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {
        fun init() {
            recyclerviewpeople.apply {
                layoutManager = LinearLayoutManager(this@SearchUser)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)


                    setOnItemClickListener(onItemClick)

                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)


        if (shouldInitRecyclerView)
            init()
        else
            updateItems()
    }

    private val onItemClick = OnItemClickListener { item, view ->

        val pictureDailog = AlertDialog.Builder(this)
        pictureDailog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Add Friend", "Send Message")
        pictureDailog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> AddFriend(item as Item)
                1 -> SendMessage(item as Item)
            }
        }
        //Toast.makeText(this, "ItemID= " + item, Toast.LENGTH_SHORT).show()
        pictureDailog.show()
    }

    private fun AddFriend(item: Item) {
        if (item is PersonItem) {
            documentReference.add(item)
            AppConstants.USER_ID to item.userId

            receivedRequests(item.userId)

        }

    }

    private fun receivedRequests(userId: String) {

        Toast.makeText(this, "otheruser id=" + userId, Toast.LENGTH_LONG).show()
        db.collection("Users").document(userId)
                .collection("ReceivedFriendRequests").add(current_User)
    }


    private fun SendMessage(item: Item) {
        if (item is PersonItem) {
            startActivity<ChatActivity>(
                    AppConstants.USER_NAME to item.person.fName,
                    AppConstants.USER_ID to item.userId
            )
        }
    }

}