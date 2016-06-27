package org.opencloudengine.flamingo2.mapreduce.applications.sabermetrics;

public class Pitching {
    String playerId;
    int yearId;
    String leagueId;
    String teamId;
    int stint; // player's stint (order of appearances within a season)

    double BAOpp; // Opponent's Batting Average
    int BB; // Walks
    int BFP; // Batters faced by Pitcher
    int BK; // Balks
    int CG; // Complete Games
    int ER; // Earned Runs
    double ERA; // Earned Run Average
    int G; // Games
    int GF; // Games Finished
    int GIDP; // Grounded into double plays by opposing batter
    int GS; // Games Started
    int H; // Hits
    int HBP; // Batters Hit By Pitch
    int HR; // Homeruns
    int IBB; // Intentional Walks
    int IPouts; // Outs Pitched (innings pitched x 3)
    int L; // Losses
    int R; // Runs Allowed
    int SF; // Sacrifice flies by opposing batters
    int SH; // Sacrifices by opposing batters
    int SHO; // Shutouts
    int SO; // Strikeouts
    int SV; // Saves
    int W; // Wins
    int WP; // Wild Pitches

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

    public double getBAOpp() {
        return BAOpp;
    }

    public void setBAOpp(double BAOpp) {
        this.BAOpp = BAOpp;
    }

    public int getBB() {
        return BB;
    }

    public void setBB(int BB) {
        this.BB = BB;
    }

    public int getBFP() {
        return BFP;
    }

    public void setBFP(int BFP) {
        this.BFP = BFP;
    }

    public int getBK() {
        return BK;
    }

    public void setBK(int BK) {
        this.BK = BK;
    }

    public int getCG() {
        return CG;
    }

    public void setCG(int CG) {
        this.CG = CG;
    }

    public int getER() {
        return ER;
    }

    public void setER(int ER) {
        this.ER = ER;
    }

    public double getERA() {
        return ERA;
    }

    public void setERA(double ERA) {
        this.ERA = ERA;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getGF() {
        return GF;
    }

    public void setGF(int GF) {
        this.GF = GF;
    }

    public int getGIDP() {
        return GIDP;
    }

    public void setGIDP(int GIDP) {
        this.GIDP = GIDP;
    }

    public int getGS() {
        return GS;
    }

    public void setGS(int GS) {
        this.GS = GS;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public int getHBP() {
        return HBP;
    }

    public void setHBP(int HBP) {
        this.HBP = HBP;
    }

    public int getHR() {
        return HR;
    }

    public void setHR(int HR) {
        this.HR = HR;
    }

    public int getIBB() {
        return IBB;
    }

    public void setIBB(int IBB) {
        this.IBB = IBB;
    }

    public int getIPouts() {
        return IPouts;
    }

    public void setIPouts(int IPouts) {
        this.IPouts = IPouts;
    }

    public int getL() {
        return L;
    }

    public void setL(int l) {
        L = l;
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getSF() {
        return SF;
    }

    public void setSF(int SF) {
        this.SF = SF;
    }

    public int getSH() {
        return SH;
    }

    public void setSH(int SH) {
        this.SH = SH;
    }

    public int getSHO() {
        return SHO;
    }

    public void setSHO(int SHO) {
        this.SHO = SHO;
    }

    public int getSO() {
        return SO;
    }

    public void setSO(int SO) {
        this.SO = SO;
    }

    public int getSV() {
        return SV;
    }

    public void setSV(int SV) {
        this.SV = SV;
    }

    public int getW() {
        return W;
    }

    public void setW(int w) {
        W = w;
    }

    public int getWP() {
        return WP;
    }

    public void setWP(int WP) {
        this.WP = WP;
    }
}