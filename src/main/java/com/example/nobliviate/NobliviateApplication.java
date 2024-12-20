package com.example.nobliviate;

import com.google.firebase.FirebaseApp;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@Controller
public class NobliviateApplication {

	public static void main(String[] args){
		SpringApplication.run(NobliviateApplication.class, args);
	}

	// PAGINILE CENTRALE

	//HOME
	@GetMapping("/")
	public String home(HttpSession session , Model model){

	  //session.setAttribute("logat" , null);

		String log = (String) session.getAttribute("logat");
		System.out.println(log);

		if(log != null &&  log.equals("logat")){
			String userName =(String) session.getAttribute("username");
			System.out.println(userName);
			model.addAttribute("name" , userName);
			model.addAttribute("logat" , log);
		}
			return "homepage.html";
	}

	//CREATEA
	@GetMapping("/create.html")
	public String create(Model model , HttpSession session){

		Firebase firebase = new Firebase();
		ArrayList<String> list = firebase.allCollection();
		model.addAttribute("list" , list);

		String logTRue= (String) session.getAttribute("logat");

		if(logTRue == null){
			return "/REZ/logPlease.html";
		}

		return "create.html";
	}
	//PLay
	@GetMapping("/play.html")
	public String play(Model model , HttpSession session){
		Firebase firebase = new Firebase();
		ArrayList<String> list = firebase.allCollection();
		list.remove("PRIVATE");
		session.removeAttribute("playPrivate");
		model.addAttribute("list" , list);
		return "play.html";
	}
	//LOG SYSTEM
	@GetMapping("/log.html")
	public String log(HttpSession session , Model model)
			throws IOException, ExecutionException, InterruptedException {

		String log = (String) session.getAttribute("logat");
		System.out.println(log);

		if(log != null &&  log.equals("logat")){
			String userName =(String) session.getAttribute("username");
			System.out.println(userName);
			return creatorPage(session , model);
		}
		else{
			return "log.html";
		}
	}
	@GetMapping("/login.html")
	public String login(HttpSession session){
			return "login.html";

	}

	@GetMapping("creatorPage.html")
	public String creatorPage(HttpSession session , Model model)
			throws IOException, ExecutionException, InterruptedException {


		System.out.println("************////////////////////////****************");
		Firebase_login firebase_log = new Firebase_login();
		String name = (String) session.getAttribute("username");
		String passowrd = (String) session.getAttribute("userpassowrd") ;

		ArrayList<String> list = firebase_log.giveDataDocumentPrivate(name , passowrd);

		model.addAttribute("list"  , list);
		model.addAttribute("name"  , name);

		return "creatorPage.html";
	}

	@PostMapping("/privatelist")
	public String privatelist(HttpSession session  , Model model ,
							  @RequestParam("privatelist") String privatelist)
			throws IOException, ExecutionException, InterruptedException {
		String name = (String) session.getAttribute("username");
		String passowrd = (String) session.getAttribute("userpassowrd") ;

		Firebase_login firebase = new Firebase_login();
		Map<String , Object> map = firebase.giveDataQuiz(name , passowrd , privatelist);
		ArrayList<Object> list = firebase.giveDataQuizAUX(name , passowrd , privatelist);

		if(list.size() == 4){
			list.remove(2);
		}
		session.setAttribute("playPrivate" , "playPrivate");
		session.setAttribute("title" , privatelist);
		model.addAttribute("title" , privatelist);
		model.addAttribute("list" ,list);
		model.addAttribute("map" , map);

		return "infoPlay.html";
	}

	@GetMapping("/singin.html")
	public String singin(){
		return "singin.html";
	}
	//LOG FUNTION
	@PostMapping("/singIn")
	public String singinform(@RequestParam("name") String name,
							 @RequestParam("password") String pass ,
							 @RequestParam("confirmpassword") String confirm ,
							 HttpSession session ){
		if(!confirm.equals(pass)){
			return null;
		}

		// Store data in the session*/
		session.setAttribute("username", name);
		session.setAttribute("userpassowrd"  , pass);
		return "phonesingin.html";
	}

