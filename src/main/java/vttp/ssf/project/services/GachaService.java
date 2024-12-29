package vttp.ssf.project.services;

import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf.project.models.Quiz;
import vttp.ssf.project.models.User;
import vttp.ssf.project.repositories.GachaRepo;
import vttp.ssf.project.utils.Constants;

@Service
public class GachaService {

    @Autowired
    private GachaRepo gachaRepo;
    
    public static final String QUIZ_URL = "https://the-trivia-api.com/v2/questions?limit=1&difficulties=easy&region=SG";

    public String quiz() {
        
        RequestEntity<Void> req = RequestEntity
        .get(QUIZ_URL)
        .accept(MediaType.APPLICATION_JSON)
        .build();

        try {
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.exchange(req, String.class);

            String payload = resp.getBody();
            System.out.printf(">>> payload: %s\n",payload);
            return getQuestionAndAnswer(payload);

        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String getQuestionAndAnswer(String json) {
        
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject result = reader.readArray().getJsonObject(0);

        String question = result.getJsonObject("question").getString("text");
        String answer = result.getString("correctAnswer").trim();
        String incorrectAnswers = result.getJsonArray("incorrectAnswers").toString().replace("\"", "").replace("[", "").replace("]", "");

        String quiz = question + "/" + answer + "/" + incorrectAnswers;
        System.out.println(quiz);

        return quiz;
    }

    Quiz setQuiz = new Quiz();

    public Quiz getQuiz() {
        
        String quiz = quiz();
        System.out.println(quiz);

        String[] quizArray = quiz.split("/");
        String quizQuestion = quizArray[0];
        String quizAnswer = quizArray[1];

        String[] incorrectAnswers = quizArray[2].split(",");

        List<String> options = new LinkedList<>();
        options.add(quizAnswer);
        for (int i = 0; i < incorrectAnswers.length; i++) {
            options.add(incorrectAnswers[i]);
        }
        Collections.shuffle(options);

        System.out.printf(">>>>> question: %s, answer: %s\n", quizQuestion, quizAnswer);
        System.out.printf(">>>>> options: %s\n", options.toString());

        setQuiz.setQuestion(quizQuestion);
        setQuiz.setAnswer(quizAnswer);
        setQuiz.setOptions(options);

        return setQuiz;
    }

    User userInfo = new User();

    public User checkUser(String un, String pw) {

        if (gachaRepo.userExists(un) == true) {
            userInfo.setUsername(gachaRepo.userUsername(un));
            userInfo.setPassword(gachaRepo.userPassword(un));
            userInfo.setCoinBalance(Integer.parseInt(gachaRepo.userBalance(un)));
        } else {
            gachaRepo.createUser(un, 100);
            gachaRepo.setPassword(un, pw);
            userInfo.setUsername(gachaRepo.userUsername(un));
            userInfo.setPassword(pw);
            userInfo.setCoinBalance(100);
        }

        return userInfo;
    }

    public String checkAnswer(String answer) {

        String quizAnswer = setQuiz.getAnswer().toLowerCase();
        System.out.printf(">>> answer: %s\n", quizAnswer); 
        System.out.printf(">>> answer2: %s\n", answer); 

        if ((answer.toLowerCase().trim()).equals(quizAnswer)) {
            int currentBalance = userInfo.getCoinBalance();
            int newBalance = currentBalance + 10;
            userInfo.setCoinBalance(newBalance);
            gachaRepo.setBalance(userInfo.getUsername(), newBalance);
            return "correct";
        } 

        return "wrong";
    }

    public int getCoinBalance(String username) {

       int currentBalance = Integer.parseInt(gachaRepo.userBalance(username));

       return currentBalance;
    }

    public String drawSanrio(String username) {
        
        Random rand = new Random();
        int n = rand.nextInt(0, 47);
        int probability = Constants.probability[n];

        String chosen = Constants.sanrioFigurines().get(probability);
        gachaRepo.addSanrioFigurine(username, chosen);
        // List<String> sanrioList = userInfo.getSanrioGachaList();
        // sanrioList.add(chosen);
        // userInfo.setKirbyGachaList(sanrioList);

        int currentBalance = userInfo.getCoinBalance();
        int newBalance = currentBalance - 5;
        userInfo.setCoinBalance(newBalance);
        gachaRepo.setBalance(userInfo.getUsername(), newBalance);        

        return chosen;
    }

    public String drawKirby(String username) {
        
        Random rand = new Random();
        int n = rand.nextInt(0, 47);
        int probability = Constants.probability[n];

        String chosen = Constants.kirbyFigurines().get(probability);
        gachaRepo.addKirbyFigurine(username, chosen);
        // List<String> kirbyList = userInfo.getKirbyGachaList();
        // kirbyList.add(chosen);
        // userInfo.setKirbyGachaList(kirbyList);

        int currentBalance = userInfo.getCoinBalance();
        int newBalance = currentBalance - 5;
        userInfo.setCoinBalance(newBalance);
        gachaRepo.setBalance(userInfo.getUsername(), newBalance);        

        return chosen;
    }


    public List<String> allUserSanrio(String username) {

        String keyName = username + ":sanrio";

        if (gachaRepo.userSanrioAll(keyName).isEmpty()) {
            return List.of();
        } else {
            System.out.printf(">>> service all sanrio: %s", gachaRepo.userSanrioAll(keyName));
            return gachaRepo.userSanrioAll(keyName);
        }
    }

    public List<String> allUserKirby(String username) {

        String keyName = username + ":kirby";

        if (gachaRepo.userKirbyAll(keyName).isEmpty()) {
            return List.of();
        } else {
            System.out.printf(">>> service all kirby: %s", gachaRepo.userKirbyAll(keyName));
            return gachaRepo.userKirbyAll(keyName);
        }
    }


}
