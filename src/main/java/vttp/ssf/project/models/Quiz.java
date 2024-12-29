package vttp.ssf.project.models;

import java.util.List;

public class Quiz {
    
    private String question;
    private String answer;
    private List<String> options;

    public List<String> getOptions() {return options;}
    public void setOptions(List<String> options) {this.options = options;}

    public String getQuestion() {return question;}
    public void setQuestion(String question) {this.question = question;}

    public String getAnswer() {return answer;}
    public void setAnswer(String answer) {this.answer = answer;}

    public Quiz() {}
}
