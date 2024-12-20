package com.example.nobliviate;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.imageio.spi.IIORegistry;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Firebase {

    private static final String APP_NAME = "nobliviateApp"; // Unique app name for this project
    private FirebaseApp firebaseApp;

    // CREAZA O INSTNATA DE BAZA FIREBASE
    /*public FirebaseApp create() throws IOException {
      /*  List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();
               // break;
            }
        }*/
/*
     if (!FirebaseApp.getApps().isEmpty()) {
            firebaseApp = FirebaseApp.getInstance(APP_NAME);
        } else{
            // Initialize FirebaseApp
            InputStream inputStream = getClass().getResourceAsStream("/nobliviate-c4306-firebase-adminsdk-5pqp6-a5d8ba6c04.json");
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
        return (firebaseApp);

    }*/

    public FirebaseApp create() throws IOException {
        try {

            firebaseApp = FirebaseApp.getInstance(APP_NAME);

        }catch (Exception e){

            InputStream inputStream = getClass().getResourceAsStream("/nobliviate-c4306-firebase-adminsdk-5pqp6-a5d8ba6c04.json");
            assert inputStream != null;

            FirebaseOptions options =FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options , APP_NAME);
            System.out.println("ERROR!!!"+e);
        }

        return firebaseApp;
    }

    


    // TOATE CATEGORIILE
    public   ArrayList<String> allCollection(){
        ArrayList<String> list = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/nobliviate-c4306-firebase-adminsdk-5pqp6-a5d8ba6c04.json");
            assert inputStream != null;
            FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            Firestore firestore = firestoreOptions.getService();
            // Fetch all collections
            Iterable<CollectionReference> collectionRefs = firestore.listCollections();

            for (CollectionReference collectionRef : collectionRefs) {
                System.out.println("Collection ID: " + collectionRef.getId());
                list.add(collectionRef.getId());
            }



            // Close the Firestore connection
            firestore.close();
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            // Remove any existing instances with the same name
            for (FirebaseApp app : firebaseApps) {
                if (app.getName().equals(APP_NAME)) {
                    FirebaseApp.getInstance(APP_NAME).delete();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    // DAUGA DATELE PENTRU QUIZ

    public void addData(String id , String password , String code , String data)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
          create();
        }

// Ensure firebaseApp is not null before
            Firestore db = FirestoreClient.getFirestore(firebaseApp);

            Map<String , Object> map = new HashMap<>();
            map.put(code , data);

            ApiFuture<WriteResult> apiFuture = db.collection(String.valueOf(id)).document(password).update(map);

            System.out.println("Added document " + apiFuture.get().getUpdateTime());
/*

        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();
                break;
            }
        }*/

        }



        // OFERA DATELE DIN TABEL , atunic cand se dauga elmenet ,
        // datele din table sunt afisate cu aceasta metoda
    public Map<String, Object> getDtaa(String USER , String PASSWORD )
            throws IOException, ExecutionException, InterruptedException {

        if (firebaseApp == null) {
            create();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);

            DocumentReference docRef = db.collection(USER).document(PASSWORD);
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


    // ADAUGA ELEMENTELE : TITLUL SI CATEGORIA
    public void addDocument(String category , String title)
            throws ExecutionException, InterruptedException, IOException {


        if (firebaseApp == null) {
            create();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
            Map<String, Object> docData = new HashMap<>();
// Add a new document (asynchronously) in collection "cities" with id "LA"
            ApiFuture<WriteResult> future = db.collection(category).document(title).set(docData);
// ...
// future.get() blocks on response
            System.out.println("Update time : " + future.get().getUpdateTime());

        /*List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();
                break;
            }
        }*/
            // Continue with Firestore operations
        }
        // Handle the case where initialization failed


    // SE DAUGA AUX
    public void addAux(String id , String password , String description,
                       String note , String name , String password1)
            throws IOException, ExecutionException, InterruptedException {


        if (firebaseApp == null) {
            create();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);

            Map<String , Object> map = new HashMap<>();
            map.put("description" , description);
            map.put("note" , note);
            map.put("creator" , name);
            map.put("passCreator" , password1);
            ApiFuture<WriteResult> apiFuture = db.collection(String.valueOf(id)).document(password).collection("AUX")
                    .document("date").set(map);

            System.out.println("Added document " + apiFuture.get().getUpdateTime());
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();

            }
        }

        }


    // ?
    public ArrayList<String> GetAllDocument(String category)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
           firebaseApp=  create();
        }
        ArrayList<String> list = new ArrayList<>();

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        ApiFuture<QuerySnapshot> future = db.collection(category).get();
// future.get() blocks on response
        System.out.println("****************************************************************");
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if(document.getData().size() != 0){
                list.add(document.getId());
            }
        }
/*
// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
            // asynchronously retrieve all documents
            ApiFuture<QuerySnapshot> future = db.collection(category).get();
// future.get() blocks on response
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            ArrayList<String> list = new ArrayList<>();

            for (QueryDocumentSnapshot document : documents) {
                if(document.getData().size() != 0){
                    list.add(document.getId());
                }
                else{
                    ApiFuture<WriteResult> writeResult = db.collection(category).document(String.valueOf(document)).delete();
                }
                System.out.println(document.getId()); //+ " => " + document.getData());
            }

        ///////    list.remove("PRIVATE");
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        // Remove any existing instances with the same name
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(APP_NAME)) {
                FirebaseApp.getInstance(APP_NAME).delete();
                break;
            }
        }*/
            return list;
        }



        // DATELE PENTRU QUIZ  , MAP
    public  Map<String , Object> giveDataQuiz(String category , String title)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
            create();
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
        }


    // DATE PENTRU A JUCA QUIZ , AUX
    public  ArrayList<Object> giveDataQuizAUX(String category , String title)
            throws ExecutionException, InterruptedException, IOException {

        if (firebaseApp == null) {
            create();
        }

// Ensure firebaseApp is not null before
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
            // asynchronously retrieve all documents
            DocumentReference documentReference = db.collection(category).document(title).collection("AUX")
                    .document("date");
            ApiFuture<DocumentSnapshot> snapshotApiFuture = documentReference.get();

            DocumentSnapshot documentSnapshot = snapshotApiFuture.get();
            if (documentSnapshot.exists()) {
                ArrayList<Object> list = new ArrayList<>(Objects.requireNonNull(documentSnapshot.getData()).values());
                System.out.println("Document data: " + documentSnapshot.getData().values());

                list.remove(2);

                return list;
            } else {
                System.out.println("No such document!");
                return null;
            }

        }


        public void delete(String category , String title) throws IOException, ExecutionException, InterruptedException {

            if (firebaseApp == null) {
            create();
            }

// Ensure firebaseApp is not null before
                Firestore db = FirestoreClient.getFirestore(firebaseApp);
                db.collection(category).document(title).
                        collection("AUX").listDocuments().forEach(subDocRef -> {
                            try {
                                subDocRef.delete().get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        });

                // Șterge documentul "Joc"
                db.collection(category).document(title).delete().get();

                System.out.println("Ștergere completă realizată cu succes.");
        }

    public void createEdit(String name , String password ,
                                  String category , String title ,
                                  Map<String , Object> map  ,
                                  String description , String note)
            throws IOException, ExecutionException, InterruptedException {

        if (firebaseApp == null) {
            create();
        }

        Firestore db = FirestoreClient.getFirestore(firebaseApp);


        ApiFuture<WriteResult> future = db.collection(category).document(title).set(map);

        addAux(category , title , description , note  ,name , password);

    }





}
/*

 */
