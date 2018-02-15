package br.com.company.paposolar.Beans;

import java.io.Serializable;

/**
 * Created by AndreLucas on 13/12/2017.
 */

public class Conversa implements Serializable{
    private String nomeUser;
    private String emailUser;
    private String id;
    private String room;

    public Conversa(String nomeUser, String emailUser, String id) {
        this.nomeUser = nomeUser;
        this.emailUser = emailUser;
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
