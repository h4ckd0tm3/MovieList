package beans;

import java.io.Serializable;

public class Movie implements Serializable {

    private String name, width, height, aspectratio, duration, filesize, fileextension;
    private int numberoffiles;

    public Movie(String name, String width, String height, String aspectratio, String duration, String filesize, String fileextension, int numberoffiles) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.aspectratio = aspectratio;
        this.duration = duration;
        this.filesize = filesize;
        this.fileextension = fileextension;
        this.numberoffiles = numberoffiles;
    }

    public String getName() {
        return name;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getAspectratio() {
        return aspectratio;
    }

    public String getDuration() {
        return duration;
    }

    public String getFilesize() {
        return filesize;
    }

    public String getFileextension() {
        return fileextension;
    }

    public int getNumberoffiles() {
        return numberoffiles;
    }

    public String toHTMLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<br><center><h1>").append(name).append("</h1><hr noshade width='80%' ><table width='100%' style='font-size:12px' >");
        sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Duration:</strong></td><td>").append(duration).append("</td></tr>");

        if (width.equals("") && height.equals("")) {
            sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Resolution:</strong></td><td width='50%' >DVD</td></tr>");
        } else {
            if (Integer.parseInt(width) >= 1280) {
                sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Resolution:</strong></td><td width='50%' >").append(width).append("x").append(height).append("  HD</td></tr>");
            } else {
                sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Resolution:</strong></td><td width='50%' >").append(width).append("x").append(height).append("  SD</td></tr>");
            }

        }

        sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Display Aspect Ratio:</strong></td><td width='50%' >").append(aspectratio).append("</td></tr>");
        sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Fileextension:</strong></td><td width='50%' >").append(fileextension).append("</td></tr>");
        sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Filesize:</strong></td><td width='50%' >").append(filesize).append("</td></tr>");
        sb.append("<tr><td width='50%' ><strong style='color:#00bda5'>Number of Files:</strong></td><td width='50%' >").append(numberoffiles).append("</td></tr>");
        sb.append("</table></center></body>");

        return sb.toString();
    }
}
