package game.app.save;

import sps.core.Logger;
import sps.core.Point2;
import sps.util.Parse;

import java.util.ArrayList;

public class GameSnapshot {
    public static final int CurrentSaveFormatVersion = 7;

    public int SaveFormatVersion;
    public String RegionName;
    public int PopulationSize;
    public int TournamentLosses;
    public int TournamentWins;
    public int CumulativeArenaScore;
    public int RegionMapSeed;
    public String TopDeathCauses;
    public String BottomDeathCauses;
    public ArrayList<Point2> SettlementLocations;

    public String toPersistable() {
        String persistable = "";
        persistable += "RegionName=" + RegionName + "\n";
        persistable += "PopulationSize=" + PopulationSize + "\n";
        persistable += "CumulativeArenaScore=" + CumulativeArenaScore + "\n";
        persistable += "SaveFormatVersion=" + SaveFormatVersion + "\n";
        persistable += "TournamentLosses=" + TournamentLosses + "\n";
        persistable += "TournamentWins=" + TournamentWins + "\n";
        persistable += "RegionMapSeed=" + RegionMapSeed + "\n";
        persistable += "TopDeathCauses=" + TopDeathCauses + "\n";
        persistable += "BottomDeathCauses=" + BottomDeathCauses + "\n";

        String settlements = "";
        for (Point2 loc : SettlementLocations) {
            settlements += loc.X + "," + loc.Y;
            if (loc != SettlementLocations.get(SettlementLocations.size() - 1)) {
                settlements += ";";
            }
        }
        persistable += "SettlementLocations=" + settlements + "\n";

        return persistable;
    }

    public static GameSnapshot fromPersistable(final String persistable) {
        GameSnapshot snapshot = new GameSnapshot();
        for (String line : persistable.split("\n")) {
            String[] keyval = line.split("=");
            String key = keyval[0];
            String val = keyval[1];
            switch (key) {
                case "CumulativeArenaScore":
                    snapshot.CumulativeArenaScore = Parse.inte(val);
                    break;
                case "SaveFormatVersion":
                    snapshot.SaveFormatVersion = Parse.inte(val);
                    break;
                case "TournamentLosses":
                    snapshot.TournamentLosses = Parse.inte(val);
                    break;
                case "TournamentWins":
                    snapshot.TournamentWins = Parse.inte(val);
                    break;
                case "PopulationSize":
                    snapshot.PopulationSize = Parse.inte(val);
                    break;
                case "RegionMapSeed":
                    snapshot.RegionMapSeed = Parse.inte(val);
                    break;
                case "RegionName":
                    snapshot.RegionName = val;
                    break;
                case "TopDeathCauses":
                    snapshot.TopDeathCauses = val;
                    break;
                case "BottomDeathCauses":
                    snapshot.BottomDeathCauses = val;
                    break;
                case "SettlementLocations":
                    snapshot.SettlementLocations = new ArrayList<>();
                    for (String rawLoc : val.split(";")) {
                        String[] rawCoords = rawLoc.split(",");
                        float x = Parse.floa(rawCoords[0]);
                        float y = Parse.floa(rawCoords[1]);
                        snapshot.SettlementLocations.add(new Point2(x, y));
                    }
                    break;
                default:
                    Logger.error("Unknown key found in save file: \"" + key + "\". The game might not load properly or crash.");
            }
        }
        return snapshot;
    }
}
