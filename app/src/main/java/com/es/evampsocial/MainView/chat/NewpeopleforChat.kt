package com.es.evampsocial.MainView.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.es.evampsocial.AppConstants
import com.es.evampsocial.R
import com.es.evampsocial.RecyclerView.PersonItem
import com.es.evampsocial.Util.FireStoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_newpeoplefor_chat.*
import kotlinx.android.synthetic.main.item_person.*
import org.jetbrains.anko.startActivity

class NewpeopleforChat : AppCompatActivity() {

    lateinit var userListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
   lateinit var peopleSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newpeoplefor_chat)
        toolbaar.setTitle("New Chat")

        group_chat.setOnClickListener {
            Toast.makeText(this,"Select atleast two persons",Toast.LENGTH_SHORT).show()
            //showhide()
        }

        userListenerRegistration = FireStoreUtil.addUersListener(this, this::updateRecyclerView)
    }

    private fun showhide() {
        select_person.visibility=View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        FireStoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {
        fun init() {
            recycler_view_people.apply {
                layoutManager = LinearLayoutManager(this@NewpeopleforChat)
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
        if (item is PersonItem) {
            startActivity<ChatActivity>(
                    AppConstants.USER_NAME to item.person.fName,
                    AppConstants.USER_ID to item.userId
            )
        }
    }

}
