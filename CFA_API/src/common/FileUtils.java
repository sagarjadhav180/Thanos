package common;

import java.io.File;

public class FileUtils {

  public static void createDir(String dirPath) {
    File f = new File(System.getProperty("user.dir") + File.separator +  dirPath);
    System.out.println(f.getAbsolutePath());
    if (!f.exists()) {
      f.mkdirs();
    }
  }
}
