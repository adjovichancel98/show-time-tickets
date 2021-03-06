package com.example.showtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveChatAdmin extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton send;
    TextInputEditText input;
    RecyclerView list_of_messages;
    ChatAdapter adapter;
    ArrayList<ChatMessage> chatList = new ArrayList<>();
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat_admin);
        i = getIntent();
        send = findViewById(R.id.floatingActionButtonAdmin);
        input = findViewById(R.id.inputAdmin);
        list_of_messages =findViewById(R.id.list_of_messages_admin);
        displayChatMessages();
    }

    private void displayChatMessages() {
        constructRecyclerView();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                CollectionReference applicationsRef = rootRef.collection("chat");
                DocumentReference applicationIdRef = applicationsRef.document(i.getStringExtra("id"));
                applicationIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                chatList= new ArrayList((Collection) document.get(i.getStringExtra("id")));
                                Log.d("chat admin",chatList.toString());

                                Map<String,List<ChatMessage>> chat = new HashMap<>();
                                chatList.add(new ChatMessage(input.getText().toString(),"Admin"));
                                chat.put(i.getStringExtra("id"),chatList);
                                input.setText("");
                                db.collection("chat")
                                        .document(i.getStringExtra("id"))
                                        .set(chat)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("chat success", "chat sent");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("chat failure", "message not sent");
                                            }
                                        });
                                constructRecyclerView();
                            }
                            else{
                                chatList =  new ArrayList<>();
                                Map<Object,List<ChatMessage>> chat = new HashMap<>();
                                chatList.add(new ChatMessage(input.getText().toString(), "Admin"));
                                chat.put(i.getStringExtra("id"),chatList);
                                input.setText("");
                                db.collection("chat")
                                        .document(i.getStringExtra("id"))
                                        .set(chat)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("chat success", "chat sent");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("chat failure", "message not sent");
                                            }
                                        });
                                constructRecyclerView();
                            }
                        }
                    }
                });



            }
        });

    }

    private void constructRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_of_messages.setLayoutManager(layoutManager);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference applicationsRef = rootRef.collection("chat");
        DocumentReference applicationIdRef = applicationsRef.document(i.getStringExtra("id"));
        applicationIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        chatList= new ArrayList<ChatMessage>((Collection) document.get(i.getStringExtra("id")));

                        Log.d("recycler chat",chatList.toArray().toString());
                        adapter = new ChatAdapter(chatList);
                        list_of_messages.setAdapter(adapter);
                        list_of_messages.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    }
                }
            }
        });

    }

//    private void displayChatMessages() {
//
//        constructRecyclerView();
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//                CollectionReference applicationsRef = rootRef.collection("chats");
//                DocumentReference applicationIdRef = applicationsRef.document("Live Chat");
//                applicationIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            DocumentSnapshot document = task.getResult();
//                            if(document.exists()){
//                                chatList= new ArrayList((Collection) document.get("Live Session"));
//                                Log.d("chat admin",chatList.toString());
//
//                                Map<String,List<ChatMessage>> chat = new HashMap<>();
//                                chatList.add(new ChatMessage(input.getText().toString(),"Admin"));
//                                chat.put("Live Session",chatList);
//                                input.setText("");
//                                db.collection("chats")
//                                        .document("Live Chat")
//                                        .set(chat)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.d("chat success", "chat sent");
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.d("chat failure", "message not sent");
//                                            }
//                                        });
//                                constructRecyclerView();
//                            }
//                        }
//                    }
//                });
//
//
//
//            }
//        });
//
//    }
//
//    private void constructRecyclerView() {
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        list_of_messages.setLayoutManager(layoutManager);
//
//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        CollectionReference applicationsRef = rootRef.collection("chats");
//        DocumentReference applicationIdRef = applicationsRef.document("Live Chat");
//        applicationIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()){
//                        chatList= new ArrayList<ChatMessage>((Collection) document.get("Live Session"));
//
//                        Log.d("recycler chat",chatList.toArray().toString());
//                        adapter = new ChatAdapter(chatList);
//                        list_of_messages.setAdapter(adapter);
//                        list_of_messages.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
//                    }
//                }
//            }
//        });
//
//
//
//    }
}