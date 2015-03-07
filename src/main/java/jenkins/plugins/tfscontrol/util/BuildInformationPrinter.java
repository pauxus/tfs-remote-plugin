package jenkins.plugins.tfscontrol.util;

import com.microsoft.tfs.core.clients.build.IBuildInformation;
import com.microsoft.tfs.core.clients.build.IBuildInformationNode;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.apache.commons.lang.StringUtils.leftPad;

/**
 * This class takes a IBuildInformation objects and prints out its test elements in various formats.
 * 
 */
public class BuildInformationPrinter {
    
    private BuildInformationPrinter() {
        // util class only
    }
    
    public static String toString(IBuildInformation information) {
        StringWriter writer = new StringWriter();
        
        printInformationToWriter(information, writer);
        
        return writer.toString();
    }
    
    public static void printInformationToWriter(IBuildInformation information, Writer writer) {
        PrintWriter out = writer instanceof PrintWriter ? (PrintWriter) writer : new PrintWriter(writer);
        
        doPrintInformationToWriter(information, out, 0);
    }

    public static void printInformationToStream(IBuildInformation information, PrintStream stream) {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(stream));

        doPrintInformationToWriter(information, out, 0);
    }

    private static void doPrintInformationToWriter(IBuildInformation information, PrintWriter out, int depth) {
        for (IBuildInformationNode node : information.getNodes()) {
            String text = node.getFields().get("DisplayText");
            if (text != null)
                out.println(leftPad(text, depth + text.length()));
            doPrintInformationToWriter(node.getChildren(), out, depth + 2);
        }
    }

}
