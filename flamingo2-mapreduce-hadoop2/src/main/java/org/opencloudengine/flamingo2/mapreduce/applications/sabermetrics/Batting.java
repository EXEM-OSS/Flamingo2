package org.opencloudengine.flamingo2.mapreduce.applications.sabermetrics;

public class Batting {

    String playerId;
    String leagueId;
    String teamId;
    int yearId;

    int stint; // player's stint (order of appearances within a season

    int _1B; // Single
    int _2B; // Doubles
    int _3B; // Triples
    int AB; // At Bats
    int BB; // Bases on Balls (Walks)
    int CS; // Caught Stealing
    int G; // Games
    int GIDP; // Grounded into double plays
    int H; // Hits
    int HBP; // Hit by Pitch
    int HR; // Homeruns
    int IBB; // Intentional Walks
    int R; // Runs
    int RBI; // Runs Batted In
    int SB; // Stolen Based
    int SF; // Sacrifice Flies
    int SH; // Sacrifice Hits
    int SO; // Strikeouts

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
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

    public int getYearId() {
        return yearId;
    }

    public void setYearId(int yearId) {
        this.yearId = yearId;
    }

    public int getStint() {
        return stint;
    }

    public void setStint(int stint) {
        this.stint = stint;
    }

    public int get_1B() {
        return _1B;
    }

    public void set_1B(int _1B) {
        this._1B = _1B;
    }

    public int get_2B() {
        return _2B;
    }

    public void set_2B(int _2B) {
        this._2B = _2B;
    }

    public int get_3B() {
        return _3B;
    }

    public void set_3B(int _3B) {
        this._3B = _3B;
    }

    public int getAB() {
        return AB;
    }

    public void setAB(int AB) {
        this.AB = AB;
    }

    public int getBB() {
        return BB;
    }

    public void setBB(int BB) {
        this.BB = BB;
    }

    public int getCS() {
        return CS;
    }

    public void setCS(int CS) {
        this.CS = CS;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getGIDP() {
        return GIDP;
    }

    public void setGIDP(int GIDP) {
        this.GIDP = GIDP;
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

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getRBI() {
        return RBI;
    }

    public void setRBI(int RBI) {
        this.RBI = RBI;
    }

    public int getSB() {
        return SB;
    }

    public void setSB(int SB) {
        this.SB = SB;
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

    public int getSO() {
        return SO;
    }

    public void setSO(int SO) {
        this.SO = SO;
    }
}