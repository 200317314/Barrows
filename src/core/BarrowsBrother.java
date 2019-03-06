package core;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public enum BarrowsBrother {
    AHRIM ("Ahrim the Blighted", 0, new Area(new Tile(3567, 3291), new Tile(3563, 3287)),
            new Area(new Tile(3561, 9704, 3), new Tile(3550, 9694, 3))),
    DHAROK("Dharok the Wretched",1, new Area(new Tile(3577, 3300), new Tile(3574, 3296)),
            new Area(new Tile(3560, 9719, 3), new Tile(3549, 9710, 3))),
    GUTHAN("Guthan the Infested", 2, new Area(new Tile(3579, 3284), new Tile(3575, 3281)),
            new Area(new Tile(3545, 9708, 3), new Tile(3534, 9699, 3))),
    KARIL("Karil the Tainted",3, new Area(new Tile(3568, 3278), new Tile(3563, 3273)),
            new Area(new Tile(3557, 9688, 3), new Tile(3545, 9678, 3))),
    TORAG("Torag the Corrupted", 4, new Area(new Tile(3555, 3284), new Tile(3552, 3281)),
            new Area(new Tile(3575, 9692, 3), new Tile(3564, 9682, 3))),
    VERAC("Verac the Defiled", 5, new Area(new Tile(3559, 3300), new Tile(3555, 3296)),
            new Area(new Tile(3579, 9710, 3), new Tile(3569, 9702, 3)));

    private final String name;
    private final int id;
    private final Area hillTopArea;
    private final Area cryptArea;

    BarrowsBrother(String name, int id, Area hillTopArea, Area cryptArea) {
        this.name = name;
        this.id = id;
        this.hillTopArea = hillTopArea;
        this.cryptArea = cryptArea;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Area getHillTopArea() {
        return hillTopArea;
    }

    public Area getCryptArea() {
        return cryptArea;
    }
}
