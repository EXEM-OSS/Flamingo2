package org.opencloudengine.flamingo2.mapreduce.applications.sabermetrics;

public class Team {
    int yearId;
    String leagueId;
    String teamId;
    String teamIdBR; // Team ID used by Baseball Reference website
    String teamIdLahman45; // Team ID used in Lahman database version 4.5
    String teamIdRetro; // Team ID used by Retrosheet
    int _1B; // Single
    int _2B; // Double
    int _3B; // Triple
    int AB; // At bats
    int attendance; // Home attendance total
    int BB; // Walks by batters
    int BBA; // Walks allowed
    int BPF; // Three-year park factor for batters
    int CG; // Complete games
    int CS; // Caught stealing
    String divId; // Team's division
    String divWin; // Division Winner (Y or N)
    int DP; // Double Plays
    int E; // Errors
    int ER; // Earned runs allowed
    double ERA; // Earned run average
    double FP; // Fielding  percentage
    String franchisId;
    int G; // Games played
    int GHOME; // Games played at home
    int H; // Hits by batters
    int HA; // Hits allowed
    int HBP; // Batters hit by pitch
    int HR; // Homeruns by batters
    int HRA; // Homeruns allowed
    int IPouts; // Outs Pitched (innings pitched x 3)
    int L; // Losses
    String leagueWin; // League Champion(Y or N)
    String name;
    String park; // Name of team's home ballpark
    int PPF; // Three-year park factor for pitchers
    int RS; // Runs scored
    int RA; // Opponents runs scored
    int RANK; // Position in final standings
    int SB; // Stolen bases
    int SF; // Sacrifice flies
    int SHO; // Shutouts
    int SO; // Strikeouts by batters
    int SOA; // Strikeouts by pitchers
    int SV; // Saves
    int W; // Wins
    String wcWin; // Wild Card Winner (Y or N)
    String wsWin; // World Series Winner (Y or N)

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

    public String getTeamIdBR() {
        return teamIdBR;
    }

    public void setTeamIdBR(String teamIdBR) {
        this.teamIdBR = teamIdBR;
    }

    public String getTeamIdLahman45() {
        return teamIdLahman45;
    }

    public void setTeamIdLahman45(String teamIdLahman45) {
        this.teamIdLahman45 = teamIdLahman45;
    }

    public String getTeamIdRetro() {
        return teamIdRetro;
    }

    public void setTeamIdRetro(String teamIdRetro) {
        this.teamIdRetro = teamIdRetro;
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

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getBB() {
        return BB;
    }

    public void setBB(int BB) {
        this.BB = BB;
    }

    public int getBBA() {
        return BBA;
    }

    public void setBBA(int BBA) {
        this.BBA = BBA;
    }

    public int getBPF() {
        return BPF;
    }

    public void setBPF(int BPF) {
        this.BPF = BPF;
    }

    public int getCG() {
        return CG;
    }

    public void setCG(int CG) {
        this.CG = CG;
    }

    public int getCS() {
        return CS;
    }

    public void setCS(int CS) {
        this.CS = CS;
    }

    public String getDivId() {
        return divId;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }

    public String getDivWin() {
        return divWin;
    }

    public void setDivWin(String divWin) {
        this.divWin = divWin;
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

    public double getFP() {
        return FP;
    }

    public void setFP(double FP) {
        this.FP = FP;
    }

    public String getFranchisId() {
        return franchisId;
    }

    public void setFranchisId(String franchisId) {
        this.franchisId = franchisId;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getGHOME() {
        return GHOME;
    }

    public void setGHOME(int GHOME) {
        this.GHOME = GHOME;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public int getHA() {
        return HA;
    }

    public void setHA(int HA) {
        this.HA = HA;
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

    public int getHRA() {
        return HRA;
    }

    public void setHRA(int HRA) {
        this.HRA = HRA;
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

    public String getLeagueWin() {
        return leagueWin;
    }

    public void setLeagueWin(String leagueWin) {
        this.leagueWin = leagueWin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public int getPPF() {
        return PPF;
    }

    public void setPPF(int PPF) {
        this.PPF = PPF;
    }

    public int getRS() {
        return RS;
    }

    public void setRS(int RS) {
        this.RS = RS;
    }

    public int getRA() {
        return RA;
    }

    public void setRA(int RA) {
        this.RA = RA;
    }

    public int getRANK() {
        return RANK;
    }

    public void setRANK(int RANK) {
        this.RANK = RANK;
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

    public int getSOA() {
        return SOA;
    }

    public void setSOA(int SOA) {
        this.SOA = SOA;
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

    public String getWcWin() {
        return wcWin;
    }

    public void setWcWin(String wcWin) {
        this.wcWin = wcWin;
    }

    public String getWsWin() {
        return wsWin;
    }

    public void setWsWin(String wsWin) {
        this.wsWin = wsWin;
    }
}