package org.opencloudengine.flamingo2.mapreduce.applications.sabermetrics;

public class Fielding {

    String playerId;

    int yearId;

    String leagueId;

    String teamId;

    int stint; // player's stint (order of appearances within a season)

    String position;

    int A; // Assists
    int CS; // Opponents Caught Stealing (by catchers)
    int DP; // Double Plays
    int E; // Errors
    int G; // Games
    int GS; // Games Started
    int innOuts; // Time played in the field expressed as outs
    int PB; // Passed Balls (by catchers)
    int PO; // Putouts
    int SB; // Opponent Stolen Bases (by catchers)
    int WP; // Wild Pitches (by catchers)
    double ZR; // Zone Rating

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getYearId() {
        return yearId;
    }

    public void setYearId(int yearId) {
        this.yearId = yearId;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getStint() {
        return stint;
    }

    public void setStint(int stint) {
        this.stint = stint;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public int getCS() {
        return CS;
    }

    public void setCS(int CS) {
        this.CS = CS;
    }

    public int getDP() {
        return DP;
    }

    public void setDP(int DP) {
        this.DP = DP;
    }

    public int getE() {
        return E;
    }

    public void setE(int e) {
        E = e;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getGS() {
        return GS;
    }

    public void setGS(int GS) {
        this.GS = GS;
    }

    public int getInnOuts() {
        return innOuts;
    }

    public void setInnOuts(int innOuts) {
        this.innOuts = innOuts;
    }

    public int getPB() {
        return PB;
    }

    public void setPB(int PB) {
        this.PB = PB;
    }

    public int getPO() {
        return PO;
    }

    public void setPO(int PO) {
        this.PO = PO;
    }

    public int getSB() {
        return SB;
    }

    public void setSB(int SB) {
        this.SB = SB;
    }

    public int getWP() {
        return WP;
    }

    public void setWP(int WP) {
        this.WP = WP;
    }

    public double getZR() {
        return ZR;
    }

    public void setZR(double ZR) {
        this.ZR = ZR;
    }
}