	@GetMapping("phonesingin.html")
	public String phonesingin(){
		return "phonesingin.html";
	}

	//SUCCES SING IN
	@GetMapping("/REZ/succes.html")
	public String succes(HttpSession session)
			throws ExecutionException, InterruptedException, IOException {
		Firebase_login firebase = new Firebase_login();
		String user = (String)session.getAttribute("username");
		String password = (String)session.getAttribute("userpassowrd");
		firebase.addClient(user , password);
		session.setAttribute("logat" , "logat");

		return "/REZ/succes.html";
	}

	@PostMapping("/logIn")
	public String logInform(@RequestParam("name") String name,
							@RequestParam("password") String pass,
							HttpSession session)
			throws IOException, ExecutionException, InterruptedException {

		Firebase_login firebase = new Firebase_login();


		System.out.println(session.getId());
		// Store data in the session

		if(firebase.checkClient(name , pass)){

			session.setAttribute("username", name);
			session.setAttribute("userpassowrd"  , pass);

			if (FirebaseApp.getApps() != null && FirebaseApp.getApps().size() > 0) {
				System.out.println("Firebase initialized successfully.");
			} else {
				System.out.println("Firebase initialization failed.");
			}
			return succesLogIn("logat",session);
		}
		else{
			session.invalidate();
			return "/REZ/tryAgain.html";
		}
	}

	@GetMapping("/succesLogIn.html")
	public String succesLogIn(@RequestParam("log")String log , HttpSession session){
		if(log.equals("logat")){;
			session.setAttribute("logat" , "logat");
			System.out.println(log);
			System.out.println("*************************");
		}
		return "/REZ/succesLogIn.html";
	}
	// CREATE QUIZ


	@PostMapping("/firstPartQuiz")
	public String firstPartQuiz(@RequestParam("title")String title ,
								@RequestParam("category") String category ,
								@RequestParam("textarea") String description  ,
								@RequestParam("note") String note ,
								HttpSession session , Model model) throws Exception {

		System.out.println(title + "->" + category + "->" + description + "->" + note);
		String name = (String)session.getAttribute("username");
		String password =  (String)session.getAttribute("userpassowrd");

		if(category.equals("PRIVATE")){

			session.setAttribute("category" , category);
			session.setAttribute("title" , title);
			session.setAttribute("description" , description);
			session.setAttribute("note" , note);

			Firebase_login firebase = new Firebase_login();
			firebase.addDocument(category , title);
			firebase.addAux(category , title , description , note  ,name , password);
		}

		else{


			session.setAttribute("title" , title);
			session.setAttribute("category" , category);
			session.setAttribute("description" , description);
			session.setAttribute("note" , note);

			Firebase firebase = new Firebase();
			firebase.addDocument(category , title);
			firebase.addAux(category , title , description , note , name , password);
		}

		return data1(model , session);
	}


	//ERROR
	@GetMapping("/REZ/tryAgain.html")
	public String tryAgain(){
		return "/REZ/tryAgain.html";
	}
	@GetMapping("/REZ/logPlease.html")
	public String logPlease(){
		return "/REZ/logPlease.html";
	}
	// REAZA QUIZ PARETEA TABEL
	@GetMapping("/tablecreate.html")
	
	public String data1( Model model , HttpSession session) throws Exception {

		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");

		String category = (String)session.getAttribute("category");
		String title = (String) session.getAttribute("title");


		Firebase firebase = new Firebase();
		Firebase_login firebaseLogin = new Firebase_login();
		HashMap<String , Object> map;
		if(category.equals("PRIVATE")){
			map = (HashMap<String, Object>)  firebaseLogin.getData(category , title , name , password);

			if(map != null){
				map.remove(" ");
			}

			session.setAttribute("map" , map);
			model.addAttribute("map",map);
		}
		else{
			map = (HashMap<String, Object>) firebase.getDtaa(category, title);
			session.setAttribute("map" , map);
			model.addAttribute("map",map);
		}


		return "tablecreate.html";
	}


