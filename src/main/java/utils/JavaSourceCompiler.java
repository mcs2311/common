//-------------------------------------------------------------------------------------
package codex.common.utils;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.jar.*;
import javax.tools.*;
import java.nio.file.*;

//-------------------------------------------------------------------------------------
public class JavaSourceCompiler {

//-------------------------------------------------------------------------------------
	public static boolean generate(Debug _debug, String _taskPath, String _jarName){
		String _buildPath = _taskPath + ".class/";
		FileUtils.ifDoesntExistCreate(_buildPath);
		boolean _success = compileJava(_debug, _buildPath, _taskPath + ".java/", _jarName);
		if(_success){
//			try{
//				_debug.outln("Changing path: " + _buildPath);
//				System.setProperty("user.dir", _buildPath);
				File[] _toBeJarred = (new File(_buildPath, "waves/tests/")).listFiles();//new File[1];
//				_toBeJarred[0] = new File("waves/tests/WaveTest.class");
				if(_toBeJarred == null){
					_debug.outln("ERROR: cannot retreive classes to be jarred in " + _buildPath);
					return false;
				}
				_debug.outln(Debug.IMPORTANT1, "Setting curent directory to: "+_taskPath);
				System.setProperty("user.dir", _taskPath);
				_success = createJar(_debug, _buildPath, new File(_taskPath + ""+_jarName), _toBeJarred);
//			}catch(IOException _e){}
		}
		return _success;
	}

//-------------------------------------------------------------------------------------
	public static boolean compileJava(Debug _debug, String _buildPath, String _javaPath, String _jarName){
		_debug.outln("Compiling...");
//		System.setProperty("useJavaUtilZip","true");
		ArrayList<String> _optionList = new ArrayList<String>();
		String _classpath = System.getProperty("java.class.path");
//		_debug.outln("Classpath: "+_classpath);
		_optionList.add("-s");


  		_optionList.add("-verbose");
      	_optionList.add("-g");
//  		_optionList.add("-XDuseJavaUtilZip");
  		_optionList.addAll(Arrays.asList("-classpath", _classpath));
		_optionList.addAll(Arrays.asList("-d", _buildPath));
		


		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		_debug.outln("Javasource: "+_javaPath);	
		ArrayList<File> _files =  new ArrayList<File>(Arrays.asList(new File(_javaPath).listFiles()));
//		String _javaPath = _taskPath+"/"+_javaSourceName;
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(_files);
		JavaCompiler.CompilationTask task = compiler.getTask(null ,fileManager ,null , _optionList, null, compilationUnits );
		boolean compiled = task.call();
		return compiled;
	}

//-------------------------------------------------------------------------------------
public static boolean createJar0(Debug _debug, String _path, String _javaSourceName, String _jarName) throws IOException {
    FileOutputStream fos = new FileOutputStream(_path + "/" + _jarName);
	Manifest manifest = new Manifest();
    JarOutputStream jos = new JarOutputStream(fos, manifest);
    BufferedOutputStream bos = new BufferedOutputStream(jos);
    jos.setComment("");
    File f = new File(_javaSourceName);
        _debug.outln(Debug.IMPORTANT1, "Writing file: " + f.toString());
        BufferedReader br = new BufferedReader(new FileReader(f));
        jos.putNextEntry(new JarEntry(f.getName()));
        int c;
        while ((c = br.read()) != -1) {
            bos.write(c);
        }
        br.close();
        bos.flush();
    bos.close();
//  JarOutputStream jor = new JarOutputStream(new FileOutputStream(PATH + FILE), manifest);
    return true;
}

//-------------------------------------------------------------------------------------
public static boolean createJar1(Debug _debug, String _path, String _javaSourceName, String _jarName) throws IOException
{
  Manifest manifest = new Manifest();
  manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
  JarOutputStream target = new JarOutputStream(new FileOutputStream(_path + "/" + _jarName), manifest);
//  add(new File(_path + "/build"), target);
  _path = _path + "/build";
  _debug.outln(Debug.IMPORTANT1, "Setting curent directory to: "+_path);
  System.setProperty("user.dir", _path);
  add(_debug, _path, new File(_path), target);
  target.close();
  return true;
}

//-------------------------------------------------------------------------------------
private static void add(Debug _debug, String _path, File source, JarOutputStream target) throws IOException
{
  BufferedInputStream in = null;
  try
  {
    if (source.isDirectory())
    {
      String name = source.getPath().replace("\\", "/");
    	name = name.substring(_path.length());
      if (!name.isEmpty())
      {
        if (!name.endsWith("/"))
          name += "/";
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        target.closeEntry();
      }
      	for (File nestedFile: source.listFiles()){
//	      	String _relativePath = nestedFile.getPath();
//	      	Path _relative = (nestedFile.toPath()).relativize(Paths.get(_path));
	      	File _relativePathFile = new File(nestedFile.getPath());
	        add(_debug, _path, _relativePathFile, target);
    	}
      return;
    }

    String _relativePath = source.getPath();
    _relativePath = "./"+_relativePath.substring(_path.length());
	_debug.outln("Adding to Jar: "+_relativePath);
    File _relativeFile = new File(_relativePath);
    JarEntry entry = new JarEntry(_relativeFile.getPath().replace("\\", "/"));
    entry.setTime(_relativeFile.lastModified());
    target.putNextEntry(entry);
    in = new BufferedInputStream(new FileInputStream(source));

    byte[] buffer = new byte[1024];
    while (true)
    {
      int count = in.read(buffer);
      if (count == -1)
        break;
      target.write(buffer, 0, count);
    }
    target.closeEntry();
  }catch(IOException _e){
  	_debug.outln(Debug.ERROR, "IOException adding file to jar: "+_e.getMessage());
  }finally {
    if (in != null)
      in.close();
  }
}

//-------------------------------------------------------------------------------------
public static int BUFFER_SIZE = 10240;
  protected static boolean createJar(Debug _debug, String _path, File archiveFile, File[] tobeJared) {
    try {
      byte buffer[] = new byte[BUFFER_SIZE];
      // Open archive file
      FileOutputStream stream = new FileOutputStream(archiveFile);
      JarOutputStream out = new JarOutputStream(stream, new Manifest());

      for (int i = 0; i < tobeJared.length; i++) {
      	String _classPath = tobeJared[i].getPath();
//      	_debug.outln(Debug.IMPORTANT1, "Check path... " + _classPath);
/*        if (tobeJared[i] == null || !tobeJared[i].exists()
            || tobeJared[i].isDirectory())
          continue; // Just in case...*/
        _debug.outln(Debug.IMPORTANT1, "Adding " + _classPath);

        // Add archive entry
        JarEntry jarAdd = new JarEntry(_classPath.replace(_path, ""));
        jarAdd.setTime(tobeJared[i].lastModified());
        out.putNextEntry(jarAdd);

        // Write file to archive
        File _file = new File(_classPath);
        FileInputStream in = new FileInputStream(_file);
        while (true) {
          int nRead = in.read(buffer, 0, buffer.length);
          if (nRead <= 0)
            break;
          out.write(buffer, 0, nRead);
        }
        in.close();
      }

      out.close();
      stream.close();
      _debug.outln(Debug.IMPORTANT1, "Adding completed OK");
    } catch (Exception ex) {
      ex.printStackTrace();
      _debug.outln(Debug.ERROR, "Error: " + ex.getMessage());
    }
    return true;
  }
//-------------------------------------------------------------------------------------

}
//-------------------------------------------------------------------------------------

/*

        String path = "/generated/Buffer.java";
        File file = new File(System.getProperty("user.dir") + path);
        File[] files1 = new File[]{file};

        List<String> optionList = new ArrayList<String>();
        // set compiler's classpath to the path of your framework-core.jar
        optionList.addAll(Arrays.asList("classpath","/lib/framework-core.jar"));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
        

        compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();

        fileManager.close();

*/