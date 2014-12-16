package at.movielist.bl;

import at.movielist.beans.Movie;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import at.lib.mediainfo.MediaInfo;
import at.movielist.ui.MainUI;
import at.movielist.ui.ProgressbarDLG;

public class MovieLoaderWorker extends SwingWorker<LinkedList<Movie>, Movie> {

    private String path;
    private LinkedList<Movie> liste = new LinkedList<Movie>();
    private JProgressBar loading;
    private JLabel lb;
    private ProgressbarDLG dlg;
    private MainUI mui;
    private UtilityClass uc = new UtilityClass();
    private SimpleDateFormat durationFormat = new SimpleDateFormat("k'h' mm'mn'");

    private static final String[] okFileExtensions
            = new String[]{".mkv", ".avi", ".mp4", ".ogg", ".flv", ".3gp", ".iso", ".img", ".vob", ".ts", ".mpg", ".m2ts"};
    private static final String[] filesToIgnore
            = new String[]{"ds_store", ".nfo", ".mp3"};

    public MovieLoaderWorker(String path, ProgressbarDLG d, MainUI mui) {
        this.path = path;
        this.loading = d.getProgBar();
        this.lb = d.getLabel();
        this.dlg = d;
        this.mui = mui;
    }

    public LinkedList<Movie> getListe() {
        return liste;
    }

    @Override
    protected LinkedList<Movie> doInBackground() throws Exception {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        MediaInfo mi = new MediaInfo();

        dlg.setMovieWorker(this);

        for (int i = 0; i < listOfFiles.length; i++) {
            String xfy = "Loading Movie " + (i + 1) + " from " + listOfFiles.length;
            double inc = 1000000 / listOfFiles.length;
            lb.setText(xfy);
            if (listOfFiles[i].isDirectory()) {
                File moviefolder = new File(listOfFiles[i].getAbsolutePath());
                File[] listoffilesinmoviefolder = moviefolder.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        for (String extension : okFileExtensions) {
                            if (file.getName().toLowerCase().endsWith(extension) || (file.getName().toLowerCase().equals("video_ts") && file.isDirectory())) {
                                return true;
                            }
                        }
                        return false;
                    }
                });

