package com.es.evampsocial.MainView.Friends.FriendsRequestsfregments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.es.evampsocial.AppConstants
import com.es.evampsocial.MainView.MainActivity
import com.es.evampsocial.R
import com.es.evampsocial.RecyclerView.PersonItem
import com.es.evampsocial.Util.FireStoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_sent_requests.*

class SentRequests : Fragment() {

    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val UID = currentUser!!.uid

    val documentReference = db.collection("Users").document(UID)
            .collection("SentRequests")

    lateinit var userListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    lateinit var peopleSection: Section


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sent_requests, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userListenerRegistration = FireStoreUtil.sentFriendrequestlistener(requireContext(), this::updateRecyclerView)
    }

    override fun onDestroy() {
        super.onDestroy()
        FireStoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {
        fun init() {
           //Toast.makeText(context, "Recyclerview called", Toast.LENGTH_SHORT).show()
            recyclerview_sentRequests.apply {
                layoutManager = LinearLayoutManager(context)
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
        val pictureDailog = AlertDialog.Builder(requireContext())
        pictureDailog.setTitle("Do you want to cancel request?")
        val pictureDialogItems = arrayOf("YES", "NO")
        pictureDailog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> DeleteRequest(item as Item)
                1 -> sendtoMainActivity()
            }
        }

        pictureDailog.show()
        //Toast.makeText(context, "item id=" + item, Toast.LENGTH_LONG).show()
    }

    private fun DeleteRequest(item: Item) {


        if (item is PersonItem) {
           val item_item= AppConstants.USER_ID to item.userId
          Toast.makeText(context, "Item = "+ item_item, Toast.LENGTH_LONG).show()
           /* documentReference.document(item.toString()).delete()
                    .addOnSuccessListener { Log.d("SentRequest", "Item is deleted") }
                    .addOnFailureListener { Log.d("SentRequest", "Item is not deleted") }
            Toast.makeText(context, "delete functionallity is yet to be added", Toast.LENGTH_LONG).show()*/
        }
    }

    private fun sendtoMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }
}


