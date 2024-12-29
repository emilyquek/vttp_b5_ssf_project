package vttp.ssf.project.models;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class User {

    @NotEmpty(message = "Please enter your username!")
    @Size(min = 6, max = 20, message = "Username is between 6 and 20 characters long!")
    private String username;

    @NotEmpty(message = "Please enter your password!")
    @Size(min = 8, max = 30, message = "Passsword is between 8 and 30 characters long!")
    private String password;

    public List<String> kirbyGachaList;

    public List<String> sanrioGachaList;

    private int coinBalance = 100;

    public Integer getCoinBalance() {return coinBalance;}
    public void setCoinBalance(Integer coinBalance) {this.coinBalance = coinBalance;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public List<String> getKirbyGachaList() {return kirbyGachaList;}
    public void setKirbyGachaList(List<String> kirbyGachaList) {this.kirbyGachaList = kirbyGachaList;}

    public List<String> getSanrioGachaList() {return sanrioGachaList;}
    public void setSanrioGachaList(List<String> sanrioGachaList) {this.sanrioGachaList = sanrioGachaList;}

    
    public User() {}
    
}