                if (listoffilesinmoviefolder != null) {
                    if (listoffilesinmoviefolder.length > 1) {

                        double filesize = 0.0;
                        String gibormib = "GiB";

                        File movie = listoffilesinmoviefolder[0];
                        mi.open(movie);
                        String name = listOfFiles[i].getName();
                        String width = mi.get(MediaInfo.StreamKind.Video, 0, "Width", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                        String height = mi.get(MediaInfo.StreamKind.Video, 0, "Height", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                        String dar = mi.get(MediaInfo.StreamKind.Video, 0, "DisplayAspectRatio/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                        String extension = mi.get(MediaInfo.StreamKind.General, 0, "FileExtension", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                        String duration = mi.get(MediaInfo.StreamKind.General, 0, "Duration/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                        mi.close();

                        for (File f : listoffilesinmoviefolder) {
                            if (f.isFile()) {
                                mi.open(f);
                                String[] spl = mi.get(MediaInfo.StreamKind.General, 0, "FileSize/String2", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name).split("\\s+");

                                if (spl[1].equals("MiB")) {
                                    filesize += (Double.parseDouble(spl[0]) / 1024.0);
                                } else {
                                    filesize += Double.parseDouble(spl[0]);
                                }
                            }
                        }

                        if (filesize < 1.0) {
                            gibormib = "MiB";
                            filesize *= 1024.0;
                        }

                        String size = String.format("%4.2f %s", filesize, gibormib);
                        size = size.replaceAll(",", ".");

                        liste.add(new Movie(name, width, height, dar, duration, size, extension, listoffilesinmoviefolder.length));
                        mi.close();
                    } else if (listoffilesinmoviefolder.length == 1) {
                        if (listoffilesinmoviefolder[0].getName().toLowerCase().equals("video_ts") && listoffilesinmoviefolder[0].isDirectory()) {

                            File[] moviesinfolderfolder = listoffilesinmoviefolder[0].listFiles(new FileFilter() {
                                @Override
                                public boolean accept(File file) {
                                    for (String extension : okFileExtensions) {
                                        if (file.getName().toLowerCase().endsWith(extension)) {
                                            return true;
                                        }
                                    }
                                    return false;
                                }
                            });

                            double filesize = 0.0;
                            String gibormib = "GiB";

                            File movie = moviesinfolderfolder[0];
                            mi.open(movie);
                            String name = listOfFiles[i].getName();
                            String width = mi.get(MediaInfo.StreamKind.Video, 0, "Width", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String height = mi.get(MediaInfo.StreamKind.Video, 0, "Height", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String dar = mi.get(MediaInfo.StreamKind.Video, 0, "DisplayAspectRatio/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String extension = mi.get(MediaInfo.StreamKind.General, 0, "FileExtension", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String duration = mi.get(MediaInfo.StreamKind.General, 0, "Duration/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            mi.close();

                            for (File f : listoffilesinmoviefolder) {
                                if (f.isFile()) {
                                    mi.open(f);
                                    String[] spl = mi.get(MediaInfo.StreamKind.General, 0, "FileSize/String2", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name).split("\\s+");

                                    if (spl[1].equals("MiB")) {
                                        filesize += (Double.parseDouble(spl[0]) / 1024.0);
                                    } else {
                                        filesize += Double.parseDouble(spl[0]);
                                    }
                                }
                            }

                            if (filesize < 1.0) {
                                gibormib = "MiB";
                                filesize *= 1024.0;
                            }

                            String size = String.format("%4.2f %s", filesize, gibormib);
                            size = size.replaceAll(",", ".");

                            liste.add(new Movie(name, width, height, dar, duration, size, extension, listoffilesinmoviefolder.length));
                            mi.close();
                        } else {
                            File movie = listoffilesinmoviefolder[0];
                            mi.open(movie);
                            String name = listOfFiles[i].getName();
                            String width = mi.get(MediaInfo.StreamKind.Video, 0, "Width", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String height = mi.get(MediaInfo.StreamKind.Video, 0, "Height", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String dar = mi.get(MediaInfo.StreamKind.Video, 0, "DisplayAspectRatio/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String duration = mi.get(MediaInfo.StreamKind.General, 0, "Duration/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String size = mi.get(MediaInfo.StreamKind.General, 0, "FileSize/String2", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            String extension = mi.get(MediaInfo.StreamKind.General, 0, "FileExtension", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                            liste.add(new Movie(name, width, height, dar, duration, size, extension, listoffilesinmoviefolder.length));
                            mi.close();
                        }

                    }
                }
            } else if (listOfFiles[i].isFile()) {

                boolean isNotIgnored = true;

                for (int j = 0; j < filesToIgnore.length; j++) {
                    if (listOfFiles[i].getName().toLowerCase().endsWith(filesToIgnore[j])) {
                        isNotIgnored = false;
                    }
                }

                if (isNotIgnored == true) {
                    mi.open(listOfFiles[i]);
                    String name = listOfFiles[i].getName();
                    String width = mi.get(MediaInfo.StreamKind.Video, 0, "Width", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                    String height = mi.get(MediaInfo.StreamKind.Video, 0, "Height", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                    String dar = mi.get(MediaInfo.StreamKind.Video, 0, "DisplayAspectRatio/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                    String duration = mi.get(MediaInfo.StreamKind.General, 0, "Duration/String", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                    String size = mi.get(MediaInfo.StreamKind.General, 0, "FileSize/String2", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                    String extension = mi.get(MediaInfo.StreamKind.General, 0, "FileExtension", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                    liste.add(new Movie(name, width, height, dar, duration, size, extension, 1));
                    mi.close();
                }

            }

            loading.setValue(loading.getValue() + (int) inc);
        }

        return liste;
    }

    @Override
    protected void done() {
        mui.setList(liste);

        if (liste.size() > 0) {
            String things = uc.getSizeAndNumberOfFiles(liste);
            mui.getLbThings().setText(things);
        }

        dlg.dispose();
    }
}