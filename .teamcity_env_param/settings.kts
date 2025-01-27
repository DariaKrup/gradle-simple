import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesExecutor
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

version = "2024.12"

project {

    buildType(Build)

    params {
        param("param", "%env.JDK_1_8%")
    }

    features {
        awsConnection {
            id = "GradleSimple_AmazonWebServicesAwsEc2"
            name = "Amazon Web Services (AWS) (EC2)"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVI62P5XDY"
                secretAccessKey = "credentialsJSON:25c02f83-4d94-46f7-a14a-b76f5459451e"
            }
            allowInSubProjects = true
            allowInBuilds = true
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_10"
            profileId = "amazon-2"
            agentPoolId = "-2"
            name = "Ubuntu"
            vpcSubnetId = "subnet-043178c302cabfe37,subnet-0ace2a91ee63119ea"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        kubernetesConnection {
            id = "PROJECT_EXT_4"
            name = "Kubernetes Connection"
            apiServerUrl = "https://A51B42A65F7E54005C95A4D353916627.gr7.eu-west-1.eks.amazonaws.com"
            authStrategy = eks {
                accessId = "AKIA5JH2VERVI62P5XDY"
                secretKey = "credentialsJSON:25c02f83-4d94-46f7-a14a-b76f5459451e"
                clusterName = "tc-dkrupkina-eks-cluster"
            }
        }
        kubernetesExecutor {
            id = "PROJECT_EXT_5"
            connectionId = "PROJECT_EXT_4"
            profileName = "K8s"
            enabled = false
            templateName = "ubuntu-agent"
        }
        amazonEC2CloudProfile {
            id = "amazon-2"
            name = "EC2 AWS"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "GradleSimple_AmazonWebServicesAwsEc2"
        }
        amazonEC2CloudProfile {
            id = "amazon-3"
            name = "EC2 AWS: other region"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.US_EAST_N_VIRGINIA
            awsConnectionId = "GradleSimple_AmazonWebServicesAwsEc2"
        }
        amazonEC2CloudProfile {
            id = "amazon-4"
            name = "AWS EC2: non-default"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.US_WEST_N_CALIFORNIA
            awsConnectionId = "AmazonWebServicesAws"
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Detach"
            id = "Detach"
            scriptContent = """echo "##teamcity[buildDetachedFromAgent]""""
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
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
