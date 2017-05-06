package afs;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Created by ovchi on 03.05.2017.
 */
public class Console {

    private Path currentDirectory;
    private LinkedList<String> historyCommands = new LinkedList<>();


    /**
     * Initialize the current directory.
     */
    public Console() {
        this.currentDirectory = Paths.get(System.getProperty("user.home"));
    }


    /**
     * The method in which reads command from the standard input.
     */
    public void run() {
        String command = "";
        while (!command.equals("exit")) {
            System.out.print(currentDirectory + ">");
            Scanner in = new Scanner(System.in);
            command = in.nextLine();
            if (!command.equals("")) {
                historyCommands.add(command);
                command = command.replaceAll("[ ]+", " ");
                runCommand(command);
            }
        }
    }

    /**
     *Method to launch the utility.
     * @param command The command to launch the utility/
     */
    private void runCommand(String command) {
        String[] buf = split(command);
        String tool = buf[0];
        String[] args = new String[buf.length - 1];
        System.arraycopy(buf, 1, args, 0, args.length);
        switch (tool) {
            case "ls":ls();break;
            case "mkdir":mkdir(args);break;
            case "cd":cd(args);break;
            case "mkf":mkf(args);break;
            case "adif":adif(args);break;
            case "cat":cat(args);break;
            case "exit":System.out.println("Bye."); break;
            default:
                System.out.println("\"" + tool + "\" not an internal or external command,"+System.lineSeparator() +
                        "operable program or batch file.");
        }
    }


    /**
     * Method for adding content to the file
     * @param args The name of the file
     */
    private void adif(String[] args) {
        if (args.length == 0) {
            System.out.println("Error in the command syntax.");
        } else {
            Path nBuf = null;
            try {
                if (Paths.get(args[0]).isAbsolute()) {
                    nBuf = Paths.get(args[0]).normalize();
                } else {
                    nBuf = Paths.get(currentDirectory.toString(), args[0]).normalize();
                }
                File forRead = new File(nBuf.toString());
                try (BufferedWriter bis = new BufferedWriter(new FileWriter(forRead,true))) {
                    String str="";
                    do  {
                        bis.append(str);
                        Scanner in = new Scanner(System.in);
                        str=in.nextLine();
                        bis.newLine();
                    }while(!str.equals("exit"));
                }catch (FileNotFoundException e) {
                    System.out.println("The system cannot find the file/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InvalidPathException e) {
                System.out.println("Syntax error in file name, directory name, or volume label.");
            }
        }
    }

    /**
     * Method to output contents of a file
     * @param args The name of the file
     */
    private void cat(String[] args) {
        if (args.length == 0) {
            System.out.println("Error in the command syntax.");
        } else {
            Path nBuf = null;
            try {
                if (Paths.get(args[0]).isAbsolute()) {
                    nBuf = Paths.get(args[0]).normalize();
                } else {
                    nBuf = Paths.get(currentDirectory.toString(), args[0]).normalize();
                }
                File forRead = new File(nBuf.toString());
                try (BufferedReader bis = new BufferedReader(new InputStreamReader(new FileInputStream(forRead)));) {
                    String str;
                    while ((str = bis.readLine()) != null) {
                        System.out.println(str);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("The system cannot find the file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InvalidPathException e) {
                System.out.println("Syntax error in file name, directory name, or volume label.");
            }
        }
    }

    /**
     * Method for creating files
     * @param args file names
     */
    private void mkf(String[] args) {
        if (args.length == 0) {
            System.out.println("Error in the command syntax.");
        } else {
            Path nBuf = null;
            for (String path : args) {
                try {
                    if (Paths.get(path).isAbsolute()) {
                        nBuf = Paths.get(path).normalize();
                    } else {
                        nBuf = Paths.get(currentDirectory.toString(), path).normalize();
                    }
                    if (!Files.exists(nBuf.getParent())) {
                        System.out.println("The system cannot find the specified directory.");
                    } else if (!new File(nBuf.toString()).createNewFile()) {
                        System.out.println("Subfolder or file " + nBuf.toString() + " already exists.");
                    }
                } catch (InvalidPathException e) {
                    System.out.println("Syntax error in file name, directory name, or volume label.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method for creating directories
     * @param args the names of directories
     */
    private void mkdir(String[] args) {
        if (args.length == 0) {
            System.out.println("Error in the command syntax.");
        } else {
            Path nBuf = null;
            for (String path : args) {
                try {
                    if (Paths.get(path).isAbsolute()) {
                        nBuf = Paths.get(path).normalize();
                    } else {
                        nBuf = Paths.get(currentDirectory.toString(), path).normalize();
                    }
                    if (!Files.exists(nBuf.getRoot())) {
                        System.out.println("The system cannot find the drive specified.");
                    } else if (!new File(nBuf.toString()).mkdirs()) {
                        System.out.println("Subfolder or file " + nBuf.toString() + " already exists.");
                    }
                } catch (InvalidPathException e) {
                    System.out.println("Syntax error in file name, directory name, or volume label.");
                }
            }
        }
    }

    /**
     * Method to output contents of a directory
     */
    private void ls() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDirectory)) {
            for (Path entry : stream) {
                if (new File(entry.toString()).isDirectory()) {
                    System.out.println("<ls> " + entry.getFileName());
                } else {
                    System.out.println("      " + entry.getFileName());
                }
            }
        } catch (IOException ignor) {
        }
    }

    /**
     * Method for navigation through the directories
     * @param args The path to the new directory
     */
    private void cd(String[] args) {
        if (args.length == 0) {
            System.out.println(currentDirectory.toString());
        } else {
            Path nBuf = null;
            try {
                if (Paths.get(args[0]).isAbsolute()) {
                    nBuf = Paths.get(args[0]).normalize();
                } else {
                    nBuf = Paths.get(currentDirectory.toString(), args[0]).normalize();
                }
                if (new File(nBuf.toString()).isDirectory()) {
                    currentDirectory = nBuf;
                } else {
                    System.out.println("The system cannot find the path specified.");
                }
            } catch (InvalidPathException e) {
                System.out.println("Syntax error in file name, directory name, or volume label.");
            }
        }
    }

    /**
     * Helper method to split strings
     * @param args the string is designed to separate
     * @return
     */
    public String[] split(String args) {
        List<String> result = new ArrayList<>();
        String buf = "";
        args = args.replaceAll("[ ]+", " ");
        args = args.replaceAll("[\"]+", "\"");
        int i;
        for (i = 0; i < args.length(); i++) {
            if (args.charAt(i) == '"') {
                for (i = i + 1; i < args.length(); i++) {
                    if (args.charAt(i) == '"') {
                        break;
                    }
                    buf += args.charAt(i);
                }
            } else if (args.charAt(i) == ' ') {
                if (!buf.equals("")) {
                    result.add(buf);
                    buf = "";
                }
            } else {
                buf += args.charAt(i);
            }
        }
        if (!buf.equals(""))
            result.add(buf);
        return result.toArray(new String[0]);
    }
}
