import com.citi.ocp4.jfrog.DownloadEventProcessor
import org.artifactory.request.Request;
import org.artifactory.repo.RepoPath;

download {
    beforeDownload { Request request, RepoPath repoPath ->
//    afterRemoteDownload { Request request, RepoPath repoPath ->
          DownloadEventProcessor.printInput(request, repoPath);
    }
}


