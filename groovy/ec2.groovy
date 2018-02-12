import com.amazonaws.services.ec2.model.InstanceType
import com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.Domain
import hudson.model.*
import hudson.plugins.ec2.AmazonEC2Cloud
import hudson.plugins.ec2.AMITypeData
import hudson.plugins.ec2.EC2Tag
import hudson.plugins.ec2.SlaveTemplate
import hudson.plugins.ec2.SpotConfiguration
import hudson.plugins.ec2.UnixData
import jenkins.model.Jenkins
 
def env_secgr = System.getenv('SECURITYGROUP')
// parameters
def SlaveTemplateUsEast1Parameters = [
  ami:                      'ami-866878fc',
  associatePublicIp:        false,
  connectBySSHProcess:      false,
  connectUsingPublicIp:     false,
  customDeviceMapping:      '',
  deleteRootOnTermination:  true,
  description:              'Jenkins slave EC2 US East 1',
  ebsOptimized:             false,
  iamInstanceProfile:       '',
  idleTerminationMinutes:   '5',
  initScript:               '',
  instanceCapStr:           '1',
  jvmopts:                  '',
  labelString:              'us-east1-jenkins-slave',
  launchTimeoutStr:         '',
  numExecutors:             '20',
  remoteAdmin:              'ec2-user',
  remoteFS:                 '',
  securityGroups:           "$env_secgr",
  stopOnTerminate:          false,
  subnetId:                 'subnet-3c414130',
  tags:                     new EC2Tag('Name', 'jenkins-slave'),
  tmpDir:                   '',
  type:                     't2.small',
  useDedicatedTenancy:      false,
  useEphemeralDevices:      true,
  usePrivateDnsName:        true,
  userData:                 '',
  zone:                     'us-east-1f'
]
 
def AmazonEC2CloudParameters = [
  cloudName:      'AWSCloud',
  credentialsId:  '',
  instanceCapStr: '1',
  privateKey:     '''-----BEGIN RSA PRIVATE KEY-----
MIIEogIBAAKCAQEAs6crJguE5UBaakNiEXXw+Jur6jawN3I9VTVdobcG2+3SVOxkGd/DbMxJn7iz
ZzV6/HzEhcYT1CNGNO1VtWZ1Hdq8l/VkaFGgP7y7ob0VwvCYIW7snpVb+7JmrMT1cn60IYQOCKjT
sl4UfEapAA0CHptMNwarzb57kn0TAUYKw1fVvr4Gm2p36Yg126npm7Rt6egDmmZXRsvqhstOnzrc
wQt/v+B0AwdFY/QyVOslgwW6DzHJqMy94XoS+tacdyNpLAupsrcsnWZ6KWFEyb4P4fq5g+PSxYaT
Qs1LFDhMicDAKeaQRWUnvJLMJ5JwVGFM2Tikplfnxjj+ba5AwRRagwIDAQABAoIBAHZXoWnPXryx
dp3XQ2ujGJh723ROkCgGHfbaT5JugZswf0Qn8AzHRPGPZNRNFAjhxuE5wy8n+vncPqwwxyvWkUGY
THGBWaIKWxiBV8xERWiDmoMzkbUpSE12CIK4v8KrSjdlyOTVaZZuuOnglgUNjJ7iyALbZyMB3l7x
+yn0nLRuNa1vBq77QrFSWFGRpbUCuN7AFjhdhHfTL/l/1ry/hRNChoPQVVk0+IMUL+duoAbl2kLA
aQZzvZefT/3JrWjbOsfjfPBVjkoNFDo3todKOZtasYZnBQ2B0oztNCLGhU+dtpu9LwFoHcJxseBn
V954+V2Mfi6auEMqf/lrj7qrNeECgYEA5D6NYHMlv/9qabbgZgv6Ju8fkdvWLTnULKBTa67rtX9k
MtITptCkbaJHl7alGORg9M7NEzETsvWO1nmLbDmTuQ8/P9uVnDwaG2Qb1mIPtaNOsa0nkJCpDYSJ
24TWHKFGcoeq8pmQfNaixzhfAYsqgavMCJPYacpqaM0pSwuxQBMCgYEAyX/syVhm3WBPom7pRFU2
91OJQWIhGqGAiLkERQojalfBs6gWw6oMuvj0YTH/D0XwWidiDTMFMz+q4yWN2GjSdHT5f+ID9tDL
0e14bI4LCBmt8NYM2u8eVgJK8teoxrd5DPuy2BhXZDFN0pO4DC29Hm4jXvhM3op85KxUHUD0KdEC
gYB0KGV45YEbwlgcruZq6MOJVv7hE1lfsOOLRpjN37qzpr6Oop0/s0VLSmBXFKvXDDtPu4NgtgRJ
enEUM81B6deFQf41eIIeffPnZr5ID6XhEdh10iKljwmKCJ7EKklxTp50oRjd2EtRTUaQTt8DARlD
3vO6lct4o3weE/Nk7vFpIwKBgDESDkAaXzKaOcp86CHT/sbMeSiLFTS/D+Y76GZHycgkBvARhzmv
N9pwtlLVMiAuEBJkdORyAFoyrxMSGjERle72baxPBlgdEQXWpvUIwnb+ifWUqxzGDsUzWb0598gs
9jXLNgqaIu4rc6BMiQ2uNdgrSkHehhnL8A5gSTu2jb+BAoGAPEgqgf7JwktPq324MEjwLErqTf5d
mYoWDO8q7m5Ff2LrJgDdyRkhvNfBl+meT8//9XXQVYyanFNunhY1FrhPjVDB/hUVaLcjkwJTl1Rk
cdAVSTc/Jvj3rmvJGl4JCIeBP+cR8DHN1wzpGKEbEyCT5+e8s/0MpDbNe4qi6ddyE/c=
-----END RSA PRIVATE KEY-----''',
  region: 'us-east-1',
  useInstanceProfileForCredentials: false
]
  
