import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Write Content to a File
public class FileWrite {
  private String outputDir;
  private FileWriter writer = null;

  public FileWrite(String dir) {
    outputDir = dir;
  }

  public void write(HtmlFile file) throws FileNotFoundException {
    String fileName = createProperFileName(file);
    File fle = new File(fileName);
    try {
      writer = new FileWriter(fle);
      writer.write("URL : " + file.getURL());
      writer.write("\n");
      writer.write("TITLE : " + file.getTITLE());
      writer.write("\n");
      writer.write("META-KEYWORDS : " + file.getMETA_KEYWORDS());
      writer.write("\n");
      writer.write("DATE : " + file.getDATE());
      writer.write("\n");
      writer.write("DOC ID : " + file.getDOC_ID());
      writer.write("\n");
      writer.write("CONTENT : " + file.getCONTENT());
    } catch (IOException e) {
      System.err.println("Failed to write to " + fileName);
    } finally {
      if (writer != null)
        try {
          writer.close();
        } catch (IOException e) {
          System.err.println("Failed to close " + fileName);
        }
    }
  }

  public void write(ArrayList<HtmlFile> files) throws FileNotFoundException {
    for (int i = 0; i < files.size(); i++) {
      write(files.get(i));
    }
  }

  private String createProperFileName(HtmlFile file) {
    if (outputDir != "") {
      return outputDir + "/Doc_" + file.getDOC_ID() + ".txt";
    }
    return "Doc_" + file.getDOC_ID() + ".txt";
  }
}
