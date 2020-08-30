import com.citi.ocp4.jfrog.DownloadEventProcessor
import org.artifactory.request.Request;
import org.artifactory.repo.RepoPath;

download {
    afterRemoteDownload { Request request, RepoPath repoPath ->
          DownloadEventProcessor.processRemoteDownload(request, repoPath);
    }
}
