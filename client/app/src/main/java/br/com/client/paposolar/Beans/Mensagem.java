package br.com.client.paposolar.Beans;

/**
 * Created by AndreLucas on 13/12/2017.
 */

public class Mensagem {
    private String messageText;
    private UserType userType;
    private Status messageStatus;
    private String nomeText;
    private long id;

    public String getNomeText() {
        return nomeText;
    }

    public void setNomeText(String nomeText) {
        this.nomeText = nomeText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    private long messageTime;

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setMessageStatus(Status messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageText() {

        return messageText;
    }

    public UserType getUserType() {
        return userType;
    }

    public Status getMessageStatus() {
        return messageStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
