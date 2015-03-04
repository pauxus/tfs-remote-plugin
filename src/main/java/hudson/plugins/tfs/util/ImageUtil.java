package hudson.plugins.tfs.util;

import com.microsoft.tfs.core.clients.build.flags.BuildStatus;
import hudson.model.Result;
import ms.tfs.build.buildservice._04._BuildStatus;

/**
 * Created by snpaux on 04.03.2015.
 */
public class ImageUtil {
    
    private ImageUtil() {
        // utility class
    }

    private final static int IN_PROGRESS = 1;
    private final static int SUCCEEDED = 2;
    private final static int PARTIALLY_SUCCEEDED = 4;
    private final static int FAILED = 8;
    private final static int STOPPED = 16;
    private final static int NOT_STARTED = 32;

    public static Result tfsStatusToResult(BuildStatus status) {

        switch (status.toIntFlags()) {
            case IN_PROGRESS: return Result.NOT_BUILT;
            case SUCCEEDED: return Result.SUCCESS;
            case PARTIALLY_SUCCEEDED: return Result.UNSTABLE;
            case FAILED: return Result.FAILURE;
            case STOPPED: return Result.ABORTED;
            case NOT_STARTED: return Result.NOT_BUILT;
        }
        
        return Result.NOT_BUILT;
    }
    
    public static String getImageForStatus(BuildStatus status) {
        return tfsStatusToResult(status).color.getImage();
    }
}
