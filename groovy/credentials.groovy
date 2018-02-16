import java.lang.System
import jenkins.*
import hudson.model.*
import jenkins.model.*
// Plugins for SSH credentials
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*

global_domain = Domain.global()
credentials_store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

cred1 = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,'artifactory-login','artifactory-login-creds','admin','password')
credentials_store.addCredentials(global_domain, cred1)

cred2 = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,'docker-hub-creds','docker-hub-creds','test-hub-user','testpassword')
credentials_store.addCredentials(global_domain, cred2)