	@PostMapping("/element")
	public String element(@RequestParam("key")String word ,
						  @RequestParam("def")String explain
			,Model model  ,HttpSession session) throws Exception {
		Firebase firebase = new Firebase();
		Firebase_login firebaseLogin = new Firebase_login();

		String category = (String)session.getAttribute("category");
		String title = (String) session.getAttribute("title");

		String name = (String)session.getAttribute("username");
		String password =  (String)session.getAttribute("userpassowrd");

		if(category.equals("PRIVATE")){
			firebaseLogin.addData(category , title , word , explain , name , password);
		}
		else{
			firebase.addData(category , title , word , explain);
		}

		return data1(model , session);
	}

	//FINISH QUIZ
	@PostMapping("/finishTable")
	public String finishTable(Model model , HttpSession session){

		String category = (String)session.getAttribute("category");
		String title = (String) session.getAttribute("title");
		String description = (String)session.getAttribute("description" );
		String note = (String) session.getAttribute("note");
		Map<String , Object> map = (Map<String, Object>) session.getAttribute("map");

		model.addAttribute("category",category);
		model.addAttribute("title", title);
		model.addAttribute("description", description);
		model.addAttribute("note" , note);
		model.addAttribute("map" , map);


		return "finishTable.html";

	}

	@GetMapping("finishTable.html")
	public String finishTable(){
		return "finishTable.html";
	}


	//PLAY FUNCTION
	@PostMapping("/playlist")
	public String playList(@RequestParam("item")String item ,
						   Model model , HttpSession session)
			throws IOException, ExecutionException, InterruptedException {
		System.out.println(item);
		session.setAttribute("item" , item);
		Firebase firebase = new Firebase();
		ArrayList <String> list = firebase.GetAllDocument(item);
		model.addAttribute("listDocuments" , list);

		return play(model , session);
	}

	@PostMapping("/DocumentList")
	public String DocumentList(Model model , @RequestParam("DocItem") String Docitem ,
							   HttpSession session)
			throws IOException, ExecutionException, InterruptedException {
		session.setAttribute("Docitem" , Docitem);
		System.out.println(Docitem);

		String category = (String)session.getAttribute("item");
		String docItem = (String)session.getAttribute("Docitem");

		Firebase firebase = new Firebase();
		Map<String , Object> map = firebase.giveDataQuiz(category , docItem);
		ArrayList<Object> list = firebase.giveDataQuizAUX(category , docItem);

		if(list.size() == 4){
			list.remove(2);
		}


		session.setAttribute("autor",list.get(1));
		session.setAttribute("note",list.get(0));
		session.setAttribute("descriere",list.get(2));

		model.addAttribute("autor", list.get(1));
		model.addAttribute("note" , list.get(0));
		model.addAttribute("descriere" , list.get(2));

		model.addAttribute("title" , Docitem);
		//model.addAttribute("list" ,list);
		model.addAttribute("map" , map);
		return "infoPlay.html";
	}

	@GetMapping("infoPlay.html")
	public String infoPlay(HttpSession session , Model model ,
						   @RequestParam("note")String note, @RequestParam("autor")String autor ,
						   @RequestParam("descriere")String descriere)
			throws IOException, ExecutionException, InterruptedException {
		String category = (String)session.getAttribute("item");
		String docItem = (String)session.getAttribute("Docitem");

		String title = (String) session.getAttribute("title");
		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");
		String playPrivate = (String) session.getAttribute("playPrivate");

		Firebase_login firebaseLogin = new Firebase_login();

		Firebase firebase = new Firebase();
		Map<String , Object> map;
		ArrayList<Object> list;
		if(playPrivate != null){
			map = firebaseLogin.giveDataQuiz(name , password , title);
			list = firebaseLogin.giveDataQuizAUX(name , password , title);
		}
		else{
			map = firebase.giveDataQuiz(category , docItem);
			list = firebase.giveDataQuizAUX(category , docItem);
		}



		model.addAttribute("title" , docItem);
		model.addAttribute("list" ,list);
		model.addAttribute("map" , map);
		System.out.println("*************************************************************************************************");

		model.addAttribute("autor", list.get(1));
		model.addAttribute("note" , list.get(0));
		model.addAttribute("descriere" , list.get(2));
		return "infoPlay.html";
	}

