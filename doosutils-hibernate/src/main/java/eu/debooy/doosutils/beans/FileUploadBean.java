package eu.debooy.doosutils.beans;


/**
 * @author Marco de Booy
 * @version $Revision: 1.1 $
 */
public class FileUploadBean {
  private byte[]  file;

  /**
   * @return the file
   */
  public byte[] getFile() {
    return file.clone();
  }

  /**
   * @param file the file to set
   */
  public void setFile(byte[] file) {
    this.file = file.clone();
  }
}
