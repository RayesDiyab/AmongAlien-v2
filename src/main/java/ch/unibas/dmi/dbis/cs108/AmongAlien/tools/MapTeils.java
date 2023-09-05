package ch.unibas.dmi.dbis.cs108.AmongAlien.tools;

/**
 * One piece of the whole Map is coled a MapTeil an it sores
 * the Information of its Texture and its use. The use will
 * be for example: accessible or inaccessible. Every use or
 * texture have its own ID.
 *
 * Use ID: 0 = accessible (Material can spawn)
 *         1 = inaccessible
 *         2 = Field with Material
 *         3 = GetTask Field
 *         4 = Fishingroth
 *         5 = accessible (no Material)
 *
 * Texture ID: 0 = The Teil in the upper left corner of the spreadsheet.
 *             1 = The next Teil to the right.
 *             As soon as the end is reached, it jumps to the next raw.
 *
 * To understand you have to have a look on to the spreadsheet.
 *
 * @author Joël Erbsland
 * @version 2021.05.10
 */
public class MapTeils {
    int textureID;
    int fieldUSE;
    int[] task = null;

    /**
     * This constructor of MapTeils will initialize a MapTeils with the
     * given textureID and fieldUSE.
     *
     * @param textureID the ID of this Tiels texturepart in the Spreadsheet.
     *                  Texture ID: 0 = The Teil in the upper left corner of the spreadsheet.
     *                              1 = The next Teil to the right.
     *                              As soon as the end is reached, it jumps to the next raw.
     * @param fieldUSE the number matching to the use of the Field.
     *                  Use ID: 0 = accessible
     *                          1 = inaccessible
     *                          2 = Field with Material
     *                          3 = GetTask Field
     *                          4 = Fishingroth
     *                          5 = accessible (no Material)
     */
    public MapTeils(int textureID, int fieldUSE) {
        this.textureID = textureID;
        this.fieldUSE = fieldUSE;
    }

    /**
     * This constructor of MapTeils will initialize a MapTeils with the
     * given textureID, fieldUSE and task (int[]]).
     *
     * @param textureID the ID of this Tiels texturepart in the Spreadsheet.
     *                  Texture ID: 0 = The Teil in the upper left corner of the spreadsheet.
     *                              1 = The next Teil to the right.
     *                              As soon as the end is reached, it jumps to the next raw.
     * @param fieldUSE the number matching to the use of the Field.
     *                  Use ID: 0 = accessible
     *                          1 = inaccessible
     *                          2 = Field with Material
     *                          3 = GetTask Field
     *                          4 = Fishingroth
     *                          5 = accessible (no Material)
     * @param task a array with the amount of materials needed to complete this fields task.
     *                  task[0] = amount of STONE
     *                  task[1] = amount of WOOD
     *                  task[2] = amount of CLAY
     *                  task[3] = amount of STRAWBERRY
     *                  task[4] = amount of BERRY
     *                  task[5] = amount of FISH
     */
    public MapTeils(int textureID, int fieldUSE, int[] task) {
        this.textureID = textureID;
        this.fieldUSE = fieldUSE;
        this.task = task; //Stone:Wood:Clay:Strawberry:Berry:Fish
    }

    /**
     * The actualize Method will change e MapTeils texture and use for
     * example after a Task is completed.
     *
     * @param textureID the ID of this Tiels texturepart in the Spreadsheet.
     *                  Texture ID: 0 = The Teil in the upper left corner of the spreadsheet.
     *                              1 = The next Teil to the right.
     *                              As soon as the end is reached, it jumps to the next raw.
     * @param fieldUSE the number matching to the use of the Field.
     *                  Use ID: 0 = accessible
     *                          1 = inaccessible
     *                          2 = Field with Material
     *                          3 = GetTask Field
     *                          4 = Fishingroth
     *                          5 = accessible (no Material)
     */
    public void actualize(int textureID, int fieldUSE) {
        this.textureID = textureID;
        this.fieldUSE = fieldUSE;
    }

    /**
     * A getter Method to get a fields textureID.
     *
     * @return int textureID: This fields textureID
     */
    public int getTextureID() {
        return textureID;
    }

    /**
     * A getter Method to get a fields fieldUSE.
     *
     * @return int fieldUSE: The fields use
     */
    public int getFieldUSE() {
        return fieldUSE;
    }

    /**
     * A getter Method to get a fields task as int-array.
     *
     * @return Fields Tasks as int[].
     */
    public int[] getTask() {
        if (task == null) {
            return new int[] {0, 0, 0, 0, 0, 0};
        } else {
            return task;
        }
    }
}
