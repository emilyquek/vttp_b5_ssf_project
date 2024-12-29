package vttp.ssf.project.utils;

import java.util.LinkedList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

public class Constants {

    public static final String REDIS_TEMPLATE = "redis-template";

    public static final int[] probability = {0, 0, 0, 0, 0 , 0, 
                                                    1, 1, 1, 1, 1, 
                                                    2, 2, 2, 2, 2, 2, 2, 2, 
                                                    3, 3, 3, 3, 
                                                    4, 4, 4, 4,
                                                    5, 5, 5, 5, 5, 5, 5,
                                                    6, 6, 6, 6, 6, 
                                                    7, 7, 7, 7, 7, 7, 7, 7};
                            
    public static final List<String> sanrioFigurines() {

        List<String> sanrioList = new LinkedList<>();
        sanrioList.add("badtzmaru.webp");
        sanrioList.add("cinamoroll.webp");
        sanrioList.add("hangyodon.webp");
        sanrioList.add("hellokitty.webp");
        sanrioList.add("kuromi.webp");
        sanrioList.add("mymelody.webp");
        sanrioList.add("pochacco.webp");
        sanrioList.add("pompompurin.webp");

        return sanrioList;
    }

    public static final List<String> kirbyFigurines() {

        List<String> kirbyList = new LinkedList<>();
        kirbyList.add("bandanawaddledee.jpg");
        kirbyList.add("eatingkirby.jpg");
        kirbyList.add("kingdedede.jpg");
        kirbyList.add("smilingkirby.jpg");
        kirbyList.add("standingkirby.jpg");
        kirbyList.add("metaknight.jpg");
        kirbyList.add("sittingkirby.jpg");
        kirbyList.add("sittingwaddledee.jpg");

        return kirbyList;
    }

    public static void initialiseSession(String username, HttpSession sess) {
        sess.setAttribute("username", username);
    }

    public static void destroySession(HttpSession sess) {
        sess.invalidate();
    }
}
