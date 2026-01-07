package europestats.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;
import europestats.JSON.RespostaAPI;

public class GestorAPI {

    private static final String API_KEY = "27470b51385298f76311ec679ef7ef3f";
    private static final String BASE_URL = "https://v3.football.api-sports.io/";

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public interface LogCallback {
        void onLog(String missatge);
    }

    private String traduirPosicio(String posAnglès) {
        if (posAnglès == null)
            return "Desconeguda";
        return switch (posAnglès) {
            case "Goalkeeper" -> "Porter";
            case "Defender" -> "Defensa";
            case "Midfielder" -> "Migcampista";
            case "Attacker" -> "Davanter";
            default -> posAnglès;
        };
    }

    public void executarImportacioEquipsEuropa(LogCallback logger) {
        logger.onLog("=== INICIANT IMPORTACIÓ MASSIVA EUROPEA (23-24) ===");
        int[] lligues = { 140, 39, 78, 135, 61, 88, 94 };

        for (int idLliga : lligues) {
            if (Thread.currentThread().isInterrupted()) {
                logger.onLog("🛑 Procés massiu aturat per l'usuari.");
                return;
            }
            importarLligaIndividual(idLliga, logger);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        logger.onLog("✅ Sincronització europea completada.");
    }

    public void importarLligaIndividual(int idLliga, LogCallback logger) {
        if (Thread.currentThread().isInterrupted())
            return;
        String nomLliga = obtenirNomLliga(idLliga);
        logger.onLog("🚀 Iniciant sincronització de: " + nomLliga);

        importarClassificacio(idLliga, logger);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        importarJugadorsPerLliga(idLliga, logger);
    }

    private void importarClassificacio(int leagueId, LogCallback logger) {
        if (Thread.currentThread().isInterrupted())
            return;
        String jsonResponse = cridarAPI(BASE_URL + "standings?league=" + leagueId + "&season=2023");
        if (jsonResponse == null)
            return;

        try {
            JsonObject jobj = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray responseArray = jobj.getAsJsonArray("response");
            if (responseArray.size() == 0)
                return;

            JsonObject leagueObj = responseArray.get(0).getAsJsonObject().getAsJsonObject("league");
            JsonArray standingsArray = leagueObj.getAsJsonArray("standings").get(0).getAsJsonArray();

            EquipDAO equipDAO = new EquipDAO();
            String nomLliga = obtenirNomLliga(leagueId);

            for (JsonElement element : standingsArray) {
                if (Thread.currentThread().isInterrupted())
                    return;

                JsonObject s = element.getAsJsonObject();
                JsonObject team = s.getAsJsonObject("team");
                JsonObject all = s.getAsJsonObject("all");
                JsonObject goals = all.getAsJsonObject("goals");

                // Netegem el nom de l'equip que ve de l'API
                String nomEquipNet = team.get("name").getAsString().replace(";", ",");

                Equip equip = new Equip(
                        team.get("id").getAsInt(),
                        leagueId,
                        nomLliga,
                        s.get("rank").getAsInt(),
                        nomEquipNet,
                        s.get("points").getAsInt(),
                        all.get("played").getAsInt(),
                        all.get("win").getAsInt(),
                        all.get("draw").getAsInt(),
                        all.get("lose").getAsInt(),
                        goals.get("for").getAsInt(),
                        goals.get("against").getAsInt(),
                        s.get("goalsDiff").getAsInt());

                equipDAO.insertarOActualitzarEquipComplet(equip);
            }
            logger.onLog("📊 Equips de " + nomLliga + " actualitzats.");
        } catch (Exception e) {
            logger.onLog("❌ Error classificació: " + e.getMessage());
        }
    }

    public void importarJugadorsPerLliga(int idLliga, LogCallback logger) {
        EquipDAO eDAO = new EquipDAO();
        List<Equip> llistaEquips = eDAO.obtenirTotsElsEquips();

        logger.onLog("⏳ Actualitzant jugadors de la lliga: " + obtenirNomLliga(idLliga));

        for (Equip equip : llistaEquips) {
            if (Thread.currentThread().isInterrupted())
                return;
            // FILTRE CRUCIAL: Només cridem a l'API per equips de la lliga que estem
            // important
            if (equip.getIdLliga() == idLliga) {
                processarTotesLesPaginesEquip(equip.getID(), equip.getNom_Equip(), idLliga, logger);
            }
        }
    }

    public void processarTotesLesPaginesEquip(int teamId, String nomEquip, int idLliga, LogCallback logger) {
        JugadorDAO jDAO = new JugadorDAO();
        int paginaActual = 1;
        int totalPagines = 1;

        do {
            if (Thread.currentThread().isInterrupted())
                return;
            String url = BASE_URL + "players?league=" + idLliga + "&season=2023&team=" + teamId + "&page="
                    + paginaActual;

            try {
                logger.onLog("📡 " + nomEquip + " -> Pàgina " + paginaActual + "...");
                String jsonResponse = cridarAPI(url);
                RespostaAPI dades = gson.fromJson(jsonResponse, RespostaAPI.class);

                if (dades != null && dades.getResponse() != null) {
                    if (dades.getPaging() != null)
                        totalPagines = dades.getPaging().total;

                    for (RespostaAPI.ItemAPI item : dades.getResponse()) {
                        if (Thread.currentThread().isInterrupted())
                            return;
                        if (item.statistics != null && !item.statistics.isEmpty()) {
                            Jugador j = mapejarItemAJugador(item, idLliga, teamId);
                            jDAO.insertarOActualitzarJugador(j);
                        }
                    }
                }
                paginaActual++;
                Thread.sleep(6500); // Pausa per seguretat de l'API

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                logger.onLog("❌ Error a " + nomEquip + ": " + e.getMessage());
                break;
            }
        } while (paginaActual <= totalPagines);
    }

    private Jugador mapejarItemAJugador(RespostaAPI.ItemAPI item, int idLliga, int teamId) {
        RespostaAPI.PlayerAPI p = item.player;
        RespostaAPI.StatsAPI s = item.statistics.get(0);

        // Netegem el nom del jugador directament des de la recepció
        String nomJugadorNet = p.name.replace(";", ",");

        Equip equipContenidor = new Equip(teamId, idLliga, obtenirNomLliga(idLliga), 0, "", 0, 0, 0, 0, 0, 0, 0, 0);

        String posicioAngles = (s.games != null && s.games.position != null) ? s.games.position : "Unknown";
        String posicioCatala = traduirPosicio(posicioAngles);

        int gols = (s.goals != null && s.goals.total != null) ? s.goals.total : 0;
        int assistencies = (s.goals != null && s.goals.assists != null) ? s.goals.assists : 0;
        int minuts = (s.games != null && s.games.minutes != null) ? s.games.minutes : 0;

        double golsPer90 = (minuts > 0) ? (double) (gols * 90.0) / minuts : 0.0;
        double assistPer90 = (minuts > 0) ? (double) (assistencies * 90.0) / minuts : 0.0;

        int grogues = (s.cards != null && s.cards.yellow != null) ? s.cards.yellow : 0;
        int vermelles = (s.cards != null && s.cards.red != null) ? s.cards.red : 0;

        return new Jugador(p.id, nomJugadorNet, posicioCatala, equipContenidor, idLliga, gols, assistencies, minuts,
                grogues, vermelles, golsPer90, assistPer90);
    }

    private String cridarAPI(String urlExterna) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlExterna))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return (response.statusCode() == 200) ? response.body() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String obtenirNomLliga(int idLliga) {
        return switch (idLliga) {
            case 140 -> "La Liga";
            case 39 -> "Premier League";
            case 78 -> "Bundesliga";
            case 135 -> "Serie A";
            case 61 -> "Ligue 1";
            case 88 -> "Eredivisie";
            case 94 -> "Primeira Liga";
            default -> "Desconeguda";
        };
    }
}