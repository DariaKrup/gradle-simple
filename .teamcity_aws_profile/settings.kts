import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.07"

project {

    buildType(Build)

    features {
        awsConnection {
            id = "AwsEc2GradleSimple_AmazonWebServicesAws"
            name = "Amazon Web Services (AWS)"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVI62P5XDY"
                secretAccessKey = "credentialsJSON:081b195d-2f89-4789-ab0f-2b728858b0de"
                stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
            }
            allowInBuilds = false
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_2"
            profileId = "amazon-1"
            agentPoolId = "-2"
            name = "Ubuntu: old"
            vpcSubnetId = "subnet-043178c302cabfe37"
            iamProfile = "dkrupkinaEc2Role"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_3"
            profileId = "amazon-2"
            agentPoolId = "-2"
            name = "Ubuntu"
            vpcSubnetId = "subnet-043178c302cabfe37"
            iamProfile = "dkrupkinaEc2Role"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_4"
            profileId = "amazon-3"
            agentPoolId = "-2"
            name = "Windows"
            vpcSubnetId = "subnet-043178c302cabfe37"
            iamProfile = "dkrupkinaEc2Role"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-02761680ebacdaa95")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_5"
            profileId = "amazon-4"
            agentPoolId = "-2"
            name = "Ubuntu"
            vpcSubnetId = "subnet-043178c302cabfe37"
            iamProfile = "dkrupkinaEc2Role"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_6"
            profileId = "amazon-2"
            agentPoolId = "-2"
            name = "Ubuntu: new"
            vpcSubnetId = "subnet-043178c302cabfe37"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_7"
            profileId = "amazon-4"
            agentPoolId = "-2"
            name = "Ubuntu: new (chain)"
            vpcSubnetId = "subnet-043178c302cabfe37"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_8"
            profileId = "amazon-1"
            agentPoolId = "-2"
            name = "Ubuntu: old (upd connection)"
            vpcSubnetId = "subnet-043178c302cabfe37"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudProfile {
            id = "amazon-1"
            name = "AWS EC2: keys, update"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AwsEc2GradleSimple_AmazonWebServicesAws"
        }
        amazonEC2CloudProfile {
            id = "amazon-2"
            name = "AWS EC2: keys, no update"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            authType = accessKey {
                keyId = "credentialsJSON:4c28e359-a90a-4576-845b-ab97eed8c1f3"
                secretKey = "credentialsJSON:081b195d-2f89-4789-ab0f-2b728858b0de"
            }
        }
        amazonEC2CloudProfile {
            id = "amazon-3"
            name = "AWS EC2: chain, update"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AwsEc2GradleSimple_AmazonWebServicesAws"
            authType = instanceIAMRole()
        }
        amazonEC2CloudProfile {
            id = "amazon-4"
            name = "AWS EC2: chain, no update"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            authType = instanceIAMRole()
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "clean build"
            gradleWrapperPath = ""
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})
