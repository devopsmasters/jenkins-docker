import java.lang.System
import hudson.model.*
import jenkins.model.*
import org.jfrog.*
import org.jfrog.hudson.*
import com.cloudbees.plugins.credentials.CredentialsProvider
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials

    def env_arturl = System.getenv('ARTIFACTORY_URL')
    def inst = Jenkins.getInstance()
    def desc = inst.getDescriptor("org.jfrog.hudson.ArtifactoryBuilder")
    CredentialsConfig deployerCredentials = new CredentialsConfig('admin', 'password', 'artifactory-user',false)
    List<ArtifactoryServer> servers =  desc.getArtifactoryServers()
ArtifactoryServer server = new ArtifactoryServer('local-artifactory', "$env_arturl", deployerCredentials, null, 300, false, 3)
    if (servers == null || servers.empty) {
      servers = [server]
    } else {
      servers.push(server)
    }
    desc.setArtifactoryServers(servers)
    desc.save()
