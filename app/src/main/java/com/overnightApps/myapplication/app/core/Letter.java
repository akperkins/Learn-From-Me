package com.overnightApps.myapplication.app.core;

import com.overnightApps.myapplication.app.service.PrivateLetterSender;

import junit.framework.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * Created by andre on 3/21/14.
 */
public class Letter implements Serializable {
    private String recipient;
    private String message;
    private String signature;
    private User creator;
    private Letter original;
    private long creationTime;
    private List<Comment> commentList;
    private int votes;
    private boolean isSignedPublicly;

    public Letter(String recipient, String message, String signature, User creator,
                  long creationTime, List<Comment> commentList, Letter original, boolean isSignedPublicly) {
        this.recipient = recipient;
        this.message = message;
        this.signature = signature;
        this.creator = creator;
        this.creationTime = creationTime;
        this.commentList = commentList;
        Assert.assertNotNull(commentList);
        this.original = original;
        votes = 0;
        this.isSignedPublicly = isSignedPublicly;
    }

    public Letter(String recipient, String message, String signature, User creator,
                  long creationTime,List<Comment> commentList, Letter original,boolean isSignedPublicly,
                  int votes) {
        this(recipient, message, signature, creator, creationTime, commentList, original,isSignedPublicly);
        this.votes = votes;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Letter getOriginal() {
        return original;
    }

    public void setOriginal(Letter original) {
        this.original = original;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void addComment(String message, User user) {
        commentList.add(new Comment(message, user));
    }

    public boolean isSignedPublicly() {
        return isSignedPublicly;
    }

    public void setSignedPublicly(boolean isSignedPublicly) {
        this.isSignedPublicly = isSignedPublicly;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    public void addAmountToRating(int changeInLetterVote) {
        votes += changeInLetterVote;
    }

    @Override
    /**creation time was removed from equals because we only use this function in the {@link
     * com.overnightApps.myapplication.app.service.PrivateLetterSender} and {@link
     * com.overnightApps.myapplication.app.service.PublicLetterSender} tests to compare that
     * the letters are saved appropriately (with respect to public or private signatures.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Letter letter = (Letter) o;

        if (isSignedPublicly != letter.isSignedPublicly) return false;
        if (votes != letter.votes) return false;
        if (!creator.equals(letter.creator)) return false;
        if (!message.equals(letter.message)) return false;
        if (original != null ? !original.equals(letter.original) : letter.original != null)
            return false;
        if (!recipient.equals(letter.recipient)) return false;
        if (!signature.equals(letter.signature)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = recipient.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + signature.hashCode();
        result = 31 * result + creator.hashCode();
        result = 31 * result + (original != null ? original.hashCode() : 0);
        result = 31 * result + votes;
        result = 31 * result + (isSignedPublicly ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "recipient='" + recipient + '\'' +
                ", message='" + message + '\'' +
                ", signature='" + signature + '\'' +
                ", creator=" + creator +
                ", original=" + original +
                ", creationTime=" + creationTime +
                ", commentList=" + commentList +
                ", votes=" + votes +
                ", isSignedPublicly=" + isSignedPublicly +
                '}';
    }
}