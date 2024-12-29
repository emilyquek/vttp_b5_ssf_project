package vttp.ssf.project.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.ssf.project.models.Quiz;
import vttp.ssf.project.models.User;
import vttp.ssf.project.services.GachaService;
import vttp.ssf.project.utils.Constants;

@Controller
@RequestMapping
public class GachaController {

    @Autowired
    private GachaService gachaSvc;

    public User userNew;

    @GetMapping("/")
    public ModelAndView getIndex() {

        ModelAndView mav = new ModelAndView("index");

        mav.addObject("user", new User());

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView invalidAccess() {

        ModelAndView mav = new ModelAndView("invalid");

        return mav;
    }
    
    @PostMapping("/home")
    public ModelAndView postLogin(@Valid @ModelAttribute User user, BindingResult bindings, HttpSession sess) {

        ModelAndView mav = new ModelAndView();

        

        if (bindings.hasErrors()) {
            mav.setViewName("index");
            return mav;
        }
        
        mav.addObject("user", new User());

        String username = user.getUsername();
        String password = user.getPassword();
        
        Constants.initialiseSession(username, sess);

        userNew = gachaSvc.checkUser(username, password);

        int coins = userNew.getCoinBalance();
        
        //Constants.initialiseSession(fullName, sess);
        System.out.println(sess.getId());

        mav.setViewName("home");
        

        mav.addObject("username", username);
        mav.addObject("balance", coins);
        mav.setStatus(HttpStatusCode.valueOf(200));

        return mav;
    }



    @GetMapping("/quiz")
    public ModelAndView getQuiz() {

        ModelAndView mav = new ModelAndView();

        // get quiz question from svc
        Quiz quiz = gachaSvc.getQuiz();

        String quizQuestion = quiz.getQuestion();
        String quizAnswer= quiz.getAnswer();
        List<String> quizOptions = quiz.getOptions();

        mav.setViewName("quiz");
        mav.addObject("question", quizQuestion);
        mav.addObject("answer", quizAnswer);
        mav.addObject("quizOptions", quizOptions);
        mav.addObject("balance", userNew.getCoinBalance());
        mav.addObject("username", userNew.getUsername());
        
        return mav;
    }

    @PostMapping("/check")
    public ModelAndView checkQuiz(@RequestBody String userAnswer) {

        ModelAndView mav = new ModelAndView();

        String answerToCheck = HtmlUtils.htmlUnescape(userAnswer.substring(11).replace("+", " "));

        String checkAnswer = gachaSvc.checkAnswer(answerToCheck);

        System.out.printf(">>> check answer: %s\n", checkAnswer);
        System.out.printf(">>> user answer: %s\n", answerToCheck);

        String username = userNew.getUsername();

        if (checkAnswer.equals("correct")) {
            int newBalance = gachaSvc.getCoinBalance(username);
            mav.addObject("balance", newBalance);

        } 

        mav.setViewName("quiz");
        mav.addObject("checkAnswer", checkAnswer);
        mav.addObject("username", username);
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

    @GetMapping("/home")
    public ModelAndView goHome() {

        ModelAndView mav = new ModelAndView("home");

        mav.addObject("username", userNew.getUsername());
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

    @GetMapping("/logout")
    public ModelAndView getLogout(HttpSession sess) {

        ModelAndView mav = new ModelAndView("logout");
        Constants.destroySession(sess);

        return mav;
    }

    @GetMapping("/sanrio") 
    public ModelAndView getSanrioList() {

        ModelAndView mav = new ModelAndView("sanrio");

        String username = userNew.getUsername();

        List<String> sanrioList = gachaSvc.allUserSanrio(username);
        // userNew.setSanrioGachaList(sanrioList);
        System.out.printf(">>>>> user sanrio list: %s\n", sanrioList);

        List<String> sanrioNameList = new LinkedList<>();

        for (int i = 0; i < sanrioList.size(); i++) {
            String sanrioName = "/sanrio/%s".formatted(sanrioList.get(i));
            sanrioNameList.add(sanrioName);
        }

        mav.addObject("sanrioNameList", sanrioNameList);
        mav.addObject("username", username);
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

    @GetMapping("/kirby") 
    public ModelAndView getKirbyList() {

        ModelAndView mav = new ModelAndView("kirby");

        String username = userNew.getUsername();

        List<String> kirbyList = gachaSvc.allUserKirby(username);
        // userNew.setKirbyGachaList(kirbyList);
        System.out.printf(">>>>> user kirby list: %s\n", kirbyList);

        List<String> kirbyNameList = new LinkedList<>();

        for (int i = 0; i < kirbyList.size(); i++) {
            String kirbyName = "/kirby/%s".formatted(kirbyList.get(i));
            kirbyNameList.add(kirbyName);
        }

        mav.addObject("kirbyNameList", kirbyNameList);
        mav.addObject("username", username);
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

    @GetMapping("/sanrio/add")
    public ModelAndView addSanrioFigurine() {

        ModelAndView mav = new ModelAndView("sanriodraw");


        mav.addObject("username", userNew.getUsername());
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

    @GetMapping("/kirby/add")
    public ModelAndView addKirbyFigurine() {

        ModelAndView mav = new ModelAndView("kirbydraw");

        mav.addObject("username", userNew.getUsername());
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

    @GetMapping("/sanrio/draw") 
    public ModelAndView drawSanrioGacha() {

        ModelAndView mav = new ModelAndView("sanriogacha");

        String draw = gachaSvc.drawSanrio(userNew.getUsername());

        String drawName = "/sanrio/%s".formatted(draw);

        String figureName = draw.replaceAll(".webp", "");

        mav.addObject("draw", drawName);
        mav.addObject("figureName", figureName);
        mav.addObject("username", userNew.getUsername());
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

    @GetMapping("/kirby/draw") 
    public ModelAndView drawKirbyGacha() {

        ModelAndView mav = new ModelAndView("kirbygacha");

        String draw = gachaSvc.drawKirby(userNew.getUsername());

        String drawName = "/kirby/%s".formatted(draw);

        String figureName = draw.replaceAll(".jpg", "");

        mav.addObject("draw", drawName);
        mav.addObject("figureName", figureName);
        mav.addObject("username", userNew.getUsername());
        mav.addObject("balance", userNew.getCoinBalance());

        return mav;
    }

}
