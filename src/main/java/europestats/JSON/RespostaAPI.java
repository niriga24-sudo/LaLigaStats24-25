package europestats.JSON;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RespostaAPI {
    public List<ItemAPI> response;
    public PagingAPI paging;

    public List<ItemAPI> getResponse() {
        return response;
    }

    public PagingAPI getPaging() {
        return paging;
    }

    public static class PagingAPI {
        public Integer current; 
        public Integer total;   // Aquest ens indica quantes pàgines hi ha per equip
    }

    public static class ItemAPI {
        public PlayerAPI player;
        public List<StatsAPI> statistics;
        public LeagueAPI league;
    }

    public static class PlayerAPI {
        public int id;
        public String name;
    }

    public static class StatsAPI {
        public TeamAPI team;
        public GamesAPI games;
        public GoalsAPI goals;
        public PassesAPI passes;
        public CardsAPI cards;
    }

    public static class TeamAPI {
        public int id; 
        public String name;
    }

    public static class GamesAPI {
        // Correcció de l'API: normalment és 'appearances'
        @SerializedName("appearances") 
        public Integer appearances;

        public Integer minutes;
        public String position;
    }

    public static class GoalsAPI {
        public Integer total;
        public Integer assists;
    }

    public static class PassesAPI {
        public Integer total;
        public Integer accuracy;
    }

    public static class CardsAPI {
        public Integer yellow;
        public Integer red;
    }

    public static class LeagueAPI {
        public List<List<StandingAPI>> standings;
    }

    public static class StandingAPI {
        public int rank;
        public TeamAPI team;
        public int points;
        public int goalsDiff;
        public AllStatsAPI all;
    }

    public static class AllStatsAPI {
        public int played;
        public int win;
        public int draw;
        public int lose;
        public GoalsStandingsAPI goals;
    }

    public static class GoalsStandingsAPI {
        @SerializedName("for") 
        public int for_;
        public int against;
    }
}