	//PLAY QUIZ SYSTEM
	@PostMapping("/playQuiz")
	public String playQuiz(Model model , HttpSession session){
		return typeGame(model , session);
	}


	@GetMapping("/TypeGame.html")
	public String typeGame(Model model , HttpSession session){
		String note = (String) session.getAttribute("note");
		String autor = (String) session.getAttribute("autor");
		String descriere = (String) session.getAttribute("descriere");

		System.out.println(note+ " " + autor+" " + descriere);

		model.addAttribute("note" , note);
		model.addAttribute("descriere", descriere);
		model.addAttribute("autor" , autor);

		return "TypeGame.html";
	}


	@PostMapping("/select")
	public String type(@RequestParam ("type") String tip ,@RequestParam("nr") int nr ,
					   Model model, HttpSession session)
			throws IOException, ExecutionException, InterruptedException {


		addAttribute(session);


		String category = (String)session.getAttribute("item");
		String docItem = (String)session.getAttribute("Docitem");
		String playPrivate = (String) session.getAttribute("playPrivate");

		String title = (String) session.getAttribute("title");
		System.out.println("|||||||||************************************|||||");
		System.out.println(title + " - " + docItem);
		System.out.println("|||||||||************************************|||||");
		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");
		Firebase_login firebaseLogin = new Firebase_login();

		Firebase firebase = new Firebase();
		Map<String , Object> map;

		if(playPrivate != null){
			map = firebaseLogin.giveDataQuiz(name , password , title);
		}
		else{
			map = firebase.giveDataQuiz(category , docItem);
		}


		System.out.println(map);

		int count;
		String type;
		int corect;
		int temp;
		try{
			System.out.println(tip + " - " + nr);
			 count = nr*map.size();
			temp = nr*map.size();
			type = tip;
			System.out.println(type +" - tipul ");

			map.remove("" , "");
			corect = count;

			session.setAttribute("temp" , temp);
			session.setAttribute("count" , (count));
			session.setAttribute("type" , type);
			session.setAttribute("corect" ,(corect));

			GameInterface(model ,session);
		}
		catch (Exception e){
		}
		return "GameInterface.html";
	}




	public void addAttribute(HttpSession session){
		HashSet<String > set = new HashSet<>();
		session.setAttribute("set",set);

		Set<String > doi = new HashSet<>();
		session.setAttribute("doi",doi);

		int a = 0;
		session.setAttribute("a"  , a);

		String ask ="";
		session.setAttribute("ask" , ask);

		String ans ="";
		session.setAttribute("ans" , ans);

		boolean status = true ;
		session.setAttribute("status" , status);

		int b = 0;
		session.setAttribute("b" , b);

	}
	public String question(HttpSession session)
			throws IOException, ExecutionException, InterruptedException {

		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");

		String category = (String)session.getAttribute("item");
		String docItem = (String)session.getAttribute("Docitem");

		HashSet<String > set = (HashSet<String>) session.getAttribute("set");
		Set<String > doi = (Set<String>) session.getAttribute("doi");

		Integer a = (Integer)  session.getAttribute("a");
		String ask = (String) session.getAttribute("ask");

		String type =  (String) session.getAttribute("type");

		String playPrivate = (String) session.getAttribute("playPrivate");

		String title = (String) session.getAttribute("title");
		Firebase_login firebaseLogin = new Firebase_login();

		Firebase firebase = new Firebase();
		Map<String , Object> map;

		if(playPrivate != null){
			map = firebaseLogin.giveDataQuiz(name , password , title);
		}
		else{
			map = firebase.giveDataQuiz(category , docItem);
		}



		RandomQuestion randomQuestion = new RandomQuestion();
		String text = switch (type) {
			case "key" -> randomQuestion.key(map , set);
			case "value" -> randomQuestion.value(map, doi);
			case"random" -> randomQuestion.random(map  , set , doi , a);
			default -> null;
		};
		ask = text;
		session.setAttribute("ask" , ask);
		if(a == 0){
			a = 1;
			session.setAttribute("a" , a);
		}
		else{
			a = 0;
			session.setAttribute("a" , a);
		}

		System.out.println(text);
		return text;
	}

