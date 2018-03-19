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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mensagem mensagem = (Mensagem) o;

        if (id != mensagem.id) return false;
        if (messageTime != mensagem.messageTime) return false;
        if (messageText != null ? !messageText.equals(mensagem.messageText) : mensagem.messageText != null)
            return false;
        if (userType != mensagem.userType) return false;
        if (messageStatus != mensagem.messageStatus) return false;
        return nomeText != null ? nomeText.equals(mensagem.nomeText) : mensagem.nomeText == null;

    }

    @Override
    public int hashCode() {
        int result = messageText != null ? messageText.hashCode() : 0;
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (messageStatus != null ? messageStatus.hashCode() : 0);
        result = 31 * result + (nomeText != null ? nomeText.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (messageTime ^ (messageTime >>> 32));
        return result;
    }
}
