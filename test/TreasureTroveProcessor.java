
import com.fuscard.commons.FileUtils;
import java.io.File;

public class TreasureTroveProcessor {

    private int deleted = 0;
    /**
     * Delete all empty directories and html files
     *
     * @param directory
     */
    private void process(File directory) {
        if (directory.isFile()) {
            throw new IllegalArgumentException(
                    "Directory expected. File received:\n " + directory.getPath());
        }
        
        File[] files = directory.listFiles();

        for (File f : files) {
            if (f.isDirectory()) {
                process(f);
            } else {
                String extension = FileUtils.getExtension(f).toLowerCase();
                if (extension != null && ("html".equals(extension) || "htm".equals(extension))) {
                    System.out.println("Deleted:\t"+(deleted++)+"\t\tDeleting -->\t" + f.getPath());
                    f.delete();
                }
            }
        }
        if (directory.length() == 0) {
            System.out.println("Deleted:\t"+(deleted++)+"\t\tDeleting -->\t" + directory.getPath());
            directory.delete();
        }
    }
    public static void main(String[] args) {
        TreasureTroveProcessor ttp = new TreasureTroveProcessor();
        for(int i = 0; i < 100; i ++ ){
            ttp.process(new File("C:\\Users\\John Mwai\\Downloads\\blog.wwwzona.ru"));
        }
    }
}