	public void GameInterface(Model model , HttpSession session)
			throws IOException, ExecutionException, InterruptedException {

		String resultat =
				question(session);

		System.out.println(resultat);

		if(!resultat.equals("")){
			model.addAttribute("resultat",resultat);
		}

	}




	@PostMapping("/game")
	public String game(@RequestParam("question") String question , Model model , HttpSession session )
			throws IOException, ExecutionException, InterruptedException {

		Integer nr = (Integer) session.getAttribute("count");
		nr--;

		Integer corect = (Integer) session.getAttribute("corect");
		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");

		String category = (String)session.getAttribute("item");
		String docItem = (String)session.getAttribute("Docitem");
		String type =  (String) session.getAttribute("type");
		String ans = (String) session.getAttribute("ans");
		Integer a = (Integer) session.getAttribute("a");
		String ask = (String) session.getAttribute("ask");
		boolean statusGeneral = (Boolean) session.getAttribute("status");
		Integer b = (Integer) session.getAttribute("b");


		Firebase firebase = new Firebase();
		Firebase_login firebaseLogin = new Firebase_login();

		Map<String , Object> map;
		String playPrivate = (String) session.getAttribute("playPrivate");

		String title = (String) session.getAttribute("title");
		if(playPrivate != null){
			map = firebaseLogin.giveDataQuiz(name , password , title);
		}
		else{
			map = firebase.giveDataQuiz(category , docItem);
		}
		RandomQuestion randomQuestion = new RandomQuestion();

		ans = question;
		boolean status = randomQuestion.checker(question ,type , a ,map , ask);
		statusGeneral = status;

		session.setAttribute("ans"  , ans);
		session.setAttribute("status" , statusGeneral);

		if(!status){
			corect--;
			session.setAttribute("corect" ,	corect);
		}


		System.out.println(status);

		model.addAttribute("b" , b);
		model.addAttribute("status" , status);

		if(nr == 0){
			return fifnishGameInterface(model , session);
		}

		b++;

		System.out.println(b);
		session.setAttribute("b", b);
		session.setAttribute("count", nr);
		GameInterface(model , session);

		return "GameInterface.html";
	}




	@PostMapping("/printtest")
	public String print( Model model1 , HttpSession session)
			throws IOException, ExecutionException, InterruptedException {

		String category = (String)session.getAttribute("item");
		String docItem = (String)session.getAttribute("Docitem");
		String type =  (String) session.getAttribute("type");
		HashSet<String > set = (HashSet<String>) session.getAttribute("set");
		Set<String > doi = (Set<String>) session.getAttribute("doi");
		Integer temp = (Integer) session.getAttribute("temp");

		Firebase firebase = new Firebase();

		Map<String , Object> map = firebase.giveDataQuiz(category , docItem);

		PrintTest printTest = new PrintTest() ;
		String text = printTest.getData(type , temp , map , set , (HashSet<String>) doi);
		System.out.print(text);

		model1.addAttribute("text" , text);
		return "Print.html";
	}

	@GetMapping("/Print.html")
	public String pri(){
		return "Print.html";
	}

	@GetMapping("/FinishGame.html")
	public String fifnishGameInterface(Model model ,
									   HttpSession session){

		//System.out.println(corect);
		int corect = (int) session.getAttribute("corect");
		int temp = (int) session.getAttribute("temp");
		String title  = (String)  session.getAttribute("Docitem");

		model.addAttribute("corect" , corect);
		model.addAttribute("din" , temp);
		model.addAttribute("title" , title);

		session.setAttribute("corect" , 0);

		HashSet<String > set = (HashSet<String>) session.getAttribute("set");
		set.clear();
		session.setAttribute("set" , set);


		return "FinishGame.html";
	}


	@PostMapping("/logOut")
	public String logOut(HttpSession session , Model  model){
		session.removeAttribute("logat");
		return home(session , model);
	}



