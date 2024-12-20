package com.example.nobliviate;

import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Firebase_login {

    private static final String APP_NAME = "nobliviateLoginApp";
    // Unique app name for this project
    private FirebaseApp firebaseApp;

   /* public FirebaseApp create2() throws IOException {

/*
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();
                break;
            }
        }
*//*
        if (!FirebaseApp.getApps().isEmpty()) {
            firebaseApp = FirebaseApp.getInstance(APP_NAME);
        } else if(FirebaseApp.getApps().isEmpty()) {
            // Initialize FirebaseApp
            InputStream inputStream = getClass().getResourceAsStream("/nobliviate-user-firebase-adminsdk-vl29u-3d053e5a45.json");
            assert inputStream != null;

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            try{
                firebaseApp = FirebaseApp.initializeApp(options, APP_NAME);
            }catch (Exception e){
                System.out.println(e);
            }

            System.out.println("FirebaseApp initialized successfully.");
        }

        return firebaseApp;
    }
*/

    public FirebaseApp create2() throws IOException {

        try {

            firebaseApp = FirebaseApp.getInstance(APP_NAME);


        }catch (Exception e){

            InputStream inputStream = getClass().getResourceAsStream("/nobliviate-user-firebase-adminsdk-vl29u-3d053e5a45.json");
            assert inputStream != null;

            FirebaseOptions options =FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options , APP_NAME);
            System.out.println("ERROR!!!"+e);
        }

        return firebaseApp;
    }


    public void addClient(String name, String password)
            throws IOException, ExecutionException, InterruptedException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        Map<Object, Object> map = new HashMap<>();
        ApiFuture<WriteResult> apiFuture = db.collection(name).document(password).set(map);
        System.out.println("Added document " + apiFuture.get().getUpdateTime());

        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();
                break;
            }
        }
    }

    public boolean checkClient(String name, String password)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        DocumentReference documentReference = db.collection(name).document(password);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot documentSnapshot = (DocumentSnapshot) future.get();

        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();

            }
        }
        return documentSnapshot.exists();
    }



    public void addDocument(String category , String title)
            throws ExecutionException, InterruptedException, IOException {


        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        Map<String, Object> docData = new HashMap<>();
        docData.put(" " , " ");
// Add a new document (asynchronously) in collection "cities" with id "LA"
        ApiFuture<WriteResult> future = db.collection(category).document(title).set(docData);
// ...
// future.get() blocks on response
        System.out.println("Update time : " + future.get().getUpdateTime());
        // Continue with Firestore operations
    }


    public void addAux(String id , String password , String description,
                       String note , String name , String password_creator)
            throws IOException, ExecutionException, InterruptedException {


        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);

        Map<String , Object> map = new HashMap<>();
        map.put("description" , description);
        map.put("note" , note);

        ApiFuture<WriteResult> apiFuture = db.collection(name).document(password_creator)
                .collection(id).document(password).collection("AUX")
                .document("date").set(map);

        System.out.println("Added document " + apiFuture.get().getUpdateTime());

    }

    public Map<String, Object> getData(String USER , String PASSWORD,
                                       String name , String password_creator )
            throws IOException, ExecutionException, InterruptedException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);

        DocumentReference docRef = db.collection(name).document(password_creator)
                .collection(USER).document(PASSWORD);
// asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
// ...
// future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.getData();
        } else {
            //  System.out.println("No such document!");
        }

        return null;
    }


    public void addData(String id , String password , String code , String data ,
                        String name , String password_creator)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);

        DocumentReference docRef = db.collection(name).document(password_creator)
                .collection(String.valueOf(id)).document(password);
// asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
// ...
// future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Map<String, Object> map = new HashMap<>();
            map.put(code, data);

            ApiFuture<WriteResult> apiFuture = db.collection(name).document(password_creator)
                    .collection(String.valueOf(id)).document(password).update(map);

            System.out.println("Added document " + apiFuture.get().getUpdateTime());
        } else {

            Map<String, Object> map = new HashMap<>();
            map.put(code, data);

            ApiFuture<WriteResult> apiFuture = db.collection(name).document(password_creator)
                    .collection(String.valueOf(id)).document(password).set(map);

            System.out.println("Added document " + apiFuture.get().getUpdateTime());
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            // Remove any existing instances with the same name
            for (FirebaseApp app : firebaseApps) {
                if (app.getName().equals(APP_NAME)) {
                    FirebaseApp.getInstance(APP_NAME).delete();
                }
            }
        }

    }

    public ArrayList<String> giveDataDocumentPrivate(String name , String password )
            throws ExecutionException, InterruptedException, IOException {

        ArrayList<String> list = new ArrayList<>();
        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        ApiFuture<QuerySnapshot> future = db.collection(name).document(password).collection("PRIVATE").get();
// future.get() blocks on response
        System.out.println("****************************************************************");
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.getId());
            System.out.println(document.getId());
        }

        return list;
    }

    public  Map<String , Object> giveDataQuiz(String name  , String password , String title)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        // asynchronously retrieve all documents
        DocumentReference documentReference = db.collection(name).document(password)
                .collection("PRIVATE").document(title);
        ApiFuture<DocumentSnapshot> snapshotApiFuture = documentReference.get();

        DocumentSnapshot documentSnapshot = snapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            System.out.println("Document data: " + documentSnapshot.getData());

            return documentSnapshot.getData();
        } else {
            System.out.println("No such document!");
            return null;
        }
    }


    // DATE PENTRU A JUCA QUIZ , AUX
    public  ArrayList<Object> giveDataQuizAUX(String name , String password , String title)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        // asynchronously retrieve all documents
        DocumentReference documentReference =  db.collection(name).document(password)
                .collection("PRIVATE").document(title).collection("AUX")
                .document("date");
        ApiFuture<DocumentSnapshot> snapshotApiFuture = documentReference.get();

        DocumentSnapshot documentSnapshot = snapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            ArrayList<Object> list = new ArrayList<>(Objects.requireNonNull(documentSnapshot.getData()).values());
            System.out.println("Document data: " + documentSnapshot.getData().values());

            if(list.size() != 2){
                list.remove(2);
            }

            return list;
        } else {
            System.out.println("No such document!");
            return null;
        }

    }

    /*public  Map<String , Object> giveDataQuiz(String category , String title)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        // asynchronously retrieve all documents
        DocumentReference documentReference = db.collection(category).document(title);
        ApiFuture<DocumentSnapshot> snapshotApiFuture = documentReference.get();

        DocumentSnapshot documentSnapshot = snapshotApiFuture.get();
        if (documentSnapshot.exists()) {
            System.out.println("Document data: " + documentSnapshot.getData());

            return documentSnapshot.getData();
        } else {
            System.out.println("No such document!");
            return null;
        }
    }*/

    public void delete( String title , String name, String password)
            throws IOException, ExecutionException, InterruptedException {

        if (firebaseApp == null) {
            create2();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        db.collection(name).document(password).collection("PRIVATE")
                .document(title).
                collection("AUX").listDocuments().forEach(subDocRef -> {
            try {
                subDocRef.delete().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        // Șterge documentul "Joc"
        db.collection(name).document(password).collection("PRIVATE").document(title).delete().get();

        System.out.println("Ștergere completă realizată cu succes.");

    }

    public void createEditPrivate(String name , String password ,
                                  String category , String title ,
                                  Map<String , Object> map  ,
                                  String description , String note)
            throws IOException, ExecutionException, InterruptedException {

        if (firebaseApp == null) {
            create2();
        }

        Firestore db = FirestoreClient.getFirestore(firebaseApp);


        ApiFuture<WriteResult> future = db.collection(name).document(password)
                .collection(category).document(title).set(map);


       /* Firestore db1 = FirestoreClient.getFirestore(firebaseApp);

        Map<String , Object> map1 = new HashMap<>();
        map.put("description" , description);
        map.put("note" , note);

        ApiFuture<WriteResult> apiFuture = db1.collection(name).document(password)
                .collection("PRIVATE").document(title).collection("AUX")
                .document("date").set(map1);

        System.out.println("Added document " + apiFuture.get().getUpdateTime());
        System.out.println(future.get().getUpdateTime());
*/

        addAux(category , title , description , note  ,name , password);

    }

}
