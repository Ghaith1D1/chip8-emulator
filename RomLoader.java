import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RomLoader {

    public byte[] load(String filePath) {
        File file = new File(filePath);
        byte[] romData = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(romData);
            System.out.println("ROM geladen: " + filePath);
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der ROM: " + e.getMessage());
        }

        return romData;
    }
}