	@PostMapping("delete")
	public String delete(HttpSession session , Model model)
			throws IOException, ExecutionException, InterruptedException {
/*
		String category = (String)session.getAttribute("item");
		String docItem = (String)session.getAttribute("Docitem");
*/

		Firebase_login firebaseLogin = new Firebase_login();
		Firebase firebase = new Firebase();

		String title = (String) session.getAttribute("title");

		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");

		String category =(String)session.getAttribute("category");

		if(category.equals("PRIVATE")){
			firebaseLogin.delete(title,name,password);

		}
		else{
			System.out.println(category + "->" + title);
			firebase.delete(category , title);

		}

		return home(session , model);
	}

	@PostMapping("/edit")
	public String edit(HttpSession session , Model model){
		return editPage(model , session) ;
	}

	@GetMapping("edit.html")
    public String editPage(Model model , HttpSession session){

		String category = (String)session.getAttribute("category");
		String docItem = (String)session.getAttribute("Docitem");
		String title = (String) session.getAttribute("title");
		String description = (String) session.getAttribute("description");
		String note = (String) session.getAttribute("note");

		System.out.println(category + " : " + docItem + " : " + title +
				" : " + description + " :  " + note);

		Firebase firebase = new Firebase();
		ArrayList<String> list = firebase.allCollection();
		for (int a = 0; a <list.size() ; a++) {
			if (list.get(a).equals(category)){
				String temp = list.get(0);
				list.set(0 , category);
				list.set(a , temp);
				break;
			}
		}

		model.addAttribute("list" , list);



		Map<String , Object> map = (Map<String, Object>) session.getAttribute("map");
		model.addAttribute("map" , map);
		model.addAttribute("category",category);
		model.addAttribute("docItem",docItem);
		model.addAttribute("title",title);
		model.addAttribute("description",description);
		model.addAttribute("note",note);

		return "edit.html";
	}

	@PostMapping("/editquiz")
	public String editquiz(Model model , HttpSession session ,
						   @RequestParam("keys") List<String> keys,
						   @RequestParam("values") List<String> values,
						   @RequestParam("note") String note,
						   @RequestParam("textarea") String description,
						   @RequestParam("title") String title,
						   @RequestParam("category") String category) throws IOException, ExecutionException, InterruptedException {

		session.removeAttribute("description");

		String oldTitle = (String)session.getAttribute("title");
		String oldCategory  =(String)session.getAttribute("category");
		System.out.println(oldTitle);
		System.out.println("| " + description  + " | "
				+ note + " | " + oldTitle);
		  Map<String , Object> map = new HashMap<>();

		for (int i = 0; i < keys.size(); i++) {
			map.put(keys.get(i), values.get(i));

		}

		map.remove("note");
		map.remove("description");

		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");


		Firebase firebase = new Firebase();
		Firebase_login firebase_login = new Firebase_login();

		if(category.equals("PRIVATE")){

			firebase_login.delete(oldTitle , name , password);
			firebase_login.createEditPrivate(name , password,
											category, title , map
											,description , note);

		}
		else{

			firebase.delete(oldCategory, oldTitle);
			firebase.createEdit( name ,  password ,
					 category ,  title ,
					 map  ,
				 description ,  note);

		}

		session.setAttribute("category",category);
		session.setAttribute("title",title);
		session.setAttribute("description",description);
		session.setAttribute("note",note);
		session.setAttribute("map" , map);

		model.addAttribute("map" , map);
		model.addAttribute("category",category);
		model.addAttribute("title",title);
		model.addAttribute("description",description);
		model.addAttribute("note",note);


		return finishTable(model , session);
	}


	@PostMapping("/clearList")
	public String clearList(@RequestParam("name1") String title
			, HttpSession session , Model model)
			throws IOException, ExecutionException, InterruptedException {

		String password = (String)session.getAttribute("userpassowrd");
		String name = (String) session.getAttribute("username");


		Firebase_login firebase_login = new Firebase_login();
		firebase_login.delete(title ,name,password );
		return creatorPage(session , model);
	}


	@PostMapping("/back")
	public String back(Model model , HttpSession session){
		return home(session , model);
	}
}