// https://github.com/jenkinsci/ec2-plugin/blob/ec2-1.38/src/main/java/hudson/plugins/ec2/SlaveTemplate.java
SlaveTemplate slaveTemplateUsEast1 = new SlaveTemplate(
  SlaveTemplateUsEast1Parameters.ami,
  SlaveTemplateUsEast1Parameters.zone,
  null,
  SlaveTemplateUsEast1Parameters.securityGroups,
  SlaveTemplateUsEast1Parameters.remoteFS,
  InstanceType.fromValue(SlaveTemplateUsEast1Parameters.type),
  SlaveTemplateUsEast1Parameters.ebsOptimized,
  SlaveTemplateUsEast1Parameters.labelString,
  Node.Mode.NORMAL,
  SlaveTemplateUsEast1Parameters.description,
  SlaveTemplateUsEast1Parameters.initScript,
  SlaveTemplateUsEast1Parameters.tmpDir,
  SlaveTemplateUsEast1Parameters.userData,
  SlaveTemplateUsEast1Parameters.numExecutors,
  SlaveTemplateUsEast1Parameters.remoteAdmin,
  new UnixData(null, null, null),
  SlaveTemplateUsEast1Parameters.jvmopts,
  SlaveTemplateUsEast1Parameters.stopOnTerminate,
  SlaveTemplateUsEast1Parameters.subnetId,
  [SlaveTemplateUsEast1Parameters.tags],
  SlaveTemplateUsEast1Parameters.idleTerminationMinutes,
  SlaveTemplateUsEast1Parameters.usePrivateDnsName,
  SlaveTemplateUsEast1Parameters.instanceCapStr,
  SlaveTemplateUsEast1Parameters.iamInstanceProfile,
  SlaveTemplateUsEast1Parameters.deleteRootOnTermination,
  SlaveTemplateUsEast1Parameters.useEphemeralDevices,
  SlaveTemplateUsEast1Parameters.useDedicatedTenancy,
  SlaveTemplateUsEast1Parameters.launchTimeoutStr,
  SlaveTemplateUsEast1Parameters.associatePublicIp,
  SlaveTemplateUsEast1Parameters.customDeviceMapping,
  SlaveTemplateUsEast1Parameters.connectBySSHProcess,
  SlaveTemplateUsEast1Parameters.connectUsingPublicIp
)
 
// https://github.com/jenkinsci/ec2-plugin/blob/ec2-1.38/src/main/java/hudson/plugins/ec2/AmazonEC2Cloud.java
AmazonEC2Cloud amazonEC2Cloud = new AmazonEC2Cloud(
  AmazonEC2CloudParameters.cloudName,
  AmazonEC2CloudParameters.useInstanceProfileForCredentials,
  AmazonEC2CloudParameters.credentialsId,
  AmazonEC2CloudParameters.region,
  AmazonEC2CloudParameters.privateKey,
  AmazonEC2CloudParameters.instanceCapStr,
  [slaveTemplateUsEast1]
)
 
// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()
  
// add cloud configuration to Jenkins
jenkins.clouds.add(amazonEC2Cloud)
 
// save current Jenkins state to disk
jenkins.save()
