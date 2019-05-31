package com.es.evampsocial.Util

import android.content.Context
import android.util.Log
import com.es.evampsocial.RecyclerView.*
import com.es.evampsocial.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

object FireStoreUtil {
    public val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }


    private val chatChannelIsCollectionref = db.collection("chatchannelis")
    private val friendChannelCollectionref = db.collection("FriendsChannel")


    val currentUser = FirebaseAuth.getInstance().currentUser
    val UId = currentUser!!.uid

    private val currentUserDocRef: DocumentReference
        get() = db.document("Users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null.")}")

    fun updateCurrentUser(name: String = "", lname: String = "", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()

        if (name.isNotBlank())
            userFieldMap.put("fName", name)
        if (lname.isNotBlank())
            userFieldMap.put("lName", lname)

        if (profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath
        db.collection("Users").document(UId).update(userFieldMap)

    }

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        "", null, mutableListOf())
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }
    }

    fun addUersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return db.collection("Users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "User Listener Error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                    }
                    onListen(items)
                }
    }

    fun sentFriendrequestlistener(context: Context, onListen: (List<Item>) -> Unit)
            : ListenerRegistration {
        return db.collection("Users")
                .document(UId).collection("SentRequests")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Sentrequest Listener Error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(sentRequestFriendItem(it.toObject(User::class.java)!!, it.id, context))
                    }
                    onListen(items)
                }
    }

    fun receivefriendrequestListener(context: Context, onListen: (List<Item>) -> Unit)
            : ListenerRegistration {
        return db.collection("Users").document(UId)
                .collection("ReceivedFriendRequests")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Received request Listener Error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(ReceivedrequestfriendItem(it.toObject(User::class.java)!!, it.id, context))
                    }
                    onListen(items)
                }

    }


    fun removeListener(registration: ListenerRegistration) = registration.remove()
    fun getorCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit) {
        currentUserDocRef.collection("engagedChatChannels")
                .document(otherUserId).get().addOnSuccessListener {
                    if (it.exists()) {
                        onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }
                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                    val newChannel = chatChannelIsCollectionref.document()
                    newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                    currentUserDocRef
                            .collection("engagedChatChannels")
                            .document(otherUserId)
                            .set(mapOf("channelId" to newChannel.id))

                    db.collection("Users").document(otherUserId)
                            .collection("engagedChatChannels")
                            .document(currentUserId)
                            .set(mapOf("channelId" to newChannel.id))
                    onComplete(newChannel.id)
                }
    }

    fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<Item>) -> Unit): ListenerRegistration {
        return chatChannelIsCollectionref.document(channelId).collection("messages")
                .orderBy("time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it["type"] == MessageType.TEXT)
                            items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                        else
                            items.add(ImageMessageItem(it.toObject(ImageMessage::class.java)!!, context))
                        return@forEach
                    }
                    onListen(items)
                }
    }

    fun sendMessage(message: Message, channelId: String) {
        chatChannelIsCollectionref.document(channelId)
                .collection("messages")
                .add(message)
    }

    fun deletemessage(item: Item, channelId: String, onComplete: (channelId: String) -> Unit) {
        val deleteItem = chatChannelIsCollectionref.document(channelId).collection("messages")
                .document("$item")
        deleteItem.delete()
    }

    /*  fun getorCreateFriend(otherUserId: String,
                            onComplete: (channelId: String) -> Unit) {
          currentUserDocRef.collection("Friendlist")
                  .document(otherUserId).get().addOnSuccessListener {
                      if (it.exists()) {
                          onComplete(it["channelId"] as String)
                          return@addOnSuccessListener
                      }
                      val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                      val newChannel = friendChannelCollectionref.document()
                      newChannel.set(FriendsChannel(mutableListOf(currentUserId, otherUserId)))

                      currentUserDocRef
                              .collection("Friendlist")
                              .document(otherUserId)
                              .set(mapOf("channelId" to newChannel.id))

                      db.collection("Users").document(otherUserId)
                              .collection("Friendlist")
                              .document(currentUserId)
                              .set(mapOf("channelId" to newChannel.id))
                      onComplete(newChannel.id)
                  }
      }*/


    /*fun addfriendListener(channelId: String, context: Context,
                          onListen: (List<Item>) -> Unit): ListenerRegistration {
        return friendChannelCollectionref.document(channelId)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Friends Listener Error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val items = mutableListOf<Item>()
                    querySnapshot!!.
                    *//* querySnapshot!!.documents.forEach {
                         if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                             items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                     }
                     onListen(items)*//*
                }

    }*/


}



