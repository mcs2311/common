package codex.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableNode implements Serializable {

  private static final long serialVersionUID = 1L;

  private boolean isDirectory;

  private String filename;

  // In case the node is a directory, here are its child nodes
  private List<SerializableNode> list;

  // In case the node is a file, this byte array is its content
  private byte[] bytes;

  public SerializableNode() {
  }

  public SerializableNode(String filePath) throws IOException {
    SerializableNode node = getNode(new File(filePath));
    this.isDirectory = node.isDirectory;
    this.filename = node.filename;
    this.list = node.list;
    this.bytes = node.bytes;
  }

  public void renameFolder(String _name) {
    filename =_name;
  }

  public void writeContentsToDir(String outputPath) throws IOException {
    if (!outputPath.endsWith(File.separator)) {
      outputPath = outputPath + File.separator;
    }
    writeToFile(outputPath, this);
  }

  private void writeToFile(String parentPath, SerializableNode node) throws IOException {
    if (node.isDirectory) {
      parentPath = parentPath + File.separator + node.filename;
      for (SerializableNode df : node.list) {
        writeToFile(parentPath, df);
      }
    } else {
      new File(parentPath).mkdirs();
      File file = new File(parentPath, node.filename);
      file.setReadable(true, false);
      file.setWritable(true, false);
      
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(file);
        fos.write(node.bytes);
        fos.flush();
      } finally {
        if (fos != null) {
          fos.close();
        }
      }
    }
  }

  private SerializableNode getNode(File file) throws IOException {

    SerializableNode node = new SerializableNode();
    node.filename = file.getName();

    if (file.isFile()) {
      node.isDirectory = false;
      node.bytes = getBytes(file);
    } else {
      node.isDirectory = true;
      for (File f : file.listFiles()) {
        node.addToList(getNode(f));
      }
    }
    return node;
  }

  private void addToList(SerializableNode node) {
    if (list == null) {
      list = new ArrayList<SerializableNode>();
    }
    list.add(node);
  }

  private byte[] getBytes(File file) throws IOException {
    FileInputStream fileInputStream = null;

    byte[] bytes = new byte[(int) file.length()];

    try {
      // convert file into array of bytes
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytes);
      return bytes;
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}