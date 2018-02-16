import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

def env_user = System.getenv('MASTERUSER')
def env_passwd = System.getenv('MASTERPWD')

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
 hudsonRealm.createAccount("$env_user","$env_passwd")
instance.setSecurityRealm(hudsonRealm)
instance.save()
