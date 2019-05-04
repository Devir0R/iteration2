package com.example.ron.Players365Client;

public class SettingsForNotification {

    private boolean notificationForGoal;
    private boolean notificationForassist;
    private boolean notificationForingame;
    private boolean notificationForCleanSheet;
    private boolean notificationForRedCard;
    private boolean notificationForYellowCard;


    public SettingsForNotification(boolean notificationForGoal, boolean notificationForassist, boolean notificationForingame, boolean notificationForCleanSheet, boolean notificationForRedCard, boolean notificationForYellowCard) {
        this.notificationForGoal = notificationForGoal;
        this.notificationForassist = notificationForassist;
        this.notificationForingame = notificationForingame;
        this.notificationForCleanSheet = notificationForCleanSheet;
        this.notificationForRedCard = notificationForRedCard;
        this.notificationForYellowCard = notificationForYellowCard;
    }

    public boolean getNotificationForGoal() {
        return notificationForGoal;
    }

    public void setNotificationForGoal(boolean notificationForGoal) {
        this.notificationForGoal = notificationForGoal;
    }

    public boolean getNotificationForassist() {
        return notificationForassist;
    }

    public void setNotificationForassist(boolean notificationForassist) {
        this.notificationForassist = notificationForassist;
    }

    public boolean getNotificationForingame() {
        return notificationForingame;
    }

    public void setNotificationForingame(boolean notificationForingame) {
        this.notificationForingame = notificationForingame;
    }

    public boolean getNotificationForCleanSheet() {
        return notificationForCleanSheet;
    }

    public void setNotificationForCleanSheet(boolean notificationForCleanSheet) {
        this.notificationForCleanSheet = notificationForCleanSheet;
    }

    public boolean getNotificationForRedCard() {
        return notificationForRedCard;
    }

    public void setNotificationForRedCard(boolean notificationForRedCard) {
        this.notificationForRedCard = notificationForRedCard;
    }

    public boolean getNotificationForYellowCard() {
        return notificationForYellowCard;
    }

    public void setNotificationForYellowCard(boolean notificationForYellowCard) {
        this.notificationForYellowCard = notificationForYellowCard;
    }

    @Override
    public String toString() {
        return "SettingsForNotification{" +
                "notificationForGoal=" + notificationForGoal +
                ", notificationForassist=" + notificationForassist +
                ", notificationForingame=" + notificationForingame +
                ", notificationForCleanSheet=" + notificationForCleanSheet +
                ", notificationForRedCard=" + notificationForRedCard +
                ", notificationForYellowCard=" + notificationForYellowCard +
                '}';
    }
}
